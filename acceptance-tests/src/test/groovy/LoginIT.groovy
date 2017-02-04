import groovy.json.JsonBuilder
import io.restassured.RestAssured
import org.junit.Before
import org.junit.Test

import static io.restassured.RestAssured.given
import static io.restassured.RestAssured.post
import static io.restassured.http.ContentType.JSON
import static org.hamcrest.Matchers.notNullValue
import static org.hamcrest.Matchers.nullValue
import static org.hamcrest.core.Is.is
import static org.hamcrest.core.IsCollectionContaining.hasItem

class LoginIT {

    @Before
    void setup() {
        RestAssured.port = 4567
        post('/setup-for-tests')
    }

    @Test
    void 'Login with wrong json'() {
        def body = "d ka sp p"

        given().with().body(body)
                .when().post("/login")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(400)
                .body("errors.size()", is(1))
                .body("errors[0]", is("Invalid json"))
    }

    @Test
    void 'Login with empty data'() {
        def body = ''

        given().with().body(body)
                .when().post("/login")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(400)
                .body("errors.size()", is(1))
                .body("errors[0]", is("Incorrect Username or Password"))
    }

    @Test
    void 'Login without password'() {
        def body = [username: 'mosampaio']

        given().with().body(toJSON(body))
                .when().post("/login")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(400)
                .body("errors.size()", is(1))
                .body("errors[0]", is("Incorrect Username or Password"))
    }

    @Test
    void 'Sign up with wrong json'() {
        def body = "d ka sp p"

        given().with().body(body)
                .when().post("/sign-up")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(400)
                .body("errors.size()", is(1))
                .body("errors[0]", is("Invalid json"))
    }

    @Test
    void 'Sign up with empty data'() {
        def body = ''

        given().with().body(body)
                .when().post("/sign-up")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(400)
                .body("errors.size()", is(3))
                .body("errors", hasItem("Password1 cannot be empty"))
                .body("errors", hasItem("Password2 cannot be empty"))
                .body("errors", hasItem("Username cannot be empty"))
    }

    @Test
    void 'Sign up with passwords not matching'() {
        def body = [username: 'mosampaio', password1: '#bOpdSas*u7&sw1D', password2: 'A9()lh$fdgBhm23l']

        given().with().body(toJSON(body))
                .when().post("/sign-up")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(400)
                .body("errors.size()", is(1))
                .body("errors", hasItem("Passwords do not match."))
    }

    @Test
    void 'Sign up with valid data'() {
        def password = '#bOpdSas*u7&sw1D'
        def body = [username: 'mosampaio', password1:  password, password2: password]

        given().with().body(toJSON(body))
                .when().post("/sign-up")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(200)
                .body("errors", is(nullValue()))
                .body("username", is(body.username))
                .cookie("JSESSIONID", is(notNullValue()))
    }

    @Test
    void 'Should log off after sign up'() {
        def password = '#bOpdSas*u7&sw1D'
        def body = [username: 'mosampaio', password1:  password, password2: password]

        def sessionId = given().with().body(toJSON(body)).when().post("/sign-up").sessionId

        given().with().sessionId(sessionId).when().post("/logoff")
                .then().log().ifError().assertThat()
                .contentType(JSON)
                .statusCode(200)
                .body("errors", is(nullValue()))
    }

    @Test
    void 'Should login after log off'() {
        def password = '#bOpdSas*u7&sw1D'
        def body = [username: 'mosampaio', password1:  password, password2: password]

        def sessionId = given().with().body(toJSON(body)).when().post("/sign-up").sessionId
        given().with().sessionId(sessionId).when().post("/logoff")

        def login = [username: body.username, password: body.password1]
        given().with().body(toJSON(login))
                .when().post("/login")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(200)
                .body("errors", is(nullValue()))
                .body("username", is(login.username))
                .cookie("JSESSIONID", is(notNullValue()))
    }

    @Test
    void 'Access secured endpoint without login'() {
        given().when().post("/secured")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(401)
                .body("errors.size()", is(1))
                .body("errors", hasItem("You are not authenticated"))
    }

    @Test
    void 'Access secured endpoint after sign up'() {
        def password = '#bOpdSas*u7&sw1D'
        def body = [username: 'mosampaio', password1:  password, password2: password]

        def sessionId = given().with().sessionId("1234").body(toJSON(body)).when().post("/sign-up").sessionId

        given().with().sessionId(sessionId).when().post("/secured")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(200)
                .body("info", is("very secret info"))
                .body("errors", is(nullValue()))
    }

    @Test
    void 'Access secured endpoint after log in'() {
        def password = '#bOpdSas*u7&sw1D'
        def body = [username: 'mosampaio', password1:  password, password2: password]

        def sessionId = given().with().sessionId("1234").body(toJSON(body)).when().post("/sign-up").sessionId

        given().with().sessionId(sessionId).when().post("/logoff")

        def login = [username: body.username, password: body.password1]
        def sessionId2 = given().with().body(toJSON(login)).when().post("/login").sessionId()
        given().with().sessionId(sessionId2).when().post("/secured")
                .then().log().ifValidationFails().assertThat()
                .contentType(JSON)
                .statusCode(200)
                .body("info", is("very secret info"))
                .body("errors", is(nullValue()))
    }

    static def toJSON(body) {
        new JsonBuilder(body).toPrettyString()
    }
}

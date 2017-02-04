package com.mosampaio.adapters.web;

import com.google.gson.JsonSyntaxException;
import com.mosampaio.domain.model.User;
import com.mosampaio.usecases.LogIn;
import com.mosampaio.usecases.SignUp;
import com.mosampaio.usecases.exception.UseCaseException;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.halt;
import static spark.Spark.post;

public class WebRouter {

    private LogIn logIn;
    private SignUp signUp;

    @Inject
    public WebRouter(LogIn logIn, SignUp signUp) {
        this.logIn = logIn;
        this.signUp = signUp;
    }

    public void register() {
        // filters
        before((req, res) -> res.type("application/json"));

        before("secured", (req, res) -> {
            User user = req.session().attribute("user");
            if (user == null) {
                halt(401, JsonUtil.toJson(ErrorResponse.errorMessage("You are not authenticated")));
            }
        });

        // routes
        post("/secured", (req, res) -> {
            return new SecuredResponse("very secret info");
        }, JsonUtil.json());

        post("/login", (req, res) -> {
            Map<String, String> body = JsonUtil.fromJson(req.body());
            User user = logIn.exec(body.get("username"), body.get("password"));
            req.session().attribute("user", user);

            return new UserResponse(user);
        }, JsonUtil.json());

        post("/sign-up", (req, res) -> {
            Map<String, String> body = JsonUtil.fromJson(req.body());
            User user = signUp.exec(body.get("username"), body.get("password1"), body.get("password2"));
            req.session().attribute("user", user);

            return new UserResponse(user);
        }, JsonUtil.json());

        post("/logoff", (req, res) -> {
            req.session().invalidate();
            return emptyResponse();
        });

        // exception handlers
        exception(UseCaseException.class, (ex, req, res) -> {
            res.body(JsonUtil.toJson(ErrorResponse.errorMessage(ex.getMessage())));
            res.status(400);
        });

        exception(JsonSyntaxException.class, (ex, req, res) -> {
            res.body(JsonUtil.toJson(ErrorResponse.errorMessage("Invalid json")));
            res.status(400);
        });

        exception(ConstraintViolationException.class, (ex, req, res) -> {
            ConstraintViolationException cve = (ConstraintViolationException) ex;
            res.body(JsonUtil.toJson(ErrorResponse.errorMessage(cve.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage).collect(Collectors.toList()))));
            res.status(400);
        });

        exception(Exception.class, (ex, req, res) -> {
            ex.printStackTrace();
            res.status(500);
        });

    }

    private static HashMap<Object, Object> emptyResponse() {
        return new HashMap<>();
    }
}

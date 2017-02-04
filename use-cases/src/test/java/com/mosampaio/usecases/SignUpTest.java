package com.mosampaio.usecases;

import com.mosampaio.usecases.exception.PasswordsDoNotMatchException;
import com.mosampaio.usecases.exception.UsernameAlreadyInUseException;
import helper.UserInMemoryRepository;
import com.mosampaio.domain.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class SignUpTest {

    private SignUp signUp;

    private UserInMemoryRepository userRepository;

    @Before
    public void setUp() {
        userRepository = new UserInMemoryRepository();
        userRepository.save(User.builderForTest(
                UUID.randomUUID().toString(),
                "user",
                "$2a$12$J2dTN4wMH7nbVDgkYq/d8uTsiTw3DQHNF5Py98Mf27PJvnqkE94iK"));

        this.signUp = new SignUp(userRepository);
    }

    @Test(expected = PasswordsDoNotMatchException.class)
    public void shouldThrowExceptionWhenPasswordDoNotMatch() {
        this.signUp.exec("user", "password1", "password2");
    }

    @Test(expected = UsernameAlreadyInUseException.class)
    public void shouldThrowExceptionWhenUsernameIsAlreadyInUse() {
        this.signUp.exec("user", "password3", "password3");
    }

    @Test
    public void shouldSaveWhenDataIsCorrect() {
        User user = this.signUp.exec("newuser", "password", "password");
        assertThat(user.getId(), is(notNullValue()));
    }
}
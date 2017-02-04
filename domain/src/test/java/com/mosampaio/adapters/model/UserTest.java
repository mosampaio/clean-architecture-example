package com.mosampaio.adapters.model;

import com.mosampaio.domain.model.User;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    @Test
    public void shouldAuthenticate() {
        User user = new User("user", "password");

        assertTrue(user.authenticate("password"));
        assertFalse(user.authenticate("password2"));
    }
}
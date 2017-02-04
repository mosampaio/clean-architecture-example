package com.mosampaio.usecases;

import com.mosampaio.usecases.exception.PasswordsDoNotMatchException;
import com.mosampaio.usecases.exception.UsernameAlreadyInUseException;
import com.mosampaio.domain.model.User;
import com.mosampaio.domain.repository.UserRepository;
import support.NotEmpty;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Optional;

public class SignUp {

    private final UserRepository userRepository;

    @Inject
    public SignUp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Valid
    public User exec(
            @NotEmpty(message = "Username cannot be empty") String username,
            @NotEmpty(message = "Password1 cannot be empty") String password1,
            @NotEmpty(message = "Password2 cannot be empty") String password2) {

        Optional<User> optUser = userRepository.findByUsername(username);

        if (!password1.equals(password2)) {
            throw new PasswordsDoNotMatchException();
        } else if (optUser.isPresent()) {
            throw new UsernameAlreadyInUseException();
        } else {
            return userRepository.save(new User(username, password1));
        }
    }

}

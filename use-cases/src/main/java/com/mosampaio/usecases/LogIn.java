package com.mosampaio.usecases;

import com.mosampaio.usecases.exception.IncorrectUsernameOrPasswordException;
import com.mosampaio.domain.model.User;
import com.mosampaio.domain.repository.UserRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Optional;

public class LogIn {

    private UserRepository userRepository;

    @Inject
    public LogIn(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Valid
    public User exec(String username, String password) {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (!optUser.isPresent()) {
            throw new IncorrectUsernameOrPasswordException();
        } else {
            User user = optUser.get();
            if (!user.authenticate(password)) {
                throw new IncorrectUsernameOrPasswordException();
            } else {
                return user;
            }
        }
    }

}

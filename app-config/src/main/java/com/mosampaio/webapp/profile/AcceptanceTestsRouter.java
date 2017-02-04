package com.mosampaio.webapp.profile;

import com.google.inject.Inject;
import com.mosampaio.domain.repository.UserRepository;

import java.util.HashMap;

import static spark.Spark.post;

public class AcceptanceTestsRouter {

    private UserRepository userRepository;

    @Inject
    public AcceptanceTestsRouter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register() {
        // for testing
        post("/setup-for-tests", (req, res) -> {
            this.userRepository.deleteAll();
            res.status(200);
            return new HashMap<>();
        });
    }
}

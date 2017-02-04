package com.mosampaio.adapters.web;

import com.mosampaio.domain.model.User;
import lombok.Value;

@Value
public class UserResponse {
    private String id;
    private String username;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}

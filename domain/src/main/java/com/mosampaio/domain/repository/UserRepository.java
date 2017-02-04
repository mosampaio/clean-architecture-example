package com.mosampaio.domain.repository;

import com.mosampaio.domain.model.User;

import java.util.Optional;

public interface UserRepository {

  User save(User user);

  Optional<User> findByUsername(String username);

  void deleteAll();

}

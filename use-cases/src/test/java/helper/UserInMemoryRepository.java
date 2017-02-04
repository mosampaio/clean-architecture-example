package helper;


import com.mosampaio.domain.model.User;
import com.mosampaio.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserInMemoryRepository implements UserRepository {

  private Map<String, User> users = new HashMap<>();

  @Override
  public User save(User user) {
    users.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return users.values().stream()
      .filter(user -> user.getUsername().equalsIgnoreCase(username))
      .findFirst();
  }

  @Override
  public void deleteAll() {
    users.clear();
  }

}

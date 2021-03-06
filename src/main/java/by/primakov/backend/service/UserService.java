package by.primakov.backend.service;

import by.primakov.backend.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    boolean isUniqUsername(String username);

    boolean isUniqEmail(String email);
}

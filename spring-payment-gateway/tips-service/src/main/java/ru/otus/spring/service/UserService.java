package ru.otus.spring.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.spring.model.User;

public interface UserService extends UserDetailsService {
    User addUser(String firstName, String lastName, String phone, String password);

    User getUserById(String id);

    User getUserByPhone(String phone);
}

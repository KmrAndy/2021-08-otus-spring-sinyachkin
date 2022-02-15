package ru.otus.spring.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.spring.models.User;

public interface UserDetailsServiceCustom extends UserDetailsService {

    User findUserByUsername(String username) throws UsernameNotFoundException;
    void saveUser(User user) throws DataAccessException;
}

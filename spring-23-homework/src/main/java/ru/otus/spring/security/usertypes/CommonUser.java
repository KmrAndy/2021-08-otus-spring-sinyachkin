package ru.otus.spring.security.usertypes;

import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

public class CommonUser extends User {
    public CommonUser(String username, String password) {
        super(username, password, new ArrayList<>());
    }
}

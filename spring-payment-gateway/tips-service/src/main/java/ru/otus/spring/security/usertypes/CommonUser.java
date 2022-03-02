package ru.otus.spring.security.usertypes;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class CommonUser extends User {
    public CommonUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static CommonUser build(ru.otus.spring.model.User user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new CommonUser(
                user.getPhone(),
                user.getPassword(),
                authorities);
    }
}

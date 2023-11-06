package com.dompoo.onlineshopping.config;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(com.dompoo.onlineshopping.domain.User user) {
        super(user.getEmail(), user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.userId = user.getId();
    }
}

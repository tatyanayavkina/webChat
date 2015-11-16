package com.chat.server.oauth2.domain;

import com.chat.server.model.Role;
import com.chat.server.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created on 10.11.2015.
 */
public class CustomUserDetails implements UserDetails {
//    public static SimpleGrantedAuthority ROLE_GUEST = new SimpleGrantedAuthority(Role.GUEST);
//    public static SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority(Role.USER);
//    public static SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority(Role.ADMIN);
    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for ( Role role : user.getRoles() ) {
            authorities.add( new SimpleGrantedAuthority( role.getCode() ) );
        }
        return authorities;
    }
 }

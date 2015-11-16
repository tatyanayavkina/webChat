package com.chat.server.oauth2.service;

import com.chat.server.oauth2.domain.TokenResponse;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created on 11.11.2015.
 */
public interface AccessService {
    TokenResponse authenticate(String login, String password);
    TokenResponse refresh(String token);
    void checkToken(String token);
    void logout();
    UserDetails currentUser();
}

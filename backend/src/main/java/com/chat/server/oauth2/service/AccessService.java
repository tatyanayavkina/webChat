package com.chat.server.oauth2.service;

import com.chat.server.oauth2.domain.TokenResponse;
import com.chat.server.oauth2.domain.UserResource;

public interface AccessService {
    TokenResponse authenticate(String login, String password);
    TokenResponse refresh(String token);
    void checkToken(String token);
    void logout();
    void tokenLogout(String token);
    UserResource getCurrentUser();
//    UserDetails currentUser();
}

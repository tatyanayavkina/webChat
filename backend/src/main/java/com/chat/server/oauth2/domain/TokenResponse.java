package com.chat.server.oauth2.domain;

import com.chat.server.model.User;

/**
 * Created on 10.11.2015.
 */
public class TokenResponse {

    private final String token;
    private final UserResource user;

    public TokenResponse(String token, User user){
        this.token = token;
        this.user = new UserResource(user);
    }
    public String getToken() {
        return token;
    }

    public UserResource getUser(){
        return user;
    }


}

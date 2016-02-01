package com.chat.server.oauth2.service;

import com.chat.server.model.User;
import com.chat.server.oauth2.domain.TokenResponse;
import com.chat.server.oauth2.domain.UserResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class TokenManager {
    private static TokenManager instance;
    private Map<String, UserDetails> validUsers;

    private Map<UserDetails, TokenResponse> tokens;

    public static TokenManager getInstance(){
        if ( instance == null ){
            instance = new TokenManager();
        }

        return instance;
    }

    protected TokenManager(){
        this.validUsers = new ConcurrentHashMap<>();
        this.tokens = new ConcurrentHashMap<>();
    }

    public TokenResponse createNewToken(UserDetails userDetails, User user) {
        String token;
        do {
            token = generateToken();
        } while ( validUsers.containsKey( token ) );

        TokenResponse tokenInfo = new TokenResponse( token, user );
        removeUserDetails( userDetails );
        UserDetails previous = validUsers.put( token, userDetails );
        if (previous != null) {
            System.out.println(" *** SERIOUS PROBLEM HERE - we generated the same token (randomly?)!");
            return null;
        }
        tokens.put( userDetails, tokenInfo );

        return tokenInfo;
    }

    private String generateToken() {
        byte[] tokenBytes = new byte[32];
        new SecureRandom().nextBytes(tokenBytes);
        return new String( Base64.encode( tokenBytes ), StandardCharsets.UTF_8 );
    }

    public void removeUserDetails(UserDetails userDetails) {
        TokenResponse token = tokens.remove( userDetails );
        if ( token != null ) {
            validUsers.remove( token.getToken() );
        }
    }

    public TokenResponse getTokenResponseByToken(String token){
        UserDetails userDetails = getUserDetails( token );
        if ( userDetails != null ){
            return tokens.get( userDetails );
        }

        return null;
    }

    public UserDetails removeToken(String token) {
        UserDetails userDetails = validUsers.remove(token);
        if (userDetails != null) {
            tokens.remove(userDetails);
        }
        return userDetails;
    }

    public UserDetails getUserDetails(String token) {
        return validUsers.get(token);
    }

    public Collection<TokenResponse> getUserTokens(UserDetails userDetails) {
        return Arrays.asList( tokens.get( userDetails ) );
    }

    public Map<String, UserDetails> getValidUsers() {
        return Collections.unmodifiableMap( validUsers );
    }

    public UserResource getUserByUserDetails(UserDetails userDetails){
        TokenResponse tokenResponse = tokens.get( userDetails );
        if ( tokenResponse != null ){
            return tokenResponse.getUser();
        }

        return null;
    }

}

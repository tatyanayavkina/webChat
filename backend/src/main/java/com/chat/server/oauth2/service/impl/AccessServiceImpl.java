package com.chat.server.oauth2.service.impl;

import com.chat.server.dao.UserDao;
import com.chat.server.model.User;
import com.chat.server.oauth2.domain.TokenResponse;
import com.chat.server.oauth2.domain.UserResource;
import com.chat.server.oauth2.service.AccessService;
import com.chat.server.oauth2.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created on 10.11.2015.
 */
@Service
public class AccessServiceImpl implements AccessService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDao userDao;

    @PostConstruct
    public void init() {
        System.out.println(" *** AuthenticationServiceImpl.init with: " + applicationContext);
    }

    @Override
    public TokenResponse authenticate(String login, String password)  throws AuthenticationException{
        System.out.println(" *** AuthenticationServiceImpl.authenticate");
        Authentication authentication = new UsernamePasswordAuthenticationToken(login, password);

        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authentication.getPrincipal() != null) {
            UserDetails userContext = (UserDetails) authentication.getPrincipal();
            String username = userContext.getUsername();
            User user = userDao.findUserByLogin(username);
            TokenResponse newToken = TokenManager.getInstance().createNewToken(userContext, user);
            if (newToken == null) {
                return null;
            }
            return newToken;
        }
        return null;
    }

    @Override
    public TokenResponse refresh(String token){
        return TokenManager.getInstance().getTokenResponseByToken(token);
    }

    @Override
    public void checkToken(String token) {
        System.out.println(" *** AuthenticationServiceImpl.checkToken");

        UserDetails userDetails = TokenManager.getInstance().getUserDetails(token);
        if (userDetails != null) {
            System.out.println(" *** UserDetails == "+ userDetails.getUsername());
            UsernamePasswordAuthenticationToken securityToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(securityToken);
        } else {
            System.out.println(" *** UserDetails == null");
            SecurityContextHolder.getContext().setAuthentication(null);
        }

    }

    @Override
    public void logout() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TokenManager.getInstance().removeUserDetails(principal);
        System.out.println(" *** AuthenticationServiceImpl.logout: " + principal);
        SecurityContextHolder.clearContext();
    }

    private UserDetails currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return (UserDetails) authentication.getPrincipal();
    }

    @Override
    public UserResource getCurrentUser(){
        UserDetails userDetails = currentUser();
        return TokenManager.getInstance().getUserByUserDetails( userDetails );
    }

}

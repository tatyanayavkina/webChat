package com.chat.server.security.service;

import com.chat.server.dao.UserDao;
import com.chat.server.model.Role;
import com.chat.server.model.User;
import com.chat.server.oauth2.domain.CustomUserDetails;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created on 05.11.2015.
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = Logger.getLogger(SecurityUserDetailsService.class);

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findUserByLogin(username);

        if (user == null) {
            String message = "Username not found" + username;
            LOGGER.info(message);
            throw new UsernameNotFoundException(message);
        }

        LOGGER.info("Found user in database: " + user);

        return new CustomUserDetails(user);
    }

}

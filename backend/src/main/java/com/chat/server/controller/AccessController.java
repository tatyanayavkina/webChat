package com.chat.server.controller;

import com.chat.server.model.Role;
import com.chat.server.model.User;
import com.chat.server.oauth2.TokenGenerationException;
import com.chat.server.oauth2.domain.TokenResponse;
import com.chat.server.oauth2.service.AccessService;
import com.chat.server.service.RoleService;
import com.chat.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 03.11.2015.
 */
@RestController
@Transactional
public class AccessController {
    @Autowired
    private AccessService accessService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private static int ROLE_GUEST_ID = 1;

    /**
    * Generate token for registered user
    * @return TokenResponse - token with user info
     */
    @RequestMapping(value = "/api/access/login", method = RequestMethod.POST)
    @ResponseBody
    public TokenResponse generateToken(@RequestParam("username") String username, @RequestParam("password") String password) {
        return accessService.authenticate(username, password);
    }

    /**
     * Delete token from token hashmap
     * @return true
     */
    @RequestMapping(value = "/api/access/logout", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteToken() {
        accessService.logout();
        return true;
    }

    /**
     * Find user info by token
     * @return TokenResponse - token and user info that was found
     */
    @RequestMapping(value = "/api/access/refresh", method = RequestMethod.GET)
    @ResponseBody
    public TokenResponse refreshToken(@RequestParam("accessToken") String token) {
        return accessService.refresh(token);
    }

    /**
     * Create new user by nickname and generate token and user info for new user
     * @return TokenResponse - generated token and user info
     */
    @RequestMapping(value = "/api/access/nickname", method = RequestMethod.POST)
    @ResponseBody
    public TokenResponse createUserGenerateToken(@RequestParam("nickname") String nickname) {
        Role roleGuest = roleService.findOne(ROLE_GUEST_ID);
        User user = userService.createUserByNickname( nickname, roleGuest );
        return accessService.authenticate( user.getLogin(), "" );
    }


}

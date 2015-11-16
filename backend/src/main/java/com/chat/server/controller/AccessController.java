package com.chat.server.controller;

import com.chat.server.model.Role;
import com.chat.server.model.User;
import com.chat.server.oauth2.TokenGenerationException;
import com.chat.server.oauth2.domain.TokenResponse;
import com.chat.server.oauth2.service.AccessService;
import com.chat.server.service.RoleService;
import com.chat.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 03.11.2015.
 */
@RestController
public class AccessController {
    @Autowired
    private AccessService accessService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    private static int ROLE_GUEST_ID = 1;

    @RequestMapping(value = "/api/access/login", method = RequestMethod.POST)
    @ResponseBody
    public TokenResponse generateToken(@RequestParam("username") String username, @RequestParam("password") String password) {
        return accessService.authenticate(username, password);
    }

    @RequestMapping(value = "/api/access/logout", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteToken() {
        accessService.logout();
        return true;
    }

    @RequestMapping(value = "/api/access/refresh", method = RequestMethod.GET)
    @ResponseBody
    public TokenResponse refreshToken(@RequestParam("accessToken") String token) {
        return accessService.refresh(token);
    }

    @RequestMapping(value = "/api/access/nickname", method = RequestMethod.POST)
    @ResponseBody
    public TokenResponse createUserGenerateToken(@RequestParam("nickname") String nickname) {
        Role roleGuest = roleService.findOne(ROLE_GUEST_ID);
        User user = userService.createUserByNickname( nickname, roleGuest );
        return accessService.authenticate( user.getLogin(), "" );
    }


}

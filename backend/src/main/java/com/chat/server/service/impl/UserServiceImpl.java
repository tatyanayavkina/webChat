package com.chat.server.service.impl;

import com.chat.server.dao.UserDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Role;
import com.chat.server.service.UserService;
import com.chat.server.service.common.AbstractService;
import com.chat.server.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 28.10.2015.
 */
@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {

    @Autowired
    private UserDao dao;

    public UserServiceImpl(){
        super();
    }

    //API
    @Transactional
    public User createUserByNickname(String nickname,  Role roleGuest){
        String username = nickname + LocalTime.now();
        User newUser = new User(username, "", nickname);
        List<Role> roles = new ArrayList<>();
        roles.add(roleGuest);
        newUser.setRoles(roles);
        create(newUser);
        return newUser;
    }

    public List<User> findUsersByLogin(List<String> logins){
        List<User> users = new ArrayList<User>();
        for( String login: logins ){
            User user = dao.findUserByLogin( login );
            if ( user != null ){
                users.add(user);
            }
        }

        return users;
    }

    @Override
    protected IOperations<User> getDao() {
        return dao;
    }
}

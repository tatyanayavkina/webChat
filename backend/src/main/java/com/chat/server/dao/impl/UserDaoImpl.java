package com.chat.server.dao.impl;

import com.chat.server.dao.UserDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.User;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao{
    public UserDaoImpl(){
        super();
        setClazz(User.class);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public User findUserByLogin(String username){
        List<User> users = new ArrayList<User>();
        users = getCurrentSession()
                .createQuery("from User where login=:login")
                .setParameter("login", username)
                .list();
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }
}

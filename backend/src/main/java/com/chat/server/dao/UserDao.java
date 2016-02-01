package com.chat.server.dao;


import com.chat.server.dao.common.IOperations;
import com.chat.server.model.User;

public interface UserDao extends IOperations<User> {
    User findUserByLogin(String username);
}

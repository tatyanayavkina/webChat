package com.chat.server.oauth2.domain;

import com.chat.server.model.Role;
import com.chat.server.model.User;

import java.util.List;

public class UserResource {
    private int id;
    private String login;
    private String password;
    private String nickname;
    private List<Role> roles;

    public UserResource(User user){
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = "";
        this.nickname = user.getNickname();
        this.roles = user.getRoles();
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getLogin(){
        return login;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public void setRoles(List<Role> roles){
        this.roles = roles;
    }
    public List<Role> getRoles(){
        return roles;
    }

}

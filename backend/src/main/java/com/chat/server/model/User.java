package com.chat.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created on 22.10.2015.
 */
@Entity
@Table(name="user")
public class User implements Serializable{
    private int id;

    private String login;

    private String password;

    private String nickname;

    private Date lastRequest;

    private List<Room> rooms;

    private List<Role> roles;

    public User(int id, String login, String password, String nickname, Date lastRequest){
        this.id = id;
        this.login = login;
        this.password = password;
        this.nickname = nickname;
        this.lastRequest = lastRequest;
    }

    public User(String login, String password, String nickname){
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    public User(){

    }

    public void setId(int id){
        this.id = id;
    }

    @Id
    @GeneratedValue
    @GenericGenerator(name = "generator", strategy = "identity")
    @Column(name="id", nullable=false, unique=true, length=11)
    public int getId(){
        return id;
    }

    public void setLogin(String login){
        this.login = login;
    }

    @Column(name="login")
    public String getLogin(){
        return login;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Column(name="password")
    public String getPassword(){
        return password;
    }


    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    @Column(name="nickname")
    public String getNickname(){
        return nickname;
    }

    public void setLastRequest(Date lastRequest){
        this.lastRequest = lastRequest;
    }

    @Column(name="last_request")
    public Date getLastRequest(){
        return lastRequest;
    }

    public void setRooms(List<Room> rooms){
        this.rooms = rooms;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    public List<Room> getRooms(){
        return rooms;
    }

    public void setRoles(List<Role> roles){
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_link_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public List<Role> getRoles(){
        return roles;
    }
}

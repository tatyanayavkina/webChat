package com.chat.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created on 22.10.2015.
 */
@Entity
@Table(name="room")
public class Room implements Serializable{
    public static int OPEN_TYPE = 0;
    public static int CLOSE_TYPE = 1;

    private int id;

    private String name;

    private int type;

    private User owner;

    private List<User> users;

    public Room(){

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

    public void setName(String name){
        this.name = name;
    }

    @Column(name="name")
    public String getName(){
        return name;
    }

    public void setType(int type){
        this.type= type;
    }

    @Column(name="type")
    public int getType(){
        return type;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id")
    public User getOwner(){
        return owner;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_link_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public List<User> getUsers(){
        return users;
    }
}

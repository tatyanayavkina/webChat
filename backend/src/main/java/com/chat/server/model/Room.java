package com.chat.server.model;

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

    @Id
    @GeneratedValue
    @GenericGenerator(name = "generator", strategy = "identity")
    @Column(name="id", nullable=false, unique=true, length=11)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="type")
    private boolean type;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "rooms")
    private List<User> users;

    public Room(){

    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setType(boolean type){
        this.type= type;
    }

    public boolean getType(){
        return type;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }

    public User getOwner(){
        return owner;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }

    public List<User> getUsers(){
        return users;
    }
}

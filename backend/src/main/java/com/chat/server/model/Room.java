package com.chat.server.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    private int type;


//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_link_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
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

    public void setType(int type){
        this.type= type;
    }

    public int getType(){
        return type;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }

//    @JsonIgnore
    public User getOwner(){
        return owner;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }

    @JsonIgnore
    public List<User> getUsers(){
        return users;
    }
}

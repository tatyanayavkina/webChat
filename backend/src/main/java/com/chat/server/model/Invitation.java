package com.chat.server.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created on 22.10.2015.
 */
@Entity
@Table(name="invitation")
public class Invitation implements Serializable{
    @Id
    @GeneratedValue
    @GenericGenerator(name = "generator", strategy = "identity")
    @Column(name="id", nullable=false, unique=true, length=11)
    private int id;

    @Column(name="status")
    private int status;

    @Column(name="type")
    private int type;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    public Invitation(){

    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

    public void setType(int type){
        this.type= type;
    }

    public int getType(){
        return type;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void setRoom(Room room){
        this.room = room;
    }

    public Room getRoom(){
        return room;
    }
}

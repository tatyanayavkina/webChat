package com.chat.server.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created on 22.10.2015.
 */

@Entity
@Table(name="message")
public class Message implements Serializable{

    private int id;

    private User user;

    private Room room;

    private Date creationTime;

    private String content;

    public Message(){

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

    public void setUser(User user){
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    public User getUser(){
        return user;
    }

    public void setRoom(Room room){
        this.room = room;
    }

    @ManyToOne
    @JoinColumn(name="room_id")
    public Room getRoom(){
        return room;
    }

    public void setCreationTime(Date creationTime){
        this.creationTime = creationTime;
    }

    @Column(name="creation_time")
    public Date getCreationTime(){
        return creationTime;
    }

    public void setContent(String content){
        this.content = content;
    }

    @Column(name="content")
    public String getContent(){
        return content;
    }
}

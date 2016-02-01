package com.chat.server.controller.utils;

import com.chat.server.model.User;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.List;

public class DeferredUnreadMessages<T> extends DeferredResult<T>{
    private List<Integer> roomIds;
    private User user;

    public DeferredUnreadMessages(Long timeout, Object timeoutResult, User user, List<Integer> roomIds){
        super(timeout, timeoutResult);
        this.user = user;
        this.roomIds = roomIds;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public List<Integer> getRoomIds(){
        return roomIds;
    }

    public void setRoomIds(List<Integer> roomIds){
        this.roomIds = roomIds;
    }

}

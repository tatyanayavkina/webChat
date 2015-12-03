package com.chat.server.controller.utils;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.List;

/**
 * Created on 03.12.2015.
 */
public class DeferredUnreadMessages<T> extends DeferredResult<T>{
    private int lastReadMessage;
    private List<Integer> roomIds;

    public DeferredUnreadMessages(Long timeout, Object timeoutResult, int lastReadMessage, List<Integer> roomIds){
        super(timeout, timeoutResult);
        this.lastReadMessage = lastReadMessage;
        this.roomIds = roomIds;
    }

    public int getLastReadMessage(){
        return lastReadMessage;
    }

    public void setLastReadMessage(int lastReadMessage){
        this.lastReadMessage = lastReadMessage;
    }

    public List<Integer> getRoomIds(){
        return roomIds;
    }

    public void setRoomIds(List<Integer> roomIds){
        this.roomIds = roomIds;
    }

}

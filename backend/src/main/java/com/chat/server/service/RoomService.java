package com.chat.server.service;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Room;
import com.chat.server.model.User;

import java.util.List;

/**
 * Created on 28.10.2015.
 */
public interface RoomService extends IOperations<Room> {
    Room joinRoom(Room room, User user);

    List<Room> findOpen();
}

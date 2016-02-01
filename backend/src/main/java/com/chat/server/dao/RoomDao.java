package com.chat.server.dao;

import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Room;

import java.util.List;


public interface RoomDao extends IOperations<Room> {

    List<Room> findByType(int type);
}

package com.chat.server.dao.impl;

import com.chat.server.dao.RoomDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Room;
import org.springframework.stereotype.Repository;

/**
 * Created on 28.10.2015.
 */
@Repository
public class RoomDaoImpl extends AbstractDao<Room> implements RoomDao {
    public RoomDaoImpl(){
        super();
        setClazz(Room.class);
    }

    //API
}

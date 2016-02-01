package com.chat.server.dao.impl;

import com.chat.server.dao.RoleDao;
import com.chat.server.dao.common.AbstractDao;
import com.chat.server.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {
    public RoleDaoImpl(){
        super();
        setClazz(Role.class);
    }

    //API
}

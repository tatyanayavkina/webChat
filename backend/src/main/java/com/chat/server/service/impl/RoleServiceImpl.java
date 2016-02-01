package com.chat.server.service.impl;

import com.chat.server.dao.RoleDao;
import com.chat.server.dao.common.IOperations;
import com.chat.server.model.Role;
import com.chat.server.service.RoleService;
import com.chat.server.service.common.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {

    @Autowired
    private RoleDao dao;

    public RoleServiceImpl(){
        super();
    }

    //API

    @Override
    protected IOperations<Role> getDao() {
        return dao;
    }
}

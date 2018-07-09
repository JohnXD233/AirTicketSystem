package com.hwq.model.impl;

import com.hwq.model.SystemUserDAO;
import com.hwq.pojo.SystemUser;

import java.util.List;

public class SystemUserFromMysql implements SystemUserDAO {
    //这边是示例，相对于 获取来自Oracle中的  管理员数据操作


    @Override
    public List<SystemUser> selectSystemUser(SystemUser systemUser) {
        return null;
    }

    @Override
    public boolean updateSystemUser(SystemUser systemUser) {
        return false;
    }

    @Override
    public boolean deleteSystemUser(SystemUser systemUser) {
        return false;
    }

    @Override
    public boolean addSystemUser(SystemUser systemUser) {
        return false;
    }
}

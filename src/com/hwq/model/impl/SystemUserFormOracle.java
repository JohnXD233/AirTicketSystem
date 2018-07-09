package com.hwq.model.impl;

import com.hwq.model.SystemUserDAO;
import com.hwq.pojo.SystemUser;

import java.util.List;

public class SystemUserFormOracle implements SystemUserDAO {
    //处理来自Oracle 数据源 的   针对SystemUser 管理员信息的   增删改查

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

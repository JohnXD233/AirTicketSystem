package com.hwq.model;

import com.hwq.pojo.SystemUser;

import java.util.List;

public interface SystemUserDAO {//管理员信息 的数据连接接口
    //声明增删改查，子类实现
    List<SystemUser> selectSystemUser(SystemUser systemUser);
    boolean updateSystemUser(SystemUser systemUser);
    boolean deleteSystemUser(SystemUser systemUser);
    boolean addSystemUser(SystemUser systemUser);
}

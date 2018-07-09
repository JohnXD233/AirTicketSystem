package com.hwq.service.ServerManagement;

import java.net.Socket;
import java.rmi.server.RMIClassLoader;

import javax.management.remote.rmi.RMIConnection;

public interface IServerOperator {
	//定义了服务器管理子系统相关业务逻辑
	void start();//启动服务器
	void stop();//停止服务
	void UpdateNetPort(RMIConnection rmiPort,Socket socketPort);
	//rmiPort 服务端口   socketPort  通信端口        修改服务器通信端口
	void initNetPort();
	void updateDbConfig(String ip,String port,String sid,String user,String password);
	void initDbConfig();
}

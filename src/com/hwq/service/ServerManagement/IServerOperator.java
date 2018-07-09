package com.hwq.service.ServerManagement;

import java.net.Socket;
import java.rmi.server.RMIClassLoader;

import javax.management.remote.rmi.RMIConnection;

public interface IServerOperator {
	//�����˷�����������ϵͳ���ҵ���߼�
	void start();//����������
	void stop();//ֹͣ����
	void UpdateNetPort(RMIConnection rmiPort,Socket socketPort);
	//rmiPort ����˿�   socketPort  ͨ�Ŷ˿�        �޸ķ�����ͨ�Ŷ˿�
	void initNetPort();
	void updateDbConfig(String ip,String port,String sid,String user,String password);
	void initDbConfig();
}

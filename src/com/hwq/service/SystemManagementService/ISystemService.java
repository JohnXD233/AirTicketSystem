package com.hwq.service.SystemManagementService;

import com.hwq.pojo.*;
import com.hwq.pojo.PageModel;

import java.util.List;

public interface ISystemService {
    //��������������ͻ��˵�ҵ���߼�
    //������½�����ع���Աʵ������˺Ż���������׳��쳣RemoteException
    SystemUser login(String username,String password);

    //����������׳� RemoteException
    void updatePassword(String username,String oldPassword,String newPassword);

    void addSystemUser(SystemUser user);//�˺��������׳��쳣

    //����Ա�б�ķ�ҳ��ѯ
    PageModel queryAllSystemUsers(String username,int currentPage,int pageSize);

    //���ù���Ա������Ϊ��ʼ���� 888888
    void resetPassword(String username);//ϵͳ����Ա����

    void lockSystemUser(String username);//�������Ա�˺�

    void unlockSystemUser(String username);//�ⶳ


    PageModel queryAllDealer(String netCode,String netName,int currentPage,int pageSize);//���������ѯ

    OiltaxSet getOilTax();

    void updateOilTax(OiltaxSet oiltaxSet);

    int findFlightID(String flightNo); //���ݺ����ŵõ� ����ID


    void addNetDealer(NetDealer dealer);//����������㣬dealer ����

    void resetNetPassword(String netid);//����������������Ϊ123456

    void lockNet(String netcode);//������������

    void unlockNet(String netcode);//�ⶳ

    void addFlight(Flight flight);//��Ӻ�����Ϣ

    void addFlightStop(FlightStop flightStop); //��Ӿ�ͣ��Ϣ

    //��ѯ������Ϣ  PageModel���صļ��ϴ�� ����ʵ�弯��
    PageModel queryFlights(String flightNo,String fromCity,String toCity,char type,char isStop,int currentPage,int pageSize);

    void addDiscount(Discount discount);//����ۿ���Ϣ

    List<String> getAllAirPorts(); //��ѯ������Ϣ���������еĻ���

    List<Dircetory> getAllAirlines();//��ѯ���չ�˾��Ϣ

    //��������������࣬����Ȼ�»���ͳ�Ƹ�������������������  PageModel���� ��������ͳ�ƶ��󼯺�
    PageModel queryNetSaleTotal(String netCode,String month,int currentPage,int pageSize);

    //��������ͳ�ƣ���������࣬����Ȼ�»���ͳ����������������������
    PageModel queryFlightSaleTotal(String flightNo,String month,int currentPage,int pageSize);

    int getPages(int allCount,int pageSize);
}

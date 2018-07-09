package com.hwq.service.SaleNetService;

import com.hwq.pojo.*;

import java.rmi.Remote;
import java.sql.Date;
import java.util.List;

public interface INetService extends Remote{
	//�ýӿڶ�������������ͻ���ҵ���߼�����
    NetDealer login(String netCode,String password);

    void updatePassword(String netCode,String oldPassword,String newPassword);

    //���ط�ҳ����ģ�Ͷ���
    //�����ѯ���ͻ��ڶ�Ʊ֮ǰ��ͨ�������ѯ���Һ�����Ϣ
    PageModel queryFlights(String fromCity, String toCity, String planTime, int currentPage, int pageSize);

    //ͨ�������ѯ�󣬿ͻ�ѡ��ָ���ĺ�����ж�Ʊʱ����
    Flight queryFlight(String flightid);

    void saleTicket(SaleRecord record);//����ͻ��Ķ�Ʊ��Ϣ

    //����ͻ���תǩ��¼�����˲��ˣ�������Ҫ������������Ҫ�����Ľ��
    String changeTicketDate(String recordid,Date newPlanDate);

    //���ط�ҳ����ģ�Ͷ���
    //���������� ���ۼ�¼ ��salerecord����ѯ
    PageModel queryMonthSale(String netcode,String month,String flightNo,int currentPage,int pageSize);

    OiltaxSet getOilTax();

    //��ѯ�ÿ͵Ķ�Ʊ��¼Ϊ��Ʊ��תǩʹ��
    SaleRecord querySaleRecord(String fromCity,String toCity,String idCard,String planDate);

    //��ѯ�õ�¼�� �����������  ������ ��Ʊ��¼�����ۼ�¼��
    List<SaleRecord> queryAllSaleRecord(String netid);

    //������Ʊ��Ϣ
    void addBounceRecord(BounceRecord bounceRecord);

    int getPages(int allCount,int pageSize);

    String[] getAllFromCity(); //��ѯ�����еĳ�������

    String[] getTocity(String fromCity);//��������������fromcity��tocity
}

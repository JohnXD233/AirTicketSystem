package com.hwq.service.SaleNetService;

import com.hwq.pojo.*;

import java.rmi.Remote;
import java.sql.Date;
import java.util.List;

public interface INetService extends Remote{
	//该接口定义了销售网点客户的业务逻辑操作
    NetDealer login(String netCode,String password);

    void updatePassword(String netCode,String oldPassword,String newPassword);

    //返回分页数据模型对象
    //航班查询，客户在订票之前可通过航班查询查找航班信息
    PageModel queryFlights(String fromCity, String toCity, String planTime, int currentPage, int pageSize);

    //通过航班查询后，客户选定指定的航班进行订票时调用
    Flight queryFlight(String flightid);

    void saleTicket(SaleRecord record);//保存客户的订票信息

    //保存客户的转签记录，多了不退，少了需要补交，返回需要补交的金额
    String changeTicketDate(String recordid,Date newPlanDate);

    //返回分页数据模型对象
    //销售网点月 销售记录 （salerecord）查询
    PageModel queryMonthSale(String netcode,String month,String flightNo,int currentPage,int pageSize);

    OiltaxSet getOilTax();

    //查询旅客的订票记录为退票和转签使用
    SaleRecord querySaleRecord(String fromCity,String toCity,String idCard,String planDate);

    //查询该登录的 销售网点对象  的所有 订票记录（销售记录）
    List<SaleRecord> queryAllSaleRecord(String netid);

    //新增退票信息
    void addBounceRecord(BounceRecord bounceRecord);

    int getPages(int allCount,int pageSize);

    String[] getAllFromCity(); //查询出所有的出发城市

    String[] getTocity(String fromCity);//二级联动，根据fromcity查tocity
}

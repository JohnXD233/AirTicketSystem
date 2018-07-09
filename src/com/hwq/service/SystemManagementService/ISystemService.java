package com.hwq.service.SystemManagementService;

import com.hwq.pojo.*;
import com.hwq.pojo.PageModel;

import java.util.List;

public interface ISystemService {
    //定义了销售网点客户端的业务逻辑
    //正常登陆，返回管理员实体对象，账号或密码错误，抛出异常RemoteException
    SystemUser login(String username,String password);

    //旧密码错误抛出 RemoteException
    void updatePassword(String username,String oldPassword,String newPassword);

    void addSystemUser(SystemUser user);//账号重名，抛出异常

    //管理员列表的分页查询
    PageModel queryAllSystemUsers(String username,int currentPage,int pageSize);

    //重置管理员的密码为初始密码 888888
    void resetPassword(String username);//系统管理员主键

    void lockSystemUser(String username);//冻结管理员账号

    void unlockSystemUser(String username);//解冻


    PageModel queryAllDealer(String netCode,String netName,int currentPage,int pageSize);//销售网点查询

    OiltaxSet getOilTax();

    void updateOilTax(OiltaxSet oiltaxSet);

    int findFlightID(String flightNo); //根据航班编号得到 航班ID


    void addNetDealer(NetDealer dealer);//添加销售网点，dealer 商人

    void resetNetPassword(String netid);//重置销售网点密码为123456

    void lockNet(String netcode);//冻结销售网点

    void unlockNet(String netcode);//解冻

    void addFlight(Flight flight);//添加航班信息

    void addFlightStop(FlightStop flightStop); //添加经停信息

    //查询航班信息  PageModel返回的集合存放 航班实体集合
    PageModel queryFlights(String flightNo,String fromCity,String toCity,char type,char isStop,int currentPage,int pageSize);

    void addDiscount(Discount discount);//添加折扣信息

    List<String> getAllAirPorts(); //查询机场信息，返回所有的机场

    List<Dircetory> getAllAirlines();//查询航空公司信息

    //按照销售网点分类，以自然月汇总统计各个销售网点的销售情况  PageModel返回 网点销售统计对象集合
    PageModel queryNetSaleTotal(String netCode,String month,int currentPage,int pageSize);

    //航班销售统计：按航班分类，以自然月汇总统计所有销售网点的销售情况
    PageModel queryFlightSaleTotal(String flightNo,String month,int currentPage,int pageSize);

    int getPages(int allCount,int pageSize);
}

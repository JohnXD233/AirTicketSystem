package com.hwq.service.SystemManagementService;

import com.hwq.form.System_Service_Menu;
import com.hwq.pojo.*;
import com.hwq.tools.OracleConnection;
import oracle.jdbc.OracleTypes;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemService implements ISystemService {
    //最大弊端，写了存储过程，没在返回 pageModel  的函数中调用，而是自己写业务逻辑

    private static Connection conn=OracleConnection.getConn();

    @Override
    public SystemUser login(String username, String password) {
        //系统管理员 登录  正常登陆，返回管理员实体对象，账号或密码错误
        SystemUser systemUser=null;
        String sql="select * from systemuser where username=? and password =?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,username);
            pt.setString(2,password);
            ResultSet rs = pt.executeQuery();
            if (rs.next())
            {
                systemUser=new SystemUser(rs.getInt("userid")+"",rs.getString("username"),
                        rs.getString("password"),rs.getString("email"),
                        (char)rs.getInt("state"),rs.getString("telephone"));
            }
            rs.close();
            pt.close();
            return systemUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            systemUser=null;
        }

        return null; //用户名或者密码错误返回 null
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        //修改密码

        String sql="update systemuser set password=? where username=? and password=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,newPassword);
            pt.setString(2,username);
            pt.setString(3,oldPassword);
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"修改密码成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"修改密码失败");
            }

            pt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addSystemUser(SystemUser user) {
        //增加系统管理员
        String sql="insert into systemuser(USERNAME,PASSWORD,EMAIL,STATE,TELEPHONE) values(?,?,?,?,?)";  //插入考虑主键和外键的问题
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(user.getUserid()));//主键 ，设置了自增
            pt.setString(1,user.getUsername());
            pt.setString(2,user.getPassword());
            pt.setString(3,user.getEmail());
            pt.setInt(4,user.getState()); //这边的int 和Char 可以互转，如果不行，该数据库中字段
            pt.setString(5,user.getTelephone());

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"增加管理员成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"增加管理员失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageModel queryAllSystemUsers(String username, int currentPage, int pageSize) {
        //分页查询所有的管理员,调用存储过程，这里比较特殊都只有一条 1/1 页 1条记录
        PageModel pageModel=new PageModel();
        List<SystemUser> list=new ArrayList<>();

        try {
            if("".equals(username))
            {
                //如果用户名为空，查所有  SYSTEMUSERPAGESPROC2
                    CallableStatement pc = conn.prepareCall("{call SYSTEMUSERPAGESPROC2(?,?,?)}");
                    pc.setInt(1,pageSize);
                    pc.setInt(2,currentPage);
                    pc.registerOutParameter(3,OracleTypes.CURSOR);
                    pc.execute();
                    ResultSet rs=(ResultSet) pc.getObject(3);
                    while (rs.next())
                    {
                        list.add(new SystemUser(rs.getInt("userid")+"",rs.getString("username"),
                                rs.getString("password"),rs.getString("email"),
                                (char)rs.getInt("state"),rs.getString("telephone")));
                    }
                    pageModel.setResult(list);
                    pageModel.setCurrentPage(currentPage);
                    pageModel.setPageSize(pageSize);

                    String sql = "SELECT  count(*) FROM SYSTEMUSER";
                    PreparedStatement pt = conn.prepareStatement(sql);
                    ResultSet rs1 = pt.executeQuery();
                    int allCount = -1;
                    if (rs1 != null) {
                        if (rs1.next()) {
                        allCount = rs1.getInt(1);
                        pageModel.setAllCount(allCount);
                        }
                    }
            }

            else {
                CallableStatement pc = conn.prepareCall("{call SYSTEMUSERPAGESPROC(?,?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);
                pc.setString(3, username);
                pc.registerOutParameter(4, OracleTypes.CURSOR);

                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(4);
                while (rs.next()) {
                    list.add(new SystemUser(rs.getInt("userid") + "", rs.getString("username"),
                            rs.getString("password"), rs.getString("email"),
                            (char) rs.getInt("state"), rs.getString("telephone")));
                }

                pageModel.setResult(list);
                pageModel.setCurrentPage(currentPage);
                pageModel.setPageSize(pageSize);
                pageModel.setAllCount(list.size()); //这边一般都只有一条，直接写了
            }
            return pageModel;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            pageModel=null;
        }

        return null;
    }

    @Override
    public void resetPassword(String username) {
        //重置管理员的密码为初始密码 888888

        //userid 由login函数返回的管理员对象获取（userid username password）
        String sql="update systemuser set password='888888' where username=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,username);
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"重置成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"重置失败");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void lockSystemUser(String username) {//冻结管理员账号  0:正常 1：冻结


        String sql="update systemuser set state='1' where username=?"; //这边用户名唯一 ，直接根据用户名 username 操作了
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,username);
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"冻结成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"冻结失败");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void unlockSystemUser(String username) {//解冻管理员账号

        String sql="update systemuser set state='0' where username=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,username);
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"解冻成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"解冻失败");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageModel queryAllDealer(String netCode, String netName, int currentPage, int pageSize) {
        //NetDealer 的分页查询 ，Oracle中存储过程,这边设置netcode和netname其中一个找到即显示   销售网点查询
       List<NetDealer> list=new ArrayList<>();
       PageModel pageModel=new PageModel();

        try {
            if(!"".equals(netCode)&&!"".equals(netName)) {
                CallableStatement pc = conn.prepareCall("{call NETDEALERPAGESPROC (?,?,?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);
                pc.setString(3, netCode);
                pc.setString(4, netName);
                pc.registerOutParameter(5, oracle.jdbc.internal.OracleTypes.CURSOR);

                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(5);//获取到结果集，查出的销售网点信息
                while (rs.next()) {
                    list.add(new NetDealer(rs.getInt("netid") + "", rs.getInt("userid") + "",
                            rs.getString("netcode"), rs.getString("netname"),
                            rs.getString("password"), rs.getString("director"),
                            rs.getString("telphone"), (char) rs.getInt("state")));
                }
                String sql="select count(*) from netdealer where USERID=(select netid from NETDEALER where NETCODE=?) and NETNAME=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1,netCode);
                pt.setString(2,netName);
                ResultSet rs1=pt.executeQuery();
                if(rs1.next()){
                    pageModel.setAllCount(rs.getInt(1));
                }
                pt.close();
                pc.close();

            }
            else{
                //初次显示，netcode and netname 中没有值
                CallableStatement pc = conn.prepareCall("{call NETDEALERALLPROC (?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);
                pc.registerOutParameter(3, oracle.jdbc.internal.OracleTypes.CURSOR);
                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(3);
                while (rs.next()) {
                    list.add(new NetDealer(rs.getInt("netid") + "", rs.getInt("userid") + "",
                            rs.getString("netcode"), rs.getString("netname"),
                            rs.getString("password"), rs.getString("director"),
                            rs.getString("telphone"), (char) rs.getInt("state")));
                }

                String sql="select count(*) from netdealer";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1=pt.executeQuery();
                if(rs1.next()){
                    pageModel.setAllCount(rs1.getInt(1));
                }
                pc.close();
            }

            pageModel.setPageSize(pageSize);
            pageModel.setCurrentPage(currentPage);
            pageModel.setResult(list); //主要数据存放
            return pageModel;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            list=null;
            pageModel=null;
        }
        return null;
    }

    @Override
    public int findFlightID(String flightNo){
        String sql="select flightid from flight where flightno=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,flightNo);
            ResultSet rs=pt.executeQuery();
            if(rs.next())
            {
                return rs.getInt("flightid");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public OiltaxSet getOilTax() {
        //这边是重复的代码，但是如果直接调NetService 中的不太好
        String sql="select * from OILTAXSET";
        OiltaxSet oiltaxSet=new OiltaxSet();
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            ResultSet rs = pt.executeQuery();
            while(rs.next()){
                oiltaxSet.setBreakpoint(rs.getInt("breakpoint")+"");
                oiltaxSet.setHighfee(rs.getInt("highfee")+"");
                oiltaxSet.setLowfee(rs.getInt("lowfee")+"");
            }
            return oiltaxSet; //返回燃油税对象

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateOilTax(OiltaxSet oiltaxSet){

        String sql="update oiltaxset set lowfee=? ,highfee=?,breakpoint=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setInt(1,Integer.parseInt(oiltaxSet.getLowfee()));
            pt.setInt(2,Integer.parseInt(oiltaxSet.getHighfee()));
            pt.setInt(3,Integer.parseInt(oiltaxSet.getBreakpoint()));
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"设置燃油税成功");
            }
            else
            {
                JOptionPane.showMessageDialog(null,"设置燃油税失败");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNetDealer(NetDealer dealer) {
        //添加销售网点  netid 设置了自增长
        String sql="insert into netdealer(USERID,NETCODE,NETNAME,PASSWORD,DIRECTOR,TELPHONE,STATE) values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(dealer.getNetId()));
            pt.setInt(1,Integer.parseInt(dealer.getUserid()));
            pt.setString(2,dealer.getNetcode());
            pt.setString(3,dealer.getNetname());
            pt.setString(4,dealer.getPassword()); //初始密码为123456
            pt.setString(5,dealer.getDirector());
            pt.setString(6,dealer.getTelphone());
            pt.setInt(7,dealer.getState());

            if(pt.execute()){
                JOptionPane.showMessageDialog(null,"添加销售网点成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"添加销售网点失败");
            }

            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetNetPassword(String netcode) {
        //重置销售网点密码  为123456
        String sql="update netdealer set password=? where netcode=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,"123456");
            pt.setString(2,netcode);

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"销售网点密码重置成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"销售网点密码重置失败");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockNet(String netcode) {
        //冻结销售网点 ,这边设置了 netcode唯一 ，直接根据netcode操作即可
        String sql="update netdealer set state=1 where netcode=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,netcode);

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"销售网点冻结成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"销售网点冻结失败");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlockNet(String netcode) {
        //解冻
        String sql="update netdealer set state=0 where netcode=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,netcode);

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"销售网点解冻成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"销售网点解冻失败");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFlight(Flight flight) {
        //添加航班信息
        String sql="insert into flight(USERID,DICID,FLIGHTNO,STARTAIRPORT,ENDAIRPORT,TYPE,PLANSTARTTIME,PLANENDTIME,AIRRANGE,PRICE,FROMCITY,TOCITY,FLIGHTTYPE,TICKETNUM,ISSTOP) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(flight.getFlightid())); //主键,设置了自增
            pt.setInt(1,Integer.parseInt(flight.getUserid()));  //外键
            pt.setInt(2,Integer.parseInt(flight.getDicid()));  //外键
            pt.setString(3,flight.getFlightno());
            pt.setString(4,flight.getStartairport());
            pt.setString(5,flight.getEndairport());
            pt.setInt(6,flight.getType());  //航班类型
            pt.setString(7,flight.getPlanstarttime());
            pt.setString(8,flight.getPlanendtime());
            pt.setInt(9,Integer.parseInt(flight.getAirrange()));
            pt.setInt(10,Integer.parseInt(flight.getPrice()));
            pt.setString(11,flight.getFromcity());
            pt.setString(12,flight.getTocity());
            pt.setString(13,flight.getFlighttype());  //机型
            pt.setInt(14,flight.getTicketnum());
            pt.setInt(15,flight.getIsStop());

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"添加航班成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"添加航班失败");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFlightStop(FlightStop flightStop){
        String sql="insert into flightstop values(?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setInt(1, Integer.parseInt(flightStop.getFlightid()));
            pt.setString(2, flightStop.getStopcity());
            pt.setString(3, flightStop.getStopairport());
            pt.setString(4, flightStop.getArrivedtime());
            pt.setString(5, flightStop.getAgaintime());
            pt.setInt(6, Integer.parseInt(flightStop.getStopprice()));
            pt.setInt(7, Integer.parseInt(flightStop.getFlightprice()));
            pt.setInt(8, Integer.parseInt(flightStop.getAirrange()));

            if (pt.execute())
            {
                JOptionPane.showMessageDialog(null,"添加经停信息成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"添加经停信息失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageModel queryFlights(String flightNo, String fromCity, String toCity, char type, char isStop, int currentPage, int pageSize) {
        PageModel pageModel=new PageModel();
        List<Flight> list=new ArrayList<>();
        int allCount=-1;
        try {

            if(!"".equals(flightNo)&&!"".equals(fromCity)&&!"".equals(toCity)) {
                CallableStatement pc = conn.prepareCall("{call FLIGHTPAGESPROC2(?,?,?,?,?,?)}");

                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);//这边可以加判断，再调用call获取总页数
                pc.setString(3, fromCity);
                pc.setString(4, toCity);
                pc.setString(5, flightNo); //注意这边的plantime 2018-6-28
                pc.registerOutParameter(6, oracle.jdbc.internal.OracleTypes.CURSOR);

                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(6); //获取结果游标 ，查出的是全部 符合fromcity tocity 和flightNo的
                while (rs.next()) {
                    //再筛选 isstop 和 type
                    if (type == '0') //国内
                    {
                        if (!rs.getString("type").equals(type))
                            continue;
                    } else if (type == '1')//国际
                    {
                        if (!rs.getString("type").equals(type))
                            continue;
                    }

                    if (isStop == '0') //无经停
                    {
                        if (!rs.getString("isstop").equals(isStop))
                            continue;
                    } else if (isStop == '1')//有经停
                    {
                        if (!rs.getString("isstop").equals(isStop))
                            continue;
                    }

                    list.add(new Flight(rs.getInt("flightid") + "", rs.getInt("userid") + ""
                            , rs.getInt("dicid") + "", rs.getString("flightno"), rs.getString("startairport")
                            , rs.getString("endairport"), (char) rs.getInt("type"),
                            rs.getString("planstarttime"), rs.getString("planendtime"),
                            rs.getInt("airrange") + "", rs.getInt("price") + "",
                            rs.getString("fromcity"), rs.getString("tocity"),
                            rs.getString("flighttype"), rs.getInt("ticketnum"), (char) rs.getInt("isstop")));
                }
                rs.close();
                pc.close();

                //获取查询的总记录数
                String sql = "SELECT count(*) FROM FLIGHT where fromcity=? and tocity=? and FLIGHTNO=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1, fromCity);
                pt.setString(2, toCity);
                pt.setString(3, flightNo);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
            }
            else {
                //FLIGHTALLPROC  查询所有航班，当 flightNo 和fromcity tocity 为空时，初始的显示
                CallableStatement pc = conn.prepareCall("{call FLIGHTALLPROC(?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);
                pc.registerOutParameter(3, oracle.jdbc.internal.OracleTypes.CURSOR);
                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(3);
                while (rs.next()) {
                    list.add(new Flight(rs.getInt("flightid") + "", rs.getInt("userid") + ""
                            , rs.getInt("dicid") + "", rs.getString("flightno"), rs.getString("startairport")
                            , rs.getString("endairport"), (char) rs.getInt("type"),
                            rs.getString("planstarttime"), rs.getString("planendtime"),
                            rs.getInt("airrange") + "", rs.getInt("price") + "",
                            rs.getString("fromcity"), rs.getString("tocity"),
                            rs.getString("flighttype"), rs.getInt("ticketnum"), (char) rs.getInt("isstop")));
                }
                rs.close();
                pc.close();

                String sql = "SELECT count(*) FROM FLIGHT";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
            }

            pageModel.setCurrentPage(currentPage);
            pageModel.setPageSize(pageSize);
            pageModel.setResult(list);
            pageModel.setAllCount(allCount);
            return pageModel;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addDiscount(Discount discount) {
        //添加折扣信息  注意主键外键问题
        String sql="insert into discount values(?,?,?)";

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(discount.getDiscountid()));  主键自增
            pt.setInt(1,Integer.parseInt(discount.getFlightid()));
            pt.setInt(2,Integer.parseInt(discount.getDiscount()));
            pt.setDate(3,discount.getDiscountdate());

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"添加折扣成功");
            }
            else{
                JOptionPane.showMessageDialog(null,"添加折扣失败");
            }

            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAllAirPorts() {
        //查询机场信息，返回所有的机场  Dirctory中的是航班公司的，不是机场的，待完成填写机场信息到dirctory
            List<String> list=new ArrayList<>();
        String  sql ="select startairport from FLIGHT where STARTAIRPORT not in (select ENDAIRPORT from FLIGHT)";
        String  sql2 ="select endairport from FLIGHT where ENDAIRPORT not in (select STARTAIRPORT from FLIGHT)";
        String  sql3 ="select startairport from FLIGHT where startairport in (select ENDAIRPORT from FLIGHT)";

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            PreparedStatement pt2 = conn.prepareStatement(sql2);
            PreparedStatement pt3 = conn.prepareStatement(sql3);
            ResultSet rs=pt.executeQuery();
            while(rs.next())
            {
                list.add(rs.getString("startairport"));  //查起飞机场有，到达机场没有
            }

            ResultSet rs2=pt2.executeQuery();
            while(rs2.next())
            {
                list.add(rs2.getString("endairport"));  //查到达机场有，起飞机场没有
            }

            ResultSet rs3=pt3.executeQuery();
            while(rs3.next())
            {
                list.add(rs3.getString("startairport")); //该机场即在起飞，也有到达
            }
            pt.close();
            pt2.close();
            pt3.close();
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            list=null;
        }
        return null;
    }

    @Override
    public List<Dircetory> getAllAirlines() {
        //查询所有航空公司信息，directory
        List<Dircetory> list=new ArrayList<>();
        String sql="select * from dirctory";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            ResultSet rs=pt.executeQuery();
            while(rs.next())
            {
             list.add(new Dircetory(rs.getInt("dicid")+"",rs.getString("dicname"),
                     rs.getInt("fatherid")+"")) ;
            }
            pt.close();
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            list=null;
        }
        return null;
    }

    @Override
    public PageModel queryNetSaleTotal(String netCode, String month, int currentPage, int pageSize) {
        //这边没有指定航班， 统计的是  销售网点，某月的  SALERECORDPAGEPROC2

        PageModel pageModel=new PageModel();
        List<SaleRecord> list=new ArrayList<>();
        int allCount=-1;

        try {
            if(!"".equals(netCode)&&!"".equals(month)) {
                CallableStatement pc = conn.prepareCall("{call SALERECORDPAGEPROC2(?,?,?,?,?)}");
                pc.setInt(1, pageSize); //rowforpage
                pc.setInt(2, currentPage); //页号
                pc.setString(3, month); //要查询的月份
                pc.setInt(4, Integer.parseInt(netCode));  //销售网点编号，不是 netid
                pc.registerOutParameter(5, oracle.jdbc.internal.OracleTypes.CURSOR);

                pc.execute();//执行存储过程
                ResultSet rs = (ResultSet) pc.getObject(5);  //将执行结果放到结果集
                while (rs.next()) {
                    list.add(new SaleRecord(rs.getInt("saleid") + "", rs.getInt("netid") + "",
                            rs.getInt("flightid") + "", rs.getInt("ticketmoney") + "",
                            rs.getDate("saletime"), rs.getInt("airporttax") + "",
                            rs.getInt("oiltax") + "", rs.getString("custname"),
                            rs.getString("custtel"), rs.getString("idcard"),
                            rs.getString("startairport"), rs.getString("endairport"),
                            rs.getDate("arrtime"), rs.getDate("starttime"),
                            (char) rs.getInt("salestate")));
                }
                pc.close();
                rs.close();

                //查询总条数
                String sql = "SELECT count(*) FROM SALERECORD where to_char(saletime,'MM')=? and NETID=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1, month);
                pt.setString(2, netCode);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
                pageModel.setAllCount(allCount);//获取所有的记录  list.size()表示本次显示的几条
            }
            else{
                CallableStatement pc = conn.prepareCall("{call SALERECORDALLPROC(?,?,?)}");
                pc.setInt(1, pageSize); //rowforpage
                pc.setInt(2, currentPage); //页号
                pc.registerOutParameter(3, oracle.jdbc.internal.OracleTypes.CURSOR);
                pc.execute();//执行存储过程
                ResultSet rs = (ResultSet) pc.getObject(3);  //将执行结果放到结果集
                while (rs.next()) {
                    list.add(new SaleRecord(rs.getInt("saleid") + "", rs.getInt("netid") + "",
                            rs.getInt("flightid") + "", rs.getInt("ticketmoney") + "",
                            rs.getDate("saletime"), rs.getInt("airporttax") + "",
                            rs.getInt("oiltax") + "", rs.getString("custname"),
                            rs.getString("custtel"), rs.getString("idcard"),
                            rs.getString("startairport"), rs.getString("endairport"),
                            rs.getDate("arrtime"), rs.getDate("starttime"),
                            (char) rs.getInt("salestate")));
                }
                pc.close();
                rs.close();
                //查询总条数
                String sql = "SELECT count(*) FROM SALERECORD";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
                pageModel.setAllCount(allCount);
            }

            pageModel.setPageSize(pageSize);
            pageModel.setCurrentPage(currentPage);
            pageModel.setResult(list);

            return pageModel;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            pageModel=null;
        }
        return null;
    }

    @Override
    public PageModel queryFlightSaleTotal(String flightNo, String month, int currentPage, int pageSize) {
        //航班 月 销售统计  统计的是 某航班，某月的  SALERECORDPAGEPROC3
        PageModel pageModel=new PageModel();
        List<SaleRecord> list=new ArrayList<>();
        int allCount=-1;
        //调用存储过程
        try {
            if(!"".equals(flightNo)&&!"".equals(month)) {
                CallableStatement pc = conn.prepareCall("{call SALERECORDPAGEPROC3(?,?,?,?,?)}");
                pc.setInt(1, pageSize); //rowforpage
                pc.setInt(2, currentPage); //页号
                pc.setString(3, month); //要查询的月份
                pc.setInt(4, Integer.parseInt(flightNo));  //航班no  不同于flightid ，在存储过程中有操作
                pc.registerOutParameter(5, oracle.jdbc.internal.OracleTypes.CURSOR);

                pc.execute();//执行存储过程
                ResultSet rs = (ResultSet) pc.getObject(5);  //将执行结果放到结果集
                while (rs.next()) {
                    list.add(new SaleRecord(rs.getInt("saleid") + "", rs.getInt("netid") + "",
                            rs.getInt("flightid") + "", rs.getInt("ticketmoney") + "",
                            rs.getDate("saletime"), rs.getInt("airporttax") + "",
                            rs.getInt("oiltax") + "", rs.getString("custname"),
                            rs.getString("custtel"), rs.getString("idcard"),
                            rs.getString("startairport"), rs.getString("endairport"),
                            rs.getDate("arrtime"), rs.getDate("starttime"),
                            (char) rs.getInt("salestate")));
                }
                pc.close();
                rs.close();

                //根据flightno 查出 flightid
                int flightid = this.findFlightID(flightNo);

                //查询总条数
                String sql = "SELECT count(*) FROM SALERECORD where to_char(saletime,'MM')=? and FLIGHTID=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1, month);
                pt.setInt(2, flightid);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
                pageModel.setAllCount(allCount);//获取所有的记录  list.size()表示本次显示的几条
            }
            else{
                CallableStatement pc = conn.prepareCall("{call SALERECORDALLPROC(?,?,?)}");
                pc.setInt(1, pageSize); //rowforpage
                pc.setInt(2, currentPage); //页号
                pc.registerOutParameter(3, oracle.jdbc.internal.OracleTypes.CURSOR);
                pc.execute();//执行存储过程
                ResultSet rs = (ResultSet) pc.getObject(3);  //将执行结果放到结果集
                while (rs.next()) {
                    list.add(new SaleRecord(rs.getInt("saleid") + "", rs.getInt("netid") + "",
                            rs.getInt("flightid") + "", rs.getInt("ticketmoney") + "",
                            rs.getDate("saletime"), rs.getInt("airporttax") + "",
                            rs.getInt("oiltax") + "", rs.getString("custname"),
                            rs.getString("custtel"), rs.getString("idcard"),
                            rs.getString("startairport"), rs.getString("endairport"),
                            rs.getDate("arrtime"), rs.getDate("starttime"),
                            (char) rs.getInt("salestate")));
                }
                pc.close();
                rs.close();

                //查询总条数
                String sql = "SELECT count(*) FROM SALERECORD";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
                pageModel.setAllCount(allCount);
            }

            pageModel.setPageSize(pageSize);
            pageModel.setCurrentPage(currentPage);
            pageModel.setResult(list);

            return pageModel;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            pageModel=null;
        }
        return null;
    }

    @Override
    public int getPages(int allCount, int pageSize) {
        if(allCount%pageSize==0)
            return allCount/pageSize;
        else{
            return allCount/pageSize+1;
        }
    }
}

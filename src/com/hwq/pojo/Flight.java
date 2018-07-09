package com.hwq.pojo;

public class Flight {
    //航班信息表
    private String flightid;//航班ID 主键
    private String userid;//管理员ID  外键：关联管理员表
    private String dicid;//所属航空公司  外键：关联数据字典表
    private String flightno;//航班编号
    private String startairport;//起飞机场
    private String endairport;//到达机场
    private char type;//航班类型 0:国内  1：国外
    private String planstarttime;//计划起飞时间
    private String planendtime;//计划到达时间
    private String airrange;//里程数
    private String price;//价格
    private String fromcity;//起飞城市
    private String tocity;//到达城市
    private String flighttype;//机型
    private int ticketnum;//座位数
    private char isStop;//是否经停  0：无经停 1：有

    @Override
    public String toString() {
        return "Flight{" +
                "flightid='" + flightid + '\'' +
                ", userid='" + userid + '\'' +
                ", dicid='" + dicid + '\'' +
                ", flightno='" + flightno + '\'' +
                ", startairport='" + startairport + '\'' +
                ", endairport='" + endairport + '\'' +
                ", type=" + type +
                ", planstarttime='" + planstarttime + '\'' +
                ", planendtime='" + planendtime + '\'' +
                ", airrange='" + airrange + '\'' +
                ", price='" + price + '\'' +
                ", fromcity='" + fromcity + '\'' +
                ", tocity='" + tocity + '\'' +
                ", flighttype='" + flighttype + '\'' +
                ", ticketnum=" + ticketnum +
                ", isStop=" + isStop +
                '}';
    }

    public Flight() {
    }

    public Flight(String flightid, String userid, String dicid, String flightno, String startairport, String endairport, char type, String planstarttime, String planendtime, String airrange, String price, String fromcity, String tocity, String flighttype, int ticketnum, char isStop) {

        this.flightid = flightid;
        this.userid = userid;
        this.dicid = dicid;
        this.flightno = flightno;
        this.startairport = startairport;
        this.endairport = endairport;
        this.type = type;
        this.planstarttime = planstarttime;
        this.planendtime = planendtime;
        this.airrange = airrange;
        this.price = price;
        this.fromcity = fromcity;
        this.tocity = tocity;
        this.flighttype = flighttype;
        this.ticketnum = ticketnum;
        this.isStop = isStop;
    }

    public String getFlightid() {

        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDicid() {
        return dicid;
    }

    public void setDicid(String dicid) {
        this.dicid = dicid;
    }

    public String getFlightno() {
        return flightno;
    }

    public void setFlightno(String flightno) {
        this.flightno = flightno;
    }

    public String getStartairport() {
        return startairport;
    }

    public void setStartairport(String startairport) {
        this.startairport = startairport;
    }

    public String getEndairport() {
        return endairport;
    }

    public void setEndairport(String endairport) {
        this.endairport = endairport;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getPlanstarttime() {
        return planstarttime;
    }

    public void setPlanstarttime(String planstarttime) {
        this.planstarttime = planstarttime;
    }

    public String getPlanendtime() {
        return planendtime;
    }

    public void setPlanendtime(String planendtime) {
        this.planendtime = planendtime;
    }

    public String getAirrange() {
        return airrange;
    }

    public void setAirrange(String airrange) {
        this.airrange = airrange;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFromcity() {
        return fromcity;
    }

    public void setFromcity(String fromcity) {
        this.fromcity = fromcity;
    }

    public String getTocity() {
        return tocity;
    }

    public void setTocity(String tocity) {
        this.tocity = tocity;
    }

    public String getFlighttype() {
        return flighttype;
    }

    public void setFlighttype(String flighttype) {
        this.flighttype = flighttype;
    }

    public int getTicketnum() {
        return ticketnum;
    }

    public void setTicketnum(int ticketnum) {
        this.ticketnum = ticketnum;
    }

    public char getIsStop() {
        return isStop;
    }

    public void setIsStop(char isStop) {
        this.isStop = isStop;
    }
}

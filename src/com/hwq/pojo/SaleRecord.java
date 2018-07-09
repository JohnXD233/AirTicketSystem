package com.hwq.pojo;

import java.sql.Date;

public class SaleRecord {
    //销售记录表
    private String saleid;//主键  销售记录ID
    private String netId;//外键：关联销售网点表
    private String flightid;//航班ID  外键：关联航班表
    private String ticketmoney;//票面金额
    private Date saletime;//销售时间  默认：sysdate
    private String airporttax;//建设费
    private String oiltax;//燃油税
    private String custname;//旅客姓名
    private String custtel;//旅客电话
    private String idcard;//旅客身份证
    private String startairport;//起飞机场
    private String endairport;//到达机场
    private Date arrtime;//到达时间
    private Date starttime;//起飞时间
    private char salesatate;//销售状态  0：正常销售，1:退票，2：转签

    @Override
    public String toString() {
        return "SaleRecord{" +
                "saleid='" + saleid + '\'' +
                ", netId='" + netId + '\'' +
                ", flightid='" + flightid + '\'' +
                ", ticketmoney='" + ticketmoney + '\'' +
                ", saletime=" + saletime +
                ", airporttax='" + airporttax + '\'' +
                ", oiltax='" + oiltax + '\'' +
                ", custname='" + custname + '\'' +
                ", custtel='" + custtel + '\'' +
                ", idcard='" + idcard + '\'' +
                ", startairport='" + startairport + '\'' +
                ", endairport='" + endairport + '\'' +
                ", arrtime=" + arrtime +
                ", starttime=" + starttime +
                ", salesatate=" + salesatate +
                '}';
    }

    public SaleRecord() {
    }

    public SaleRecord(String saleid, String netId, String flightid, String ticketmoney, Date saletime, String airporttax, String oiltax, String custname, String custtel, String idcard, String startairport, String endairport, Date arrtime, Date starttime, char salesatate) {

        this.saleid = saleid;
        this.netId = netId;
        this.flightid = flightid;
        this.ticketmoney = ticketmoney;
        this.saletime = saletime;
        this.airporttax = airporttax;
        this.oiltax = oiltax;
        this.custname = custname;
        this.custtel = custtel;
        this.idcard = idcard;
        this.startairport = startairport;
        this.endairport = endairport;
        this.arrtime = arrtime;
        this.starttime = starttime;
        this.salesatate = salesatate;
    }

    public String getSaleid() {

        return saleid;
    }

    public void setSaleid(String saleid) {
        this.saleid = saleid;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getFlightid() {
        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid;
    }

    public String getTicketmoney() {
        return ticketmoney;
    }

    public void setTicketmoney(String ticketmoney) {
        this.ticketmoney = ticketmoney;
    }

    public Date getSaletime() {
        return saletime;
    }

    public void setSaletime(Date saletime) {
        this.saletime = saletime;
    }

    public String getAirporttax() {
        return airporttax;
    }

    public void setAirporttax(String airporttax) {
        this.airporttax = airporttax;
    }

    public String getOiltax() {
        return oiltax;
    }

    public void setOiltax(String oiltax) {
        this.oiltax = oiltax;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getCusttel() {
        return custtel;
    }

    public void setCusttel(String custtel) {
        this.custtel = custtel;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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

    public Date getArrtime() {
        return arrtime;
    }

    public void setArrtime(Date arrtime) {
        this.arrtime = arrtime;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public char getSalesatate() {
        return salesatate;
    }

    public void setSalesatate(char salesatate) {
        this.salesatate = salesatate;
    }
}


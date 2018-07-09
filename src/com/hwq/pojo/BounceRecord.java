package com.hwq.pojo;

import java.sql.Date;

public class BounceRecord {
    //退票记录表
    private String bounceid;// 退票记录ID 主键
    private String saleid;//销售记录ID  外键：关联销售记录表
    private String netId;// 网点ID  外键：关联销售网点表
    private Date bouncedate;//退票时间
    private String custname;//旅客姓名
    private String custtel;//旅客电话
    private String reason;//退票原因
    private String money;//退票金额

    @Override
    public String toString() {
        return "BounceRecord{" +
                "bounceid='" + bounceid + '\'' +
                ", saleid='" + saleid + '\'' +
                ", netId='" + netId + '\'' +
                ", bouncedate=" + bouncedate +
                ", custname='" + custname + '\'' +
                ", custtel='" + custtel + '\'' +
                ", reason='" + reason + '\'' +
                ", money='" + money + '\'' +
                '}';
    }

    public BounceRecord() {
    }

    public BounceRecord(String bounceid, String saleid, String netId, Date bouncedate, String custname, String custtel, String reason, String money) {

        this.bounceid = bounceid;
        this.saleid = saleid;
        this.netId = netId;
        this.bouncedate = bouncedate;
        this.custname = custname;
        this.custtel = custtel;
        this.reason = reason;
        this.money = money;
    }

    public String getBounceid() {

        return bounceid;
    }

    public void setBounceid(String bounceid) {
        this.bounceid = bounceid;
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

    public Date getBouncedate() {
        return bouncedate;
    }

    public void setBouncedate(Date bouncedate) {
        this.bouncedate = bouncedate;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

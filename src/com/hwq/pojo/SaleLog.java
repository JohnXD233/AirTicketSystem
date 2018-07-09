package com.hwq.pojo;


import java.sql.Date;

public class SaleLog {
    //销售日志表
    private String logid;//日志ID 主键
    private String saleid;//销售记录ID  外键：管理销售记录表
    private String netId;//外键：管理销售网点表
    private Date operatordate;//操作时间 默认值 sysdate 即当前系统时间
    private char operatortype;//操作类型  1：售票  2：转签  3：退票

    @Override
    public String toString() {
        return "SaleLog{" +
                "logid='" + logid + '\'' +
                ", saleid='" + saleid + '\'' +
                ", netId='" + netId + '\'' +
                ", operatordate=" + operatordate +
                ", operatortype=" + operatortype +
                '}';
    }

    public SaleLog() {
    }

    public SaleLog(String logid, String saleid, String netId, Date operatordate, char operatortype) {

        this.logid = logid;
        this.saleid = saleid;
        this.netId = netId;
        this.operatordate = operatordate;
        this.operatortype = operatortype;
    }

    public String getLogid() {

        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
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

    public Date getOperatordate() {
        return operatordate;
    }

    public void setOperatordate(Date operatordate) {
        this.operatordate = operatordate;
    }

    public char getOperatortype() {
        return operatortype;
    }

    public void setOperatortype(char operatortype) {
        this.operatortype = operatortype;
    }
}

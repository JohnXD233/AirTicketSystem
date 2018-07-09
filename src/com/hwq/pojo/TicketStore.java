package com.hwq.pojo;

import java.sql.Date;

public class TicketStore {
    //票据仓库表
    private String storeid;//仓库ID 主键
    private String flightid;//外键：关联航班信息表
    private Date ticketdate;//票面日期
    private String fromcity;//起飞城市
    private String tocity;//到达城市
    private String storenum;//剩余座位数
    private int cityseq;//航班序号

    @Override
    public String toString() {
        return "TicketStore{" +
                "storeid='" + storeid + '\'' +
                ", flightid='" + flightid + '\'' +
                ", ticketdate=" + ticketdate +
                ", fromcity='" + fromcity + '\'' +
                ", tocity='" + tocity + '\'' +
                ", storenum='" + storenum + '\'' +
                ", cityseq=" + cityseq +
                '}';
    }

    public TicketStore() {
    }

    public TicketStore(String storeid, String flightid, Date ticketdate, String fromcity, String tocity, String storenum, int cityseq) {

        this.storeid = storeid;
        this.flightid = flightid;
        this.ticketdate = ticketdate;
        this.fromcity = fromcity;
        this.tocity = tocity;
        this.storenum = storenum;
        this.cityseq = cityseq;
    }

    public String getStoreid() {

        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getFlightid() {
        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid;
    }

    public Date getTicketdate() {
        return ticketdate;
    }

    public void setTicketdate(Date ticketdate) {
        this.ticketdate = ticketdate;
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

    public String getStorenum() {
        return storenum;
    }

    public void setStorenum(String storenum) {
        this.storenum = storenum;
    }

    public int getCityseq() {
        return cityseq;
    }

    public void setCityseq(int cityseq) {
        this.cityseq = cityseq;
    }
    /**
     * 无经停飞的航班：0
     * 有经停飞航班：辅助飞往西藏，经停成都。
     * 则：
     * 福州-西藏：0
     * 福州-成都：1
     * 成都-西藏：2

     */
}

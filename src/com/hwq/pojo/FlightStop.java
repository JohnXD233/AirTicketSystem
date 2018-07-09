package com.hwq.pojo;

public class FlightStop {
    //航班经停表
    private String flightid;//航班ID  主键，外键：关联航信息表
    private String stopcity;//经停城市
    private String stopairport;//经停机场
    private String arrivedtime;//到港时间
    private String againtime;//再次起飞时间
    private String stopprice;//经停价格
    private String flightprice;//起飞价格
    private String airrange;//里程数

    @Override
    public String toString() {
        return "FlightStop{" +
                "flightid='" + flightid + '\'' +
                ", stopcity='" + stopcity + '\'' +
                ", stopairport='" + stopairport + '\'' +
                ", arrivedtime='" + arrivedtime + '\'' +
                ", againtime='" + againtime + '\'' +
                ", stopprice='" + stopprice + '\'' +
                ", flightprice='" + flightprice + '\'' +
                ", airrange='" + airrange + '\'' +
                '}';
    }

    public FlightStop() {
    }

    public FlightStop(String flightid, String stopcity, String stopairport, String arrivedtime, String againtime, String stopprice, String flightprice, String airrange) {

        this.flightid = flightid;
        this.stopcity = stopcity;
        this.stopairport = stopairport;
        this.arrivedtime = arrivedtime;
        this.againtime = againtime;
        this.stopprice = stopprice;
        this.flightprice = flightprice;
        this.airrange = airrange;
    }

    public String getFlightid() {

        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid;
    }

    public String getStopcity() {
        return stopcity;
    }

    public void setStopcity(String stopcity) {
        this.stopcity = stopcity;
    }

    public String getStopairport() {
        return stopairport;
    }

    public void setStopairport(String stopairport) {
        this.stopairport = stopairport;
    }

    public String getArrivedtime() {
        return arrivedtime;
    }

    public void setArrivedtime(String arrivedtime) {
        this.arrivedtime = arrivedtime;
    }

    public String getAgaintime() {
        return againtime;
    }

    public void setAgaintime(String againtime) {
        this.againtime = againtime;
    }

    public String getStopprice() {
        return stopprice;
    }

    public void setStopprice(String stopprice) {
        this.stopprice = stopprice;
    }

    public String getFlightprice() {
        return flightprice;
    }

    public void setFlightprice(String flightprice) {
        this.flightprice = flightprice;
    }

    public String getAirrange() {
        return airrange;
    }

    public void setAirrange(String airrange) {
        this.airrange = airrange;
    }
}

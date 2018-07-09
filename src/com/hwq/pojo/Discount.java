package com.hwq.pojo;

import java.sql.Date;

public class Discount {
    //航班打折信息表
    private String discountid;//打折ID 主键
    private String flightid;//航班ID  外键：关联航班信息表
    private String discount;//折扣率
    private Date discountdate;//日期

    @Override
    public String toString() {
        return "Discount{" +
                "discountid='" + discountid + '\'' +
                ", flightid='" + flightid + '\'' +
                ", discount='" + discount + '\'' +
                ", discountdate=" + discountdate +
                '}';
    }

    public Discount() {
    }

    public Discount(String discountid, String flightid, String discount, Date discountdate) {

        this.discountid = discountid;
        this.flightid = flightid;
        this.discount = discount;
        this.discountdate = discountdate;
    }

    public String getDiscountid() {

        return discountid;
    }

    public void setDiscountid(String discountid) {
        this.discountid = discountid;
    }

    public String getFlightid() {
        return flightid;
    }

    public void setFlightid(String flightid) {
        this.flightid = flightid;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Date getDiscountdate() {
        return discountdate;
    }

    public void setDiscountdate(Date discountdate) {
        this.discountdate = discountdate;
    }
}

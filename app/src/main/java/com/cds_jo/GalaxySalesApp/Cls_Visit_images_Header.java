package com.cds_jo.GalaxySalesApp;

/**
 * Created by Hp on 15/09/2017.
 */

public class Cls_Visit_images_Header {

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getTr_Date() {
        return Tr_Date;
    }

    public void setTr_Date(String tr_Date) {
        Tr_Date = tr_Date;
    }

    public String getTr_Time() {
        return Tr_Time;
    }

    public void setTr_Time(String tr_Time) {
        Tr_Time = tr_Time;
    }

    String OrderNo;
    String CustName;
    String Tr_Date;
    String Tr_Time;

    public String getCustNo() {
        return CustNo;
    }

    public void setCustNo(String custNo) {
        CustNo = custNo;
    }

    public String getFlg() {
        return Flg;
    }

    public void setFlg(String flg) {
        Flg = flg;
    }

    String CustNo;
    String Flg;
}

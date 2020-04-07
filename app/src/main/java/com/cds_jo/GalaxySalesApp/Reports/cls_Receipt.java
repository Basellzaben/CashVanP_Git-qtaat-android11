package com.cds_jo.GalaxySalesApp.Reports;

public class cls_Receipt {
    String OrderNo = null;
    String Date = null;
    String Amt = null;
    String Cash = null;
    String CheckTotal = null;
    String notes = null;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    public String getCash() {
        return Cash;
    }

    public void setCash(String cash) {
        Cash = cash;
    }

    public String getCheckTotal() {
        return CheckTotal;
    }

    public void setCheckTotal(String checkTotal) {
        CheckTotal = checkTotal;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

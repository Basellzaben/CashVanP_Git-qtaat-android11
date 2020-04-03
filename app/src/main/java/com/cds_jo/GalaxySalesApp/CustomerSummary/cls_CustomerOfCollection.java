package com.cds_jo.GalaxySalesApp.CustomerSummary;

public class cls_CustomerOfCollection {
    String NameCust;
    String Tr_date;
    String orderNo;
    String Amt;
    String NewAmt;
    String InoviceAmt;
    String Notes;

    public cls_CustomerOfCollection()
    {

    }

    public cls_CustomerOfCollection(String nameCust, String tr_date, String orderNo, String amt, String newAmt, String inoviceAmt, String notes) {
        NameCust = nameCust;
        Tr_date = tr_date;
        this.orderNo = orderNo;
        Amt = amt;
        NewAmt = newAmt;
        InoviceAmt = inoviceAmt;
        Notes = notes;
    }

    public String getNameCust() {
        return NameCust;
    }

    public void setNameCust(String nameCust) {
        NameCust = nameCust;
    }

    public String getTr_date() {
        return Tr_date;
    }

    public void setTr_date(String tr_date) {
        Tr_date = tr_date;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    public String getNewAmt() {
        return NewAmt;
    }

    public void setNewAmt(String newAmt) {
        NewAmt = newAmt;
    }

    public String getInoviceAmt() {
        return InoviceAmt;
    }

    public void setInoviceAmt(String inoviceAmt) {
        InoviceAmt = inoviceAmt;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
}

package com.cds_jo.GalaxySalesApp.assist;


public class cls_TableOfCollection {
    String NameCust;
    String Tr_date;
    String NewTr_date;
    String custNo1;
    String orderNo;
    String Amt;
    String NewAmt;
    String OrderDate;
    String InoviceAmt;
    String Notes;
    String SupervisorNutes;

    public cls_TableOfCollection()
    {

    }
    public cls_TableOfCollection(String nameCust, String tr_date, String newTr_date, String custNo1, String orderNo, String amt, String newAmt, String orderDate, String inoviceAmt, String notes, String supervisorNutes) {
        NameCust = nameCust;
        Tr_date = tr_date;
        NewTr_date = newTr_date;
        this.custNo1 = custNo1;
        this.orderNo = orderNo;
        Amt = amt;
        NewAmt = newAmt;
        OrderDate = orderDate;
        InoviceAmt = inoviceAmt;
        Notes = notes;
        SupervisorNutes = supervisorNutes;
    }

    public String getNewTr_date() {
        return NewTr_date;
    }

    public void setNewTr_date(String newTr_date) {
        NewTr_date = newTr_date;
    }



    public String getOrderNo() {
        return orderNo;
    }

    public String getNewAmt() {
        return NewAmt;
    }

    public void setNewAmt(String newAmt) {
        NewAmt = newAmt;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
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





    public void setCustNo(String custNo1) {
        this.custNo1 = custNo1;
    }

    public String getOrderNo1() {
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

    public String getSupervisorNutes() {
        return SupervisorNutes;
    }

    public void setSupervisorNutes(String supervisorNutes) {
        SupervisorNutes = supervisorNutes;
    }
}

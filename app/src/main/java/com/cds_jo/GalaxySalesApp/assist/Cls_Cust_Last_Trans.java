package com.cds_jo.GalaxySalesApp.assist;

public class Cls_Cust_Last_Trans {

    public String getCust_No() {
        return Cust_No;
    }

    public void setCust_No(String cust_No) {
        Cust_No = cust_No;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getV_Type() {
        return V_Type;
    }

    public void setV_Type(String v_Type) {
        V_Type = v_Type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayCount() {
        return DayCount;
    }

    public void setDayCount(String dayCount) {
        DayCount = dayCount;
    }

    public String getPaymethod() {
        return Paymethod;
    }

    public void setPaymethod(String paymethod) {
        Paymethod = paymethod;
    }

    public String getDocTypeDesc() {
        return DocTypeDesc;
    }

    public void setDocTypeDesc(String docTypeDesc) {
        DocTypeDesc = docTypeDesc;
    }

    public String getCust_Nm() {
        return Cust_Nm;
    }

    public void setCust_Nm(String cust_Nm) {
        Cust_Nm = cust_Nm;
    }

    String Cust_Nm, Cust_No,amt,doctype,V_Type,date,DayCount,Paymethod,DocTypeDesc;
}

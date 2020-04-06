package com.cds_jo.GalaxySalesApp.Reports;

public class cls_sales {
    String Custname = null;
    String ManName = null;
    String TransDate = null;
    String NetTotal = null;
    String Total = null;
    String TaxTotal = null;
    String OrderNo = null;
    String Dis_Amt = null;
    String is_Damage;

    public cls_sales(String custname, String manName, String transDate, String netTotal, String total, String taxTotal, String orderNo, String dis_Amt, String is_Damage) {
        Custname = custname;
        ManName = manName;
        TransDate = transDate;
        NetTotal = netTotal;
        Total = total;
        TaxTotal = taxTotal;
        OrderNo = orderNo;
        Dis_Amt = dis_Amt;
        this.is_Damage = is_Damage;
    }

    public String getCustname() {
        return Custname;
    }

    public void setCustname(String custname) {
        Custname = custname;
    }

    public String getManName() {
        return ManName;
    }

    public String getIs_Damage() {
        return is_Damage;
    }

    public void setIs_Damage(String is_Damage) {
        this.is_Damage = is_Damage;
    }

    public void setManName(String manName) {
        ManName = manName;
    }

    public String getTransDate() {
        return TransDate;
    }

    public void setTransDate(String transDate) {
        TransDate = transDate;
    }

    public String getNetTotal() {
        return NetTotal;
    }

    public void setNetTotal(String netTotal) {
        NetTotal = netTotal;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getTaxTotal() {
        return TaxTotal;
    }

    public void setTaxTotal(String taxTotal) {
        TaxTotal = taxTotal;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getDis_Amt() {
        return Dis_Amt;
    }

    public void setDis_Amt(String dis_Amt) {
        Dis_Amt = dis_Amt;
    }
}

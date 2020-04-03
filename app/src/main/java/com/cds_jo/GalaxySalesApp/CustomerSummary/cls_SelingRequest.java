package com.cds_jo.GalaxySalesApp.CustomerSummary;

public class cls_SelingRequest {
    String date ;
    String Dec ;
    String TaxNo;
    String name ;
    String Tot ;
    String item_no ;
    String TaxPerc ;
    String Sales_No ;
    String day ;

    public cls_SelingRequest(String date, String dec, String taxNo, String name, String tot, String item_no, String taxPerc, String sales_No, String day) {
        this.date = date;
        Dec = dec;
        TaxNo = taxNo;
        this.name = name;
        Tot = tot;
        this.item_no = item_no;
        TaxPerc = taxPerc;
        Sales_No = sales_No;
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDec() {
        return Dec;
    }

    public void setDec(String dec) {
        Dec = dec;
    }

    public String getTaxNo() {
        return TaxNo;
    }

    public void setTaxNo(String taxNo) {
        TaxNo = taxNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTot() {
        return Tot;
    }

    public void setTot(String tot) {
        Tot = tot;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getTaxPerc() {
        return TaxPerc;
    }

    public void setTaxPerc(String taxPerc) {
        TaxPerc = taxPerc;
    }

    public String getSales_No() {
        return Sales_No;
    }

    public void setSales_No(String sales_No) {
        Sales_No = sales_No;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

package com.cds_jo.GalaxySalesApp.CustomerSummary;

public class cls_Bill {
    String date ;
    String Dec ;
    String SumWithOutTax;
    String name ;
    String Tot ;
    String item_no ;
    String totalwithtax ;
    String bill ;
    String cluse ;

    public cls_Bill(String date, String dec, String sumWithOutTax, String name, String tot, String item_no, String totalwithtax, String bill, String cluse) {
        this.date = date;
        Dec = dec;
        SumWithOutTax = sumWithOutTax;
        this.name = name;
        Tot = tot;
        this.item_no = item_no;
        this.totalwithtax = totalwithtax;
        this.bill = bill;
        this.cluse = cluse;
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

    public String getSumWithOutTax() {
        return SumWithOutTax;
    }

    public void setSumWithOutTax(String sumWithOutTax) {
        SumWithOutTax = sumWithOutTax;
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

    public String getTotalwithtax() {
        return totalwithtax;
    }

    public void setTotalwithtax(String totalwithtax) {
        this.totalwithtax = totalwithtax;
    }

    public String getbill() {
        return bill;
    }

    public void setSales_No(String bill) {
        this.bill = bill;
    }

    public String getCluse() {
        return cluse;
    }

    public void setCluse(String cluse) {
        this.cluse = cluse;
    }
}

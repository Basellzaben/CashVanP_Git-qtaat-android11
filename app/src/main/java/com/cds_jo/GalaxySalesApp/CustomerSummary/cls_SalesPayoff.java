package com.cds_jo.GalaxySalesApp.CustomerSummary;

public class cls_SalesPayoff{
    String date ;
    String Dec ;
    String DocNo;
    String name ;
    String Tot ;
    String item_no ;
    String totalwithtax ;
    String cluse ;
    String TotalTax ;

    public cls_SalesPayoff(String date, String dec, String docNo, String name, String tot, String item_no, String totalwithtax, String cluse, String totalTax) {
        this.date = date;
        Dec = dec;
        DocNo = docNo;
        this.name = name;
        Tot = tot;
        this.item_no = item_no;
        this.totalwithtax = totalwithtax;
        this.cluse = cluse;
        TotalTax = totalTax;
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

    public String getDocNo() {
        return DocNo;
    }

    public void setDocNo(String docNo) {
        DocNo = docNo;
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

    public String getCluse() {
        return cluse;
    }

    public void setCluse(String cluse) {
        this.cluse = cluse;
    }

    public String getTotalTax() {
        return TotalTax;
    }

    public void setTotalTax(String totalTax) {
        TotalTax = totalTax;
    }
}

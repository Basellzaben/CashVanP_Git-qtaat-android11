package com.cds_jo.GalaxySalesApp.AccountStatement;

public class cls_AccountStatement {
    String itemname;
    String orderno;
    String qty;
    String price;
    String bounes;
    String sum;

    public cls_AccountStatement(String itemname, String orderno, String qty, String price, String bounes, String sum) {
        this.itemname = itemname;
        this.orderno = orderno;
        this.qty = qty;
        this.price = price;
        this.bounes = bounes;
        this.sum = sum;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBounes() {
        return bounes;
    }

    public void setBounes(String bounes) {
        this.bounes = bounes;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}

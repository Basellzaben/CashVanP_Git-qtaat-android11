package com.cds_jo.GalaxySalesApp.CustomerSummary;

public class cls_SelingRequestC {
    String price ;
    String Bonus ;
    String Tot ;
    String A_Qty ;
    String Item_Name ;
    String Sales_No ;

    public cls_SelingRequestC(String price, String bonus, String tot, String a_Qty, String item_Name, String sales_No) {
        this.price = price;
        Bonus = bonus;
        Tot = tot;
        A_Qty = a_Qty;
        Item_Name = item_Name;
        Sales_No = sales_No;
    }

    public String getSales_No() {
        return Sales_No;
    }

    public void setSales_No(String sales_No) {
        Sales_No = sales_No;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBonus() {
        return Bonus;
    }

    public void setBonus(String bonus) {
        Bonus = bonus;
    }

    public String getTot() {
        return Tot;
    }

    public void setTot(String tot) {
        Tot = tot;
    }

    public String getA_Qty() {
        return A_Qty;
    }

    public void setA_Qty(String a_Qty) {
        A_Qty = a_Qty;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }
}

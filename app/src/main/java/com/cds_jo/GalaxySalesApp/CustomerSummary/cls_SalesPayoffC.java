package com.cds_jo.GalaxySalesApp.CustomerSummary;

public class cls_SalesPayoffC {
    String price ;
    String Bonus ;
    String totalwithtax ;
    String A_Qty ;
    String Item_Name ;

    public cls_SalesPayoffC(String price, String bonus, String totalwithtax, String a_Qty, String item_Name) {
        this.price = price;
        Bonus = bonus;
        this.totalwithtax = totalwithtax;
        A_Qty = a_Qty;
        Item_Name = item_Name;
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

    public String getTotalwithtax() {
        return totalwithtax;
    }

    public void setTotalwithtax(String totalwithtax) {
        this.totalwithtax = totalwithtax;
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

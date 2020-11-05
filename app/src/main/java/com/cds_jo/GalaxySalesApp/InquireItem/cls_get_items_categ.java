package com.cds_jo.GalaxySalesApp.InquireItem;

public class cls_get_items_categ {

    String ItemCode = null;
    String CategNo = null;
    String Price = null;
    String MinPrice = null;
    String dis = null;
    String bonus = null;
    String CatName = null;

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getCategNo() {
        return CategNo;
    }

    public void setCategNo(String categNo) {
        CategNo = categNo;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(String minPrice) {
        MinPrice = minPrice;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getCatName() {
        return CatName;
    }

    public void setCatName(String catName) {
        CatName = catName;
    }
}

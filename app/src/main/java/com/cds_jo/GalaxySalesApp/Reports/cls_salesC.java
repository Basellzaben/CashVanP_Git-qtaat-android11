package com.cds_jo.GalaxySalesApp.Reports;

public class cls_salesC {
    String Item_Name = null;
    String OrgPrice = null;
    String price = null;
    String Qty = null;
    String Bounce = null;

    String UnitName = null;

    public cls_salesC(String item_Name, String orgPrice, String price, String qty, String bounce, String unitName) {
        Item_Name = item_Name;
        OrgPrice = orgPrice;
        this.price = price;
        Qty = qty;
        Bounce = bounce;
        UnitName = unitName;
    }

    public String getItem_Name() {
        return Item_Name;
    }

    public void setItem_Name(String item_Name) {
        Item_Name = item_Name;
    }

    public String getOrgPrice() {
        return OrgPrice;
    }

    public void setOrgPrice(String orgPrice) {
        OrgPrice = orgPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getBounce() {
        return Bounce;
    }

    public void setBounce(String bounce) {
        Bounce = bounce;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }
}

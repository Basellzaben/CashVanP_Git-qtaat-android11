package com.cds_jo.GalaxySalesApp.SmanChart;

public class Cls_Monthly_Items_Amount {
   private String Type_Name;
    private String Quantity;
    private String Price;
    private String SalesQty;
    private String RetQty;
    private String SalesAmt;
    private String RetAmt;
    private String AmtPrecent;
    private String QtyPrecent;
    private String PaymnetAmt;
    private String CollectionsAmount;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPaymnetAmt() {
        return PaymnetAmt;
    }

    public void setPaymnetAmt(String paymnetAmt) {
        PaymnetAmt = paymnetAmt;
    }

    public String getCollectionsAmount() {
        return CollectionsAmount;
    }

    public void setCollectionsAmount(String collectionsAmount) {
        CollectionsAmount = collectionsAmount;
    }




    public Cls_Monthly_Items_Amount(String type_Name, String quantity, String price, String salesQty, String retQty, String salesAmt, String retAmt, String amtPrecent,
                                       String qtyPrecent, String Collections_Amount , String  Paymnet_Amt) {
        Type_Name = type_Name;
        Quantity = quantity;
        Price = price;
        SalesQty = salesQty;
        RetQty = retQty;
        SalesAmt = salesAmt;
        RetAmt = retAmt;
        AmtPrecent = amtPrecent;
        QtyPrecent = qtyPrecent;
        CollectionsAmount = Collections_Amount;
        PaymnetAmt = Paymnet_Amt;
    }

    public String getType_Name() {
        return Type_Name;
    }

    public void setType_Name(String type_Name) {
        Type_Name = type_Name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice1() {
        return Price;
    }

    public void setPrice1(String price) {
        Price = price;
    }

    public String getSalesQty() {
        return SalesQty;
    }

    public void setSalesQty(String salesQty) {
        SalesQty = salesQty;
    }

    public String getRetQty() {
        return RetQty;
    }

    public void setRetQty(String retQty) {
        RetQty = retQty;
    }

    public String getSalesAmt() {
        return SalesAmt;
    }

    public void setSalesAmt(String salesAmt) {
        SalesAmt = salesAmt;
    }

    public String getRetAmt() {
        return RetAmt;
    }

    public void setRetAmt(String retAmt) {
        RetAmt = retAmt;
    }

    public String getAmtPrecent() {
        return AmtPrecent;
    }

    public void setAmtPrecent(String amtPrecent) {
        AmtPrecent = amtPrecent;
    }

    public String getQtyPrecent() {
        return QtyPrecent;
    }

    public void setQtyPrecent(String qtyPrecent) {
        QtyPrecent = qtyPrecent;
    }
}

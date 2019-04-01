package com.cds_jo.GalaxySalesApp.assist;

/**
 * Created by Hp on 04/08/2017.
 */

public class Cls_Select_Item_From_Pakage {


    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    public String getItemNm() {
        return ItemNm;
    }

    public void setItemNm(String itemNm) {
        ItemNm = itemNm;
    }

    public void setMinQty(String qty) {
        MinQty = qty;
    }

    public String getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    public String getUnitNm() {
        return UnitNm;
    }

    public void setUnitNm(String unitNm) {
        UnitNm = unitNm;
    }

    public String getFlg() {
        return Flg;
    }

    public void setFlg(String flg) {
        Flg = flg;
    }

    String ItemNo;
    String ItemNm;
    String MinQty;
    String UnitNo;
    String UnitNm;
    String Flg;

    public String getTotalItems() {
        return TotalItems;
    }

    public void setTotalItems(String totalItems) {
        TotalItems = totalItems;
    }

    public String getSumQty() {
        return SumQty;
    }

    public void setSumQty(String sumQty) {
        SumQty = sumQty;
    }

    String TotalItems;
    String SumQty;
    public String getMinQty() {
        return MinQty;
    }

    public String getGiftQty() {
        return GiftQty;
    }

    public void setGiftQty(String giftQty) {
        GiftQty = giftQty;
    }

    String GiftQty;


}

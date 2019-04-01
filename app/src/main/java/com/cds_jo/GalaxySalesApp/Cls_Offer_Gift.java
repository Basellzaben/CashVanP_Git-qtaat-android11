package com.cds_jo.GalaxySalesApp;

/**
 * Created by Hp on 11/08/2017.
 */

public class Cls_Offer_Gift {

    String ItemNo;

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

    public String getQy() {
        return Qy;
    }

    public void setQy(String qy) {
        Qy = qy;
    }

    public String getOfferNo() {
        return OfferNo;
    }

    public void setOfferNo(String offerNo) {
        OfferNo = offerNo;
    }

    String ItemNm;
    String Qy;
    String OfferNo;

    public String getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(String totalItem) {
        this.totalItem = totalItem;
    }

    String totalItem;
}

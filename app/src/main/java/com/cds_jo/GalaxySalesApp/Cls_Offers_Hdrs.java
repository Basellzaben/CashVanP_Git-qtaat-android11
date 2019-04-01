package com.cds_jo.GalaxySalesApp;

/**
 * Created by Hp on 11/08/2017.
 */

public class Cls_Offers_Hdrs {

    String Desc;
    String OfferNo;
    String OfferDate;

    public String getCate_Offer() {
        return Cate_Offer;
    }

    public void setCate_Offer(String cate_Offer) {
        Cate_Offer = cate_Offer;
    }

    public String getOffer_Type_Item() {
        return Offer_Type_Item;
    }

    public void setOffer_Type_Item(String offer_Type_Item) {
        Offer_Type_Item = offer_Type_Item;
    }

    String Cate_Offer;
    String Offer_Type_Item;

    public String getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(String end_Date) {
        End_Date = end_Date;
    }

    String End_Date;

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getOfferNo() {
        return OfferNo;
    }

    public void setOfferNo(String offerNo) {
        OfferNo = offerNo;
    }

    public String getOfferDate() {
        return OfferDate;
    }

    public void setOfferDate(String offerDate) {
        OfferDate = offerDate;
    }
}
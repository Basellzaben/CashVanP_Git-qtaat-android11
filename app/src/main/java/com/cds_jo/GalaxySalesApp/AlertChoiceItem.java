package com.cds_jo.GalaxySalesApp;

/**
 * Created by Hp on 07/08/2017.
 */

public class AlertChoiceItem {
    private boolean isChecked;
    private String title;
    private int prior;
    private String ItemNo;

    public String getFlg() {
        return Flg;
    }
//
    public void setFlg(String flg) {
        Flg = flg;
    }

    String Flg ;


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

    public String getOfferNo() {
        return OfferNo;
    }

    public void setOfferNo(String offerNo) {
        OfferNo = offerNo;
    }

    private String ItemNm;
    private String OfferNo;
    public AlertChoiceItem(boolean isChecked, String title, int prior ,String  No , String Nm , String OfferNo) {
        super();
        this.isChecked = isChecked;
        this.title = title;
        this.prior = prior;
        this.ItemNm = Nm;
        this.ItemNo = No;
        this.OfferNo = OfferNo;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getPrior() {
        return prior;
    }
    public void setPrior(int prior) {
        this.prior = prior;
    }


}

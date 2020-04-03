package com.cds_jo.GalaxySalesApp.assist;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Search_po_return  {
    String OrderNo;
    String name;
    String Date;
    String Nettotal;
    String Acc;

    public String getTot() {
        return Tot;
    }

    public void setTot(String tot) {
        Tot = tot;
    }

    String Tot;

    public String getdocType() {
        return docType;
    }

    public void setdocType(String type) {
        docType = type;
    }

    String docType;
    public String getAcc() {
        return Acc;
    }

    public void setAcc(String acc) {
        Acc = acc;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }
    public cls_Search_po_return(String OrderNo  , String name, String Date, String Nettotal,String doctype,String Acc) {
        super();
        this.OrderNo=OrderNo;
        this.name = name;
        this.Date = Date;
        this.Nettotal = Nettotal;
        //this.docType=doctype;
        this.Acc=Acc;

    }
    public cls_Search_po_return() {
        super();
        this.OrderNo="";
        this.name = "";
        this.Date = "";
        this.Nettotal = "";
        this.Acc="";
        //this.docType="";

    }
    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


}

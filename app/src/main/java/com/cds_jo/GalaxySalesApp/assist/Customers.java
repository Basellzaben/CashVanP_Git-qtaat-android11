package com.cds_jo.GalaxySalesApp.assist;


public class Customers {


    String Nm;
    String Address;
    String Mobile;
    String Acc;
    String No;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    String Location;
    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }



    public Customers(String no , String Nm, String Address, String Mobile, String Acc) {
        super();
        this.No=no;
        this.Nm = Nm;
        this.Address = Address;
        this.Mobile = Mobile;
        this.Acc = Acc;
    }

    public Customers()
    {
        super();
        this.No="";
        this.Nm = "";
        this.Address = "";
        this.Mobile = "";
        this.Acc = "";

    }
    public String getNm() {
        return Nm;
    }

    public void setNm(String nm) {
        Nm = nm;
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }



    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAcc() {
        return Acc;
    }

    public void setAcc(String acc) {
        Acc = acc;
    }



}

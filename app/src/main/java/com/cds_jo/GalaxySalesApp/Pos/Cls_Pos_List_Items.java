package com.cds_jo.GalaxySalesApp.Pos;

public class Cls_Pos_List_Items {
    private String Title;
    private int Image ;
    private int ID ;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    private String Price ;
    public Cls_Pos_List_Items(String title, int image, int ID,String Price) {
        Title = title;
        Image = image;
        this.ID = ID;
        this.Price = Price;
    }

    public Cls_Pos_List_Items() {

    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

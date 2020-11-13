package com.cds_jo.GalaxySalesApp.NewHomePage;


public class Cls_Home_Show_Ditial {
    public String getActivityNm() {
        return ActivityNm;
    }

    public void setActivityNm(String activityNm) {
        ActivityNm = activityNm;
    }

    private String ActivityNm;
    private String Title;
    private int Image ;

    private int ID ;

    public Cls_Home_Show_Ditial(String title, int image, int ID,String Activity) {
        Title = title;
        Image = image;
        this.ID = ID;
        this.ActivityNm = Activity;

    }

    public Cls_Home_Show_Ditial() {

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

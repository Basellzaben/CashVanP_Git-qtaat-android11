package com.cds_jo.GalaxySalesApp.ManCard;

public class cls_DetailCardMan {
    String name ;
    String email ;
    String photo;
    String BranchName;
    String manNo;
    String SupervisorName;
    String phone ;

    public cls_DetailCardMan(String name, String email, String photo, String branchName, String manNo, String supervisorName, String phone) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        BranchName = branchName;
        this.manNo = manNo;
        SupervisorName = supervisorName;
        this.phone = phone;
    }

    public String getName1() {
        return name;
    }

    public void setName1(String name) {
        this.name = name;
    }

    public String getEmail1() {
        return email;
    }

    public void setEmail1(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getManNo() {
        return manNo;
    }

    public void setManNo1(String manNo) {
        this.manNo = manNo;
    }

    public String getSupervisorName() {
        return SupervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        SupervisorName = supervisorName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

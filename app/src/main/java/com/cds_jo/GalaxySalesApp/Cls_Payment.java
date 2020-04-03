package com.cds_jo.GalaxySalesApp;

public class Cls_Payment {

        int no;
        String custNo;
        String manNo;
        String orderNo;
        String Amt;
        String Tr_date;
        String Notes;
        String InoviceAmt;

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getManNo() {
            return manNo;
        }

        public void setManNo(String manNo) {
            this.manNo = manNo;
        }

        public String getCustNo() {
            return custNo;
        }

        public void setCustNo(String custNo) {
            this.custNo = custNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getInoviceAmt() {
            return InoviceAmt;
        }

        public void setInoviceAmt(String inoviceAmt) {
            InoviceAmt = inoviceAmt;
        }


        public String getTr_date() {
            return Tr_date;
        }

        public void setTr_date(String tr_date) {
            Tr_date = tr_date;
        }



        public String getNotes() {
            return Notes;
        }

        public void setNotes(String notes) {
            Notes = notes;
        }

        public String getAmt() {
            return Amt;
        }

        public void setAmt(String amt) {
            Amt = amt;
        }
    }


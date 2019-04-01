package com.cds_jo.GalaxySalesApp.assist;

/**
 * Created by Hp on 28/11/2017.
 */

public class Cls_CustomerWithoutPayment {

    public Cls_CustomerWithoutPayment(String custNm, String custNo, String orderNO) {
        CustNm = custNm;
        CustNo = custNo;
        OrderNO = orderNO;
    }

    public String getCustNm() {
        return CustNm;
    }

    public void setCustNm(String custNm) {
        CustNm = custNm;
    }

    public String getCustNo() {
        return CustNo;
    }

    public void setCustNo(String custNo) {
        CustNo = custNo;
    }

    public String getOrderNO() {
        return OrderNO;
    }

    public void setOrderNO(String orderNO) {
        OrderNO = orderNO;
    }

    String CustNm, CustNo,OrderNO;
}

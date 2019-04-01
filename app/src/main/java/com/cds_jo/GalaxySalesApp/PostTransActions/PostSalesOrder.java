package com.cds_jo.GalaxySalesApp.PostTransActions;

import android.content.Context;
import android.database.Cursor;
import com.cds_jo.GalaxySalesApp.ContactListItems;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostSalesOrder {
    Context context;
    String query;
    SqlHandler sqlHandler;
    ArrayList<ContactListItems> PoList;
    public PostSalesOrder(Context conText) {
        context = conText;
        sqlHandler = new SqlHandler(conText);

    }
    public long DoTrans() {
         long Result = -1;
        query = "Select   distinct orderno   from  Po_Hdr   where ifnull(posted,-1)  ='-1' order by no  ";
        Cursor c1 = sqlHandler.selectQuery(query);
       if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Result = Post_Purch_Order(c1.getString(c1.getColumnIndex("orderno")));
                } while (c1.moveToNext());
            }
            c1.close();
        }
        return Result;
    }
    public long Post_Purch_Order(String OrderNo) {
        long Result = -1;
        final String pno = OrderNo;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        query ="Delete from  Po_dtl  where orderno in   ( select orderno from  Po_Hdr   where ifnull(posted,'-1') !='-1' And ( (date)   <  ('"+currentDateandTime+"')))  " ;
        sqlHandler.executeQuery(query);
        query ="Delete from  Po_Hdr where ifnull(posted,'-1')!='-1'  And ((date)   <  ('"+currentDateandTime+"'))  " ;
        sqlHandler.executeQuery(query);

        PoList = new ArrayList<ContactListItems>();
        PoList.clear();

        Fill_Po_Adapter(OrderNo);
        String json = "[{''}]";
        try {
            if (PoList.size() > 0) {
                json = new Gson().toJson(PoList);

            }else  {
                Result= -1; ;
            }
        } catch (Exception ex) {
            Result= -1; ;
        }

        final String str;


        String query = " SELECT  distinct V_OrderNo, acc, userid , Delv_day_count ,date" +
                       " ,orderno ,  Total , Net_Total ,Tax_Total , disc_Total" +
                       " , OrderType,OverCelling  ,include_Tax ,DeliveryDate , Notes,OrderType FROM Po_Hdr  where orderno  ='" + OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("Cust_No", c1.getString(c1.getColumnIndex("acc")));
                jsonObject.put("day_Count", c1.getString(c1.getColumnIndex("Delv_day_count")));
                jsonObject.put("Date", c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("UserID", c1.getString(c1.getColumnIndex("userid")).replace(",", ""));
                jsonObject.put("OrderNo", c1.getString(c1.getColumnIndex("orderno")));
                jsonObject.put("OrderType", c1.getString(c1.getColumnIndex("OrderType")));
                jsonObject.put("OverCelling", c1.getString(c1.getColumnIndex("OverCelling")));
                jsonObject.put("Total", c1.getString(c1.getColumnIndex("Total")).replace(",", ""));
                jsonObject.put("Net_Total", c1.getString(c1.getColumnIndex("Net_Total")).replace(",", ""));
                jsonObject.put("Tax_Total", c1.getString(c1.getColumnIndex("Tax_Total")).replace(",", ""));
                jsonObject.put("bounce_Total", "0");
                jsonObject.put("disc_Total", c1.getString(c1.getColumnIndex("disc_Total")).replace(",", ""));
                jsonObject.put("include_Tax", c1.getString(c1.getColumnIndex("include_Tax")).replace(",", ""));
                jsonObject.put("V_OrderNo", c1.getString(c1.getColumnIndex("V_OrderNo")));
                jsonObject.put("DeliveryDate", c1.getString(c1.getColumnIndex("DeliveryDate")));
                jsonObject.put("Notes", c1.getString(c1.getColumnIndex("Notes")));
                jsonObject.put("OrderType", c1.getString(c1.getColumnIndex("OrderType")));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }


            c1.close();
        }
        if(json.length()<10 ){
            Result= -1;
        }
        str = jsonObject.toString() + json;
         CallWebServices ws = new CallWebServices(context);
        Result = ws.Save_po(str, "Insert_PurshOrder");

                try {
                    if (Result > 0) {
                        sqlHandler.executeQuery("Update Po_Hdr set posted ='"+Result+"' Where orderno ='"+pno+"'");
                  }
                } catch (final Exception e) {
                    Result= -1;
                }
     return  Result ;
    }
    private void Fill_Po_Adapter(String OrderNo) {
        String query = "";
        PoList = new ArrayList<ContactListItems>();
        PoList.clear();




        query = "  select distinct Unites.UnitName, pod.OrgPrice ,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo ,pod.dis_Amt,pod.dis_per, ifnull(pod.bounce_qty,'0') as  bounce_qty ,  pod.tax_Amt   , pod.total  " +
                " , pod.pro_Total, ifnull(pod.Price_From_AB ,0) AS Price_From_AB  ,ifnull( pod.ExpDate,'') as ExpDate  ,ifnull( pod.Batch,'') as  Batch  , pod.ProID , ifnull(pod.Pro_bounce,'0') as  Pro_bounce  ,ifnull(pod.Pro_dis_Per,'0') as Pro_dis_Per ,ifnull(pod.Pro_amt,'0' ) as  Pro_amt" +
                "  from Po_dtl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + OrderNo.toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();
                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("price")));
                    contactListItems.setItemOrgPrice(c1.getString(c1
                            .getColumnIndex("OrgPrice")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setBounce(c1.getString(c1
                            .getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1
                            .getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));
                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));
                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));
                    contactListItems.setProID("");
                    contactListItems.setPro_bounce(c1.getString(c1.getColumnIndex("Pro_bounce")));

                    contactListItems.setPro_dis_Per(c1.getString(c1.getColumnIndex("Pro_dis_Per")));
                    contactListItems.setPro_amt(c1.getString(c1.getColumnIndex("Pro_amt")));
                    contactListItems.setPro_Total("0");
                    contactListItems.setDisAmtFromHdr("0");
                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setBatch(c1.getString(c1
                            .getColumnIndex("Batch")));
                    contactListItems.setExpDate(c1.getString(c1
                            .getColumnIndex("ExpDate")));
                    contactListItems.setPrice_From_AB(c1.getString(c1
                            .getColumnIndex("Price_From_AB")));
                    PoList.add(contactListItems);
                } while (c1.moveToNext());

            }
            c1.close();
        }

    }
}

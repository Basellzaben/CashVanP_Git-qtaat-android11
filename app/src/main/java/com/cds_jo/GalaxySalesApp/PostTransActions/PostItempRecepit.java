package com.cds_jo.GalaxySalesApp.PostTransActions;

import android.content.Context;
import android.database.Cursor;

import com.cds_jo.GalaxySalesApp.ContactListItems;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostItempRecepit {
    Context context;
    String query;
    SqlHandler sqlHandler;
    ArrayList<ContactListItems> PoList;
    public PostItempRecepit(Context conText) {
        context = conText;
        sqlHandler = new SqlHandler(conText);

    }
    public long DoTrans() {
         long Result = -1;
        query = "Select   distinct orderno   from  ItemsReceipthdr   where ifnull(posted,-1)  ='-1' order by no  ";
        Cursor c1 = sqlHandler.selectQuery(query);
       if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Result = Post_Item_Recepit_Order(c1.getString(c1.getColumnIndex("orderno")));
                } while (c1.moveToNext());
            }
            c1.close();
        }
        return Result;
    }
    public long Post_Item_Recepit_Order(String OrderNo) {
        long Result = -1;
        final String pno = OrderNo;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        query ="Delete from  ItemsReceiptdtl  where orderno in   ( select orderno from  ItemsReceipthdr   where ifnull(posted,'-1') !='-1' And ( (date)   <  ('"+currentDateandTime+"')))  " ;
        //sqlHandler.executeQuery(query);
        query ="Delete from  ItemsReceipthdr where ifnull(posted,'-1')!='-1'  And ((date)   <  ('"+currentDateandTime+"'))  " ;
       // sqlHandler.executeQuery(query);

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


        String query = " SELECT  orderno,date,userid,Total,Net_Total,Tax_Total,Notes,Time,purches_serial_no" +
                                " ,purches_order_no,purches_year_no,purches_serial_nm,vendor_no,vendor_nm,hdr_dis_per,hdr_dis_value   " +
                                  " FROM ItemsReceipthdr   where orderno  ='" + OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("orderno", c1.getString(c1.getColumnIndex("orderno")));
                jsonObject.put("Orderdate", c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("userid", c1.getString(c1.getColumnIndex("userid")));
                jsonObject.put("Total", c1.getString(c1.getColumnIndex("Total")).replace(",", ""));
                jsonObject.put("Net_Total", c1.getString(c1.getColumnIndex("Net_Total")));
                jsonObject.put("Notes", c1.getString(c1.getColumnIndex("Notes")));
                jsonObject.put("Time", c1.getString(c1.getColumnIndex("Time")));
                jsonObject.put("purches_serial_no", c1.getString(c1.getColumnIndex("purches_serial_no")).replace(",", ""));
                jsonObject.put("purches_order_no", c1.getString(c1.getColumnIndex("purches_order_no")).replace(",", ""));
                jsonObject.put("purches_year_no", c1.getString(c1.getColumnIndex("purches_year_no")).replace(",", ""));
                jsonObject.put("purches_serial_nm", c1.getString(c1.getColumnIndex("purches_serial_nm")).replace(",", ""));
                jsonObject.put("vendor_no", c1.getString(c1.getColumnIndex("vendor_no")).replace(",", ""));
                jsonObject.put("vendor_nm", c1.getString(c1.getColumnIndex("vendor_nm")).replace(",", ""));
                jsonObject.put("hdr_dis_per", c1.getString(c1.getColumnIndex("hdr_dis_per")));
                jsonObject.put("hdr_dis_value", c1.getString(c1.getColumnIndex("hdr_dis_value")));

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
        Result = ws.Save_ItemRecepit(str);

                try {
                    if (Result > 0) {
                        sqlHandler.executeQuery("Update ItemsReceipthdr set posted ='"+Result+"' Where orderno ='"+pno+"'");
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




        query = "  select distinct  orderno,itemno,qty,tax,unitNo,dis_per,OrgPrice,bounce_qty,tax_Amt" +
                "  ,total,net_total,Opraned,Price_From_AB,store_no,store_nm"+
                "  from ItemsReceiptdtl          Where orderno='" + OrderNo.toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();
                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName("");
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("Price_From_AB")));
                    contactListItems.setItemOrgPrice(c1.getString(c1
                            .getColumnIndex("Price_From_AB")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    contactListItems.setUniteNm("");
                    contactListItems.setBounce(c1.getString(c1
                            .getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1
                            .getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt("");

                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));
                    contactListItems.setTax_Amt("");
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));
                    contactListItems.setProID("");
                    contactListItems.setPro_bounce("0");

                    contactListItems.setPro_dis_Per("0");
                    contactListItems.setPro_amt("0");
                    contactListItems.setPro_Total("0");
                    contactListItems.setDisAmtFromHdr("0");
                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setBatch("0");
                    contactListItems.setExpDate("0");
                    contactListItems.setOperand(c1.getString(c1.getColumnIndex("Opraned")));
                    contactListItems.setPrice_From_AB(c1.getString(c1
                            .getColumnIndex("Price_From_AB")));

                    contactListItems.setStoreNo(c1.getString(c1.getColumnIndex("store_no")));
                    PoList.add(contactListItems);
                } while (c1.moveToNext());

            }
            c1.close();
        }

    }
}

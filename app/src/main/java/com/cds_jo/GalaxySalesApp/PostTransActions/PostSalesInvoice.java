package com.cds_jo.GalaxySalesApp.PostTransActions;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.cds_jo.GalaxySalesApp.Cls_Sal_InvItems;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostSalesInvoice {
    Context context;
    String query;
    SqlHandler sqlHandler;
    ArrayList<Cls_Sal_InvItems> contactList;
    public PostSalesInvoice(Context conText) {
        context = conText;
        sqlHandler = new SqlHandler(conText);
    }
    public long DoTrans() {

        long Result = -1;
        query = "Select    distinct OrderNo,doctype ,ifnull(Time,'') as Time  " +
                "from  Sal_invoice_Hdr where     Post  ='-1' order by no   ";//AND   ( ( strftime('%s','"+currentDateandTime+"'   ) - strftime('%s','+Sal_invoice_Hdr.Time+' ))  /60 )   >=  " + M ;
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() != 0) {

            if (c1.moveToFirst()) {
                do {

              Result = Post_Sal_Inv(c1.getString(c1.getColumnIndex("OrderNo")),c1.getString(c1.getColumnIndex("doctype")));


                } while (c1.moveToNext());
            }
            c1.close();
        }
        return Result;
    }
    public long Post_Sal_Inv(String OrderNo , String DocType) {
        long Result= -1;
        final String pno = OrderNo;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        try {
           // الفاتورة
            query = "Delete from  Sal_invoice_Det  where   ifnull(doctype,'1')='1' and  OrderNo in   " +
                    " ( select OrderNo from  Sal_invoice_Hdr  where   ifnull(doctype,'1')='1' and   Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) )";
            sqlHandler.executeQuery(query);
            query = "Delete from  Sal_invoice_Hdr where  ifnull(doctype,'1')='1' and Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) ";
            sqlHandler.executeQuery(query);

            // المرتجع
            query = "Delete from  Sal_invoice_Det  where   ifnull(doctype,'1')='2' and  OrderNo in   " +
                    " ( select OrderNo from  Sal_invoice_Hdr  where   ifnull(doctype,'1')='2' and   Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) )";
            sqlHandler.executeQuery(query);
            query = "Delete from  Sal_invoice_Hdr where  ifnull(doctype,'1')='2' and Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) ";
            sqlHandler.executeQuery(query);



            // سند تسليم بضاعة
            query = "Delete from  Sal_invoice_Det  where   ifnull(doctype,'1')='3' and  OrderNo in   " +
                    " ( select OrderNo from  Sal_invoice_Hdr  where   ifnull(doctype,'1')='3' and   Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) )";
            sqlHandler.executeQuery(query);
            query = "Delete from  Sal_invoice_Hdr where  ifnull(doctype,'1')='3' and Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) ";
            sqlHandler.executeQuery(query);

        } catch (Exception ex) {

        }
        FillSal_InvAdapter(OrderNo,DocType);
        String json = "[{''}]";
        try {
            if (contactList.size() > 0) {
                json = new Gson().toJson(contactList);
            } else {
                Result= -1;
            }

        } catch (Exception ex) {

        }


        final String str;

        String query = "SELECT distinct     COALESCE(DocType,0) as DocType  , COALESCE(driverno,0) as driverno  ,   V_OrderNo,OrderNo, acc,date,UserID, COALESCE(hdr_dis_per,0) as hdr_dis_per  , COALESCE(hdr_dis_value ,0) as  hdr_dis_value , COALESCE(Total ,0) as  Total , COALESCE(Net_Total ,0) as Net_Total , COALESCE( Tax_Total ,0) as Tax_Total , COALESCE(bounce_Total ,0) as bounce_Total , COALESCE( include_Tax ,0) as include_Tax" +
                " ,Nm ,COALESCE( disc_Total ,0) as  disc_Total , COALESCE(inovice_type ,0)  as inovice_type " +
                " ,Pos_System ,Check_Number,Cash_flg,Check_flg,Visa_flg,Cash_Paid_Amt,Cust_Amt_Paid,Remain_Amt,Check_Paid_Amt,Check_Paid_Date,Check_Paid_Bank" +
                " ,Check_Paid_Person,Visa_Paid_Amt,Visa_Paid_Expire_Date,Visa_Paid_Type  FROM Sal_invoice_Hdr" +
                " where  ifnull(doctype,'1')='"+DocType.toString()+"' and OrderNo  ='" + OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("Cust_No", c1.getString(c1.getColumnIndex("acc")));
                jsonObject.put("Date", c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("UserID", c1.getString(c1.getColumnIndex("UserID")));
                jsonObject.put("OrderNo", c1.getString(c1.getColumnIndex("OrderNo")));
                jsonObject.put("hdr_dis_per", c1.getString(c1.getColumnIndex("hdr_dis_per")));
                if (c1.getString(c1.getColumnIndex("hdr_dis_value")).equals("")) {
                    jsonObject.put("hdr_dis_value", "0");
                } else {
                    jsonObject.put("hdr_dis_value", c1.getString(c1.getColumnIndex("hdr_dis_value")));
                }
                jsonObject.put("Total", c1.getString(c1.getColumnIndex("Total")));
                jsonObject.put("Net_Total", c1.getString(c1.getColumnIndex("Net_Total")));
                jsonObject.put("Tax_Total", c1.getString(c1.getColumnIndex("Tax_Total")));
                jsonObject.put("bounce_Total", c1.getString(c1.getColumnIndex("bounce_Total")));
                jsonObject.put("include_Tax", c1.getString(c1.getColumnIndex("include_Tax")));
                jsonObject.put("disc_Total", c1.getString(c1.getColumnIndex("disc_Total")));
                jsonObject.put("inovice_type", c1.getString(c1.getColumnIndex("inovice_type")));
                jsonObject.put("CashCustNm", c1.getString(c1.getColumnIndex("Nm")));
                jsonObject.put("V_OrderNo", c1.getString(c1.getColumnIndex("V_OrderNo")));
                jsonObject.put("DocType", c1.getString(c1.getColumnIndex("DocType")));
                jsonObject.put("DriverNo", c1.getString(c1.getColumnIndex("driverno")));
                jsonObject.put("Cash_flg", c1.getString(c1.getColumnIndex("Cash_flg")));
                jsonObject.put("Check_flg", c1.getString(c1.getColumnIndex("Check_flg")));
                jsonObject.put("Visa_flg", c1.getString(c1.getColumnIndex("Visa_flg")));
                jsonObject.put("Cash_Paid_Amt", c1.getString(c1.getColumnIndex("Cash_Paid_Amt")));
                jsonObject.put("Cust_Amt_Paid", c1.getString(c1.getColumnIndex("Cust_Amt_Paid")));
                jsonObject.put("Remain_Amt", c1.getString(c1.getColumnIndex("Remain_Amt")));
                jsonObject.put("Check_Paid_Amt", c1.getString(c1.getColumnIndex("Check_Paid_Amt")));
                jsonObject.put("Check_Paid_Date", c1.getString(c1.getColumnIndex("Check_Paid_Date")));
                jsonObject.put("Check_Paid_Bank", c1.getString(c1.getColumnIndex("Check_Paid_Bank")));
                jsonObject.put("Check_Paid_Person", c1.getString(c1.getColumnIndex("Check_Paid_Person")));
                jsonObject.put("Visa_Paid_Amt", c1.getString(c1.getColumnIndex("Visa_Paid_Amt")));
                jsonObject.put("Visa_Paid_Expire_Date", c1.getString(c1.getColumnIndex("Visa_Paid_Expire_Date")));
                jsonObject.put("Visa_Paid_Type", c1.getString(c1.getColumnIndex("Visa_Paid_Type")));
                jsonObject.put("Check_Number", c1.getString(c1.getColumnIndex("Check_Number")));
                jsonObject.put("Pos_System", c1.getString(c1.getColumnIndex("Pos_System")));

            } catch (JSONException ex) {
                Result= -1;
            } catch (Exception ex) {
                Result= -1;

            }
            c1.close();
        }

        if (json.length() < 10) {
            Result= -1;
        }
        str = jsonObject.toString() + json;
        CallWebServices ws = new CallWebServices(context);
        Result= ws.Save_Sal_Invoice(str);
        try {
            if (Result> 0) {
                ContentValues cv = new ContentValues();
                cv.put("Post", Result);
                long i;
                i = sqlHandler.Update("Sal_invoice_Hdr", cv, "  ifnull(doctype,'1')='"+DocType.toString()+"' and     OrderNo='" + pno + "'");
               // Get_ManStore();
            }
        } catch (final Exception e) {
            Result= -1;
        }

        return Result;
    }
    private void FillSal_InvAdapter(String OrderNo , String DocType) {
        String query = "";
        contactList = new ArrayList<Cls_Sal_InvItems>();
        contactList.clear();


        query = "  select distinct   ifnull(pod.DisAmtFromHdr,'0') as DisAmtFromHdr,  ifnull(pod.DisPerFromHdr,'0') as DisPerFromHdr  ,    ifnull(pod.weight,0) as weight   ,       ifnull(pod.Operand,0) as Operand  ,  pod.bounce_qty,pod.dis_per , pod.dis_Amt , pod.OrgPrice , pod.tax_Amt , pod.total ,Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt    " +
                " from Sal_invoice_Det pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo " +
                " Where    ifnull(pod.doctype,'1')='"+DocType.toString()+"' and pod.OrderNo='" + OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemNo")));
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
                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));
                    contactListItems.setOperand(c1.getString(c1
                            .getColumnIndex("Operand")));
                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));
                    contactListItems.setProID(c1.getString(c1
                            .getColumnIndex("ProID")));
                    contactListItems.setPro_bounce(c1.getString(c1
                            .getColumnIndex("Pro_bounce")));
                    contactListItems.setPro_dis_Per(c1.getString(c1
                            .getColumnIndex("Pro_dis_Per")));
                    contactListItems.setPro_amt(c1.getString(c1
                            .getColumnIndex("Pro_amt")));
                    contactListItems.setPro_Total(c1.getString(c1
                            .getColumnIndex("pro_Total")));

                    contactListItems.setDisPerFromHdr(c1.getString(c1
                            .getColumnIndex("DisPerFromHdr")));

                    contactListItems.setDisAmtFromHdr(c1.getString(c1
                            .getColumnIndex("DisAmtFromHdr")));

                    contactListItems.setWeight(c1.getString(c1
                            .getColumnIndex("weight")));
                    contactList.add(contactListItems);
                } while (c1.moveToNext());
            }
            c1.close();
        }
        Cls_Sal_InvItems Inv_Obj;
        for (int j = 0; j < contactList.size(); j++) {
            Inv_Obj = new Cls_Sal_InvItems();
            Inv_Obj = contactList.get(j);
            Inv_Obj.setDis_Amt(String.valueOf(Double.parseDouble(Inv_Obj.getDis_Amt()) + Double.parseDouble(Inv_Obj.getPro_amt()) + Double.parseDouble(Inv_Obj.getDisAmtFromHdr())));
            Inv_Obj.setDiscount(String.valueOf(Double.parseDouble(Inv_Obj.getDiscount()) + Double.parseDouble(Inv_Obj.getPro_dis_Per()) + Double.parseDouble(Inv_Obj.getDisPerFromHdr())));
            Inv_Obj.setBounce(String.valueOf(Double.parseDouble(Inv_Obj.getBounce()) + Double.parseDouble(Inv_Obj.getPro_bounce())));
        }

    }
    public void Get_ManStore() {
        String q;

        final Handler _handler = new Handler();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String UserID = sharedPreferences.getString("UserID", "");




                CallWebServices ws = new CallWebServices(context );
                ws.TrnsferQtyFromMobile(UserID, "0", "");
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_date = js.getJSONArray("date");
                    JSONArray js_fromstore = js.getJSONArray("fromstore");
                    JSONArray js_tostore = js.getJSONArray("tostore");
                    JSONArray js_des = js.getJSONArray("des");
                    JSONArray js_docno = js.getJSONArray("docno");
                    JSONArray js_itemno = js.getJSONArray("itemno");
                    JSONArray js_qty = js.getJSONArray("qty");
                    JSONArray js_UnitNo = js.getJSONArray("UnitNo");
                    JSONArray js_UnitRate = js.getJSONArray("UnitRate");
                    JSONArray js_myear = js.getJSONArray("myear");
                    JSONArray js_StoreName = js.getJSONArray("StoreName");
                    JSONArray js_RetailPrice = js.getJSONArray("RetailPrice");

                    final String Ser = "1";
                    q = "Delete from ManStore";
                    sqlHandler.executeQuery(q);
                    q = "delete from sqlite_sequence where name='ManStore'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_docno.length(); i++) {
                        q = "Insert INTO ManStore(SManNo,date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear,RetailPrice ,StoreName ,ser) values ("
                                + UserID.toString()
                                + ",'" + js_date.get(i).toString()
                                + "','" + js_fromstore.get(i).toString()
                                + "','" + js_tostore.get(i).toString()
                                + "','" + js_des.get(i).toString()
                                + "','" + js_docno.get(i).toString()
                                + "','" + js_itemno.get(i).toString()
                                + "','" + js_qty.get(i).toString()
                                + "','" + js_UnitNo.get(i).toString()
                                + "','" + js_UnitRate.get(i).toString()
                                + "','" + js_myear.get(i).toString()
                                + "','" + js_RetailPrice.get(i).toString()
                                + "','" + js_StoreName.get(i).toString()
                                + "'," + Ser.toString()
                                + " )";
                        sqlHandler.executeQuery(q);

                    }

                  /*  _handler.post(new Runnable() {

                        public void run() {
                           // Get_ItemPrice();
                        }
                    });*/

                } catch (final Exception e) {

                }

    }

}

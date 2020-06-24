package com.cds_jo.GalaxySalesApp.PostTransActions;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cds_jo.GalaxySalesApp.Cls_Sal_InvItems;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostSalesreturn {
    Context context;
    String query;
    SqlHandler sqlHandler;
    ArrayList<Cls_Sal_InvItems> contactList;
    public PostSalesreturn(Context conText) {
        context = conText;
        sqlHandler = new SqlHandler(conText);
    }
    public long DoTrans() {

        long Result = -1;
        query = "Select    distinct Orderno,doctype ,ifnull(Time,'') as Time  " +
                "from  Sal_return_Hdr where     Post  ='-1' order by no   ";//AND   ( ( strftime('%s','"+currentDateandTime+"'   ) - strftime('%s','+Sal_invoice_Hdr.Time+' ))  /60 )   >=  " + M ;
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() != 0) {

            if (c1.moveToFirst()) {
                do {

              Result = Post_Sal_return(c1.getString(c1.getColumnIndex("Orderno")),c1.getString(c1.getColumnIndex("doctype")));


                } while (c1.moveToNext());
            }
            c1.close();
        }
        return Result;
    }
    public long Post_Sal_return(String OrderNo , String DocType) {
        long Result= -1;
        final String pno = OrderNo;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        try {
            // المرتجع
            query = "Delete from  Sal_return_Det  where Orderno in   " +
                    " ( select Orderno from  Sal_return_Hdr  where Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) )";
            sqlHandler.executeQuery(query);
            query = "Delete from  Sal_return_Hdr where Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) ";
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

        String query = "SELECT distinct acc,UserID as UserID , COALESCE(DocType,0) as DocType  , COALESCE(driverno,0) as driverno  ,   V_OrderNo,Orderno, acc,date,UserID, COALESCE(hdr_dis_per,0) as hdr_dis_per  , COALESCE(hdr_dis_value ,0) as  hdr_dis_value , COALESCE(Total ,0) as  Total , COALESCE(Net_Total ,0) as Net_Total , COALESCE( Tax_Total ,0) as Tax_Total , COALESCE(bounce_Total ,0) as bounce_Total , COALESCE( include_Tax ,0) as include_Tax" +
                " ,Nm ,COALESCE( disc_Total ,0) as  disc_Total   FROM Sal_return_Hdr" +
                " where Orderno  ='" + OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("Cust_No", c1.getString(c1.getColumnIndex("acc")));
                jsonObject.put("Date", c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("UserID", c1.getString(c1.getColumnIndex("UserID")));
                jsonObject.put("Orderno", c1.getString(c1.getColumnIndex("Orderno")));
                jsonObject.put("Total", c1.getString(c1.getColumnIndex("Total")));
                jsonObject.put("Net_Total", c1.getString(c1.getColumnIndex("Net_Total")));
                jsonObject.put("Tax_Total", c1.getString(c1.getColumnIndex("Tax_Total")));
                jsonObject.put("bounce_Total", c1.getString(c1.getColumnIndex("bounce_Total")));
                jsonObject.put("include_Tax", c1.getString(c1.getColumnIndex("include_Tax")));
                jsonObject.put("CashCustNm", c1.getString(c1.getColumnIndex("Nm")));
                jsonObject.put("V_OrderNo", c1.getString(c1.getColumnIndex("V_OrderNo")));
                jsonObject.put("DocType", c1.getString(c1.getColumnIndex("DocType")));

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
        Result= ws.Save_Ret_Sal_Invoice(str);
        try {
            if (Result> 0) {
                ContentValues cv = new ContentValues();
                cv.put("Post", Result);
                long i;
                i = sqlHandler.Update("Sal_return_Hdr", cv, " Orderno='" + pno + "'");
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


        query = "  select distinct      ifnull(pod.weight,0) as weight   ,       ifnull(pod.Operand,0) as Operand  ,  pod.bounce_qty, pod.OrgPrice , pod.tax_Amt , pod.total ,Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo  " +
                " from Sal_return_Det pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo " +
                " Where pod.Orderno='" + OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getColumnCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("ItemNo")));
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
                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));
                    contactListItems.setOperand(c1.getString(c1
                            .getColumnIndex("Operand")));
                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("Tax_Amt")));
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("Total")));
                    contactListItems.setWeight(c1.getString(c1
                            .getColumnIndex("weight")));
                    contactList.add(contactListItems);
                } while (c1.moveToNext());
            }
            c1.close();
        }

    }


}

package com.cds_jo.GalaxySalesApp.PostTransActions;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.WriteTxtFile;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostPayments {
    Context context;
    String query;
    SqlHandler sqlHandler;
    ArrayList<Cls_Check> ChecklList;
    public PostPayments(Context conText) {
        context = conText;
        sqlHandler = new SqlHandler(conText);
    }

    public long DoTrans() {
        ChecklList = new ArrayList<Cls_Check>();
        ChecklList.clear();
        long Result = -1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        query = "Delete from  RecCheck  where DocNo in   ( select DocNo from  RecVoucher   " +
                "  where ifnull(Post,-1) !=-1 And ( (TrDate)   <  ('" + currentDateandTime + "')))";
        sqlHandler.executeQuery(query);
        query = "Delete from  RecVoucher where ifnull(Post,-1)!=-1  And ( (TrDate)   <  ('" + currentDateandTime + "'))  ";
        sqlHandler.executeQuery(query);

        query = "Select   distinct DocNo from  RecVoucher  where ifnull(Post,-1)  =-1 order by ID";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Result = Post_Payments(c1.getString(c1.getColumnIndex("DocNo")));
                } while (c1.moveToNext());
            }
            c1.close();
        }
        return Result;
    }
    public long Post_Payments(String OrderNo) {
        final String pno = OrderNo;
        long Reslut = -1;
        FillPayMents_Check_Adapter(OrderNo);

        String json = "[{''}]";
        try {
            if (ChecklList.size() > 0) {
                json = new Gson().toJson(ChecklList);
            }

        } catch (Exception ex) {

        }


        String query = "Select  distinct rc.V_OrderNo, rc.DocNo,  IFNULL(rc.CheckTotal,0) as CheckTotal, IFNULL(rc.Cash,0) as Cash, rc.Desc,rc.Amnt,rc.TrDate,rc.CustAcc  ,c.name , rc.curno  ,COALESCE(Post, -1)  as Post , " +
                "rc.UserID ,rc.VouchType ,rc.UserID from RecVoucher rc   left join Customers c on c.no = rc.CustAcc " +
                " where rc.DocNo = '" + OrderNo.toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("OrderNo", c1.getString(c1.getColumnIndex("DocNo")));
                jsonObject.put("acc", c1.getString(c1.getColumnIndex("CustAcc")));
                jsonObject.put("Amt", c1.getString(c1.getColumnIndex("Amnt")));
                jsonObject.put("Date", c1.getString(c1.getColumnIndex("TrDate")));
                jsonObject.put("notes", c1.getString(c1.getColumnIndex("Desc")));
                jsonObject.put("VouchType", c1.getString(c1.getColumnIndex("VouchType")));
                jsonObject.put("CurNo", c1.getString(c1.getColumnIndex("curno")));
                if (c1.getString(c1.getColumnIndex("Cash")).toString().length() == 0) {
                    jsonObject.put("Cash", "0.0");
                } else {
                    jsonObject.put("Cash", c1.getString(c1.getColumnIndex("Cash")));
                }
                jsonObject.put("CheckTotal", c1.getString(c1.getColumnIndex("CheckTotal")));
                jsonObject.put("V_OrderNo", c1.getString(c1.getColumnIndex("V_OrderNo")));
                jsonObject.put("UserID", c1.getString(c1.getColumnIndex("UserID")));
            } catch (JSONException ex) {
               ex.printStackTrace();
            } catch (Exception ex) {
               ex.printStackTrace();
            }
            c1.close();
        }
        final String str;
        str = jsonObject.toString() + json;


        CallWebServices ws = new CallWebServices(context);
        Reslut= ws.SavePayment(str);
        try {

            if (Reslut> 0) {
                //query = "Update  RecVoucher  set Post="+Reslut+" Where DocNo='"+ OrderNo+"'  and  "+ Reslut + " Not in (Select Post from RecVoucher )";
                query = "Update  RecVoucher  set Post="+Reslut+" Where DocNo='"+ OrderNo+"'    ";
                sqlHandler.executeQuery(query );
            }

        } catch (Exception e) {
            Reslut= -1;
        }


        return Reslut;
    }
    private void FillPayMents_Check_Adapter(String OrderNo) {
        ChecklList = new ArrayList<Cls_Check>();
        String query = "";
        ChecklList.clear();
        query = "Select  distinct rc.CheckNo,rc.CheckDate,rc.BankNo,   IFNULL(rc.Amnt,0)as Amnt , b.Bank from  RecCheck rc  left join banks b on b.bank_num = rc.BankNo" +
                " where DocNo ='" + OrderNo.toString() + "'";
        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Check cls_check_obj = new Cls_Check();
                    cls_check_obj.setSer(i);
                    cls_check_obj.setCheckNo(c1.getString(c1
                            .getColumnIndex("CheckNo")));
                    cls_check_obj.setCheckDate(c1.getString(c1
                            .getColumnIndex("CheckDate")));
                    cls_check_obj.setBankName(c1.getString(c1
                            .getColumnIndex("Bank")));
                    cls_check_obj.setBankNo(c1.getString(c1
                            .getColumnIndex("BankNo")));
                    cls_check_obj.setAmnt(c1.getString(c1
                            .getColumnIndex("Amnt")));
                    ChecklList.add(cls_check_obj);
                    i = i + 1;
                } while (c1.moveToNext());
            }
            c1.close();
        }

    }
}

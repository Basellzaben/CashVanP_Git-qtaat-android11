package com.cds_jo.GalaxySalesApp.PostTransActions;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;

import com.cds_jo.GalaxySalesApp.Cls_ManLocation;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostManLocations {
    Context context;
    String query;
    SqlHandler sqlHandler;
    String currentDateandTime;
    long Result;

    public PostManLocations(Context conText) {
        context = conText;
        sqlHandler = new SqlHandler(conText);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        currentDateandTime = sdf.format(new Date());
    }

    public long DoTrans() {
        long Result = -1;
        Result = PostManLocstion();
        return Result;
    }

    public long PostManLocstion() {
        Result = -1;

        String q = "   ";

        q = "select  distinct  ManNo,Tr_date,Tr_time,X,Y,Loct,V_OrderNo,Posted  , no " +
                "  from ManLocation   where    Posted = '-1' and Tr_date = '" + currentDateandTime + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        ArrayList<Cls_ManLocation> RoundLocation;
        RoundLocation = new ArrayList<Cls_ManLocation>();
        RoundLocation.clear();


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_ManLocation obj = new Cls_ManLocation();
                    obj.setManNo(c1.getString(c1
                            .getColumnIndex("ManNo")));
                    obj.setTr_date(c1.getString(c1
                            .getColumnIndex("Tr_date")));
                    obj.setTr_time(c1.getString(c1
                            .getColumnIndex("Tr_time")));
                    obj.setX(c1.getString(c1
                            .getColumnIndex("X")));
                    obj.setY(c1.getString(c1
                            .getColumnIndex("Y")));
                    obj.setLoct(c1.getString(c1
                            .getColumnIndex("Loct")));
                    obj.setV_OrderNo(c1.getString(c1
                            .getColumnIndex("V_OrderNo")));
                    obj.setNo(c1.getString(c1
                            .getColumnIndex("no")));
                    RoundLocation.add(obj);
                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(RoundLocation);


        CallWebServices ws = new CallWebServices(context);
        Result = ws.SaveManLoactios(json);
        try {
            if (Result > 0) {
                query = " Update  ManLocation  set Posted='1'  where Posted = '-1' and Tr_date = '" + currentDateandTime + "'";
                sqlHandler.executeQuery(query);
                query = " delete from   ManLocation   where Posted ='1'  and Tr_date <= '" + currentDateandTime + "'";
            }
        } catch (final Exception e) {
        }

        return 1;
    }


}

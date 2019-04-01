package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;

import com.cds_jo.GalaxySalesApp.SqlHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Get_Amt_Tafkeed {
    Context context;
    String query;
    SqlHandler sqlHandler;

    public Get_Amt_Tafkeed(Context conText) {
        context = conText;
        sqlHandler = new SqlHandler(conText);

    }
    public String DoTrans(  String Amt) throws JSONException {
         String Result ="";
        CallWebServices ws = new CallWebServices(context);
        Result = ws.Tafkeet(Amt);
        JSONObject js = new JSONObject(Result);
        JSONArray js_Tafkeet= js.getJSONArray("Tafkeet");
        try {
            if (Result !="") {
                Result= js_Tafkeet.get(0).toString() ;
            }
        } catch (final Exception e) {
            Result= "";
        }
        return Result;
    }


}

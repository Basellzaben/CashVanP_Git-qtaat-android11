package com.cds_jo.GalaxySalesApp.assist.Logtrans;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;


import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;



public class InsertLogTrans  {
    Context c;
    String   UserID;
    String  TabletId;
    String   Currentdate;
      public InsertLogTrans(Context context, String SCR_NO, int ActionNo,String OrderNo  ,String CustNo,String Notes) {
try {
    c = context;

                  if (CustNo.equalsIgnoreCase("")){
                      CustNo= "-1";
                  }


                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                UserID = sharedPreferences.getString("UserID", "");
                TabletId = Settings.Secure.getString(context.getContentResolver(), "bluetooth_name");
                TabletId = TabletId + " (" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + ")";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
                Currentdate = sdf.format(new Date());


                String Battery_level = get_Battery();
                SqlHandler sqlHandler = new SqlHandler(context);

                ContentValues cv = new ContentValues();
                cv.put("ManNo", UserID);
                cv.put("CustNo", CustNo);
                cv.put("ScreenCode", SCR_NO);
                cv.put("ActionNo", ActionNo+"");
                cv.put("TransNo", OrderNo);
                cv.put("Trans_Date", Currentdate);
                cv.put("TabletId", TabletId);
                cv.put("BattryCharge", Battery_level);
                cv.put("Notes", Notes);
                cv.put("Posted", "-1");
                sqlHandler.Insert("ManLogTrans", null, cv);

                PostLogTrans nv = new PostLogTrans(context);
             sqlHandler.executeQuery("delete from   ManLogTrans ");
        }catch (Exception sd){}

    }
    public  String get_Battery() {
        String  getBattery ="";
        try {


            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = c.registerReceiver(null, ifilter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level / (float)scale;
            float p = batteryPct * 100;
            getBattery=  String.valueOf(Math.round(p));


        } catch (Exception ex) {

        }
        return   getBattery;
    }

}
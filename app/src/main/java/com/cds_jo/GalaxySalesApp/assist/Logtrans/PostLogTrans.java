package com.cds_jo.GalaxySalesApp.assist.Logtrans;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PostLogTrans {
    Context c;
    String   no,ManNo,CustNo,ScreenCode,ActionNo,TransNo,Trans_Date,TabletId,BattryCharge,Notes;
    SqlHandler sqlHandler;
      public PostLogTrans(Context context) {

          c = context;
          DoPost();
      }
      private void DoPost() {
         try {


             String query = " SELECT  Distinct  no,ManNo,CustNo,ScreenCode,ActionNo,TransNo,Trans_Date,TabletId,BattryCharge,Notes" +
                     " FROM ManLogTrans     where  ifnull(Posted,'-1')='-1'";

             no=ManNo=CustNo=ScreenCode=ActionNo=TransNo=Trans_Date=TabletId=BattryCharge=Notes="";
               sqlHandler = new SqlHandler(c);
             Cursor c1 = sqlHandler.selectQuery(query);

             if (c1 != null && c1.getCount() > 0) {
                 c1.moveToFirst();
                 no = c1.getString(c1.getColumnIndex("no"));
                 ManNo = c1.getString(c1.getColumnIndex("ManNo"));
                 CustNo = c1.getString(c1.getColumnIndex("CustNo"));
                 ScreenCode = c1.getString(c1.getColumnIndex("ScreenCode"));
                 ActionNo = c1.getString(c1.getColumnIndex("ActionNo"));
                 TransNo = c1.getString(c1.getColumnIndex("TransNo"));
                 Trans_Date = c1.getString(c1.getColumnIndex("Trans_Date"));
                 TabletId = c1.getString(c1.getColumnIndex("TabletId"));
                 BattryCharge = c1.getString(c1.getColumnIndex("BattryCharge"));
                 Notes = c1.getString(c1.getColumnIndex("Notes"));
                 c1.close();
             }

        if (!ManNo.equalsIgnoreCase("")) {
                 new PostAtten().execute(ManNo, CustNo, ScreenCode, ActionNo, TransNo, Trans_Date, TabletId, BattryCharge, Notes);
             }
         }catch ( Exception re){
             Toast.makeText(c,"يجب تحديث البيانات",Toast.LENGTH_SHORT).show();
         }
    }

         private class PostAtten extends AsyncTask<String, Void, Void> {
        final ProgressDialog progressDialog = new ProgressDialog(c);

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                CallWebServices ws = new CallWebServices(c);
                ws.PostManTrans(params[0].toString(), params[1].toString(), params[2].toString(), params[3].toString(), params[4].toString(), params[5].toString(), params[6].toString(), params[7].toString(), params[8].toString());
            }catch (Exception s){}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sqlHandler.executeQuery("update  ManLogTrans set Posted ='1' where no ='"+no+"'");
            DoPost();


        }

    }
}
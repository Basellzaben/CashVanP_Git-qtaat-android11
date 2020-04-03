package com.cds_jo.GalaxySalesApp.ManLocation;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.Cls_HttpGet_Location;
import com.cds_jo.GalaxySalesApp.Cls_ManLocation;
import com.cds_jo.GalaxySalesApp.GPSService;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import com.google.gson.Gson;



import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AutoPostLocation extends Service {
    public AutoPostLocation() {
    }
    public     long INTERVAL=3000;//variable for execute services every 0.5 minute
    private Handler mHandler=new Handler(); // run on another Thread to avoid crash
    private Timer mTimer=null; // timer handling
    String currentDate;
    String currentDateandTime;
    long Result =-1;
    String UserID ;


    int F ;
    String query = "";
    SqlHandler sqlHandler ;
    int M= 0 ;
    GPSService mGPSService;

     String str ="";
    Intent intent1 ;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

    }
    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
            DoTran();
                }
            });
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "onStartCommand", Toast.LENGTH_SHORT).show();
        INTERVAL = INTERVAL  *1 ;// 3 minuts
        intent1 = intent;
        // INTERVAL=5000;
        if(mTimer!=null){
            mTimer.cancel();
            Toast.makeText(getApplicationContext(), "mTimer.cancel", Toast.LENGTH_SHORT).show();
        }

        else
            mTimer=new Timer();
            mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(),0,INTERVAL);// schedule task
        return START_STICKY;

    }
    @Override
    public  void onDestroy(){
        try {
            super.onDestroy();
            Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT).show();

            super.stopService(intent1);
             mTimer.cancel();
        }catch (Exception ex ){
             Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }

    private   void DoTran(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
     Toast.makeText(getApplicationContext(), "Do_Tran       ", Toast.LENGTH_SHORT).show();
         sqlHandler = new SqlHandler(getApplicationContext());
          mGPSService= new GPSService(getApplicationContext());
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
         currentDateandTime = sdf.format(new Date());
        UserID=sharedPreferences.getString("UserID", "");
          GetlocationNew();
    }
    public void GetlocationNew() {

      //  Toast.makeText(getApplicationContext(), " Get location ", Toast.LENGTH_SHORT).show();

        mGPSService.getLocation();
        String X,Y,Loc ;
        X="";
        Y="";
        Loc="";


        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());



        if (mGPSService.isLocationAvailable == true) {
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            //   Loc = mGPSService.getLocationAddress();

            Loc ="0";

            X= String.valueOf(latitude);
            Y=String.valueOf(longitude);
        }

        ContentValues cv = new ContentValues();
        cv.put("ManNo",UserID);
        cv.put("Tr_date",currentDateandTime);
        cv.put("Tr_time",StringTime);
        cv.put("X", X);
        cv.put("Y",Y);
        cv.put("Loct",Loc);
        cv.put("V_OrderNo", "-1");
        cv.put("Posted", "-1");
        try {
            Long i = sqlHandler.Insert("ManLocation", null, cv);
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

          PostManLocstion();
    }
  /*  private void setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager conman =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(
                    iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                    .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }  */
    private String chkNetWorkStatus() {
      String GpsStatus="";
      /*   final LocationManager manager = (LocationManager)this.getSystemService    (Context.LOCATION_SERVICE );
        GpsStatus="";
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //Toast.makeText(this, "GPS is disabled!", Toast.LENGTH_LONG).show();
            GpsStatus="GPS is disabled!";
            try {
                Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
                intent.putExtra("enabled", true);
                sendBroadcast(intent);
            }catch (Exception ex ){

            }
           *//* final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);*//*
        } else {
            //Toast.makeText(this, "GPS is enabled!", Toast.LENGTH_LONG).show();
        }

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (!wifi.isConnectedOrConnecting ()) {
            wifiManager.setWifiEnabled(true);

        }if (mobile.isConnectedOrConnecting ()) {
            setMobileDataEnabled(getApplicationContext(),true);
        }*//* else {
          //  Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
            GpsStatus = GpsStatus+"  ,  No Network ";
        }*//*

        GpsStatus = "";
*/
        return  GpsStatus;
    }


    public void PostManLocstion() {
          Result =-1;
        final Handler _handler = new Handler();
        String    q = "   ";

        q = "select  distinct  ManNo,Tr_date,Tr_time,X,Y,Loct,V_OrderNo,Posted  , no " +
                "  from ManLocation   where    Posted = '-1' and Tr_date = '"+currentDateandTime+"'" ;
        Cursor c1 = sqlHandler.selectQuery(q);
        ArrayList<Cls_ManLocation> RoundLocation    ;
        RoundLocation = new ArrayList<Cls_ManLocation>();
        RoundLocation.clear();

        //query = " delete from   SaleManRounds   ";
        // sqlHandler.executeQuery(query);

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

        Calendar c = Calendar.getInstance();



        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getApplicationContext() );
                Result=  ws.SaveManLoactios(json);
                try {
                    if (Result > 0) {

                        _handler.post(new Runnable() {
                            public void run() {
                                query = " Update  ManLocation  set Posted='1'  where Posted = '-1' and Tr_date = '"+currentDateandTime+"'" ;
                                sqlHandler.executeQuery(query);
                                query = " delete from   ManLocation   where Posted ='1'  and Tr_date <= '"+currentDateandTime+"'" ;

                               // sqlHandler.executeQuery(query);

                            }
                        });


                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }

}


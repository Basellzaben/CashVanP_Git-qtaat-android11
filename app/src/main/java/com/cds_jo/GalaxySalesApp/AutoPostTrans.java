package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.PostTransActions.PostManLocations;
import com.cds_jo.GalaxySalesApp.PostTransActions.PostPayments;
import com.cds_jo.GalaxySalesApp.PostTransActions.PostSalesInvoice;
import com.cds_jo.GalaxySalesApp.PostTransActions.PostSalesOrder;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AutoPostTrans extends Service {
    public AutoPostTrans() {
    }
//
    public long INTERVAL = 12000;//300000;//10000;//60000;//variable for execute services every 1 minute
    private Handler mHandler = new Handler(); // run on another Thread to avoid crash
    private Timer mTimer = null; // timer handling
    String currentDate;
    String currentDateandTime;
    ArrayList<Cls_Sal_InvItems> contactList;
    ArrayList<ContactListItems> PoList;
    MsgNotiAutoPost noti;
    int F;
    String query = "";
    SqlHandler sqlHandler;
    int M = 0;
    Date date1, date2;
    ArrayList<Cls_Check> ChecklList;
    String str = "";
    Intent intent1;
    int dayOfWeek;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        // cancel if service is  already existed


    }

    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast at every 1 minute

                    DoTran();
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub


        Toast.makeText(getApplicationContext(), "تم تفعيل التحديث التلقائي", Toast.LENGTH_SHORT).show();
        intent1 = intent;
        M = Integer.parseInt(DB.GetValue(getApplicationContext(), "OrdersSitting", "PostDely", "1=1"));
        if (M > 0) {
            INTERVAL = M * 60000;
        } else {
            INTERVAL = 12000;
        }
        INTERVAL = 40000;
        // INTERVAL=5000;
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer(); // recreate new timer
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, INTERVAL);// schedule task
        //  return START_STICKY ;//super.onStartCommand(intent, flags, startId);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        //super.onDestroy();
        super.stopService(intent1);
        mTimer.cancel();
        Toast.makeText(getApplicationContext(), "تم ايقاف التحديث التلقائي", Toast.LENGTH_SHORT).show();

    }

    private void DoTran() {
        // Toast.makeText(getApplicationContext(), " Server   ", Toast.LENGTH_SHORT).show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        sqlHandler = new SqlHandler(getApplicationContext());
        currentDate = sdf.format(new Date());
        contactList = new ArrayList<Cls_Sal_InvItems>();
        contactList.clear();
        currentDate = sdf.format(new Date());
        PoList = new ArrayList<ContactListItems>();
        PoList.clear();
        M = Integer.parseInt(DB.GetValue(getApplicationContext(), "OrdersSitting", "PostDely", "1=1"));

        if (M <= 0) {
            M = 1;
        }
        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        SharManVisits();
        UpdateMsg();



       new SalesInvoicePosting().execute();
       new SalesOrderPosting().execute();
       new PaymentsPosting().execute();
       new ManLocationPosting().execute();
    }

    private class SalesOrderPosting extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            PostSalesOrder obj = new PostSalesOrder(getApplicationContext());
            obj.DoTrans();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


        }

    }
    private class SalesInvoicePosting extends AsyncTask<Void, Void, Void> {
       @Override
        protected void onPreExecute() {  }

        @Override
        protected Void doInBackground(Void... params) {
            PostSalesInvoice obj = new PostSalesInvoice(getApplicationContext());
            obj.DoTrans();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

        }

    }
    private class PaymentsPosting extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            PostPayments obj = new PostPayments(getApplicationContext());
            obj.DoTrans();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {


        }

    }
    private class ManLocationPosting extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            PostManLocations obj = new PostManLocations(getApplicationContext());
            obj.DoTrans();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        }

    }


    private Date ConvertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }
    private Long CalcMin(Date date2, Date date1) {
        long diffMinutes;
        try {
            long diff = date2.getTime() - date1.getTime();
            long diffSeconds = diff / 1000 % 60;
            diffMinutes = diff / (60 * 1000);// % 60;
        } catch (Exception ex) {
            diffMinutes = 0;
        }
        return diffMinutes;
    }
   //////////////////////////////////////////////////////////
    public void SharManVisits() {


        final Handler _handler = new Handler();
        String q = " delete from   SaleManRounds   where Posted =1  ";


        sqlHandler.executeQuery(q);


        q = "select  distinct no,ManNo, CusNo, DayNum ,Tr_Data ,Start_Time,End_Time, Duration,OrderNo " +
                "  from SaleManRounds   where    Closed !=0   AND  Posted = -1";
        Cursor c1 = sqlHandler.selectQuery(q);
        ArrayList<Cls_SaleManDailyRound> RoundList;
        RoundList = new ArrayList<Cls_SaleManDailyRound>();
        RoundList.clear();

        //query = " delete from   SaleManRounds   ";
        // sqlHandler.executeQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_SaleManDailyRound cls_saleManDailyRound = new Cls_SaleManDailyRound();
                    cls_saleManDailyRound.setNo(c1.getString(c1
                            .getColumnIndex("no")));
                    cls_saleManDailyRound.setManNo(c1.getString(c1
                            .getColumnIndex("ManNo")));
                    cls_saleManDailyRound.setCusNo(c1.getString(c1
                            .getColumnIndex("CusNo")));
                    cls_saleManDailyRound.setDayNum(c1.getString(c1
                            .getColumnIndex("DayNum")));
                    cls_saleManDailyRound.setTr_Data(c1.getString(c1
                            .getColumnIndex("Tr_Data")));
                    cls_saleManDailyRound.setStart_Time(c1.getString(c1
                            .getColumnIndex("Start_Time")));
                    cls_saleManDailyRound.setEnd_Time(c1.getString(c1
                            .getColumnIndex("End_Time")));

                    cls_saleManDailyRound.setDuration(c1.getString(c1
                            .getColumnIndex("Duration")));

                    cls_saleManDailyRound.setOrderNo(c1.getString(c1
                            .getColumnIndex("OrderNo")));

                    RoundList.add(cls_saleManDailyRound);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(RoundList);

        Calendar c = Calendar.getInstance();
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getApplicationContext());
                ws.SaveManVisits(json);
                try {
                    if (We_Result.ID > 0) {
                        query = " Update  SaleManRounds  set Posted=1  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {

                            }
                        });

                        query = " delete from   SaleManRounds   where Posted =1";

                        sqlHandler.executeQuery(query);
                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }
    //////////////////////////////////////////////////////////
    private void UpdateMsg() {
        // Toast.makeText(this,"Update" ,Toast.LENGTH_SHORT).show();
        final Handler _handler = new Handler();

        final SqlHandler sqlHandler;
        sqlHandler = new SqlHandler(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getApplicationContext());

                ws.GetCustomersMsg(UserID);
                try {
                    Integer i;
                    String q;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Cusno = js.getJSONArray("Cusno");
                    JSONArray js_msg = js.getJSONArray("msg");
                    JSONArray js_Man_No = js.getJSONArray("Man_No");
                    JSONArray js_SaleManFlg = js.getJSONArray("SaleManFlg");


                    q = "Delete from CustomersMsg";
                    sqlHandler.executeQuery(q);
                    q = " delete from sqlite_sequence where name='CustomersMsg'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_Cusno.length(); i++) {
                        q = "INSERT INTO CustomersMsg(Cusno,msg,Man_No,SaleManFlg) values ('"
                                + js_Cusno.get(i).toString()
                                + "','" + js_msg.get(i).toString()
                                + "'," + js_Man_No.get(i).toString() + "," + js_SaleManFlg.get(i).toString() + ")";
                        sqlHandler.executeQuery(q);

                    }
                    final int total = i;
                    _handler.post(new Runnable() {
                        public void run() {
                            //    Call_marque();
                        }
                    });
                } catch (final Exception e) {

                    _handler.post(new Runnable() {
                        public void run() {
                            // Call_marque();
                        }
                    });
                }
            }
        }).start();

    }
}


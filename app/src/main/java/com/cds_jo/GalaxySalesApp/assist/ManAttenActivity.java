package com.cds_jo.GalaxySalesApp.assist;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.Cls_SaleManDailyRound;

import com.cds_jo.GalaxySalesApp.GPSService;
import com.cds_jo.GalaxySalesApp.GetLocation;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.Logtrans.InsertLogTrans;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.zebra.zq110.ZQ110;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import Methdes.MyTextView;
import Methdes.MyTextView_Digital;
import cn.pedant.SweetAlert.SweetAlertDialog;
import header.Header_Frag;


public class ManAttenActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView tv;
    String SCR_NO ="11029";
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    ImageView imglogo;
    Double Lat = 0.0;
    Double Lng = 0.0;
    Button btn_print, scanBtn;
    RelativeLayout button10;
    LocationManager locationmanager;
    private TextView contentTxt;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print = new ESCPSample3();
    int dayOfWeek;
    SqlHandler sqlHandler;
    String DeviceDate,js_SERVERDATE, js_SERVERTIME, js_MYEAR, js_MMONTH, js_MDAY, js_MHOUR, js_MMINUTE, js_MSECOND, js_DAYWEEK;
    SweetAlertDialog pDialog ;
    Date ServrTime;
    int GetTime = 0;
    String Week_Num = "1";
    private GoogleMap mMap;
    ArrayList<Cls_SaleManDailyRound> RoundList;
    public ProgressDialog loadingdialog;
    static ZQ110 mZQ110;
    boolean isGPSEnabled = false;
    private Timer timer;
    ListView Att_List;
    Thread t;
    long Result;
    private static final long TIME = 30000;
    // Minimum distance fluctuation for next update (in meters)
    private static final long DISTANCE = 20;
    TextView et_Notes;
    MyTextView_Digital TrDate,   et_ServerTime;

    TextView tv_x, tv_y,tv_Loc;
    Methdes.MyTextView et_Day, tv_y1, tv_location;
    ImageView imageButton4;
    int Isopen = 0;
    // Declaring a Location Manager
    Calendar CalnederServerTime;
    Calendar CalnederVisitStartTime ;
    SimpleDateFormat ServerFormat;
    String GpsStatus = "";
    String TabDate;
    String url;
    Document doc;
    String[] tags;
    Elements elements;
    String Unix_time ;
    String[] split;
    Intent BackInt;
    String UserID,UserName,COMPUTERNAME;
    TextView CustNm ;

    ArrayList<Cls_Att> ArrayAtts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.man_atten);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
              UserID =  sharedPreferences.getString("UserID", "");
              UserName =  sharedPreferences.getString("UserName", "");
              CustNm = (TextView) findViewById(R.id.tv_CustName);
              CustNm.setText(UserName);
            COMPUTERNAME= Settings.Secure.getString(this.getContentResolver(), "bluetooth_name"  );
            COMPUTERNAME=COMPUTERNAME+" (" + Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID  )+")";
            CalnederServerTime  = Calendar.getInstance();
            CalnederVisitStartTime  = Calendar.getInstance();

            ServerFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            TabDate = sdf.format(new Date());
            Att_List = (ListView) findViewById(R.id.Att_List);
            et_Day = (MyTextView) findViewById(R.id.et_Day);
            et_ServerTime = (MyTextView_Digital) findViewById(R.id.et_ServerTime);

            ArrayAtts = new ArrayList<Cls_Att>();
            Calendar c = Calendar.getInstance();
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            et_Day.setText(GetDayName(dayOfWeek ) );




            TrDate = (MyTextView_Digital) findViewById(R.id.et_Date);
            TrDate.setText(TabDate);
            // TrDate.setText(TabDate);
            UpdateTimeEverySecond();


            imageButton4 = (ImageView) findViewById(R.id.imageButton4);
            imageButton4.setVisibility(View.VISIBLE);
            button10 = (RelativeLayout) findViewById(R.id.button10);
            RoundList = new ArrayList<Cls_SaleManDailyRound>();
            RoundList.clear();

            contentTxt = (TextView) findViewById(R.id.scan_content);
            RelativeLayout scanBtn = (RelativeLayout) findViewById(R.id.scan_button);





            et_Notes = (EditText) findViewById(R.id.tv_Notes);



            CustNm.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));

            RelativeLayout btn_Save_Location = (RelativeLayout) findViewById(R.id.btn_Save_Location);
            tv_x = (TextView) findViewById(R.id.tv_x);
            tv_y = (TextView) findViewById(R.id.tv_y);


            tv_y1 = (Methdes.MyTextView) findViewById(R.id.tv_y1);


            tv_location = (Methdes.MyTextView) findViewById(R.id.tv_location);
             tv_Loc = (TextView) findViewById(R.id.tv_Loc);

            sqlHandler = new SqlHandler(this);

            js_SERVERDATE="";

            sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            DeviceDate = sdf.format(new Date());
            getUnixTime(-1);
            try {
                GetlocationNew();
            } catch (Exception ex) {
            }




            Fragment frag = new Header_Frag();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng sydney = new LatLng(Lat, Lng);
                    googleMap.addMarker(new MarkerOptions().title("Paris").position(sydney));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.animateCamera(zoom);
                }
            });

        } catch (Exception ex) {
            new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)

                    .setContentText(ex.getMessage().toString())
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
        }
        GetData();
    }
    public void GetData() {
        ArrayAtts.clear();
        ShowList();
        tv = new TextView(ManAttenActivity.this);
        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

        final Handler _handler = new Handler();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(this);
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("الإجازات");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        String MaxSeer = "1";


        final String Ser = "1";


        new Thread(new Runnable() {
            @Override
            public void run() {




                CallWebServices ws = new CallWebServices(ManAttenActivity.this);
                ws.GetSalesManAtt(UserID );
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID = js.getJSONArray("ID");
                    JSONArray js_UserID= js.getJSONArray("UserID");
                    JSONArray js_ActionNo = js.getJSONArray("ActionNo");
                    JSONArray js_ActionDate = js.getJSONArray("ActionDate");
                    JSONArray js_ActionTime = js.getJSONArray("ActionTime");
                    JSONArray js_Coor_X = js.getJSONArray("Coor_X");
                    JSONArray js_Coor_Y = js.getJSONArray("Coor_Y");
                    JSONArray js_ManAddress = js.getJSONArray("ManAddress");
                    JSONArray js_Notes= js.getJSONArray("Notes");
                    JSONArray js_Img = js.getJSONArray("Img");
                    JSONArray js_BattryLevel = js.getJSONArray("BattryLevel");
                    JSONArray js_TabletName = js.getJSONArray("TabletName");
                    JSONArray js_DayNo= js.getJSONArray("DayNo");
                    JSONArray js_DayNm= js.getJSONArray("DayNm");


                    /*q = "Delete from Man_Vac";
                    sql_Handler.executeQuery(q);
                    q = "delete from sqlite_sequence where name='Man_Vac'";
                    sql_Handler.executeQuery(q);*/
                    Cls_Att obj;
                    for (i = 0; i < js_ID.length(); i++) {
                        obj=new Cls_Att();
                        obj.setActionDate(js_ActionDate.get(i).toString());
                        obj.setDayNm(js_DayNm.get(i).toString());
                        obj.setActionTime(js_ActionTime.get(i).toString());
                        obj.setActionNo(js_ActionNo.get(i).toString());
                        obj.setNotes(js_Notes.get(i).toString());
                        obj.setBattryLevel(js_BattryLevel.get(i).toString());
                        ArrayAtts.add(obj);


                        custDialog.setMax(js_ID.length());
                        custDialog.incrementProgressBy(1);
                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();
                            ShowList();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ManAttenActivity.this).create();
                            alertDialog.setTitle("تحديث الإجازات");
                            alertDialog.setMessage("تمت عملية التحديث بنجاح");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                            // alertDialog.show();


                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                   ManAttenActivity.this).create();
                            alertDialog.setTitle("تحديث الإجازات");
                            alertDialog.setMessage("لا يوجد بيانات");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            // alertDialog.show();
                        }
                    });
                }
            }
        }).start();
    }
    public void ShowList(){
        AttAdapter attAdapter = new AttAdapter(
                this, ArrayAtts);
        Att_List.setAdapter(attAdapter);
   }

    private class PostAtten extends AsyncTask<String, Void, Void> {
        final ProgressDialog progressDialog = new ProgressDialog(ManAttenActivity.this);

        @Override
        protected void onPreExecute() {

            tv = new TextView(ManAttenActivity.this);
            lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);



            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على حفظ البيانات  ");
            tv.setText("الدوام");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            CallWebServices ws = new CallWebServices(ManAttenActivity.this);
            Result = ws.PostManAtten("-1",UserID,params[0],TrDate.getText().toString(),et_ServerTime.getText().toString(),tv_x.getText().toString(),tv_y.getText().toString(),tv_Loc.getText().toString(),
                    et_Notes.getText().toString(),"",params[1],COMPUTERNAME,dayOfWeek+"",et_Day.getText().toString());


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            et_Notes.setText("");


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ManAttenActivity.this);

            alertDialog.setMessage("تمت العملية الحفظ بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setTitle("الدوام" );
            if (Result == -5 ) {

                new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("العملية لم تتم بنجاح")
                        .setContentText(We_Result.Msg)
                        .setCustomImage(R.drawable.error_new)
                        .setConfirmText("رجــــوع")
                        .show();


            }
            else if (Result == -2) {
                alertDialog.setTitle("الدوام");
                alertDialog.setMessage(We_Result.Msg);
                alertDialog.setIcon(R.drawable.error_new);


                new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("العملية لم تتم بنجاح")
                        .setContentText(We_Result.Msg)
                        .setCustomImage(R.drawable.error_new)
                        .setConfirmText("رجــــوع")
                        .show();


            } else if (Result > 0) {

                GetData();

                new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("الدوام" )
                        .setContentText("تمت العملية  بنجاح ")
                        .setConfirmText("رجــــوع")
                        .show();

            } else {

                new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("الدوام" )
                        .setContentText("العملية  لم تتم بنجاح")
                        .setCustomImage(R.drawable.error_new)
                        .setConfirmText("رجــــوع")
                        .show();



            }
            progressDialog.dismiss();
            //alertDialog.show();

        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            Lat = 0.0;
            Lng = 0.0;

            if (!tv_x.getText().toString().equalsIgnoreCase(""))
                Lat = Double.parseDouble(tv_x.getText().toString());

            if (!tv_y.getText().toString().equalsIgnoreCase(""))
                Lng = Double.parseDouble(tv_y.getText().toString());

            if (Lat > 0 && Lng > 0) {
                ShowMap(Lat, Lng);
            }
        } catch (Exception ex) {
        }

    }
    private void ShowMap(Double Lat, Double Long) {

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                }
            });
        }




        mMap.setMapType(2);

        if (Lat > 0 && Long > 0) {
            LatLng sydney = new LatLng(Lat, Long);
            // LatLng sydney = new LatLng(Lat,Long);
            try {
                mMap.clear();

            }catch (Exception ex){

            }
            try {
                  sydney = new LatLng(Lat, Long);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                //
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.addMarker(new MarkerOptions().position(sydney).title(tv_Loc.getText().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(zoom);

            }catch (Exception ex){
                // Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class UpdateClock extends AsyncTask<String, Void, Void> {
        String flag;

        final ProgressDialog custDialog = new ProgressDialog(ManAttenActivity.this);
        @Override
        protected void onPreExecute() {
            pDialog = new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("العمل جاري على تحديث الوقت ، الرجاء الانتظار");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(String ... params) {
            flag = params[0];

                Unix_time="00:00:00";
                 elements=null;
                 doc = null;
                tags = new String[]{
                        "span[id=smalltime]"
                };
                url = "https://time.is/Unix_time_now";

            try {
                doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

            } catch (Exception ex) {
                ManAttenActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        GetTime = 0;
                        new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setContentText("حدث خطأ اثناء عملية تحديث الوقت")
                                .setCustomImage(R.drawable.error_new)
                                .setConfirmText("رجــــوع")
                                .show();
                        pDialog.dismiss();
                    }
                });

                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                pDialog.dismiss();
                elements = doc.select(tags[0]);

                for (int i = 0; i < tags.length; i++) {
                    elements = elements.select(tags[i]);
                    GetTime=1;
                }

                Unix_time = elements.text().substring(0, 8);

                if (Unix_time.equalsIgnoreCase("00:00:00") ||  GetTime == 0 ) {
                    new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)

                            .setContentText("حصل خطأ اثناء تحديث الوقت")
                            .setCustomImage(R.drawable.error_new)
                            .setConfirmText("رجــــوع")
                            .show();
                    GetTime = 0;
                } else {
                    GetTime = 1;
                    et_ServerTime.setText(Unix_time);
                    split = Unix_time.split(":");
                    String H = split[0];
                    String M = split[1];
                    String S = split[2];
                    CalnederServerTime.set(Integer.parseInt(js_MYEAR), Integer.parseInt(js_MMONTH), Integer.parseInt(js_MDAY), Integer.parseInt(H), Integer.parseInt(M), Integer.parseInt(S));



                    if (Unix_time.equalsIgnoreCase("00:00:00")) {
                        GetTime = 0;

                        CalnederServerTime.set(Integer.parseInt(js_MYEAR), Integer.parseInt(js_MMONTH), Integer.parseInt(js_MDAY), 0, 0, 0);
                    }



                }
                  }catch(Exception Ex){

                }


        }

    }
    private String getUnixTime( final int f)   {



        final Handler _handler = new Handler();
        try {
            Unix_time="00:00:00";

            tags = new String[]{
                    "span[id=smalltime]"
            };
            url = "https://time.is/Unix_time_now";
            new   Thread(new Runnable() {
                @Override
                public void run () {
                    try {
                        doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
                        elements = doc.select(tags[0]);
                        for (int i = 0; i < tags.length; i++) {
                            elements = elements.select(tags[i]);
                        }
                        Unix_time = elements.text().substring(0, 8);
                        _handler.post(new Runnable() {
                            public void run() {
                                GetTime=1;
                                et_ServerTime.setText(Unix_time);
                                split = Unix_time.split(":");
                                String H = split[0];
                                String M = split[1];
                                String S = split[2];

                                Toast.makeText(ManAttenActivity.this, "الوقت في عمّان - الاردن :"+Unix_time,Toast.LENGTH_SHORT).show();



                            }
                        });

                    } catch (final Exception e) { }
                }
            }).start();


        }catch ( Exception ex){
            GetTime=0;
            new SweetAlertDialog(ManAttenActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)

                    .setContentText(ex.getMessage().toString())
                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
        }


        if(Unix_time.equalsIgnoreCase("00:00:00")){
            GetTime=0;

        }

        return  (    Unix_time );
    }




    private void   UpdateTimeEverySecond(){
    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTextView();
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };

    t.start();
}
    private void updateTextView() {
       if (GetTime==0){
            et_ServerTime.setText("00:00:00");
            return;
        }

        Date date=null;


        try {
            CalnederServerTime.add(Calendar.SECOND,1);
            String StringTime = ServerFormat.format(CalnederServerTime.getTime());
            et_ServerTime.setText(StringTime);

        } catch (Exception e) {

        }
    }

    public  String get_Battery() {
        String  getBattery ="";
        try {


            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level / (float)scale;
            float p = batteryPct * 100;
            getBattery=  String.valueOf(Math.round(p));


        } catch (Exception ex) {

        }
        return   getBattery;
    }
    public void GetlocationNew() {
        String result;
        String address = "";
               final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);
         tv_Loc = (TextView) findViewById(R.id.tv_Loc);


        try {
            GetLocation mGPSService = new GetLocation();
            Location l =   mGPSService.CurrentLocation(ManAttenActivity.this);
            double latitude = l.getLatitude();
            double longitude = l.getLongitude();

            tv_x.setText(String.valueOf(latitude));
            tv_y.setText(String.valueOf(longitude));

         /*  Cls_HttpGet_Location task = new Cls_HttpGet_Location();

            result = task.execute(tv_x.getText().toString(), tv_y.getText().toString()).get();
            tv_Loc.setText(result);
            if(tv_Loc.getText().toString().equalsIgnoreCase("fail")){
                Getlocation();
            }*/
            address=  GetStreetName();
            tv_Loc.setText(address);
            if(tv_Loc.getText().toString().equalsIgnoreCase("fail") ||address.equalsIgnoreCase("") ){
                Getlocation();
            }
        } catch (Exception ex) {
            Getlocation();
           /* if(!GpsStatus.equalsIgnoreCase("")) {
                tv_Loc.setText(GpsStatus);
            }else
                tv_Loc.setText("الموقع غير معروف.");*/
        }


    }
    public  String GetStreetName() {
        String  StreetName ="";
        try {


            Geocoder myLocation = new Geocoder(this, Locale.getDefault());
            List<Address> myList = null;
            try {
                myList = myLocation.getFromLocation(Double.parseDouble(tv_x.getText().toString()), Double.parseDouble(tv_y.getText().toString()), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address1 = (Address) myList.get(0);
            String addressStr = "";
            addressStr += address1.getAddressLine(0) + ", ";
            addressStr += address1.getAddressLine(1) + ", ";
            addressStr += address1.getAddressLine(2);
            StreetName = addressStr.replace("null", "").replace(",", "") ;


        } catch (Exception ex) {
            StreetName="";
             //  Toast.makeText(this, ex.getMessage().toString() + "  GetStreetName", Toast.LENGTH_SHORT).show();
        }
        return   StreetName;
    }
    public void Getlocation() {


        String address = "";

        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);
         tv_Loc = (TextView) findViewById(R.id.tv_Loc);
        contentTxt = (TextView) findViewById(R.id.scan_content);
try {

    GPSService mGPSService = new GPSService(this);
    mGPSService.getLocation();

    if (mGPSService.isLocationAvailable == false) {
        return;
    } else {
        double latitude = mGPSService.getLatitude();
        double longitude = mGPSService.getLongitude();
        address = mGPSService.getLocationAddress();
        try {
             tv_x.setText(String.valueOf(latitude));
            tv_y.setText(String.valueOf(longitude));
            if (address.contains("IO Exception")) {
                if(!GpsStatus.equalsIgnoreCase("")) {
                    tv_Loc.setText(GpsStatus);
                }else
                    tv_Loc.setText("الموقع غير معروف  .");

            } else {
                tv_Loc.setText(address);
            }

        } catch (Exception ex) {
          /*  tv_x.setText("0.0");
            tv_y.setText("0.0");*/
        }

    }
        }catch ( Exception ex ){

        }
    }





    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) ManAttenActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    static  int id = 1;


    public void btn_StartRound(View view) {
        SaveLog(16);
        new PostAtten().execute("1",get_Battery() ) ;

    }

    private  void SaveLog(int Action){

        InsertLogTrans obj=new InsertLogTrans(ManAttenActivity.this,SCR_NO , Action,"","","");


    }
    public void btn_EndRound(View view) {
        SaveLog(17);
        new PostAtten().execute("2",get_Battery() ) ;

    }


    public  String GetDayName(int Day){
        String DayNm = "";
        if (Day == 1) DayNm = "الاحد";
        else if (Day == 2) DayNm = "الاثنين";
        else if (Day == 3) DayNm = "الثلاثاء";
        else if (Day == 4) DayNm = "الاربعاء";
        else if (Day == 5) DayNm = "الخميس";
        else if (Day == 6) DayNm = "الجمعة";
        else if (Day == 7) DayNm = "السبت";
        return DayNm;
    }
    public void BtnScan(View view) {
        if (view.getId() == R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
         /*   String address = "";
            GPSService mGPSService = new GPSService(view.getContext());
            mGPSService.getLocation();*/

        }


    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();


            contentTxt = (TextView) findViewById(R.id.scan_content);


            contentTxt.setText("");



            if (scanningResult.getContents() != null) {
                contentTxt.setText( scanContent.toString());


            }
            //contentTxt.setText( "6253803400018");
            //  GetCustomer();


        }
        // GetCustomer();
    }
    public void btn_GetLocation(View view) {
        try {
            GetlocationNew();
        }catch ( Exception ex){}

        //Getlocation();

    }


    public void btn_delete(View view) {


        sqlHandler.Delete("SaleManRounds", "");



    }
    public void btn_Share(View view) {


        sqlHandler = new SqlHandler(this);
        String query = "  select Po_Hdr.orderno as Po_Order  , s.no as no ,s.ManNo as ManNo, s.CusNo as CusNo , s.DayNum as DayNum,s.Tr_Data as Tr_Data ," +
                        " s.Start_Time as Start_Time ,s.End_Time as End_Time, s.Duration as Duration , s.VisitType1 as VisitType1, " +
                        " s.VisitType2 as VisitType2 ,s.VisitType3 as VisitType3 ,s.VisitType4 as VisitType4  ,s.X as X,s.Y as Y ,s.Locat as  Locat" +
                        " , s.OrderNo as  OrderNo from SaleManRounds  s " +
                        " Left join  Po_Hdr on Po_Hdr.V_OrderNo = s.OrderNo   where s.Posted = -1";
        Cursor c1 = sqlHandler.selectQuery(query);
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

                    RoundList.add(cls_saleManDailyRound);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(RoundList);

        Calendar c = Calendar.getInstance();

        loadingdialog = ProgressDialog.show(ManAttenActivity.this, "الرجاء الانتظار ...", "العمل جاري على ترحيل زيارات المندوب", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(ManAttenActivity.this);
                ws.SaveManVisits(json);
                try {

                    if (We_Result.ID > 0) {

                        String query = " Update  SaleManRounds  set Posted=1  where Posted = '-1'";
                        sqlHandler.executeQuery(query);

                        query = " delete from   SaleManRounds   where Posted =1 and  DayNum < " + dayOfWeek;
                        sqlHandler.executeQuery(query);


                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        ManAttenActivity.this).create();
                                alertDialog.setTitle("ترحيل زيارات المندوب ");
                                alertDialog.setMessage("تمت عملية ترحيل زيارات العميل بنجاح");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();

                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        ManAttenActivity.this).create();
                                if (We_Result.ID == 0) {
                                    alertDialog.setMessage("لا يوجد زيارات غير مرحلة");

                                } else {
                                    alertDialog.setMessage("عملية الترحيل لم تتم بنجاح " + "    ");

                                }

                                alertDialog.setTitle(" عملية الترحيل لم تتم بنجاح"+ "   " + We_Result.ID+"");
                                //  alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                            }
                        });
                    }

                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ManAttenActivity.this).create();
                            alertDialog.setTitle("فشل في عمليه الاتصال");
                            alertDialog.setMessage(e.getMessage().toString());
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertDialog.show();
                        }
                    });
                }
            }
        }).start();
    }
    public void btm_delete(View view) {
        sqlHandler = new SqlHandler(this);

        String   query = " delete from   SaleManRounds   ";
        sqlHandler.executeQuery(query);

    }

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), JalMasterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void btn_back(View view) {

        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), JalMasterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



    public void btn_RefreshTime(View view) {

        getUnixTime(-1);
    }

    private  void ShareVisitNew(){

        String query = "  select  Po_Hdr.orderno as Po_Order ,s.Notes  , s.no as no ,s.ManNo as ManNo, s.CusNo as CusNo , s.DayNum as DayNum,s.Tr_Data as Tr_Data ," +
                " s.Start_Time as Start_Time ,s.End_Time as End_Time, s.Duration as Duration , s.VisitType1 as VisitType1, " +
                " s.VisitType2 as VisitType2 ,s.VisitType3 as VisitType3 ,s.VisitType4 as VisitType4  ,s.X as X,s.Y as Y ,s.Locat as  Locat" +
                " , s.OrderNo as  OrderNo from SaleManRounds  s " +
                " Left join  Po_Hdr on Po_Hdr.V_OrderNo = s.OrderNo   where (s.Posted = -1  )  order by s.no desc  Limit 1 ";

        String    COMPUTERNAME= "";


        Cursor c1 = sqlHandler.selectQuery(query);

        if(c1!=null && c1.getCount()>0)
        {
            Cls_SaleManDailyRound cls_saleManDailyRound = new Cls_SaleManDailyRound();
            c1.moveToFirst();
            cls_saleManDailyRound.setNo(c1.getString(c1.getColumnIndex("no")));
            cls_saleManDailyRound.setManNo(c1.getString(c1.getColumnIndex("ManNo")));
            cls_saleManDailyRound.setCusNo(c1.getString(c1.getColumnIndex("CusNo")));
            cls_saleManDailyRound.setDayNum(c1.getString(c1.getColumnIndex("DayNum")));
            cls_saleManDailyRound.setTr_Data(c1.getString(c1.getColumnIndex("Tr_Data")));
            cls_saleManDailyRound.setStart_Time(c1.getString(c1.getColumnIndex("Start_Time")));
            cls_saleManDailyRound.setEnd_Time(c1.getString(c1.getColumnIndex("End_Time")));
            cls_saleManDailyRound.setDuration(c1.getString(c1.getColumnIndex("Duration")));
            c1.close();
            Do_share_Visits(cls_saleManDailyRound);

        }
    }
    public void Do_share_Visits( final Cls_SaleManDailyRound obj) {
        final String str;
        loadingdialog = ProgressDialog.show(this, "الرجاء الانتظار ...",    "العمل جاري على اعتماد الدوام رقم :" + obj.getOrderNo(), true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.dismiss();
      //  loadingdialog.show();
        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getApplicationContext());
                long i =   ws.SaveManVisitsNew(obj );
                try {
                    if (i> 0) {
                        String query = " Update  SaleManRounds  set Posted='"+ We_Result.ID+"'  where OrderNo ='"+obj.getOrderNo() +"'";
                        sqlHandler.executeQuery(query );

                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();

                                ShareVisitNew();
                            }
                        });
                    } else {
                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();




                            }
                        });
                    }
                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                  getApplicationContext()).create();
                            alertDialog.setTitle("إعتماد الزيارات");
                            alertDialog.setMessage("عملية اعتماد الزيارات لم تتم بنجاح، الرجاء المحاولة لاحقا");            // Setting Icon to Dialog

                            alertDialog.setIcon(R.drawable.error_new);
                            alertDialog.setButton("رجـــوع", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                            loadingdialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }
}

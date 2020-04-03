package com.cds_jo.GalaxySalesApp.Tracking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.Cls_ManLocationReport;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_ManLocation_Adapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


//import com.loopj.android.http.HttpGet;// temp

public class ManLocationsReport extends Activity {
    ListView items_Lsit;
    public ProgressDialog loadingdialog;
    SqlHandler sqlHandler;
    private ArrayList<LatLng> traceOfMe = null;
    private Polyline mPolyline = null;
    ArrayList<Cls_ManLocationReport> LocationList;
    DecimalFormat Per;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_location_report);

        LocationList = new ArrayList<Cls_ManLocationReport>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        items_Lsit = (ListView) findViewById(R.id.lst_acc);

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_ManLocationReport o = (Cls_ManLocationReport) items_Lsit.getItemAtPosition(position);
                ShowMap(o.getX(), o.getY(),o.getManNo());

            }
        });


        onProgressUpdate();
    }

    private void ShowMap(String x, String y, String Man) {
        Intent k = new Intent(this, MapsActivity.class);
        k.putExtra("Lat", x);
        k.putExtra("Lon", y);
        k.putExtra("Man", Man);
        startActivity(k);
    }
    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
    public void onProgressUpdate() {


        final List<String> items_ls = new ArrayList<String>();
        items_Lsit = (ListView) findViewById(R.id.lst_acc);
        items_Lsit.setAdapter(null);
        LocationList.clear();
        final Handler _handler = new Handler();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(ManLocationsReport.this);

        custDialog.setTitle("الرجاء الانتظار");
        custDialog.setMessage("العمل جاري على نسخ البيانات");
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(ManLocationsReport.this);
                ws.CallManLocationReport("1", "1", "1", "1", "1");


                try {

                    Integer i;
                    String q = "";


                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID = js.getJSONArray("ID");
                    JSONArray js_ManNo = js.getJSONArray("ManNo");
                    JSONArray js_Tr_time = js.getJSONArray("Tr_time");
                    JSONArray js_Tr_date = js.getJSONArray("Tr_date");
                    JSONArray js_no = js.getJSONArray("no");
                    JSONArray js_X = js.getJSONArray("X");
                    JSONArray js_Y = js.getJSONArray("Y");
                    JSONArray js_Loct = js.getJSONArray("Loct");
                    JSONArray js_V_OrderNo = js.getJSONArray("V_OrderNo");
                    JSONArray js_man_name = js.getJSONArray("man_name");


                    Cls_ManLocationReport obj = new Cls_ManLocationReport();

                    obj.setID("");
                    obj.setManNo("رقم المندوب");
                    obj.setTime("الوقت");
                    obj.setDate("التاريخ");
                    obj.setX("احداثيات X");
                    obj.setY("إحداثيات Y");
                    obj.setLoct("الموقع");
                    obj.setV_OrderNo(" ");
                    obj.setNo("  ");
                    obj.setMan_Name("اسم المندوب");
                    LocationList.add(obj);

                    // date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear


                    for (i = 0; i < js_ID.length(); i++) {
                        obj = new Cls_ManLocationReport();


                        obj.setID(js_ID.get(i).toString());
                        obj.setManNo(js_ManNo.get(i).toString());
                        obj.setTime(js_Tr_time.get(i).toString());
                        obj.setDate(js_Tr_date.get(i).toString());
                        obj.setX(js_X.get(i).toString());
                        obj.setY(js_Y.get(i).toString());
                        obj.setLoct(js_Loct.get(i).toString());
                        obj.setV_OrderNo(js_V_OrderNo.get(i).toString());
                        obj.setNo(js_no.get(i).toString());
                        obj.setMan_Name(js_man_name.get(i).toString());
                        LocationList.add(obj);


                        custDialog.setMax(js_ID.length());
                        custDialog.incrementProgressBy(1);

                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }

                    }


                    _handler.post(new Runnable() {

                        public void run() {

                            Cls_ManLocation_Adapter Location_adapter = new Cls_ManLocation_Adapter(
                                    ManLocationsReport.this, LocationList);

                            items_Lsit.setAdapter(Location_adapter);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ManLocationsReport.this).create();
                            alertDialog.setTitle("تحديث البيانات");

                            alertDialog.setMessage("تمت عملية استرجاع  البيانات بنجاح ");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            // alertDialog.show();

                            custDialog.dismiss();
                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ManLocationsReport.this).create();
                            alertDialog.setTitle("مواقع المندوبيين");
                            if (We_Result.ID == -404) {
                                alertDialog.setMessage(We_Result.Msg);
                            } else {
                                alertDialog.setMessage("لا يوجد بيانات");
                            }
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
    public void btnPrint(View view) {

    }
    public void btn_back(View view) {
        Intent k = new Intent(this, JalMasterActivity.class);
        startActivity(k);
    }
    public void btn_GetData(View view) {
        onProgressUpdate();
    }

    @Override
    public void onBackPressed() {
       this.finish();

        Intent k = new Intent(this, JalMasterActivity.class);
        startActivity(k);
    }

    public void btn_Back(View view) {
        this.finish();
        Intent k = new Intent(this, JalMasterActivity.class);
        startActivity(k);

    }
    private void drawPoints(ArrayList<LatLng> points, GoogleMap mMaps) {
        if (points == null) {
            return;
        }
        traceOfMe = points;
        PolylineOptions polylineOpt = new PolylineOptions();
        for (LatLng latlng : traceOfMe) {
            polylineOpt.add(latlng);
        }
        polylineOpt.color(Color.GREEN);
        if (mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
        if (mMap != null) {
            mPolyline = mMap.addPolyline(polylineOpt);

        } else {

        }
        if (mPolyline != null)
            mPolyline.setWidth(10);


    }
}





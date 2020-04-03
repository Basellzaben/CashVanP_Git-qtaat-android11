package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.Tracking.MapsActivity;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Acc_Report;
import com.cds_jo.GalaxySalesApp.assist.Cls_Acc_Report_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf;
import com.cds_jo.GalaxySalesApp.assist.Cls_ManLocation_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Convert_ccReportTo_ImgActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import header.Header_Frag;

//import com.loopj.android.http.HttpGet;// temp

public class ManLocationsReport extends AppCompatActivity {
    ListView items_Lsit;
    public ProgressDialog loadingdialog;
    SqlHandler sqlHandler;



    ArrayList<Cls_ManLocationReport>  LocationList;
    DecimalFormat Per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_location_report);

        LocationList = new ArrayList<Cls_ManLocationReport>();
         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        items_Lsit=(ListView)findViewById(R.id.lst_acc);

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_ManLocationReport o = (Cls_ManLocationReport) items_Lsit.getItemAtPosition(position);
                ShowMap(o.getX(),o.getY());

            }
        });

        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();


        onProgressUpdate();
    }
    private  void ShowMap(String x ,String y){
        Intent k= new Intent(this, MapsActivity.class);
        k.putExtra("Lat", x);
        k.putExtra("Lon", y);
        startActivity(k);
    }
     private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",","");
        Double d = 0.0;
        if (str.length()==0) {
            str = "0";
        }
        if (str.length()>0)
            try {
                d =  Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            }
            catch (Exception ex)
            {
                str="0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }


    public void onProgressUpdate( ){



        final List<String> items_ls = new ArrayList<String>();
        items_Lsit=(ListView)findViewById(R.id.lst_acc);
        items_Lsit.setAdapter(null);



        final   TextView acc = (TextView)findViewById(R.id.tv_acc);




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
               ws.CallManLocationReport("1","1","1","1","1");


                try {

                    Integer i;
                    String q = "";


                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID= js.getJSONArray("ID");
                    JSONArray js_ManNo= js.getJSONArray("ManNo");
                    JSONArray js_Tr_time= js.getJSONArray("Tr_time");
                    JSONArray js_Tr_date= js.getJSONArray("Tr_date");
                    JSONArray js_no= js.getJSONArray("no");
                    JSONArray js_X= js.getJSONArray("X");
                    JSONArray js_Y= js.getJSONArray("Y");
                    JSONArray js_Loct= js.getJSONArray("Loct");
                    JSONArray js_V_OrderNo= js.getJSONArray("V_OrderNo");
                    JSONArray js_man_name= js.getJSONArray("man_name");



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


                    for( i =0 ; i<js_ID.length();i++)
                    {
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
                                alertDialog.setMessage("لا يوجد بيانات" );
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
    public void btn_searchCustomer(View view) {


        TextView acc = (TextView)findViewById(R.id.tv_acc);
        TextView tv_cusnm = (TextView)findViewById(R.id.tv_cusnm);
        acc.setText("");
        tv_cusnm.setText("");

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "AccReport");
        FragmentManager Manager =  getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void Set_Cust(final String No, String Nm) {
        final    ProgressDialog progressDialog;
        TextView CustNm =(TextView)findViewById(R.id.tv_cusnm);
        TextView acc = (TextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);

         //onProgressUpdate();

    }

    public void btn_back(View view) {
        Intent k = new Intent(this,JalMasterActivity.class);
        startActivity(k);
    }

    public void btn_GetData(View view) {
        onProgressUpdate();
    }

    @Override
    public void onBackPressed() {
        Intent k= new Intent(this, JalMasterActivity.class);
        startActivity(k);
    }
    public void btn_Back(View view) {
        Intent k = new Intent(this,JalMasterActivity.class);
        startActivity(k);

    }
}





package com.cds_jo.GalaxySalesApp.assist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.TransQtyReportActivity;
import com.cds_jo.GalaxySalesApp.We_Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Bill_Details extends AppCompatActivity {

    TextView Item_Name2;
    TextView qty2;
    TextView UnitName2;
    TextView price2;
    TextView bonus2;
    TextView DisPer2;
    TextView SumWithOutTax2;
    TextView bill2;
    TextView myear2;
    String MaxStoreQtySer = "0" ;

    int AllowSalInvMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill__details);
        Item_Name2=(TextView) findViewById(R.id.Item_Name2);
        qty2=(TextView) findViewById(R.id.qty2);
        UnitName2=(TextView) findViewById(R.id.UnitName2);
        price2=(TextView) findViewById(R.id.price2);
        bonus2=(TextView) findViewById(R.id.bonus2);
        DisPer2=(TextView) findViewById(R.id.DisPer2);
        SumWithOutTax2=(TextView) findViewById(R.id.SumWithOutTax2);
        bill2=(TextView) findViewById(R.id.bill2);
        myear2=(TextView) findViewById(R.id.myear2);

        bill2.setText(getIntent().getStringExtra("Doc_num"));
        myear2.setText(getIntent().getStringExtra("Date"));

        ShowData();
    }

    private void ShowData() {
        final ProgressDialog progressDialog;
        final Handler _handler = new Handler();
        final TextView bill=(TextView) findViewById(R.id.bill2);
        final TextView myaer=(TextView) findViewById(R.id.myear2);
        progressDialog = new ProgressDialog(Bill_Details.this);
        progressDialog.setTitle("الرجاء الانتظار");
        progressDialog.setMessage("العمل جاري على استرجاع معلومات الفاتورة");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

               CallWebServices ws = new CallWebServices(Bill_Details.this);
              // ws.Bill_Details(bill.getText().toString(), myaer.getText().toString());

                try {
                    Integer i;
                    String q = "";

                    final ArrayList<Cls_bill_Details> cls_trans_qties = new ArrayList<Cls_bill_Details>();
                    cls_trans_qties.clear();

                    Cls_bill_Details cls_bill_Details = new Cls_bill_Details();

                    SqlHandler sqlHandler = new SqlHandler(Bill_Details.this);

                    Double qty = 0.0;
                    Cursor c = sqlHandler.selectQuery(q);
                    i = 0;
                    if (c != null && c.getCount() != 0) {
                        if (c.moveToFirst()) {
                            do {

                                cls_bill_Details = new Cls_bill_Details();

                                cls_bill_Details.setItem_Name(c.getString(c
                                        .getColumnIndex("Item_Name")));
                                cls_bill_Details.setqty(c.getString(c
                                        .getColumnIndex("qty")));
                                cls_bill_Details.setUnitName(c.getString(c
                                        .getColumnIndex("UnitName")));
                                cls_bill_Details.setprice(c.getString(c
                                        .getColumnIndex("price")));
                                cls_bill_Details.setDisPer(c.getString(c
                                        .getColumnIndex("DisPer")));
                                cls_bill_Details.setbonus(c.getString(c
                                        .getColumnIndex("bonus")));
                                cls_bill_Details.setSumWithOutTax(c.getString(c
                                        .getColumnIndex("SumWithOutTax")));

                                if ( qty !=0){
                                    cls_trans_qties.add(cls_bill_Details);
                                }
                                qty = 0.0;


                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }

                                i = i + 1;

                            } while (c.moveToNext());

                        }
                        c.close();
                    }


                    final int total = i;
                    _handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    Bill_Details.this).create();

                            alertDialog.setMessage("تمت عملية جلب معلومات الفاتورة" + "   " + String.valueOf(total));
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            //alertDialog.show();

                        }
                    });
                } catch (final Exception e) {
                    progressDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    Bill_Details.this).create();
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

}

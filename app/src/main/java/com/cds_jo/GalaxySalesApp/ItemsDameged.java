package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.assist.Cls_Trans_Qty;
import com.cds_jo.GalaxySalesApp.assist.Cls_Trans_Qty_Adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ItemsDameged extends AppCompatActivity {

    ListView lst_Items ;
    String MaxStoreQtySer = "0" ;
    int AllowSalInvMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_dameged);
        lst_Items =(ListView)findViewById(R.id.lst_Items);
        ShowData();
    }

    private void ShowData() {
        final ProgressDialog progressDialog;
        final Handler _handler = new Handler();
        AllowSalInvMinus =Integer.parseInt( DB.GetValue(this,"ComanyInfo","AllowSalInvMinus","1=1"));

        progressDialog = new ProgressDialog(ItemsDameged.this);
        progressDialog.setTitle("الرجاء الانتظار");
        progressDialog.setMessage("العمل جاري على استرجاع المواد التالفة");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        final String currentDateandTime = sdf.format(new Date());

        Calendar cc = Calendar.getInstance();
        final int dayOfWeek = cc.get(Calendar.DAY_OF_WEEK);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Integer i;
                    String q = "";

                    final ArrayList<Cls_Items_Dameged> cls_trans_qties = new ArrayList<Cls_Items_Dameged>();
                    cls_trans_qties.clear();

                    Cls_Items_Dameged cls_items_dameged = new Cls_Items_Dameged();

                    //cls_trans_qty.setDate("      " + "التاريخ" + "        ");
                    cls_items_dameged.setDocno("     " + "رقم السند" + "  ");
                    cls_items_dameged.setItemno("رقم المادة");
                    cls_items_dameged.setItem_Name("اسم  المادة");
                    cls_items_dameged.setqty("الكمية");

                    cls_trans_qties.add(cls_items_dameged);

                    SqlHandler sqlHandler = new SqlHandler(ItemsDameged.this);


                    q = " SELECT Sal_return_Det.Orderno, Sal_return_Det.ItemNo,  invf.Item_Name , sal_return_Det.qty from Sal_return_Det" +
                         " Inner join invf on Sal_return_Det.ItemNo=invf.Item_No where Sal_return_Det.Damaged=1";



                    Double qty = 0.0;
                    Cursor c = sqlHandler.selectQuery(q);
                    i = 0;
                    if (c != null && c.getCount() != 0) {
                        if (c.moveToFirst()) {
                            do {

                                //date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear
                                cls_items_dameged = new Cls_Items_Dameged();

                                cls_items_dameged.setDocno(c.getString(c
                                        .getColumnIndex("Orderno")));
                                cls_items_dameged.setItemno(c.getString(c
                                        .getColumnIndex("ItemNo")));
                                cls_items_dameged.setItem_Name(c.getString(c
                                        .getColumnIndex("Item_Name")));
                                cls_items_dameged.setqty(c.getString(c
                                        .getColumnIndex("qty")));

                                cls_trans_qties.add(cls_items_dameged);




                                progressDialog.setMax(c.getCount());
                                progressDialog.incrementProgressBy(1);

                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }

                                i = i + 1;

                            } while (c.moveToNext());

                        }
                        c.close();
                    }
                    _handler.post(new Runnable() {
                        public void run() {
                            Cls_Items_Dameged_Adapter cls_items_dameged_adapter = new Cls_Items_Dameged_Adapter(
                                    ItemsDameged.this, cls_trans_qties);
                            lst_Items.setAdapter(cls_items_dameged_adapter);
                        }
                    });

                    final int total = i;
                    _handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ItemsDameged.this).create();

                            alertDialog.setMessage("تمت عملية تحديث البيانات بنجاح" + "   " + String.valueOf(total));
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            // alertDialog.show();

                        }
                    });
                } catch (final Exception e) {
                    progressDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ItemsDameged.this).create();
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
    public void btn_back(View view) {
        Intent i = new Intent(this,JalMasterActivity.class);
        startActivity(i);
    }
}

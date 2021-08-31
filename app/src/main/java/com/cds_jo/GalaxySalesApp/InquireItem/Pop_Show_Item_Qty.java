package com.cds_jo.GalaxySalesApp.InquireItem;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.warehouse.Cls_Item_Store_Qty;
import com.cds_jo.GalaxySalesApp.warehouse.Item_Store_qty_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Pop_Show_Item_Qty extends DialogFragment implements View.OnClickListener {

    View form;
    SharedPreferences sharedPreferences;
    Button  back,update  ;
    SwipeMenuListView items_Lsit ;
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    String UserID ;
    SqlHandler sql_Handler;
    public ProgressDialog loadingdialog;
    ArrayList<Cls_Item_Store_Qty> cls_item_store_qties ;

    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form= inflater.inflate(R.layout.pop_show_item_qty, container, false);

        Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

        items_Lsit = (SwipeMenuListView) form.findViewById(R.id.Lst);

        cls_item_store_qties = new ArrayList<Cls_Item_Store_Qty>();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UserID = sharedPreferences.getString("UserID", "");
        sql_Handler = new SqlHandler(this.getActivity());


        back=(Button) form.findViewById(R.id.back);
        back.setOnClickListener(this);
        update=(Button) form.findViewById(R.id.update);
        update.setOnClickListener(this);

       GetData();
        return form;
    }

    public void ShowList(){

        Item_Store_qty_Adapter item_store_qty_adapter = new Item_Store_qty_Adapter(
                this.getActivity(), cls_item_store_qties);
        items_Lsit.setAdapter(item_store_qty_adapter);

    }
    public void GetData() {
        cls_item_store_qties.clear();
        ShowList();
        tv = new TextView(getActivity());
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(getActivity());
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("استعلام عن مادة");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        String MaxSeer = "1";


        final String Ser = "1";


        new Thread(new Runnable() {
            @Override
            public void run() {




                CallWebServices ws = new CallWebServices(getActivity());
                ws.GetItemQty(UserID,GlobleV.itemno );
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                  //  JSONArray js_itemno = js.getJSONArray("itemno");
                    JSONArray js_UnitName= js.getJSONArray("UnitName");
                    JSONArray js_qty = js.getJSONArray("qty");
                    JSONArray js_storeno = js.getJSONArray("storeno");
                    JSONArray js_sname = js.getJSONArray("sname");


                    Cls_Item_Store_Qty obj ;

                    for (i = 0; i < js_storeno.length(); i++) {
                        obj = new Cls_Item_Store_Qty();
                        obj.setItemno("");
                        obj.setQty(js_qty.get(i).toString());
                        obj.setSname(js_sname.get(i).toString());
                        obj.setStoreno(js_storeno.get(i).toString());
                        obj.setUnitName(js_UnitName.get(i).toString());
                        cls_item_store_qties.add(obj);

                        custDialog.setMax(js_storeno.length());
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
                                    getActivity()).create();
                            alertDialog.setTitle("استعلام عن مادة");
                            alertDialog.setMessage("تمت عملية التحديث بنجاح");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                        //   alertDialog.show();


                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();
try{
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("استعلام عن مادة");
                            alertDialog.setMessage("لا يوجد بيانات");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();}catch (Exception e){}
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
           dismiss();
        }else if (v == update) {
            GetData();

        }
    }
}





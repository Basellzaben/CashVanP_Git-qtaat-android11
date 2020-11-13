package com.cds_jo.GalaxySalesApp.InquireItem;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.ManCard.SplashLoginAct;
import com.cds_jo.GalaxySalesApp.NewHomePage.NewHomePage;
import com.cds_jo.GalaxySalesApp.R;

import com.cds_jo.GalaxySalesApp.StartUpActivity;
import com.cds_jo.GalaxySalesApp.VisitImges;
import com.cds_jo.GalaxySalesApp.We_Result;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import header.Header_Frag;

public class InquireItemACT extends AppCompatActivity {
    public ProgressDialog loadingdialog;
    String a;
    cls_Item_hdr cls_acc_report ;
    ArrayList<cls_Item_hdr> list;
    String No1;
    JSONArray Item_Name1,type_name1,ol1,OQ11;
    JSONArray dno1,Price1,originno1,pack201;
    JSONArray pack401,brandno1,itemWeight1,Expired1,tax1;
    EditText Item_Name,type_name,ol,OQ1,dno,Price,originno,pack20,pack40,brandno,itemWeight,Expired,tax,et_OrdeNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire_item_a_c_t);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Item_Name = (EditText)findViewById(R.id.Item_Name);
        type_name = (EditText)findViewById(R.id.type_name);
        OQ1 = (EditText)findViewById(R.id.OQ1);
        ol = (EditText)findViewById(R.id.ol);
        //    dno = (TextView)findViewById(R.id.dno);
        Price = (EditText)findViewById(R.id.Price);
        //  originno = (TextView)findViewById(R.id.originno);
        pack20 = (EditText)findViewById(R.id.pack20);
        brandno = (EditText)findViewById(R.id.brandno);
        pack40 = (EditText)findViewById(R.id.pack40);
        et_OrdeNo = (EditText)findViewById(R.id.et_OrdeNo);
       // itemWeight = (EditText)findViewById(R.id.itemWeight);
        Expired = (EditText)findViewById(R.id.Expired);
        tax = (EditText)findViewById(R.id.tax);
        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

    }
    public void btn_Search_Orders(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("Item", "Itemshow");
        FragmentManager Manager = getFragmentManager();
        Item_SearchAct obj = new Item_SearchAct();
        obj.setArguments(bundle);
        obj.show(Manager,null);
    }
    public void btn_Getfile(View view) {
        fileFrg exampleDialog = new fileFrg();
        exampleDialog.show(getFragmentManager(), "MyCustomDialog");

    }
    public void btn_GetData(View view) {
        Categ_Frq exampleDialog = new Categ_Frq();
        exampleDialog.show(getFragmentManager(), "MyCustomDialog");

    }
    public void btn_GetUint(View view) {
        UintFrq exampleDialog = new UintFrq();
        exampleDialog.show(getFragmentManager(), "MyCustomDialog");

    }
    public void bt_back(View view) {
       GlobleV.itemno="";
        startActivity(new Intent(InquireItemACT.this, NewHomePage.class));
        finish();

    }
    public void Set_Order(String No) {
        GlobleV.itemno =No;
        final Handler _handler = new Handler();
        et_OrdeNo.setText(GlobleV.itemno);
        DeleteRecord();
        loadingdialog = ProgressDialog.show(InquireItemACT.this, "الرجاء الانتظار ...", "العمل جاري على استرجاع البيانات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.show();
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(InquireItemACT.this);
                ws.GetItem_D(GlobleV.itemno,1);
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);


                       Item_Name1 = js.getJSONArray("Item_Name");
                      type_name1= js.getJSONArray("type_name");
                      ol1 = js.getJSONArray("ol");
                      OQ11 = js.getJSONArray("OQ1");
                      dno1= js.getJSONArray("dno");
                      Price1= js.getJSONArray("Price");
                      pack201 = js.getJSONArray("pack20");
                      pack401 = js.getJSONArray("pack40");
                       brandno1 = js.getJSONArray("brandno");
                       //itemWeight1 = js.getJSONArray("itemWeight");
                        Expired1 = js.getJSONArray("Expired");
                        tax1 = js.getJSONArray("tax");
                   a= Item_Name1.get(0).toString();
                    _handler.post(new Runnable() {
                        public void run() {
                            try {
                                loadingdialog.dismiss();
                                Item_Name.setText(Item_Name1.get(0).toString());
                                type_name.setText(type_name1.get(0).toString());
                                ol.setText(ol1.get(0).toString());
                                OQ1.setText(OQ11.get(0).toString());
                                //      dno.setText(dno1);
                                Price.setText(Price1.get(0).toString());
                                //   originno.setText(originno1);
                                pack40.setText(pack401.get(0).toString());
                                pack20.setText(pack201.get(0).toString());
                                brandno.setText(dno1.get(0).toString());
                             //   itemWeight.setText(itemWeight1.get(0).toString());
                                if(Expired1.get(0).toString().equals("0"))
                                {
                                    Expired.setText("لا");

                                }
                                else
                                {
                                    Expired.setText("نعم");
                                }
                                tax.setText(tax1.get(0).toString()+"%");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });





                } catch (final Exception e) {

                    _handler.post(new Runnable() {

                        public void run() {
                            loadingdialog.dismiss();
                            new SweetAlertDialog(InquireItemACT.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                    .setTitleText("استعلام عن المواد")
                                    .setContentText("لا يوجد بيانات ")
                                    //.setCustomImage(R.drawable.custom_img)
                                    .show();

                        }
                    });

                }

            }
        };
        thread.start();

    }

    private void DeleteRecord() {
        Item_Name.setText("");
        type_name.setText("");
        ol.setText("");
        OQ1.setText("");
        //      dno.setText(dno1);
        Price.setText("");
        //   originno.setText(originno1);
        pack40.setText("");
        pack20.setText("");
        brandno.setText("");
        //   itemWeight.setText(itemWeight1.get(0).toString());
        Expired.setText("");
        tax.setText("");
    }

    public void btn_ShowQty(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Item", "Itemshow");
        FragmentManager Manager = getFragmentManager();
        Pop_Show_Item_Qty obj = new Pop_Show_Item_Qty();
        obj.setArguments(bundle);
        obj.show(Manager,null);
    }
}
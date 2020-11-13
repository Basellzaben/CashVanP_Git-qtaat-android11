package com.cds_jo.GalaxySalesApp.InquireItem;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Categ_Frq extends DialogFragment {
    cls_get_items_categ cls_acc_report ;
    ArrayList<cls_get_items_categ> list;
    final Handler _handler = new Handler();
    cls_get_items_categ_Adabter adapter;
    ListView listView;
    Button back,update;
    public ProgressDialog loadingdialog;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("فئات الاسعار");
        View view = inflater.inflate(R.layout.activity_categ__frq, container, false);
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        back=(Button) view.findViewById(R.id.back);
        update=(Button) view.findViewById(R.id.update);
        //cls_acc_report =new cls_ACC_Report();
        list =new ArrayList<>();
        listView=(ListView) view.findViewById(R.id.LstvItems);
        showD();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                showD();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent myIntent = new Intent(getActivity(), Sale_InvoiceActivity.class);
                //getActivity().startActivity(myIntent);
                dismiss();

            }});
        return view;
    }

    private void showD() {
        loadingdialog = ProgressDialog.show(getActivity(), "الرجاء الانتظار ...", "العمل جاري على استرجاع البيانات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.show();
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(getActivity());
                ws.GetItem_D(GlobleV.itemno,3);
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray CategNo = js.getJSONArray("CategNo");
                    JSONArray CatName= js.getJSONArray("CatName");
                    JSONArray Price = js.getJSONArray("Price");
                    JSONArray MinPrice = js.getJSONArray("MinPrice");
                    JSONArray Bonus= js.getJSONArray("bonus");

                    JSONArray Dis = js.getJSONArray("dis");

                    cls_acc_report = new cls_get_items_categ();
                    for (i = 0; i < CategNo.length(); i++) {
                        cls_acc_report = new cls_get_items_categ();

                        cls_acc_report.setCategNo(CategNo.get(i).toString());
                        cls_acc_report.setCatName(CatName.get(i).toString());
                        cls_acc_report.setPrice(Price.get(i).toString());
                        cls_acc_report.setMinPrice(MinPrice.get(i).toString());
                        cls_acc_report.setBonus(Bonus.get(i).toString());
                        cls_acc_report.setDis(Dis.get(i).toString());



                        list.add(cls_acc_report);
                    }
                    _handler.post(new Runnable() {
                        public void run() {
                            loadingdialog.dismiss();
                            adapter = new cls_get_items_categ_Adabter(getActivity(), list);
                            listView.setAdapter(adapter);
                        }
                    });

                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                            loadingdialog.dismiss();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                    .setTitleText("استعلام عن الفئات")
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

}
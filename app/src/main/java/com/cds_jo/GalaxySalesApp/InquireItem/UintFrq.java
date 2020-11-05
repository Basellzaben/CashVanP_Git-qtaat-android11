package com.cds_jo.GalaxySalesApp.InquireItem;

import android.app.DialogFragment;
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

public class UintFrq extends DialogFragment {
    cls_get_Uint cls_acc_report;
    ArrayList<cls_get_Uint> list;
    final Handler _handler = new Handler();
    cls_get_Uint_Adabter adapter;
    ListView listView;
    Button back,update;

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("الوحدات");
        View view = inflater.inflate(R.layout.activity_uint_frq, container, false);
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        back = (Button) view.findViewById(R.id.back);
        update = (Button) view.findViewById(R.id.update);
        //cls_acc_report =new cls_ACC_Report();
        list = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.LstvItems);
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

            }
        });

        return view;
    }

    private void showD() {

        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(getActivity());
                ws.GetItem_D(GlobleV.itemno, 2);
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray Uintno = js.getJSONArray("uintno");
                    JSONArray UnitName = js.getJSONArray("UnitName");
                    JSONArray Price = js.getJSONArray("price");
                    JSONArray Min = js.getJSONArray("Min");
                    JSONArray Operand = js.getJSONArray("Operand");

                    JSONArray Barcode = js.getJSONArray("barcode");

                    cls_acc_report = new cls_get_Uint();
                    for (i = 0; i < Uintno.length(); i++) {

                        cls_acc_report = new cls_get_Uint();

                        cls_acc_report.setUintno(Uintno.get(i).toString());
                        cls_acc_report.setUnitName(UnitName.get(i).toString());
                        cls_acc_report.setPrice(Price.get(i).toString());
                        cls_acc_report.setMin(Min.get(i).toString());
                        cls_acc_report.setOperand(Operand.get(i).toString());
                        cls_acc_report.setBarcode(Barcode.get(i).toString());


                        list.add(cls_acc_report);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            adapter = new cls_get_Uint_Adabter(getActivity(), list);
                            listView.setAdapter(adapter);
                        }
                    });

                } catch (final Exception e) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("العملية لم تتم بنجاح")
                            .setContentText(We_Result.Msg)
                            .setCustomImage(R.drawable.error_new)
                            .setConfirmText("رجــــوع")
                            .show();
                }

            }
        };
        thread.start();

    }

}
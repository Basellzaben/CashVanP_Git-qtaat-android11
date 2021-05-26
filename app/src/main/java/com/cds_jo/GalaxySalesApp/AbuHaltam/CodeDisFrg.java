package com.cds_jo.GalaxySalesApp.AbuHaltam;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.GlobaleVar;
import com.cds_jo.GalaxySalesApp.Pos.PopSavePosInvoice;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.note_manFrg;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CodeDisFrg  extends DialogFragment {
    View form ;
Button Show;
Button Update;
EditText et_Input;
    Button Accept;
    ProgressDialog progressdialog;


    /*  @Override
      public void onStart()
      {
          super.onStart();

          // safety check
          if (getDialog() == null)
              return;
          int dialogWidth =  WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
          int dialogHeight =  WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here
          getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

      }*/
    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form= inflater.inflate(R.layout.fragment_code_dis_frg, container, false);

        Show=(Button)form.findViewById(R.id.Show);
        Accept=(Button)form.findViewById(R.id.Accept);
        Update=(Button)form.findViewById(R.id.Update);
        et_Input=(EditText) form.findViewById(R.id.et_Input);
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequstCodeFrq exampleDialog = new RequstCodeFrq();
                exampleDialog.show(getFragmentManager(), "example dialog");
            }
        });
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PopSavePosInvoice s =new PopSavePosInvoice();
              /*  s.ed_bounse.setText(GlobaleVar.bounse);
                s.et_Discount.setText(GlobaleVar.dis);*/
                s.setdis();

            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 progressdialog = new ProgressDialog(getActivity());
                progressdialog.setMessage("الرجاءالانتظار ");
                progressdialog.show();
                final Handler _handler = new Handler();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        CallWebServices ws = new CallWebServices(getActivity());
                        ws.GetDisCount("6");
                        try {
                            Integer i;
                            String q;
                            JSONObject js = new JSONObject(We_Result.Msg);
                            JSONArray BarCode = js.getJSONArray("BarCode");
                            JSONArray Bounse = js.getJSONArray("Bounse");
                            JSONArray dis = js.getJSONArray("dis");
                            JSONArray Type_dis = js.getJSONArray("Type_dis");

                           GlobaleVar.barcode = BarCode.get(0).toString();
                           GlobaleVar.bounse = Bounse.get(0).toString();
                           GlobaleVar.dis = dis.get(0).toString();
                           GlobaleVar.type_dis = Type_dis.get(0).toString();





                            _handler.post(new Runnable() {
                                public void run() {
                                    progressdialog.dismiss();
                                    et_Input.setText(GlobaleVar.barcode);
                                }
                            });
                        } catch (final Exception e) { progressdialog.dismiss();}}}).start();}});
        return form;
    }





}

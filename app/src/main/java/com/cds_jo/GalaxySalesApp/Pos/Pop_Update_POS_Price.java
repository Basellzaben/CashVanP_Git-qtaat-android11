package com.cds_jo.GalaxySalesApp.Pos;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import Methdes.MyTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Hp on 21/08/2017.
 */

public class Pop_Update_POS_Price extends DialogFragment implements View.OnClickListener {
    Button Inc, Dec,btn_Clear,btn_Back;
    View form;
    EditText tv_price;
    MyTextView   Title;
    String OrderNo ,ItemNo="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        form = inflater.inflate(R.layout.update_pos_price, container, false);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER|Gravity.TOP);
        Inc = (Button) form.findViewById(R.id.btn_Increment);
        Inc.setOnClickListener(this);

        btn_Clear = (Button) form.findViewById(R.id.btn_Clear);
        btn_Clear.setOnClickListener(this);

        btn_Back = (Button) form.findViewById(R.id.btn_Back);
        btn_Back.setOnClickListener(this);

        Dec = (Button) form.findViewById(R.id.btn_Dicrement);
        Dec.setOnClickListener(this);
        Title = (MyTextView)form.findViewById(R.id.Title);
        Title.setText(getArguments().getString("Nm"));
        OrderNo= getArguments().getString("OrderNo") ;
        ItemNo= getArguments().getString("ItemNo") ;


        tv_price = (EditText)form.findViewById(R.id.tv_price);
        tv_price.setText(getArguments().getString("Price"));
        tv_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tv_price.getText().toString().equalsIgnoreCase("")){
                        ((Pos_Activity) getActivity()).UpdatePrice("1");
                }
            ((Pos_Activity) getActivity()).UpdatePrice(tv_price.getText().toString());
            }


        });

        getDialog().setTitle(getArguments().getString("Nm"));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hideKeyboard(form);
        return form;
    }

    private Double SToD(String str) {
        str=str .replaceAll("[^\\d.]", "");
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
    public   void hideKeyboard(View v ) {

        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
    @Override
    public void onClick(View v) {

        if (tv_price.getText().toString().equalsIgnoreCase("")){
            tv_price.setText("1");
            return;
        }
        if(v==btn_Back){
            this.dismiss();
        }
        if(v==btn_Clear){
            tv_price.setText("");
            tv_price.requestFocus();
        }
        if (v==Inc){
            tv_price.setText( (Double.parseDouble(tv_price.getText().toString() )+1)   +"" );

         if( Double.parseDouble( tv_price.getText().toString() ) > 1 ) {
                Dec.setVisibility(View.VISIBLE);
            }

        }

        if(v==Dec){

            tv_price.setText( (Double.parseDouble(tv_price.getText().toString() )-1)   +"" );
            if( Double.parseDouble( tv_price.getText().toString() ) < 1 ) {
                tv_price.setText("1");
                Dec.setVisibility(View.INVISIBLE);
            }


        }
  }
}


package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopEnterInvoiceHeaderDiscount extends DialogFragment implements View.OnClickListener {
    View form;
    NumberPicker np;
    Button btnSave;
    EditText et_Discount, ed_Total, ed_AfterDiscount;
    RadioButton rdoPrecent, rdoAmt;
String DiscountMethod="1";
SqlHandler sqlHandler ;
    @Override
    public void onStart() {
        super.onStart();


        if (getDialog() == null)
            return;

        //  int dialogWidth =740; // WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here

        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here*/


        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        form = inflater.inflate(R.layout.view_enter_invoice_discount, container, false);
        getDialog().setTitle("الخصم النهائي على الفاتورة");

        rdoPrecent = (RadioButton) form.findViewById(R.id.rdoPrecent);
        rdoAmt = (RadioButton) form.findViewById(R.id.rdoAmt);
        rdoPrecent.setChecked(true);
        et_Discount = (EditText) form.findViewById(R.id.et_Discount);
        ed_Total = (EditText) form.findViewById(R.id.ed_Total);
        ed_AfterDiscount = (EditText) form.findViewById(R.id.ed_AfterDiscount);
        sqlHandler=new SqlHandler(getActivity());

        ed_Total.setText(getArguments().getString("NetTotal"));
        et_Discount.setText(getArguments().getString("Discount"));

        et_Discount.requestFocus();

        btnSave = (Button) form.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        rdoPrecent.setOnClickListener(this);
        rdoAmt.setOnClickListener(this);
        et_Discount.setOnClickListener(this);

        et_Discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                CalcDiscount();
            }


        });
        GetDiscountInfo();
        return form;
    }
private  void GetDiscountInfo(){
       String OrederNo="";
        OrederNo =  getArguments().getString("OrederNo");

        String q= " select  ifnull( hdr_dis_per,'0') as hdr_dis_per ,ifnull(hdr_dis_value ,'0') as hdr_dis_value " +
                "           ,ifnull(hdr_dis_Type,'1') as hdr_dis_Type  , ifnull(TotalWithoutDiscount,'0') as TotalWithoutDiscount   from Sal_invoice_Hdr where OrderNo ='"+OrederNo+"'";

    Cursor c = sqlHandler.selectQuery(q);
     if(c!=null && c.getCount()>0){
         c.moveToFirst();
         if(c.getString(c.getColumnIndex("hdr_dis_Type")).equalsIgnoreCase("1")){
             rdoPrecent.setChecked(true);
             et_Discount.setText(c.getString(c.getColumnIndex("hdr_dis_per")));

         }
         if(c.getString(c.getColumnIndex("hdr_dis_Type")).equalsIgnoreCase("2")){
             rdoAmt.setChecked(true);
             et_Discount.setText(c.getString(c.getColumnIndex("hdr_dis_value")));

         }

      //   ed_Total.setText(c.getString(c.getColumnIndex("TotalWithoutDiscount")));
         c.close();
     }
    CalcDiscount();
  /*  cv.put("hdr_dis_per", FinalDiscountpercent+"");
    cv.put("hdr_dis_value",FinalDiscountAmt+"");
    cv.put("hdr_dis_Type",FinalDiscountType+"");
*/
}
    private void CalcDiscount() {
        double Result = 0.0;
        et_Discount.setError(null);
        et_Discount.setTextColor(Color.BLACK);
        ed_AfterDiscount.setError(null);
        ed_AfterDiscount.setTextColor(Color.BLACK);
        if (et_Discount.getText().toString().equalsIgnoreCase("")) {
            return;
        }
        if (rdoPrecent.isChecked() == true) {
            if (SToD(et_Discount.getText().toString()) > 100) {
                et_Discount.setError("!");
                et_Discount.setTextColor(Color.RED);
                return;
            }
            Result = (SToD(et_Discount.getText().toString()) / 100) * SToD(ed_Total.getText().toString());
            Result=SToD(Result+"");
            Result = SToD(ed_Total.getText().toString()) - Result;
            ed_AfterDiscount.setText(SToD(Result + "") + "");
        } else {
            Result = SToD(ed_Total.getText().toString()) - SToD(et_Discount.getText().toString());
            ed_AfterDiscount.setText(SToD(Result + "") + "");
        }

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


    public void onClick(View v) {

        if (v == btnSave) {

            if (SToD(ed_AfterDiscount.getText().toString()) <= 0) {
                ed_AfterDiscount.setError("!");
                ed_AfterDiscount.setTextColor(Color.RED);
            } else {

                if (rdoPrecent.isChecked() == true) {
                    DiscountMethod="1";
                } else {
                    DiscountMethod="2";
                }
                ((Sale_InvoiceActivity) getActivity()).InsertDiscount(et_Discount.getText().toString(), DiscountMethod);
                this.dismiss();
            }
        } else if (v == rdoPrecent) {
            CalcDiscount();

        } else if (v == rdoAmt) {
            CalcDiscount();
        } else if (v == et_Discount) {
            et_Discount.setText("");
        }
    }


}



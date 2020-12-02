
package com.cds_jo.GalaxySalesApp.Pos;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.cds_jo.GalaxySalesApp.Cls_Bank_Search;
import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.Cls_Bank_search_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Cur;
import com.cds_jo.GalaxySalesApp.assist.Cls_Cur_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopSavePosInvoice extends DialogFragment implements View.OnClickListener {
    View form;
    NumberPicker np;
    Button btnSave,btn_Back;
    EditText et_Discount, ed_Total, ed_AfterDiscount ;
    EditText ed_Check_Paid_Amt,ed_Visa_Paid_Amt,ed_Cash_Paid_Amt,ed_Cust_Amt_Paid,ed_RemainAmt,ed_Check_Number,ed_Check_Bank_Name,ed_Check_Date,ed_Visa_Number,ed_Visa_Expire_Date;
    RadioButton rdoPrecent, rdoAmt;
    String DiscountMethod="1";
    CheckBox cbx_Cash,cbx_Check,cbx_Visa,chk_print;
    SqlHandler sqlHandler ;
    Spinner sp_banks;
    ArrayList<Cls_Bank_Search> cls_bank_searches;
    String bankNo="";
    String Print_Flg="0";
    @Override
    public void onStart() {
        super.onStart();


        if (getDialog() == null)
            return;

        //  int dialogWidth =740; // WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogWidth = 1700; // specify a value here
         dialogWidth=1200;
        if(ComInfo.ComNo== Companies.Sector.getValue()){
            dialogWidth = 1700;
        }
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here*/


        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        form = inflater.inflate(R.layout.popsaveposinvoice, container, false);
        getDialog().setTitle("الخصم النهائي على الفاتورة");
        sp_banks = (Spinner) form.findViewById(R.id.sp_banks);
        rdoPrecent = (RadioButton) form.findViewById(R.id.rdoPrecent);
        rdoAmt = (RadioButton) form.findViewById(R.id.rdoAmt);
        rdoPrecent.setChecked(true);
        et_Discount = (EditText) form.findViewById(R.id.et_Discount);
        ed_Total = (EditText) form.findViewById(R.id.ed_Total);
        ed_AfterDiscount = (EditText) form.findViewById(R.id.ed_AfterDiscount);

        cbx_Cash = (CheckBox) form.findViewById(R.id.cbx_Cash);
        cbx_Check = (CheckBox) form.findViewById(R.id.cbx_Check);
        cbx_Visa = (CheckBox) form.findViewById(R.id.cbx_Visa);
        chk_print = (CheckBox) form.findViewById(R.id.chk_print);




        ed_Check_Paid_Amt = (EditText) form.findViewById(R.id.ed_Check_Paid_Amt);
        ed_Visa_Paid_Amt = (EditText) form.findViewById(R.id.ed_Visa_Paid_Amt);
        ed_Cash_Paid_Amt = (EditText) form.findViewById(R.id.ed_Cash_Paid_Amt);
        ed_Cust_Amt_Paid = (EditText) form.findViewById(R.id.ed_Cust_Amt_Paid);
        ed_RemainAmt = (EditText) form.findViewById(R.id.ed_RemainAmt);


        ed_Check_Number = (EditText) form.findViewById(R.id.ed_Check_Number);
        ed_Check_Bank_Name = (EditText) form.findViewById(R.id.ed_Check_Bank_Name);
        ed_Check_Date = (EditText) form.findViewById(R.id.ed_Check_Date);
        ed_Visa_Number = (EditText) form.findViewById(R.id.ed_Visa_Number);
        ed_Visa_Expire_Date = (EditText) form.findViewById(R.id.ed_Visa_Expire_Date);


        ed_Cash_Paid_Amt.requestFocus();


        sqlHandler=new SqlHandler(getActivity());

        ed_Total.setText(getArguments().getString("NetTotal"));
        et_Discount.setText(getArguments().getString("Discount"));




        btnSave = (Button) form.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btn_Back = (Button) form.findViewById(R.id.btn_Back);
        btn_Back.setOnClickListener(this);
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
        ed_Cust_Amt_Paid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {CalcRemain();}});

        ed_Check_Paid_Amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {CalcRemain();}});

        ed_Visa_Paid_Amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {CalcRemain();}});


        if (getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        }

        ed_Check_Paid_Amt.setEnabled(false);
        ed_Check_Paid_Amt.setText("0.000");
        ed_Check_Date.setEnabled(false);
        sp_banks.setEnabled(true);
        ed_Visa_Paid_Amt.setEnabled(false);
        ed_Visa_Paid_Amt.setText("0.000");
        ed_Visa_Number.setEnabled(false);
        ed_Visa_Expire_Date.setEnabled(true);
        cbx_Check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    ed_Check_Paid_Amt.setEnabled(true);
                    ed_Check_Paid_Amt.setText(ed_AfterDiscount.getText().toString());
                    ed_Check_Date.setEnabled(true);
                    sp_banks.setEnabled(true);

                }else{
                    ed_Check_Paid_Amt.setEnabled(false);
                    ed_Check_Paid_Amt.setText("0.000");
                    ed_Check_Date.setEnabled(false);
                    sp_banks.setEnabled(true);
                }

            }
        });

        cbx_Visa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    ed_Visa_Paid_Amt.setEnabled(true);
                    ed_Visa_Paid_Amt.setText(ed_AfterDiscount.getText().toString());
                    ed_Visa_Number.setEnabled(true);
                    ed_Visa_Expire_Date.setEnabled(true);

                }else{
                    ed_Visa_Paid_Amt.setEnabled(false);
                    ed_Visa_Paid_Amt.setText("0.000");
                    ed_Visa_Number.setEnabled(false);
                    ed_Visa_Expire_Date.setEnabled(true);
                }

            }
        });




        cbx_Cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    ed_Cash_Paid_Amt.setEnabled(true);
                    ed_Cust_Amt_Paid.setEnabled(true);
                    ed_Cash_Paid_Amt.setText(ed_AfterDiscount.getText().toString());
                    ed_Cust_Amt_Paid.setText(ed_AfterDiscount.getText().toString());


                }else{
                    ed_Cash_Paid_Amt.setEnabled(false);
                    ed_Cash_Paid_Amt.setText("0.000");
                    ed_Cust_Amt_Paid.setText("0.000");

                }

            }
        });
        cls_bank_searches = new ArrayList<Cls_Bank_Search>();
        cls_bank_searches.clear();

        FillBanks();

        ed_Cust_Amt_Paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_Cust_Amt_Paid.setText("");

            }

        });

        et_Discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_Discount.setText("");
            }

        });







        GetDiscountInfo();

        return form;
    }

    private void FillBanks() {
        String query = "SELECT distinct * FROM banks ";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Bank_Search cls_bank_search = new Cls_Bank_Search();

                    cls_bank_search.setNo(c1.getString(c1
                            .getColumnIndex("bank_num")));
                    cls_bank_search.setName(c1.getString(c1
                            .getColumnIndex("Bank")));

                    cls_bank_searches.add(cls_bank_search);


                } while (c1.moveToNext());

            }
            c1.close();
        }
        Cls_Pos_Bank_Adapter cls_bank_search_adapter = new Cls_Pos_Bank_Adapter(
                getActivity(), cls_bank_searches);
        sp_banks.setAdapter(cls_bank_search_adapter);

    }
    private  void GetDiscountInfo(){
        cbx_Cash.setChecked(false);
        cbx_Visa.setChecked(false);
        cbx_Check.setChecked(false);
       String OrederNo="";
       String Check_Paid_Bank ="";
        OrederNo =  getArguments().getString("OrederNo");

        String q= " select      ifnull(Cash_flg,'0') as Cash_flg ,  ifnull(Check_flg,'0') as Check_flg , ifnull(Visa_flg,'0') as Visa_flg , ifnull(Cash_Paid_Amt,'0') as Cash_Paid_Amt, ifnull(Cust_Amt_Paid,'0') as  Cust_Amt_Paid, ifnull(Remain_Amt,'0') as Remain_Amt , ifnull(Check_Paid_Amt,'0')  as Check_Paid_Amt" +
                            " , ifnull(Check_Paid_Date,'') as Check_Paid_Date,  ifnull(Check_Paid_Bank,'') as Check_Paid_Bank, ifnull(Check_Paid_Person,'') as Check_Paid_Person,  ifnull(Visa_Paid_Amt,'0') as Visa_Paid_Amt , ifnull(Visa_Paid_Expire_Date,'') as Visa_Paid_Expire_Date" +
                            " , ifnull(Visa_Paid_Type,'0') as Visa_Paid_Type   , ifnull(Check_Number ,'0') as Check_Number " +
                            " , ifnull( hdr_dis_per,'0') as hdr_dis_per ,ifnull(hdr_dis_value ,'0') as hdr_dis_value " +
                            " ,ifnull(hdr_dis_Type,'1') as hdr_dis_Type  , ifnull(TotalWithoutDiscount,'0') as TotalWithoutDiscount   from Sal_invoice_Hdr where OrderNo ='"+OrederNo+"'";
        try {
    Cursor c = sqlHandler.selectQuery(q);
    if (c != null && c.getCount() > 0) {
        c.moveToFirst();

        if (c.getString(c.getColumnIndex("Cash_flg")).equalsIgnoreCase("1")) {
            cbx_Cash.setChecked(true);
        }
        if (c.getString(c.getColumnIndex("Check_flg")).equalsIgnoreCase("1")) {
            cbx_Check.setChecked(true);
        }
        if (c.getString(c.getColumnIndex("Visa_flg")).equalsIgnoreCase("1")) {
            cbx_Visa.setChecked(true);
        }

        Check_Paid_Bank =c.getString(c.getColumnIndex("Check_Paid_Bank"));
        ed_Cash_Paid_Amt.setText(SToD( c.getString(c.getColumnIndex("Cash_Paid_Amt")))+"");
        ed_Cust_Amt_Paid.setText(SToD(c.getString(c.getColumnIndex("Cust_Amt_Paid")))+"");
        ed_RemainAmt.setText(SToD(c.getString(c.getColumnIndex("Remain_Amt")))+"");

        ed_Check_Paid_Amt.setText(SToD(c.getString(c.getColumnIndex("Check_Paid_Amt")))+"");
        ed_Check_Number.setText(c.getString(c.getColumnIndex("Check_Number")));
        ed_Check_Date.setText(c.getString(c.getColumnIndex("Check_Paid_Date")));


        ed_Visa_Paid_Amt.setText(SToD(c.getString(c.getColumnIndex("Visa_Paid_Amt")))+"");
        ed_Visa_Number.setText(c.getString(c.getColumnIndex("Visa_Paid_Type")));
        ed_Visa_Expire_Date.setText(c.getString(c.getColumnIndex("Visa_Paid_Expire_Date")));





        if (c.getString(c.getColumnIndex("hdr_dis_Type")).equalsIgnoreCase("1")) {
            rdoPrecent.setChecked(true);
            et_Discount.setText(c.getString(c.getColumnIndex("hdr_dis_per")));

        }
        if (c.getString(c.getColumnIndex("hdr_dis_Type")).equalsIgnoreCase("2")) {
            rdoAmt.setChecked(true);
            et_Discount.setText(c.getString(c.getColumnIndex("hdr_dis_value")));

        }

      //  ed_Total.setText(c.getString(c.getColumnIndex("TotalWithoutDiscount")));
        c.close();
    }

            SetBank(Check_Paid_Bank);
              CalcDiscount();
             }catch (Exception d){}

}
    private  void GetSumDetails(){

        String OrederNo="";

        OrederNo =  getArguments().getString("OrederNo");

        String q= " select     sum( cast( OrgPrice as float) * cast( qty  as float) ) as tot  from Sal_invoice_Det where OrderNo ='"+OrederNo+"'";
        try {
            Cursor c = sqlHandler.selectQuery(q);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                ed_Total.setText(SToD(   c.getString(c.getColumnIndex("tot")))+"");
                c.close();
            }
        }catch (Exception d){}

    }
private  void SetBank(String Check_Paid_Bank){
    Cls_Bank_Search cls_bank_search;
        Cls_Pos_Bank_Adapter cls_pos_bank_adapter = (Cls_Pos_Bank_Adapter) sp_banks.getAdapter();
    for (int i = 0; i < cls_pos_bank_adapter.getCount(); i++) {
        cls_bank_search = (Cls_Bank_Search) cls_pos_bank_adapter.getItem(i);
        if (cls_bank_search.getNo().equals(Check_Paid_Bank)) {
            sp_banks.setSelection(i);
            break;
        }
    }

}
    private  void CalcRemain(){


    double NetAmt = SToD(ed_AfterDiscount.getText().toString())- ( SToD(ed_Check_Paid_Amt.getText().toString() )+ SToD(ed_Visa_Paid_Amt.getText().toString()))    ;

    if (NetAmt<0){
        NetAmt= 0;
    }
    ed_Cash_Paid_Amt.setText( SToD( NetAmt+"")+"");

    ed_RemainAmt.setText(SToD((SToD(ed_Cust_Amt_Paid.getText().toString())- SToD(ed_Cash_Paid_Amt.getText().toString()) )+"")+"");
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


        ed_Cash_Paid_Amt.setText( SToD(ed_AfterDiscount.getText().toString())+"");
        CalcRemain();
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

        if (v == btn_Back) {
            this.dismiss();
        }
        if (v == btnSave) {

            if (cbx_Check.isChecked()){

                if( SToD( ed_Check_Paid_Amt.getText().toString())<=0  || ed_Check_Paid_Amt.getText().toString().equalsIgnoreCase("") ){
                    ed_Check_Paid_Amt.setError("القيمة خاطئة!");
                    ed_Check_Paid_Amt.requestFocus();
                    return;
                }
                if(  ed_Check_Number.getText().toString().equalsIgnoreCase("") ){
                    ed_Check_Number.setError("القيمة خاطئة!");
                    ed_Check_Number.requestFocus();
                    return;
                }
                 if(  ed_Check_Date.getText().toString().equalsIgnoreCase("") ){
                     ed_Check_Date.setError("القيمة خاطئة!");
                     ed_Check_Date.requestFocus();
                    return;
                }

                AlertDialog alertDialog  ;
                alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("تاريخ الشيك");
                alertDialog.setMessage( "هناك خطأ في طريقة ادخال  تاريخ الشيك ، الرجاء ادخال التاريخ كالتالي 31/12/2020");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ed_Check_Date.setText("");
                    }
                });

                if(!isValidDate(ed_Check_Date.getText().toString())){
                    alertDialog.show();
                    return;
                }



            }
            if (cbx_Visa.isChecked()){

                if( SToD( ed_Visa_Paid_Amt.getText().toString())<=0  || ed_Visa_Paid_Amt.getText().toString().equalsIgnoreCase("") ){
                    ed_Visa_Paid_Amt.setError("القيمة خاطئة!");
                    ed_Visa_Paid_Amt.requestFocus();
                    return;
                }
                if(  ed_Visa_Number.getText().toString().equalsIgnoreCase("") ){
                    ed_Visa_Number.setError("القيمة خاطئة!");
                    ed_Visa_Number.requestFocus();
                    return;
                }
                 if(  ed_Visa_Expire_Date.getText().toString().equalsIgnoreCase("") ){
                     ed_Visa_Expire_Date.setError("القيمة خاطئة!");
                     ed_Visa_Expire_Date.requestFocus();
                    return;
                }

            }







            Cls_Bank_Search bank = (Cls_Bank_Search) ((Spinner) form.findViewById(R.id.sp_banks)).getSelectedItem();
            if( SToD( ed_RemainAmt.getText().toString())<0){
                new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("فاتورة البيع")
                        .setContentText("الرجاء التأكد من المبلغ المتبقي")
                         .setCustomImage(R.drawable.error_new)
                        .show();
                ed_RemainAmt.setError("القيمة خاطئة!");
                ed_RemainAmt.requestFocus();
                return;

            }
            int cash,check,Visa ;
            cash=0;
            check=0;
            Visa=0;

            if(cbx_Cash.isChecked()){
                cash=1;
            }
            if(cbx_Check.isChecked()){
                check=1;
                bankNo= bank.getNo().toString();
            }else{
                bankNo="";
            }
            if(cbx_Visa.isChecked()){
                Visa=1;
            }
            if (SToD(ed_AfterDiscount.getText().toString()) <= 0) {
                ed_AfterDiscount.setError("!");
                ed_AfterDiscount.setTextColor(Color.RED);
            } else {

                if (rdoPrecent.isChecked() == true) {
                    DiscountMethod="1";
                } else {
                    DiscountMethod="2";
                }//   // ed_Check_Number,ed_Check_Bank_Name,ed_Check_Date,ed_Visa_Number,ed_Visa_Expire_Date
                if (chk_print.isChecked()){
                    Print_Flg="1";
                }else{
                    Print_Flg="0";
                }
            ((Pos_Activity) getActivity()).InsertDiscount(et_Discount.getText().toString(), DiscountMethod,cash,check,Visa, ed_Check_Paid_Amt.getText().toString(),ed_Visa_Paid_Amt.getText().toString(),ed_Cash_Paid_Amt.getText().toString(),ed_Cust_Amt_Paid.getText().toString(),ed_RemainAmt.getText().toString(),ed_Check_Date.getText().toString(), bankNo
                          ,"",ed_Visa_Expire_Date.getText().toString(),ed_Visa_Number.getText().toString(),ed_Check_Number.getText().toString(),Print_Flg);
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
    private boolean isValidDate(String dateOfBirth) {
        boolean valid = true;

        if(dateOfBirth.trim().length()<10) {
            return false;
        }
        try {

        } catch (Exception e) {
            valid = false;
            return valid;
        }


        String[] parts = dateOfBirth.split("/");
        String part3 = parts[0];
        String part2 = parts[1];
        String part1 = parts[2];
        if ( SToD(part1) <0  || SToD(part1)>31 ){
            valid = false;
            return valid;
        }
        if ( SToD(part2) <0  || SToD(part2)>12 ){
            valid = false;
            return valid;
        }

        if ( SToD(part3) <1990  || SToD(part3)>2050 ){
            valid = false;
            return valid;
        }

        return valid;
    }

}



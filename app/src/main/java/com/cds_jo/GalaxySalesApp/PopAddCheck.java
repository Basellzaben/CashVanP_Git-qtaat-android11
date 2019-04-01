
package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;

import android.widget.Spinner;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.assist.CheckAdapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Bank_search_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;
import com.cds_jo.GalaxySalesApp.assist.Convert_RecVouch_To_Img_GoodSystem;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopAddCheck extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0 ;
    EditText DayCount ,filter ,edCheckData  ;
    ImageButton btn_filter_search ;
    String UnitNo ="";
    String UnitName ="";
    String str= "";
    RadioButton rad_Per ;
    RadioButton rad_Amt;
    Spinner sp_banks ;
    ArrayList<Cls_Bank_Search> cls_bank_searches;
    SqlHandler sql_Handler;
    TextView CheckData;
    private int year, month, day;
    ListView lstView ;
    ArrayList<Cls_Check> ChecklList;


    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }


    @Override
 public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){

        form =inflater.inflate(R.layout.popaddcheck,container,false);

        getDialog().setTitle("معلومات الشيكات");
        add =(ImageButton) form.findViewById(R.id.btn_save_Check);
        add.setOnClickListener(this);
        cancel=(ImageButton) form.findViewById(R.id.btn_Back);

        cancel.setOnClickListener(this);
        lstView = (ListView) form.findViewById(R.id.lstCheck);
        ChecklList = new ArrayList<Cls_Check>();
        ChecklList.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String CustNo = sharedPreferences.getString("CustNo", "");
        DayCount =(EditText)form.findViewById(R.id.DayCount);
        DayCount.setText(DB.GetValue(getActivity(),"Customers","CheckAlowedDay","no='"+CustNo+"'"));
        DayCount.setText("500");
        edCheckData =(EditText)form.findViewById(R.id.ed_CheckData);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                final  Integer pos = position;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("حذف الشيكات");
                alertDialog.setMessage("هل انت متاكد من عملية الحذف ..؟");

                alertDialog.setIcon(R.drawable.delete);


                alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteCheck(pos);
                    }
                });


                alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });


                alertDialog.show();

        }
        });
        sp_banks = (Spinner) form.findViewById(R.id.sp_banks);

         cls_bank_searches = new ArrayList<Cls_Bank_Search>();
        cls_bank_searches.clear();

        sql_Handler = new SqlHandler(getActivity());
        FillBanks();
        CheckData = (TextView) form.findViewById(R.id.CheckData);



        CheckData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog picker = new  DatePickerDialog(getActivity(), myDateListener, year, month, day);
                picker.show();

             //showDialog(999);

            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        CheckData.setText(currentDateandTime);
        showList();


        return  form;
}

private  void DeleteCheck(int pos ){
    ChecklList.remove(pos);
    Integer pos1 = ChecklList.size();
    Cls_Check obj = new Cls_Check();
    for (int x = 0; x < pos1; x++) {
        obj = new Cls_Check();
        obj = ChecklList.get(x);
        ChecklList.remove(x);
        obj.setSer(x + 1);
        ChecklList.add(x, obj);
    }
    CheckAdapter checkAdapter = new CheckAdapter(
            getActivity(), ChecklList);
    lstView.setAdapter(checkAdapter);
    UpdateTable();
    total();

}
private void UpdateTable(){
    String q="delete from t_RecCheck ";
    sql_Handler = new SqlHandler(this.getActivity());
    sql_Handler.executeQuery(q);
    Cls_Check obj = new Cls_Check();
    for(int i = 0 ; i<ChecklList.size();i++ ){
        obj = new Cls_Check();
        obj = ChecklList.get(i);
          q = "insert into t_RecCheck (CheckNo,CheckDate,BankNo, Amnt ) values ( '" +
                  obj.getCheckNo()
                  + "','"+ obj.getCheckDate()
                  + "','"+ obj.getBankNo()
                  + "','"+ obj.getAmnt() +"')";

        sql_Handler.executeQuery(q);

    }


}
    private void showList() {

        ChecklList.clear();
        lstView.setAdapter(null);

        sql_Handler = new SqlHandler(this.getActivity());

        String query = "Select distinct rc.CheckNo,rc.CheckDate,rc.BankNo, rc.Amnt  , b.Bank from  t_RecCheck rc  left join banks b on b.bank_num = rc.BankNo ";
        Integer i = 1;
        Cursor c1 = sql_Handler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Check cls_check_obj = new Cls_Check();
                    cls_check_obj.setSer(i);
                    cls_check_obj.setCheckNo(c1.getString(c1
                            .getColumnIndex("CheckNo")));
                    cls_check_obj.setCheckDate(c1.getString(c1
                            .getColumnIndex("CheckDate")));
                    cls_check_obj.setBankName(c1.getString(c1
                            .getColumnIndex("Bank")));
                    cls_check_obj.setBankNo(c1.getString(c1
                            .getColumnIndex("BankNo")));
                    cls_check_obj.setAmnt(c1.getString(c1
                            .getColumnIndex("Amnt")));

                    ChecklList.add(cls_check_obj);
                    i=i+1;
                } while (c1.moveToNext());


            }
            c1.close();
        }



        CheckAdapter checkAdapter = new CheckAdapter(
                this.getActivity(), ChecklList);
        lstView.setAdapter(checkAdapter);
        total();

        //  json = new Gson().toJson(contactList);
    }

    public void setDate(View view) {

        DatePickerDialog picker = new  DatePickerDialog(getActivity(), myDateListener, year, month, day);
        picker.show();


        //  showDialog(999);
       /* Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();*/
    }


    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(getActivity(), myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

            CheckData.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));

    }

    private  void FillBanks(){
       String query = "SELECT distinct * FROM banks ";

       Cursor c1 = sql_Handler.selectQuery(query);
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
       }
       c1.close();
       Cls_Bank_search_Adapter cls_bank_search_adapter = new Cls_Bank_search_Adapter(
               getActivity(), cls_bank_searches);
       sp_banks.setAdapter(cls_bank_search_adapter);

   }
    private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",","");
        Double d = 0.0;
        if (str.length()==0) {
            str = "0";
        }
        if (str.length()>0)
            try {
                d =  Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            }
            catch (Exception ex)
            {
                str="0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
    private boolean isValidDate(String dateOfBirth) {
        boolean valid = true;

        if(dateOfBirth.trim().length()<10) {
            return false;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date date= formatter.parse(edCheckData.getText().toString());

        } catch (Exception e) {
            valid = false;
            return valid;
        }


        String[] parts = dateOfBirth.split("/");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
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
 private  long get_days_between_dates(String CurrentDate, String FinalDate){
     long differenceDates=0;
     try {


         Date date1;
         Date date2;

         SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");

         //Setting dates
         date1 = dates.parse(CurrentDate);
         date2 = dates.parse(FinalDate);

         //Comparing dates
         long difference = Math.abs(date1.getTime() - date2.getTime());
           differenceDates = difference / (24 * 60 * 60 * 1000);

         //Convert long to String
         String dayDifference = Long.toString(differenceDates);

         Log.e("HERE","HERE: " + dayDifference);

     } catch (Exception exception) {
         Log.e("DIDN'T WORK", "exception " + exception);
     }
     return  differenceDates ;
 }



    public void onClick(View v) {

        Cls_Bank_Search bank = (Cls_Bank_Search) ((Spinner) form.findViewById(R.id.sp_banks)).getSelectedItem();

         if (v == cancel) {
             ((RecvVoucherActivity) getActivity()).UpdateCheck();

             this.dismiss();
        }

        if (v == add) {



            EditText CheckNo = (EditText) form.findViewById(R.id.CheckNo);
            TextView CheckData = (TextView) form.findViewById(R.id.CheckData);
            EditText ChekAmt = (EditText) form.findViewById(R.id.ChekAmt);


            AlertDialog alertDialog  ;
            alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("تاريخ الشيك");
            alertDialog.setMessage( "هناك خطأ في طريقة ادخال  تاريخ الشيك ، الرجاء ادخال التاريخ كالتالي 31/12/2015");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    edCheckData.setText("");
                }
            });

          if(!isValidDate(edCheckData.getText().toString())){
              alertDialog.show();
            return;
          }

         if (CheckNo.getText().toString().length() == 0) {
                CheckNo.setError("required!");
                CheckNo.requestFocus();
                return;
            }

            if (edCheckData.getText().toString().length() == 0) {
                edCheckData.setError("required!");
                edCheckData.requestFocus();
                return;
            }
            if (ChekAmt.getText().toString().length() == 0) {
                ChekAmt.setError("required!");
                ChekAmt.requestFocus();
                return;
            }



            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String currentDate = sdf.format(new Date());


            long u=    get_days_between_dates(currentDate,edCheckData.getText().toString());
            if( u> SToD( DayCount.getText().toString())) {

                alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("تاريخ استحقاق الشيك بعد " + u + " يوم  ");
                alertDialog.setMessage("يجب ان تكون مدة استحقاق الشيك اقل من " + DayCount.getText().toString());
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();


               /* if (ComInfo.ComNo == Companies.bustanji.getValue()) {
                    return;
                }*/

            }

            else if( u>45){

                alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(  "تاريخ استحقاق الشيك بعد " + u+ " يوم  ");
                alertDialog.setMessage( "تاريخ  استحقاق الشيك اكبر من 45 يوم وبالتالي لايوجد  عمولة على هذا سند القبض");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
           ((RecvVoucherActivity) getActivity()).save_Check(CheckNo.getText().toString(), ChekAmt.getText().toString(), bank, edCheckData.getText().toString());
            save_Check(CheckNo.getText().toString(), ChekAmt.getText().toString(), bank, edCheckData.getText().toString());
            ChekAmt.setText("");
            edCheckData.setText("");
            CheckNo.setText("");
            CheckNo.requestFocus();
        }
    }
    public void save_Check(String CheckNo , String ChekAmt , Cls_Bank_Search bank ,String Check_Data ) {


     /* TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        if (DocNo.getText().toString().length() == 0) {
            DocNo.setError("required!");
            DocNo.requestFocus();
            return;
        }*/



        Cls_Check cls_check_obj = new Cls_Check();
        cls_check_obj.setSer(Integer.valueOf(lstView.getCount() + 1));
        cls_check_obj.setCheckNo(CheckNo);
        cls_check_obj.setCheckDate(Check_Data);
        cls_check_obj.setBankNo(bank.getNo().toString());
        cls_check_obj.setBankName(bank.getName().toString());
        cls_check_obj.setAmnt(ChekAmt);
        ChecklList.add(cls_check_obj);

        CheckAdapter checkAdapter = new CheckAdapter(
                this.getActivity() , ChecklList);
        lstView.setAdapter(checkAdapter);

        sql_Handler = new SqlHandler(this.getActivity());
        String q = "insert into t_RecCheck (CheckNo,CheckDate,BankNo, Amnt ) values ( '" +
                CheckNo
                + "','"+Check_Data
                + "','"+ bank.getNo().toString()
                + "','"+ChekAmt +"')";

        sql_Handler.executeQuery(q);

        total();
        /*CalcTotal();*/
    }

    private void total(){
        int x1 = 0;
        Cls_Check cls_check_obj = new Cls_Check();
        Double  sum = 0.0;
        for ( int x = 0; x < ChecklList.size(); x++) {
            cls_check_obj = new Cls_Check();
            cls_check_obj = ChecklList.get(x);
            sum = sum + Double.parseDouble(cls_check_obj.getAmnt());
            x1 = x ;
        }
        TextView tv_CheckAmt = (TextView)form.findViewById(R.id.tv_CheckAmt);
        tv_CheckAmt.setText(sum + "");
        MyTextView tv_Check_count = (MyTextView)form.findViewById(R.id.tv_Check_count);
        tv_Check_count.setText((x1+1) + "");

        if ( ChecklList.size()==0){
            tv_Check_count.setText("لا يوجد شيكات");
        }
    }


}



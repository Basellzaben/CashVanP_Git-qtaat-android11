package com.cds_jo.GalaxySalesApp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.cds_jo.GalaxySalesApp.Pos.Pos_Activity;
import com.cds_jo.GalaxySalesApp.TspPrinter.PrinterFunctions;
import com.cds_jo.GalaxySalesApp.XprinterDoc.Xprinter_ManSummeryTo_img;
import com.cds_jo.GalaxySalesApp.XprinterDoc.Xprinter_RecVoucher;
import com.cds_jo.GalaxySalesApp.XprinterDoc.Xprinter_SalesInvoice;
import com.cds_jo.GalaxySalesApp.assist.Convert_ManSummery_To_Img;
import com.cds_jo.GalaxySalesApp.assist.Convert_RecVouch_To_Img;
import com.cds_jo.GalaxySalesApp.assist.ManAttenActivity;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;
import header.Header_Frag;

public class ManSummeryNew extends FragmentActivity {

    private Context context = ManSummeryNew.this;
    private MyTextView T1, T2, T3, T4, T5, T6,T8;
    private Fragment frag;
    String[] items = new String[]{"2021/06/16", "2/2/2020"};
    EditText dateedit;
    LinearLayout  T2l,  T3l, T8l;
    LinearLayout  T1l,  T4l, T5l,T6l;
    DatePickerDialog picker;
    Button b;
    private Xprinter_ManSummeryTo_img flag1;
    private android.support.v4.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_summery_new);
        Initi();
        Click();
        frag = new Header_Frag();


        //LinearLayout  T1l,  T4l, T5l,T6l;


        T1l=(LinearLayout)findViewById(R.id.T1l);
        T4l=(LinearLayout)findViewById(R.id.T4l);
        T5l=(LinearLayout)findViewById(R.id.T5l);
        T6l=(LinearLayout)findViewById(R.id.T6l);

        T2l=(LinearLayout)findViewById(R.id.T2l);
        T3l=(LinearLayout)findViewById(R.id.T3l);
        T8l=(LinearLayout)findViewById(R.id.T8l);


        if (ComInfo.ComNo == Companies.Sector.getValue()) {

            T4l.setVisibility(View.GONE);
            T3l.setVisibility(View.GONE);
            T8l.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
            T1l.getLayoutParams();
            T2l.getLayoutParams();
            T5l.getLayoutParams();
            T6l.getLayoutParams();

            T1l.getLayoutParams();
            T2l.getLayoutParams();
            T5l.getLayoutParams();
            T6l.getLayoutParams();
            params.weight = 1.7f;
            T1l.setLayoutParams(params);
            T2l.setLayoutParams(params);
            T5l.setLayoutParams(params);
            T6l.setLayoutParams(params);


        }else {

        }









        //    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        // dropdown.setAdapter(adapter);
    /*    picker=(DatePicker)findViewById(R.id.datePicker1);
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String date= picker.getYear()+"/"+ (picker.getMonth() + 1)+"/"+picker.getDayOfMonth();


                Toast.makeText(getApplicationContext(),date,Toast.LENGTH_LONG).show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("spinnerdateselected",date);
                editor.apply();

            }
        });*/



  /*      dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getApplicationContext(),items[position],Toast.LENGTH_LONG).show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("spinnerdateselected",items[position]);
                editor.apply();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }

        });*/


        dateedit=(EditText) findViewById(R.id.dateedit);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("spinnerdateselected",currentDateandTime);
        editor.apply();


        dateedit.setText(currentDateandTime);
        dateedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog

                picker = new DatePickerDialog(ManSummeryNew.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear+=1;
                                String days = String.valueOf(dayOfMonth);
                                String months = String.valueOf(monthOfYear);
                                String years = String.valueOf(year);
                                // date picker dialog
                                if(days.length()==1){
                                    days="0"+days;
                                }
                                if(months.length()==1){
                                    months="0"+months;
                                }

                                String d=years+ "/" + months + "/" + days ;
                                dateedit.setText(d);
                                Toast.makeText(getApplicationContext(),d,Toast.LENGTH_LONG).show();
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("spinnerdateselected",d);
                                editor.apply();

                                String CLICK =preferences.getString("CLICK", "");
                                if(CLICK.equals("T1")){
                                    frag = new Tab_SalesSummery();
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
                                }else if(CLICK.equals("T2")) {
                                    frag = new Tab_Payments();
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
                                }
                                else  if(CLICK.equals("T3")) {
                                    frag = new Tab_SalesOrders();
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
                                }
                                else  if(CLICK.equals("T4")) {
                                    frag = new Tab_UsedCode();
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
                                }else  if(CLICK.equals("T5")) {
                                    frag = new Tab_UnpostedTransaction();
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
                                }else  if(CLICK.equals("T8")) {
                                    frag = new Tab_returnproduct();
                                    fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();

                                }

                            }
                        }, year, month, day);
                picker.show();
            }
        });



        frag = new Header_Frag();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();


    }

    private void Click() {
        T1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T1.setBackgroundColor(getResources().getColor(R.color.Blue));
                T1.setTextColor(Color.WHITE);

                T8.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T8.setTextColor(getResources().getColor(R.color.Blue));


                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CLICK","T1");
                editor.apply();

                frag = new Tab_SalesSummery();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        //////////////////////////////////////////////////////////////

        T2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CLICK","T2");
                editor.apply();
                T8.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T8.setTextColor(getResources().getColor(R.color.Blue));

                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackgroundColor(getResources().getColor(R.color.Blue));
                T2.setTextColor(Color.WHITE);


                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));

                frag = new Tab_Payments();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////

        T3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CLICK","T3");
                editor.apply();

                T8.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T8.setTextColor(getResources().getColor(R.color.Blue));

                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));


                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackgroundColor(getResources().getColor(R.color.Blue));
                T3.setTextColor(Color.WHITE);

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));

                frag = new Tab_SalesOrders();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        //////////////////////////////////////////////////////////////

        T4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CLICK","T4");
                editor.apply();

                T8.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T8.setTextColor(getResources().getColor(R.color.Blue));


                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));


                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackgroundColor(getResources().getColor(R.color.Blue));
                T4.setTextColor(Color.WHITE);

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));

                frag = new Tab_UsedCode();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////

        T5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CLICK","T5");
                editor.apply();

                T8.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T8.setTextColor(getResources().getColor(R.color.Blue));


                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));


                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackgroundColor(getResources().getColor(R.color.Blue));
                T5.setTextColor(Color.WHITE);

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));

                frag = new Tab_UnpostedTransaction();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }
        });

        ////////////////////////////////////////////////////////////////////

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent k = new Intent(ManSummeryNew.this, JalMasterActivity.class);
                startActivity(k);


/*            if (ComInfo.ComNo == Companies.Ukrania.getValue()) {

                }else{
                frag = new Convert_ManSummery_To_Img();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }*/
                //  }

            }

        });

        T6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                T8.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T8.setTextColor(getResources().getColor(R.color.Blue));


                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));


                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));


                T6.setBackgroundColor(getResources().getColor(R.color.Blue));
                T6.setTextColor(Color.WHITE);
                btn_print();
                Intent k = new Intent(ManSummeryNew.this, Xprinter_ManSummeryTo_img.class);
                startActivity(k);


/*            if (ComInfo.ComNo == Companies.Ukrania.getValue()) {

                }else{
                frag = new Convert_ManSummery_To_Img();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }*/
                //  }

            }

        });



        T8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CLICK","T8");
                editor.apply();

                T1.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T1.setTextColor(getResources().getColor(R.color.Blue));

                T2.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T2.setTextColor(getResources().getColor(R.color.Blue));

                T3.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T3.setTextColor(getResources().getColor(R.color.Blue));

                T4.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T4.setTextColor(getResources().getColor(R.color.Blue));

                T5.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T5.setTextColor(getResources().getColor(R.color.Blue));

                T6.setBackground(getResources().getDrawable(R.drawable.blue_fill_white));
                T6.setTextColor(getResources().getColor(R.color.Blue));


                T8.setBackgroundColor(getResources().getColor(R.color.Blue));
                T8.setTextColor(Color.WHITE);

                frag = new Tab_returnproduct();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();


/*            if (ComInfo.ComNo == Companies.Ukrania.getValue()) {

                }else{
                frag = new Convert_ManSummery_To_Img();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.Frag11, frag).commit();
            }*/
                //  }

            }

        });


    }

    private void Initi() {
        b=(Button)findViewById(R.id.back);
        //  dropdown=(Spinner)findViewById(R.id.datespinner);

        T1 = (MyTextView) findViewById(R.id.T1);
        T2 = (MyTextView) findViewById(R.id.T2);
        T3 = (MyTextView) findViewById(R.id.T3);
        T4 = (MyTextView) findViewById(R.id.T4);
        T5 = (MyTextView) findViewById(R.id.T5);
        T6 = (MyTextView) findViewById(R.id.T6);
        T8 = (MyTextView) findViewById(R.id.T8);


    }

    public void btn_print() {
        if (ComInfo.ComNo == Companies.Afrah.getValue()) {
            Intent k = new Intent(this, Xprinter_ManSummeryTo_img.class);
            startActivity(k);
        }
        else if (ComInfo.ComNo == Companies.Sector.getValue()) {
            String portName ="";// PrinterTypeActivity.getPortName();
            String portSettings = "";

            PrinterFunctions.RasterCommand rasterType = PrinterFunctions.RasterCommand.Standard;
            int paperWidth = 576;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String UserName =  sharedPreferences.getString("UserName", "");
            try {
                PrinterFunctions.PrintManSummary(this, portName, portSettings, "Line", getResources(),  "3inch (80mm)", rasterType);



            }
            catch (Exception ex){
                Toast.makeText(ManSummeryNew.this,ex.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        else {
            //  Intent k = new Intent(this, Convert_ManSummery_To_Img.class);
            Intent k = new Intent(this, Xprinter_ManSummeryTo_img.class);
            startActivity(k);
        }
    }
}

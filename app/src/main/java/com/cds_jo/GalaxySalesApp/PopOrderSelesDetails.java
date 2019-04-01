
package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CheckAdapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Bank_search_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopOrderSelesDetails extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel;
    Button btn_Save,btn_Cancel;
    LinearLayout mContent;
    View view;

    Bitmap bitmap;
    SqlHandler sqlHandler;
    float min = 0 ;
    EditText Ed_Notes  ;
    ImageButton btn_filter_search ;
    String UnitNo ="";
    String UnitName ="";
    String str= "";
    private Calendar calendar;


    SqlHandler sql_Handler;
    TextView CheckData;
    private int year, month, day;



    NumberPicker Np_Day,Np_Month,Np_Year;
    int SaveImg = 0 ;
    @Override
    public void onStart()
    {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;
        int dialogWidth =  WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight =  WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }


    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){

        form =inflater.inflate(R.layout.poporderdetails,container,false);
           getDialog().setTitle("ملاحظات وتاريخ تسليم طلب البيع");

        sqlHandler = new SqlHandler(getActivity());
        SaveImg = 0 ;


        btn_Save=(Button) form.findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(this);

        btn_Cancel=(Button) form.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(this);


        Ed_Notes=(EditText) form.findViewById(R.id.Ed_Notes);


        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, null);

        Np_Day =(NumberPicker)form.findViewById(R.id.Day);
        Np_Day.setMaxValue(31);
        Np_Day.setMinValue(1);


        Np_Day.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });


        Np_Month =(NumberPicker)form.findViewById(R.id.Month);
        Np_Month.setMaxValue(12);
        Np_Month.setMinValue(1);


        Np_Month.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        Np_Year =(NumberPicker)form.findViewById(R.id.Year);
        Np_Year.setMaxValue(2030);
        Np_Year.setMinValue(2017);


        Np_Year.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%04d", i);
            }
        });

        final Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);


        calendar = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        Np_Day.setValue(day);
        Np_Month.setValue(month);
        Np_Year.setValue(year);




        sql_Handler = new SqlHandler(getActivity());
        String q = "SELECT  Notes,DeliveryDate From Po_Hdr po   Where OrderNo ='"+getArguments().getString("OrdeNo")+ "'";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);
        String Date="";
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
            Ed_Notes.setText(c1.getString(c1.getColumnIndex("Notes")));
            Date=(c1.getString(c1.getColumnIndex("DeliveryDate")));
         }
         if (Date!=null && !Date.equalsIgnoreCase("")) {
             try {
                 Np_Year   .setValue(Integer.parseInt(Date.substring(0, 4)));
                 Np_Month.setValue(Integer.parseInt(Date.substring(5, 7)));
                 Np_Day.setValue(Integer.parseInt(Date.substring(8, 10)));
             }catch (Exception ex){
                 Np_Day.setValue(day);
                 Np_Month.setValue(month);
                 Np_Year.setValue(year);
             }

         }
         c1.close();
        }

        return  form;
    }








    private boolean isValidDate(String dateOfBirth) {
        boolean valid = true;

        if(dateOfBirth.trim().length()<10) {
            return false;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");


        } catch (Exception e) {
            valid = false;
            return valid;
        }




        return valid;
    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    private Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }
    public void onClick(View v) {


         if (v == btn_Cancel) {
            this.dismiss();
        }


        if (v == btn_Save) {
            EditText Ed_Notes = (EditText) form.findViewById(R.id.Ed_Notes);
            String Date=  ( Np_Year.getValue()+"/"+intToString(Np_Month.getValue(),2)+ "/"+ intToString(Np_Day.getValue(),2)).toString();

            // Toast.makeText(getActivity(),Date,Toast.LENGTH_SHORT).show();
            String q = " SELECT * FROM PO_Hdr   Where OrderNo ='"+getArguments().getString("OrdeNo")+ "'";
            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 == null || c1.getCount() == 0) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("طلب البيع");
                alertDialog.setMessage("يجب تخزين   طلب البيع اولاّ");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
                c1.close();
                return;
            }

            ContentValues cv = new ContentValues();
            cv.put("Notes", Ed_Notes.getText().toString());
            cv.put("DeliveryDate", Date);
            Long i;
            i = sqlHandler.Update("Po_Hdr", cv, "orderno ='" + getArguments().getString("OrdeNo") + "'");
            if(i>0  ){

                Toast.makeText(getActivity(), "تمت عملية تخزين الملاحظات وتاريخ التسليم بنجاح", Toast.LENGTH_SHORT).show();

            }

           /* Np_Day.setValue(day);
            Np_Month.setValue(month);
            Np_Year.setValue(year);
*/
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getActivity().getResources().updateConfiguration(config, null);


        }
    }

}



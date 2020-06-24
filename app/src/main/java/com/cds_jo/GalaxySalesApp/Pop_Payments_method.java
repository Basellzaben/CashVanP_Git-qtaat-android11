package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class Pop_Payments_method  extends DialogFragment implements View.OnClickListener {

    View form;
    private Calendar calendar;
    private TextView dateView1;
    private int year, month, day;
    Button bt_save,bt_add,back;
    ListView lstView ;
    ArrayList<Cls_Payment> PaymentList;
    SqlHandler sql_Handler;
       EditText et_Amt;
    EditText et_Note;
    TextView textView275;
    String orderno;
    Button minday, minmonth, minyear, plusyear, plusmonth, plusday;
    EditText year2, month2, day2;
    int CurrentYear;
    Double sum = 0.0;
    SharedPreferences sharedPreferences;
    Cls_Payment cls_payment_obj ;
    Double Amt,NetTotal;
    public ProgressDialog loadingdialog;
    String date="";
    MyTextView tv_ScrTitle;
    public int position1;

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day

            //  showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        /*dateView1.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));*/

    }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form= inflater.inflate(R.layout.fragment_pop__payments_method, container, false);

        Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

        lstView = (ListView) form.findViewById(R.id.lstpayment);
        PaymentList = new ArrayList<Cls_Payment>();
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),
                        "Click ListItem Number " + i, Toast.LENGTH_LONG)
                        .show();                registerForContextMenu(lstView);

            }
        });



        textView275=(TextView) form.findViewById(R.id.textView275);

        et_Amt=(EditText)form.findViewById(R.id.et_Amt);
        et_Note=(EditText)form.findViewById(R.id.et_Note);

        plusyear = (Button) form.findViewById(R.id.plusyear);
        plusmonth = (Button) form.findViewById(R.id.plusmonth);
        plusday = (Button) form.findViewById(R.id.plusday);
        minyear = (Button) form.findViewById(R.id.minyear);
        minmonth = (Button) form.findViewById(R.id.minmonth);
        minday = (Button) form.findViewById(R.id.minday);

        year2 = (EditText) form.findViewById(R.id.year2);
        month2 = (EditText) form.findViewById(R.id.month2);
        day2 = (EditText) form.findViewById(R.id.day2);
        bt_save=(Button) form.findViewById(R.id.bt_save);
        bt_add=(Button) form.findViewById(R.id.btn_add_item1);
        back=(Button) form.findViewById(R.id.back);
        calendar = Calendar.getInstance();
        year = c.get(Calendar.YEAR);

        month = c.get(Calendar.MONTH);

        day = c.get(Calendar.DAY_OF_MONTH);
        showDate(year, month, day);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());



        SimpleDateFormat Myear = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        CurrentYear = Integer.parseInt( Myear.format(new Date()));




        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String stryear = mdformatyear.format(calendar.getTime());
        displayyear(stryear);

        SimpleDateFormat mdformatmonth = new SimpleDateFormat("MM", Locale.ENGLISH);
        String strmonth = mdformatmonth.format(calendar.getTime());
        displaymonth(strmonth);

        SimpleDateFormat mdformatday = new SimpleDateFormat("dd", Locale.ENGLISH);
        String strday = mdformatday.format(calendar.getTime());
        displayday(strday);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_Payment();
                et_Amt.setText("");
                et_Note.setText("");
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i= add_Item();
                et_Amt.setText("");
                et_Note.setText("");
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

        plusyear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int t = Integer.parseInt(year2.getText().toString());
                year2.setText(String.valueOf(t + 1));
            }

        });

        plusmonth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int t = Integer.parseInt(month2.getText().toString());
                month2.setText(String.valueOf(t + 1));
                if (t >= 12) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد الاشهر")
                            .setConfirmText("رجــــوع")
                            .show();
                    month2.setText("12");
                    month2.setError("required!");
                    month2.requestFocus();
                    return;
                }
            }
        });

        plusday.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int maxDay=0;
                switch (Integer.parseInt(month2.getText().toString())) {
                    case 1:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.JANUARY, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        break;

                    case 2:

                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.FEBRUARY, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                        break;
                    case 3:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.MARCH, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                        break;
                    case 4:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.APRIL, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                        break;
                    case 5:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.MAY, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        break;
                    case 6:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.JUNE, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        break;

                    case 7:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.JULY, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        break;
                    case 8:

                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.AUGUST, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    case 9:

                        break;
                    case 10:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.SEPTEMBER, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        break;
                    case 11:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.NOVEMBER, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        break;
                    case 12:
                        calendar.set(Integer.parseInt(year2.getText().toString()), Calendar.DECEMBER, 1);
                        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        break;
                }





                int t = Integer.parseInt(day2.getText().toString());
                day2.setText(String.valueOf(t + 1));
                if (t >= maxDay) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد الايام")
                            .setConfirmText("رجــــوع")
                            .show();
                    day2.setText(String.valueOf(maxDay));
                    day2.setError("required!");
                    day2.requestFocus();
                    return;
                }
            }
        });

        minyear.setOnClickListener(new View.OnClickListener() {

            private int t;

            public void onClick(View v) {
                int t = Integer.parseInt(year2.getText().toString());
                t--;
                year2.setText("0"+String.valueOf(t ));
                if (t <=0) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد السنة")
                            .setConfirmText("رجــــوع")
                            .show();
                    year2.setText("2020");
                    year2.setError("required!");
                    year2.requestFocus();
                    return;
                }
            }
        });

        minmonth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int t = Integer.parseInt(month2.getText().toString());
                t--;
                month2.setText("0"+String.valueOf(t - 1));
                if (t <= 1) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد الشهر")
                            .setConfirmText("رجــــوع")
                            .show();
                    month2.setText("1");
                    month2.setError("required!");
                    month2.requestFocus();
                    return;
                }
            }
        });

        minday.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int t = Integer.parseInt(day2.getText().toString());
                t--;
                day2.setText("0"+String.valueOf(t ));
                if (t <= 1 ) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            // .setCustomImage(R.mipmap.icon_delete)
                            .setContentText("تجاوزت حد الايام")
                            .setConfirmText("رجــــوع")
                            .show();
                    day2.setText("1");
                    day2.setError("required!");
                    day2.requestFocus();
                    return;

                }
            }
        });
        ShowRecord();
        return form;
    }






    @Override
    public void onClick(View v) {

    }

    private void displayyear(String num) {
        EditText year = (EditText) form.findViewById(R.id.year2);
        year2.setText(num);
    }

    private void displaymonth(String num) {
        EditText month = (EditText) form.findViewById(R.id.year2);
        month2.setText(num);
    }

    private void displayday(String num) {
        EditText day = (EditText) form.findViewById(R.id.year2);
        day2.setText(num);
    }


    public void save_Payment( ) {



        /*year2 = (EditText) form.findViewById(R.id.year2);
        month2 = (EditText) form.findViewById(R.id.month2);
        day2 = (EditText) form.findViewById(R.id.day2);

        et_Amt = (EditText) form.findViewById(R.id.et_Amt);
        if (et_Amt.getText().toString().length() == 0) {
            et_Amt.setError("required!");
            et_Amt.requestFocus();
            return;
        }
        if ((year2.getText().toString().length() == 0)) {
            year2.setError("required!");
            year2.requestFocus();
            return;
        }

        if (month2.getText().toString().length() == 0) {
            month2.setError("required!");
            month2.requestFocus();
            return;
        }

        if (day2.getText().toString().length() == 0) {
            day2.setError("required!");
            day2.requestFocus();
            return;
        }

        int t1 = Integer.parseInt(day2.getText().toString());
        if (t1 > 31 || t1<=0) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("تجاوزت حد الايام")
                    .setConfirmText("رجــــوع")
                    .show();
            day2.setText("31");
            return;


        }

        int t = Integer.parseInt(month2.getText().toString());
        if (t > 12 || t<=0) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("تجاوزت حد الشهر")
                    .setConfirmText("رجــــوع")
                    .show();
            month2.setText("12");
            return;
        }
        int t2 = Integer.parseInt(year2.getText().toString());
        if (t2 < CurrentYear) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("يجب ان تكون السنة الحالية او اكبر ")
                    .setConfirmText("رجــــوع")
                    .show();
            year2.setText("2019");
            return;


        }

        int x1=0;



        int day = Integer.parseInt(day2.getText().toString());
        int month = Integer.parseInt(month2.getText().toString());

        String Day_Str,Month_Str;
        //String date =String.format("%02d", day, Locale.ENGLISH) +"/" +  String.format("%02d",  month,Locale.ENGLISH)+"/" +  year2.getText().toString();
        if ((day+"").length()==1){
            Day_Str = "0"+ day+"";
        }else{
            Day_Str =   day+"";
        }

        if ((month+"").length()==1){
            Month_Str = "0"+ month+"";
        }else{
            Month_Str =   month+"";
        }

         date =  Month_Str  +"/" +   Day_Str    +"/" +  year2.getText().toString();

        SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String stryear = mdformatyear.format(calendar.getTime());
        displayyear(stryear);

        SimpleDateFormat mdformatmonth = new SimpleDateFormat("MM", Locale.ENGLISH);
        String strmonth = mdformatmonth.format(calendar.getTime());
        displaymonth(strmonth);

        SimpleDateFormat mdformatday = new SimpleDateFormat("dd", Locale.ENGLISH);
        String strday = mdformatday.format(calendar.getTime());
        displayday(strday);


        //Cls_Payment cls_payment_obj = new Cls_Payment();

        cls_payment_obj.setOrderno(getArguments().getString("OrderNo"));
        cls_payment_obj.setDate(date);
        cls_payment_obj.setAmt(et_Amt.getText().toString());
        cls_payment_obj.setNote(et_Note.getText().toString());
        PaymentList.add(cls_payment_obj);

        for (int x = 0; x < PaymentList.size(); x++) {
            cls_payment_obj = new Cls_Payment();
            cls_payment_obj = PaymentList.get(x);
            sum = sum + Double.parseDouble(cls_payment_obj.getAmt().toString());
            x1 = x;
        }

        // Amt = Double.parseDouble(et_Amt.getText().toString());
        NetTotal = Double.parseDouble(getArguments().getString("Net_Total"));

        //sum=0.0;
        if (sum > NetTotal ) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setCustomImage(R.mipmap.icon_delete)
                    .setContentText("يجب ان تكون قيمة الدفعة اقل من قيمة الفاتورة  ")
                    .setConfirmText("رجــــوع")
                    .show();
            et_Amt.setText("");
            return;
        }
        PaymentAdapter paymentAdapter = new PaymentAdapter(
                this.getActivity() , PaymentList);
        lstView.setAdapter(paymentAdapter);
        paymentAdapter.notifyDataSetChanged();*/

        //Toast.makeText(getActivity(),cls_payment_obj.getAmt(),Toast.LENGTH_SHORT).show();
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        if (PaymentList.size() == 0) {


            //alertDialog.setTitle(tv_ScrTitle.getText().toString());
            alertDialog.setMessage("لا يمكن تخزين المستند بدون مواد");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    Intent k = new Intent(getActivity(), Pop_Payments_method.class);
                    startActivity(k);



                }
            });

            alertDialog.show();

            return;

        }



        final String json = new Gson().toJson(PaymentList);
        final Handler _handler = new Handler();

        loadingdialog = ProgressDialog.show(getActivity(), "الرجاء الانتظار ...", "العمل جاري على ترحيل الدفعات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(getActivity());
                ws.SaveInovicePayments(json);
                try {

                    if (We_Result.ID > 0) {

                        /*String query = " Update  SaleManRounds  set Posted=1  where Posted = '-1'";
                        sqlHandler.executeQuery(query);

                        query = " delete from   SaleManRounds   where Posted =1 and  DayNum < " + dayOfWeek;
                        sqlHandler.executeQuery(query)*/
                        ;


                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        getActivity()).create();
                                alertDialog.setTitle("ترحيل الدفعات ");
                                alertDialog.setMessage("تمت عملية ترحيل الدفعات بنجاح");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        deletsqlite();

                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();


                            }
                        });
                    }
                    else
                    {

                        loadingdialog.dismiss();


                    }
                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("فشل في عمليه الاتصال");
                            alertDialog.setMessage(e.getMessage().toString());
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertDialog.show();
                        }
                    });
                }
            }
        }).start();




    }
    public void deletsqlite()
    {
       /* SqlHandler sql_Handler = new SqlHandler(getActivity());

        String  q = " delete from Pay_method";
        sql_Handler.executeQuery(q);

        PaymentAdapter paymentAdapter = new PaymentAdapter(
                this.getActivity(), PaymentList);
        lstView.setAdapter(null);
        paymentAdapter.notifyDataSetChanged();*/
    }

    public int add_Item() {



        year2 = (EditText) form.findViewById(R.id.year2);
        month2 = (EditText) form.findViewById(R.id.month2);
        day2 = (EditText) form.findViewById(R.id.day2);

        et_Amt = (EditText) form.findViewById(R.id.et_Amt);
        if (et_Amt.getText().toString().length() == 0) {
            et_Amt.setError("required!");
            et_Amt.requestFocus();
            return 1;
        }
        if ((year2.getText().toString().length() == 0)) {
            year2.setError("required!");
            year2.requestFocus();
            return 1;
        }

        if (month2.getText().toString().length() == 0) {
            month2.setError("required!");
            month2.requestFocus();
            return 1;
        }

        if (day2.getText().toString().length() == 0) {
            day2.setError("required!");
            day2.requestFocus();
            return 1;
        }
        Calendar dateCal = Calendar.getInstance();
        dateCal.set(Integer.parseInt(year2.getText().toString()), Integer.parseInt(month2.getText().toString()), 2);
        int maxDay = dateCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int t1 = Integer.parseInt(day2.getText().toString());
        if (t1 > maxDay || t1<=0) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("تجاوزت حد الايام")
                    .setConfirmText("رجــــوع")
                    .show();
            day2.setText("31");
            return 1;


        }

        int t = Integer.parseInt(month2.getText().toString());
        if (t > 12 || t<=0) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("تجاوزت حد الشهر")
                    .setConfirmText("رجــــوع")
                    .show();
            month2.setText("12");
            return 1;
        }
        int t2 = Integer.parseInt(year2.getText().toString());
        if (t2 < CurrentYear) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    //.setCustomImage(R.mipmap.icon_delete)
                    .setContentText("يجب ان تكون السنة الحالية او اكبر ")
                    .setConfirmText("رجــــوع")
                    .show();
            SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            String stryear = mdformatyear.format(calendar.getTime());
            year2.setText(stryear);
            return 1;


        }

        int x1=0;



        int day = Integer.parseInt(day2.getText().toString());
        int month = Integer.parseInt(month2.getText().toString());

        String Day_Str,Month_Str;
        //String date =String.format("%02d", day, Locale.ENGLISH) +"/" +  String.format("%02d",  month,Locale.ENGLISH)+"/" +  year2.getText().toString();
        if ((day+"").length()==1){
            Day_Str = "0"+ day+"";
        }else{
            Day_Str =   day+"";
        }

        if ((month+"").length()==1){
            Month_Str = "0"+ month+"";
        }else{
            Month_Str =   month+"";
        }

        date =  Day_Str   +"/" +   Month_Str   +"/" +  year2.getText().toString();

        SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String stryear = mdformatyear.format(calendar.getTime());
        displayyear(stryear);

        SimpleDateFormat mdformatmonth = new SimpleDateFormat("MM", Locale.ENGLISH);
        String strmonth = mdformatmonth.format(calendar.getTime());
        displaymonth(strmonth);

        SimpleDateFormat mdformatday = new SimpleDateFormat("dd", Locale.ENGLISH);
        String strday = mdformatday.format(calendar.getTime());
        displayday(strday);




        sum=0.0;
      /*  for (int x = 0; x < PaymentList.size(); x++) {
            cls_payment_obj = new Cls_Payment();
            cls_payment_obj = PaymentList.get(x);
            sum = sum + Double.parseDouble(cls_payment_obj.getInoviceAmt());
            x1 = x;
        }*/

        // Amt = Double.parseDouble(et_Amt.getText().toString());
        NetTotal = Double.parseDouble(getArguments().getString("Net_Total"));

        //sum=0.0;
    /*    if (sum + Double.parseDouble(et_Amt.getText().toString()) > NetTotal ) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setCustomImage(R.mipmap.icon_delete)
                    .setContentText("يجب ان تكون قيمة الدفعة اقل من قيمة الفاتورة  ")
                    .setConfirmText("رجــــوع")
                    .show();
            et_Amt.setText("");
            return 1;*/
      //  }else  {

            String UserID = sharedPreferences.getString("UserID", "");
            //  String custNo = sharedPreferences.getString("CustNo", "");

            /*Cls_Payment cls_payment_obj = new Cls_Payment();
            cls_payment_obj.setCustNo(getArguments().getString("custNo"));
            cls_payment_obj.setManNo(UserID);
            cls_payment_obj.setOrderNo(getArguments().getString("OrderNo"));
           // cls_payment_obj.setAmt(getArguments().getString("Net_Total"));
            NetTotal = Double.parseDouble(getArguments().getString("Net_Total"));
            cls_payment_obj.setTr_date(date);
            cls_payment_obj.setNotes(et_Note.getText().toString());

            cls_payment_obj.setInoviceAmt(et_Amt.getText().toString());

            // Toast.makeText(this.getActivity(),et_Amt.getText()+"    الدفعة",Toast.LENGTH_LONG).show();*/
           sql_Handler = new SqlHandler(this.getActivity());
            String q = "insert into Pay_method (manNo,custNo,orderNo,Amt,Tr_date,Notes,InoviceAmt) values ( '" +
                    UserID
                    + "','" + getArguments().getString("custNo")
                    + "','" + getArguments().getString("OrderNo")
                    + "','" + et_Amt.getText().toString()
                    + "','" + date
                    + "','" + et_Note.getText().toString()
                    + "','" + getArguments().getString("Net_Total")
                    + "')";

            sql_Handler.executeQuery(q);
           // PaymentList.add(cls_payment_obj);

          //  PaymentAdapter paymentAdapter = new PaymentAdapter(          this.getActivity(), PaymentList);
           // lstView.setAdapter(paymentAdapter);
          //  paymentAdapter.notifyDataSetChanged();


            ShowRecord();



            return 1;
       // }





    }
    public void ShowRecord()
    {
        SqlHandler sql_Handler = new SqlHandler(this.getActivity());
      String  ss= getArguments().getString("OrderNo");

        String query = "Select * from Pay_method  where orderNo='"+ getArguments().getString("OrderNo")+"'";//   Customers.Cust_type='"+Cust_type+"'";

        PaymentList.clear();
        Cursor c = sql_Handler.selectQuery(query);

        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
                do{


                    //  String custNo = sharedPreferences.getString("CustNo", "");

                    Cls_Payment cls_payment_obj = new Cls_Payment();


                    cls_payment_obj.setCustNo(c.getString(c.getColumnIndex("custNo")));
                    cls_payment_obj.setManNo(c.getString(c.getColumnIndex("manNo")));
                    cls_payment_obj.setOrderNo(c.getString(c.getColumnIndex("orderNo")));
                    cls_payment_obj.setAmt(c.getString(c.getColumnIndex("Amt")));
                    cls_payment_obj.setTr_date(c.getString(c.getColumnIndex("Tr_date")));
                    cls_payment_obj.setNotes(c.getString(c.getColumnIndex("Notes")));
                    cls_payment_obj.setInoviceAmt(c.getString(c.getColumnIndex("InoviceAmt")));
                    cls_payment_obj.setNo(c.getInt(c.getColumnIndex("no")));
                    PaymentList.add(cls_payment_obj);

                    PaymentAdapter paymentAdapter = new PaymentAdapter(
                            this.getActivity(), PaymentList);
                    lstView.setAdapter(paymentAdapter);
                    paymentAdapter.notifyDataSetChanged();

                }while (c.moveToNext());
            }
            c.close();
        }

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose your option");
        menu.add(0, v.getId(), 0, "Call");
        menu.add(0,v.getId(),0,"Message");
        menu.add(0,v.getId(),0,"Mail");
    }
  /*  @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId()==R.id.edit){
            Cls_Payment  cls_payment = PaymentList.get(position1);

            et_Amt.setText(cls_payment.getAmt());
            et_Note.setText(cls_payment.getNote());
            String date=cls_payment.getDate();

            String[] seperatedTime= date.split("/");
               year2.setText(Integer.parseInt(seperatedTime[0]));
          month2.setText(Integer.parseInt(seperatedTime[1]));
            day2.setText( Integer.parseInt(seperatedTime[2]));


            PaymentList.remove(position1);
        }
        else if(item.getItemId()==R.id.remove){
            PaymentList.remove(position1);
        }else{
            return false;
        }
        return true;
    }*/

    public void setData(String a)
    {



        //  EditText et_Amt = (EditText) form.findViewById(R.id.et_Amt);
        String w=a;
        //   Toast.makeText(getContext(),"adse",Toast.LENGTH_LONG).show();
        textView275.setText(a);


    }
}





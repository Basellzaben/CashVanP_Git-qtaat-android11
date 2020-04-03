package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Man_Vac;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class Pop_Man_Vac extends DialogFragment implements View.OnClickListener {
   ImageView imgFrom,imgTo;
    public int FlgDate = 0;
    View form;
   boolean IsNew ;
    long Result;
    private Calendar calendar;
    private TextView dateView1;
    private int year, month, day;
    Button bt_GetData,bt_add,back,bt_Reset;
    SwipeMenuListView items_Lsit ;
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    ArrayList<Cls_Man_Vac> PaymentList;
    SqlHandler sql_Handler;
    RadioButton  rdoAnnual,rdoSick;
    EditText et_Note,tv_FromDate,tv_ToDate;
    TextView textView275;
    String orderno,Date,Id;
    Button minday, minmonth, minyear, plusyear, plusmonth, plusday;
    EditText year2, month2, day2;
    int CurrentYear;
    SwipeMenuCreator creator;
    SharedPreferences sharedPreferences;
    Cls_Payment cls_payment_obj ;
int List_index;
    public ProgressDialog loadingdialog;
    String currentDateandTime;
    String UserID,VacType;
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    Calendar myCalendar = Calendar.getInstance() ;//global
    public void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String M = intToString(Integer.valueOf(monthOfYear+1), 2);
            String D = intToString(Integer.valueOf(dayOfMonth), 2);
            if(FlgDate==1) {
                tv_FromDate.setText(year + "/" + M + "/" + D);
            }else{
                tv_ToDate.setText(year + "/" + M + "/" + D);
            }
        }

    };
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
        form= inflater.inflate(R.layout.pop_man_vac, container, false);

        Calendar c = Calendar.getInstance();
        c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

        items_Lsit = (SwipeMenuListView) form.findViewById(R.id.Lst);
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                try {
                    SwipeMenuItem Approve = new SwipeMenuItem(getActivity());
                    Approve.setBackground(R.color.Blue);
                    Approve.setWidth(200);
                    Approve.setTitle("تعديل");
                    //Approve.setIcon(R.mipmap.edite_white);
                    Approve.setTitleSize(16);
                    Approve.setTitleColor(Color.WHITE);
                    menu.addMenuItem(Approve);

                    SwipeMenuItem NotApprove = new SwipeMenuItem(getActivity());
                    NotApprove.setBackground(R.color.Red);
                    NotApprove.setWidth(200);
                    NotApprove.setTitle("حذف  ");
                    // NotApprove.setIcon(R.mipmap.not_done_white);
                    NotApprove.setTitleSize(16);
                    NotApprove.setTitleColor(Color.CYAN);
                    menu.addMenuItem(NotApprove);
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        };
        items_Lsit.setMenuCreator(creator);
        items_Lsit.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

                List_index = position;
                items_Lsit.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
        items_Lsit.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 1:
                        List_index = position;
                        Delete_Problem(position);
                        break;
                    case 0:
                        List_index = position;
                        Edite_Problem(position);

                        break;
                }

                return false;
            }
        });

           /* items_Lsit.setSwipeDirection(DIRECTION_RIGHT);
            items_Lsit.setSwipeDirection(DIRECTION_LEFT);


            items_Lsit.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

                @Override
                public void onSwipeStart(int position) {
                    // swipe start
                }

                @Override
                public void onSwipeEnd(int position) {
                    // swipe end
                }
            });
*/

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_Man_Vac obj = (Cls_Man_Vac) items_Lsit.getItemAtPosition(position);
                Edite_Problem(position);


            }
        });



        PaymentList = new ArrayList<Cls_Man_Vac>();
        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),
                        "Click ListItem Number " + i, Toast.LENGTH_LONG)
                        .show();
                registerForContextMenu(items_Lsit);

            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UserID = sharedPreferences.getString("UserID", "");
        sql_Handler = new SqlHandler(this.getActivity());

        textView275=(TextView) form.findViewById(R.id.textView275);


        et_Note=(EditText)form.findViewById(R.id.et_Note);

        imgFrom = (ImageView) form.findViewById(R.id.imgFrom);
        imgTo = (ImageView) form.findViewById(R.id.imgTo);
        plusyear = (Button) form.findViewById(R.id.plusyear);
        plusmonth = (Button) form.findViewById(R.id.plusmonth);
        plusday = (Button) form.findViewById(R.id.plusday);
        minyear = (Button) form.findViewById(R.id.minyear);
        minmonth = (Button) form.findViewById(R.id.minmonth);
        minday = (Button) form.findViewById(R.id.minday);

        year2 = (EditText) form.findViewById(R.id.year2);
        month2 = (EditText) form.findViewById(R.id.month2);
        day2 = (EditText) form.findViewById(R.id.day2);
        bt_GetData=(Button) form.findViewById(R.id.bt_GetData);
        bt_add=(Button) form.findViewById(R.id.btn_add_item1);
        back=(Button) form.findViewById(R.id.back);
        bt_Reset=(Button) form.findViewById(R.id.bt_Reset);
        calendar = Calendar.getInstance();
        year = c.get(Calendar.YEAR);

        rdoAnnual=(RadioButton) form.findViewById(R.id.rdoAnnual);
        rdoSick=(RadioButton) form.findViewById(R.id.rdoSick);


        month = c.get(Calendar.MONTH);

        day = c.get(Calendar.DAY_OF_MONTH);

        tv_FromDate = (EditText) form.findViewById(R.id.tv_FromDate);
        tv_ToDate = (EditText) form.findViewById(R.id.tv_ToDate);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
          currentDateandTime = sdf.format(new Date());
        tv_FromDate.setText(currentDateandTime);
        tv_ToDate.setText(currentDateandTime);



        imgFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlgDate = 1;
                showDatePickerDialog();
            }
        });

        imgTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlgDate = 2;
                showDatePickerDialog();
            }
        });




        bt_GetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  save_Payment();
                 GetData();

            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 add_Item();


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });

        bt_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rest();

            }
        });




       Id="-1";
       IsNew = true;
       GetData();
        return form;
    }



private void Rest(){
        IsNew = true;
        et_Note.setText("");
        Id="-1";
    tv_FromDate.setText(currentDateandTime);
    tv_ToDate.setText(currentDateandTime);
}
    private void Edite_Problem(final int position) {
        Cls_Man_Vac obj = (Cls_Man_Vac) items_Lsit.getItemAtPosition(position);

        if(!obj.getProcedureType().equalsIgnoreCase("3") ){
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setContentText("تم إتخاذ إجراء لا يمكن التعديل")
                    .setTitleText("العطل والإجازات")

                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
            return;
        }

        if(!obj.getVacationType().equalsIgnoreCase("1") && !obj.getVacationType().equalsIgnoreCase("2")) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setContentText("لا يمكن التعديل على العطل الرسمية")
                    .setTitleText("العطل والإجازات")

                    .setCustomImage(R.drawable.error_new)
                    .setConfirmText("رجــــوع")
                    .show();
       return;
        }
        et_Note.setText(obj.getNote());
        tv_FromDate.setText(obj.getFromDate());
        tv_ToDate.setText(obj.getToDate());
        Id=obj.getId();
        if(obj.getVacationType().equalsIgnoreCase("1")){
            rdoAnnual.setChecked(true);
        }else{
            rdoSick.setChecked(true);
        }

        IsNew = false;

    }

    private void Delete_Problem(final int position) {
       new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("هل انت متأكد من عملية الحذف ؟")
                //.setContentText("الاصلاحات والأعطال")
                .setConfirmText("نعم")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Cls_Man_Vac obj = (Cls_Man_Vac) items_Lsit.getItemAtPosition(position);
                        new MaintCardPosting().execute(obj.getId(),"2");
                    }
                })
                .setCancelButton("لا", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();


    }

    @Override
    public void onClick(View v) {

    }



    public void add_Item() {


        if ((tv_FromDate.getText().toString().length() == 0)) {
            tv_FromDate.setError("required!");
            tv_FromDate.requestFocus();
            return  ;
        }

        if (tv_ToDate.getText().toString().length() == 0) {
            tv_ToDate.setError("required!");
            tv_ToDate.requestFocus();
            return  ;
        }

        if(rdoAnnual.isChecked()==true) {
            VacType="1";
        }else {
            VacType="2";
        }
       if(Id.equalsIgnoreCase("-1")){
           new MaintCardPosting().execute("-1","1");
       }else{
           new MaintCardPosting().execute(Id,"3");
       }

    }



    private class MaintCardPosting extends AsyncTask<String, Void, Void> {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {

            tv = new TextView(getActivity());
            lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);



            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على حفظ البيانات  ");
            tv.setText("الإجازات");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            CallWebServices ws = new CallWebServices(getActivity());
            Result = ws.PostManVac(params[0],UserID,tv_FromDate.getText().toString(),tv_ToDate.getText().toString(),et_Note.getText().toString(),"1",VacType,params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Rest();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

            alertDialog.setMessage("تمت العملية الحفظ بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setTitle("طلب إجازة" );
            if (Result == -5 ) {
               new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("العملية لم تتم بنجاح")
                        .setContentText(We_Result.Msg)
                        .setCustomImage(R.drawable.error_new)
                        .setConfirmText("رجــــوع")
                        .show();


            }
                else if (Result == -2) {
                alertDialog.setTitle("طلب إجازة");
                alertDialog.setMessage(We_Result.Msg);
                alertDialog.setIcon(R.drawable.error_new);


                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("العملية لم تتم بنجاح")
                        .setContentText(We_Result.Msg)
                        .setCustomImage(R.drawable.error_new)
                        .setConfirmText("رجــــوع")
                        .show();


            } else if (Result > 0) {
                GetData();
                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("طلب إجازة" )
                        .setContentText("تمت العملية  بنجاح ")
                        .setConfirmText("رجــــوع")
                        .show();

            } else {

                new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("طلب إجازة" )
                        .setContentText("العملية  لم تتم بنجاح")
                        .setCustomImage(R.drawable.error_new)
                        .setConfirmText("رجــــوع")
                        .show();



            }
            progressDialog.dismiss();
            //alertDialog.show();
        }

    }
    public void ShowList(){
        String query = " Select Id,ManId,CustId,FromDate,ToDate,Note ,ProcedureType,VacationType  , ProcedureType_Desc , VacationType_Desc,VacDays from Man_Vac  where ManId='"+   UserID +"'";//   Customers.Cust_type='"+Cust_type+"'";
        PaymentList.clear();
        Cursor c = sql_Handler.selectQuery(query);

        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
                do{
                    Cls_Man_Vac cls_payment_obj = new Cls_Man_Vac();
                    cls_payment_obj.setId(c.getString(c.getColumnIndex("Id")));
                    cls_payment_obj.setManId(c.getString(c.getColumnIndex("ManId")));
                    cls_payment_obj.setCustId(c.getString(c.getColumnIndex("CustId")));
                    cls_payment_obj.setFromDate(c.getString(c.getColumnIndex("FromDate")));
                    cls_payment_obj.setToDate(c.getString(c.getColumnIndex("ToDate")));
                    cls_payment_obj.setNote(c.getString(c.getColumnIndex("Note")));
                    cls_payment_obj.setProcedureType(c.getString(c.getColumnIndex("ProcedureType")));
                    cls_payment_obj.setVacationType(c.getString(c.getColumnIndex("VacationType")));
                    cls_payment_obj.setProcedureType_Desc(c.getString(c.getColumnIndex("ProcedureType_Desc")));
                    cls_payment_obj.setVacationType_Desc(c.getString(c.getColumnIndex("VacationType_Desc")));
                    cls_payment_obj.setVacDays(c.getString(c.getColumnIndex("VacDays")));
                    PaymentList.add(cls_payment_obj);

                }while (c.moveToNext());
            }
            c.close();
        }
        ManvacAdapter paymentAdapter = new ManvacAdapter(
                this.getActivity(), PaymentList);
        items_Lsit.setAdapter(paymentAdapter);
       // paymentAdapter.notifyDataSetChanged();

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

    public void setData(String a)
    {




        String w=a;

        textView275.setText(a);


    }
    public void GetData() {
        sql_Handler.executeQuery("Delete from Man_Vac");
        ShowList();
        tv = new TextView(getActivity());
        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

        final Handler _handler = new Handler();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(getActivity());
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("الإجازات");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        String MaxSeer = "1";


        final String Ser = "1";


        new Thread(new Runnable() {
            @Override
            public void run() {




                CallWebServices ws = new CallWebServices(getActivity());
                ws.GetManVacations(UserID );
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Id = js.getJSONArray("Id");
                    JSONArray js_ManId= js.getJSONArray("ManId");
                    JSONArray js_CustId = js.getJSONArray("CustId");
                    JSONArray js_FromDate = js.getJSONArray("FromDate");
                    JSONArray js_ToDate = js.getJSONArray("ToDate");
                    JSONArray js_Note = js.getJSONArray("Note");
                    JSONArray js_ProcedureType = js.getJSONArray("ProcedureType");
                    JSONArray js_VacationType = js.getJSONArray("VacationType");
                    JSONArray js_ProcedureType_Desc= js.getJSONArray("ProcedureType_Desc");
                    JSONArray js_VacationType_Desc = js.getJSONArray("VacationType_Desc");
                    JSONArray js_VacDays = js.getJSONArray("VacDays");


                    q = "Delete from Man_Vac";
                    sql_Handler.executeQuery(q);
                    q = "delete from sqlite_sequence where name='Man_Vac'";
                    sql_Handler.executeQuery(q);

                    for (i = 0; i < js_Id.length(); i++) {
                        q = "Insert INTO Man_Vac( Id,ManId,CustId,FromDate,ToDate,Note ,ProcedureType,VacationType, ProcedureType_Desc , VacationType_Desc ,VacDays) values ("

                                + "'" + js_Id.get(i).toString()
                                + "','" + js_ManId.get(i).toString()
                                + "','" + js_CustId.get(i).toString()
                                + "','" + js_FromDate.get(i).toString()
                                + "','" + js_ToDate.get(i).toString()
                                + "','" + js_Note.get(i).toString()
                                + "','" + js_ProcedureType.get(i).toString()
                                + "','" + js_VacationType.get(i).toString()
                                + "','" + js_ProcedureType_Desc.get(i).toString()
                                + "','" + js_VacationType_Desc.get(i).toString()
                                + "','" + js_VacDays.get(i).toString()
                                + "' )";
                        sql_Handler.executeQuery(q);
                        custDialog.setMax(js_Id.length());
                        custDialog.incrementProgressBy(1);
                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();
                            ShowList();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث الإجازات");
                            alertDialog.setMessage("تمت عملية التحديث بنجاح");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                           // alertDialog.show();


                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث الإجازات");
                            alertDialog.setMessage("لا يوجد بيانات");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                           // alertDialog.show();
                        }
                    });
                }
            }
        }).start();
    }
}





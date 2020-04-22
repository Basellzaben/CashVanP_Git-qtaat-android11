package com.cds_jo.GalaxySalesApp.Reports;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.CustomerSummary.CustomerManVisitAdabter;
import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_Bill;
import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_BillC;
import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_ManVisit;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.Select_Cash_Customer;
import com.cds_jo.GalaxySalesApp.Select_Customer;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Methdes.MyTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Report_Home extends AppCompatActivity {
    String ManNo,CustNo,CoutNo;
    Cls_Listtitle obj;
    ImageView imgFrom,imgTo;
    ListView listView,listView1;
    EditText et_Man,editText5,ezditText5;
    public int FlgDate = 0;
    EditText et_fromDate;
    cls_VisitingInformation  cls_VisitingInformation;
    EditText et_Todate;
    cls_DelegateInformation cls_delegateInformation;
    String UserID;
    DelegateInformation_Adapter delegateInformation_adapter;
    VisitingInformationAdapter visitingInformationAdapter;
    achievement_rate_Adapter achievement_rate_adapter;
    Receipt_Adapter receipt_adapter;
    net_profit_Adapter net_profit_adapter;
    listtitleadapter listtitleadapter;
    ArrayList<Cls_Listtitle>  cls_listtitles = new ArrayList<Cls_Listtitle>();
    ArrayList<cls_VisitingInformation>  vlist;
    ArrayList<cls_DelegateInformation>  Dlist;
    ArrayList<cls_Receipt>  Rlist;
    ArrayList<cls_achievement_rate>  ARlist;
    ArrayList<cls_net_profit>  NPlist;
    ArrayList<cls_sales> listDataHeader;
    ExpandableListView lst_acc;
    HashMap<List<cls_sales>, List<cls_salesC>> listDataChild;
    final Handler _handler = new Handler();

    LinearLayout receipt;
    LinearLayout DelegateInformation;
    LinearLayout Data;
    int  x ;

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }


    Calendar myCalendar = Calendar.getInstance() ;//global
    public void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(Report_Home.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
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
                et_fromDate.setText(year + "/" + M + "/" + D);
            }else{
                et_Todate.setText(year + "/" + M + "/" + D);
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__home);
        cls_listtitles.clear();
        listView=(ListView)findViewById(R.id.LV);
        listView1=(ListView)findViewById(R.id.listView1);
        lst_acc=(ExpandableListView) findViewById(R.id.lst_acc);
        et_fromDate=(EditText) findViewById(R.id.et_fromDate);
        editText5=(EditText) findViewById(R.id.editText5);
        et_Todate=(EditText)findViewById(R.id.et_Todate);
        imgFrom = (ImageView) findViewById(R.id.imgFrom);
        imgTo = (ImageView) findViewById(R.id.imgTo);
        et_Man=(EditText)findViewById(R.id.et_Man);
        ezditText5=(EditText)findViewById(R.id.ezditText5);

        receipt=(LinearLayout)findViewById(R.id.receipt);
        DelegateInformation=(LinearLayout)findViewById(R.id.DelegateInformation);

        Data=(LinearLayout)findViewById(R.id.Data);



        FillList();
        listtitleadapter = new listtitleadapter(Report_Home.this, cls_listtitles);
        listView.setAdapter(listtitleadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(et_Man.getText().toString().equals(""))
                {
                    new SweetAlertDialog(Report_Home.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("يجب أختيار المندوب")
                            .setConfirmText("رجــــوع")
                            .show();
                    et_Man.setError("required!");
                    et_Man.requestFocus();
                    return;
                } else if(editText5.getText().toString().equals(""))
                {
                    new SweetAlertDialog(Report_Home.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("يجب أختيار العميل")
                            .setConfirmText("رجــــوع")
                            .show();
                    editText5.setError("required!");
                    editText5.requestFocus();
                    return;
                }else if(ezditText5.getText().toString().equals(""))
                {
                    new SweetAlertDialog(Report_Home.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("يجب أختيار المنطفة")
                            .setConfirmText("رجــــوع")
                            .show();
                    ezditText5.setError("required!");
                    ezditText5.requestFocus();
                    return;
                }else if(et_fromDate.getText().toString().equals(""))
                {
                    new SweetAlertDialog(Report_Home.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("يجب أدخال تاريخ بداية الفترة")
                            .setConfirmText("رجــــوع")
                            .show();
                    et_fromDate.setError("required!");
                    et_fromDate.requestFocus();
                    return;
                }else if(et_Todate.getText().toString().equals(""))
                {
                    new SweetAlertDialog(Report_Home.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            //.setCustomImage(R.mipmap.icon_delete)
                            .setContentText("يجب أدخال تاريخ نهاية الفترة")
                            .setConfirmText("رجــــوع")
                            .show();
                    et_Todate.setError("required!");
                    et_Todate.requestFocus();
                    return;
                }
                vlist = new ArrayList<cls_VisitingInformation>();
                Rlist = new ArrayList<cls_Receipt>();
                obj =cls_listtitles.get(position);
                 x=Integer.parseInt(obj.getNo());
                ReportId.Id=x;

                receipt.setVisibility(View.GONE);
                Data.setVisibility(View.GONE);
                DelegateInformation.setVisibility(View.GONE);
                lst_acc.setVisibility(View.GONE);
                listView1.setAdapter(null);
                if(x==1)
                {


                    listView1.setVisibility(View.VISIBLE);

                    getVisitingInformation();
                }
                else if(x==2||x==3||x==4)
                {
                    if(x==4)
                {
                    Data.setWeightSum((float) 10);
                    MyTextView Is_damge = (MyTextView) findViewById(R.id.Is_damge);
                    Is_damge.setVisibility(View.VISIBLE);

                }


                    listView1.setVisibility(View.VISIBLE);

                    getdata();
                }
                else if(x==5)
                {

                    getreceipt();
                }
                else if(x==6)
                {
                   RouteScore();
                }
                else if(x==7)
                {
                    getManSummery();

                }
                else if(x==8)
                {
                    receipt.setVisibility(View.GONE);
                    Data.setVisibility(View.GONE);
                    DelegateInformation.setVisibility(View.GONE);
                    listView1.setVisibility(View.VISIBLE);
                    lst_acc.setVisibility(View.GONE);
                    getnet_profit();
                }
            }
        });

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


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                cls_sales o = (cls_sales) listView1.getItemAtPosition(position);
                showDtl(o.getOrderNo(),o.getOrderType(),o.getNetTotal()+"");
            }
        });

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    private  void showDtl(String DocNo , String OrderType,String Amt){



            Bundle bundle = new Bundle();
            bundle.putString("Scr", "po");
            bundle.putString("DocNo", DocNo);
            bundle.putString("OrderType", OrderType);
            bundle.putString("Amt", Amt);
            FragmentManager Manager = getFragmentManager();
            PopShowInvoiceDtl obj = new PopShowInvoiceDtl();
            obj.setArguments(bundle);
            obj.show(Manager, null);


    }
    public void Set_Cust(String No, String Nm) {
        CustNo=No;
        editText5.setText(Nm);
        editText5.setError(null);
    }
    public void Set_Country(String No, String Nm) {
        CoutNo=No;
        et_Man.setText(Nm);
        et_Man.setError(null);
    }
    public void Set_Man(String No, String Nm) {
        ManNo=No;
        ezditText5.setText(Nm);
        ezditText5.setError(null);
    }
    public void showCust(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "Rep_Cust");
                FragmentManager Manager = getFragmentManager();
                Select_Customer obj = new Select_Customer();
                obj.setArguments(bundle);
                obj.show(Manager, null);



    }
    public void getCountry(View view) {


        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Rep_Cout");
        FragmentManager Manager = getFragmentManager();
        Select_Country obj = new Select_Country();
        obj.setArguments(bundle);
        obj.show(Manager, null);



    }
    public void getMan(View view) {


        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Rep_Man");
        FragmentManager Manager = getFragmentManager();
        Select_Man obj = new Select_Man();
        obj.setArguments(bundle);
        obj.show(Manager, null);



    }
    private void getnet_profit() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_Report_Home("-1","-1","-1","-1","01-01-2020","01-01-2021","-1","-1","-1");
                try {
                    if (We_Result.ID>0){
                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray SalesAmt = js.getJSONArray("SalesAmt");
                    JSONArray PaymentAmt = js.getJSONArray("PaymentAmt");



                    cls_net_profit cls_net_profit= new cls_net_profit();

                    for (i = 0; i < PaymentAmt.length(); i++) {
                        cls_net_profit.setSalesAmt(SalesAmt.get(i).toString());
                        cls_net_profit.setPaymentAmt(PaymentAmt.get(i).toString());




                        NPlist.add(cls_net_profit);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            net_profit_adapter = new net_profit_Adapter(Report_Home.this, NPlist);
                            listView1.setAdapter(net_profit_adapter);
                        }
                    });}
                    else
                    {
                        Toast.makeText(Report_Home.this,"لا يوجد بيانات",Toast.LENGTH_LONG).show();
                    }

                } catch (final Exception e) {

                }

            }
        };
        thread.start();

    }

    private void RouteScore() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_Report_Home_RouteScore("-1","-1","13","-1","01-01-2020","01-01-2021","-1","-1","-1");
                try {
                    if(We_Result.ID>0) {
                        Integer i;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ManNo = js.getJSONArray("ManNo");
                        JSONArray js_ManName = js.getJSONArray("ManName");
                        JSONArray js_Tr_Date = js.getJSONArray("Tr_Date");
                        JSONArray js_TotalCust = js.getJSONArray("TotalCust");
                        JSONArray js_Visited = js.getJSONArray("Visited");
                        JSONArray js_VisitPrecent = js.getJSONArray("VisitPrecent");
                        JSONArray js_SuccVisit = js.getJSONArray("SuccVisit");
                        JSONArray js_SuccPercent = js.getJSONArray("SuccPercent");
                        JSONArray js_Score = js.getJSONArray("Score");


                        cls_achievement_rate cls_achievement_rate = new cls_achievement_rate();

                        for (i = 0; i < js_ManNo.length(); i++) {
                            cls_achievement_rate = new cls_achievement_rate();
                            cls_achievement_rate.setManName(js_ManNo.get(i).toString());
                            cls_achievement_rate.setManName(js_ManName.get(i).toString());
                            cls_achievement_rate.setTr_Date(js_Tr_Date.get(i).toString());
                            cls_achievement_rate.setTotalCust(js_TotalCust.get(i).toString());
                            cls_achievement_rate.setVisited(js_Visited.get(i).toString());
                            cls_achievement_rate.setVisitPrecent(js_VisitPrecent.get(i).toString());
                            cls_achievement_rate.setSuccVisit(js_SuccVisit.get(i).toString());
                            cls_achievement_rate.setVisitPrecent(js_SuccPercent.get(i).toString());
                            cls_achievement_rate.setScore(js_Score.get(i).toString());
                            ARlist.add(cls_achievement_rate);
                        }
                        _handler.post(new Runnable() {
                            public void run() {

                                achievement_rate_adapter = new achievement_rate_Adapter(Report_Home.this, ARlist);
                                listView1.setAdapter(receipt_adapter);
                            }
                        });
                    } else
                    {
                        Toast.makeText(Report_Home.this,"لا يوجد بيانات",Toast.LENGTH_LONG).show();
                    }

                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {

                            Toast.makeText(Report_Home.this,   e.getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        };
        thread.start();
    }

    private void getManSummery() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_Report_Home_RouteActivity("-1","-1","6","-1","01-01-2020","01-01-2021","-1","-1","-1");
                try {
                    if(We_Result.ID>0) {
                        Integer i;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray ManNo = js.getJSONArray("ManNo");
                        JSONArray ManNm = js.getJSONArray("ManNm");
                        JSONArray CheckIn = js.getJSONArray("CheckIn");
                        JSONArray checkOut = js.getJSONArray("checkOut");
                        JSONArray Payemnt = js.getJSONArray("Payemnt");
                        JSONArray Sales = js.getJSONArray("Sales");
                        JSONArray returnsSales = js.getJSONArray("returnsSales");
                        JSONArray SalesOrders = js.getJSONArray("SalesOrders");
                        JSONArray Precent = js.getJSONArray("Precent");
                        JSONArray NewCustomers = js.getJSONArray("NewCustomers");

                        cls_delegateInformation = new cls_DelegateInformation();

                        for (i = 0; i < ManNo.length(); i++) {
                            cls_delegateInformation = new cls_DelegateInformation();
                            cls_delegateInformation.setManNo1(ManNo.get(i).toString());
                            cls_delegateInformation.setManNm(ManNm.get(i).toString());
                            cls_delegateInformation.setCheckIn(CheckIn.get(i).toString());
                            cls_delegateInformation.setCheckOut(checkOut.get(i).toString());
                            cls_delegateInformation.setPayemnt(Payemnt.get(i).toString());
                            cls_delegateInformation.setSales(Sales.get(i).toString());
                            cls_delegateInformation.setReturnsSales(returnsSales.get(i).toString());
                            cls_delegateInformation.setSalesOrders(SalesOrders.get(i).toString());
                            cls_delegateInformation.setPrecent(Precent.get(i).toString());

                            Dlist.add(cls_delegateInformation);
                        }
                        _handler.post(new Runnable() {
                            public void run() {

                                delegateInformation_adapter = new DelegateInformation_Adapter(Report_Home.this, Dlist);
                                listView1.setAdapter(delegateInformation_adapter);
                            }
                        });
                    } else
                    {
                        Toast.makeText(Report_Home.this,"لا يوجد بيانات",Toast.LENGTH_LONG).show();
                    }
                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {

                            Toast.makeText(Report_Home.this,   e.getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        };
        thread.start();
    }

    private void getreceipt() {

        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_Report_Payments("-1","-1","5","-1","01-01-2020","01-01-2021","-1","-1","-1");
                try {
                    if(We_Result.ID>0) {
                        Integer i;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray OrderNo = js.getJSONArray("OrderNo");
                        JSONArray Date = js.getJSONArray("Date");
                        JSONArray Amt = js.getJSONArray("Amt");
                        JSONArray Cash = js.getJSONArray("Cash");
                        JSONArray CheckTotal = js.getJSONArray("CheckTotal");
                        JSONArray notes = js.getJSONArray("notes");
                        JSONArray CustName = js.getJSONArray("CustName");
                        JSONArray manName = js.getJSONArray("manName");


                        cls_Receipt cls_receipt;


                        for (i = 0; i < OrderNo.length(); i++) {
                            cls_receipt = new cls_Receipt();
                            cls_receipt.setOrderNo(OrderNo.get(i).toString());
                            cls_receipt.setDate(Date.get(i).toString());
                            cls_receipt.setAmt(Amt.get(i).toString());
                            cls_receipt.setCash(Cash.get(i).toString());
                            cls_receipt.setCheckTotal(CheckTotal.get(i).toString());
                            cls_receipt.setNotes(notes.get(i).toString());
                            cls_receipt.setCustnm(CustName.get(i).toString());
                            cls_receipt.setManName(manName.get(i).toString());


                            Rlist.add(cls_receipt);
                        }
                        _handler.post(new Runnable() {
                            public void run() {

                                receipt_adapter = new Receipt_Adapter(Report_Home.this, Rlist);
                                listView1.setAdapter(receipt_adapter);
                            }
                        });
                    } else
                    {
                        Toast.makeText(Report_Home.this,"لا يوجد بيانات",Toast.LENGTH_LONG).show();
                    }
                } catch (final Exception e) {

                }

            }
        };
        thread.start();
    }

    private void getdata() {
        listDataHeader = new ArrayList<cls_sales>();
        listDataChild = new HashMap<List<cls_sales>, List<cls_salesC>>();
        Thread thread = new Thread() {
            @Override
            public void run() {



                CallWebServices ws = new CallWebServices(Report_Home.this);
                   if (x==2) {
                    ws.GET_Report_Home_TransHeader("-1", "-1", "11", "-1", "01-01-2020", "01-01-2021", "-1", "-1", "-1");
                }else if(x==3){
                    ws.GET_Report_Home_TransHeader("-1","-1","10","-1","01-01-2020","01-01-2021","-1","-1","-1");

                }else if(x==4){
                     ws.GET_Report_Home_TransHeader("-1","-1","12","-1","01-01-2020","01-01-2021","-1","-1","-1");
                }

                try {
                    if (We_Result.ID>0) {


                        Integer i;
                        Integer j;
                        String sn = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Custname = js.getJSONArray("Custname");
                        JSONArray ManName = js.getJSONArray("ManName");
                        JSONArray TransDate = js.getJSONArray("TransDate");
                        JSONArray NetTotal = js.getJSONArray("NetTotal");
                        JSONArray Total = js.getJSONArray("Total");
                        JSONArray TaxTotal = js.getJSONArray("TaxTotal");
                        JSONArray OrderNo = js.getJSONArray("OrderNo");
                        JSONArray disc_Total = js.getJSONArray("disc_Total");
                        JSONArray OrderType = js.getJSONArray("OrderType");


                        listDataHeader.add(new cls_sales("أسم العميل", "أسم المندوب", "التاريخ", "المجموع النهائي", "المجموع ", "الضريبة", "رقم الحركة", "الخصم", "تالف"));

                        for (i = 0; i < js_Custname.length(); i++) {
                          listDataHeader.add(new cls_sales(js_Custname.get(i).toString(), ManName.get(i).toString(), TransDate.get(i).toString(), NetTotal.get(i).toString(), Total.get(i).toString(), TaxTotal.get(i).toString(), OrderNo.get(i).toString(), disc_Total.get(i).toString(), OrderType.get(i).toString()   ));
                        }
                        _handler.post(new Runnable() {
                            public void run() {

                                SalesAdapter adapter = new SalesAdapter(Report_Home.this, listDataHeader);
                                listView1.setAdapter(adapter);
                            }
                        });


                    } else
                    {
                        Toast.makeText(Report_Home.this,"لا يوجد بيانات",Toast.LENGTH_LONG).show();
                    }
                } catch (final Exception e) {

                }

            }
        };
        thread.start();
    }

    private  void FillList(){
             obj=new  Cls_Listtitle ();
            obj.setTitle("الجولات تفصيلي");
            obj.setNo("1");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("طلبات البيع");
            obj.setNo("2");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("فواتير المبيعات");
            obj.setNo("3");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("المرتجعات");
            obj.setNo("4");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("سندات القبض");
            obj.setNo("5");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("نسبة الجولات");
            obj.setNo("6");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("ملخص المندوب");
            obj.setNo("7");
            cls_listtitles.add( obj);




        }

    private void getVisitingInformation() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
               // ws.GET_Report_Home(ReportId.CustNo,ReportId.ManNo,ReportId.Id,ReportId.flag,ReportId.FromDate,ReportId.ToDate,"","",ReportId.Countryno);
                ws.GET_Report_Home("-1","-1","1","-1","01-01-2020","01-01-2021","-1","-1","-1");
                try {
                    if(We_Result.ID>0) {
                        Integer i;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Custname = js.getJSONArray("Custname");
                        JSONArray js_ManName = js.getJSONArray("ManName");
                        JSONArray js_Tr_Data = js.getJSONArray("Tr_Data");
                        JSONArray js_DayNm = js.getJSONArray("DayNm");
                        JSONArray Start_Time = js.getJSONArray("Start_Time");
                        JSONArray js_End_Time = js.getJSONArray("End_Time");
                        JSONArray js_Duration = js.getJSONArray("Duration");
                        JSONArray js_Visit_Note = js.getJSONArray("Visit_Note");
                        JSONArray js_StreatNm = js.getJSONArray("StreatNm");

                        cls_VisitingInformation = new cls_VisitingInformation();
                        cls_VisitingInformation.setCustname("أسم العميل");
                        cls_VisitingInformation.setManName("اسم المندوب");
                        cls_VisitingInformation.setTr_Data("تاريخ الزيارة");
                        cls_VisitingInformation.setDayNm("اليوم");
                        cls_VisitingInformation.setStart_Time("وقت البداية");
                        cls_VisitingInformation.setEnd_Time("وقت النهاية");
                        cls_VisitingInformation.setDuration("المدة");
                        cls_VisitingInformation.setVisit_Note("ملاحظات");
                        cls_VisitingInformation.setStreatNm("اسم المنطقة");
                        vlist.add(cls_VisitingInformation);
                        for (i = 0; i < js_Custname.length(); i++) {
                            cls_VisitingInformation=  new cls_VisitingInformation();
                            cls_VisitingInformation.setCustname(js_Custname.get(i).toString());
                            cls_VisitingInformation.setManName(js_ManName.get(i).toString());
                            cls_VisitingInformation.setTr_Data(js_Tr_Data.get(i).toString());
                            cls_VisitingInformation.setDayNm(js_DayNm.get(i).toString());
                            cls_VisitingInformation.setStart_Time(Start_Time.get(i).toString());
                            cls_VisitingInformation.setEnd_Time(js_End_Time.get(i).toString());

                            cls_VisitingInformation.setDuration(js_Duration.get(i).toString());
                            cls_VisitingInformation.setVisit_Note(js_Visit_Note.get(i).toString());
                            cls_VisitingInformation.setStreatNm(js_StreatNm.get(i).toString());

                            vlist.add(cls_VisitingInformation);
                        }
                        _handler.post(new Runnable() {
                            public void run() {

                                visitingInformationAdapter = new VisitingInformationAdapter(Report_Home.this, vlist);
                                listView1.setAdapter(visitingInformationAdapter);
                            }
                        });
                    } else
                    {
                        Toast.makeText(Report_Home.this,"لا يوجد بيانات",Toast.LENGTH_LONG).show();
                    }
                } catch (final Exception e) {

                }

            }
        };
        thread.start();
    }
}

package com.cds_jo.GalaxySalesApp.Reports;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.Select_Customer;
import com.cds_jo.GalaxySalesApp.SmanChart.BarChartItem;
import com.cds_jo.GalaxySalesApp.SmanChart.ChartItem;
import com.cds_jo.GalaxySalesApp.SmanChart.Cls_Monthly_Items_Amount;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.QuantitiesFragment;
import com.cds_jo.GalaxySalesApp.assist.getDataSer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import Methdes.MyTextView;


public class Report_Home extends FragmentActivity {
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
    ArrayList<Cls_SalesValues>  SalesValuesList;
    ArrayList<cls_VisitingInformation>  vlist;
    ArrayList<cls_DelegateInformation>  Dlist;
    ArrayList<cls_Receipt>  Rlist;
    ArrayList<cls_achievement_rate>  ARlist;
    ArrayList<cls_net_profit>  NPlist;
    ArrayList<cls_sales> listDataHeader;
     MyTextView ReportTitle;
    HashMap<List<cls_sales>, List<cls_salesC>> listDataChild;
    final Handler _handler = new Handler();
    ArrayList<ChartItem_Report> ChartList;

    int  x , Flg;

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

        et_fromDate=(EditText) findViewById(R.id.et_fromDate);
        editText5=(EditText) findViewById(R.id.editText5);
        et_Todate=(EditText)findViewById(R.id.et_Todate);
        imgFrom = (ImageView) findViewById(R.id.imgFrom);
        imgTo = (ImageView) findViewById(R.id.imgTo);
        et_Man=(EditText)findViewById(R.id.et_Man);
        ezditText5=(EditText)findViewById(R.id.ezditText5);
        ReportTitle=(MyTextView) findViewById(R.id.ReportTitle);

        ChartList = new ArrayList<>();

        FillList();
        listtitleadapter = new listtitleadapter(Report_Home.this, cls_listtitles);
        listView.setAdapter(listtitleadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SalesValuesList = new ArrayList<Cls_SalesValues>();
                vlist = new ArrayList<cls_VisitingInformation>();
                ARlist = new ArrayList<cls_achievement_rate>();
                Dlist = new ArrayList<cls_DelegateInformation>();
                Rlist = new ArrayList<cls_Receipt>();



                obj =cls_listtitles.get(position);

                 x=Integer.parseInt(obj.getNo());
                 Flg=Integer.parseInt(obj.getFlg());
                 ReportId.Id=x;
                 ReportTitle.setText(  obj.getTitle().toString());


                listView1.setAdapter(null);
                if(x==1)
                {
                   listView1.setVisibility(View.VISIBLE);
                   getVisitingInformation();
                }
                else if(x==2||x==3||x==4)
                {
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
                 SalesValues();
                }
                else if(x==9)
                {
                    ShowChart();
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

                if ((x == 2 || x == 3 || x == 4) && position>0 ){

                    cls_sales o = (cls_sales) listView1.getItemAtPosition(position);
                    showDtl(o.getOrderNo(), o.getOrderType(), o.getNetTotal() + "");
                }
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

                        cls_achievement_rate.setManNo("رقم المندوب");
                        cls_achievement_rate.setManName("اسم المندوب");
                        cls_achievement_rate.setTr_Date("التاريخ");
                        cls_achievement_rate.setTotalCust("عدد عملاء الجولة");
                        cls_achievement_rate.setVisited("عدد الجولات ");
                        cls_achievement_rate.setVisitPrecent("النسبة");
                        cls_achievement_rate.setSuccVisit("الجولات الناجحة");
                        cls_achievement_rate.setSuccPercent("نسبة الجولات الناجحة");
                        cls_achievement_rate.setScore("النسبة النهائية");
                        ARlist.add(cls_achievement_rate);

                        for (i = 0; i < js_ManNo.length(); i++) {
                            cls_achievement_rate = new cls_achievement_rate();
                            cls_achievement_rate.setManNo(js_ManNo.get(i).toString());
                            cls_achievement_rate.setManName(js_ManName.get(i).toString());
                            cls_achievement_rate.setTr_Date(js_Tr_Date.get(i).toString());
                            cls_achievement_rate.setTotalCust(js_TotalCust.get(i).toString());
                            cls_achievement_rate.setVisited(js_Visited.get(i).toString());
                            cls_achievement_rate.setVisitPrecent(js_VisitPrecent.get(i).toString());
                            cls_achievement_rate.setSuccVisit(js_SuccVisit.get(i).toString());
                            cls_achievement_rate.setSuccPercent(js_SuccPercent.get(i).toString());
                            cls_achievement_rate.setScore(js_Score.get(i).toString());
                            ARlist.add(cls_achievement_rate);
                        }
                        _handler.post(new Runnable() {
                            public void run() {
                                achievement_rate_adapter = new achievement_rate_Adapter(Report_Home.this, ARlist);
                                listView1.setAdapter(achievement_rate_adapter);
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
                        JSONArray ManNm = js.getJSONArray("ManName");
                        JSONArray CheckIn = js.getJSONArray("CheckIn");
                        JSONArray checkOut = js.getJSONArray("checkOut");
                        JSONArray Payemnt = js.getJSONArray("Payments");
                        JSONArray Sales = js.getJSONArray("SalesAmt");
                        JSONArray returnsSales = js.getJSONArray("SalesReturn");
                        JSONArray SalesOrders = js.getJSONArray("OrdersAmt");
                        JSONArray NewCustomers = js.getJSONArray("NewCustomers");

                        cls_delegateInformation = new cls_DelegateInformation();
                        cls_delegateInformation.setManNo1("رقم المندوب");
                        cls_delegateInformation.setManNm("اسم المندوب");
                        cls_delegateInformation.setCheckIn("بداية الدوام");
                        cls_delegateInformation.setCheckOut("نهاية الدوام");
                        cls_delegateInformation.setPayemnt("القبوضات");
                        cls_delegateInformation.setSales("المبيعات");
                        cls_delegateInformation.setReturnsSales("المرتجعات");
                        cls_delegateInformation.setSalesOrders("طلبات البيع");
                        cls_delegateInformation.setPrecent("العملاء الجدد");

                        Dlist.add(cls_delegateInformation);

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
                            cls_delegateInformation.setPrecent(NewCustomers.get(i).toString());

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
                        _handler.post(new Runnable() {
                            public void run() {

                                Toast.makeText(Report_Home.this,"لا يوجد بيانات",Toast.LENGTH_LONG).show();
                            }
                        });

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
            obj.setFlg("-1");
            obj.setNo("1");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("طلبات البيع");
            obj.setFlg("-1");
            obj.setNo("2");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("فواتير المبيعات");
            obj.setFlg("-1");
            obj.setNo("3");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("المرتجعات");
            obj.setFlg("-1");
            obj.setNo("4");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("سندات القبض");
            obj.setFlg("-1");
            obj.setNo("5");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("نسبة الجولات");
            obj.setFlg("-1");
            obj.setNo("6");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("ملخص المندوب");
            obj.setFlg("-1");
            obj.setNo("7");
            cls_listtitles.add( obj);



            obj=new  Cls_Listtitle ();
            obj.setTitle("أعلى الأصناف مبيعا- كمية");
            obj.setFlg("1");
            obj.setNo("8");
            cls_listtitles.add( obj);


            obj=new  Cls_Listtitle ();
            obj.setTitle("أقل الأصناف مبيعا - كمية");
             obj.setFlg("2");
            obj.setNo("8");
            cls_listtitles.add( obj);


            obj=new  Cls_Listtitle ();
            obj.setTitle("أعلى الأصناف مبيعا- مبلغ");
            obj.setFlg("3");
            obj.setNo("8");
            cls_listtitles.add( obj);


            obj=new  Cls_Listtitle ();
            obj.setTitle("أقل الأصناف مبيعا- مبلغ");
            obj.setFlg("4");
            obj.setNo("8");
            cls_listtitles.add( obj);




        obj=new  Cls_Listtitle ();
        obj.setTitle("أعلى الأصناف مبيعا- كمية");
        obj.setFlg("1");
        obj.setNo("9");
        cls_listtitles.add( obj);


        obj=new  Cls_Listtitle ();
        obj.setTitle("أقل الأصناف مبيعا - كمية");
        obj.setFlg("2");
        obj.setNo("9");
        cls_listtitles.add( obj);


        obj=new  Cls_Listtitle ();
        obj.setTitle("أعلى الأصناف مبيعا- مبلغ");
        obj.setFlg("3");
        obj.setNo("9");
        cls_listtitles.add( obj);


        obj=new  Cls_Listtitle ();
        obj.setTitle("أقل الأصناف مبيعا- مبلغ");
        obj.setFlg("4");
        obj.setNo("9");
        cls_listtitles.add( obj);


        obj=new  Cls_Listtitle ();
        obj.setTitle("الهدف الشهري مبالغ");
        obj.setFlg("5");
        obj.setNo("9");
        cls_listtitles.add( obj);


        obj=new  Cls_Listtitle ();
        obj.setTitle("الهدف الشهري-كميات");
        obj.setFlg("6");
        obj.setNo("9");
        cls_listtitles.add( obj);



        obj=new  Cls_Listtitle ();
        obj.setTitle("line_chart");
        obj.setFlg("7");
        obj.setNo("9");
        cls_listtitles.add( obj);

    }
    private void ShowChart() {
        ChartList.clear();
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_Report_Home_SalesValues("-1","-1","14",Flg+"","01-01-2020","01-01-2021","-1","-1","-1");
                try {
                    if(We_Result.ID>0) {
                        Integer i;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Item_No = js.getJSONArray("Item_No");
                        JSONArray js_Item_Name= js.getJSONArray("Item_Name");
                        JSONArray js_Tr_Value = js.getJSONArray("Tr_Value");


                        Cls_SalesValues obj  = new Cls_SalesValues();



                       // for (i = 0; i < js_Item_No.length(); i++) {
                        for (i = 0; i < 5; i++) {

                            obj=  new Cls_SalesValues();
                            obj.setItem_No(js_Item_No.get(i).toString());
                            obj.setItem_Name(js_Item_Name.get(i).toString());
                            obj.setTr_Value(js_Tr_Value.get(i).toString());
                            SalesValuesList.add(obj);
                        }




                        _handler.post(new Runnable() {
                            public void run() {
                                ChartList.add(new BarChartItem_Report(generateDataBar(SalesValuesList),Report_Home.this,SalesValuesList));
                                ChartList.add(new LineChartItem(generateDataLine(SalesValuesList), Report_Home.this));
                                ChartDataAdapter cda = new ChartDataAdapter(Report_Home.this, ChartList);
                                listView1.setAdapter(cda);

/* SalesValues_Adapter adapter = new SalesValues_Adapter(Report_Home.this, SalesValuesList);
                                listView1.setAdapter(adapter);*/


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
    private LineData generateDataLine(ArrayList<Cls_SalesValues> cnt) {

        ArrayList<Entry> values1 ;
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        for (int i = 0; i < cnt.size(); i++) {
            Cls_SalesValues c=cnt.get(i);
            values1 = new ArrayList<>();
            values1.add(new Entry(i,Float.parseFloat(c.getTr_Value())));
            LineDataSet d1 = new LineDataSet(values1, c.getItem_Name());
            d1.setLineWidth(2.5f);
            d1.setCircleRadius(4.5f);
            d1.setHighLightColor(Color.rgb(244+i, 117+i, 117+i));
            d1.setDrawValues(false);
            sets.add(d1);
        }





        return new LineData(sets);
    }
    private BarData generateDataBar (List<Cls_SalesValues> objects){

        ArrayList<BarEntry> entries = new ArrayList<>();

       for (int i = 0; i < objects.size(); i++) {
            Cls_SalesValues obj = objects.get(i);
            float r= Float.parseFloat(obj.getTr_Value());
            entries.add(new BarEntry(i, r));
        }

        BarDataSet d = new BarDataSet(entries,"Amount");

        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        // d.setHighLightAlpha(255);
        d.setValueTextSize(25f);

        BarData cd = new BarData(d);

        cd.setBarWidth(0.9f);
        return cd;
    }

    public void btn_Back(View view) {
        Intent i  = new Intent(this, JalMasterActivity.class);
        startActivity(i);


    }

    @Override
    public void onBackPressed() {
        Intent i  = new Intent(this, JalMasterActivity.class);
        startActivity(i);


    }
    private class ChartDataAdapter extends ArrayAdapter<ChartItem_Report> {

        ChartDataAdapter(Context context, List<ChartItem_Report> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //noinspection ConstantConditions
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            ChartItem_Report ci = getItem(position);
            return 1;//ci != null ? ci.getItemType() :1;
        }

        @Override
        public int getViewTypeCount() {
            return 2; // we have 3 different item-types
        }
    }
    private void SalesValues() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_Report_Home_SalesValues("-1","-1","14",Flg+"","01-01-2020","01-01-2021","-1","-1","-1");
                try {
                    if(We_Result.ID>0) {
                        Integer i;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Item_No = js.getJSONArray("Item_No");
                        JSONArray js_Item_Name= js.getJSONArray("Item_Name");
                        JSONArray js_Tr_Value = js.getJSONArray("Tr_Value");


                        Cls_SalesValues obj  = new Cls_SalesValues();
                        obj.setItem_No("رقم المادة");
                        obj.setItem_Name("اسم المادة");
                        obj.setTr_Value("القيمة");

                        SalesValuesList.add(obj);
                        for (i = 0; i < js_Item_No.length(); i++) {
                            obj=  new Cls_SalesValues();
                            obj.setItem_No(js_Item_No.get(i).toString());
                            obj.setItem_Name(js_Item_Name.get(i).toString());
                            obj.setTr_Value(js_Tr_Value.get(i).toString());
                            SalesValuesList.add(obj);
                        }
                        _handler.post(new Runnable() {
                            public void run() {

                                SalesValues_Adapter adapter = new SalesValues_Adapter(Report_Home.this, SalesValuesList);
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

    public void Do_Whatsapp(View view) {

        StoreImage();
         openWhatsApp();
    }
    private void openWhatsApp() {
        File imageFileToShare = new File("//sdcard/z1.jpg");
        Uri uri2 = Uri.fromFile(imageFileToShare);
        String toNumber = "+962785381939";
        toNumber="+962" +"785381939";
        toNumber = toNumber.replace("+", "").replace(" ", "");
        Intent shareIntent = new Intent("android.intent.action.MAIN");
        shareIntent.setAction(Intent.ACTION_SEND);
        String ExtraText;
        ExtraText = "Galaxy Sales App";
        shareIntent.putExtra(Intent.EXTRA_TEXT, ExtraText);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri2);
        shareIntent.setType("image/jpg");
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {

            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getBaseContext(), "Sharing tools have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private  void StoreImage(){


      try {
          LinearLayout lay = (LinearLayout) findViewById(R.id.Data);

          Bitmap b = loadBitmapFromView(lay);
          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
          String filename = "z1.jpg";
          File sd = Environment.getExternalStorageDirectory();
          File dest = new File(sd, filename);

          try {
              FileOutputStream out = new FileOutputStream(dest);
              b.compress(Bitmap.CompressFormat.JPEG, 70, out);
              out.flush();
              out.close();
              //  bitmap.recycle();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }catch (Exception e ){}


    }
    public static Bitmap loadBitmapFromView(View v) {

        v.measure(View.MeasureSpec.makeMeasureSpec(v.getLayoutParams().width,
                View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                v.getLayoutParams().height, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
}

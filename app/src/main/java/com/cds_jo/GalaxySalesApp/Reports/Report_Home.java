package com.cds_jo.GalaxySalesApp.Reports;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.CustomerSummary.CustomerManVisitAdabter;
import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_Bill;
import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_BillC;
import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_ManVisit;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Report_Home extends AppCompatActivity {
    Cls_Listtitle obj;
    ImageView imgFrom,imgTo;
    ListView listView,listView1;
    EditText et_Man;
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
    List<cls_sales> listDataHeader;
    ExpandableListView lst_acc;
    HashMap<List<cls_sales>, List<cls_salesC>> listDataChild;
    final Handler _handler = new Handler();


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
        et_Todate=(EditText)findViewById(R.id.et_Todate);
        imgFrom = (ImageView) findViewById(R.id.imgFrom);
        imgTo = (ImageView) findViewById(R.id.imgTo);
        et_Man=(EditText)findViewById(R.id.et_Man);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Report_Home.this);
        UserID = sharedPreferences.getString("UserID", "");
        et_Man.setText(UserID);

        FillList();
        listtitleadapter = new listtitleadapter(Report_Home.this, cls_listtitles);
        listView.setAdapter(listtitleadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vlist = new ArrayList<cls_VisitingInformation>();
                Rlist = new ArrayList<cls_Receipt>();
                obj =cls_listtitles.get(position);
                int x=Integer.parseInt(obj.getNo());
                if(x==1)
                {
                    listView1.setVisibility(View.VISIBLE);
                    lst_acc.setVisibility(View.GONE);
                    getVisitingInformation();
                }
                else if(x==2||x==3||x==4)
                {
                    listView1.setVisibility(View.GONE);
                    lst_acc.setVisibility(View.VISIBLE);
                    getdata();
                }
                else if(x==5)
                {
                    listView1.setVisibility(View.VISIBLE);
                    lst_acc.setVisibility(View.GONE);
                    getreceipt();
                }
                else if(x==6)
                {
                    listView1.setVisibility(View.VISIBLE);
                    lst_acc.setVisibility(View.GONE);
                    getDelegateInformation();
                }
                else if(x==7)
                {
                    listView1.setVisibility(View.VISIBLE);
                    lst_acc.setVisibility(View.GONE);
                    getachievement_rate();
                }
                else if(x==8)
                {
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

    }

    private void getnet_profit() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_CustReportManVisit("11041000011");
                try {
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
                    });

                } catch (final Exception e) {

                }

            }
        };
        thread.start();

    }

    private void getachievement_rate() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_CustReportManVisit("11041000011");
                try {
                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray TotQtyTarqet = js.getJSONArray("TotQtyTarqet");
                    JSONArray TotAmtTarqet = js.getJSONArray("TotAmtTarqet");



                    cls_achievement_rate cls_achievement_rate= new cls_achievement_rate();

                    for (i = 0; i < TotAmtTarqet.length(); i++) {
                        cls_achievement_rate.setTotQtyTarqet(TotQtyTarqet.get(i).toString());
                        cls_achievement_rate.setTotAmtTarqet(TotAmtTarqet.get(i).toString());




                        ARlist.add(cls_achievement_rate);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            achievement_rate_adapter = new achievement_rate_Adapter(Report_Home.this, ARlist);
                            listView1.setAdapter(receipt_adapter);
                        }
                    });

                } catch (final Exception e) {

                }

            }
        };
        thread.start();
    }

    private void getDelegateInformation() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_CustReportManVisit("11041000011");
                try {
                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray ManNo1 = js.getJSONArray("ManNo1");
                    JSONArray ManNm = js.getJSONArray("ManNm");
                    JSONArray CheckIn = js.getJSONArray("CheckIn");
                    JSONArray checkOut = js.getJSONArray("checkOut");
                    JSONArray Payemnt = js.getJSONArray("Payemnt");
                    JSONArray Sales = js.getJSONArray("Sales");
                    JSONArray returnsSales= js.getJSONArray("returnsSales");
                    JSONArray SalesOrders = js.getJSONArray("SalesOrders");
                    JSONArray Precent= js.getJSONArray("Precent");

                    cls_delegateInformation = new cls_DelegateInformation();
                    cls_delegateInformation.setManNo1("رقم المندوب");
                    cls_delegateInformation.setManNm("اسم المندوب");
                    cls_delegateInformation.setCheckIn("بداية دوام");
                    cls_delegateInformation.setCheckOut("نهاية دوام");
                    cls_delegateInformation.setPayemnt("القبوضات");
                    cls_delegateInformation.setSales("المبيعات");

                    cls_delegateInformation.setReturnsSales("المرتجعات");
                    cls_delegateInformation.setSalesOrders("طلبات البيع");
                    cls_delegateInformation.setPrecent("النسبة");
                    for (i = 0; i < ManNo1.length(); i++) {
                        cls_delegateInformation.setManNo1(ManNo1.get(i).toString());
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

                } catch (final Exception e) {

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
                ws.GET_CustReportManVisit("11041000011");
                try {
                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray OrderNo = js.getJSONArray("OrderNo");
                    JSONArray Date = js.getJSONArray("Date");
                    JSONArray Amt = js.getJSONArray("Amt");
                    JSONArray Cash = js.getJSONArray("Cash");
                    JSONArray CheckTotal = js.getJSONArray("CheckTotal");
                    JSONArray notes = js.getJSONArray("notes");


                    cls_Receipt cls_receipt= new cls_Receipt();
                    cls_receipt.setOrderNo("رقم القبض");
                    cls_receipt.setDate("تاريخ القبض");
                    cls_receipt.setAmt("مبلغ القبض");
                    cls_receipt.setCash("المبلغ النقدي");
                    cls_receipt.setCheckTotal("مبلغ الشيكات");
                    cls_receipt.setNotes("ملاحظات");

                    for (i = 0; i < OrderNo.length(); i++) {
                        cls_receipt.setOrderNo(OrderNo.get(i).toString());
                        cls_receipt.setDate(Date.get(i).toString());
                        cls_receipt.setAmt(Amt.get(i).toString());
                        cls_receipt.setCash(Cash.get(i).toString());
                        cls_receipt.setCheckTotal(CheckTotal.get(i).toString());
                        cls_receipt.setNotes(notes.get(i).toString());



                        Rlist.add(cls_receipt);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            receipt_adapter = new Receipt_Adapter(Report_Home.this, Rlist);
                            listView1.setAdapter(receipt_adapter);
                        }
                    });

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
                ws.GET_CustReportBill("");
                try {
                    Integer i;
                    Integer j;
                    String sn="";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Custname = js.getJSONArray("Custname");
                    JSONArray ManName = js.getJSONArray("ManName");
                    JSONArray TransDate = js.getJSONArray("TransDate");
                    JSONArray NetTotal = js.getJSONArray("NetTotal");
                    JSONArray Total = js.getJSONArray("Total");
                    JSONArray TaxTotal = js.getJSONArray("TaxTotal");
                    JSONArray OrderNo = js.getJSONArray("OrderNo");
                    JSONArray Item_Name = js.getJSONArray("Item_Name");
                    JSONArray OrgPrice = js.getJSONArray("OrgPrice");
                    JSONArray price = js.getJSONArray("price");
                    JSONArray Qty = js.getJSONArray("Qty");
                    JSONArray Bounce = js.getJSONArray("Bounce");
                    JSONArray Dis_Amt = js.getJSONArray("Dis_Amt");
                    JSONArray UnitName = js.getJSONArray("UnitName");
                    JSONArray is_Damage = js.getJSONArray("is_Damage");


                    //  cls_selingRequest = new cls_SelingRequest();
                    //  cls_selingRequestC = new cls_SelingRequestC();
                    for (i = 0; i < js_Custname.length(); i++)
                    {
                        sn=OrderNo.get(i).toString();
                        List<cls_salesC> q = new ArrayList<cls_salesC>();
                        for (j = 0; j < js_Custname.length(); j++)
                        {

                            if(OrderNo.get(j).toString()==sn)
                            {
                                q.add(new cls_salesC(Item_Name.get(j).toString(),OrgPrice.get(j).toString(),price.get(j).toString(),Qty.get(j).toString(),Bounce.get(j).toString(),UnitName.get(j).toString()));
                                if(j==0)
                                {
                                    listDataHeader.add(new cls_sales(js_Custname.get(j).toString(),ManName.get(j).toString(),TransDate.get(j).toString(),NetTotal.get(j).toString(),Total.get(j).toString(),TaxTotal.get(j).toString(),OrderNo.get(j).toString(),Dis_Amt.get(j).toString(),is_Damage.get(j).toString()));

                                }

                            }
                        }
                        listDataChild.put(listDataHeader, q);

                    }

                } catch (final Exception e) {

                }

            }
        };
        thread.start();
    }

    private  void FillList(){
             obj=new  Cls_Listtitle ();
            obj.setTitle("معلومات الزيارة");
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
            obj.setTitle("تيست");
            obj.setNo("6");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("تيست");
            obj.setNo("7");
            cls_listtitles.add( obj);

            obj=new  Cls_Listtitle ();
            obj.setTitle("تيست");
            obj.setNo("8");
            cls_listtitles.add( obj);


        }

    private void getVisitingInformation() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(Report_Home.this);
                ws.GET_CustReportManVisit("11041000011");
                try {
                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Custname = js.getJSONArray("Custname");
                    JSONArray js_ManName = js.getJSONArray("ManName");
                    JSONArray js_Tr_Data = js.getJSONArray("Tr_Data");
                    JSONArray js_DayNm = js.getJSONArray("DayNm");
                    JSONArray Start_Time = js.getJSONArray("Start_Time");
                    JSONArray js_End_Time = js.getJSONArray("End_Time");
                    JSONArray js_Duration= js.getJSONArray("Duration");
                    JSONArray js_Visit_Note = js.getJSONArray("Visit_Note");
                    JSONArray js_StreatNm= js.getJSONArray("StreatNm");

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
                    for (i = 0; i < js_Custname.length(); i++) {
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

                } catch (final Exception e) {

                }

            }
        };
        thread.start();
    }
}

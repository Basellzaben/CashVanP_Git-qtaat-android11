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
import android.widget.ImageView;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.CustomerSummary.CustomerManVisitAdabter;
import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_ManVisit;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Report_Home extends AppCompatActivity {
    Cls_Listtitle obj;
    ImageView imgFrom,imgTo;
    ListView listView,listView1;
    EditText et_Man;
    public int FlgDate = 0;
    EditText et_fromDate;
    cls_VisitingInformation  cls_VisitingInformation;
    EditText et_Todate;
    String UserID;
    VisitingInformationAdapter visitingInformationAdapter;
    listtitleadapter listtitleadapter;
    ArrayList<Cls_Listtitle>  cls_listtitles = new ArrayList<Cls_Listtitle>();
    ArrayList<cls_VisitingInformation>  vlist;
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
                obj =cls_listtitles.get(position);
                int x=Integer.parseInt(obj.getNo());
                if(x==1)
                {
                    getVisitingInformation();
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

package com.cds_jo.GalaxySalesApp.Reports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.CustomerSummary.CustomerManVisitAdabter;
import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

public class Report_Home extends AppCompatActivity {
    Cls_Listtitle obj;
    ListView listView;
    listtitleadapter listtitleadapter;
    ArrayList<Cls_Listtitle>  cls_listtitles = new ArrayList<Cls_Listtitle>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__home);
        cls_listtitles.clear();
        listView=(ListView)findViewById(R.id.LV);
        FillList();
        listtitleadapter = new listtitleadapter(Report_Home.this, cls_listtitles);
        listView.setAdapter(listtitleadapter);
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
}

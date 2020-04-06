package com.cds_jo.GalaxySalesApp.Reports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

public class Report_Home extends AppCompatActivity {

    ArrayList<Cls_Listtitle>  cls_listtitles = new ArrayList<Cls_Listtitle>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__home);
        cls_listtitles.clear();
        FillList();
        }

        private  void FillList(){
            Cls_Listtitle obj=new  Cls_Listtitle ();
            obj.setTitle("الدوام");
            cls_listtitles.add( obj);

            new  Cls_Listtitle ();
            obj.setTitle("التحصيلات");
            cls_listtitles.add( obj);
        }
}

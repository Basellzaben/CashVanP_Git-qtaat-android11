package com.cds_jo.GalaxySalesApp.CustomerSummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

public class CustomerManVisitAdabter  extends BaseAdapter {
    Context context;
    ArrayList<cls_ManVisit> cls_checks;

    public CustomerManVisitAdabter(Context context, ArrayList<cls_ManVisit> cls_checks) {
        this.context = context;
        this.cls_checks = cls_checks;
    }

    @Override
    public int getCount() {
        return cls_checks.size();    }

    @Override
    public Object getItem(int position) {
        return cls_checks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final cls_ManVisit  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.desginmanvisit, null);

        }


        MyTextView name = (MyTextView) convertView.findViewById(R.id.B_CustName);
        name.setText(cls_customerCatch.getName());


        MyTextView Date = (MyTextView) convertView.findViewById(R.id.date);
        Date.setText(cls_customerCatch.getDate());

        MyTextView Name = (MyTextView) convertView.findViewById(R.id.start);
        Name.setText(cls_customerCatch.getStart());
        MyTextView end = (MyTextView) convertView.findViewById(R.id.end);
        end.setText(cls_customerCatch.getEnd());

        MyTextView day = (MyTextView) convertView.findViewById(R.id.day);
        if(Integer.parseInt(cls_customerCatch.getDay())==1)
        {
            day.setText("السبت");
        }
        else if(Integer.parseInt(cls_customerCatch.getDay())==2)
        {
            day.setText("الاحد");
        }
        else if(Integer.parseInt(cls_customerCatch.getDay())==3)
        {
            day.setText("الاثنين");
        }else if(Integer.parseInt(cls_customerCatch.getDay())==4)
        {
            day.setText("الثلاثاء");
        }else if(Integer.parseInt(cls_customerCatch.getDay())==5)
        {
            day.setText("الاربعاء");
        }else if(Integer.parseInt(cls_customerCatch.getDay())==6)
        {
            day.setText("الخميس");
        }else if(Integer.parseInt(cls_customerCatch.getDay())==7)
        {
            day.setText("الجمعة");
        }
        MyTextView note = (MyTextView) convertView.findViewById(R.id.Note);
        note.setText(cls_customerCatch.getNote());



        return convertView;
    }
}

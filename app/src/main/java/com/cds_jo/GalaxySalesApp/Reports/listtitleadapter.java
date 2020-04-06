package com.cds_jo.GalaxySalesApp.Reports;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.Cls_Acc_Report;

import java.util.ArrayList;

import Methdes.MyTextView;

public class listtitleadapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Listtitle> cls_listtitles;


    public listtitleadapter(Context context, ArrayList<Cls_Listtitle> list) {

        this.context = context;
        cls_listtitles = list;
    }

    @Override
    public int getCount() {

        return cls_listtitles.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_listtitles.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Listtitle  cls_acc_report = cls_listtitles.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listtitle_desgin, null);
        }
     /*   MyTextView tv_doc_num = (MyTextView) convertView.findViewById(R.id.tv_doc_num);
        MyTextView Doctype = (MyTextView) convertView.findViewById(R.id.tv_doctype);*/
        MyTextView contact_name = (MyTextView) convertView.findViewById(R.id.contact_name);

        contact_name.setText(cls_acc_report.getTitle());


        return convertView;
    }

}
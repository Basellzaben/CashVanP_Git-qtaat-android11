package com.cds_jo.GalaxySalesApp.CustomerSummary;

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

/**
 * Created by Hp on 06/03/2016.
 */
public class Cls_Acc_Repor_custSummery_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Acc_Report> cls_acc_reports;


    public Cls_Acc_Repor_custSummery_Adapter(Context context, ArrayList<Cls_Acc_Report> list) {

        this.context = context;
        cls_acc_reports = list;
    }

    @Override
    public int getCount() {

        return cls_acc_reports.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_acc_reports.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Acc_Report  cls_acc_report = cls_acc_reports.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.acc_report_row_cust, null);
        }
     /*   MyTextView tv_doc_num = (MyTextView) convertView.findViewById(R.id.tv_doc_num);
        MyTextView Doctype = (MyTextView) convertView.findViewById(R.id.tv_doctype);*/
        MyTextView cur_no = (MyTextView) convertView.findViewById(R.id.tv_cur_no);
        MyTextView tvdate = (MyTextView) convertView.findViewById(R.id.tv_date);
        MyTextView tvdes = (MyTextView) convertView.findViewById(R.id.tv_des);
        MyTextView tvbb = (MyTextView) convertView.findViewById(R.id.tv_bb);
        MyTextView tvdept = (MyTextView) convertView.findViewById(R.id.tv_dept);
        MyTextView tvcred = (MyTextView) convertView.findViewById(R.id.tv_cred);
        MyTextView tv_tot = (MyTextView) convertView.findViewById(R.id.tv_tot);
        MyTextView tv_rate = (MyTextView) convertView.findViewById(R.id.tv_rate);


        /*tv_doc_num.setText(cls_acc_report.getDoc_num());
        Doctype.setText(cls_acc_report.getDoctype());*/
        cur_no.setText(cls_acc_report.getCur_no());
        tvdate.setText(cls_acc_report.getDate());
        tvdes.setText(cls_acc_report.getDes());
        tvbb.setText(cls_acc_report.getBb());
        tvdept.setText(cls_acc_report.getDept());
        tvcred.setText(cls_acc_report.getCred());
        tv_rate.setText(cls_acc_report.getRate().replace(",","."));
        tv_tot.setText(cls_acc_report.getTot());

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);
        if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Black11));
         /*   tv_doc_num.setTextColor(Color.WHITE);
            Doctype.setTextColor(Color.WHITE);*/
            cur_no.setTextColor(Color.WHITE);
            tvdate.setTextColor(Color.WHITE);
            tvdes.setTextColor(Color.WHITE);
            tvbb.setTextColor(Color.WHITE);
            tvdept.setTextColor(Color.WHITE);
            tvcred.setTextColor(Color.WHITE);
            tv_rate.setTextColor(Color.WHITE);
            tv_tot.setTextColor(Color.WHITE);
        }
        else if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
        }


        return convertView;
    }

}


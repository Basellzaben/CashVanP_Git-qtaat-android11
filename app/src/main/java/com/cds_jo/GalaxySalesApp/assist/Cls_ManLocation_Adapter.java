package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.Cls_ManLocationReport;
import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 06/03/2016.
 */
public class Cls_ManLocation_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_ManLocationReport> Obj;


    public Cls_ManLocation_Adapter(Context context, ArrayList<Cls_ManLocationReport> list) {

        this.context = context;
        Obj = list;
    }

    @Override
    public int getCount() {

        return Obj.size();
    }

    @Override
    public Object getItem(int position) {

        return Obj.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_ManLocationReport  Location = Obj.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.location_report_row, null);
        }

        MyTextView Man_Name = (MyTextView) convertView.findViewById(R.id.tv_Man_Name);
        MyTextView tvDate = (MyTextView) convertView.findViewById(R.id.tv_date);
        MyTextView tvTime = (MyTextView) convertView.findViewById(R.id.tv_Time);

        MyTextView Loct = (MyTextView) convertView.findViewById(R.id.tv_Loct);





        Man_Name.setText(Location.getMan_Name());
        tvDate.setText(Location.getDate());
        tvTime.setText(Location.getTime());

        Loct.setText(Location.getLoct());






        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);
        if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Black11));
         /*   tv_doc_num.setTextColor(Color.WHITE);
            Doctype.setTextColor(Color.WHITE);*/
            Man_Name.setTextColor(Color.WHITE);
            tvDate.setTextColor(Color.WHITE);
            tvTime.setTextColor(Color.WHITE);

            Loct.setTextColor(Color.WHITE);


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


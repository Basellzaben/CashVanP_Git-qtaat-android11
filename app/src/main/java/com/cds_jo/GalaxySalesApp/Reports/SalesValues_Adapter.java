package com.cds_jo.GalaxySalesApp.Reports;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

public class SalesValues_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_SalesValues> cls_checks;

    public SalesValues_Adapter(Context context, ArrayList<Cls_SalesValues> cls_checks) {
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
        final Cls_SalesValues  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sales_values_row, null);

        }


        MyTextView manNo = (MyTextView) convertView.findViewById(R.id.Item_No);
        manNo.setText(cls_customerCatch.getItem_No());


        MyTextView Man_Name = (MyTextView) convertView.findViewById(R.id.Item_Name);
        Man_Name.setText(cls_customerCatch.getItem_Name());

        MyTextView CheckIn = (MyTextView) convertView.findViewById(R.id.Tr_Value);
        CheckIn.setText(cls_customerCatch.getTr_Value());





        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);
        if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.blue123));
            manNo.setTextColor(Color.WHITE);
            Man_Name.setTextColor(Color.WHITE);
            CheckIn.setTextColor(Color.WHITE);


        }

        else if(position%2==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            manNo.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            CheckIn.setTextColor(Color.BLACK);

        }

        else
        {
            RR.setBackgroundColor(Color.WHITE);
            manNo.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            CheckIn.setTextColor(Color.BLACK);


        }
        return convertView;
    }
}


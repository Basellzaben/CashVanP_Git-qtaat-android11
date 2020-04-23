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

public class SalesAdapter extends BaseAdapter {
    Context context;
    ArrayList<cls_sales> cls_checks;

    public SalesAdapter(Context context, ArrayList<cls_sales> cls_checks) {
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
        final cls_sales  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.desginreportbill, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView name = (MyTextView) convertView.findViewById(R.id.B_CustName);
        name.setText(cls_customerCatch.getCustname());


        MyTextView Man_Name = (MyTextView) convertView.findViewById(R.id.Man_Name);
        Man_Name.setText(cls_customerCatch.getManName());

       MyTextView orderno = (MyTextView) convertView.findViewById(R.id.orderno);
        orderno.setText(cls_customerCatch.getOrderNo());

        MyTextView TransDate = (MyTextView) convertView.findViewById(R.id.TransDate);
        TransDate.setText(cls_customerCatch.getTransDate());

        MyTextView NetTotal = (MyTextView) convertView.findViewById(R.id.NetTotal);
        NetTotal.setText(cls_customerCatch.getNetTotal());


        MyTextView TaxTotal = (MyTextView) convertView.findViewById(R.id.TaxTotal);
        TaxTotal.setText(cls_customerCatch.getTaxTotal());


        MyTextView Dis_Amt = (MyTextView) convertView.findViewById(R.id.Dis_Amt);
        Dis_Amt.setText(cls_customerCatch.getDis_Amt());

        MyTextView Total = (MyTextView) convertView.findViewById(R.id.Total);
        Total.setText(cls_customerCatch.getTotal());




        if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.blue123));

            name.setTextColor(Color.WHITE);
            Man_Name.setTextColor(Color.WHITE);
            orderno.setTextColor(Color.WHITE);
            TransDate.setTextColor(Color.WHITE);
            NetTotal.setTextColor(Color.WHITE);
            TaxTotal.setTextColor(Color.WHITE);
            Dis_Amt.setTextColor(Color.WHITE);
            Total.setTextColor(Color.WHITE);

        }

       else if(position%2==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            name.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            orderno.setTextColor(Color.BLACK);
            TransDate.setTextColor(Color.BLACK);
            NetTotal.setTextColor(Color.BLACK);
            TaxTotal.setTextColor(Color.BLACK);
            Dis_Amt.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
        }

        else
        {
            RR.setBackgroundColor(Color.WHITE);
            name.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            orderno.setTextColor(Color.BLACK);
            TransDate.setTextColor(Color.BLACK);
            NetTotal.setTextColor(Color.BLACK);
            TaxTotal.setTextColor(Color.BLACK);
            Dis_Amt.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}

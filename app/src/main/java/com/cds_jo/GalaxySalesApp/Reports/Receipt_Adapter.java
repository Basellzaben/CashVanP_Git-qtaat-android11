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

public class Receipt_Adapter extends BaseAdapter {
    Context context;
    ArrayList<cls_Receipt> cls_checks;

    public Receipt_Adapter(Context context, ArrayList<cls_Receipt> cls_checks) {
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
        final cls_Receipt  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.desgin_receipt, null);

        }


        MyTextView CustName = (MyTextView) convertView.findViewById(R.id.CustName);
        CustName.setText(cls_customerCatch.getCustnm());



        MyTextView Man_Name = (MyTextView) convertView.findViewById(R.id.Man_Name);
        Man_Name.setText(cls_customerCatch.getManName());


        MyTextView Date = (MyTextView) convertView.findViewById(R.id.Date);
        Date.setText(cls_customerCatch.getDate());

        MyTextView Amt = (MyTextView) convertView.findViewById(R.id.Amt);
        Amt.setText(cls_customerCatch.getAmt());

        MyTextView Cash = (MyTextView) convertView.findViewById(R.id.Cash);
        Cash.setText(cls_customerCatch.getCash());

        MyTextView CheckTotal = (MyTextView) convertView.findViewById(R.id.CheckTotal);
        CheckTotal.setText(cls_customerCatch.getCheckTotal());


        MyTextView notes = (MyTextView) convertView.findViewById(R.id.notes);
        notes.setText(cls_customerCatch.getNotes());
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        if(position==0) {
            RR.setBackgroundColor(context.getResources().getColor(R.color.blue123));
            CustName.setTextColor(Color.WHITE);
            Man_Name.setTextColor(Color.WHITE);
            Date.setTextColor(Color.WHITE);
            Amt.setTextColor(Color.WHITE);
            Cash.setTextColor(Color.WHITE);
            CheckTotal.setTextColor(Color.WHITE);
            notes.setTextColor(Color.WHITE);

        }
        else if(position%2==0) {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            CustName.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            Date.setTextColor(Color.BLACK);
            Amt.setTextColor(Color.BLACK);
            Cash.setTextColor(Color.BLACK);
            CheckTotal.setTextColor(Color.BLACK);
            notes.setTextColor(Color.BLACK);
        }
        else
        {
            RR.setBackgroundColor(Color.WHITE);

            CustName.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            Date.setTextColor(Color.BLACK);
            Amt.setTextColor(Color.BLACK);
            Cash.setTextColor(Color.BLACK);
            CheckTotal.setTextColor(Color.BLACK);
            notes.setTextColor(Color.BLACK);
        }


        return convertView;
    }
}

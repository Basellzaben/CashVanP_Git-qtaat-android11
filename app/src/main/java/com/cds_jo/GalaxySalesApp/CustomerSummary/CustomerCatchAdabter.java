package com.cds_jo.GalaxySalesApp.CustomerSummary;

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

public class CustomerCatchAdabter extends BaseAdapter {
    Context context;
    ArrayList<cls_CustomerCatch> cls_checks;

    public CustomerCatchAdabter(Context context, ArrayList<cls_CustomerCatch> cls_checks) {
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
        final cls_CustomerCatch  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.catch_desgin, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView Amt = (MyTextView) convertView.findViewById(R.id.catchAmt);
        Amt.setText(cls_customerCatch.getAmt());


        MyTextView Date = (MyTextView) convertView.findViewById(R.id.catchDate);
        Date.setText(cls_customerCatch.getDate());

        MyTextView Name = (MyTextView) convertView.findViewById(R.id.catchCustName);
        Name.setText(cls_customerCatch.getCustName());
        MyTextView type = (MyTextView) convertView.findViewById(R.id.catchVType);
        type.setText(cls_customerCatch.getDate());

        MyTextView chaq = (MyTextView) convertView.findViewById(R.id.catchChaq);
        chaq.setText(cls_customerCatch.getCustName());




        if(position%2==0)
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

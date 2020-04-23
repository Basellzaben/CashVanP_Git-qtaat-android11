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

public class DelegateInformation_Adapter  extends BaseAdapter {
    Context context;
    ArrayList<cls_DelegateInformation> cls_checks;

    public DelegateInformation_Adapter(Context context, ArrayList<cls_DelegateInformation> cls_checks) {
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
        final cls_DelegateInformation  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.desgin_delegate_information, null);

        }


        MyTextView manNo = (MyTextView) convertView.findViewById(R.id.manNo);
        manNo.setText(cls_customerCatch.getManNo1());


        MyTextView Man_Name = (MyTextView) convertView.findViewById(R.id.Man_Name);
        Man_Name.setText(cls_customerCatch.getManNm());

        MyTextView CheckIn = (MyTextView) convertView.findViewById(R.id.CheckIn);
        CheckIn.setText(cls_customerCatch.getCheckIn());
        MyTextView checkOut = (MyTextView) convertView.findViewById(R.id.checkOut);
        checkOut.setText(cls_customerCatch.getCheckOut());

        MyTextView Payemnt = (MyTextView) convertView.findViewById(R.id.Payemnt);
        Payemnt.setText(cls_customerCatch.getPayemnt());


        MyTextView Sales = (MyTextView) convertView.findViewById(R.id.Sales);
        Sales.setText(cls_customerCatch.getSales());


        MyTextView returnsSales = (MyTextView) convertView.findViewById(R.id.returnsSales);
        returnsSales.setText(cls_customerCatch.getReturnsSales());

        MyTextView SalesOrders = (MyTextView) convertView.findViewById(R.id.SalesOrders);
        SalesOrders.setText(cls_customerCatch.getSalesOrders());


        MyTextView Precent = (MyTextView) convertView.findViewById(R.id.Precent);
        Precent.setText(cls_customerCatch.getNewCustomers());



        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);
        if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.blue123));
            manNo.setTextColor(Color.WHITE);
            Man_Name.setTextColor(Color.WHITE);
            CheckIn.setTextColor(Color.WHITE);
            checkOut.setTextColor(Color.WHITE);
            Payemnt.setTextColor(Color.WHITE);
            Sales.setTextColor(Color.WHITE);
            returnsSales.setTextColor(Color.WHITE);
            SalesOrders.setTextColor(Color.WHITE);
            Precent.setTextColor(Color.WHITE);

        }

        else if(position%2==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            manNo.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            CheckIn.setTextColor(Color.BLACK);
            checkOut.setTextColor(Color.BLACK);
            Payemnt.setTextColor(Color.BLACK);
            Sales.setTextColor(Color.BLACK);
            returnsSales.setTextColor(Color.BLACK);
            SalesOrders.setTextColor(Color.BLACK);
            Precent.setTextColor(Color.BLACK);
        }

        else
        {
            RR.setBackgroundColor(Color.WHITE);
            manNo.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            CheckIn.setTextColor(Color.BLACK);
            checkOut.setTextColor(Color.BLACK);
            Payemnt.setTextColor(Color.BLACK);
            Sales.setTextColor(Color.BLACK);
            returnsSales.setTextColor(Color.BLACK);
            SalesOrders.setTextColor(Color.BLACK);
            Precent.setTextColor(Color.BLACK);
        }
        return convertView;
    }
}


package com.cds_jo.GalaxySalesApp.OrderApproval;

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

/**
 * Created by Hp on 17/03/2016.
 */
public class Cls_Sales_Order_Hdr_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_SalesOrder_H> cls_Records;


    public Cls_Sales_Order_Hdr_Adapter(Context context, ArrayList<Cls_SalesOrder_H> list) {

        this.context = context;
        cls_Records = list;
    }@Override
    public int getCount() {

        return cls_Records.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_Records.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_SalesOrder_H  obj = cls_Records.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sales_order_hdr_row, null);

        }


        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

        MyTextView OrderNo = (MyTextView) convertView.findViewById(R.id.OrderNo);
        OrderNo.setText(obj.getOrderno());

        MyTextView ManNm = (MyTextView) convertView.findViewById(R.id.ManNm);
        ManNm.setText(obj.getManNm());


        MyTextView tv_CusNm = (MyTextView) convertView.findViewById(R.id.tv_CusNm);
        tv_CusNm.setText(obj.getCustNm());

        MyTextView tv_Date = (MyTextView) convertView.findViewById(R.id.tv_Date);
        tv_Date.setText(obj.getDate());

        MyTextView tv_Total = (MyTextView) convertView.findViewById(R.id.tv_Total);
        tv_Total.setText(obj.getNet_Total());

        RR.setBackgroundColor(context.getResources().getColor(R.color.Black11));
          if(position%2==0)
        {

            ManNm.setTextColor(Color.BLACK);
            OrderNo.setTextColor(Color.BLACK);
            tv_CusNm.setTextColor(Color.BLACK);
            tv_Date.setTextColor(Color.BLACK);
            tv_Total.setTextColor(Color.BLACK);

            ManNm.setBackgroundColor(Color.WHITE);
            OrderNo.setBackgroundColor(Color.WHITE);
            tv_CusNm.setBackgroundColor(Color.WHITE);
            tv_Date.setBackgroundColor(Color.WHITE);
            tv_Total.setBackgroundColor(Color.WHITE);



        }
        else
        {

            ManNm.setTextColor(Color.BLACK);
            OrderNo.setTextColor(Color.BLACK);
            tv_CusNm.setTextColor(Color.BLACK);
            tv_Date.setTextColor(Color.BLACK);
            tv_Total.setTextColor(Color.BLACK);

            ManNm.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            OrderNo.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_CusNm.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_Date.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_Total.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
        }





        return convertView;
    }

}



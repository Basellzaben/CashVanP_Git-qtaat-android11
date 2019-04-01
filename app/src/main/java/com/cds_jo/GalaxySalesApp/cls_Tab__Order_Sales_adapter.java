package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Tab__Order_Sales_adapter extends BaseAdapter {

    Context context;
    ArrayList<cls_Tab_Order_Sales> cls_Tab_Sales;

    public cls_Tab__Order_Sales_adapter(Context context, ArrayList<cls_Tab_Order_Sales> list) {

        this.context = context;
        cls_Tab_Sales = list;
    }

    @Override
    public int getCount() {

        return cls_Tab_Sales.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_Tab_Sales.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        cls_Tab_Order_Sales cls_search_po = cls_Tab_Sales.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cls_tab_sales_order_row, null);

        }

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        MyTextView tvcustNo = (MyTextView) convertView.findViewById(R.id.tv_no);
        tvcustNo.setText(cls_search_po.getCustNo());

        MyTextView tvCustNm = (MyTextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(cls_search_po.getCustNm());

        MyTextView tvDate = (MyTextView) convertView.findViewById(R.id.tvDate);
        tvDate.setText(cls_search_po.getDate());

        MyTextView tvacc = (MyTextView) convertView.findViewById(R.id.tv_AccNo);
        tvacc.setText(cls_search_po.getAcc());


        MyTextView tv_tot = (MyTextView) convertView.findViewById(R.id.tv_tot);
        tv_tot.setText(cls_search_po.getTot());


        MyTextView tv_notes = (MyTextView) convertView.findViewById(R.id.tv_notes);

           if (cls_search_po.getNotes().equals("-3")) {
               tv_notes.setText("حالة الطلب");
           } else if (cls_search_po.getNotes().equals("-1")) {
               tv_notes.setText("غير مرحلة");
           } else {
               tv_notes.setText("مرحلة");
           }

        tvcustNo.setTextColor(Color.BLACK);
        tvCustNm.setTextColor(Color.BLACK);
        tvDate.setTextColor(Color.BLACK);
        tvacc.setTextColor(Color.BLACK);
        tv_tot.setTextColor(Color.BLACK);
        tv_notes.setTextColor(Color.BLACK);

        tvcustNo.setBackgroundColor(Color.WHITE);
        tvCustNm.setBackgroundColor(Color.WHITE);
        tvDate.setBackgroundColor(Color.WHITE);
        tvacc.setBackgroundColor(Color.WHITE);
        tv_tot.setBackgroundColor(Color.WHITE);
        tv_notes.setBackgroundColor(Color.WHITE);




        if(position%2==0 &&  position> 0 )
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tvcustNo.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tvCustNm.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tvDate.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tvacc.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_tot.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_notes.setBackgroundColor(context.getResources().getColor(R.color.Gray2));


        }

        else
        {
            RR.setBackgroundColor(Color.WHITE);
        }



        if(position==0){
            tvcustNo.setBackgroundColor(Color.BLACK);
            tvcustNo.setTextColor(Color.WHITE);

            tvCustNm.setBackgroundColor(Color.BLACK);
            tvCustNm.setTextColor(Color.WHITE);

            tvDate.setBackgroundColor(Color.BLACK);
            tvDate.setTextColor(Color.WHITE);

            tvacc.setBackgroundColor(Color.BLACK);
            tvacc.setTextColor(Color.WHITE);

            tv_tot.setBackgroundColor(Color.BLACK);
            tv_tot.setTextColor(Color.WHITE);

            tv_notes.setBackgroundColor(Color.BLACK);
            tv_notes.setTextColor(Color.WHITE);


        }else
        {

            tvcustNo.setTextColor(Color.BLACK);
            tvCustNm.setTextColor(Color.BLACK);
            tvDate.setTextColor(Color.BLACK);
            tvacc.setTextColor(Color.BLACK);
            tv_tot.setTextColor(Color.BLACK);
            tv_notes.setTextColor(Color.BLACK);
        }

        return convertView;
    }

}


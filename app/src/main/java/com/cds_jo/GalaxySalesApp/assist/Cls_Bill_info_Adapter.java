package com.cds_jo.GalaxySalesApp.assist;

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
 * Created by Hp on 06/03/2016.
 */
public class Cls_Bill_info_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Bill_Info> cls_bill_infos;


    public Cls_Bill_info_Adapter(Context context, ArrayList<Cls_Bill_Info> list) {

        this.context = context;
        cls_bill_infos = list;
    }

    @Override
    public int getCount() {

        return cls_bill_infos.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_bill_infos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Bill_Info  obj = cls_bill_infos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bill_info_row, null);
        }

    MyTextView tv_Bill = (MyTextView) convertView.findViewById(R.id.tv_Bill);
    MyTextView tvdate = (MyTextView) convertView.findViewById(R.id.tv_date);
    MyTextView tv_tot = (MyTextView) convertView.findViewById(R.id.tv_tot);
    MyTextView tv_Price2 = (MyTextView) convertView.findViewById(R.id.tv_Price2);
    MyTextView tv_Qty = (MyTextView) convertView.findViewById(R.id.tv_Qty);
    MyTextView tv_Bounce = (MyTextView) convertView.findViewById(R.id.tv_Bounce);
    MyTextView tv_RetQty = (MyTextView) convertView.findViewById(R.id.tv_RetQty);




        tv_Bill.setText("الفاتورة"+": "+obj.getBill());
        tvdate.setText("تاريخ الفاتورة"+": "+obj.getTr_Date());
        tv_tot.setText("اجمالي الفاتورة"+": "+obj.getTot());

        tv_Price2.setText("سعر البيع"+" "+obj.getPrice2());
        tv_Qty.setText("الكمية"+" "+obj.getQty());
        tv_Bounce.setText("البونص"+" "+obj.getBonus());
        tv_RetQty.setText("المرتجع"+" "+obj.getRetuen_qty());



        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

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


package com.cds_jo.GalaxySalesApp.warehouse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.Cls_Man_Vac;
import com.cds_jo.GalaxySalesApp.warehouse.Cls_Item_Store_Qty;

import java.util.ArrayList;

import Methdes.MyTextView;


public class Item_Store_qty_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Item_Store_Qty> cls_Man_Vac;


    public Item_Store_qty_Adapter(Context context, ArrayList<Cls_Item_Store_Qty> list) {

        this.context = context;
        cls_Man_Vac = list;
    }

    @Override
    public int getCount() {

        return cls_Man_Vac.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_Man_Vac.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final Cls_Item_Store_Qty  cls_payment = cls_Man_Vac.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_store_qty_row, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView tv_Type = (MyTextView) convertView.findViewById(R.id.tv_Storeno);
        tv_Type.setText(cls_payment.getStoreno());


        MyTextView tv_fromdate = (MyTextView) convertView.findViewById(R.id.tv_Sname);
        tv_fromdate.setText(cls_payment.getSname());

        MyTextView tv_todate = (MyTextView) convertView.findViewById(R.id.tv_Qty);
        tv_todate.setText(cls_payment.getQty());

        MyTextView tv_procuder = (MyTextView) convertView.findViewById(R.id.tv_UnitName);
        tv_procuder.setText(cls_payment.getUnitName());



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

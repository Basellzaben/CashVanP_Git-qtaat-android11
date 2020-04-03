package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.assist.Cls_Man_Vac;

import java.util.ArrayList;

import Methdes.MyTextView;


public class ManvacAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Man_Vac> cls_Man_Vac;


    public ManvacAdapter(Context context, ArrayList<Cls_Man_Vac> list) {

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
        final Cls_Man_Vac  cls_payment = cls_Man_Vac.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.man_vac_row, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView tv_Type = (MyTextView) convertView.findViewById(R.id.tv_Type);
        tv_Type.setText(cls_payment.getVacationType_Desc());


        MyTextView tv_fromdate = (MyTextView) convertView.findViewById(R.id.tv_fromdate);
        tv_fromdate.setText(cls_payment.getFromDate());

        MyTextView tv_todate = (MyTextView) convertView.findViewById(R.id.tv_todate);
        tv_todate.setText(cls_payment.getToDate());

        MyTextView tv_procuder = (MyTextView) convertView.findViewById(R.id.tv_procuder);
        tv_procuder.setText(cls_payment.getProcedureType_Desc());

        MyTextView tv_Note = (MyTextView) convertView.findViewById(R.id.tv_Note);
        tv_Note.setText(cls_payment.getNote());


        MyTextView tv_Daycount = (MyTextView) convertView.findViewById(R.id.tv_Daycount);
        tv_Daycount.setText(cls_payment.getVacDays());



        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));

        }
        if(cls_payment.getProcedureType().equalsIgnoreCase("1")){
            RR.setBackgroundColor(Color.GREEN);
        }
        if(cls_payment.getProcedureType().equalsIgnoreCase("2")){
            RR.setBackgroundColor(Color.RED);
        }
        return convertView;
    }
    private static class ViewHolder{
        MyTextView tv_Note,tv_Date,tv_Amt;
    }

}

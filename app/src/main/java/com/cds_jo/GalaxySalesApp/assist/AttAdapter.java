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


public class AttAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Att> cls_Man_Vac;


    public AttAdapter(Context context, ArrayList<Cls_Att> list) {

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
        final Cls_Att  cls_payment = cls_Man_Vac.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.att_row, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView tv_Daynm = (MyTextView) convertView.findViewById(R.id.tv_Daynm);
        tv_Daynm.setText(cls_payment.getDayNm());


        MyTextView tv_date = (MyTextView) convertView.findViewById(R.id.tv_date);
        tv_date.setText(cls_payment.getActionDate());

        MyTextView tv_time = (MyTextView) convertView.findViewById(R.id.tv_time);
        tv_time.setText(cls_payment.getActionTime());

        MyTextView tv_ActionNo = (MyTextView) convertView.findViewById(R.id.tv_ActionNo);
        if(cls_payment.getActionNo().equalsIgnoreCase("1")) {
            tv_ActionNo.setText("بداية دوام");
        }
        if(cls_payment.getActionNo().equalsIgnoreCase("2")) {
            tv_ActionNo.setText("نهاية دوام");
        }
        MyTextView tv_Note = (MyTextView) convertView.findViewById(R.id.tv_Note);
        tv_Note.setText(cls_payment.getNotes());





        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));

        }
        if(cls_payment.getActionNo().equalsIgnoreCase("1")){
            RR.setBackgroundColor(Color.GREEN);
        }
        if(cls_payment.getActionNo().equalsIgnoreCase("2")){
            RR.setBackgroundColor(Color.RED);
        }
        return convertView;
    }
    private static class ViewHolder{
        MyTextView tv_Note,tv_Date,tv_Amt;
    }

}

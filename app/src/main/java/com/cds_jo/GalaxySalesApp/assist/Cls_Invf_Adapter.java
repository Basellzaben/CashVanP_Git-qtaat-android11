package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

/**
 * Created by Hp on 16/03/2016.
 */
public class Cls_Invf_Adapter  extends BaseAdapter {

    Context context;
    ArrayList<Cls_Invf> cls_invfs;


    public Cls_Invf_Adapter(Context context, ArrayList<Cls_Invf> list) {

        this.context = context;
        cls_invfs = list;
    }
    @Override
    public int getCount() {

        return cls_invfs.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_invfs.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Invf cls_invf = cls_invfs.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.invf_row, null);

        }
        Methdes.MyTextView tv_itemno = (Methdes.MyTextView) convertView.findViewById(R.id.tv_itemno);
        tv_itemno.setText(cls_invf.getItem_No());


        Methdes.MyTextView tv_itemname = (Methdes.MyTextView) convertView.findViewById(R.id.tv_itemname);
        tv_itemname.setText(cls_invf.getItem_Name());

        TextView tv_SalesCotaPrecentage = (TextView) convertView.findViewById(R.id.tv_SalesCotaPrecentage);
        TextView tv_SalesCota = (TextView) convertView.findViewById(R.id.tv_SalesCota);
        TextView tv_SalesAmt = (TextView) convertView.findViewById(R.id.tv_SalesAmt);




        ProgressBar pp1 = (ProgressBar) convertView.findViewById(R.id.pp1);
        if(position==0) {
            tv_SalesCotaPrecentage.setVisibility(View.VISIBLE);
            tv_SalesCota.setVisibility(View.VISIBLE);
            tv_SalesAmt.setVisibility(View.VISIBLE);
            pp1.setVisibility(View.VISIBLE);
            pp1.setMax(850);
            pp1.setProgress(350);
            pp1.setIndeterminate(false);
            tv_SalesCotaPrecentage.setText("%41.17");
            tv_SalesCota.setText("850");
            tv_SalesAmt.setText("350");
        } else if(position==1) {
            tv_SalesCotaPrecentage.setVisibility(View.VISIBLE);
            tv_SalesCota.setVisibility(View.VISIBLE);
            tv_SalesAmt.setVisibility(View.VISIBLE);
            pp1.setVisibility(View.VISIBLE);
            pp1.setMax(600);
            pp1.setProgress(100);
            pp1.setIndeterminate(false);
            tv_SalesCotaPrecentage.setText("%16.666");
            tv_SalesCota.setText("600");
            tv_SalesAmt.setText("100");

       } else if(position==2) {
        tv_SalesCotaPrecentage.setVisibility(View.VISIBLE);
        tv_SalesCota.setVisibility(View.VISIBLE);
        tv_SalesAmt.setVisibility(View.VISIBLE);
        pp1.setVisibility(View.VISIBLE);

        pp1.setMax(1200);
        pp1.setProgress(500);
        pp1.setIndeterminate(false);
        tv_SalesCotaPrecentage.setText("%41.666");
        tv_SalesCota.setText("1200");
        tv_SalesAmt.setText("500");
    }
        else
        {
            tv_SalesCotaPrecentage.setVisibility(View.INVISIBLE);
            tv_SalesCota.setVisibility(View.INVISIBLE);
            tv_SalesAmt.setVisibility(View.INVISIBLE);
            pp1.setVisibility(View.INVISIBLE);
        }

        tv_SalesCotaPrecentage.setVisibility(View.INVISIBLE);
        tv_SalesCota.setVisibility(View.INVISIBLE);
        tv_SalesAmt.setVisibility(View.INVISIBLE);
        pp1.setVisibility(View.INVISIBLE);
/*
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);
        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
            tv_itemno.setTextColor(Color.BLACK);
            tv_itemname.setTextColor(Color.BLACK);

        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_itemno.setTextColor(Color.BLACK);
            tv_itemname.setTextColor(Color.BLACK);

        }*/



        return convertView;
    }

}


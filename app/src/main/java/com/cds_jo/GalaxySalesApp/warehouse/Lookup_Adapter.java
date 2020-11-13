package com.cds_jo.GalaxySalesApp.warehouse;


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


public class Lookup_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Lookup> clsSystemCodes;

    public Lookup_Adapter(Context context, ArrayList<Cls_Lookup> list) {

        this.context = context;
        clsSystemCodes = list;
    }

    @Override
    public int getCount() {
        return clsSystemCodes.size();
    }

    @Override
    public Object getItem(int i) {
        return clsSystemCodes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Cls_Lookup obj = clsSystemCodes.get(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.search_lookup_row, null);
        }
        LinearLayout RR2 = (LinearLayout) view.findViewById(R.id.RR1);

        MyTextView tv_no = (MyTextView) view.findViewById(R.id.tv_no);
        MyTextView tv_desc = (MyTextView) view.findViewById(R.id.tv_desc);

        tv_no.setText(obj.getITEM_NO());
        tv_desc.setText(obj.getDESC_A());


        //RR2.setBackgroundColor(context.getResources().getColor(R.color.Black11));


        if (i % 2 == 0) {
            RR2.setBackgroundColor(Color.WHITE);
            tv_no.setBackgroundColor(Color.WHITE);
            tv_desc.setBackgroundColor(Color.WHITE);
            tv_no.setTextColor(Color.BLACK);
            tv_desc.setTextColor(Color.BLACK);
        } else {
            RR2.setBackgroundColor(context.getResources().getColor(R.color.Gray1));

            tv_no.setTextColor(Color.BLACK);
            tv_no.setBackgroundColor(context.getResources().getColor(R.color.Gray1));


            tv_desc.setTextColor(Color.BLACK);
            tv_desc.setBackgroundColor(context.getResources().getColor(R.color.Gray1));
        }


        return view;
    }
}

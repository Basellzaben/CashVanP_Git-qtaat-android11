package com.cds_jo.GalaxySalesApp.InquireItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_po;

import java.util.ArrayList;

import static com.cds_jo.GalaxySalesApp.R.id.RR1;

public class getItem_name_Adabter extends BaseAdapter {

    Context context;
    ArrayList<getItem_name> cls_search_pos;

    public getItem_name_Adabter(Context context, ArrayList<getItem_name> list) {

        this.context = context;
        cls_search_pos = list;
    }

    @Override
    public int getCount() {

        return cls_search_pos.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_search_pos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        getItem_name cls_search_po = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_shr_name, null);

        }
        TextView tvcustNo = (TextView) convertView.findViewById(R.id.itemname);
        tvcustNo.setText(cls_search_po.getItemname());

        TextView tvCustNm = (TextView) convertView.findViewById(R.id.itemno);
        tvCustNm.setText(cls_search_po.getItemno());

        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);

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


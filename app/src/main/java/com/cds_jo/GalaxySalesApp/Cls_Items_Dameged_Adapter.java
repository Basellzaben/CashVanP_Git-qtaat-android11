package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;


import java.util.ArrayList;

public class Cls_Items_Dameged_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Items_Dameged> clsTransQties;

    public Cls_Items_Dameged_Adapter(Context context, ArrayList<Cls_Items_Dameged> list) {

        this.context = context;
        clsTransQties = list;
    }

    @Override
    public int getCount() {

        return clsTransQties.size();
    }

    @Override
    public Object getItem(int position) {

        return clsTransQties.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Items_Dameged cls_items_dameged = clsTransQties.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_dameged_row, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

        Methdes.MyTextView DocNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Doc_no);
        DocNo.setText(cls_items_dameged.getDocno());

        Methdes.MyTextView Item_No = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Item_No);
        Item_No.setText(cls_items_dameged.getItemno());

        Methdes.MyTextView ItemNm = (Methdes.MyTextView) convertView.findViewById(R.id.tv_ItemNm);
        ItemNm.setText(cls_items_dameged.getItem_Name());

        Methdes.MyTextView qty = (Methdes.MyTextView) convertView.findViewById(R.id.tv_qty);
        qty.setText(cls_items_dameged.getqty());

        DocNo.setTextColor(Color.BLACK);
        Item_No.setTextColor(Color.BLACK);
        ItemNm.setTextColor(Color.BLACK);
        qty.setTextColor(Color.BLACK);


        return convertView;






    }
}

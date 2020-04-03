package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 18/03/2016.
 */
public class Cls_ItemCost_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_ItemCost> cls_itemCosts;

    public Cls_ItemCost_Adapter(Context context, ArrayList<Cls_ItemCost> list) {

        this.context = context;
        cls_itemCosts = list;
    }

    @Override
    public int getCount() {

        return cls_itemCosts.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_itemCosts.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_ItemCost cls_itemCost = cls_itemCosts.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemcostrow, null);

        }
        MyTextView bill_no = (MyTextView) convertView.findViewById(R.id.tv_bill_no);
        bill_no.setText(cls_itemCost.getBill_no());

        MyTextView bill_date = (MyTextView) convertView.findViewById(R.id.tv_bill_date);
        bill_date.setText(cls_itemCost.getBill_date());

        MyTextView venname = (MyTextView) convertView.findViewById(R.id.tv_venname);
        venname.setText(cls_itemCost.getVenname());

        MyTextView qty = (MyTextView) convertView.findViewById(R.id.tv_qty);
        qty.setText(cls_itemCost.getQty());

        MyTextView cost = (MyTextView) convertView.findViewById(R.id.tv_cost);
        cost.setText(cls_itemCost.getCost());

        MyTextView UnitName = (MyTextView) convertView.findViewById(R.id.tv_UnitName);
        UnitName.setText(cls_itemCost.getUnitName());


        MyTextView unitcost = (MyTextView) convertView.findViewById(R.id.tv_unitcost);
        unitcost.setText(cls_itemCost.getUnitcost());

        return convertView;
    }

}


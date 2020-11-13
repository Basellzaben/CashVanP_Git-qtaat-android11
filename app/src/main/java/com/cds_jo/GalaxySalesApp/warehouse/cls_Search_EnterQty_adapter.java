package com.cds_jo.GalaxySalesApp.warehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.cls_Search_TransQty;

import java.util.ArrayList;

import Methdes.MyTextView;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Search_EnterQty_adapter extends BaseAdapter {

    Context context;
    ArrayList<cls_Search_TransQty> cls_search_pos;

    public cls_Search_EnterQty_adapter(Context context, ArrayList<cls_Search_TransQty> list) {

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
        cls_Search_TransQty obj = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.enter_qty_row_search, null);

        }
        MyTextView tv_OrderNo = (MyTextView) convertView.findViewById(R.id.tv_OrderNo);
        tv_OrderNo.setText(obj.getOrderNo());

        MyTextView tv_OrderDate = (MyTextView) convertView.findViewById(R.id.tv_OrderDate);
        tv_OrderDate.setText(obj.getOrderDate());

        MyTextView tv_FromDate = (MyTextView) convertView.findViewById(R.id.tv_FromDate);
        tv_FromDate.setText(obj.getFromStore());


        return convertView;
    }

}


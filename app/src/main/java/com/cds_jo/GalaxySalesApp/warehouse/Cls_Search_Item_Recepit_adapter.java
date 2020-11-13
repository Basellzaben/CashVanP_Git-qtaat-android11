package com.cds_jo.GalaxySalesApp.warehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_po;

import java.util.ArrayList;


public class Cls_Search_Item_Recepit_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_ItemRecepit_Search_Row> cls_search_pos;

    public Cls_Search_Item_Recepit_adapter(Context context, ArrayList<Cls_ItemRecepit_Search_Row> list) {

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
        Cls_ItemRecepit_Search_Row cls_search_po = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_item_recpeit_row, null);

        }
        TextView tvcustNo = (TextView) convertView.findViewById(R.id.tv_orderno);
        tvcustNo.setText(cls_search_po.getOrderno());

        TextView tvCustNm = (TextView) convertView.findViewById(R.id.tv_date);
        tvCustNm.setText(cls_search_po.getDate());

        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_purches_serial_nm);
        tvDate.setText(cls_search_po.getPurches_serial_nm());

        TextView tvacc = (TextView) convertView.findViewById(R.id.tv_purches_order_no);
        tvacc.setText(cls_search_po.getPurches_order_no());

     TextView tv_year = (TextView) convertView.findViewById(R.id.tv_year);
        tv_year.setText(cls_search_po.getMYear());

        return convertView;
    }

}


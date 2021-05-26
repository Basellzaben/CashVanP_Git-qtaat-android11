package com.cds_jo.GalaxySalesApp.AbuHaltam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.cls_Search_po;

import java.util.ArrayList;

public class cls_Search_Del_Sale_adapter extends BaseAdapter {

    Context context;
    ArrayList<cls_Search_Dev> cls_search_pos;

    public cls_Search_Del_Sale_adapter(Context context, ArrayList<cls_Search_Dev> list) {

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
        cls_Search_Dev cls_search_po = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sale_del_row_search, null);

        }
        TextView tvcustNo = (TextView) convertView.findViewById(R.id.doc);
        tvcustNo.setText(cls_search_po.getDoc_no());

        TextView tvCustNm = (TextView) convertView.findViewById(R.id.cusno);
        tvCustNm.setText(cls_search_po.getCustNo());

        TextView tvDate = (TextView) convertView.findViewById(R.id.cusname);
        tvDate.setText(cls_search_po.getCustNm());



        return convertView;
    }

}


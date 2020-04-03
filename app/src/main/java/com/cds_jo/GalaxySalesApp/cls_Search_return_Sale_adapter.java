package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

public class cls_Search_return_Sale_adapter extends BaseAdapter {
    Context context;
    ArrayList<com.cds_jo.GalaxySalesApp.assist.cls_Search_po_return> cls_search_pos_return;

    public cls_Search_return_Sale_adapter(Context context, ArrayList<com.cds_jo.GalaxySalesApp.assist.cls_Search_po_return> list) {

        this.context = context;
        cls_search_pos_return = list;
    }

    @Override
    public int getCount() {

        return cls_search_pos_return.size();
    }


    @Override
    public Object getItem(int position) {

        return cls_search_pos_return.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        com.cds_jo.GalaxySalesApp.assist.cls_Search_po_return obj = cls_search_pos_return.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sale_return_row_search, null);

        }
        TextView tvcustNo = (TextView) convertView.findViewById(R.id.tv_no);
        tvcustNo.setText(obj.OrderNo);

        TextView tvCustNm = (TextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(obj.getname());

        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_notes);
        tvDate.setText(obj.getDate());

        TextView tvacc = (TextView) convertView.findViewById(R.id.tv_AccNo);
        tvacc.setText(obj.getAcc());


        TextView tv_tot = (TextView) convertView.findViewById(R.id.tv_Bill);
        tv_tot.setText(obj.getTot());

        return convertView;
    }

}


package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Hp on 05/03/2016.
 */
public class Cls_Search_CusCard extends BaseAdapter {

    Context context;
    ArrayList<Cls_Visit_images_Header> cls_search_pos;

    public Cls_Search_CusCard(Context context, ArrayList<Cls_Visit_images_Header> list) {

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
        Cls_Visit_images_Header cls_search_po = cls_search_pos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_cuscard_row, null);

        }
        Methdes.MyTextView tvcustNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
        tvcustNo.setText("رقم البطاقة :"+cls_search_po.getCustNo());

        Methdes.MyTextView tvCustNm = (Methdes.MyTextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(" اسم العميل :"+cls_search_po.getCustName());

        Methdes.MyTextView tvDate = (Methdes.MyTextView) convertView.findViewById(R.id.tv_notes);
        tvDate.setText("التاريخ :"+cls_search_po.getTr_Date());

        Methdes.MyTextView tvacc = (Methdes.MyTextView) convertView.findViewById(R.id.tv_AccNo);
        tvacc.setText("رقم العميل :"+cls_search_po.getOrderNo());

        return convertView;
    }

}


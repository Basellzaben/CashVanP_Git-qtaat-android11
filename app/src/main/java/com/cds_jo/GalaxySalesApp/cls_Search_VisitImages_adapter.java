package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.assist.cls_Search_po;

import java.util.ArrayList;

/**
 * Created by Hp on 05/03/2016.
 */
public class cls_Search_VisitImages_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Visit_images_Header> cls_search_pos;

    public cls_Search_VisitImages_adapter(Context context, ArrayList<Cls_Visit_images_Header> list) {

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
            convertView = inflater.inflate(R.layout.visit_images_search_search, null);

        }
        Methdes.MyTextView tvcustNo = (Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
        tvcustNo.setText(cls_search_po.getCustNo());

        Methdes.MyTextView tvCustNm = (Methdes.MyTextView) convertView.findViewById(R.id.tv_nm);
        tvCustNm.setText(cls_search_po.getCustName());

        Methdes.MyTextView tvDate = (Methdes.MyTextView) convertView.findViewById(R.id.tv_notes);
        tvDate.setText(cls_search_po.getTr_Date());

        Methdes.MyTextView tvacc = (Methdes.MyTextView) convertView.findViewById(R.id.tv_AccNo);
        tvacc.setText(cls_search_po.getOrderNo());

        return convertView;
    }

}


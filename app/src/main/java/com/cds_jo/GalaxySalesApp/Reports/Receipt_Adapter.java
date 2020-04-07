package com.cds_jo.GalaxySalesApp.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

public class Receipt_Adapter extends BaseAdapter {
    Context context;
    ArrayList<cls_Receipt> cls_checks;

    public Receipt_Adapter(Context context, ArrayList<cls_Receipt> cls_checks) {
        this.context = context;
        this.cls_checks = cls_checks;
    }

    @Override
    public int getCount() {
        return cls_checks.size();    }

    @Override
    public Object getItem(int position) {
        return cls_checks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final cls_Receipt  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.desgin_receipt, null);

        }


        MyTextView OrderNo = (MyTextView) convertView.findViewById(R.id.OrderNo);
        OrderNo.setText(cls_customerCatch.getOrderNo());


        MyTextView Date = (MyTextView) convertView.findViewById(R.id.Date);
        Date.setText(cls_customerCatch.getDate());

        MyTextView Amt = (MyTextView) convertView.findViewById(R.id.Amt);
        Amt.setText(cls_customerCatch.getAmt());
        MyTextView Cash = (MyTextView) convertView.findViewById(R.id.Cash);
        Cash.setText(cls_customerCatch.getCash());

        MyTextView CheckTotal = (MyTextView) convertView.findViewById(R.id.CheckTotal);
        CheckTotal.setText(cls_customerCatch.getCheckTotal());


        MyTextView notes = (MyTextView) convertView.findViewById(R.id.notes);
        notes.setText(cls_customerCatch.getNotes());



        return convertView;
    }
}

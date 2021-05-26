package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.Acc_Report_F.cls_ACC_Report;
import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

public class Acc_Report_Adapter extends BaseAdapter {
    Context context;
    ArrayList<cls_ACC_Report> contactList;

    public Acc_Report_Adapter(Context context, ArrayList<cls_ACC_Report> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        cls_ACC_Report contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.acc_report_d, null);

        }
        Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.itemno);
        tvSlNo.setText(contactListItems.getItem_no());
        Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.itemname);
        tvName.setText(contactListItems.getItemname());
        Methdes.MyTextView tvPrice = (Methdes.MyTextView) convertView.findViewById(R.id.price);
        tvPrice.setText(contactListItems.getPrice());
        Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.qty);
        tvQTY.setText(contactListItems.getQty());
        Methdes.MyTextView tvTAX = (Methdes.MyTextView) convertView.findViewById(R.id.taxvlue);
        tvTAX.setText(contactListItems.getTaxValue());

        Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.uint);
        Unit.setText(contactListItems.getUnitName());




        Methdes.MyTextView Total = (Methdes.MyTextView) convertView.findViewById(R.id.bounse);
        Total.setText(contactListItems.getBonus());


        Methdes.MyTextView Tax_Amt = (Methdes.MyTextView) convertView.findViewById(R.id.dis);
        Tax_Amt.setText(contactListItems.getLinedis());

        double tax;
        double sum;
        tax = Double.parseDouble(contactListItems.getTaxValue());
        sum = Double.parseDouble(contactListItems.getSumWithOutTax());
        sum = sum - tax;
        Methdes.MyTextView  suma = (Methdes.MyTextView) convertView.findViewById(R.id.sum);
        suma.setText(contactListItems.getSumWithOutTax());




        return convertView;
    }
}

package com.cds_jo.GalaxySalesApp;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.assist.Cls_Cust_Last_Trans;

import java.util.ArrayList;

public class Cust_Discount_Dept_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_DeptDiscount> contactList;

    public Cust_Discount_Dept_Adapter(Context context, ArrayList<Cls_DeptDiscount> list) {

        this.context = context;
        contactList = list;
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
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_DeptDiscount contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cust_dept_dicount_row, null);

        }
        Methdes.MyTextView FromAmt = (Methdes.MyTextView) convertView.findViewById(R.id.FromAmt);
        FromAmt.setText(contactListItems.getFrom_Value() + " - " + contactListItems.getTo_Value());

        Methdes.MyTextView ToAmt = (Methdes.MyTextView) convertView.findViewById(R.id.ToAmt);
        ToAmt.setText(contactListItems.getTo_Value());


        Methdes.MyTextView DiscountAmt = (Methdes.MyTextView) convertView.findViewById(R.id.DiscountAmt);
        DiscountAmt.setText(contactListItems.getDiscount_Value());

        Methdes.MyTextView DscountType = (Methdes.MyTextView) convertView.findViewById(R.id.DscountType);
        DscountType.setText("نسبة");


   Methdes.MyTextView CustNm = (Methdes.MyTextView) convertView.findViewById(R.id.CustNm);
        CustNm.setText(contactListItems.getCustNo());






        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));

        }


        return convertView;
    }

}

package com.cds_jo.GalaxySalesApp.assist;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

public class Code_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Code> contactList;

    public Code_Adapter(Context context, ArrayList<Cls_Code> list) {

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
        Cls_Code contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.code_row_adapter, null);

        }
        Methdes.MyTextView CustNm = (Methdes.MyTextView) convertView.findViewById(R.id.CustNm);
        CustNm.setText(contactListItems.getCode());

        Methdes.MyTextView DocType = (Methdes.MyTextView) convertView.findViewById(R.id.DocType);
        DocType.setText(contactListItems.getMaxBouns());


        Methdes.MyTextView Date = (Methdes.MyTextView) convertView.findViewById(R.id.Date);
        Date.setText(contactListItems.getMaxDiscount());

        Methdes.MyTextView DayCount = (Methdes.MyTextView) convertView.findViewById(R.id.DayCount);
        if(contactListItems.getAllBillMaterial().equalsIgnoreCase("1")) {
            DayCount.setText("نعم");
        }else {
            DayCount.setText("لا");
        }
        Methdes.MyTextView Amt = (Methdes.MyTextView) convertView.findViewById(R.id.Amt);
        Amt.setText(contactListItems.getNote());




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

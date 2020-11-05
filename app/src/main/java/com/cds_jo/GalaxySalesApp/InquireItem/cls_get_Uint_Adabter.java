package com.cds_jo.GalaxySalesApp.InquireItem;

import android.content.Context;
import android.net.http.DelegatingSSLSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

public class cls_get_Uint_Adabter extends BaseAdapter {
    Context context;
    ArrayList<cls_get_Uint> contactList;

    public cls_get_Uint_Adabter(Context context, ArrayList<cls_get_Uint> contactList) {
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
        cls_get_Uint contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.uint_d, null);

        }
        Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.uno);
        tvSlNo.setText(contactListItems.getUintno());
        Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.uname);
        tvName.setText(contactListItems.getUnitName());
        Methdes.MyTextView tvPrice = (Methdes.MyTextView) convertView.findViewById(R.id.uprice);
        tvPrice.setText(contactListItems.getPrice());
        Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.rate);
        tvQTY.setText(contactListItems.getOperand());
        if (contactListItems.getMin().equals("1")){
        Methdes.MyTextView tvTAX = (Methdes.MyTextView) convertView.findViewById(R.id.minu);
        tvTAX.setText("X");}
else
        {
            Methdes.MyTextView tvTAX = (Methdes.MyTextView) convertView.findViewById(R.id.minu);
            tvTAX.setText("");
        }
        Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.bcode);
        Unit.setText(contactListItems.getBarcode());








        return convertView;
    }
}

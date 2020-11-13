package com.cds_jo.GalaxySalesApp.InquireItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;


import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import static com.cds_jo.GalaxySalesApp.R.id.RR1;

public class cls_get_items_categ_Adabter  extends BaseAdapter {
    Context context;
    ArrayList<cls_get_items_categ> contactList;

    public cls_get_items_categ_Adabter(Context context, ArrayList<cls_get_items_categ> contactList) {
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
        cls_get_items_categ contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.categ_d, null);

        }
        Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.cno);
        tvSlNo.setText(contactListItems.getCategNo());
        Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.cname);
        tvName.setText(contactListItems.getCatName());
        Methdes.MyTextView tvPrice = (Methdes.MyTextView) convertView.findViewById(R.id.cprice);
        tvPrice.setText(contactListItems.getPrice());
        Methdes.MyTextView tvQTY = (Methdes.MyTextView) convertView.findViewById(R.id.cminprice);
        tvQTY.setText(contactListItems.getMinPrice());
        Methdes.MyTextView tvTAX = (Methdes.MyTextView) convertView.findViewById(R.id.dis);
        tvTAX.setText(contactListItems.getDis());

        Methdes.MyTextView Unit = (Methdes.MyTextView) convertView.findViewById(R.id.bouns);
        Unit.setText(contactListItems.getBonus());




        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);

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

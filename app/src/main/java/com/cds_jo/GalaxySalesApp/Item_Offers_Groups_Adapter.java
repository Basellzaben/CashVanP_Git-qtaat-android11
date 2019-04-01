package com.cds_jo.GalaxySalesApp;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Item_Offers_Groups_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Offers_Groups> contactList;

    public Item_Offers_Groups_Adapter(Context context, ArrayList<Cls_Offers_Groups> list) {

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
        Cls_Offers_Groups contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_groups_row, null);

        }
        Methdes.MyTextView ItemNo = (Methdes.MyTextView) convertView.findViewById(R.id.ItemNo);
        ItemNo.setText(contactListItems.getItem_no());
        Methdes.MyTextView ItemNm = (Methdes.MyTextView) convertView.findViewById(R.id.ItemNm);
        ItemNm.setText(contactListItems.getGro_ename() );
        Methdes.MyTextView Qty = (Methdes.MyTextView) convertView.findViewById(R.id.Qty);
        Qty.setText(contactListItems.getQty());




        ItemNo.setTextColor(Color.BLACK);
        ItemNm.setTextColor(Color.BLACK);
        Qty.setTextColor(Color.BLACK);





        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
            ItemNo.setBackgroundColor(Color.WHITE);
            ItemNm.setBackgroundColor(Color.WHITE);
            Qty.setBackgroundColor(Color.WHITE);

        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            ItemNo.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            ItemNm.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            Qty.setBackgroundColor(context.getResources().getColor(R.color.Gray2));


        }
        return convertView;
    }

}

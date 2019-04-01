package com.cds_jo.GalaxySalesApp;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.assist.Cls_Select_Item_From_Pakage;

import java.util.ArrayList;

public class Item_Offer_Hdrs_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Offers_Hdrs> contactList;

    public Item_Offer_Hdrs_Adapter(Context context, ArrayList<Cls_Offers_Hdrs> list) {

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
        Cls_Offers_Hdrs contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.offer, null);

        }
        Methdes.MyTextView OfferNo = (Methdes.MyTextView) convertView.findViewById(R.id.OfferNo);
        OfferNo.setText(contactListItems.getOfferNo());
        Methdes.MyTextView Desc = (Methdes.MyTextView) convertView.findViewById(R.id.Desc);
        Desc.setText(contactListItems.getDesc());
        Methdes.MyTextView OfferDate = (Methdes.MyTextView) convertView.findViewById(R.id.OfferDate);
        OfferDate.setText(contactListItems.getOfferDate());

       Methdes.MyTextView tv_Cate_Offer = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Cate_Offer);
        tv_Cate_Offer.setText(contactListItems.getCate_Offer());

        Methdes.MyTextView tv_Offer_Type_Item = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Offer_Type_Item);
        tv_Offer_Type_Item.setText(contactListItems.getOffer_Type_Item());


        Methdes.MyTextView OfferExpDate = (Methdes.MyTextView) convertView.findViewById(R.id.OfferExpDate);
        OfferExpDate.setText(contactListItems.getEnd_Date());



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

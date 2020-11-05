package com.cds_jo.GalaxySalesApp.InquireItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cds_jo.GalaxySalesApp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Methdes.MyTextView;

public class file_Adabter  extends BaseAdapter {
    Context context;
    ArrayList<cls_Item_hdr> contactList;

    public file_Adabter(Context context, ArrayList<cls_Item_hdr> contactList) {
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
        cls_Item_hdr contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.file_d, null);

        }
        MyTextView tvSlNo = (MyTextView) convertView.findViewById(R.id.dis);
        tvSlNo.setText(contactListItems.getDescription());
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img);
        Picasso.get().load("http://10.0.1.64:98/"+contactListItems.getImg_Text()).into(imageView);
     








        return convertView;
    }
}

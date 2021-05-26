package com.cds_jo.GalaxySalesApp.AbuHaltam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

public class cls_get_items_search_adabter extends BaseAdapter {
    Context context;
    ArrayList<cls_get_items_search> contactList;

    public cls_get_items_search_adabter(Context context, ArrayList<cls_get_items_search> contactList) {
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
        cls_get_items_search contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.items_search_lay, null);

        }
        Methdes.MyTextView tvSlNo = (Methdes.MyTextView) convertView.findViewById(R.id.cno);
        tvSlNo.setText(contactListItems.getItemNo());
        Methdes.MyTextView tvName = (Methdes.MyTextView) convertView.findViewById(R.id.cname);
        tvName.setText(contactListItems.getItemName());








        return convertView;
    }
}


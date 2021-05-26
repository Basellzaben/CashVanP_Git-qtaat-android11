package com.cds_jo.GalaxySalesApp.AbuHaltam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

public class GetDoneBarCodeAdapter extends BaseAdapter {
    Context context;
    ArrayList<GetDoneBarCode> contactList;

    public GetDoneBarCodeAdapter(Context context, ArrayList<GetDoneBarCode> contactList) {
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
        GetDoneBarCode GetBarCode = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.getbarcodedeisgin, null);

        }
        TextView tvSlNo = (TextView) convertView.findViewById(R.id.barcod);
        tvSlNo.setText(GetBarCode.getBarCode());
        TextView tvName = (TextView) convertView.findViewById(R.id.Note);
        tvName.setText(GetBarCode.getItemNote());









        return convertView;
    }
}

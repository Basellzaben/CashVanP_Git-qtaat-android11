package com.cds_jo.GalaxySalesApp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import Methdes.MyTextView;


public class AlertListAdapter  extends BaseAdapter{
    ArrayList<AlertChoiceItem> mData;
    Context mContext;
    LayoutInflater inflater;
    public AlertListAdapter(ArrayList<AlertChoiceItem> data, Context context) {
        mData = data;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_alert_list, null);
        }
        MyTextView tvTitle = (MyTextView) convertView.findViewById(R.id.textView235);
      //  View v = (View) convertView.findViewById(R.id.vPriorAlertList);
        //v.setBackgroundColor(GetColorByPriority.getColor(position, mContext));
        tvTitle.setText(mData.get(position).getItemNm());



        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);

          if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
        }
        else
        {
            RR.setBackgroundColor(mContext.getResources().getColor(R.color.Gray2));
        }


        return convertView;
    }
}
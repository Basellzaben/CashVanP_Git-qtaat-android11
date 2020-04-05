package com.cds_jo.GalaxySalesApp.assist.dummy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.Cls_UpdateData;

import java.util.ArrayList;

import Methdes.MyTextView;

import static com.cds_jo.GalaxySalesApp.R.id.RR1;

//123****************
public class Cls_UpdateData_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Cls_UpdateData> cls_updateDatas;

    public Cls_UpdateData_Adapter(Context context, ArrayList<Cls_UpdateData> list) {

        this.context = context;
        cls_updateDatas = list;
    }

    @Override
    public int getCount() {

        return cls_updateDatas.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_updateDatas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_UpdateData obj = cls_updateDatas.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.update_data_row, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(RR1);
        MyTextView getDocNo = (MyTextView) convertView.findViewById(R.id.tv_DocNo);
        getDocNo.setText(obj.getDocNo());

        MyTextView tv_DocType = (MyTextView) convertView.findViewById(R.id.tv_DocType);
        tv_DocType.setText(obj.getDocType());


        MyTextView tv_Posted = (MyTextView) convertView.findViewById(R.id.tv_Posted);
        tv_Posted.setText(obj.getDocPostFlag());


        if (obj.getDocPostFlag().equalsIgnoreCase("0")) {
            tv_Posted.setText("   ");
        }else if (obj.getDocPostFlag().equalsIgnoreCase("-1")) {
            tv_Posted.setText("غير مرحلة");

        } else {
            tv_Posted.setText("  مرحلة");
        }
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




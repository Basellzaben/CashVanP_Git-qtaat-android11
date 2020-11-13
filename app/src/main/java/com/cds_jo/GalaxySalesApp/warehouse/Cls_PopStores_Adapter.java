package com.cds_jo.GalaxySalesApp.warehouse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.assist.Cls_Stores;

import java.util.ArrayList;

import static com.cds_jo.GalaxySalesApp.R.id.RR1;


public class Cls_PopStores_Adapter extends BaseAdapter{
    Context context;
    ArrayList<Cls_Stores> cls_curs;


    public Cls_PopStores_Adapter(Context context, ArrayList<Cls_Stores> list) {

        this.context = context;
        cls_curs = list;
    }@Override
     public int getCount() {

        return cls_curs.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_curs.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    public long getPostion(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Stores cls_cur = cls_curs.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.stores_row_sp, null);

        }
        TextView tv_Name = (TextView) convertView.findViewById(R.id.tv_Name);
        tv_Name.setText(cls_cur.getSname());

        TextView tv_No = (TextView) convertView.findViewById(R.id.tv_No);
        tv_No.setText(cls_cur.getSno());

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




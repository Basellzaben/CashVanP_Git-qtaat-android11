package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

/**
 * Created by Hp on 17/03/2016.
 */
public class Cls_Deptf_adapter extends BaseAdapter {

    Context context;
    ArrayList<Cls_Deptf> cls_deptfs;


    public Cls_Deptf_adapter(Context context, ArrayList<Cls_Deptf> list) {

        this.context = context;
        cls_deptfs = list;
    }@Override
    public int getCount() {

        return cls_deptfs.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_deptfs.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Deptf  cls_deptf = cls_deptfs.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.deptf_row, null);

        }
         Methdes.MyTextView tv_itemno = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_Desc);
        tv_itemno.setText(cls_deptf.getType_No());

        Methdes.MyTextView tv_itemname = ( Methdes.MyTextView) convertView.findViewById(R.id.tv_no);
        tv_itemname.setText(cls_deptf.getType_Name());

       /* LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR);
        if(position%2==0)
        {
            RR.setBackgroundColor(Color.WHITE);
            tv_itemno.setTextColor(Color.BLACK);
            tv_itemname.setTextColor(Color.BLACK);

        }
        else
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            tv_itemno.setTextColor(Color.BLACK);
            tv_itemname.setTextColor(Color.BLACK);

        }
        tv_itemno.setTextColor(Color.WHITE);
        tv_itemname.setTextColor(Color.WHITE);
*/
        return convertView;
    }

}



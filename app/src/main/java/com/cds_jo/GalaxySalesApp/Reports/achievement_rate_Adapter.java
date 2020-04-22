package com.cds_jo.GalaxySalesApp.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

public class achievement_rate_Adapter extends BaseAdapter {
    Context context;
    ArrayList<cls_achievement_rate> cls_checks;

    public achievement_rate_Adapter(Context context, ArrayList<cls_achievement_rate> cls_checks) {
        this.context = context;
        this.cls_checks = cls_checks;
    }

    @Override
    public int getCount() {
        return cls_checks.size();    }

    @Override
    public Object getItem(int position) {
        return cls_checks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final cls_achievement_rate  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.achievement_rate, null);

        }


      /*  MyTextView TotQtyTarqet = (MyTextView) convertView.findViewById(R.id.TotQtyTarqet);
        TotQtyTarqet.setText(cls_customerCatch.getManNo() );


        MyTextView TotAmtTarqet = (MyTextView) convertView.findViewById(R.id.TotAmtTarqet);
        TotAmtTarqet.setText(cls_customerCatch.getManName() );
*/

        return convertView;
    }
}

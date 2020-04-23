package com.cds_jo.GalaxySalesApp.Reports;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

public class TarnsDetailsAdapter extends BaseAdapter {
    Context context;
    ArrayList<cls_salesC> cls_checks;

    public TarnsDetailsAdapter(Context context, ArrayList<cls_salesC> cls_checks) {
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
        final cls_salesC  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.trans_report_details, null);

        }
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);

        MyTextView ItemNo = (MyTextView) convertView.findViewById(R.id.ItemNo);
        ItemNo.setText(cls_customerCatch.getItemno());

        MyTextView ItemNm = (MyTextView) convertView.findViewById(R.id.ItemNm);
        ItemNm.setText(cls_customerCatch.getItem_Name());


        MyTextView Qty = (MyTextView) convertView.findViewById(R.id.Qty);
        Qty.setText(cls_customerCatch.getQty());

       MyTextView Price = (MyTextView) convertView.findViewById(R.id.Price);
        Price.setText(cls_customerCatch.getPrice());

        MyTextView OrgPrice = (MyTextView) convertView.findViewById(R.id.OrgPrice);
        OrgPrice.setText(cls_customerCatch.getOrgPrice());

        MyTextView Dis = (MyTextView) convertView.findViewById(R.id.Dis);
        Dis.setText(cls_customerCatch.getDiscount());


        MyTextView bonus = (MyTextView) convertView.findViewById(R.id.bonus);
         bonus.setText(cls_customerCatch.getBounce());


        MyTextView Total = (MyTextView) convertView.findViewById(R.id.Total);
       Total.setText(cls_customerCatch.getLineTotal());




        if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.blue123));

            ItemNm.setTextColor(Color.WHITE);
            ItemNo.setTextColor(Color.WHITE);
            Qty.setTextColor(Color.WHITE);
            Price.setTextColor(Color.WHITE);
            OrgPrice.setTextColor(Color.WHITE);
            Dis.setTextColor(Color.WHITE);
            bonus.setTextColor(Color.WHITE);
            Total.setTextColor(Color.WHITE);


        }

       else if(position%2==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            ItemNo.setTextColor(Color.BLACK);
            ItemNm.setTextColor(Color.BLACK);
            Qty.setTextColor(Color.BLACK);
            Price.setTextColor(Color.BLACK);
            OrgPrice.setTextColor(Color.BLACK);
            Dis.setTextColor(Color.BLACK);
            bonus.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
        }

        else
        {
            RR.setBackgroundColor(Color.WHITE);
            ItemNo.setTextColor(Color.BLACK);
            ItemNm.setTextColor(Color.BLACK);
            Qty.setTextColor(Color.BLACK);
            Price.setTextColor(Color.BLACK);
            OrgPrice.setTextColor(Color.BLACK);
            Dis.setTextColor(Color.BLACK);
            bonus.setTextColor(Color.BLACK);
            Total.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}

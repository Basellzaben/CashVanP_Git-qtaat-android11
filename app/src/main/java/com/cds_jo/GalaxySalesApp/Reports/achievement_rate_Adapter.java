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


        MyTextView ManNo = (MyTextView) convertView.findViewById(R.id.ManNo);
        ManNo.setText(cls_customerCatch.getManNo() );

       MyTextView Man_Name = (MyTextView) convertView.findViewById(R.id.Man_Name);
        Man_Name.setText(cls_customerCatch.getManName() );

        MyTextView Tr_Data = (MyTextView) convertView.findViewById(R.id.Tr_Data);
        Tr_Data.setText(cls_customerCatch.getTr_Date() );

        MyTextView TotalCust = (MyTextView) convertView.findViewById(R.id.TotalCust);
        TotalCust.setText(cls_customerCatch.getTotalCust() );

        MyTextView Visited = (MyTextView) convertView.findViewById(R.id.Visited);
        Visited.setText(cls_customerCatch.getVisited() );

        MyTextView VisitPrecent = (MyTextView) convertView.findViewById(R.id.VisitPrecent);
        VisitPrecent.setText(cls_customerCatch.getVisitPrecent() );

        MyTextView SuccVisit = (MyTextView) convertView.findViewById(R.id.SuccVisit);
        SuccVisit.setText(cls_customerCatch.getSuccVisit() );

         MyTextView SuccPercent = (MyTextView) convertView.findViewById(R.id.SuccPercent);
        SuccPercent.setText(cls_customerCatch.getSuccPercent() );

        MyTextView Score = (MyTextView) convertView.findViewById(R.id.Score);
        Score.setText(cls_customerCatch.getScore() );

        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR1);
        if(position==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.blue123));
            ManNo.setTextColor(Color.WHITE);
            Man_Name.setTextColor(Color.WHITE);
            Tr_Data.setTextColor(Color.WHITE);
            TotalCust.setTextColor(Color.WHITE);
            Visited.setTextColor(Color.WHITE);
            VisitPrecent.setTextColor(Color.WHITE);
            SuccVisit.setTextColor(Color.WHITE);
            SuccPercent.setTextColor(Color.WHITE);
            Score.setTextColor(Color.WHITE);

        }

        else if(position%2==0)
        {
            RR.setBackgroundColor(context.getResources().getColor(R.color.Gray2));
            ManNo.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            Tr_Data.setTextColor(Color.BLACK);
            TotalCust.setTextColor(Color.BLACK);
            Visited.setTextColor(Color.BLACK);
            VisitPrecent.setTextColor(Color.BLACK);
            SuccVisit.setTextColor(Color.BLACK);
            SuccPercent.setTextColor(Color.BLACK);
            Score.setTextColor(Color.BLACK);
        }

        else
        {
            RR.setBackgroundColor(Color.WHITE);

            ManNo.setTextColor(Color.BLACK);
            Man_Name.setTextColor(Color.BLACK);
            Tr_Data.setTextColor(Color.BLACK);
            TotalCust.setTextColor(Color.BLACK);
            Visited.setTextColor(Color.BLACK);
            VisitPrecent.setTextColor(Color.BLACK);
            SuccVisit.setTextColor(Color.BLACK);
            SuccPercent.setTextColor(Color.BLACK);
            Score.setTextColor(Color.BLACK);
        }
        return convertView;
    }
}

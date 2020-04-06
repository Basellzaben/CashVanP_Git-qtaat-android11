package com.cds_jo.GalaxySalesApp.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.CustomerSummary.cls_ManVisit;
import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;

import Methdes.MyTextView;

public class VisitingInformationAdapter  extends BaseAdapter {
    Context context;
    ArrayList<cls_VisitingInformation> cls_checks;

    public VisitingInformationAdapter(Context context, ArrayList<cls_VisitingInformation> cls_checks) {
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
        final cls_VisitingInformation  cls_customerCatch = cls_checks.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.desgin_visiting_information, null);

        }


        MyTextView name = (MyTextView) convertView.findViewById(R.id.CustName);
        name.setText(cls_customerCatch.getCustname());


        MyTextView Man_Name = (MyTextView) convertView.findViewById(R.id.Man_Name);
        Man_Name.setText(cls_customerCatch.getManName());

        MyTextView Name = (MyTextView) convertView.findViewById(R.id.start);
        Name.setText(cls_customerCatch.getStart_Time());
        MyTextView end = (MyTextView) convertView.findViewById(R.id.end);
        end.setText(cls_customerCatch.getEnd_Time());

        MyTextView day = (MyTextView) convertView.findViewById(R.id.day);
        day.setText(cls_customerCatch.getDayNm());


        MyTextView note = (MyTextView) convertView.findViewById(R.id.Note);
        note.setText(cls_customerCatch.getVisit_Note());


        MyTextView Duration = (MyTextView) convertView.findViewById(R.id.Duration);
        Duration.setText(cls_customerCatch.getDuration());

        MyTextView StreatNm = (MyTextView) convertView.findViewById(R.id.StreatNm);
        StreatNm.setText(cls_customerCatch.getStreatNm());


        MyTextView Tr_Data = (MyTextView) convertView.findViewById(R.id.Tr_Data);
        Tr_Data.setText(cls_customerCatch.getTr_Data());

        return convertView;
    }
}

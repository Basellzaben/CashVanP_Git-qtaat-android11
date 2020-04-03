package com.cds_jo.GalaxySalesApp.SmanChart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class QuantitieslistAdabter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Monthly_Items_Amount> cls_chart;


    public QuantitieslistAdabter(Context context, ArrayList<Cls_Monthly_Items_Amount> list) {

        this.context = context;
        cls_chart = list;
    }

    @Override
    public int getCount() {

        return cls_chart.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_chart.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_Monthly_Items_Amount cls_check = cls_chart.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.amountlist, null);

        }


        Methdes.MyTextView ser = (Methdes.MyTextView) convertView.findViewById(R.id.type);
        ser.setText(cls_check.getType_Name());

        Methdes.MyTextView tv_Checkno = (Methdes.MyTextView) convertView.findViewById(R.id.sales);
        tv_Checkno.setText(cls_check.getQuantity().toString());

        Methdes.MyTextView cur_no = (Methdes.MyTextView) convertView.findViewById(R.id.Ret);
        cur_no.setText(cls_check.getSalesQty().toString());
        Methdes.MyTextView cur_no2 = (Methdes.MyTextView) convertView.findViewById(R.id.Ret2);
        cur_no2.setText(cls_check.getRetQty().toString());
        float s= Float.parseFloat(cls_check.getSalesQty());
        float r= Float.parseFloat(cls_check.getRetQty());
        float q= Float.parseFloat(cls_check.getQuantity());

        float p=(((s-r)/q)*100);

        Methdes.MyTextView cur_no1 = (Methdes.MyTextView) convertView.findViewById(R.id.Ret1);
        cur_no1.setText( SToD(p+"")+"");

        return convertView;
    }
    private Double SToD(String str) {
        str=str .replaceAll("[^\\d.]", "");
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }

}
package com.cds_jo.GalaxySalesApp.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SmanChart.Cls_Monthly_Items_Amount;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Methdes.MyTextView;

public class AmountlistAdabter extends BaseAdapter {
    Context context;
    ArrayList<Cls_Monthly_Items_Amount> cls_chart;


    public AmountlistAdabter(Context context, ArrayList<Cls_Monthly_Items_Amount> list) {
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

        MyTextView ser = (MyTextView) convertView.findViewById(R.id.type);
        ser.setText(cls_check.getType_Name());

        MyTextView tv_Checkno = (MyTextView) convertView.findViewById(R.id.sales);
        tv_Checkno.setText(cls_check.getPrice1 ().toString());

        MyTextView cur_no = (MyTextView) convertView.findViewById(R.id.Ret);
        cur_no.setText(cls_check.getSalesAmt().toString());
        MyTextView cur_no2 = (MyTextView) convertView.findViewById(R.id.Ret2);
        cur_no2.setText(cls_check.getRetAmt().toString());
        float s= Float.parseFloat(cls_check.getSalesAmt());
        float r= Float.parseFloat(cls_check.getRetAmt());
        float q= Float.parseFloat(cls_check.getPrice1());

        float p=((s-r)/q)*100;

        MyTextView cur_no1 = (MyTextView) convertView.findViewById(R.id.Ret1);
        cur_no1.setText(String.valueOf( SToD(p+"")));
       /* TextView tvbb = (TextView) convertView.findViewById(R.id.tv_bb);
        tvbb.setText(cls_check.getUserID());


        TextView tvdept = (TextView) convertView.findViewById(R.id.tv_dept);
        tvdept.setText(cls_check.getPost());
*/

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
package com.cds_jo.GalaxySalesApp.assist;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SmanChart.BarChartItem;
import com.cds_jo.GalaxySalesApp.SmanChart.BarChartItemPayment;
import com.cds_jo.GalaxySalesApp.SmanChart.ChartItem;
import com.cds_jo.GalaxySalesApp.SmanChart.Cls_Monthly_Items_Amount;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Methdes.MyTextView;


public class PaymentsChartFragment extends Fragment {
    SqlHandler sqlHandler;
    ArrayList<Cls_Monthly_Items_Amount> List_Data_MP_Chart = new ArrayList<>();
    ArrayList<ChartItem> list;
    Cls_Monthly_Items_Amount obj;
    ListView lv;
    MyTextView tv_CollectionAmt,tv_PaymentAmt,tv_Percent;
    public PaymentsChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        {
            View v = inflater.inflate(R.layout.fragment_payment_chart, container, false);
            // Inflate the layout for this fragment
            list = new ArrayList<>();
try {


    list.add(new BarChartItemPayment(generateDataBar(getDataSer.ChartDate), getActivity(), getDataSer.ChartDate));


    lv = (ListView) v.findViewById(R.id.listView1);
    tv_CollectionAmt = (MyTextView) v.findViewById(R.id.tv_CollectionAmt);
    tv_PaymentAmt = (MyTextView) v.findViewById(R.id.tv_PaymentAmt);
    tv_Percent = (MyTextView) v.findViewById(R.id.tv_Percent);
    List<Cls_Monthly_Items_Amount> t = getDataSer.ChartDate;
    tv_CollectionAmt.setText(t.get(0).getCollectionsAmount());
    tv_PaymentAmt.setText(t.get(0).getPaymnetAmt());
    float s = Float.parseFloat(t.get(0).getPaymnetAmt());

    float q = Float.parseFloat(t.get(0).getCollectionsAmount());

    float p = (((s) / q) * 100);
    tv_Percent.setText(SToD(p + "") + "" + "%");
    // ChartDataAdapter cda = new ChartDataAdapter(getActivity(), list);
    // lv.setAdapter(cda);
}catch ( Exception e){}
    return v;

        }}
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
        private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

            ChartDataAdapter(Context context, List<ChartItem> objects) {
                super(context, 0, objects);
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                //noinspection ConstantConditions
                return getItem(position).getView(position, convertView, getContext());
            }

            @Override
            public int getItemViewType(int position) {
                // return the views type
                ChartItem ci = getItem(position);
                return ci != null ? ci.getItemType() : 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1; // we have 3 different item-types
            }
        }

        /**
         * generates a random ChartData object with just one DataSet
         *
         * @return Bar data
         */
        private BarData generateDataBar (List<Cls_Monthly_Items_Amount> objects){

            ArrayList<BarEntry> entries = new ArrayList<>();

           // for (int i = 0; i <1; i++) {
            try {
                Cls_Monthly_Items_Amount obj = objects.get(0);

                float s = Float.parseFloat(obj.getPaymnetAmt());

                float q = Float.parseFloat(obj.getCollectionsAmount());

                float p = (((s) / q) * 100);
                entries.add(new BarEntry(0, p));

            }catch (Exception ex){}

           //}

            BarDataSet d = new BarDataSet(entries,"Amount");

            d.setColors(ColorTemplate.VORDIPLOM_COLORS);
           // d.setHighLightAlpha(255);
            d.setValueTextSize(25f);

            BarData cd = new BarData(d);

            cd.setBarWidth(0.9f);
            return cd;
        }


}
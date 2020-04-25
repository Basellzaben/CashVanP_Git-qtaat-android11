package com.cds_jo.GalaxySalesApp.Reports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SmanChart.ChartItem;
import com.cds_jo.GalaxySalesApp.SmanChart.Cls_Monthly_Items_Amount;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class BarChartItem_Report extends ChartItem_Report {

 //   private final Typeface mTf;
    ArrayList<Cls_SalesValues> list;

    public BarChartItem_Report(ChartData<?> cd, Context c, ArrayList<Cls_SalesValues> list1) {
        super(cd);
        list=list1;

     //   mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder;
        final ArrayList<String> arrayList=new ArrayList<>();

        for(int i=0;i<list.size();i++)
        { Cls_SalesValues cls_mp_chart=list.get(i);
            arrayList.add(cls_mp_chart.getItem_Name());

        }

        if (convertView == null) {

            holder = new BarChartItem_Report.ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (BarChartItem_Report.ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);

        XAxis xAxis = holder.chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(15.5f);


        xAxis.setDrawAxisLine(true);

        XAxis a = holder.chart.getXAxis();
        a.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        a.setDrawLabels(true);
        a.setGranularity(1f);
        a.setLabelRotationAngle(+50);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(arrayList));

        YAxis leftAxis = holder.chart.getAxisLeft();
        // leftAxis.setTypeface(mTf);

        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        // xAxis.setValueFormatter(new DefaultValueFormatter(2));
        YAxis rightAxis = holder.chart.getAxisRight();

        //   rightAxis.setTypeface(mTf);
        rightAxis.setTextSize(15.5f);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(2f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        //  mChartData.setValueTypeface(mTf);

        // set data
        holder.chart.setData((BarData) mChartData);
        holder.chart.setFitBars(true);


        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart.animateY(500);

        return convertView;
    }


    private static class ViewHolder {
        BarChart chart;
    }
}

package com.cds_jo.GalaxySalesApp.SmanChart;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

public abstract class ChartItem {

    static final int TYPE_BARCHART = 0;
    public static final int TYPE_LINECHART = 1;
    static final int TYPE_PIECHART = 2;

    public ChartData<?> mChartData;

    public ChartItem(ChartData<?> cd) {
        this.mChartData = cd;
    }

    public abstract int getItemType();

    public abstract View getView(int position, View convertView, Context c);
}

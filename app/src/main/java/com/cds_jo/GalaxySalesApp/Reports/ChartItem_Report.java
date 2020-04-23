package com.cds_jo.GalaxySalesApp.Reports;

import android.content.Context;
import android.view.View;

import com.github.mikephil.charting.data.ChartData;

public abstract class ChartItem_Report {

    static final int TYPE_BARCHART = 0;
    static final int TYPE_LINECHART = 1;
    static final int TYPE_PIECHART = 2;

    ChartData<?> mChartData;

    ChartItem_Report(ChartData<?> cd) {
        this.mChartData = cd;
    }

    public abstract int getItemType();

    public abstract View getView(int position, View convertView, Context c);
}

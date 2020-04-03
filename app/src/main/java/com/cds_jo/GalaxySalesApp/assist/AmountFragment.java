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

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SmanChart.BarChartItem;
import com.cds_jo.GalaxySalesApp.SmanChart.ChartItem;
import com.cds_jo.GalaxySalesApp.SmanChart.Cls_Monthly_Items_Amount;
import com.cds_jo.GalaxySalesApp.assist.getDataSer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmountFragment extends Fragment {

    ArrayList<ChartItem> list;
    ListView lv;


    public AmountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        {
            View v = inflater.inflate(R.layout.fragment_q, container, false);
            // Inflate the layout for this fragment
            list = new ArrayList<>();



                    list.add(new BarChartItem(generateDataBar(getDataSer.ChartDate),getContext(),getDataSer.ChartDate));



            lv = (ListView) v.findViewById(R.id.listView2);
            ChartDataAdapter cda = new ChartDataAdapter(this.getActivity(), list);
            lv.setAdapter(cda);

            return v;
        }}

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
            return 2; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Bar data
     */
    private BarData generateDataBar (List<Cls_Monthly_Items_Amount> objects){

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            Cls_Monthly_Items_Amount obj = objects.get(i);
            float s= Float.parseFloat(obj.getSalesAmt());
            float r= Float.parseFloat(obj.getRetAmt());
            float q= Float.parseFloat(obj.getPrice1());

            float p=((s-r)/q)*100;
            entries.add(new BarEntry(i,p));
        }

        BarDataSet d = new BarDataSet(entries, "Quantity");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
      //  d.setHighLightAlpha(255);
        d.setValueTextSize(24f);

        BarData cd = new BarData(d);

        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Pie data
     */
    private PieData generateDataPie (List<Cls_Monthly_Items_Amount> objects) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            Cls_Monthly_Items_Amount obj = objects.get(i);

            entries.add(new PieEntry((Float.parseFloat(obj.getRetAmt())), obj.getType_Name()));
        }
      /*  entries.add(new PieEntry((float) (50.0), "معن " ));
        entries.add(new PieEntry((float) (24.1), "محمد" ));
        entries.add(new PieEntry((float) (25.9), "نضال"));*/

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setValueTextSize(50.5f);
        d.setValueTextColor(R.color.Black11);

        return new PieData(d);
    }


    }



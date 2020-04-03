package com.cds_jo.GalaxySalesApp.assist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SmanChart.Cls_Monthly_Items_Amount;
import com.cds_jo.GalaxySalesApp.assist.AmountlistAdabter;
import com.cds_jo.GalaxySalesApp.assist.getDataSer;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmountListFragment extends Fragment {

    ArrayList<Cls_Monthly_Items_Amount> List_Data_MP_Chart = new ArrayList<>();

    Cls_Monthly_Items_Amount obj;
    ListView lv;

    public AmountListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qlist, container, false);
        lv = (ListView) v.findViewById(R.id.Qlv);

        AmountlistAdabter cda = new AmountlistAdabter(getContext(), getDataSer.ChartDate);
        lv.setAdapter(cda);
        return v;
    }


}



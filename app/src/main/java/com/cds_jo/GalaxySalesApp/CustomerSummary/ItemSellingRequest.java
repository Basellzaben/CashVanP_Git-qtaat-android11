package com.cds_jo.GalaxySalesApp.CustomerSummary;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemSellingRequest extends AppCompatDialogFragment {
    ItemSellingRequestAdapter adabter;
    ListView lv;

    public ItemSellingRequest() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_customer_selling_request_fraq, null);
        lv=(ListView)view.findViewById(R.id.lv);
        adabter = new ItemSellingRequestAdapter (getActivity(), sendData.listDatac2);
        lv.setAdapter(adabter);
        return builder.create();
    }}

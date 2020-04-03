package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;


public class PopItemDamaged extends DialogFragment implements View.OnClickListener {

    View form ;
    RadioButton Damaged,NotDamaged;

    public void onStart()
    {
        super.onStart();


        if (getDialog() == null)
            return;

        //  int dialogWidth =740; // WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here

        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here*/




        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        form =inflater.inflate(R.layout.fragment_pop_item_damaged,container,false);
        getDialog().setTitle( getArguments().getString("ItemName"));
        Damaged=(RadioButton)form.findViewById(R.id.Damaged);
        return form;
    }


    @Override
    public void onClick(View v) {

    }
}


package com.cds_jo.GalaxySalesApp;


import android.app.DialogFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cds_jo.GalaxySalesApp.assist.OrdersItems;


/**
 * Created by Hp on 07/02/2016.
 */

public class Pop_Confirm_New extends DialogFragment implements View.OnClickListener  {
    View form ;
     Button add, cancel;
    @Override
    public void onStart()
    {
        super.onStart();
         if (getDialog() == null)
            return;

       /* int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

*/


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog2);
    }


    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){

        form =inflater.inflate(R.layout.dialog,container,false);

        Methdes.MyTextView text = (Methdes.MyTextView) form.findViewById(R.id.text_dialog);
        text.setText(getArguments().getString("msg") );

        cancel = (Button) form.findViewById(R.id.btn_Cancel);
        cancel.setOnClickListener(this);

        add = (Button) form.findViewById(R.id.btn_dialog);
        add.setOnClickListener(this);

        return  form;
    }

    @Override
    public void onClick(View v) {
        if (v == cancel) {
            this.dismiss();
        }
        if (v == add) {
            if (getArguments().getString("Scr") == "po") {
                ((OrdersItems) getActivity()).Do_New();
            }
            this.dismiss();
        }
    }

    public void show(FragmentManager manager, Object o) {

    }
}



package com.cds_jo.GalaxySalesApp.assist;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.R;


public class ViewDialog {
    public     int Result = 0 ;
    public  int  showDialog(Activity activity, String msg  ){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result=1;
                return;

            }
        });



        Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Result=-1;
                return;

            }
        });

        dialog.show();
        Result=2;
        return Result;
    }
}
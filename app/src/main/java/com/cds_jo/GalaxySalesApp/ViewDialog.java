package com.cds_jo.GalaxySalesApp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.romainpiel.shimmer.Shimmer;

import Methdes.MyTextView;

/**
 * Created by Hp on 23/08/2017.
 */

public class ViewDialog {

    public void showDialog(Activity activity, String msg , String SCR_CODE){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.permission_dialog);



        MyTextView text = (MyTextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Hacen Tunisia Lt.ttf"));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        com.romainpiel.shimmer.ShimmerTextView shimmer_tv = (com.romainpiel.shimmer.ShimmerTextView )dialog.findViewById(R.id.SCR_CODE);
        Shimmer shimmer = new Shimmer();
        shimmer.setRepeatCount(-1)
                .setDuration(1000)
                .setStartDelay(100)
                .setDirection(Shimmer.ANIMATION_DIRECTION_RTL);
        shimmer_tv.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Hacen Tunisia Lt.ttf"));
        shimmer_tv.setText(SCR_CODE);
        shimmer.start(shimmer_tv);


        dialog.show();

    }

}

package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;



public class uptodate extends DialogFragment implements View.OnClickListener{
    RelativeLayout.LayoutParams lp;
   View form;
    Drawable greenProgressbar;
    TextView lastmax;
Button update;
EditText newmax;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        form = inflater.inflate(R.layout.fragment_uptodate, container, false);

        greenProgressbar = this.getResources().getDrawable(R.drawable.progrees_states);

        lastmax=(TextView)form.findViewById(R.id.lastmax);
        Bundle bundle =getArguments();
        final String g=bundle.getString("maxn");
        String activit=bundle.getString("activity");
        lastmax.setText(g);
        update = (Button) form.findViewById(R.id.updated);
        update.setOnClickListener(this);
        newmax=(EditText)form.findViewById(R.id.newmax);
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
if(!newmax.getText().toString().isEmpty()) {

    ((Sale_InvoiceActivity) getActivity()).setmaxn(newmax.getText().toString());


    TextView tv = new TextView(form.getContext());
     lp = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    tv.setLayoutParams(lp);
    tv.setLayoutParams(lp);
    tv.setPadding(10, 15, 10, 15);
    tv.setGravity(Gravity.CENTER);
    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
    tv.setTextColor(Color.WHITE);
    tv.setBackgroundColor(Color.BLUE);
    tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


    final ProgressDialog custDialog = new ProgressDialog(getActivity());
    custDialog.setMessage(" الرحاء الانتظار ... " + "العمل جاري على تحديث البيانات");
    custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
    custDialog.setCanceledOnTouchOutside(false);
    custDialog.setProgress(0);
    tv.setText( "تحديث البيانات");
    custDialog.setCustomTitle(tv);
    custDialog.setMax(1);
    custDialog.setProgressDrawable(greenProgressbar);
    custDialog.show();

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {

    SqlHandler sqlHandler = new SqlHandler(form.getContext());
    String query = " update Sal_invoice_Det set OrderNo = '"+newmax.getText().toString()+ "' where OrderNo='"+g+"'";
    sqlHandler.executeQuery(query);

                String query2 = " update Sal_invoice_Hdr set OrderNo = '"+newmax.getText().toString()+ "' where OrderNo='"+g+"'";
                sqlHandler.executeQuery(query2);



               custDialog.dismiss();
                if (custDialog.getProgress() == custDialog.getMax()) {
                    custDialog.dismiss();
                }

            } catch (final Exception e) {
//              Toast.makeText(form.getContext(),"لم يتم التحديث بنجاح",Toast.LENGTH_LONG).show();
                custDialog.dismiss();
                };
            }
        }
    ).start();

}else
    Toast.makeText(form.getContext(),"لا يمكن ان يكون التحديث بقيمة فارغة",Toast.LENGTH_LONG).show();
                getActivity().getFragmentManager().beginTransaction().remove(uptodate.this).commit();







/*
                custDialog.setMax(js_no.length());
                custDialog.incrementProgressBy(1);

                if (custDialog.getProgress() == custDialog.getMax()) {

                    custDialog.dismiss();
                }
*/






            }
        });
        return form;
    }

    @Override
    public void onClick(View v) {
    }
}






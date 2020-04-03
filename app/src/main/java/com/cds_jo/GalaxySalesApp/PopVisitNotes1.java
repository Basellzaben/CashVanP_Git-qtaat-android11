package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.util.Locale;

public class PopVisitNotes1 extends android.app.DialogFragment  implements View.OnClickListener  {

Button btn_save,btn_Cancel;
EditText Ed_Notes;
    View form ;
    SqlHandler sqlHandler,sql_Handler;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        form =inflater.inflate(R.layout.fragment_pop_visit_notes1,container,false);
        getDialog().setTitle("الملاحظات");

        btn_save=(Button) form.findViewById(R.id.btn_Save);
        btn_save.setOnClickListener(this);

        btn_Cancel=(Button) form.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(this);
        Ed_Notes=(EditText) form.findViewById(R.id.Ed_Notes);

        sqlHandler =new SqlHandler(getActivity());

        sql_Handler = new SqlHandler(getActivity());
        String q = " SELECT Notes From SaleManRounds where start_time ='"+getArguments().getString("start_time")+ "' ";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                Ed_Notes.setText(c1.getString(c1.getColumnIndex("Notes")));

            }

            c1.close();
        }
                return form;
    }
    private  void SaveNotes() {
    ContentValues cv = new ContentValues();
    cv.put("Notes", Ed_Notes.getText().toString());

    long i;
    i = sqlHandler.Update("SaleManRounds", cv, "Closed =0");
    if(i>0){
        Toast.makeText(getActivity(), "تم التخزين بنجاح", Toast.LENGTH_SHORT).show();
    }
    else {
        Toast.makeText(getActivity(), "لم تتم عملية التخزين", Toast.LENGTH_SHORT).show();
    }

}
    @Override
    public void onClick(View v) {
        Ed_Notes = (EditText) form.findViewById(R.id.Ed_Notes);
        if (v == btn_Cancel) {

            this.dismiss();
        } else if (v == btn_save) {
            Ed_Notes = (EditText) form.findViewById(R.id.Ed_Notes);
            SaveNotes();

        }


        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, null);

    }

        }







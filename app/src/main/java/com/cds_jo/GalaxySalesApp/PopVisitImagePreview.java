
package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CheckAdapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Bank_search_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopVisitImagePreview extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    LinearLayout Prv,Nxt;
    SqlHandler sqlHandler;
    EditText  Desc ;
    ImageView img ;
    String ID,OrderNo;

    @Override
    public void onStart(){
        super.onStart();

// safety check
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }
    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.visitimagepreview,container,false);
        getDialog().setTitle("تفاصيل الصورة");
        add =(Button) form.findViewById(R.id.btn_Save_Desc);
        add.setOnClickListener(this);
        cancel=(Button) form.findViewById(R.id.btn_Back);
        cancel.setOnClickListener(this);
        Desc = (EditText)form.findViewById(R.id.et_Desc) ;
        img = (ImageView)form.findViewById(R.id.img);
        sqlHandler = new SqlHandler(getActivity());
        Prv = (LinearLayout)form.findViewById(R.id.LytPrv);
        Nxt = (LinearLayout)form.findViewById(R.id.LytNxt);

        Prv.setOnClickListener(this);
        Nxt.setOnClickListener(this);

        ID = getArguments().getString("ID") ;
        OrderNo= getArguments().getString("OrderNo") ;
        ShowRecod(ID);
        return  form;
    }
    private   void ShowRecod(String ID){
        String q = "";
        q= "Select *  From VisitImagesDtl  Where ID ='"+ID+"'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if(c1!=null && c1.getCount() != 0){
            c1.moveToFirst();
            Desc.setText(c1.getString(c1.getColumnIndex("Desc")));
            File imgFile = new  File(c1.getString(c1.getColumnIndex("ImgUrl1")));
            try {
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    img.setImageBitmap(myBitmap);
                }
            }
            catch (Exception ex){}

            c1.close();
        }



    }

    public void onClick(View v ) {


        if (v == cancel) {
            ((VisitImges)getActivity()).UpdateList();
            this.dismiss();
        }

        if (v == add) {


            String q = "Update VisitImagesDtl set Desc='"+Desc.getText().toString()+"' where ID ='"+ID+"'";
            sqlHandler.executeQuery(q);
            Toast.makeText(getActivity(),"تمت عملية تخزين الوصف بنجاح",Toast.LENGTH_LONG).show();
        }

        if (v == Prv) {
            String q = " Select   ID  From  VisitImagesDtl Where ID <  "+ ID+"  AND OrderNo = '"+OrderNo+"'  Order by ID Desc  limit 1";
            Cursor c1 = sqlHandler.selectQuery(q);
            if(c1!=null && c1.getCount() != 0){
                c1.moveToFirst();
                ID=c1.getString(c1.getColumnIndex("ID")) ;
                c1.close();
            }
            ShowRecod(ID);
        }

        if (v == Nxt) {
            String q = " Select  ID  From  VisitImagesDtl Where ID >  "+ ID+"  AND OrderNo = '"+OrderNo+"'  Order by ID asc  limit 1";
            Cursor c1 = sqlHandler.selectQuery(q);
            if(c1!=null && c1.getCount() != 0){
                c1.moveToFirst();
                ID=c1.getString(c1.getColumnIndex("ID")) ;
                c1.close();
            }
            ShowRecod(ID);
        }

    }
}



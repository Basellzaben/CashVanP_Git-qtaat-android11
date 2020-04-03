
package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopshowCustImage extends DialogFragment implements View.OnClickListener  {
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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
        q= "Select *  From CusfCardAtt  Where no ='"+ID+"'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if(c1!=null && c1.getCount() != 0){
            c1.moveToFirst();
            Desc.setText(c1.getString(c1.getColumnIndex("ImgDesc")));
            File imgFile = new  File(c1.getString(c1.getColumnIndex("img")));
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
            ((CusfCard)getActivity()).UpdateList();
            this.dismiss();
        }

        if (v == add) {


            String q = "Update CusfCardAtt set ImgDesc='"+Desc.getText().toString()+"' where no ='"+ID+"'";
            sqlHandler.executeQuery(q);
            Toast.makeText(getActivity(),"تمت عملية تخزين الوصف بنجاح",Toast.LENGTH_LONG).show();
        }

        if (v == Prv) {
            String q = " Select   no  From  CusfCardAtt Where no <  "+ ID+"  AND OrderNo = '"+OrderNo+"'  Order by no Desc  limit 1";
            Cursor c1 = sqlHandler.selectQuery(q);
            if(c1!=null && c1.getCount() != 0){
                c1.moveToFirst();
                ID=c1.getString(c1.getColumnIndex("no")) ;
                c1.close();
            }
            ShowRecod(ID);
        }

        if (v == Nxt) {
            String q = " Select  no  From  CusfCardAtt Where no >  "+ ID+"  AND OrderNo = '"+OrderNo+"'  Order by no asc  limit 1";
            Cursor c1 = sqlHandler.selectQuery(q);
            if(c1!=null && c1.getCount() != 0){
                c1.moveToFirst();
                ID=c1.getString(c1.getColumnIndex("no")) ;
                c1.close();
            }
            ShowRecod(ID);
        }

    }
}




package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopVideoPreview extends DialogFragment implements View.OnClickListener  {
View form ;
    Button cancel;
    LinearLayout Prv,Nxt;
    SqlHandler sqlHandler;

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
public View onCreateView(LayoutInflater inflater   , ViewGroup container  , Bundle savestate){
    form =inflater.inflate(R.layout.video_preview,container,false);
    getDialog().setTitle(" ");

    cancel=(Button) form.findViewById(R.id.btn_Back);
    cancel.setOnClickListener(this);

    img = (ImageView)form.findViewById(R.id.img);
    sqlHandler = new SqlHandler(getActivity());
    Prv = (LinearLayout)form.findViewById(R.id.LytPrv);
    Nxt = (LinearLayout)form.findViewById(R.id.LytNxt);

    Prv.setOnClickListener(this);
    Nxt.setOnClickListener(this);

    ID = "-1";//getArguments().getString("ID") ;
    OrderNo= getArguments().getString("OrderNo") ;

    String q= "Select ID  From VisitImagesDtl  Where OrderNo ='"+OrderNo+"'  Order by ID Asc  limit 1";
    Cursor c1 = sqlHandler.selectQuery(q);
    if(c1!=null && c1.getCount() != 0){
        c1.moveToFirst();
        ID = c1.getString(c1.getColumnIndex("ID"));
         c1.close();
    }
    File VideoFile ;
    VideoFile = new File("//sdcard/Android/Cv_Videos/1.3gp");
    ShowVideo(VideoFile);
    // ShowRecod(ID);

return  form;
}
    public void ShowVideo(File Path) {
        VideoView video_player_view;
        MediaController media_Controller;
        video_player_view = (VideoView)  form.findViewById(R.id.v1);
        media_Controller = new MediaController(getActivity());



        /*dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        video_player_view.setMinimumWidth(width);
        video_player_view.setMinimumHeight(height);*/

        if (Path != null && Path.exists()) {
            video_player_view.setMediaController(media_Controller);
            video_player_view.setVideoPath(Path+"");
            video_player_view.start();
        }else
        {

            video_player_view.pause();
        }

    }
/*private   void ShowRecod(String ID){
    String q = "";
    String Desc_img = "";
    String bs64 = "";
    q= "Select Imgbase64 ,Desc  From VisitImagesDtl  Where ID ='"+ID+"'";
    Cursor c1 = sqlHandler.selectQuery(q);
    if(c1!=null && c1.getCount() != 0){
        c1.moveToFirst();
        bs64 = c1.getString(c1.getColumnIndex("Imgbase64"));
        Desc_img = c1.getString(c1.getColumnIndex("Desc"));

        c1.close();
    }
    TextView tv_image_desc = (TextView)form.findViewById(R.id.tv_image_desc);
    tv_image_desc.setText(Desc_img);
   img.setImageBitmap(StringToBitMap(bs64));
}
  public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }*/
public void onClick(View v ) {


        if (v == cancel) {
           // ((VisitImges)getActivity()).UpdateList();
            this.dismiss();
        }



        if (v == Prv) {
                String q = " Select   ID  From  VisitImagesDtl Where ID <  "+ ID+"  AND OrderNo = '"+OrderNo+"'  Order by ID Desc  limit 1";
                Cursor c1 = sqlHandler.selectQuery(q);
                if(c1!=null && c1.getCount() != 0){
                    c1.moveToFirst();
                       ID=c1.getString(c1.getColumnIndex("ID")) ;
                    c1.close();
                }
                //ShowRecod(ID);
            }

    if (v == Nxt) {
        String q = " Select  ID  From  VisitImagesDtl Where ID >  "+ ID+"  AND OrderNo = '"+OrderNo+"'  Order by ID asc  limit 1";
        Cursor c1 = sqlHandler.selectQuery(q);
        if(c1!=null && c1.getCount() != 0){
            c1.moveToFirst();
            ID=c1.getString(c1.getColumnIndex("ID")) ;
            c1.close();
        }
      //  ShowRecod(ID);
    }

    }
}



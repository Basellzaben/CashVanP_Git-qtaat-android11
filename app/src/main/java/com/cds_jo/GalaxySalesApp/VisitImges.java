package com.cds_jo.GalaxySalesApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import header.Header_Frag;

import static com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_LEFT;
import static com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_RIGHT;


public class VisitImges extends AppCompatActivity {
    SqlHandler sqlHandler;
    String UserID = "";
    EditText OrderNo;
    SwipeMenuListView lv_Items;
    ArrayList<Cls_VisitImages> Records;
    Cls_VisitImages  obj;
    int REQUEST_CAMERA = 0;
    String StoredPath ;
    String DIRECTORY ;
    String  MaxID="";
    Cls_VisitImages     ListObj ;
    Cls_VisitImages_Adapter contactListAdapter;
    TextView CustNo,CustNm;
    public ProgressDialog loadingdialog;
    SharedPreferences sharedPreferences;
    byte[] byteArray;
    String encodedImage ,ImageTime,ImageDesc,Img_Record_ID;
    Bitmap myBitmap =null;
    public void GetMaxPONo() {
        String query = "SELECT  Distinct  COALESCE(MAX(OrderNo), 0) +1 AS no FROM VisitImagesHdr where UserNo ='" + UserID + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";

        if (c1.getCount() > 0 && c1 != null) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        String max1 = "0";
          sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        max1 = sharedPreferences.getString("m6", "");
        if (max1 == "") {
            max1 = "0";
        }
        max1 = String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max)) {
            max = max1;
        }

        if (max.length() == 1) {
            OrderNo.setText(intToString(Integer.valueOf(UserID), 2) + intToString(Integer.valueOf(max), 5));

        } else {
            OrderNo.setText(intToString(Integer.valueOf(max), 7));

        }


        OrderNo.setFocusable(false);
        OrderNo.setEnabled(false);
        OrderNo.setCursorVisible(false);



    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
    SwipeMenuCreator creator;
     String Desc = "";
    String currentDate;
    String currentDateandTime;
    SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_imges);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        sqlHandler = new SqlHandler(this);
        OrderNo = (EditText) findViewById(R.id.et_OrdeNo);
        Records = new ArrayList<Cls_VisitImages>();
        Records.clear();
        CustNo = (TextView) findViewById(R.id.tv_acc);
        CustNm = (TextView) findViewById(R.id.tv_cusnm);

        CustNo.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));

        lv_Items = (SwipeMenuListView) findViewById(R.id.lv_Items);
        GetMaxPONo();
        ShowList();


        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu  ) {

                        SwipeMenuItem Unfav = new SwipeMenuItem(getApplicationContext());
                        Unfav.setBackground(R.color.Red);
                        Unfav.setWidth(lv_Items.getWidth() /4 );
                        Unfav.setTitle("حذف");
                        Unfav.setTitleSize(25);
                        Unfav.setTitleColor(Color.WHITE);
                        menu.addMenuItem(Unfav);

                        SwipeMenuItem fav= new SwipeMenuItem(getApplicationContext());
                        fav.setBackground(R.color.Blue);
                        fav.setWidth(lv_Items.getWidth() / 4);
                        fav.setTitle("تفاصيل");
                        fav.setTitleSize(25);
                        fav.setTitleColor(Color.WHITE);
                        menu.addMenuItem(fav);
                       // break;

                //}
            }
        };

        lv_Items.setMenuCreator(creator);

        lv_Items.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

                if( Records.get(position).getImgUrl().equalsIgnoreCase("1"))
                {
                    //LsvCars.setMenuCreator(creator);
                    lv_Items.smoothOpenMenu(position);

                }


            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
        lv_Items.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 1:
                         ShowDetails(position);
                        break;
                    case 0:
                        DeleteRow(position, menu, index);
                        // delete
                        break;
                }

                return false;
            }
        });

        lv_Items.setSwipeDirection(DIRECTION_RIGHT);


        lv_Items.setSwipeDirection(DIRECTION_LEFT);


        lv_Items.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });


        lv_Items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                      ListObj = (Cls_VisitImages) lv_Items.getItemAtPosition(position);
                ShowDetails(position);

            }
        });

        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

    }
    private  void DeleteRow(final int position, SwipeMenu menu, int index) {




        String q = "SELECT Distinct *  from  VisitImagesHdr where   Posted >0 AND   OrderNo ='" +OrderNo.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("صور الجولة");
            alertDialog.setMessage("لا يمكن التعديل  لقد تم ترحيل الطلب");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            c1.close();
            return;
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("صور الجولة");
            alertDialog.setMessage("هل انت متاكد من عملية الحذف");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton("" +

                    "نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Do_DeleteRow(position);

                }
            });

            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    private  void Do_DeleteRow(int index){
        ListObj=Records.get(index);
        String q = "Delete from VisitImagesDtl Where ID ='"+ListObj.getID().toString()+"'";
       long i =0;
         sqlHandler.executeQuery(q);
        ShowList();

    }
    private  void ShowDetails(int position) {
        String Strf  = "";
        String R = "0" ;

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");

        bundle.putString("OrderNo", OrderNo.getText().toString());
        bundle.putString("ID", Records.get(position).getID().toString());
        FragmentManager Manager = getFragmentManager();
        PopVisitImagePreview obj = new PopVisitImagePreview();
        obj.setArguments(bundle);
        obj.show(Manager, null);




    }
    private  void ShowList(){

        Records.clear();
        String q = "Select * from VisitImagesDtl Where OrderNo ='"+OrderNo.getText().toString()+"'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if(c1!=null && c1.getCount() != 0){
            c1.moveToFirst();
            do{
                obj = new Cls_VisitImages();
                obj.setDesc(c1.getString(c1.getColumnIndex("Desc")));
                obj.setOrderNo(c1.getString(c1.getColumnIndex("OrderNo")));
                obj.setTr_Date(c1.getString(c1.getColumnIndex("Tr_Date")));
                obj.setTr_Time(c1.getString(c1.getColumnIndex("Tr_Time")));
                obj.setImgUrl(c1.getString(c1.getColumnIndex("ImgUrl1")));
                obj.setID(c1.getString(c1.getColumnIndex("ID")));
                Records.add(obj);

            } while (c1.moveToNext());
            c1.close();
        }

       contactListAdapter = new Cls_VisitImages_Adapter(
                VisitImges.this, Records);
       lv_Items.setAdapter(contactListAdapter);

    }
    public void btn_Add_New_Record(View view) {
        /// EnterDesc();
        selectImage();
    }
    private  void SetTitle( String Desc){

    }

    private void selectImage() {

        if(CustNo.getText().toString().equalsIgnoreCase("")){
            com.cds_jo.GalaxySalesApp.ViewDialog alert = new com.cds_jo.GalaxySalesApp.ViewDialog();
            alert.showDialog(VisitImges.this, "لا يمكن اضافة الصور دون فتح الجولة", "صور الجولة");
            return;
        }

        try {
            //   image_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp_image.jpg";
            image_path=  "//sdcard/Android/Cv_Images/tmp_image.jpg" ;
            File img = new File(image_path);
            Uri uri = Uri.fromFile(img);

            // Uri  uri = FileProvider.getUriForFile(CusfCard.this, BuildConfig.APPLICATION_ID + ".provider",img);


             Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
             startActivityForResult(intent, 2);


           /*
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            startActivityForResult(intent, 2);
*/



        }
        catch (Exception ex){
            Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    String image_path;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        image_path=  "//sdcard/Android/Cv_Images/tmp_image.jpg" ;
          if (resultCode == Activity.RESULT_OK) {

            Bitmap bmp = BitmapFactory.decodeFile(image_path);
             SaveImageFromCammera(bmp);

        }

       /*  if(resultCode==Activity.RESULT_OK) {
            onCaptureImageResult(data);
        }*/
    }
    private  void SaveImageFromCammera(Bitmap b){
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        currentDate = sdf.format(new Date());
        sdf = new SimpleDateFormat("hh:mm:ss ", Locale.ENGLISH);
        currentDateandTime = sdf.format(new Date());
        // Bitmap thumbnail =  b.createScaledBitmap(b, 2000, 1000, false);




        DIRECTORY = "//sdcard/Android/Cv_Images/VisitsImages/"+OrderNo.getText()+"/";
        File sd = Environment.getExternalStorageDirectory();
        StoredPath = DIRECTORY  +System.currentTimeMillis()+".png";
        String folder_main = "/Android/Cv_Images/VisitsImages/"+OrderNo.getText();

        File f ;
        f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File destination = new  File(StoredPath);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fo);
            fo.flush();
            fo.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

          SaveRecord();
         EnterDesc();
          ShowList();
    }
    private void onCaptureImageResult(Intent data) {

        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        currentDate = sdf.format(new Date());
        sdf = new SimpleDateFormat("hh:mm:ss ", Locale.ENGLISH);
        currentDateandTime = sdf.format(new Date());
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        thumbnail = Bitmap.createScaledBitmap(thumbnail, 600, 600, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        DIRECTORY = "//sdcard/Android/Cv_Images/VisitsImages/"+OrderNo.getText()+"/";
        StoredPath = DIRECTORY  +System.currentTimeMillis()+".png";
        String folder_main = "/Android/Cv_Images/VisitsImages/"+OrderNo.getText();
        File f ;
        f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File destination = new  File(StoredPath);
        SaveRecord();
        EnterDesc();
        ShowList();
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
    private  void SaveRecord(){
        String q = "";
        long i = 0 ;
        q = "Select * from VisitImagesHdr    Where OrderNo ='"+OrderNo+ "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if(c1!=null && c1.getCount()!=0){
            i=1;
            c1.close();
        }else{

            ContentValues cv = new ContentValues();
            cv.put("OrderNo", OrderNo.getText().toString() );
            cv.put("CustNo",CustNo.getText().toString());
            cv.put("Tr_Date",currentDate);
            cv.put("Tr_Time",currentDateandTime);
            cv.put("UserNo",UserID);
            cv.put("DayNum","");
            cv.put("Posted",-1);
            cv.put("V_OrderNo",sharedPreferences.getString("V_OrderNo", "0"));
            i = sqlHandler.Insert("VisitImagesHdr",null,cv);
        }


            if(i!=0) {
                ContentValues cv = new ContentValues();

                cv.put("OrderNo", OrderNo.getText().toString());
                cv.put("Tr_Date", currentDate);
                cv.put("Tr_Time", currentDateandTime);
                cv.put("ImgUrl1", StoredPath);

                i = sqlHandler.Insert("VisitImagesDtl", null, cv);


            }


    }
    public String EnterDesc(   ) {

            Desc="";

        String q = " Select   ID   From  VisitImagesDtl Where  OrderNo = '"+OrderNo.getText().toString()+"'  Order by ID Desc  limit 1 "  ;
        Cursor c1 = sqlHandler.selectQuery(q);
        if(c1!=null && c1.getCount() != 0){
            c1.moveToFirst();
            MaxID=c1.getString(c1.getColumnIndex("ID")) ;
            c1.close();
        }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(VisitImges.this);
            alertDialog.setTitle("");
            alertDialog.setMessage("الرجاء ادخال الوصف");

            final EditText input = new EditText(VisitImges.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);

            input.setBackground(getResources().getDrawable(R.drawable.btn_cir_white_fill_black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setIcon(R.drawable.key);

            alertDialog.setPositiveButton("موافق",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            if (input.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(), "الوصف المدخل غير مقبول ", Toast.LENGTH_SHORT).show();
                                input.setError("required!");
                                input.requestFocus();

                                return;

                            } else {
                                    Desc = input.getText().toString();
                                String q = "Update VisitImagesDtl set Desc='"+Desc+"' where ID ='"+MaxID+"'";
                                sqlHandler.executeQuery(q);
                                ShowList();
                                     //selectImage();



                                }

                        }
                    });

            alertDialog.setNegativeButton("لا",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
return  Desc;
}
    public void btn_Search(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "VisitImges");
        FragmentManager Manager = getFragmentManager();
        SearchVisitImagesOrders obj = new SearchVisitImagesOrders();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void UpdateList() {
        ShowList();
    }
    public void Set_Order(String   No, String Nm, String acc) {

        OrderNo.setText(No);
        CustNo.setText(acc);
        CustNm.setText(Nm);
        ShowList();
    }
    public void Do_share(final  String CustomerNo,final String OrderDate,final String UserNo, final String Visit_OrderNo ,final String Order_No
                         ,final String ImageTime,final String ImageDesc ,final String ImageBase64) {
        final String str;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CustNo", CustomerNo);
            jsonObject.put("OrderNo",Order_No);
            jsonObject.put("OrderDate",OrderDate);
            jsonObject.put("UserNo", UserNo);
            jsonObject.put("V_OrderNo", Visit_OrderNo);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        loadingdialog = ProgressDialog.show(VisitImges.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد صور الجولة", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(VisitImges.this);
                ws.Save_VisitImages( CustomerNo,OrderDate,UserNo,Visit_OrderNo,Order_No,ImageTime,ImageDesc,ImageBase64);
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("Posted", We_Result.ID);
                        long i;
                        i = sqlHandler.Update("VisitImagesHdr", cv, "OrderNo='" + DocNo.getText().toString() + "'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        VisitImges.this).create();
                                alertDialog.setTitle("اعتماد طلب بيع");
                                alertDialog.setMessage("تمت عملية اعتماد صور الجولة بنجاح" + We_Result.ID + "");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();
                                Records.clear();
                                ShowList();
                                GetMaxPONo();


                            }
                        });
                    } else if (We_Result.ID == -3) {
                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        VisitImges.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID+"");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage(We_Result.Msg);
                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        VisitImges.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID+"");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage("عملية الاعتماد لم تتم بنجاح" + "    ");
                            }
                        });
                    }

                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    VisitImges.this).create();
                            alertDialog.setTitle("فشل في عمليه الاتصال");
                            alertDialog.setMessage(e.getMessage().toString());
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertDialog.show();
                        }
                    });
                }
            }
        }).start();
    }
    public void btn_share(View view) {
        Toast.makeText(this,"",Toast.LENGTH_LONG).show();
        String CustomerNo,OrderDate,UserNo,Visit_OrderNo,Order_No  ;
         CustomerNo=OrderDate=UserNo=Visit_OrderNo=Order_No = ImageTime=ImageDesc="";


        String query = " SELECT  Distinct  CustNo,OrderNo,Tr_Date,UserNo,V_OrderNo  " +
                " FROM VisitImagesHdr    where OrderNo  ='" + OrderNo.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1.getCount() > 0 && c1 != null) {
            c1.moveToFirst();
            Order_No = c1.getString(c1.getColumnIndex("OrderNo"));
            CustomerNo = c1.getString(c1.getColumnIndex("CustNo"));
            OrderDate = c1.getString(c1.getColumnIndex("Tr_Date"));
            UserNo = c1.getString(c1.getColumnIndex("UserNo"));
            Visit_OrderNo = c1.getString(c1.getColumnIndex("V_OrderNo"));
            c1.close();
        }

         for(int i=0 ;i<Records.size();i++){
             encodedImage = ConvertImgToString64(Records.get(i).getImgUrl().toString());
             ImageTime=Records.get(i).getTr_Time().toString();
             ImageDesc=Records.get(i).getDesc().toString();

             Do_share(CustomerNo,OrderDate,UserNo,Visit_OrderNo,Order_No,ImageTime,ImageDesc,encodedImage);
         }
    }
    private  String ConvertImgToString64(String file){
        Bitmap myBitmap =null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
         String ConvertToBase64 = "";

        File imgFile = new  File(file);
        if(imgFile.exists()){
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }

        if (myBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            ConvertToBase64= Base64.encodeToString(byteArray, Base64.DEFAULT);

        }
        return ConvertToBase64 ;
    }
    public void btn_new(View view) {
        GetMaxPONo();
        ShowList();
    }
    public void btn_delete(View view) {

        String q = "SELECT Distinct *  from  VisitImagesHdr where   Posted >0 AND   OrderNo ='" +OrderNo.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
     /*   if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("صور الجولة");
            alertDialog.setMessage("لا يمكن التعديل  لقد تم ترحيل الطلب");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
           // alertDialog.show();
            c1.close();
          //  return;
        } else {*/


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("حذف صور الجولة");
            alertDialog.setMessage("هل انت متاكد من عملية الحذف");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Delete_Record_PO();

                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });


            alertDialog.show();
        //}
    }
    public void Delete_Record_PO() {
        String query = "Delete from  VisitImagesHdr where OrderNo ='" + OrderNo.getText().toString() + "'";
        sqlHandler.executeQuery(query);

        query = "Delete from  VisitImagesDtl where OrderNo ='" + OrderNo.getText().toString() + "'";
        sqlHandler.executeQuery(query);
        Records.clear();
        GetMaxPONo();
        ShowList();

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("صور الجولة");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();


    }
    public void btn_back(View view) {
        Intent k= new Intent(this, JalMasterActivity.class);
        startActivity(k);
    }
    @Override
    public void onBackPressed() {
       Intent k= new Intent(this, JalMasterActivity.class);
        startActivity(k);

    }
}

package com.cds_jo.GalaxySalesApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cds_jo.GalaxySalesApp.Cusf_Card.PostCusfCardAndGetAcc;
import com.cds_jo.GalaxySalesApp.Cusf_Card.SearchCusf_Cards;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import header.Header_Frag;

import static com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_LEFT;
import static com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_RIGHT;


public class CusfCard extends FragmentActivity implements OnMapReadyCallback {
    SqlHandler sqlHandler;
    TextView tv;
    ProgressDialog progressDialog;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    String UserID = "";
    TextView ed_Acc;
    EditText OrderNo,ed_CusName,ed_Area,ed_CustType,ed_Mobile,ed_GpsLocation,ed_Lat,ed_Long;
    SwipeMenuListView lv_Items;
    ArrayList<Cls_VisitImages> Records;
    Cls_VisitImages  obj;
    int REQUEST_CAMERA = 0;
    String StoredPath ;
    int GoogleMapType = 1;
    String DIRECTORY,Img_ID ;
    String  MaxID="";
    Boolean IsNew;
    Cls_VisitImages     ListObj ;
    Cls_Cusf_Card_Image_Adapter contactListAdapter;
    Double Lat = 0.0;
    Double Lng = 0.0;
    private GoogleMap mMap;
    public ProgressDialog loadingdialog;
    SharedPreferences sharedPreferences;
    byte[] byteArray;
    String encodedImage ,ImageTime,ImageDesc,Img_Record_ID,ImageBase_64;
    Bitmap myBitmap =null;
    public void GetMaxPONo() {
        String query =" SELECT  Distinct  COALESCE(MAX(OrderNo), 0) +1 AS no FROM CusfCard ";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";

        if (c1.getCount() > 0 && c1 != null) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        String max1 = "0";
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        max1 = "0";
        if (max1 == "") {
            max1 = "0";
        }
        max1 = String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max)) {
            max = max1;
        }
        if (max.length() == 1) {
            try {
                OrderNo.setText(intToString(Integer.valueOf(UserID), 2) + intToString(Integer.valueOf(max), 5));
            }catch (Exception e){

            }
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
    String AccFromAB="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.cusf_card);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        sqlHandler = new SqlHandler(this);
        OrderNo = (EditText) findViewById(R.id.et_OrdeNo);
        ed_Acc = (TextView) findViewById(R.id.ed_Acc);
        Records = new ArrayList<Cls_VisitImages>();
        Records.clear();

        ed_CusName = (EditText) findViewById(R.id.ed_CusName);
        IsNew =true;
      //  ed_Area,ed_CustType,ed_Mobile,ed_GpsLocation,ed_Lat
        ed_Area = (EditText) findViewById(R.id.ed_Area);
        ed_CustType = (EditText) findViewById(R.id.ed_CustType);
        ed_Mobile = (EditText) findViewById(R.id.ed_Mobile);
        ed_GpsLocation = (EditText) findViewById(R.id.ed_GpsLocation);
        ed_Lat = (EditText) findViewById(R.id.ed_Lat);
        ed_Long = (EditText) findViewById(R.id.ed_Long);


        Getlocation();
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


        try {
            Lat = 0.0;
            Lng = 0.0;

            if (!ed_Lat.getText().toString().equalsIgnoreCase(""))
                Lat = Double.parseDouble(ed_Lat.getText().toString());

            if (!ed_Long.getText().toString().equalsIgnoreCase(""))
                Lng = Double.parseDouble(ed_Long.getText().toString());

           /* if (Lat > 0 && Long > 0) {
                ShowMap(Lat, Long);
            }*/
        } catch (Exception ex) {
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng sydney = new LatLng(Lat, Lng);
                googleMap.addMarker(new MarkerOptions().title("Paris").position(sydney));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.setMyLocationEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera(zoom);
                mMap.setOnMyLocationChangeListener(myLocationChangeListener);
            }
        });

        String folder_main = "/Android/CashVan_Images/Customer_CardImages/"+OrderNo.getText();
        File f ;
        f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

    }
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

        @Override
        public void onMyLocationChange(Location location) {
            ed_Lat.setText(String.valueOf(location.getLatitude()));
            ed_Long.setText(String.valueOf(location.getLongitude()));

            ShowMap( Double.parseDouble( ed_Lat.getText().toString()),Double.parseDouble(  ed_Long.getText().toString() ));
            // Toast.makeText(ManAttenActivity.this,location.getProvider()+" ",Toast.LENGTH_SHORT).show();

        }
    };
    private  void DeleteRow(final int position, SwipeMenu menu, int index) {

         Cls_VisitImages istObj=Records.get(position);
        String q = "SELECT Distinct *  from  CusfCardAtt  where   Posted  !='-1' AND   no ='" +istObj.getID().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("وثائق العميل");
            alertDialog.setMessage("لا يمكن التعديل ، لقد تم ترحيل الوثيقة");            // Setting Icon to Dialog
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
            alertDialog.setTitle("وثائق العميل");
            alertDialog.setMessage("هل انت متاكد من عملية حذف الوثيقة");
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
    public void btn_save_po(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT Distinct *  from  CusfCard where   Posted !='-1' AND   OrderNo ='" + OrderNo.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("بطاقة العميل");
            alertDialog.setMessage("لا يمكن التعديل على البطاقة، لقد تم ترحيل الطلب");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
           // alertDialog.show();
          //  c1.close();//7
          //  return;
        }






        if (ed_CusName.getText().toString().length() == 0) {
            ed_CusName.setError("يجب إدخال اسم العميل!");
            ed_CusName.requestFocus();
            return;
        }

        if (OrderNo.getText().toString().length() == 0) {
            OrderNo.setError("يجب إدخال رقم البطاقة!");
            OrderNo.requestFocus();
            return;
        }

        AlertDialog Dialog = new AlertDialog.Builder(this).create();



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("بطاقة العميل");
        alertDialog.setMessage("هل تريد الاستمرار بعملية الحفظ");
        alertDialog.setIcon(R.drawable.save);
        alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

               Save_Recod_Po();
            }
        });


        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialog.show();


    }
    public void Save_Recod_Po() {

        sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        int dayOfWeek;

        String q1 = "Select * From CusfCard Where OrderNo='" + OrderNo.getText().toString() + "'";
        Cursor c1;
        c1 = sqlHandler.selectQuery(q1);

        if (c1 != null && c1.getCount() != 0) {
            IsNew = false;
            c1.close();//6
        } else {
            IsNew = true;
        }


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long i;
        ContentValues cv = new ContentValues();
        cv.put("OrderNo", OrderNo.getText().toString());
        cv.put("Nm", ed_CusName.getText().toString());
        cv.put("AreaDesc", ed_Area.getText().toString());
        cv.put("CustType", ed_CustType.getText().toString());
        cv.put("Mobile", ed_Mobile.getText().toString());
        cv.put("Lan", ed_Lat.getText().toString());
        cv.put("Long", ed_Long.getText().toString());
        cv.put("GpsLocation",ed_GpsLocation.getText().toString());
        cv.put("UserID", UserID);
        cv.put("posted", "-1");
          if(ed_Acc.getText().toString().equalsIgnoreCase("")){
              cv.put("Acc", "");
          }else {
              cv.put("Acc", ed_Acc.getText().toString().trim());

          }
        cv.put("Tr_Date", currentDateandTime);
        cv.put("Tr_Time",currentDateandTime);
        if (IsNew == true) {
            i = sqlHandler.Insert("CusfCard", null, cv);
        } else {
            i = sqlHandler.Update("CusfCard", cv, "OrderNo ='" + OrderNo.getText().toString() + "'");
        }


        if (i > 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("بطاقة العميل");
            alertDialog.setMessage("تمت عملية الحفظ بنجاح ");
            // UpDateMaxOrderNo();
            IsNew = false;
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ShareAndGetAcc();
                    View view = null;
                }
            });
            alertDialog.show();
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("بطاقة العميل");
            alertDialog.setMessage("عملية الحفظ لم تتم بنجاح");
            // UpDateMaxOrderNo();
            IsNew = false;
            alertDialog.setIcon(R.drawable.error_new);
            alertDialog.setButton("رجوع", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    View view = null;
                }
            });
            alertDialog.show();
        }
    }
    private  void Do_DeleteRow(int index){
        ListObj=Records.get(index);
        String q = "Delete from CusfCardAtt Where no ='"+ListObj.getID().toString()+"'";
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
        PopshowCustImage obj = new PopshowCustImage();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public   void ShowList(){

        Records.clear();
        String q = "Select img,ImgDesc,OrderNo,no ,  ifnull(Posted,'-1') as Posted  from CusfCardAtt Where OrderNo ='"+OrderNo.getText().toString()+"'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if(c1!=null && c1.getCount() != 0){
            c1.moveToFirst();
            do{
                obj = new Cls_VisitImages();
                obj.setDesc(c1.getString(c1.getColumnIndex("ImgDesc")));
                obj.setOrderNo(c1.getString(c1.getColumnIndex("OrderNo")));
                 obj.setPosted(c1.getString(c1.getColumnIndex("Posted")));
              /*  obj.setTr_Time(c1.getString(c1.getColumnIndex("Tr_Time")));*/
                obj.setImgUrl(c1.getString(c1.getColumnIndex("img")));
                obj.setID(c1.getString(c1.getColumnIndex("no")));
                Records.add(obj);

            } while (c1.moveToNext());
            c1.close();
        }

       contactListAdapter = new Cls_Cusf_Card_Image_Adapter(
                CusfCard.this, Records);
       lv_Items.setAdapter(contactListAdapter);

       try {
           loadingdialog.dismiss();
       }catch (Exception ex){}
    }
    public void btn_Add_New_Record(View view) {
        /// EnterDesc();
        selectImage();
    }
    private  void SetTitle( String Desc){

    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    String image_path;
    private void selectImage() {
        try {
         //   image_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp_image.jpg";
           image_path=  "//sdcard/Android/Cv_Images/tmp_image.jpg" ;
            File img = new File(image_path);
            Uri uri = Uri.fromFile(img);

           // Uri  uri = FileProvider.getUriForFile(CusfCard.this, BuildConfig.APPLICATION_ID + ".provider",img);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, 2);
        }
        catch (Exception ex){
            Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (  resultCode == Activity.RESULT_OK) {

            Bitmap bmp = BitmapFactory.decodeFile(image_path);
            SaveImageFromCammera(bmp);

        }
    }
    private  void StoreImage_NEW(Bitmap b){
        // LinearLayout lay = (LinearLayout) findViewById(R.id.Mainlayout);


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String filename = "z1.jpg";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);

        try {
            FileOutputStream out = new FileOutputStream(dest);
            b.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();
            //  bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private  void SaveImageFromCammera(Bitmap b){
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        currentDate = sdf.format(new Date());
        sdf = new SimpleDateFormat("hh:mm:ss ", Locale.ENGLISH);
        currentDateandTime = sdf.format(new Date());
        // Bitmap thumbnail =  b.createScaledBitmap(b, 2000, 1000, false);




        DIRECTORY = "//sdcard/Android/CashVan_Images/Customer_CardImages/"+OrderNo.getText()+"/";
        File sd = Environment.getExternalStorageDirectory();
        StoredPath = DIRECTORY  +System.currentTimeMillis()+".png";
        String folder_main = "/Android/CashVan_Images/Customer_CardImages/"+OrderNo.getText();

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

        SavImage();
       EnterDesc();
        ShowList();
    }
    private void onCaptureImageResult(Intent data) {

        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        currentDate = sdf.format(new Date());
        sdf = new SimpleDateFormat("hh:mm:ss ", Locale.ENGLISH);
        currentDateandTime = sdf.format(new Date());
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        thumbnail = Bitmap.createScaledBitmap(thumbnail, 300, 300, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        DIRECTORY = "//sdcard/Android/CashVan_Images/Customer_CardImages/"+OrderNo.getText()+"/";
        StoredPath = DIRECTORY  +System.currentTimeMillis()+".png";
        String folder_main = "/Android/CashVan_Images/Customer_CardImages/"+OrderNo.getText();
        File f ;
        f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File destination = new  File(StoredPath);
        SavImage();
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
    private  void SavImage(){
        String q = "";
        long i = 0 ;
                ContentValues cv = new ContentValues();
                cv.put("OrderNo", OrderNo.getText().toString());
                cv.put("img", StoredPath);
                cv.put("ImgDesc", "");
                cv.put("Posted", "-1");
                i = sqlHandler.Insert("CusfCardAtt", null, cv);


    }
    public String EnterDesc(   ) {
try {
    Desc = "";

    String q = " Select   no   From  CusfCardAtt Where  OrderNo = '" + OrderNo.getText().toString() + "'  Order by no Desc  limit 1 ";
    Cursor c1 = sqlHandler.selectQuery(q);
    if (c1 != null && c1.getCount() != 0) {
        c1.moveToFirst();
        MaxID = c1.getString(c1.getColumnIndex("no"));
        c1.close();
    }

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CusfCard.this);
    alertDialog.setTitle("");
    alertDialog.setMessage("الرجاء ادخال الوصف");

    final EditText input = new EditText(CusfCard.this);
    input.setInputType(InputType.TYPE_CLASS_TEXT);

    input.setBackground(getResources().getDrawable(R.drawable.btn_cir_white_fill_black));
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
    input.setLayoutParams(lp);
    alertDialog.setView(input);
    alertDialog.setIcon(R.drawable.key);

    alertDialog.setPositiveButton("تخزين",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AddImageDesc(input.getText().toString());
                }
            });

    alertDialog.setNegativeButton("رجوع",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

    alertDialog.setNeutralButton("وتخزين إضافة وثيقة", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            AddImageDesc(input.getText().toString());
            selectImage();
        }
    });

    alertDialog.show();
}catch (Exception ex){
    Toast.makeText(this ,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
}
return  Desc;
}
    private  void  AddImageDesc( String Desc ){
    if (Desc.toString().equalsIgnoreCase("")) {
        Toast.makeText(getApplicationContext(), "الوصف المدخل غير مقبول ", Toast.LENGTH_SHORT).show();


        return;

    } else {

        String q = "Update CusfCardAtt set ImgDesc='"+Desc+"' where no ='"+MaxID+"'";
        sqlHandler.executeQuery(q);
        ShowList();
        // selectImage();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }
}
    public void btn_Search(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Cusf_Cards");
        FragmentManager Manager = getFragmentManager();
        SearchCusf_Cards obj = new SearchCusf_Cards();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void UpdateList() {
        ShowList();
    }
    private void ShowCustInfo(String No){
        String q= "select   * from CusfCard where OrderNo='"+No+"'";
        Cursor c = sqlHandler.selectQuery(q);
        if(c!=null  && c.getCount()> 0 ){
            c.moveToNext();
            ed_CusName.setText(c.getString(c.getColumnIndex("Nm")));
            ed_Area.setText(c.getString(c.getColumnIndex("AreaDesc")));
            ed_CustType.setText(c.getString(c.getColumnIndex("CustType")));
            ed_Mobile.setText(c.getString(c.getColumnIndex("Mobile")));
            ed_Acc.setText(c.getString(c.getColumnIndex("Acc")));
            ed_Lat.setText(c.getString(c.getColumnIndex("Lan")));
            ed_Long.setText(c.getString(c.getColumnIndex("Long")));
            ed_GpsLocation.setText(c.getString(c.getColumnIndex("GpsLocation")));
            c.close();
        }
    }
    public void Set_Order(String   No, String Nm, String acc) {

        OrderNo.setText(No);
        ShowCustInfo(No);
        ShowList();
    }

    public void btn_Add_sig(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("OrderNo", OrderNo.getText().toString());
        FragmentManager Manager = getFragmentManager();
        CustomerSigActivity obj = new CustomerSigActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);


    }

    public void btn_Customer(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "CusfCard");
        FragmentManager Manager = getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void Set_Cust(String No, String Nm) {

        ed_Acc.setText(No);
        ed_CusName.setText(Nm);
        ed_CusName.setError(null);
    }
    public void Do_share_Images(final  String ImgID, final  String CusName,final String OrderNo,final String Acc,final String ImageDesc ,final String ImageBase64) {
        final String str;
        loadingdialog = ProgressDialog.show(CusfCard.this, "الرجاء الانتظار ..."+ ImageDesc, "العمل جاري على اعتماد الصور ", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.dismiss();
        loadingdialog.show();
        final Handler _handler = new Handler();



        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(CusfCard.this);
                ws.SaveCusImnages(Acc     ,ImageDesc ,ImageBase64);
                try {
                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("Posted", We_Result.ID);
                        sqlHandler.Update("CusfCardAtt", cv, "no='" + ImgID + "'");
                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();
                                ShowList();
                                ShreImages();
                            }
                        });
                    } else {
                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();
                            }
                        });
                    }
                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                            loadingdialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    private class Do_share_Images_New extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {


            loadingdialog = ProgressDialog.show(CusfCard.this, "الرجاء الانتظار ..." + ImageDesc, "العمل جاري على اعتماد الصور ", true);
            loadingdialog.setCancelable(false);
            loadingdialog.setCanceledOnTouchOutside(true);
            loadingdialog.dismiss();
            loadingdialog.show();
            final Handler _handler = new Handler();

        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            CallWebServices ws = new CallWebServices(CusfCard.this);
            ws.SaveCusImnages( ed_Acc.getText().toString(), ImageDesc, encodedImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                if (We_Result.ID > 0) {
                    ContentValues cv = new ContentValues();
                    cv.put("Posted", We_Result.ID);
                    sqlHandler.Update("CusfCardAtt", cv, "no='" + Img_ID + "'");

                    loadingdialog.dismiss();
                    ShowList();
                    ShreImages();
                } else {


                    loadingdialog.dismiss();

                }
            } catch (final Exception e) {

                loadingdialog.dismiss();
            }


        }
    }
    private class shareCustAndGetAcc extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            tv = new TextView(getApplicationContext());
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


            progressDialog = new ProgressDialog(CusfCard.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على حفظ البيانات  ");
            tv.setText("بطاقة العميل");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            PostCusfCardAndGetAcc obj = new PostCusfCardAndGetAcc(CusfCard.this);
            AccFromAB = obj.DoTrans(OrderNo.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CusfCard.this);

            alertDialog.setMessage("تمت عملية الحفظ بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setTitle("بطاقة العميل");
            if (Double.parseDouble( AccFromAB) >0) {
                alertDialog.setMessage("تمت عملية الاعتماد بنجاح");
                alertDialog.setIcon(R.drawable.tick);
                ed_Acc.setText(AccFromAB+"");
                InsertIntoCustomer(AccFromAB+"");
                ShreImages();

            } else {


                alertDialog.setMessage(" عملية الاعتماد لم تتم بنجاح");
                alertDialog.setIcon(R.drawable.error_new);
                alertDialog.show();
            }
            progressDialog.dismiss();
          //  alertDialog.show();
        }

    }
    private  void  InsertIntoCustomer(String ACC){
        String q="Select * from Customers where no ='"+ACC+"'";
        Cursor c = sqlHandler.selectQuery(q);
        if(c!=null && c.getCount()>0){
            c.close();
        }else{
            ContentValues cv = new ContentValues();
            cv.put("no", ACC);
            cv.put("name", ed_CusName.getText().toString());
            cv.put("Ename",ed_CusName.getText().toString());
            cv.put("catno", "1");
            cv.put("SMan", UserID);
            cv.put("Latitude", ed_Lat.getText().toString());
            cv.put("Longitude", ed_Long.getText().toString());
            cv.put("sat", "1");
            cv.put("sun", "1");
            cv.put("mon", "1");
            cv.put("tues", "1");
            cv.put("wens", "1");
            cv.put("thurs", "1");
            cv.put("Celing", "0");
            cv.put("Cust_type", "1");
            cv.put("Tax_Status", "0");
            cv.put("CheckAlowedDay", "0");
            cv.put("PromotionFlag", "0");
            sqlHandler.Insert("Customers", null, cv);

        }
    }
    public void btn_share(View view) {
        String CustomerNo, OrderDate, Visit_OrderNo, Order_No;
        CustomerNo = OrderDate = Visit_OrderNo = Order_No = ImageTime = ImageDesc = "";

        if (ed_CusName.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this,"لم يمكن الاعتماد قبل تخزين البطاقة",Toast.LENGTH_SHORT).show();
            } else {
            ShareAndGetAcc();
          //  ShreImages();

        }
    }
    private  void ShreImages(){
             for (int i = 0; i < Records.size(); i++) {
                if(Records.get(i).getPosted().toString().equalsIgnoreCase("-1")) {
                      encodedImage = ConvertImgToString64(Records.get(i).getImgUrl().toString());
                     if(Records.get(i).getDesc()!=null) {
                         ImageDesc = Records.get(i).getDesc().toString();
                     }else{
                         ImageDesc="غير مدخل";
                     }

                     Do_share_Images(Records.get(i).getID().toString(), ed_CusName.getText().toString(), OrderNo.getText().toString(), ed_Acc.getText().toString(), ImageDesc, encodedImage);
                  // new Do_share_Images_New().execute();
                    // Img_ID=Records.get(i).getID().toString();
                     /* try {
                        loadingdialog.dismiss();
                    }catch (Exception ex){}*/
               break;
                }
            }



    }

    private  void ShareAndGetAcc(){
      new  shareCustAndGetAcc().execute();
    }
    private  String ConvertImgToString64(String file){
        String ConvertToBase64 = "";
        try {
            Bitmap myBitmap = null;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


            File imgFile = new File(file);
            if (imgFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }

            if (myBitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byteArray = stream.toByteArray();
                ConvertToBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

            }
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
        return ConvertToBase64 ;
    }
    public void btn_new(View view) {
        Do_New();
        GetMaxPONo();

        ShowList();
    }
    public void btn_delete(View view) {
     //   Delete_Record_PO();
        String q = "SELECT Distinct *  from  CusfCard where   Posted !='-1' AND   OrderNo ='" +OrderNo.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("بطاقة العميل");
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
            alertDialog.setTitle("بطاقة العميل");
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
        }
    }
    public void Delete_Record_PO() {
        String query = "Delete from  CusfCard where OrderNo ='" + OrderNo.getText().toString() + "'";
        sqlHandler.executeQuery(query);

        query = "Delete from  CusfCardAtt where OrderNo ='" + OrderNo.getText().toString() + "'";
        sqlHandler.executeQuery(query);
        Records.clear();
        GetMaxPONo();
        ShowList();

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("بطاقة العميل");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Do_New();
            }
        });
        alertDialog.show();


    }
    private  void Do_New(){
        ed_CusName.setText("");
        ed_Area.setText("");
        ed_CustType.setText("");
        ed_Mobile.setText("");
        ed_Acc.setText("");
        ed_Lat.setText("");
        ed_Long.setText("");
        ed_GpsLocation.setText("");
        GetMaxPONo();
        Getlocation();
        String folder_main = "/Android/CashVan_Images/Customer_CardImages/"+OrderNo.getText();
        File f ;
        f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            Lat = 0.0;
            Lng = 0.0;

            if (!ed_Lat.getText().toString().equalsIgnoreCase(""))
                Lat = Double.parseDouble(ed_Lat.getText().toString());

            if (!ed_Long.getText().toString().equalsIgnoreCase(""))
                Lng = Double.parseDouble(ed_Long.getText().toString());

            if (Lat > 0 && Lng > 0) {
                ShowMap(Lat, Lng);
            }
        } catch (Exception ex) {
        }

    }
    public void btn_Satellite(View view) {
        GoogleMapType = 2;
        mMap.setMapType(GoogleMapType);
    }

    public void btn_Normal(View view) {
        GoogleMapType = 1;
        mMap.setMapType(GoogleMapType);
    }
    public void btn_UpdateLocations(View view) {
        Getlocation();
        ShowMap(0.0,0.0);
    }
    public  void GetStreetName() {
        try {

            Geocoder myLocation = new Geocoder(this, Locale.getDefault());
            List<Address> myList = null;
            try {
                myList = myLocation.getFromLocation(Double.parseDouble(ed_Lat.getText().toString()), Double.parseDouble(ed_Long.getText().toString()), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address1 = (Address) myList.get(0);
            String addressStr = "";
            addressStr += address1.getAddressLine(0) + ", ";
            addressStr += address1.getAddressLine(1) + ", ";
            addressStr += address1.getAddressLine(2);

            ed_GpsLocation.setText(addressStr.replace("null", "").replace(",", ""));
        } catch (Exception ex) {
        }
    }
    public void Getlocation() {

try {
    String address = "";
    GPSService mGPSService = new GPSService(this);
    mGPSService.getLocation();

    if (mGPSService.isLocationAvailable == false) {

        // Here you can ask the user to try again, using return; for that
        // Toast.makeText(this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
        return;

        // Or you can continue without getting the location, remove the return; above and uncomment the line given below
        // address = "Location not available";
    } else {

        // Getting location co-ordinates
        double latitude = mGPSService.getLatitude();
        double longitude = mGPSService.getLongitude();

        address = mGPSService.getLocationAddress();


        int precision = (int) Math.pow(10, 6);
        //tv_x.setText(String.format("%.4f", latitude, Locale.US));

        try {


        } catch (Exception ex) {
            ed_Lat.setText("0.0");
            ed_Long.setText("0.0");
        }
        ed_Lat.setText(String.valueOf(latitude));
        ed_Long.setText(String.valueOf(longitude));
        ed_GpsLocation.setText(address);

    }

    GetStreetName();
}catch (Exception ex){}
    }
    private void ShowMap(Double Lat, Double Long) {

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                }
            });
        }
        Lat = 0.0;
        Long = 0.0;

        if (!ed_Lat.getText().toString().equalsIgnoreCase(""))
            Lat = Double.parseDouble(ed_Lat.getText().toString());

        if (!ed_Long.getText().toString().equalsIgnoreCase(""))
            Long = Double.parseDouble(ed_Long.getText().toString());


        if (Lat > 0 && Long > 0) {
            LatLng sydney = new LatLng(Lat, Long);
            // LatLng sydney = new LatLng(Lat,Long);
            try {
                mMap.clear();

            }catch (Exception ex){

            }
            try {
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                //
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.addMarker(new MarkerOptions().position(sydney).title( ed_CusName.getText().toString() ));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(zoom);
                mMap.setMyLocationEnabled(true);
                mMap.setBuildingsEnabled(true);
                mMap.animateCamera(zoom);
                mMap.setOnMyLocationChangeListener(myLocationChangeListener);
            }catch (Exception ex){
                // Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        }
    }
}

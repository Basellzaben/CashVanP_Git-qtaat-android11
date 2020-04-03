package com.cds_jo.GalaxySalesApp.ManCard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.GalaxyLoginActivity;
import com.cds_jo.GalaxySalesApp.ManCard.cls_DetailCardMan;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.UpdateDataToMobileActivity;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;
import com.google.android.gms.games.appcontent.AppContentAction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import Methdes.MyTextView;

public class DetailCardMan extends DialogFragment implements View.OnClickListener  {
    private MyTextView email;
    MyTextView name, phone;
    TextView superf,Br,manNo;
    String  UserID;
    ImageView imageView, addimg;
    String image64,Phone,Email,Name1;

    Integer REQUEST_CAMERA=1, SELECT_FILE=0;
    public ProgressDialog loadingdialog;
    cls_DetailCardMan obj;
    ArrayList<cls_DetailCardMan> alist;
    SqlHandler sqlHandler;
    final Handler _handler = new Handler();
    View form ;
    Button cancel,btn_Update;
    @Override
    public View onCreateView(LayoutInflater inflater   , ViewGroup container  , Bundle savestate){

        form =inflater.inflate(R.layout.cardmandailog,container,false);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alist=new ArrayList<>();
        imageView=(ImageView) form.findViewById(R.id.img_profile);
        addimg=(ImageView) form.findViewById(R.id.img_plus);
        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();


            }
        });

        superf=(TextView) form.findViewById(R.id.SupervisorNutes);
        name=(MyTextView)form.findViewById(R.id.name);
        Br=(TextView)form.findViewById(R.id.Br);
        manNo=(TextView)form.findViewById(R.id.manNo);
        email=(MyTextView) form.findViewById(R.id.email);
        phone=(MyTextView) form.findViewById(R.id.phone);
        MyTextView  tv_name=(MyTextView) form.findViewById(R.id.tv_name);

        getData1();
        showphoto();

        name.clearFocus();
        phone.clearFocus();
        manNo.requestFocus();

        cancel=(Button) form.findViewById(R.id.btn_Back);
        cancel.setOnClickListener(this);
        btn_Update=(Button) form.findViewById(R.id.btn_Update);
        btn_Update.setOnClickListener(this);


        btn_Update.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        cancel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));

//        String query = " select count(*) as count from cardMan ";
//        Cursor c1 = sqlHandler.selectQuery(query);


        //  getData1();


       /* builder.setView(view)
                .setNegativeButton("تحديث البيانات", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updata();
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

*/


        getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        hideKeyboard();

        return form;
    }
    private void hideKeyboard() {


        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = new View(getActivity());
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void SelectImage(){

        final CharSequence[] items={"فتح الكاميرا","ملفات الجهاز","استرجاع من نظام المحاسبة" ,"إالغاء"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("إضافة الصورة");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("فتح الكاميرا")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("ملفات الجهاز")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent,SELECT_FILE);

                } else if (items[i].equals("استرجاع من نظام المحاسبة")) {
                    get_photo_ser();

                } else if (items[i].equals("إالغاء")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }


    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==REQUEST_CAMERA){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bmp);

            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
            }

        }
    }
    public void getData1() {

        sqlHandler = new SqlHandler(this.getActivity());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
        String query = "Select name,ifnull(BranchName,'') as brnm,ifnull(SupervisorName,'') as super_nm,man,Mobile1 ,Email from manf where man='"+UserID+"'";
        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
           c1.moveToFirst() ;
                    // AM
                    name.setText(c1.getString(c1.getColumnIndex("name")).toString());
                    Br.setText(c1.getString(c1.getColumnIndex("brnm")));
                    email.setText(c1.getString(c1.getColumnIndex("Email")));
                    superf.setText(c1.getString(c1.getColumnIndex("super_nm")));
                    manNo.setText(c1.getString(c1.getColumnIndex("man")));
                    phone.setText(c1.getString(c1.getColumnIndex("Mobile1")));

            c1.close();
        }

    }
    public void updata()
    {
        sqlHandler = new SqlHandler(this.getActivity());
        loadingdialog = ProgressDialog.show(getActivity(), "الرجاء الانتظار ...", "العمل جاري على ترحيل الدفعات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UserID = sharedPreferences.getString("UserID", "-1");
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
        Bitmap bitmap;
        if(bitmapDrawable==null){
            imageView.buildDrawingCache();
            bitmap = imageView.getDrawingCache();
            imageView.buildDrawingCache(false);
        }else
        {
            bitmap = bitmapDrawable .getBitmap();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        image64= Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        Name1=name.getText().toString();
        Phone=phone.getText().toString();
        Email=email.getText().toString();
        String query =" UPDATE manf SET name='"+Name1+"',Mobile1='"+Phone+"',Email='"+Email+"' WHERE man='"+UserID+"'";
        String q =" UPDATE PhotoMan SET Photo='"+image64+"' WHERE man='"+UserID+"'";

        sqlHandler.executeQuery(q);

        sqlHandler.executeQuery(query);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                ws.UpdataCardMan(UserID,Name1,Phone,Email,image64);
                try {

                    if (We_Result.ID>0){

                       _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(  getActivity()).create();
                                alertDialog.setTitle("بطاقة المندوب ");
                                alertDialog.setMessage("تمت عملية التعديل  بنجاح");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();


                            }
                        });
                        loadingdialog.dismiss();


                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        getActivity()).create();
                                alertDialog.setTitle("تم تحديث البيانات");

                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                            }});}
                    else {
                        loadingdialog.dismiss();

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        getActivity()).create();
                                alertDialog.setTitle("لم يتم تحديث البيانات");

                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                            }});
                    }
                }
                catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
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
    public void showphoto() {

        sqlHandler = new SqlHandler(this.getActivity());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // final String UserID = sharedPreferences.getString("UserID", "");
        String query = "Select * from PhotoMan ";
        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] imageBytes = baos.toByteArray();
                    imageBytes = Base64.decode(c1.getString(c1.getColumnIndex("Photo")), Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(decodedImage);

                    i = i + 1;
                } while (c1.moveToNext());


            }
            c1.close();
        }
        else {
            get_photo_ser();
        }

    }
    public void get_photo_ser() {
        sqlHandler = new SqlHandler(this.getActivity());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                ws.Get_Man_Photo(UserID);
                try {

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_man = js.getJSONArray("manNo");
                    JSONArray js_photo = js.getJSONArray("photo");


                    sqlHandler.executeQuery("delete from PhotoMan");


                        String q = "Insert INTO PhotoMan(manNo,Photo) values ('"
                                + js_man.get(0).toString()
                                + "','" + js_photo.get(0).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                        // getData.arrayList.add(new cls_DetailCardMan(name.get(i).toString(),email.get(i).toString(),photo.get(i).toString(),BranchName.get(i).toString(),manNo.get(i).toString(),SupervisorName.get(i).toString(),phone.get(i).toString()));
                        final String a=js_photo.get(0).toString();
                        _handler.post(new Runnable() {
                            public void run() {

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] imageBytes = baos.toByteArray();
                                imageBytes = Base64.decode(a, Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                imageView.setImageBitmap(decodedImage);

                            }
                        });


                }catch (final Exception e) {


                }

            }


        }).start();

    }

    @Override
    public void onClick(View v) {
        if (v == cancel) {
            this.dismiss();
        }
        else if (v == btn_Update) {
            updata();
        }
    }

   /* public void getData1() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                ws.Get_DetailsMan(UserID);
                try {

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray name = js.getJSONArray("Name");
                    JSONArray email = js.getJSONArray("Email");
                    JSONArray photo = js.getJSONArray("photo");
                    JSONArray BranchName = js.getJSONArray("BranchName");
                    JSONArray manNo = js.getJSONArray("manNo");
                    JSONArray SupervisorName = js.getJSONArray("SupervisorName");
                    JSONArray phone = js.getJSONArray("Phone");

                      for(int i=0;i<name.length();i++)
                      {
                    alist.add( new cls_DetailCardMan(name.get(i).toString(),email.get(i).toString(),photo.get(i).toString(),BranchName.get(i).toString(), manNo.get(i).toString(),SupervisorName.get(i).toString(),phone.get(i).toString()));
                      }
                        // getData.arrayList.add(new cls_DetailCardMan(name.get(i).toString(),email.get(i).toString(),photo.get(i).toString(),BranchName.get(i).toString(),manNo.get(i).toString(),SupervisorName.get(i).toString(),phone.get(i).toString()));

                    _handler.post(new Runnable() {
                        public void run() {
                            gata();

                        }
                    });




                } catch (final Exception e) {


                }

            }


        }).start();

    }*/
    /*public void gata() {
        sqlHandler = new SqlHandler(getActivity());


        cls_DetailCardMan a;
        if(alist.size()>0) {

              a = alist.get(0);
           String q = "INSERT INTO cardMan (manName,Email,BranchName,manNo,SupervisorName,Phone,photo) values ('"
                                + a.getName1()
                                + "','" + a.getEmail1()
                                + "','" + a.getManNo()
                                + "','" + a.getSupervisorName()
                                + "','" + a.getPhone()
                                + "','" + a.getBranchName()
                                + "','" + a.getPhoto()

                                + "')";


                        sqlHandler.executeQuery(q);
                       getData();


        }else {
                  Toast.makeText(getActivity(),"ttttttttttttttttttt",Toast.LENGTH_LONG).show();
        }
    }

    public void getData() {
        String query = " select * from cardMan";
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                   ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] imageBytes = baos.toByteArray();
                    imageBytes = Base64.decode(c1.getString(c1.getColumnIndex("photo")), Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(decodedImage);
                    Calendar now = Calendar.getInstance();
                    if(now.get(Calendar.AM_PM) == Calendar.AM){
                        // AM
                        name.setText("صباح الخير استاذ "+c1.getString(c1.getColumnIndex("manName")).toString());
                    }else{
                        // PM
                        name.setText("مساء الخير استاذ "+c1.getString(c1.getColumnIndex("manName")).toString());
                    }


                   // name.setText(c1.getString(c1.getColumnIndex("Name")));
                    Br.setText(c1.getString(c1.getColumnIndex("BranchName")));
                    email.setText(c1.getString(c1.getColumnIndex("Email")));
                    superf.setText(c1.getString(c1.getColumnIndex("SupervisorName")));
                    manNo.setText(c1.getString(c1.getColumnIndex("manNo")));
                    phone.setText(c1.getString(c1.getColumnIndex("Phone")));


                } while (c1.moveToNext());
            }
            c1.close();


//                    JSONObject js = new JSONObject(We_Result.Msg);
//                    JSONArray name = js.getJSONArray("Name");
//                    JSONArray email = js.getJSONArray("Email");
//                    JSONArray photo = js.getJSONArray("photo");
//                    JSONArray BranchName = js.getJSONArray("BranchName");
//                    JSONArray manNo = js.getJSONArray("manNo");
//                    JSONArray SupervisorName = js.getJSONArray("SupervisorName");
//                    JSONArray phone = js.getJSONArray("Phone");


    }}*/


}



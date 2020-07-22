//package com.cds_jo.GalaxySalesApp;
 //
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.os.Handler;
//import android.preference.PreferenceManager;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
//import org.json.JSONException;
//import org.json.JSONObject;
//import header.Header_Frag;
//
//public class Change_Password extends AppCompatActivity {
//
//    EditText et_oldpass,et_newpass,et_returnnewpass;
//    ImageButton im_save;
//    SqlHandler sqlHandler,sql_Handler;
//    String UserID;
//    Context context;
//    public ProgressDialog loadingdialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_change__password);
//
//        et_oldpass = (EditText) findViewById(R.id.et_oldpass);
//        et_newpass = (EditText) findViewById(R.id.et_newpass);
//        et_returnnewpass = (EditText) findViewById(R.id.et_returnnewpass);
//
//
//        sqlHandler = new SqlHandler(this);
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        UserID = sharedPreferences.getString("UserID", "");
//
//        Fragment frag = new Header_Frag();
//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();
//
//    }
//
//    public void savepass(){
//
//        et_oldpass=(EditText) findViewById(R.id.et_oldpass);
//        et_newpass=(EditText) findViewById(R.id.et_newpass);
//        et_returnnewpass=(EditText) findViewById(R.id.et_returnnewpass);
//        String t="";
//        sql_Handler = new SqlHandler(this);
//        String q = " SELECT password From manf where man ='" + UserID + "' ";
//        SqlHandler sqlHandler = new SqlHandler(this);
//        Cursor c1 = sqlHandler.selectQuery(q);
//        if (c1 != null && c1.getCount() != 0) {
//            if (c1.moveToFirst()) {
//                  t = c1.getString(c1.getColumnIndex("password"));
//                c1.close();
//        }
//
//        }
//
//        if (!et_oldpass.getText().toString().equalsIgnoreCase(t)) {
//            Toast.makeText(this, "المعلومات غير صحيحة", Toast.LENGTH_SHORT).show();
//            Change_Password.super.closeContextMenu();
//
//        }
//        else if(!et_returnnewpass.getText().toString().equalsIgnoreCase(et_newpass.getText().toString()) ) {
//            Toast.makeText(this, "المعلومات غير صحيحة", Toast.LENGTH_SHORT).show();
//
//        }
//        else{
//
//            long i;
//            ContentValues cv = new ContentValues();
//            cv.put("password", et_newpass.getText().toString());
//
//            i = sqlHandler.Update("manf", cv, "man ='" + UserID + "'");
//            if(i>0){
//                Toast.makeText(this, "تم التخزين بنجاح", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(this, "لم تتم عملية التخزين", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }
//
//
//}
//
//    public void Do_share (final String man,final String password) {
//
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//
//            jsonObject.put("man", man);
//            jsonObject.put("password", password);
//
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//
//        loadingdialog = ProgressDialog.show(Change_Password.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد الرقم الجديد", true);
//        loadingdialog.setCancelable(true);
//        loadingdialog.setCanceledOnTouchOutside(true);
//        loadingdialog.show();
//        final Handler _handler = new Handler();
//
//
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                CallWebServices ws = new CallWebServices(Change_Password.this);
//                ws.changepassword(UserID, et_newpass.getText().toString());
//                try {
//
//                    if (We_Result.ID > 0) {
//                        ContentValues cv = new ContentValues();
//                        cv.put("password", We_Result.ID);
//                        long i;
//                        i = sqlHandler.Update("manf", cv, "man='" + man + "'");
//
//                        _handler.post(new Runnable() {
//                            public void run() {
//                                AlertDialog alertDialog = new AlertDialog.Builder(
//                                        Change_Password.this).create();
//                                alertDialog.setTitle("اعتماد الباسورد");
//                                alertDialog.setMessage("تمت عملية اعتمادالباسورد بنجاح" + We_Result.ID + "");
//                                alertDialog.setIcon(R.drawable.tick);
//                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                });
//                                loadingdialog.dismiss();
//                                alertDialog.show();
//
//
//                            }
//                        });
//                    }else {
//
//                        _handler.post(new Runnable() {
//                            public void run() {
//                                AlertDialog alertDialog = new AlertDialog.Builder(
//                                        Change_Password.this).create();
//                                alertDialog.setTitle("اعتماد الباسورد");
//                                alertDialog.setMessage("لم تتم عملية اعتمادالباسورد بنجاح" + We_Result.ID + "");
//                                alertDialog.setIcon(R.drawable.tick);
//                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                });
//                                loadingdialog.dismiss();
//                                alertDialog.show();
//
//
//                            }
//                        });
//                    }
//
//
//
//
//                } catch (final Exception e) {
//                    loadingdialog.dismiss();
//                    _handler.post(new Runnable() {
//                        public void run() {
//                            AlertDialog alertDialog = new AlertDialog.Builder(
//                                    Change_Password.this).create();
//                            alertDialog.setTitle("فشل في عمليه الاتصال");
//                            alertDialog.setMessage(e.getMessage().toString());
//                            alertDialog.setIcon(R.drawable.tick);
//                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            });
//
//                            alertDialog.show();
//                        }
//                    });
//                }
//            }
//        }).start();
//    }
//
//
//
//    public void bt_save(View view) {
//        savepass();
//        Do_share(UserID,et_newpass.getText().toString());
//
//
//
//    }
//
//    public void btn_back(View view){
//        Intent i=new Intent(this,JalMasterActivity.class);
//        startActivity(i);
//    }
//}

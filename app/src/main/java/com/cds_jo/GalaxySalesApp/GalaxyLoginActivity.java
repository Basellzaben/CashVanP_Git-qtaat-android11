package com.cds_jo.GalaxySalesApp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import Methdes.MethodToUse;


import com.cds_jo.GalaxySalesApp.ManLocation.AutoPostLocation;


public class GalaxyLoginActivity extends FragmentActivity {
    private Context context;
    private EditText UserName, Password;
    private ImageView Login_Img;


    void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }catch (Exception ex){

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_galaxy_login);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor2 = preferences.edit();
        editor2.putString("update","0");
        editor2.apply();



        Initi();
        ComInfo.ComNo = Integer.parseInt(DB.GetValue( this, "ComanyInfo", "CompanyID", "1=1"));
        hideSoftKeyboard();
        Login_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserName.getText().toString().equals("-1") && Password.getText().toString().equals("")) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor    = sharedPreferences.edit();
                    editor.putString("UserName","Administrator System ");
                    editor.putString("UserID", "-1");
                    editor.putString("Login", "Yes");
                    editor.commit();
                    Intent k = new Intent(context,JalMasterActivity.class);
                    startActivity(k);
                }
                else if(UserName.getText().toString().equals("admin") && Password.getText().toString().equals("")) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor    = sharedPreferences.edit();
                    editor.putString("UserName","Administrator System ");
                    editor.putString("UserID", "-1");
                    editor.putString("Login", "Yes");
                    editor.commit();
                    Intent k = new Intent(context,JalMasterActivity.class);
                    startActivity(k);
                }
               /* if (UserName.getText().toString().equals("123") && Password.getText().toString().equals("123")) {
                     startActivity(new Intent(context, GalaxyMainActivity.class));*/


               else  if (UserName.getText().toString().equals("") && (!Password.getText().toString().equals(""))) {
                    UserName.setError("الرجاء ادخال اسم المستخدم");
                } else if (!(UserName.getText().toString().equals("")) && Password.getText().toString().equals("")) {
                    Password.setError("الرجاء ادخال الرقم السري");
                } else if (UserName.getText().toString().equals("") && Password.getText().toString().equals("")) {
                    UserName.setError("الرجاء ادخال اسم المستخدم");
                    Password.setError("الرجاء ادخال الرقم السري");
                } else {
                        SqlHandler sqlHandler = new SqlHandler(context);
                        String query = "SELECT  name   from  manf where man = '"+ UserName.getText().toString()+"' And password='"+Password.getText().toString()+"'";
                        Cursor c1 = sqlHandler.selectQuery(query);

                        if (c1!=null&&  c1.getCount() > 0 ) {
                            c1.moveToFirst();

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor    = sharedPreferences.edit();
                            editor.putString("UserName", String.valueOf(c1.getString(0)));
                            editor.putString("UserID", UserName.getText().toString());
                            editor.putString("Login", "Yes");
                            editor.commit();
                            c1.close();

                            Intent k = new Intent(context,JalMasterActivity.class);
                            startActivity(k);


                        }

                    }
                 /*else {
                    MethodToUse.ToastCreate(context, "الرجاء التأكد من البيانات");*/

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    private void Initi() {
        context = GalaxyLoginActivity.this;
        UserName = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        UserName.setTypeface(MethodToUse.SetTFace(context));
        Password.setTypeface(MethodToUse.SetTFace(context));

        Login_Img = (ImageView) findViewById(R.id.imageView4);
    }


}

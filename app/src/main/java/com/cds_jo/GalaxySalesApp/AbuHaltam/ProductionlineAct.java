package com.cds_jo.GalaxySalesApp.AbuHaltam;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.Pos.Cls_Item_Adapter;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.UpdateDataToMobileActivity;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems;
import com.google.gson.Gson;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class ProductionlineAct extends AppCompatActivity  {
    List<String> categories;
    List<String> categories2;
    String item2;
    GetBarCode cls_barCode;
    GetDoneBarCode cls_barCode1;
    String ItemName;
    String ItemNo;
    String TransNo;
    String Date;
    long Result= -1;
    TextView textView;
    EditText TransNo1;
    EditText date1;
    EditText Man_Name;
    SqlHandler sqlHandler;
    EditText serch,tv_BarcodeValue,  tv_SearchValue ;
    TextView countdone, countndone;
    ArrayList<GetBarCode> list;
    ArrayList<GetDoneBarCode> list1;
    final Handler _handler = new Handler();
    GetBarCodeAdapter adapter;
    String Searsh;
    GetDoneBarCodeAdapter adapter1;
    ListView listView;
    ListView listView1;
    Spinner spinner;
    public ProgressDialog loadingdialog;

    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productionline);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        serch = (EditText) findViewById(R.id.Man_Name);
        serch.setText(sharedPreferences.getString("UserName", "")+"/"+sharedPreferences.getString("UserID", ""));
        //tv_UserNm.setText( sharedPreferences.getString("UserName", "")+"/"+TypeDesc);
        listView = (ListView) findViewById(R.id.lv1);
        TransNo1 = (EditText) findViewById(R.id.TransNo1);
        date1 = (EditText) findViewById(R.id.date1);
        listView1 = (ListView) findViewById(R.id.lv2);

        tv_BarcodeValue = (EditText)  findViewById(R.id.tv_BarcodeValue);
        tv_SearchValue = (EditText)  findViewById(R.id.tv_SearchValue);
        countdone = (TextView) findViewById(R.id.countdone);
        countndone = (TextView) findViewById(R.id.countndone);
        // Spinner Drop down elements
        sqlHandler = new SqlHandler(this);
        categories = new ArrayList<String>();
        categories2 = new ArrayList<String>();
        tv_SearchValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
               FilterItems3(arg0.toString());
            }
        });

        tv_BarcodeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.toString().length() > 0){
                    if (((int)(arg0.toString().charAt(arg0.toString().length()-1)) == 10)) {
                        GetItemByBarcode(arg0.toString().subSequence(0, arg0.toString().length() - 1).toString());
                    }
                }

            }
        });

        tv_BarcodeValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                hideKeyboard(view);

            }
        });
        tv_SearchValue.setVisibility(View.GONE);
        //showBarCode();
        FilterItems();
        FilterItems1();
//        countdone.setText(String.valueOf(adapter1.getCount()));
//        countndone.setText(String.valueOf(adapter.getCount()));
        // Creating adapter for spinner
        getDate();

        // Drop down layout style - list view with radio button


        // attaching data adapter to spinner


//           @Override
//           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//               String item = parent.getItemAtPosition(position).toString();
//               ItemName = parent.getItemAtPosition(position).toString();
//               ItemNo =categories2.get(position).toString();
//               item2 =categories2.get(position).toString();
//               spinner.setSelection(position);
//               Thread thread = new Thread() {
//                   @Override
//                   public void run() {
//
//
//                       CallWebServices ws = new CallWebServices(ProductionlineAct.this);
//                       ws.GetBarCode("1010001");
//                       try {
//                           Integer i;
//
//                           JSONObject js = new JSONObject(We_Result.Msg);
//                           JSONArray Item_Name = js.getJSONArray("Item_Name");
//                           JSONArray barcode= js.getJSONArray("barcode");
//
//
//
//                           cls_barCode = new GetBarCode();
//                           for (i = 0; i < Item_Name.length(); i++) {
//                               cls_barCode = new GetBarCode();
//
//                               cls_barCode.setBarCode(Item_Name.get(i).toString());
//                               cls_barCode.setItemNote(barcode.get(i).toString());
//
//
//
//                               list.add(cls_barCode);
//                           }
//                           _handler.post(new Runnable() {
//                               public void run() {
//
//                                   adapter = new GetBarCodeAdapter(ProductionlineAct.this, list);
//                                   listView.setAdapter(adapter);
//                               }
//                           });
//
//                       } catch (final Exception e) {
//
//                       }
//
//                   }
//               };
//               thread.start();
//
//           }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Invf_Serials    " +
                    "( no integer primary key autoincrement, Item_No  text null, Barcode text null,Item_Name  text null  , Type_No text null, States text null )");
        } catch (SQLException e) {
            // Log.i("CREATE TABLE   CustLastPrice","Week already Operand");
        }
        try {
            sqlHandler.executeQuery("Alter Table Invf_Serials  Add  COLUMN  Date  text null ");
        } catch (SQLException e) {


        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cls_barCode = list.get(position);
                cls_barCode1 = new GetDoneBarCode();
                cls_barCode1.setBarCode(cls_barCode.getBarCode());
                cls_barCode1.setItemNote(cls_barCode.getItemNote());
                cls_barCode1.setType(cls_barCode.getType());
                cls_barCode1.setItemNo(cls_barCode.getItemNo());
                cls_barCode1.setDate(cls_barCode.getDate());
                list1.add(cls_barCode1);
                adapter1 = new GetDoneBarCodeAdapter(ProductionlineAct.this, list1);
                list.remove(position);
                adapter.notifyDataSetChanged();
                listView1.setAdapter(adapter1);
                int x = adapter.getCount();
                int x1 = adapter1.getCount();
                countdone.setText(String.valueOf(x1));
                countndone.setText(String.valueOf(x));
                String  query = "Update Invf_Serials SET States = '1' where Barcode ='"+ cls_barCode.getBarCode() +"'";
                sqlHandler.executeQuery(query);
            }
        });
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cls_barCode1 = list1.get(position);
                cls_barCode = new GetBarCode();
                cls_barCode.setBarCode(cls_barCode1.getBarCode());
                cls_barCode.setItemNote(cls_barCode1.getItemNote());
                cls_barCode.setType(cls_barCode1.getType());
                cls_barCode.setItemNo(cls_barCode1.getItemNo());
                cls_barCode.setDate(cls_barCode1.getDate());
                list.add(cls_barCode);
                adapter = new GetBarCodeAdapter(ProductionlineAct.this, list);
                list1.remove(position);
                adapter1.notifyDataSetChanged();
                listView.setAdapter(adapter);
                int x1 = adapter1.getCount();
                int x = adapter.getCount();
                countdone.setText(String.valueOf(x1));
                countndone.setText(String.valueOf(x));
                String  query = "Update Invf_Serials SET States ='0' where Barcode ='"+ cls_barCode1.getBarCode() +"'";
                sqlHandler.executeQuery(query);
            }
        });

        int x = adapter.getCount();
        int x1 = adapter1.getCount();
        countdone.setText(String.valueOf(x1));
        countndone.setText(String.valueOf(x));
        String  query = "Update Invf_Serials SET Date ='"+ date1.getText().toString()+"' where States  ='0'";
        sqlHandler.executeQuery(query);

    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        String strDate = mdformat.format(calendar.getTime());

        date1.setText(strDate);
    }

    private void showD() {
        Searsh = serch.getText().toString();

        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(ProductionlineAct.this);
                ws.Get_Item(Searsh);
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray ItemNeme = js.getJSONArray("Item_Name");
                    JSONArray ItemNo = js.getJSONArray("Item_No");


                    for (i = 0; i < ItemNeme.length(); i++) {
                        categories.add(ItemNeme.get(i).toString());
                        categories2.add(ItemNo.get(i).toString());

                    }


                } catch (final Exception e) {

                }

            }
        };
        thread.start();

    }

    private void showBarCode() {
        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(ProductionlineAct.this);
                ws.GetBarCode("1010001");
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray Item_Name = js.getJSONArray("Item_Name");
                    JSONArray barcode = js.getJSONArray("barcode");


                    cls_barCode = new GetBarCode();
                    for (i = 0; i < Item_Name.length(); i++) {
                        cls_barCode = new GetBarCode();

                        cls_barCode.setBarCode(Item_Name.get(i).toString());
                        cls_barCode.setItemNote(barcode.get(i).toString());


                        list.add(cls_barCode);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            adapter = new GetBarCodeAdapter(ProductionlineAct.this, list);
                            listView.setAdapter(adapter);
                        }
                    });

                } catch (final Exception e) {

                }

            }
        };
        thread.start();


    }

    public void s(View v) {
        SearchFrg exampleDialog = new SearchFrg();
        exampleDialog.show(getFragmentManager(), "sqe");
    }

    public void Approval(View v) {
        final String json = new Gson().toJson(list1);

        Date = date1.getText().toString();
        TransNo = TransNo1.getText().toString();
        loadingdialog = ProgressDialog.show(ProductionlineAct.this, "الرجاء الانتظار ...", "الرجاء الانتظار .... العمل جاري على اعتماد الحركات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();


        final Handler _handler = new Handler();
       long s=postData();

    }

    public void update(View v) {




        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(ProductionlineAct.this);
                ws.GetBarCode("");
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Item_No = js.getJSONArray("Item_No");
                    JSONArray js_Item_Name = js.getJSONArray("Item_Name");
                    JSONArray js_Type_No = js.getJSONArray("Type_No");
                    JSONArray js_barCode = js.getJSONArray("barcode");



                    q = "Delete from Invf_Serials";
                    sqlHandler.executeQuery(q);
                    q = "delete from sqlite_sequence where name='Invf_Serials'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_Item_No.length(); i++) {
                        q = "Insert INTO Invf_Serials(States,Item_No,Item_Name,Type_No,Barcode,Date )values('"
                                + "0"
                                + "','"  + js_Item_No.get(i).toString()
                                + "','" + js_Item_Name.get(i).toString()
                                + "','" + js_Type_No.get(i).toString()
                                + "','" + js_barCode.get(i).toString()
                                + "','" + date1.getText().toString()
                                + "')";
                        sqlHandler.executeQuery(q);


                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {

                      Toast.makeText(ProductionlineAct.this,"SS",Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (final Exception e) {

                    _handler.post(new Runnable() {

                        public void run() {
                            Toast.makeText(ProductionlineAct.this,"ff",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
//        Intent intent=new Intent(ProductionlineAct.this,ProductionlineAct.class);
//        startActivity(intent);
//        startActivity(ProductionlineAct.this, ProductionlineAct.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        finish();
   FilterItems();
   FilterItems1();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case CUSTOMIZED_REQUEST_CODE: {
                Toast.makeText(this, "REQUEST_CODE = " + requestCode, Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(0x0000c0de,resultCode, data);

        if(result.getContents() == null) {
            int originalIntent = result.getOrientation();
            if (originalIntent == 0) {

            }

        } else {
            Log.d("MainActivity", "Scanned");

            tv_BarcodeValue.setText(result.getContents());
        }
    }

    public  void hideKeyboard(View v ) {

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
    public void btn_BarcodeReading(View view) {
        tv_SearchValue.setVisibility(View.GONE);


        tv_BarcodeValue.setVisibility(View.VISIBLE);
        tv_BarcodeValue.setRawInputType(InputType.TYPE_NULL);
        tv_BarcodeValue.setFocusable(true);
        tv_BarcodeValue.setText("");
        tv_BarcodeValue.requestFocus();
        hideKeyboard(view);
        new IntentIntegrator(this).initiateScan();
    }
    public void FilterItems( ) {
        String    query = "Select   *   from Invf_Serials  where States = '0' " ;
              //  "  where "+
              //  "  Item_No   like '%" + Filter + "%'  or Item_Name like  '%" + Filter + "%' ";


list=new ArrayList<GetBarCode>();
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_barCode = new GetBarCode();
                    cls_barCode.setItemNo(c1.getString(c1
                            .getColumnIndex("Item_No")));
                    cls_barCode.setItemNote(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    cls_barCode.setBarCode(c1.getString(c1
                            .getColumnIndex("Barcode")));
                    cls_barCode.setType(c1.getString(c1
                            .getColumnIndex("Type_No")));
                    cls_barCode.setDate(c1.getString(c1
                            .getColumnIndex("Date")));


                    list.add(cls_barCode);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        adapter = new GetBarCodeAdapter(ProductionlineAct.this, list);
        listView.setAdapter(adapter);
       //rec_Item_id.setLayoutManager(new GridLayoutManager(this,4));
      //  rec_Item_id.setAdapter(myAdapter);



    }
    public void FilterItems1( ) {
        String    query = "Select   *   from Invf_Serials  where States = '1' " ;
        //  "  where "+
        //  "  Item_No   like '%" + Filter + "%'  or Item_Name like  '%" + Filter + "%' ";



        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_barCode1 = new GetDoneBarCode();
                    cls_barCode1.setItemNo(c1.getString(c1
                            .getColumnIndex("Item_No")));
                    cls_barCode1.setItemNote(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    cls_barCode1.setBarCode(c1.getString(c1
                            .getColumnIndex("Barcode")));
                    cls_barCode1.setType(c1.getString(c1
                            .getColumnIndex("Type_No")));
                    cls_barCode1.setDate(c1.getString(c1
                            .getColumnIndex("Date")));


                    list1.add(cls_barCode1);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        adapter1 = new GetDoneBarCodeAdapter(ProductionlineAct.this, list1);
        listView1.setAdapter(adapter1);
        //rec_Item_id.setLayoutManager(new GridLayoutManager(this,4));
        //  rec_Item_id.setAdapter(myAdapter);



    }
    public void FilterItems3(String Filter ) {
        String    query = "Select   *   from Invf_Serials  where  States='0' and (Barcode   like '%" + Filter + "%'  or Item_Name like  '%" + Filter + "%' )" ;
        //  "  where "+
        //  "  Item_No   like '%" + Filter + "%'  or Item_Name like  '%" + Filter + "%' ";

        ArrayList<GetBarCode> list5;
        list5=new ArrayList<GetBarCode>();

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_barCode = new GetBarCode();
                    cls_barCode.setItemNo(c1.getString(c1
                            .getColumnIndex("Item_No")));
                    cls_barCode.setItemNote(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    cls_barCode.setBarCode(c1.getString(c1
                            .getColumnIndex("Barcode")));
                    cls_barCode.setType(c1.getString(c1
                            .getColumnIndex("Type_No")));
                    cls_barCode.setType(c1.getString(c1
                            .getColumnIndex("Date")));


                    list5.add(cls_barCode);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        adapter = new GetBarCodeAdapter(ProductionlineAct.this, list5);
        listView.setAdapter(adapter);
        //rec_Item_id.setLayoutManager(new GridLayoutManager(this,4));
        //  rec_Item_id.setAdapter(myAdapter);



    }
    private void GetItemByBarcode(String Key){

        String    query = "Select   *   from Invf_Serials  where Barcode = '"+Key+"' " ;





        Cursor c1 = sqlHandler.selectQuery(query);
        Cls_UnitItems cls_unitItems = new Cls_UnitItems();

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                cls_barCode1 = new GetDoneBarCode();
                cls_barCode1.setBarCode(c1.getString(c1.getColumnIndex("Barcode")));
                cls_barCode1.setItemNote(c1.getString(c1.getColumnIndex("Item_Name")));
                cls_barCode1.setItemNo(c1.getString(c1.getColumnIndex("Item_No")));
                cls_barCode1.setType(c1.getString(c1.getColumnIndex("Type_No")));
                cls_barCode1.setDate(c1.getString(c1.getColumnIndex("Date")));
                list1.add(cls_barCode1);
            }
            c1.close();





            adapter1 = new GetDoneBarCodeAdapter(ProductionlineAct.this, list1);


            listView1.setAdapter(adapter1);
            int x = adapter.getCount();
            int x1 = adapter1.getCount();
            countdone.setText(String.valueOf(x1));
            countndone.setText(String.valueOf(x));
              query = "Update Invf_Serials SET States = '1' where Barcode ='"+Key+"'";
            sqlHandler.executeQuery(query);
//            Intent intent=new Intent(ProductionlineAct.this,ProductionlineAct.class);
//            startActivity(intent);
        //    finish();
            finish();
            startActivity(getIntent());
           // Save_List(cls_unitItems.getItem_no(),cls_unitItems.getPrice(),"1",cls_unitItems.getTax(),cls_unitItems.getUnitno(),"0","0",cls_unitItems.getItemName(),cls_unitItems.getUnitDesc(),"0",cls_unitItems.getOperand(),"0");

        }else{
            Toast.makeText(this," غير موجود " +Key,Toast.LENGTH_SHORT).show();
        }
        tv_BarcodeValue.setText("");
    }
    public void btn_Seach(View view) {
        tv_SearchValue.setVisibility(View.VISIBLE);
        tv_BarcodeValue.setVisibility(View.GONE);

        tv_SearchValue.setText("");
        tv_SearchValue.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

    }
    public long postData( ) {
        String json = "[{''}]";
        try {
            if (list1.size() > 0) {
                json = new Gson().toJson(list1);
            } else {
                Result= -1;
            }

        } catch (Exception ex) {

        }


        final String str;
        String    query = "Select   *   from Invf_Serials  where States = '1' " ;
        //  "  where "+
        //  "  Item_No   like '%" + Filter + "%'  or Item_Name like  '%" + Filter + "%' ";



        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("Item_No", c1.getString(c1.getColumnIndex("Item_No")));
                jsonObject.put("Barcode", c1.getString(c1.getColumnIndex("Barcode")));
                jsonObject.put("Item_Name", c1.getString(c1.getColumnIndex("Item_Name")));
                jsonObject.put("Type_No", c1.getString(c1.getColumnIndex("Type_No")));
                jsonObject.put("Date", c1.getString(c1.getColumnIndex("Date")));


            } catch (JSONException ex) {
                Result= -1;
            } catch (Exception ex) {
                Result= -1;

            }
            c1.close();
        }

        if (json.length() < 10) {
            Result= -1;
        }
        str = jsonObject.toString() + json;
        CallWebServices ws = new CallWebServices(ProductionlineAct.this);
        Result= ws.Save_store_pepr(str);
        try {
            if (Result> 0) {
                ContentValues cv = new ContentValues();
                cv.put("Post", Result);
                long i;
                _handler.post(new Runnable() {
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                ProductionlineAct.this).create();
                        alertDialog.setTitle(" اعتماد  الحركات الانتاج");
                        alertDialog.setMessage("تمت عملية اعتماد الحركات الانتاج بنجاح");
                        alertDialog.setIcon(R.drawable.tick);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        loadingdialog.dismiss();
                        alertDialog.show();
                        alertDialog.show();


                    }
                });
              //  i = sqlHandler.Update("Sal_invoice_Hdr", cv, "  ifnull(doctype,'1')='"+DocType.toString()+"' and     OrderNo='" + pno + "'");
                // Get_ManStore();
            } else {

                loadingdialog.dismiss();
                _handler.post(new Runnable() {
                    public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                ProductionlineAct.this).create();
                        alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID + "");
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

            Result= -1;
            loadingdialog.dismiss();
            _handler.post(new Runnable() {
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ProductionlineAct.this).create();
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

        return Result;

    }
}
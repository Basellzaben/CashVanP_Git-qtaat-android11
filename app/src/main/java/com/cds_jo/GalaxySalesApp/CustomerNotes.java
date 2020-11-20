package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.cds_jo.GalaxySalesApp.ManLocation.AutoPostLocation;
import com.cds_jo.GalaxySalesApp.NewHomePage.NewHomePage;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_CustNotes;
import com.cds_jo.GalaxySalesApp.assist.CustNotesAdapter;
import com.cds_jo.GalaxySalesApp.assist.Customer_List;
import com.cds_jo.GalaxySalesApp.assist.Customers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import header.Header_Frag;

public class CustomerNotes extends FragmentActivity {

    Button btn_save, btn_Cancel;
    EditText Ed_Notes;

    public ProgressDialog loadingdialog;
    SqlHandler sqlHandler, sql_Handler;
    String q = "";
    ListView  LstAllNotes;

    String UserID = "-1";
    String CustomerNo = "-1";
    TextView CustNm,acc;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pop_visit_notes1);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");

        LstAllNotes = (ListView)  findViewById(R.id.LstAllNotes);


          CustNm = (TextView) findViewById(R.id.tv_cusnm);
          acc = (TextView) findViewById(R.id.tv_acc);

        Ed_Notes = (EditText) findViewById(R.id.Ed_Notes);

        sqlHandler = new SqlHandler(this);





        FillCustNotes("");
        Fragment frag = new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();

    }
    private void FillCustNotes(String Cust) {
        q = "Select  distinct   manf.name as manName,    CustNo , ManNo , Notes , NotesDate  , Posted   , ifnull(Customers.name,'')  as name   from" +
                "      CustomerNotes  " +
                "     left join Customers on  Customers.no= CustNo   " +
                "     left join manf on  manf.no= ManNo   " +
                "      where CustNo like'" + Cust + "'";
        Cursor c = sqlHandler.selectQuery(q);
        ArrayList<Cls_CustNotes> custNotesLst = new ArrayList<Cls_CustNotes>();
        if (c != null && c.getCount() != 0) {
            if (c.moveToFirst()) {
                do {
                    Cls_CustNotes cls_custNotes = new Cls_CustNotes();

                    cls_custNotes.setManNm(c.getString(c.getColumnIndex("manName")));
                    cls_custNotes.setCustNo(c.getString(c.getColumnIndex("CustNo")));
                    cls_custNotes.setCustName(c.getString(c.getColumnIndex("name")));
                    cls_custNotes.setManNo(c.getString(c.getColumnIndex("ManNo")));
                    cls_custNotes.setNotes(c.getString(c.getColumnIndex("Notes")));
                    cls_custNotes.setNotesDate(c.getString(c.getColumnIndex("NotesDate")));
                    cls_custNotes.setPosted(c.getString(c.getColumnIndex("Posted")));
                    custNotesLst.add(cls_custNotes);
                } while (c.moveToNext());
            }
            c.close();
        }

        CustNotesAdapter Customer_List_adapter = new CustNotesAdapter(
                this, custNotesLst);
        LstAllNotes.setAdapter(Customer_List_adapter);
    }
    public void btn_new(View view) {
        acc.setText("");
        CustNm.setText("");
        Ed_Notes.setText("");
    }
    public void btn_save_po(View view) {
        Do_save_po();
    }
    public void Do_save_po(   ) {

        if (acc.getText().toString().length() == 0) {
            acc.setError("required!");
            acc.requestFocus();
            return;
        }

        if (Ed_Notes.getText().toString().length() == 0) {
            Ed_Notes.setError("required!");
            Ed_Notes.requestFocus();
            return;
        }

        stopService(new Intent(  this, AutoPostLocation.class));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        ContentValues cv = new ContentValues();
        cv.put("CustNo", acc.getText().toString());
        cv.put("ManNo", UserID);
        cv.put("Notes", Ed_Notes.getText().toString());
        cv.put("NotesDate", currentDateandTime);
        cv.put("Posted", -1);

        long i;
        i = sqlHandler.Insert("CustomerNotes", null, cv);
        if (i > 0) {
            Toast.makeText(this, "تم التخزين بنجاح", Toast.LENGTH_SHORT).show();
            FillCustNotes(acc.getText().toString());
            Ed_Notes.setText("");
            ShareNotes();

        } else {
            Toast.makeText(this, "لم تتم عملية التخزين", Toast.LENGTH_SHORT).show();
        }

    }

    private void ShareNotes() {
       String No,CustNo,ManNo,NotesDate,Notes;
        No=CustNo=ManNo=NotesDate=Notes="";
        String query = "  select  No, CustNo , ManNo , NotesDate , Notes from CustomerNotes " +
                "  where ( Posted = -1  )  order by  No desc  Limit 1 ";


        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() > 0) {

            c1.moveToFirst();
            No= c1.getString(c1.getColumnIndex("No")) ;
            CustNo = c1.getString(c1.getColumnIndex("CustNo") );
            ManNo= c1.getString(c1.getColumnIndex("ManNo") );
            NotesDate= c1.getString(c1.getColumnIndex("NotesDate") );
            Notes =c1.getString(c1.getColumnIndex("Notes") );

            c1.close();


        }
        Cls_CustNotes cls_custNotes = new Cls_CustNotes();
        cls_custNotes.setNo(No);
        cls_custNotes.setCustNo(CustNo);
        cls_custNotes.setManNo(ManNo);
        cls_custNotes.setNotes(Notes);
        cls_custNotes.setNotesDate(NotesDate);
        Do_share_Notes(cls_custNotes);
    }

    public void Do_share_Notes(final Cls_CustNotes obj) {
        final String str;
        loadingdialog = ProgressDialog.show(this, "الرجاء الانتظار ...", "العمل جاري على اعتماد الملاحظات", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.dismiss();

        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(CustomerNotes.this);
                long i = ws.SaveCustNotes(obj);
                try {
                    if (i > 0) {
                        String query = " Update  CustomerNotes  set Posted='" + We_Result.ID + "'  where No ='" + obj.getNo() + "'";
                        sqlHandler.executeQuery(query);

                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();
                                FillCustNotes(acc.getText().toString());
                                ShareNotes();
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
                            AlertDialog alertDialog = new AlertDialog.Builder(CustomerNotes.this).create();
                            alertDialog.setTitle("إعتماد الملاحظات");
                            alertDialog.setMessage("عملية اعتماد الملاحظات لم تتم بنجاح، الرجاء المحاولة لاحقا");            // Setting Icon to Dialog

                            alertDialog.setIcon(R.drawable.error_new);
                            alertDialog.setButton("رجـــوع", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                            loadingdialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
    Intent k= new Intent(this, NewHomePage.class);
        startActivity(k);
    }

    public void btn_back(View view) {
        Intent k= new Intent(this, NewHomePage.class);
        startActivity(k);
    }
    public void btn_searchCustomer(View view) {

            Bundle bundle = new Bundle();
            bundle.putString("Scr", "CustNotes");
            FragmentManager Manager = getFragmentManager();
            Select_Customer obj = new Select_Customer();
            obj.setArguments(bundle);
            obj.show(Manager, null);

    }
    public void Set_Cust(String No, String Nm) {

        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
        FillCustNotes( No );
    }

    public void btn_share(View view) {
        ShareNotes();
    }

    public void btn_delete(View view) {

        sqlHandler.executeQuery("DELETE FROM CustomerNotes where CustNo='"+acc.getText().toString()+"'");
        Toast.makeText(this, "تم عملية الحذف", Toast.LENGTH_SHORT).show();
        FillCustNotes(acc.getText().toString());
    }
}




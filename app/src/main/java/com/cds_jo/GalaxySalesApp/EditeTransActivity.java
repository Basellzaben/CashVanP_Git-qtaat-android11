package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class EditeTransActivity extends AppCompatActivity {
EditText OrderNo1 ,OrderNo2,OrderNo3;
    SqlHandler sqlHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_trans);

        sqlHandler = new SqlHandler(EditeTransActivity.this);
        OrderNo1= (EditText)findViewById(R.id.et_OrderNo1);
        OrderNo2= (EditText)findViewById(R.id.et_OrderNo2);
        OrderNo3= (EditText)findViewById(R.id.et_OrderNo3);

        OrderNo1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    OrderNo1.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        OrderNo2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    OrderNo2.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });



        OrderNo3.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    OrderNo3.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });
    }

    public void btn_Del_Pay(View view) {

        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("سند قبض");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        i =  sqlHandler.Delete("RecCheck","DocNo ='"+OrderNo1.getText()+"'");
        i =  sqlHandler.Delete("RecVoucher","DocNo ='"+OrderNo1.getText()+"'");
        if (i>0){
            UpDateMaxOrderNo();

            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية الحذف بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else
        {
            alertDialog.setMessage("عملية الحذف لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }
    private  void  UpDateMaxOrderNo() {
        SqlHandler sqlHandler;
        sqlHandler = new SqlHandler(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String query = "SELECT  ifnull(MAX (DocNo), 0)AS no FROM RecVoucher     where UserID = '"+u.toString()+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
        }
        max=(intToString(Integer.valueOf(max), 7)  );

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("m2",max);
        editor.commit();
    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    public void btn_search_Recv(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "EditeRec");
        FragmentManager Manager = getFragmentManager();
        RecVoucherSearchActivity obj = new RecVoucherSearchActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void Set_Order(String No) {

        OrderNo1.setText(No);

    }

    public void btn_Post_Payment(View view) {

        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("سند قبض");

        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        ContentValues cv = new ContentValues();
        cv.put("Post","-1");
        i =  sqlHandler.Update("RecVoucher",cv,"DocNo ='"+OrderNo1.getText()+"'");
        if (i>0){
            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية الغاء الاعتماد بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else {
            alertDialog.setMessage("عملية الغاء الاعتماد لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }


    public void btn_cancel_doc(View view) {
        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("سند قبض");

        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        ContentValues cv = new ContentValues();
        cv.put("Post","2");
        i =  sqlHandler.Update("RecVoucher",cv,"DocNo ='"+OrderNo1.getText()+"'");
        if (i>0){
            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية الغاء السند  بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else {
            alertDialog.setMessage("عملية الغاء السند لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }


    }

    public void btn_Post_SalInvoic(View view) {
        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("فاتورة مبيعات");


        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        ContentValues cv = new ContentValues();
        cv.put("Post","-1");
        i =  sqlHandler.Update("Sal_invoice_Hdr",cv,"OrderNo ='"+OrderNo2.getText()+"'");
        if (i>0){
            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية الغاء الاعتماد بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else {
            alertDialog.setMessage("عملية الغاء الاعتماد لم تتم بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }

    public void btn_cancel_SalInvo(View view) {

        long i = -1 ;
        String query = "";

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("فاتورة مبيعات");

        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        ContentValues cv = new ContentValues();
        cv.put("Post","2");
        i =  sqlHandler.Update("Sal_invoice_Hdr",cv,"OrderNo ='"+OrderNo2.getText()+"'");
        if (i>0){
            OrderNo1.setText("");
            alertDialog.setMessage("تمت عملية اخفاء الفاتورة بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.show();
        }else {
            alertDialog.setMessage("عملية اخفاء الفاترة لم تتم  بنجاح");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.show();

        }
    }
    public void Set_Order_Sal_Inv(String No) {
        OrderNo2.setText(No);

    }
    public void btn_Search_Orders(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Edite_inv");
        bundle.putString("typ", "0");
        bundle.putString("doctype", "1");
        FragmentManager Manager =  getFragmentManager();
        Sal_Inv_SearchActivity obj = new Sal_Inv_SearchActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void btn_DeleteInvoice(View view) {

        String query ="Delete from  Sal_invoice_Hdr where OrderNo ='"+ OrderNo2.getText().toString()+"'";
        sqlHandler.executeQuery(query);


        query ="Delete from  Sal_invoice_Det where OrderNo ='"+ OrderNo2.getText().toString()+"'";
        sqlHandler.executeQuery(query);



        OrderNo2.setText("");
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();


        alertDialog.setTitle("فاتورة مبيعات");


        alertDialog.setMessage("تمت عملية الحذف بنجاح");


        alertDialog.setIcon(R.drawable.tick);


        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();

    }

    public void btn_Print(View view) {
        /*try{
            Socket socket = new Socket("192.168.1.10",9100);
            PrintWriter  oStream = new PrintWriter(socket.getOutputStream());
            oStream.println("Galaxy");
            oStream.close();
            socket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }
}

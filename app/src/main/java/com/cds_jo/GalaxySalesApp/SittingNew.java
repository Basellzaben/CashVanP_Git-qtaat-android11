package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import port.bluetooth.BluetoothConnectMenu;


public class SittingNew extends FragmentActivity {
    EditText Sal_inv ,Payments,PrepQty,SalesOrder,ReturnQty,CustCash , PostDelay ,TransQtySerial,Visits;
    SqlHandler sqlHandler;
    Button btn_Delete , button25,ButtonConnectBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitting_new);

        btn_Delete = (Button)findViewById(R.id.btn_Delete);
        ButtonConnectBT = (Button)findViewById(R.id.ButtonConnectBT);
        button25 = (Button)findViewById(R.id.button25);
        btn_Delete.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        button25.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        ButtonConnectBT.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Sal_inv = (EditText)findViewById(R.id.et_Sal_inv);
        Payments = (EditText)findViewById(R.id.et_Payments);
        PrepQty = (EditText)findViewById(R.id.et_PrepQty);
        SalesOrder = (EditText)findViewById(R.id.et_SalesOrder);
        ReturnQty = (EditText)findViewById(R.id.et_ReturnQty);
        CustCash = (EditText)findViewById(R.id.et_CustCash);
        PostDelay = (EditText)findViewById(R.id.et_PostDelay);
        TransQtySerial= (EditText)findViewById(R.id.ed_TransSerial);
        Visits= (EditText)findViewById(R.id.et_Visit);
        EditText     AddressBT = (EditText)findViewById(R.id.EditTextAddressBT);


        EditText     IP = (EditText)findViewById(R.id.et_IP);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AddressBT.setText(sharedPreferences.getString("AddressBT", "0"));
        IP.setText(sharedPreferences.getString("ServerIP", "0"));


        Sal_inv.setText(DB.GetValue(SittingNew.this,"OrdersSitting","Sales","1=1"));
        Payments.setText(DB.GetValue(SittingNew.this,"OrdersSitting","Payment","1=1"));
        PrepQty.setText(DB.GetValue(SittingNew.this,"OrdersSitting","PrepareQty","1=1"));
        SalesOrder.setText(DB.GetValue(SittingNew.this,"OrdersSitting","SalesOrder","1=1"));
        ReturnQty.setText(DB.GetValue(SittingNew.this,"OrdersSitting","ReturnQty","1=1"));
        CustCash.setText(DB.GetValue(SittingNew.this,"OrdersSitting","CustCash","1=1"));
        PostDelay.setText(DB.GetValue(SittingNew.this,"OrdersSitting","PostDely","1=1"));
        TransQtySerial.setText(DB.GetValue(SittingNew.this,"OrdersSitting","TransQtySerial","1=1"));
        Visits.setText(DB.GetValue(SittingNew.this,"OrdersSitting","Visits","1=1"));
        sqlHandler = new SqlHandler(this);

    }
    public void btn_Save(View view) {

        EditText     AddressBT = (EditText)findViewById(R.id.EditTextAddressBT);
        EditText     IP = (EditText)findViewById(R.id.et_IP);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor    = sharedPreferences.edit();
        editor.putString("ServerIP", IP.getText().toString().trim());
        editor.putString("AddressBT", AddressBT.getText().toString().trim());

        editor.commit();



        ContentValues cv = new ContentValues();
        cv.put("Sales", Sal_inv.getText().toString());
        cv.put("Payment", Payments.getText().toString());
        cv.put("PrepareQty", PrepQty.getText().toString());
        cv.put("SalesOrder", SalesOrder.getText().toString());
        cv.put("ReturnQty", ReturnQty.getText().toString().replace(",", ""));
        cv.put("CustCash", CustCash.getText().toString().replace(",", ""));
        cv.put("PostDely", PostDelay.getText().toString().replace(",", ""));
        cv.put("TransQtySerial", TransQtySerial.getText().toString().replace(",", ""));
        cv.put("Visits", Visits.getText().toString().replace(",", ""));

       Long  i = sqlHandler.Update("OrdersSitting", cv, "1=1");

        if(i>0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    SittingNew.this).create();
            alertDialog.setTitle("إعدادت عامة");
            alertDialog.setMessage("تمت عملية التعديل بنجاح");
            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
           }
            });
            alertDialog.show();
        }else {
            i = sqlHandler.Insert("OrdersSitting", null, cv);
            if (i > 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        SittingNew.this).create();
                alertDialog.setTitle("إعدادت عامة");
                alertDialog.setMessage("تمت عملية التعديل بنجاح");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                }
                });
                alertDialog.show();
            }
        }
    }

    private void DeleteData(){

        String q;
        SqlHandler sqlHandler = new SqlHandler(SittingNew.this);

        sqlHandler.executeQuery("Delete from RecVoucher");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='RecVoucher'");

        sqlHandler.executeQuery("Delete from RecCheck");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='RecCheck'");

        sqlHandler.executeQuery("Delete from banks");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='banks'");

        sqlHandler.executeQuery("Delete from invf");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='invf'");


        sqlHandler.executeQuery("Delete from UnitItems");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='UnitItems'");

        sqlHandler.executeQuery("Delete from Unites");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Unites'");


        sqlHandler.executeQuery("Delete from curf");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='curf'");

        sqlHandler.executeQuery(" delete from sqlite_sequence where name='deptf'");
        sqlHandler.executeQuery("Delete from deptf");
        //	sqlHandler.executeQuery("Delete from manf");
        sqlHandler.executeQuery("Delete from SaleManRounds");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='SaleManRounds'");

        sqlHandler.executeQuery("Delete from SaleManPath");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='SaleManPath'");


        sqlHandler.executeQuery("Delete from Customers");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Customers'");

         sqlHandler.executeQuery("Delete from Offers_Groups");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Offers_Groups'");

        sqlHandler.executeQuery("Delete from Offers_Dtl_Gifts");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Offers_Dtl_Gifts'");


        sqlHandler.executeQuery("Delete from Offers_Dtl_Cond");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Offers_Dtl_Cond'");

        sqlHandler.executeQuery("Delete from Sal_invoice_Hdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Sal_invoice_Hdr'");


        sqlHandler.executeQuery("Delete from Sal_invoice_Det");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Sal_invoice_Det'");


        sqlHandler.executeQuery("Delete from Po_dtl");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Po_dtl'");


        sqlHandler.executeQuery("Delete from Po_Hdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Po_Hdr'");


        sqlHandler.executeQuery("Delete from CustStoreQtydetl");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='CustStoreQtydetl'");


        sqlHandler.executeQuery("Delete from ReturnQtydetl");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='ReturnQtydetl'");


        sqlHandler.executeQuery("Delete from ReturnQtyhdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='ReturnQtyhdr'");


        sqlHandler.executeQuery("Delete from ManStore");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='ManStore'");



        sqlHandler.executeQuery("Delete from Offers_Hdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Offers_Hdr'");

        sqlHandler.executeQuery("Delete from CustStoreQtyhdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='CustStoreQtyhdr'");

  /*      sqlHandler.executeQuery("Delete from ComanyInfo");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='ComanyInfo'");*/


        sqlHandler.executeQuery("Delete from CustLastPrice");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='CustLastPrice'");


        sqlHandler.executeQuery("Delete from BalanceQty");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='BalanceQty'");


        sqlHandler.executeQuery("Delete from ManPermession");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='ManPermession'");

        sqlHandler.executeQuery("Delete from CustLocation");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='CustLocation'");


        sqlHandler.executeQuery("Delete from Tab_Password");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Tab_Password'");

        sqlHandler.executeQuery("Delete from Tab_Password");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Tab_Password'");


        sqlHandler.executeQuery("Delete from CASHCUST");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='CASHCUST'");


        sqlHandler.executeQuery("Delete from PrepManQtyhdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='PrepManQtyhdr'");


        sqlHandler.executeQuery("Delete from PrepManQtydetl");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='PrepManQtydetl'");

        sqlHandler.executeQuery("UPDATE    OrdersSitting set Payment=0, Sales =0 ,SalesOrder=0,PrepareQty=0,RetSales=0,CustCash=0,ReturnQty=0");
        sqlHandler.executeQuery("Delete from Offers_Hdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='Offers_Hdr'");

        sqlHandler.executeQuery("Delete from ManLocation");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='ManLocation'");

        sqlHandler.executeQuery("Delete from TransferQtydtl");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='TransferQtydtl'");

        sqlHandler.executeQuery("Delete from TransferQtyhdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='TransferQtyhdr'");

        sqlHandler.executeQuery("Delete from stores");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='stores'");

        sqlHandler.executeQuery("Delete from VisitImagesDtl");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='VisitImagesDtl'");

        sqlHandler.executeQuery("Delete from VisitImagesHdr");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='VisitImagesHdr'");

        sqlHandler.executeQuery("Delete from ACC_REPORT");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='ACC_REPORT'");

        sqlHandler.executeQuery("Delete from CusfCardAtt");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='CusfCardAtt'");


        sqlHandler.executeQuery("Delete from CusfCard");
        sqlHandler.executeQuery(" delete from sqlite_sequence where name='CusfCard'");



        EditText     et_Sal_inv = (EditText)findViewById(R.id.et_Sal_inv);
        EditText     et_Payments = (EditText)findViewById(R.id.et_Payments);
        EditText     et_PrepQty = (EditText)findViewById(R.id.et_PrepQty);




        et_Sal_inv.setText("0");
        et_Payments.setText("0");
        et_PrepQty.setText("0");
        SalesOrder.setText("0");
        Visits.setText("0");


/*



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SittingNew.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CompanyID", "0");
        editor.putString("CompanyNm", "مجموعة المجرة الدولية");
        editor.putString("TaxAcc1", "0");
        editor.putString("Address", "عمان");
        editor.putString("Notes","");
        editor.putString("CustNo", "");
        editor.putString("CustNm", "");
        editor.putString("CustAdd", "");
        editor.putString("m1", "0");
        editor.putString("m2", "0");
        editor.putString("m3", "0");
        editor.putString("m4", "0");
        editor.putString("m5", "0");
        editor.putString("m6", "0");

        editor.commit();
*/


        AlertDialog alertDialog = new AlertDialog.Builder(
                SittingNew.this).create();
        alertDialog.setTitle("الإعدادات العامة");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();



    }


    public void DeleteAll(View view) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("إعدادات عامة");
        alertDialog.setMessage("هل انت متاكد من عملية الحذف");
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DeleteData();
            }
        });


        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });


        alertDialog.show();
    }
    private void GetSerials(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "-1");


        if(UserID=="-1") {
            return;
        }

        final Handler _handler = new Handler();

        new   Thread(new Runnable() {
            @Override
            public void run () {
                CallWebServices ws = new CallWebServices(SittingNew.this);
                ws.GetOrdersSerials(UserID);
                try {

                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Sales= js.getJSONArray("Sales");
                    JSONArray js_Payment= js.getJSONArray("Payment");
                    JSONArray js_SalesOrder= js.getJSONArray("SalesOrder");
                    JSONArray js_PrepareQty= js.getJSONArray("PrepareQty");
                    JSONArray js_RetSales= js.getJSONArray("RetSales");
                    JSONArray js_PostDely= js.getJSONArray("PostDely");
                    JSONArray js_ReturnQty= js.getJSONArray("ReturnQty");
                    JSONArray js_CustCash= js.getJSONArray("CustCash");
                    JSONArray js_Visits= js.getJSONArray("Visits");

                    q = "Delete from OrdersSitting";
                    sqlHandler.executeQuery(q);


                    q = " delete from sqlite_sequence where name='OrdersSitting'";
                    sqlHandler.executeQuery(q);



                    q = "INSERT INTO OrdersSitting(Sales, Payment , SalesOrder , PrepareQty , RetSales, PostDely , ReturnQty , CustCash,Visits  ) values ('"
                                    + js_Sales.get(0).toString()
                            + "','" + js_Payment.get(0).toString()
                            + "','" + js_SalesOrder.get(0).toString()
                            + "','" + js_PrepareQty.get(0).toString()
                            + "','" + js_RetSales.get(0).toString()
                            + "','" + js_PostDely.get(0).toString()
                            + "','" + js_ReturnQty.get(0).toString()
                            + "','" + js_CustCash.get(0).toString()
                            + "','" + js_Visits.get(0).toString()
                            + "')";
                    sqlHandler.executeQuery(q);

                    _handler.post(new Runnable() {

                        public void run() {

                            Sal_inv.setText(DB.GetValue(SittingNew.this,"OrdersSitting","Sales","1=1"));
                            Payments.setText(DB.GetValue(SittingNew.this,"OrdersSitting","Payment","1=1"));
                            PrepQty.setText(DB.GetValue(SittingNew.this,"OrdersSitting","PrepareQty","1=1"));
                            SalesOrder.setText(DB.GetValue(SittingNew.this,"OrdersSitting","SalesOrder","1=1"));
                            ReturnQty.setText(DB.GetValue(SittingNew.this,"OrdersSitting","ReturnQty","1=1"));
                            CustCash.setText(DB.GetValue(SittingNew.this,"OrdersSitting","CustCash","1=1"));
                            PostDelay.setText(DB.GetValue(SittingNew.this,"OrdersSitting","PostDely","1=1"));
                            Visits.setText(DB.GetValue(SittingNew.this,"OrdersSitting","Visits","1=1"));


                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    SittingNew.this).create();
                            alertDialog.setTitle("إعدادت عامة");
                            alertDialog.setMessage("تمت عملية  تحديث التسلسلات بنجاح");
                            alertDialog.setIcon(R.drawable.tick);

                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();




                        }
                    });

                } catch (final Exception e) {

                    _handler.post(new Runnable() {
                        public void run() {

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    SittingNew.this).create();
                            alertDialog.setTitle("إعدادت عامة");
                            alertDialog.setMessage("حدث خطأ اثناء عملية الاتصال بالسيرفر الرئيسي ، الرجاء التاكد من اتصال الجهاز بالانترنت");
                            alertDialog.setIcon(R.drawable.delete);

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

    public void btn_GetSerial(View view) {
        GetSerials();

    }
}

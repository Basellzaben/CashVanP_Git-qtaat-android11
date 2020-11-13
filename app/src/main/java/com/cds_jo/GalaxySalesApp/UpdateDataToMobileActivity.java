package com.cds_jo.GalaxySalesApp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.PostTransActions.PostPayments;
import com.cds_jo.GalaxySalesApp.PostTransActions.PostSalesInvoice;
import com.cds_jo.GalaxySalesApp.PostTransActions.PostSalesOrder;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Check;
import com.cds_jo.GalaxySalesApp.assist.Cls_UpdateData;
import com.cds_jo.GalaxySalesApp.assist.dummy.Cls_UpdateData_Adapter;
import com.google.gson.Gson;
import com.jrummyapps.android.shell.CommandResult;
import com.jrummyapps.android.shell.Shell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class UpdateDataToMobileActivity extends AppCompatActivity {

    String str = "";
    private static final int LASTUPDATE = 146 ;
    String FD;
    String TD;
    private Handler progressBarHandler = new Handler();
    SqlHandler sqlHandler;

    boolean a;
    ArrayList<Cls_UpdateData> List_Result;
    ListView Lv_Result;
    String q = "";
    Cls_UpdateData_Adapter listAdapter;
    ArrayList<Cls_Check> ChecklList;
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    String UserID;
    int DB_VERVSION;

    CheckBox chk_PaymentSchudel,chk_cust,chk_LookUp, chk_banks, chk_Items, chk_Unites, Chk_Items_Unites, Chk_Curf, Chk_deptf, Chk_Users, Chk_Drivers, Chk_CustLastTrans;
    CheckBox Chk_TransQty, chk_Pro, chkCompany, chkCashCust, chk_Item_cat, chk_Cust_Cat, chk_Serial, chk_LastPrice, Chk_Msg, Chk_Batch,chk_country;
    CheckBox Chk_Post_Inv, Chk_Post_Payments, chk_po_post, Chk_Code, chk_Stores, chk_Gift, chk_OfferGroups;

    private void filllist(String str, int f, int c) {

        Cls_UpdateData obj = new Cls_UpdateData();
        String msg = "";
        if (f == 1) {
            msg = " تمت عملية تحديث " + str + " بنجاح";
        } else {
            msg = "عملية تحديث " + str + " لم تتم بنجاح";

        }

        obj.setDocType(msg);
        obj.setDocNo(c + "");
        obj.setFlag(f);
        obj.setDocPostFlag("0");
        List_Result.add(obj);

        listAdapter = new Cls_UpdateData_Adapter(UpdateDataToMobileActivity.this, List_Result);
        Lv_Result.setAdapter(listAdapter);

    }

    ArrayList<Cls_Sal_InvItems> contactList;
    ArrayList<ContactListItems> PoList;

    private void DataBaseChanges() {

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add  COLUMN  Operand  text null ");
        } catch (SQLException e) {
            Log.i("ADD COLUMN Operand", "Week already Operand");

        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  CustLastPrice    " +
                    "( no integer primary key autoincrement, Item_No  text null, Cust_No text null,Unit_No  text null  , Price text null )");
        } catch (SQLException e) {
            // Log.i("CREATE TABLE   CustLastPrice","Week already Operand");
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  BalanceQty    " +
                    "( no integer primary key autoincrement,OrderNo text null, Item_No  text null  ,Unit_No  text null  , Qty text null,ActQty text null,Diff text null  ,UserID text null  , posted text null , date text  null)");
        } catch (SQLException e) {

        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ManPermession    " +
                    "( no integer primary key autoincrement,User_ID text null, APP_Code  text null  ,SCR_Code   text null  , Branch_Code text null,SCR_Action text null,Permession text null  )");
        } catch (SQLException e) {
            // Log.i("CREATE TABLE  ManPermession","ManPermession");
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ManExceptions    " +
                    "( no integer primary key autoincrement,CustNo text null, ExptCat  text null  ,Freq   text null   )");
        } catch (SQLException e) {

        }


        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  Permession  text null ");
        } catch (SQLException e) {
            // Log.i("ADD Permession","Week already ComanyInfo");

        }

        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  CompanyMobile  text null ");
        } catch (SQLException e) {
            // Log.i("ADD CompanyMobile","Week already CompanyMobile");

        }

        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  CompanyMobile2  text null ");
        } catch (SQLException e) {


        }

        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  VisitWeekNo  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  SuperVisorMobile  text null ");
        } catch (SQLException e) {
            //Log.i("ADD SuperVisorMobile","Week already SuperVisorMobile");

        }

        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  SalInvoiceUnit  text null ");
        } catch (SQLException e) {
            //Log.i("ADD SalInvoiceUnit","Week already SalInvoiceUnit");

        }


        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  PoUnit  text null ");
        } catch (SQLException e) {
            //Log.i("ADD PoUnit","Week already PoUnit");

        }

        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  AllowSalInvMinus  text null ");
        } catch (SQLException e) {
            //Log.i("ADD AllowSalInvMinus","Week already AllowSalInvMinus");

        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add  COLUMN  DisAmtFromHdr  text null ");
        } catch (SQLException e) {
            //Log.i("ADD AllowSalInvMinus","Week already AllowSalInvMinus");

        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add  COLUMN  DisPerFromHdr  text null ");
        } catch (SQLException e) { }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  TotalWithoutDiscount  text null ");
        } catch (SQLException e) { }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  CustLocation    " +
                    "( no integer primary key autoincrement,CusNo text null, Lat  text null  ,Long  text null ,Address  text null   ,UserID text null , posted text null , date text  null)");


        } catch (SQLException e) {
            Log.i("ADD CustLocation", "Week already CustLocation");
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Tab_Password    " +
                    "( ID integer primary key autoincrement,PassNo text null, PassDesc  text null  ,Password  text null )");


        } catch (SQLException e) {
            Log.i("ADD Tab_Password", "Week already Tab_Password");
        }


        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  CustType  text null");


        } catch (SQLException e) {
            Log.i("ADD CustType", "Week already CustType");
        }


        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  PAMENT_PERIOD_NO  text null");


        } catch (SQLException e) {
            Log.i("ADD PAMENT_PERIOD_NO", "Week already PAMENT_PERIOD_NO");
        }


        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  SalInvoiceUnit  text null ");
        } catch (SQLException e) {
            //Log.i("ADD SalInvoiceUnit","Week already SalInvoiceUnit");

        }

        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  GPSAccurent  text null ");
        } catch (SQLException e) {
            //Log.i("ADD SalInvoiceUnit","Week already SalInvoiceUnit");

        }


        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  CUST_PRV_MONTH  text null ");
        } catch (SQLException e) {
            //Log.i("ADD SalInvoiceUnit","Week already SalInvoiceUnit");

        }


        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  CUST_NET_BAL  text null ");
        } catch (SQLException e) {
            //Log.i("ADD SalInvoiceUnit","Week already SalInvoiceUnit");

        }
        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  Pay_How  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  RndNum    " +
                    "( No integer primary key autoincrement,ID text null," +
                    " Value  text null   )");


        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  NumOfInvPerVisit    text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table RndNum  Add  COLUMN  Flg    text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  NumOfPayPerVisit    text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  UsedCode    " +
                    "( No integer primary key autoincrement,Status text null,Code text null," +
                    "  OrderNo  text null    , CustomerNo  text null , ItemNo text null ,Tr_Date  text null " +
                    ", Tr_Time  text null ,UserNo text null , Posted integer null)");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  DoctorReport    " +
                    "( ID integer primary key autoincrement,VType integer null,No integer null,CustNo text null,LocatNo text null ," +
                    "  Sp1  text null    , SampleType  text null , VNotes text null,SNotes text null ,Tr_Date  text null " +
                    ", Tr_Time  text null ,UserNo text null , Posted integer null)");
        } catch (SQLException e) {
        }
        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  Cust_type  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Area    " +
                    "( ID integer primary key autoincrement,No integer null,Name text null,Ename text null ," +
                    "  City  text null    , Country  text null )");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Doctor    " +
                    "( ID integer primary key autoincrement,Dr_No integer null,Dr_AName text null,Dr_EName text null ," +
                    "  Dr_Tel  text null    , Specialization_No  integer null , Area integer null  )");
        } catch (SQLException e) {
        }

        //  Specialization  No  Aname Ename
        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Specialization    " +
                    "( ID integer primary key autoincrement,No integer null,Aname text null,Ename text null   )");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  OrderNo  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  V_OrderNo  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  V_OrderNo  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  driverno  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table RecVoucher  Add  COLUMN  V_OrderNo  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ReturnQtyhdr  Add  COLUMN  V_OrderNo  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table CASHCUST  Add  COLUMN   V_OrderNo  text null   ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table CASHCUST  Add  COLUMN   Acc  text null   ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table CASHCUST  Add  COLUMN   Person text null   ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table CASHCUST  Add  COLUMN   Celing text null     ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table CASHCUST  Add  COLUMN      State text null  ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table CASHCUST  Add  COLUMN      Posted text null  ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add COLUMN EnbleHdrDiscount  text null  ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add COLUMN AllowDeleteInvoice  text null  ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table CASHCUST  Add  COLUMN   Type  text null   ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  Mobile1  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN Mobile2  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ACC_REPORT    " +
                    "( ID integer primary key autoincrement,Cust_No  text null,Cust_Nm  text null ,FDate text null,TDate text null , TrDate text null ," +
                    "   Tot  text null   , Rate  text null , Cred text null , Dept text null , Bb text null , Des text null , Date text null" +
                    " , Cur_no text null , Doctype text null , Doc_num text null , CheqBal text null  , Ball text null, CusTop text null, NetBall text null" +
                    " , Notes text null )");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Po_dtl  Add  COLUMN Batch  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_dtl  Add  COLUMN ExpDate  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Po_dtl  Add  COLUMN Opraned  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  QtyBatch    " +
                    "( no integer primary key autoincrement, Item_No  text null, expdate text null,batchno  text null  , net text null )");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  CustomersMsg    " +
                    "( no integer primary key autoincrement, Cusno  text null, msg text null )");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  OrderType  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  OverCelling  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Tab_Password  Add  COLUMN  ManNo  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  Notes  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  Sig  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  DeliveryDate  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  OrdersSitting    " +
                    "( ID integer primary key autoincrement,Sales  text null,Payment  text null ,SalesOrder text null,PrepareQty text null , RetSales text null ," +
                    "  PostDely  text null )");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  CustCash  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  ReciveItemToCustomer  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  Visits  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  ReturnQty  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  Time  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Time  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table RecVoucher  Add  COLUMN  Time  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ReturnQtyhdr  Add  COLUMN  Time  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  DayNum  integer null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  DayNum  integer null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table RecVoucher  Add  COLUMN  DayNum  integer null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ReturnQtyhdr  Add  COLUMN  DayNum  integer null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  DelveryNm  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  VisitImagesHdr    " +
                    "( ID integer primary key autoincrement ,OrderNo  text null,CustNo  text null ,Tr_Date text null,Tr_Time text null , " +
                    " UserNo text null , DayNum  integer null, Posted  integer null , V_OrderNo  text null)");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  VisitImagesDtl   " +
                    "( ID integer primary key autoincrement,Desc text null ,OrderNo  text null   ,Tr_Date text null,Tr_Time text null , " +
                    " DayNum  integer null, Posted  integer null , ImgUrl1 text null ,ImgUrl2 text null , ImgUrl3  ,ImgUrl4 text null , V_OrderNo  text null)");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table CustomersMsg  Add  COLUMN  Man_No  integer null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table CustomersMsg  Add  COLUMN  SaleManFlg  integer null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_Hdr  Add  COLUMN  Seq  integer  null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Seq integer    null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table RecVoucher  Add  COLUMN  Seq integer    null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ReturnQtyhdr  Add  COLUMN  Seq integer    null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  TransferQtyhdr    " +
                    "( No integer primary key autoincrement,OrderNo text null," +
                    "  fromstore  text null    , tostore  text null ,Tr_Date  text null " +
                    ", Tr_Time  text null ,UserNo text null , Posted integer null, Kind  text null ,  " +
                    " Notes  text null ,Seq integer  text null , DayNum  integer null)");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  TransferQtydtl    " +
                    "( No integer primary key autoincrement,OrderNo text null , " +
                    "  itemno  text null    , qty  text null ,UnitNo  text null " +
                    ", cost  text null ,UnitRate text null , Wcost  text null ,Kind  text null)");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  stores    " +
                    "( No integer primary key autoincrement,sno text null , " +
                    "  sname  text null    , esname  text null ,ACCOUNT_NO  text null )");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  storesSetting    " +
                    "( No integer primary key autoincrement,ScrKind text null , " +
                    "  FromStore  text null    , ToStore  text null   )");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table ManStore  Add  COLUMN  Kind  text null");
        } catch (SQLException e) {
        }

        sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS  TempStore ( no integer primary key autoincrement,SManNo integer null,date text null ,fromstore text null ,tostore text null , des text null , docno text null , itemno text null  ,qty text null  , UnitNo text null " +
                " , StoreName text null , UnitRate text null  , myear text null , RetailPrice text null ,ser integer null,Kind  text null ) ");


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  storesSetting    " +
                    "( No integer primary key autoincrement,ScrKind text null , " +
                    "  FromStore  text null    , ToStore  text null   )");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Offers_Groups  Add  COLUMN  gro_qty text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Offers_Groups  Add  COLUMN  gro_amt text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN  Allaw_Repet text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN  Offer_priority text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN  gro_no text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Po_dtl  Add  COLUMN  Pro_Type text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  TempGifts    " +
                    "( No integer primary key autoincrement,OrderNo text null ,ItemNo text null , " +
                    "  OfferNo  text null    , Qty  text null ,UnitNo text null   )");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN  total_item text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN  Gift_Items_Count text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table UnitItems  Add  COLUMN  UnitSale text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  TransQtySerial  text null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_dtl  Add  COLUMN Price_From_AB  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN Location  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN Tax_Status  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN LocationNo  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ManLocation    " +
                    " ( no integer primary key autoincrement, ManNo  text null, Tr_date text null,Tr_time  text null  , X text null " +
                    " , Y text null , Loct text null ,V_OrderNo text null ,Posted text null  )");
        } catch (SQLException e) {
            // Log.i("CREATE TABLE   CustLastPrice","Week already Operand");
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN   CelingCode text    null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  PermissionCode    " +
                    " ( no integer primary key autoincrement, ManNo  text null, Tr_date text null,Tr_time  text null  ,CustNo  text null " +
                    " , OrderNo text null , Type text null ,V_OrderNo text null ,Posted text null ,Desc text null,Status  text null    )");
        } catch (SQLException e) {
            // Log.i("CREATE TABLE   CustLastPrice","Week already Operand");
        }


        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add  COLUMN   Pro_Type text    null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_dtl  Add  COLUMN Price_From_AB  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Po_dtl  Add  COLUMN ItemInOffer  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add  COLUMN ItemInOffer  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN sum_Qty_item  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN Gift_Items_Sum_Qty  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ServerDateTime    " +
                    "( no integer primary key autoincrement, ServerDate  text null, ServerTime text null)");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  PriceList    " +
                    "( ID integer primary key autoincrement,Cust_No  text null,Item_No  text null ,Unit_No text null,Unit_Rate text null , Price text null , Dis  text null     )");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table UnitItems  Add  COLUMN Weight  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add  COLUMN   weight text    null");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add  COLUMN   doctype text    null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN   doctype text    null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Driver    " +
                    "( ID integer primary key autoincrement,DriverNo  text null,DriverNm  text null )");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table ComanyInfo  Add  COLUMN  CheckAlowedDay  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  CheckAlowedDay  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  CusLastTrans    " +
                    "( ID integer primary key autoincrement,Cust_No  text null,amt  text null ,doctype text null,V_Type text null , date text null , DayCount  text null   , Paymethod  text null    , DocTypeDesc  text null     )");
        } catch (SQLException e) {
        }


        if (!isFieldExist()) {
/////////////////////////////////////////////////////////////////////////////////////////////////
            Toast.makeText(this, " العمل جاري على تحديث قاعدة البيانات", Toast.LENGTH_SHORT).show();
            try {
                sqlHandler.executeQuery(" DROP TABLE IF EXISTS 'Offers_Dtl_Gifts'");
            } catch (SQLException e) {
            }

            try {
                sqlHandler.executeQuery(" DROP TABLE IF EXISTS 'Offers_Groups'");
            } catch (SQLException e) {
            }

            try {
                sqlHandler.executeQuery(" DROP TABLE IF EXISTS 'Offers_Dtl_Cond'");
            } catch (SQLException e) {
            }

            try {
                sqlHandler.executeQuery(" DROP TABLE IF EXISTS 'Offers_Hdr'");
            } catch (SQLException e) {
            }

        }
///////////////////////////////////////////////////////////////////////////////////////////////////

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Offers_Hdr    " +
                    "( no integer primary key autoincrement" +
                    ",ID  text null" +
                    ",Offer_No  text null " +
                    ",Offer_Name  text null " +
                    ",Offer_Date  text null " +
                    ",Offer_Type  text null " +
                    ",Offer_Status  text null " +
                    ",Offer_Begin_Date  text null " +
                    ",Offer_Exp_Date  text null " +
                    ",Offer_Result_Type  text null " +
                    ",Offer_Dis  text null " +
                    ",Offer_Amt  text null " +
                    ",TotalValue  text null " +
                    ",Allaw_Repet  text null " +
                    ",Offer_priority  text null " +
                    ",gro_no  text null " +
                    ",total_item  text null " +
                    ",Gift_Items_Count  text null " +
                    ",sum_Qty_item  text null " +
                    ",Gift_Items_Sum_Qty  text null " + ")");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Offers_Dtl_Cond    " +
                    "( no integer primary key autoincrement" +
                    ",ID  text null" +
                    ",Trans_ID  text null " +
                    ",Gro_Num  text null " +
                    ",Allaw_Repet  text null " + ")");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Offers_Groups    " +
                    "( no integer primary key autoincrement" +
                    ",ID  text null" +
                    ",grv_no  text null " +
                    ",gro_name  text null " +
                    ",gro_ename  text null " +
                    ",gro_type  text null " +
                    ",item_no  text null " +
                    ",unit_no  text null " +
                    ",unit_rate  text null " +
                    ",qty  text null " +
                    ",SerNo  text null " +
                    ",gro_qty  text null " +
                    ",gro_amt  text null " + ")");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Offers_Dtl_Gifts    " +
                    "( no integer primary key autoincrement" +
                    ",ID  text null" +
                    ",Trans_ID  text null " +
                    ",Item_No  text null " +
                    ",Unit_No  text null " +
                    ",Unit_Rate  text null " +
                    ",QTY  text null " + ")");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table RecVoucher  Add  COLUMN  SalesOrderNo    null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN  Cate_Offer  text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table Offers_Hdr  Add  COLUMN  Offer_Type_Item  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Categ    " +
                    "( no integer primary key autoincrement,catno text null, catName text null)");
        } catch (SQLException e) {
            Log.i("ADD CustLocation", "Week already Categ");
        }

        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  PromotionFlag  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  DB_VERVSION    " +
                    "( ID integer primary key autoincrement,No text null)");


        } catch (SQLException e) {
            Log.i("ADD DataBase_ID", "Week already CustLocation");
        }


        try {
            sqlHandler.executeQuery("Alter Table ManExceptions  Add  COLUMN  ExtraAmt  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ManExceptions  Add  COLUMN  ExtraPrecent  text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add COLUMN  hdr_dis_Type     text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  CusfCard    " +
                    "( no integer primary key autoincrement" +
                    ",OrderNo  text null" +
                    ",Nm  text null " +
                    ",AreaDesc  text null " +
                    ",CustType  text null " +
                    ",Mobile  text null " +
                    ",Lan  text null " +
                    ",Long  text null " +
                    ",GpsLocation  text null " +
                    ",UserID  text null " +
                    ",Posted  text null " +
                    ",Acc  text null " +
                    ",Tr_Date  text null " +
                    ",Tr_Time  text null " +
                    ")");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  CusfCardAtt    " +
                    "( no integer primary key autoincrement" +
                    ",OrderNo  text null" +
                    ",Nm  text null " +
                    ",ImgDesc  text null " +
                    ",img  text null " +
                    ",Posted  text null " +
                    ",ImgID  text null " +
                    ")");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Sal_return_Hdr    " +
                    "( no integer primary key autoincrement,Orderno text null, nm  text null  ,acc   text null ,date   text null ,UserID   text null ,Post   text null ,hdr_dis_per   text null ,hdr_dis_value   text null ,Qtystoreser   text null ,Total   text null ,Net_Total   text null ,Tax_Total   text null ,bounce_Total   text null  ,include_Tax   text null ,disc_Total   text null ,inovice_Type   text null ,v_Orderno   text null ,driverno   text null ,Time   text null ,DayNum   text null ,DelveryNm   text null ,seq   text null ,CellingCode   text null ,docType   text null ,TotalwithoutDiscount    text null ,hdr_dis_type   text null )");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Sal_return_Det    " +
                    "( no integer primary key autoincrement,Orderno text null, ItemNo  text null  ,unitNo   text null ,price   text null ,qty   text null ,tax   text null ,UserID   text null ,Post   text null ,dis_Amt   text null ,dis_per   text null ,OrgPrice   text null ,bounce_qty   text null ,bounce_unitno   text null ,Tax_Amt   text null ,Total   text null ,net_Total   text null ,ProID   text null ,Pro_bounce   text null ,Pro_dis_Per   text null ,Pro_Amt   text null ,Pro_Total   text null ,Operand   text null ,Pro_type   text null ,ItemInOffer   text null ,weight    text null ,doctype   text null ,DisAmtFromHdr   text null ,DisPerFromHdr   text null )");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add COLUMN  Note text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_return_Det  Add COLUMN  Damaged text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_return_Det  Add COLUMN  Note text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Payment_method  " +
                    "( no integer primary key autoincrement,Orderno text null, Date  text null  ,Amt   text null ,Note   text null )");
        } catch (SQLException e) {

        }


        try {
            sqlHandler.executeQuery("Alter Table UsedCode  Add COLUMN  Flag text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add COLUMN  Code text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Det  Add COLUMN  Code_Desc text null ");
        } catch (SQLException e) {
        }
        try {
            sqlHandler.executeQuery("Alter Table Payment_method  Add COLUMN  custNo text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Pay_method " +
                    "( no integer primary key autoincrement,custNo text null,manNo  text null  ,orderNo   text null ,Amt  text null,Tr_date  text null,Notes  text null,Flag  text null,SupervisorNutes  text null,OldDate  text null,InoviceAmt  text null )");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  cardMan " +
                    "( no integer primary key autoincrement,manNo text null,manName  text  NULL  ,Email   text null ,photo  text null,Phone text null,BranchName  text null,SupervisorName  text null )");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  InvoicePaymentSchedule " +
                    "( no integer primary key autoincrement,custName  text  NULL  ,custNo   text null ,New_Amt  text null,orderNo text null,Amt  text null,InoviceAmt  text null ,Notes  text null,SupervisorNutes text null,Tr_date  text null,New_Tr_date  text null )");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  TimeLogin " +
                    "( no integer primary key autoincrement,manNo text null ,DateLogin text null)");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("Alter Table InvoicePaymentSchedule  Add COLUMN  orderDate text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  CHECK_IMG  text null");


        } catch (SQLException e) {
            Log.i("ADD PAMENT_PERIOD_NO", "Week already PAMENT_PERIOD_NO");
        }



    }
    private void DataBaseChanges1() {
        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  ManType  text null ");
        } catch (SQLException e) {
            Log.i("ADD COLUMN Operand", "Week already Operand");

        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Pay_method " +
                    "( no integer primary key autoincrement,custNo text null,manNo  text null  ,orderNo   text null ,Amt  text null,Tr_date  text null,Notes  text null,Flag  text null,SupervisorNutes  text null,OldDate  text null,InoviceAmt  text null )");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  cardMan " +
                    "( no integer primary key autoincrement,manNo text null,manName  text  NULL  ,Email   text null ,photo  text null,Phone text null,BranchName  text null,SupervisorName  text null )");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  InvoicePaymentSchedule " +
                    "( no integer primary key autoincrement,custName  text  NULL  ,custNo   text null ,New_Amt  text null,orderNo text null,Amt  text null,InoviceAmt  text null ,Notes  text null,SupervisorNutes text null,Tr_date  text null,New_Tr_date  text null )");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  TimeLogin " +
                    "( no integer primary key autoincrement,manNo text null ,DateLogin text null)");
        } catch (SQLException e) {

        }
        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  PhotoMan " +
                    "( no integer primary key autoincrement,manNo text null ,Photo text null)");
        } catch (SQLException e) {

        }

        try {
            sqlHandler.executeQuery("Alter Table InvoicePaymentSchedule  Add COLUMN  orderDate text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table UsedCode  Add COLUMN Tr_Desc text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table UsedCode  Add COLUMN NewValue text null ");
        } catch (SQLException e) {
        }
        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  AlternativeMan  text null ");
        } catch (SQLException e) {
            Log.i("ADD COLUMN Operand", "Week already Operand");
        }
        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  TypeDesc  text null ");
        } catch (SQLException e) {
            Log.i("ADD COLUMN Operand", "Week already Operand");

        }
        try {
            sqlHandler.executeQuery("Alter Table Customers  Add  COLUMN  CloseVisitWithoutimg  text null ");
        } catch (SQLException e) {
            Log.i("ADD COLUMN Operand", "Week already Operand");

        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  CodeDefinition " +
                    "( no integer primary key autoincrement,Id text null ,ManId text null " +
                    ",CustomerId text null,ItemId text null,MaterialId text null" +
                    ",Code text null,Note text null,MaxBouns text null,MaxDiscount text null" +
                    ",AllBillMaterial text null,Code_Status text null,tabManNo text null" +
                    ",tabCustNo text null,TabItemNo text null,TabBonce text null,TabDiscount text null" +
                    ",Tr_DateTime text null,InvoiceNo text null )");
        } catch (SQLException e) {

        }


        try {
            sqlHandler.executeQuery("Alter Table UsedCode  Add COLUMN  BouncePercent text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table UsedCode  Add COLUMN  DiscountPercent text null ");
        } catch (SQLException e) {
        }
            // Create
        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Man_Vac" +
                    "( no integer primary key autoincrement,Id text null ,ManId text null " +
                    ",CustId text null,FromDate text null,ToDate text null,Note text null,ProcedureType text null,VacationType text null )");
        } catch (SQLException e) {

        }
        try {
            sqlHandler.executeQuery("Alter Table Man_Vac  Add COLUMN  VacationType_Desc text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Man_Vac  Add COLUMN  ProcedureType_Desc text null ");
        } catch (SQLException e) {
        }
        try {
            sqlHandler.executeQuery("Alter Table Man_Vac  Add COLUMN  VacDays text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add COLUMN  X_Lat text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add COLUMN  Y_Long text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add COLUMN  Loct text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ManLogTrans" +
                    "( no integer primary key autoincrement,ManNo text null ,CustNo text null " +
                    ",ScreenCode text null,ActionNo text null,TransNo text null,Trans_Date text null,TabletId text null,BattryCharge text null,Notes text null )");
        } catch (SQLException e) {}
        try {
            sqlHandler.executeQuery("Alter Table ManLogTrans  Add COLUMN  Posted text null ");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("Alter Table manf  Add COLUMN  BranchName text null ");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table manf  Add COLUMN  SupervisorName text null ");
        } catch (SQLException e) {
        }
 try {
            sqlHandler.executeQuery("Alter Table manf  Add COLUMN  Email text null ");
        } catch (SQLException e) {
        }
        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Country" +
                    "( no integer primary key autoincrement,CountryNo text null , CountryName null " );
        } catch (SQLException e) {}



        try {
            sqlHandler.executeQuery("Alter Table SaleManRounds  Add  COLUMN  IsException  text null");
        } catch (SQLException e) {
        }



        sqlHandler.executeQuery(" CREATE TABLE IF NOT EXISTS CustLocaltions ( no integer primary key autoincrement,CustNo text null , ManNo text null , Lat_X text null ,Lat_Y text null,Locat text null,Post INTEGER null " +
                " , Note Text null , Tr_Date text null , PersonNm  text null , MobileNo text null ,Stutes text null   ) ");








        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  EnterQtyhdr    " +
                    "( No integer primary key autoincrement,OrderNo text null," +
                    "  fromstore  text null    , tostore  text null ,Tr_Date  text null " +
                    ", Tr_Time  text null ,UserNo text null , Posted integer null, Kind  text null ,  " +
                    " Notes  text null ,Seq integer  text null , DayNum  integer null)");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  EnterQtydtl    " +
                    "( No integer primary key autoincrement,OrderNo text null , " +
                    "  itemno  text null    , qty  text null ,UnitNo  text null " +
                    ", cost  text null ,UnitRate text null , Wcost  text null ,Kind  text null)");
        } catch (SQLException e) {
        }
        try {
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  EnterQtySerial  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  Lookup    " +
                    "( No integer primary key autoincrement,ItemNo text null , " +
                    "  ArDesc  text null    , TableNo  text null)");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table OrdersSitting  Add  COLUMN  ItemsRecepitSerial  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ItemsReceipthdr    " +
                    "( no integer primary key autoincrement,orderno text null," +
                    "  date  text null    , posted  text null ,userid  text null " +
                    ", Total  text null ,Net_Total text null , Tax_Total text null, Notes  text null ,  " +
                    "  Time  text null ,Seq integer  text null , purches_serial_no  text null, purches_order_no  text null, purches_year_no  text null," +
                    "  purches_serial_nm  text null,vendor_no  text null,vendor_nm  text null ,hdr_dis_per text null  , hdr_dis_value text null)");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  ItemsReceiptdtl    " +
                    "( No integer primary key autoincrement,orderno text null , " +
                    "  itemno  text null    , qty  text null ,tax  text null " +
                    ", unitNo  text null ,dis_per text null , OrgPrice  text null ,bounce_qty  text null" +
                    ",tax_Amt  text null,total  text null,net_total  text null,Opraned  text null ,Price_From_AB text null)");
        } catch (SQLException e) {
        }


        try {
            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  PurchesOrderTemp    " +
                    "( No integer primary key autoincrement,order_no text null , " +
                    "  odate  text null    , ven  text null ,br_no  text null " +
                    ", tot  text null ,dis text null , item_no  text null ,UnitNo  text null" +
                    ",UnitRate  text null,qty  text null,StoreNo  text null,item_name  text null ,cost text null" +
                    "  , OrderMyear text null ,Order_V_TYPE text null , StoreNm text null ,br_nm text null ,venNm  text null)");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table PurchesOrderTemp  Add  COLUMN  UnitName  text null");
        } catch (SQLException e) {
        }
    try {
            sqlHandler.executeQuery("Alter Table PurchesOrderTemp  Add  COLUMN  Po_Total  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ItemsReceiptdtl  Add  COLUMN  store_no  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table ItemsReceiptdtl  Add  COLUMN  store_nm  text null");
        } catch (SQLException e) {
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////
        //cash, check, Visa , Check_Paid_Amt , Visa_Paid_Amt , Cash_Paid_Amt , Cust_Amt_Paid ,  RemainAm
        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Cash_flg  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Check_flg  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Visa_flg  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Cash_Paid_Amt  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Cust_Amt_Paid  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Remain_Amt  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Check_Paid_Amt  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Check_Paid_Date  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Check_Paid_Bank  text null");
        } catch (SQLException e) {
        }
         try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Check_Paid_Person  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Visa_Paid_Amt  text null");
        } catch (SQLException e) {
        }
        try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Visa_Paid_Expire_Date  text null");
        } catch (SQLException e) {
        }

      try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Visa_Paid_Type  text null");
        } catch (SQLException e) {
        }

      try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Check_Number  text null");
        } catch (SQLException e) {
        }
     try {
            sqlHandler.executeQuery("Alter Table Sal_invoice_Hdr  Add  COLUMN  Pos_System  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  Acc  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  AccName  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  PosAcc  text null");
        } catch (SQLException e) {
        }

        try {
            sqlHandler.executeQuery("Alter Table manf  Add  COLUMN  PosAccName  text null");
        } catch (SQLException e) {
        }



    }

    CheckBox chkall;

    public boolean isFieldExist() {
        boolean isExist = false;
        Cursor res = null;
        try {
            res = sqlHandler.selectQuery("select  * from Offers_Hdr limit 1 ");
            int colIndex = res.getColumnIndex("no");
            if (colIndex != -1) {
                isExist = true;
            }

        } catch (Exception e) {
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception e1) {
            }
        }

        return isExist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List_Result = new ArrayList<Cls_UpdateData>();
        setContentView(R.layout.view_update_data_to_mobile);
        Lv_Result = (ListView) findViewById(R.id.LvResult);
        List_Result.clear();
        Lv_Result.setAdapter(null);
        sqlHandler = new SqlHandler(this);
        isFieldExist();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "-1");


        ChecklList = new ArrayList<Cls_Check>();
        ChecklList.clear();

        LinearLayout mainLayout = (LinearLayout) this.findViewById(R.id.phLayout1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button19 = (Button) findViewById(R.id.button19);

        button2.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        button19.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));


        chkall = (CheckBox) findViewById(R.id.chkall);
        chkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  if (chkall.isChecked()) {
                                                      CheckedAll(1);
                                                  } else {
                                                      CheckedAll(0);
                                                  }
                                              }
                                          }
        );
        chk_PaymentSchudel = (CheckBox) findViewById(R.id.chk_PaymentSchudel);
        chk_cust = (CheckBox) findViewById(R.id.Chk_Custs);
        chk_banks = (CheckBox) findViewById(R.id.Chk_Banks);
        chk_LookUp = (CheckBox) findViewById(R.id.chk_LookUp);
        chk_Items = (CheckBox) findViewById(R.id.Chk_Items);
        chk_Unites = (CheckBox) findViewById(R.id.Chk_Unites);
        Chk_Items_Unites = (CheckBox) findViewById(R.id.Chk_Items_Unites);
        Chk_Curf = (CheckBox) findViewById(R.id.Chk_Curf);
        Chk_deptf = (CheckBox) findViewById(R.id.Chk_deptf);
        Chk_Users = (CheckBox) findViewById(R.id.Chk_Users);
        Chk_Drivers = (CheckBox) findViewById(R.id.Chk_Drivers);
        Chk_CustLastTrans = (CheckBox) findViewById(R.id.Chk_CustLastTrans);
        Chk_TransQty = (CheckBox) findViewById(R.id.Chk_TransQty);
        chk_Pro = (CheckBox) findViewById(R.id.chk_Pro);
        chkCompany = (CheckBox) findViewById(R.id.chkCompany);
        chkCashCust = (CheckBox) findViewById(R.id.chkCashCust);
        chk_Item_cat = (CheckBox) findViewById(R.id.chk_Item_cat);
        chk_Cust_Cat = (CheckBox) findViewById(R.id.chk_Cust_Cat);
        chk_Serial = (CheckBox) findViewById(R.id.chk_Serial);
        chk_LastPrice = (CheckBox) findViewById(R.id.chk_LastPrice);
        Chk_Msg = (CheckBox) findViewById(R.id.Chk_Msg);
        Chk_Batch = (CheckBox) findViewById(R.id.Chk_Batch);
        Chk_Post_Inv = (CheckBox) findViewById(R.id.Chk_Post_Inv);
        Chk_Post_Payments = (CheckBox) findViewById(R.id.Chk_Post_Payments);
        chk_po_post = (CheckBox) findViewById(R.id.chk_po_post);
        Chk_Code = (CheckBox) findViewById(R.id.Chk_Code);
        chk_Stores = (CheckBox) findViewById(R.id.chk_Stores);
        chk_Gift = (CheckBox) findViewById(R.id.chk_Gift);
        chk_OfferGroups = (CheckBox) findViewById(R.id.chk_OfferGroups);
        chk_country  = (CheckBox) findViewById(R.id.chk_country);

        chk_PaymentSchudel.setChecked(true);
        chk_cust.setChecked(true);
        chk_banks.setChecked(true);
        chk_LookUp.setChecked(true);
        chk_Items.setChecked(true);
        chk_Unites.setChecked(true);
        Chk_Items_Unites.setChecked(true);
        Chk_Curf.setChecked(true);
        Chk_deptf.setChecked(true);

        Chk_Users.setChecked(true);
        Chk_Drivers.setChecked(true);
        Chk_CustLastTrans.setChecked(true);
        Chk_TransQty.setChecked(true);
        chk_Pro.setChecked(true);
        chkCompany.setChecked(true);
        chkCashCust.setChecked(true);
        chk_Item_cat.setChecked(true);
        chk_Cust_Cat.setChecked(true);
        chk_Serial.setChecked(true);
        chk_LastPrice.setChecked(false);
        Chk_Post_Inv.setChecked(true);
        Chk_Post_Payments.setChecked(true);
        chk_po_post.setChecked(true);
        Chk_Code.setChecked(true);
        Chk_Msg.setChecked(true);
        Chk_Batch.setChecked(true);
        chk_Stores.setChecked(false);
        chk_Gift.setChecked(true);
        chk_OfferGroups.setChecked(true);
        chk_country.setChecked(true);

        chk_Serial.setEnabled(false);
        chk_Gift.setEnabled(false);
        chk_OfferGroups.setEnabled(false);
        chk_Pro.setEnabled(false);

        chk_PaymentSchudel.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_cust.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_banks.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_LookUp.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_Items.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_Unites.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Items_Unites.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Curf.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Curf.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_deptf.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Users.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Drivers.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_CustLastTrans.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_TransQty.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_Pro.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chkCompany.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chkCashCust.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_Item_cat.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_Cust_Cat.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_Serial.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_LastPrice.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Post_Inv.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Post_Payments.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Code.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Msg.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Chk_Batch.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_Stores.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_po_post.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_Gift.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_OfferGroups.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chk_country.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));

        chkall.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        chkall.setText("  اختيار الكل");
        tv = new TextView(getApplicationContext());
        lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, // Width of TextView
                LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tv.setText("");
        tv.setTextColor(Color.parseColor("#FF808562"));
        tv.setBackgroundColor(Color.parseColor("#dee5ae"));
        greenProgressbar = this.getResources().getDrawable(R.drawable.progrees_states);
        try {


            sqlHandler.executeQuery("CREATE TABLE IF NOT EXISTS  DB_VERVSION    " +
                    "( ID integer primary key autoincrement,No text null)");


            DB_VERVSION = Integer.parseInt(DB.GetValue(UpdateDataToMobileActivity.this, "DB_VERVSION", "No", "5=5"));

            q = "DELETE  FROM  DB_VERVSION  ";
            sqlHandler.executeQuery(q);

            q = "INSERT INTO DB_VERVSION(No) values ('" + LASTUPDATE + "')";
            //   q = "INSERT  DB_VERVSION set No=" + LASTUPDATE + "";
            sqlHandler.executeQuery(q);
            if (DB_VERVSION < LASTUPDATE)
                DataBaseChanges();
                DataBaseChanges1();
        } catch (Exception ex) {
            WriteTxtFile.MakeText("DB_VERVSION < LASTUPDATE", ex.getMessage().toString());
        }
        SetDeviceDateTime();
    }

    private void CheckedAll(int f) {

        final CheckBox chk_cust = (CheckBox) findViewById(R.id.Chk_Custs);


        if (f == 1) {
            chkall.setText("عدم اختيار الكل");
            chk_PaymentSchudel.setChecked(true);
            chk_cust.setChecked(true);
            chk_banks.setChecked(true);
            chk_LookUp.setChecked(true);
            chk_Items.setChecked(true);
            chk_Unites.setChecked(true);
            Chk_Items_Unites.setChecked(true);
            Chk_Curf.setChecked(true);
            Chk_deptf.setChecked(true);
            Chk_deptf.setChecked(true);
            Chk_Users.setChecked(true);
            Chk_Drivers.setChecked(true);
            Chk_CustLastTrans.setChecked(true);
            Chk_TransQty.setChecked(true);
            chk_Pro.setChecked(true);
            chkCompany.setChecked(true);
            chkCashCust.setChecked(true);
            chk_Item_cat.setChecked(true);
            chk_Cust_Cat.setChecked(true);
            chk_Serial.setChecked(true);
            chk_LastPrice.setChecked(false);
            Chk_Post_Inv.setChecked(true);
            Chk_Post_Payments.setChecked(true);
            chk_po_post.setChecked(true);
            Chk_Code.setChecked(true);
            Chk_Msg.setChecked(true);
            Chk_Batch.setChecked(true);
            chk_Stores.setChecked(true);
            chk_Gift.setChecked(true);
            chk_OfferGroups.setChecked(true);
            chk_country.setChecked(true);

        } else {
            chkall.setText("  اختيار الكل");
            chk_PaymentSchudel.setChecked(false);
            chk_cust.setChecked(false);
            chk_banks.setChecked(false);
            chk_LookUp.setChecked(false);
            chk_Items.setChecked(false);
            chk_Unites.setChecked(false);
            Chk_Items_Unites.setChecked(false);
            Chk_Curf.setChecked(false);
            Chk_deptf.setChecked(false);
            Chk_deptf.setChecked(false);
            Chk_Users.setChecked(false);
            Chk_Drivers.setChecked(false);
            Chk_CustLastTrans.setChecked(false);
            Chk_TransQty.setChecked(false);
            // chk_Pro.setChecked(false);
            chkCompany.setChecked(false);
            chkCashCust.setChecked(false);
            chk_Item_cat.setChecked(false);
            chk_Cust_Cat.setChecked(false);
            // chk_Serial.setChecked(false);
            chk_LastPrice.setChecked(false);
            Chk_Post_Inv.setChecked(false);
            Chk_Post_Payments.setChecked(false);
            chk_po_post.setChecked(false);
            Chk_Code.setChecked(false);
            Chk_Msg.setChecked(false);
            Chk_Batch.setChecked(false);
            chk_Stores.setChecked(false);
            chk_country.setChecked(false);
        /*    chk_Gift.setChecked(false);
            chk_OfferGroups.setChecked(false);*/

        }


    }

    private void Get_Tab_Password() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.GetTab_Password();
                try {

                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_PassNo = js.getJSONArray("PassNo");
                    JSONArray js_PassDesc = js.getJSONArray("PassDesc");
                    JSONArray js_Password = js.getJSONArray("Password");
                    JSONArray js_ManNo = js.getJSONArray("ManNo");


                    q = "Delete from Tab_Password";
                    sqlHandler.executeQuery(q);

                    q = " delete from sqlite_sequence where name='Tab_Password'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_PassNo.length(); i++) {

                        q = "INSERT INTO Tab_Password(PassNo, PassDesc , Password,ManNo ) values ('"
                                + js_PassNo.get(i).toString()
                                + "','" + js_PassDesc.get(i).toString()
                                + "','" + js_Password.get(i).toString()
                                + "','" + js_ManNo.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                    }

                } catch (final Exception e) {


                }

            }
        }).start();

    }

    private void GetManPermession() {


        if (UserID == "-1") {
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.GetGetManPermession(UserID);
                try {

                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_User_ID = js.getJSONArray("User_ID");
                    JSONArray js_APP_Code = js.getJSONArray("APP_Code");
                    JSONArray js_SCR_Code = js.getJSONArray("SCR_Code");
                    JSONArray js_Branch_Code = js.getJSONArray("Branch_Code");
                    JSONArray js_SCR_Action = js.getJSONArray("SCR_Action");
                    JSONArray js_Permession = js.getJSONArray("Permession");

                    q = "Delete from ManPermession";
                    sqlHandler.executeQuery(q);


                    q = " delete from sqlite_sequence where name='ManPermession'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_User_ID.length(); i++) {

                        q = "INSERT INTO ManPermession(User_ID, APP_Code , SCR_Code , Branch_Code, SCR_Action, Permession) values ('"
                                + js_User_ID.get(i).toString()
                                + "','" + js_APP_Code.get(i).toString()
                                + "','" + js_SCR_Code.get(i).toString()
                                + "','" + js_Branch_Code.get(i).toString()
                                + "','" + js_SCR_Action.get(i).toString()
                                + "','" + js_Permession.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                    }

                } catch (final Exception e) {


                }

            }
        }).start();

    }

    private void GetManExceptions() {

        if (UserID == "-1") {
            return;
        }

        q = "Delete from ManExceptions";
        sqlHandler.executeQuery(q);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_ManExceptions(UserID);
                try {

                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_CustNo = js.getJSONArray("CustNo");
                    JSONArray js_ExptCat = js.getJSONArray("ExptCat");
                    JSONArray js_Freq = js.getJSONArray("Freq");
                    JSONArray js_ExtraAmt = js.getJSONArray("ExtraAmt");
                    JSONArray js_ExtraPrecent = js.getJSONArray("ExtraPrecent");

                    q = "Delete from ManExceptions";
                    sqlHandler.executeQuery(q);


                    q = " delete from sqlite_sequence where name='ManExceptions'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_CustNo.length(); i++) {

                        q = "INSERT INTO ManExceptions(CustNo, ExptCat  , Freq ,ExtraAmt,ExtraPrecent) values ('"
                                + js_CustNo.get(i).toString()
                                + "','" + js_ExptCat.get(i).toString()
                                + "','" + js_Freq.get(i).toString()
                                + "','" + js_ExtraAmt.get(i).toString()
                                + "','" + js_ExtraPrecent.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                    }

                } catch (final Exception e) {


                }

            }
        }).start();

    }

    private void GetSerials() {


        if (UserID == "-1") {
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.GetOrdersSerials(UserID);
                try {

                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Sales = js.getJSONArray("Sales");
                    JSONArray js_Payment = js.getJSONArray("Payment");
                    JSONArray js_SalesOrder = js.getJSONArray("SalesOrder");
                    JSONArray js_PrepareQty = js.getJSONArray("PrepareQty");
                    JSONArray js_RetSales = js.getJSONArray("RetSales");
                    JSONArray js_PostDely = js.getJSONArray("PostDely");
                    JSONArray js_ReturnQty = js.getJSONArray("ReturnQty");
                    JSONArray js_CustCash = js.getJSONArray("CustCash");
                    JSONArray js_Visits = js.getJSONArray("Visits");

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


                } catch (final Exception e) {


                }

            }
        }).start();

    }


    public void btn_Transfer_Data(View view) {
        List_Result.clear();
        Lv_Result.setAdapter(null);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("CustNo", "");
        //editor.putString("CustNm", "");
        //editor.putString("CustAdd", "");
/*
        editor.putString("CompanyID","1");
        editor.putString("CompanyNm", " مجموعة المجرة الدولية ");
        editor.putString("TaxAcc1", "123456");
        editor.putString("Address","عمان  - الاردن");
        editor.putString("Notes", "");

        editor.commit();
*/

        Get_ServerDateTime();


        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.GetcompanyInfo();
                try {

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID = js.getJSONArray("ID");
                    _handler.post(new Runnable() {
                        public void run() {
                            System.out.println("Yes Internet");
                            Do_Trans_From_Server();
                        }
                    });

                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                            System.out.println("No Internet");
                            AlertDialog alertDialog = new AlertDialog.Builder(UpdateDataToMobileActivity.this).create();
                            alertDialog = new AlertDialog.Builder(
                                    UpdateDataToMobileActivity.this).create();
                            alertDialog.setTitle("تحديث البيانات");
                            alertDialog.setMessage("حدث خطأ اثناء عملية الاتصال بالسيرفر الرئيسي ، الرجاء التاكد من اتصال الجهاز بالانترنت");
                            alertDialog.setIcon(R.drawable.delete);
                            alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                            alertDialog.show();

                        }
                    });
                }
            }
        }).start();


    }

    public void Do_Trans_From_Server() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String query;
        final CheckBox chk_PaymentSchudel = (CheckBox) findViewById(R.id.chk_PaymentSchudel);
        final CheckBox chk_cust = (CheckBox) findViewById(R.id.Chk_Custs);
        final CheckBox chk_banks = (CheckBox) findViewById(R.id.Chk_Banks);
        final CheckBox chk_LookUp = (CheckBox) findViewById(R.id.chk_LookUp);
        final CheckBox chk_Items = (CheckBox) findViewById(R.id.Chk_Items);
        final CheckBox chk_Unites = (CheckBox) findViewById(R.id.Chk_Unites);
        final CheckBox Chk_Items_Unites = (CheckBox) findViewById(R.id.Chk_Items_Unites);
        final CheckBox Chk_Curf = (CheckBox) findViewById(R.id.Chk_Curf);
        final CheckBox Chk_deptf = (CheckBox) findViewById(R.id.Chk_deptf);
        final CheckBox Chk_Users = (CheckBox) findViewById(R.id.Chk_Users);
        final CheckBox Chk_Drivers = (CheckBox) findViewById(R.id.Chk_Drivers);
        final CheckBox Chk_CustLastTrans = (CheckBox) findViewById(R.id.Chk_CustLastTrans);
        final CheckBox Chk_TransQty = (CheckBox) findViewById(R.id.Chk_TransQty);
        final CheckBox chk_Pro = (CheckBox) findViewById(R.id.chk_Pro);
        final CheckBox chkCompany = (CheckBox) findViewById(R.id.chkCompany);
        final CheckBox chkCashCust = (CheckBox) findViewById(R.id.chkCashCust);
        final CheckBox chk_Item_cat = (CheckBox) findViewById(R.id.chk_Item_cat);
        final CheckBox chk_Cust_Cat = (CheckBox) findViewById(R.id.chk_Cust_Cat);
        final CheckBox chk_Serial = (CheckBox) findViewById(R.id.chk_Serial);
        final CheckBox chk_LastPrice = (CheckBox) findViewById(R.id.chk_LastPrice);
        final CheckBox Chk_Code = (CheckBox) findViewById(R.id.Chk_Code);
        final CheckBox Chk_Msg = (CheckBox) findViewById(R.id.Chk_Msg);
        final CheckBox Chk_Batch = (CheckBox) findViewById(R.id.Chk_Batch);
        final CheckBox chk_Stores = (CheckBox) findViewById(R.id.chk_Stores);
        final CheckBox chk_country = (CheckBox) findViewById(R.id.chk_country);



        if (Chk_Code.isChecked()) {
            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog CodeDailoge;
            CodeDailoge = new ProgressDialog(UpdateDataToMobileActivity.this);
            CodeDailoge.setProgressStyle(CodeDailoge.STYLE_HORIZONTAL);
            CodeDailoge.setCanceledOnTouchOutside(false);
            CodeDailoge.setProgress(0);
            CodeDailoge.setMax(100);
            CodeDailoge.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("كود السقوفات");
            CodeDailoge.setCustomTitle(tv);
            CodeDailoge.setProgressDrawable(greenProgressbar);
            CodeDailoge.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                   // ws.GetRndNum();
                    ws.GetCodeDefinition(UserID);
                    try {
                        Integer i;
                        String q;
                        /*JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        JSONArray js_Value = js.getJSONArray("Value");*/

                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Id = js.getJSONArray("Id");
                        JSONArray js_ManId = js.getJSONArray("ManId");
                        JSONArray js_CustomerId = js.getJSONArray("CustomerId");
                        JSONArray js_ItemId = js.getJSONArray("ItemId");
                        JSONArray js_MaterialId = js.getJSONArray("MaterialId");
                        JSONArray js_Code = js.getJSONArray("Code");
                        JSONArray js_Note = js.getJSONArray("Note");
                        JSONArray js_MaxBouns = js.getJSONArray("MaxBouns");
                        JSONArray js_MaxDiscount = js.getJSONArray("MaxDiscount");
                        JSONArray js_AllBillMaterial = js.getJSONArray("AllBillMaterial");
                        JSONArray js_Code_Status = js.getJSONArray("Code_Status");

                        q = "Delete from RndNum";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='RndNum'";
                        sqlHandler.executeQuery(q);

                        q = "Delete from CodeDefinition";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='CodeDefinition'";
                        sqlHandler.executeQuery(q);


                      /*  for (i = 0; i < js_ID.length(); i++) {
                            q = "INSERT INTO RndNum(ID,Value,Flg) values ('"
                                    + js_ID.get(i).toString()
                                    + "','" + js_Value.get(i).toString() + "' , '0' )";

                            sqlHandler.executeQuery(q);
                            CodeDailoge.setMax(js_ID.length());
                            CodeDailoge.incrementProgressBy(1);
                            if (CodeDailoge.getProgress() == CodeDailoge.getMax()) {
                                CodeDailoge.dismiss();
                            }
                        }*/
                        for (i = 0; i < js_Id.length(); i++) {
                            q = "Insert INTO CodeDefinition(Id,ManId,CustomerId,ItemId,MaterialId,Code,Note,MaxBouns,MaxDiscount,AllBillMaterial,Code_Status) values ("
                                    + js_Id.get(i).toString()
                                    + ",'" + js_ManId.get(i).toString()
                                    + "','" + js_CustomerId.get(i).toString()
                                    + "','" + js_ItemId.get(i).toString()
                                    + "','" + js_MaterialId.get(i).toString()
                                    + "','" + js_Code.get(i).toString()
                                    + "','" + js_Note.get(i).toString()
                                    + "','" + js_MaxBouns.get(i).toString()
                                    + "','" + js_MaxDiscount.get(i).toString()
                                    + "','" + js_AllBillMaterial.get(i).toString()
                                    + "','" + js_Code_Status.get(i).toString()
                                    + "' )";
                            sqlHandler.executeQuery(q);
                            CodeDailoge.setMax(js_Id.length());
                            CodeDailoge.incrementProgressBy(1);
                            if (CodeDailoge.getProgress() == CodeDailoge.getMax()) {
                                CodeDailoge.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("كود السقوفات", 1, total);
                                Chk_Code.setChecked(false);
                                CodeDailoge.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        CodeDailoge.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("كود السقوفات", 0, 0);
                                Chk_Code.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (chk_Stores.isChecked()) {

            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("تعريف المستودعات");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Get_Stores();
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_sno = js.getJSONArray("sno");
                        JSONArray js_sname = js.getJSONArray("sname");
                        JSONArray js_esname = js.getJSONArray("esname");
                        JSONArray js_ACCOUNT_NO = js.getJSONArray("ACCOUNT_NO");


                        //  Specialization  No  Aname Ename

                        q = "Delete from stores";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='stores'";
                        sqlHandler.executeQuery(q);
                        for (i = 0; i < js_sno.length(); i++) {
                            q = "INSERT INTO stores(sno, sname ,esname , ACCOUNT_NO ) values ('"
                                    + js_sno.get(i).toString()
                                    + "','" + js_sname.get(i).toString()
                                    + "','" + js_esname.get(i).toString()
                                    + "','" + js_ACCOUNT_NO.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);


                            progressDialog.setMax(js_sno.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {

                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("تعريف المستودعات", 1, total);
                                chk_Stores.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("تعريف المستودعات", 0, 0);
                                chk_Stores.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }
        else if (chk_PaymentSchudel.isChecked()) {
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat mdformatyear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            String stryear = mdformatyear.format(calendar.getTime());

            SimpleDateFormat mdformatmonth = new SimpleDateFormat("MM", Locale.ENGLISH);
            String strmonth = mdformatmonth.format(calendar.getTime());

            SimpleDateFormat mdformatday = new SimpleDateFormat("dd", Locale.ENGLISH);
            String strday = mdformatday.format(calendar.getTime());
            FD=strday+"/"+strmonth+"/"+stryear;
            TD=strday+"/"+strmonth+"/"+stryear;
            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("جدول التحصيلات");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int i;
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GatTableCollections(UserID, FD, TD, "-1");
                    try {

                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray NameCust = js.getJSONArray("NameCust");
                        JSONArray Tr_date = js.getJSONArray("Tr_date");
                        JSONArray new_Tr_date = js.getJSONArray("NewTr_date");
                        JSONArray custNo1 = js.getJSONArray("custNo1");
                        JSONArray orderNo = js.getJSONArray("orderNo");
                        JSONArray Amt = js.getJSONArray("Amt");
                        JSONArray InoviceAmt = js.getJSONArray("InoviceAmt");
                        JSONArray Notes = js.getJSONArray("Notes");
                        JSONArray SupervisorNutes = js.getJSONArray("SupervisorNutes");
                        JSONArray newAmt = js.getJSONArray("InoviceAmt");
                        JSONArray orderDate = js.getJSONArray("Order_date");


                        //  Specialization  No  Aname Ename

                        q = "Delete from InvoicePaymentSchedule";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='InvoicePaymentSchedule'";
                        sqlHandler.executeQuery(q);
                        for ( i = 0; i < NameCust.length(); i++) {
                            q = "INSERT INTO InvoicePaymentSchedule(custName,custNo,New_Amt ,orderNo ,Amt  ,InoviceAmt ,Notes,SupervisorNutes,Tr_date,New_Tr_date,orderDate ) values ('"
                                    +  NameCust.get(i).toString()
                                    + "','" + custNo1.get(i).toString()
                                    + "','" + newAmt.get(i).toString()
                                    + "','" + orderNo.get(i).toString()
                                    + "','" + Amt.get(i).toString()
                                    + "','" + InoviceAmt.get(i).toString()
                                    + "','" + Notes.get(i).toString()
                                    + "','" + SupervisorNutes.get(i).toString()
                                    + "','" + Tr_date.get(i).toString()
                                    + "','" + new_Tr_date.get(i).toString()
                                    + "','" + orderDate.get(i).toString()
                                    + "')";

                            sqlHandler.executeQuery(q);


                            progressDialog.setMax(NameCust.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {

                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("جدول التحصيلات", 1, total);
                                chk_PaymentSchudel.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("جدول التحصيلات", 0, 0);
                                chk_PaymentSchudel.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }

        else if (Chk_Batch.isChecked()) {

            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("تاريخ الصلاحية");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.TrnsferQtyFromMobileBatch("1", "1");
                    try {
                        Integer i;
                        String q;

                        if (We_Result.ID>0) {
                            JSONObject js = new JSONObject(We_Result.Msg);
                            JSONArray js_Item_No = js.getJSONArray("Item_No");
                            JSONArray js_expdate = js.getJSONArray("expdate");
                            JSONArray js_batchno = js.getJSONArray("batchno");
                            JSONArray js_net = js.getJSONArray("net");


                            q = "Delete from QtyBatch";
                            sqlHandler.executeQuery(q);
                            q = " delete from sqlite_sequence where name='QtyBatch'";
                            sqlHandler.executeQuery(q);

                            for (i = 0; i < js_Item_No.length(); i++) {
                                q = "INSERT INTO QtyBatch(Item_No,expdate,batchno,net) values ('"
                                        + js_Item_No.get(i).toString()
                                        + "','" + js_expdate.get(i).toString()
                                        + "','" + js_batchno.get(i).toString()
                                        + "','" + js_net.get(i).toString()
                                        + "')";
                                sqlHandler.executeQuery(q);
                                progressDialog.setMax(js_Item_No.length());
                                progressDialog.incrementProgressBy(1);
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                            final int total = i;
                            _handler.post(new Runnable() {
                                public void run() {
                                    filllist("تاريخ الصلاحية", 1, total);
                                    Chk_Batch.setChecked(false);
                                    progressDialog.dismiss();
                                    Do_Trans_From_Server();
                                }
                            });
                        }else{
                            _handler.post(new Runnable() {
                                public void run() {
                                    filllist("تاريخ الصلاحية", 0, 0);
                                    Chk_Batch.setChecked(false);
                                    progressDialog.dismiss();
                                    Do_Trans_From_Server();
                                }
                            });
                        }
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("تاريخ الصلاحية", 0, 0);
                                Chk_Batch.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }
        else if (chk_country.isChecked()) {

            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("المناطق");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.getCountry();
                    try {
                        Integer i;
                        String q;

                        if (We_Result.ID>0) {
                            JSONObject js = new JSONObject(We_Result.Msg);
                            JSONArray Name = js.getJSONArray("Name");
                            JSONArray No = js.getJSONArray("No");


                            q = "Delete from Country";
                            sqlHandler.executeQuery(q);
                            q = " delete from sqlite_sequence where name='Country'";
                            sqlHandler.executeQuery(q);

                            for (i = 0; i < Name.length(); i++) {
                                q = "INSERT INTO Country(CountryNo,CountryName) values ('"
                                        + Name.get(i).toString()
                                        + "','" + No.get(i).toString()

                                        + "')";
                                sqlHandler.executeQuery(q);
                                progressDialog.setMax(No.length());
                                progressDialog.incrementProgressBy(1);
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                            final int total = i;
                            _handler.post(new Runnable() {
                                public void run() {
                                    filllist("المناطق", 1, total);
                                    chk_country.setChecked(false);
                                    progressDialog.dismiss();
                                    Do_Trans_From_Server();
                                }
                            });
                        }else{
                            _handler.post(new Runnable() {
                                public void run() {
                                    filllist("المناطق", 0, 0);
                                    chk_country.setChecked(false);
                                    progressDialog.dismiss();
                                    Do_Trans_From_Server();
                                }
                            });
                        }
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("المناطق", 0, 0);
                                chk_country.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        }
        else if (Chk_Msg.isChecked()) {

            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("  رسائل  العميل");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);

                    ws.GetCustomersMsg(UserID);
                    try {
                        Integer i;
                        String q;
                        q = "Delete from CustomersMsg";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='CustomersMsg'";
                        sqlHandler.executeQuery(q);


                        if(We_Result.ID>0) {
                            JSONObject js = new JSONObject(We_Result.Msg);
                            JSONArray js_Cusno = js.getJSONArray("Cusno");
                            JSONArray js_msg = js.getJSONArray("msg");
                            JSONArray js_Man_No = js.getJSONArray("Man_No");
                            JSONArray js_SaleManFlg = js.getJSONArray("SaleManFlg");


                            for (i = 0; i < js_Cusno.length(); i++) {
                                q = "INSERT INTO CustomersMsg(Cusno,msg,Man_No,SaleManFlg) values ('"
                                        + js_Cusno.get(i).toString()
                                        + "','" + js_msg.get(i).toString()
                                        + "'," + js_Man_No.get(i).toString() + "," + js_SaleManFlg.get(i).toString() + ")";
                                sqlHandler.executeQuery(q);
                                progressDialog.setMax(js_Cusno.length());
                                progressDialog.incrementProgressBy(1);
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                            final int total = i;
                            _handler.post(new Runnable() {
                                public void run() {
                                    filllist("رسائل العميل", 1, total);
                                    Chk_Msg.setChecked(false);
                                    progressDialog.dismiss();
                                    Do_Trans_From_Server();
                                }
                            });
                        }else{

                            _handler.post(new Runnable() {
                                public void run() {
                                    progressDialog.dismiss();
                                    filllist("رسائل العميل", 0, 0);
                                    Chk_Msg.setChecked(false);
                                    Do_Trans_From_Server();
                                }
                            });
                        }
                    } catch (final Exception e) {

                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist("رسائل العميل", 0, 0);
                                Chk_Msg.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        } else if (chk_banks.isChecked()) {

            final Handler _handler = new Handler();
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("  البنـــوك");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetAccNoBanks();
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Bank = js.getJSONArray("Bank");
                        JSONArray js_BEName = js.getJSONArray("BEName");
                        JSONArray js_bank_num = js.getJSONArray("bank_num");
                        JSONArray js_Accno = js.getJSONArray("Accno");
                        JSONArray js_CCntrNo = js.getJSONArray("CCntrNo");

                        q = "Delete from banks";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='banks'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_Bank.length(); i++) {
                            q = "INSERT INTO banks(Bank,BEName,bank_num,Accno,CCntrNo) values ('"
                                    + js_Bank.get(i).toString()
                                    + "','" + js_BEName.get(i).toString()
                                    + "','" + js_bank_num.get(i).toString()
                                    + "','" + js_Accno.get(i).toString()
                                    + "','" + js_CCntrNo.get(i).toString() + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_Bank.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("البنوك", 1, total);
                                chk_banks.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("البنوك", 0, 0);
                                chk_banks.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        } else if (chk_cust.isChecked()) {
            final Handler _handler = new Handler();


            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setMessage(" الرحاء الانتظار ... " + "العمل جاري على نسخ البيانات");
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            tv.setText(" العمــلاء");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetCustomers(UserID);
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_no = js.getJSONArray("no");
                        JSONArray js_name = js.getJSONArray("name");
                        JSONArray js_Ename = js.getJSONArray("Ename");
                        JSONArray js_barCode = js.getJSONArray("barCode");
                        JSONArray js_Address = js.getJSONArray("Address");
                        JSONArray js_State = js.getJSONArray("State");
                        JSONArray js_SMan = js.getJSONArray("SMan");
                        JSONArray js_Latitude = js.getJSONArray("Latitude");
                        JSONArray js_Longitude = js.getJSONArray("Longitude");
                        JSONArray js_Note2 = js.getJSONArray("Note2");
                        JSONArray js_sat = js.getJSONArray("sat");

                        JSONArray js_sun = js.getJSONArray("sun");
                        JSONArray js_mon = js.getJSONArray("mon");
                        JSONArray js_tues = js.getJSONArray("tues");
                        JSONArray js_wens = js.getJSONArray("wens");
                        JSONArray js_thurs = js.getJSONArray("thurs");
                        JSONArray js_sat1 = js.getJSONArray("sat1");
                        JSONArray js_sun1 = js.getJSONArray("sun1");
                        JSONArray js_mon1 = js.getJSONArray("mon1");
                        JSONArray js_tues1 = js.getJSONArray("tues1");
                        JSONArray js_wens1 = js.getJSONArray("wens1");
                        JSONArray js_thurs1 = js.getJSONArray("thurs1");
                        JSONArray js_Celing = js.getJSONArray("Celing");
                        JSONArray js_CatNo = js.getJSONArray("CatNo");
                        JSONArray js_CustType = js.getJSONArray("CustType");
                        JSONArray js_PAMENT_PERIOD_NO = js.getJSONArray("PAMENT_PERIOD_NO");
                        JSONArray js_CUST_PRV_MONTH = js.getJSONArray("CUST_PRV_MONTH");
                        JSONArray js_CUST_NET_BAL = js.getJSONArray("CUST_NET_BAL");
                        JSONArray js_Pay_How = js.getJSONArray("Pay_How");
                        JSONArray js_Cust_type = js.getJSONArray("Cust_type");


                        JSONArray js_TaxStatus = js.getJSONArray("TaxStatus");
                        JSONArray js_country_Nm = js.getJSONArray("country_Nm");
                        JSONArray js_country_No = js.getJSONArray("country_No");
                        JSONArray js_CheckAlowedDay = js.getJSONArray("CheckAlowedDay");
                        JSONArray js_PromotionFlag = js.getJSONArray("PromotionFlag");
                        JSONArray js_CloseVisitWithoutimg = js.getJSONArray("CloseVisitWithoutimg");


                        q = "Delete from Customers";
                        sqlHandler.executeQuery(q);
                        q = "delete from sqlite_sequence where name='Customers'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_no.length(); i++) {
                            q = "Insert INTO Customers(no,name,Ename,barCode,Address,State,SMan,Latitude,Longitude,Note2,sat " +
                                    " ,sun,mon,tues,wens,thurs,sat1,sun1,mon1,tues1,wens1,thurs1 , Celing , CatNo " +
                                    ",CustType,PAMENT_PERIOD_NO , CUST_PRV_MONTH,CUST_NET_BAL,Pay_How,Cust_type,LocationNo,Tax_Status,Location,CheckAlowedDay,PromotionFlag,CloseVisitWithoutimg) values ('"
                                    + js_no.get(i).toString()
                                    + "','" + js_name.get(i).toString()
                                    + "','" + js_Ename.get(i).toString()
                                    + "','" + js_barCode.get(i).toString()
                                    + "','" + js_Address.get(i).toString()
                                    + "','" + js_State.get(i).toString()
                                    + "','" + js_SMan.get(i).toString()
                                    + "','" + js_Latitude.get(i).toString()
                                    + "','" + js_Longitude.get(i).toString()
                                    + "','" + js_Note2.get(i).toString()
                                    + "'," + js_sat.get(i).toString()
                                    + "," + js_sun.get(i).toString()
                                    + "," + js_mon.get(i).toString()
                                    + "," + js_tues.get(i).toString()
                                    + "," + js_wens.get(i).toString()
                                    + "," + js_thurs.get(i).toString()
                                    + "," + js_sat1.get(i).toString()
                                    + "," + js_sun1.get(i).toString()
                                    + "," + js_mon1.get(i).toString()
                                    + "," + js_tues1.get(i).toString()
                                    + "," + js_wens1.get(i).toString()
                                    + "," + js_thurs1.get(i).toString()
                                    + ",'" + js_Celing.get(i).toString()
                                    + "','" + js_CatNo.get(i).toString()
                                    + "','" + js_CustType.get(i).toString()
                                    + "','" + js_PAMENT_PERIOD_NO.get(i).toString()
                                    + "','" + js_CUST_PRV_MONTH.get(i).toString()
                                    + "','" + js_CUST_NET_BAL.get(i).toString()
                                    + "','" + js_Pay_How.get(i).toString()
                                    + "','" + js_Cust_type.get(i).toString()
                                    + "','" + js_country_No.get(i).toString()
                                    + "','" + js_TaxStatus.get(i).toString()
                                    + "','" + js_country_Nm.get(i).toString()
                                    + "','" + js_CheckAlowedDay.get(i).toString()
                                    + "','" + js_PromotionFlag.get(i).toString()
                                    + "','" + js_CloseVisitWithoutimg.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);

                            custDialog.setMax(js_no.length());
                            custDialog.incrementProgressBy(1);

                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {

                                filllist("العملاء", 1, total);
                                chk_cust.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("العملاء", 0, 0);
                                chk_cust.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (chk_Items.isChecked()) {

            tv = new TextView(getApplicationContext());
            tv.setText("المــــــــواد");
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();

            final Handler _handler = new Handler();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetItems(UserID);
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray Item_No = js.getJSONArray("Item_No");
                        JSONArray Item_Name = js.getJSONArray("Item_Name");
                        JSONArray Ename = js.getJSONArray("Ename");
                        JSONArray js_Ename = js.getJSONArray("Ename");
                        JSONArray Unit = js.getJSONArray("Unit");
                        JSONArray Price = js.getJSONArray("Price");
                        JSONArray OL = js.getJSONArray("OL");
                        JSONArray OQ1 = js.getJSONArray("OQ1");
                        JSONArray Type_No = js.getJSONArray("Type_No");
                        JSONArray Pack = js.getJSONArray("Pack");
                        JSONArray Place = js.getJSONArray("Place");
                        JSONArray dno = js.getJSONArray("dno");
                        JSONArray tax = js.getJSONArray("tax");

                        q = "Delete from invf";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='invf'";
                        sqlHandler.executeQuery(q);


                        for (i = 0; i < Item_No.length(); i++) {
                            q = "Insert INTO invf(Item_No,Item_Name,Ename,Unit,Price,OL,OQ1,Type_No,Pack,Place,dno,tax) values ('"
                                    + Item_No.get(i).toString()
                                    + "','" + Item_Name.get(i).toString()
                                    + "','" + Ename.get(i).toString()
                                    + "','" + Unit.get(i).toString()
                                    + "','" + Price.get(i).toString()
                                    + "','" + OL.get(i).toString()
                                    + "','" + OQ1.get(i).toString()
                                    + "','" + Type_No.get(i).toString()
                                    + "','" + Pack.get(i).toString()
                                    + "','" + Place.get(i).toString()
                                    + "','" + dno.get(i).toString()
                                    + "','" + tax.get(i).toString()
                                    + "')";
                            try {
                                sqlHandler.executeQuery(q);
                            } catch (final Exception ex) {
                                _handler.post(new Runnable() {
                                    public void run() {
                                        filllist(ex.toString(), 0, 0);
                                        chk_Items.setChecked(false);
                                        Do_Trans_From_Server();

                                    }
                                });
                            }

                            progressDialog.setMax(Item_No.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                chk_Items.setChecked(false);
                                filllist("المواد", 1, total);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("المواد", 0, 0);
                                chk_Items.setChecked(false);
                                Do_Trans_From_Server();

                            }
                        });
                    }
                }
            }).start();


    } else if (chk_LookUp.isChecked()) {

        final Handler _handler = new Handler();
        tv = new TextView(getApplicationContext());
        lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("تعريفات النظام");
        progressDialog.setCustomTitle(tv);
        progressDialog.setProgressDrawable(greenProgressbar);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.GetLookUp(UserID);
                try {
                    Integer i;
                    String q;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ItemNo = js.getJSONArray("ItemNo");
                    JSONArray js_ArDesc = js.getJSONArray("ArDesc");
                    JSONArray js_TableNo = js.getJSONArray("TableNo");


                    q = "Delete from Lookup";
                    sqlHandler.executeQuery(q);
                    q = " delete from sqlite_sequence where name='Lookup'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_ItemNo.length(); i++) {
                        q = "INSERT INTO Lookup(ItemNo, ArDesc , TableNo  ) values ('"
                                + js_ItemNo.get(i).toString()
                                + "','" + js_ArDesc.get(i).toString()
                                + "','" + js_TableNo.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                        progressDialog.setMax(js_ItemNo.length());
                        progressDialog.incrementProgressBy(1);
                        if (progressDialog.getProgress() == progressDialog.getMax()) {
                            progressDialog.dismiss();
                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {
                        public void run() {
                            filllist("تعريفات النظام", 1, total);
                            chk_LookUp.setChecked(false);
                            progressDialog.dismiss();
                            Do_Trans_From_Server();
                        }
                    });
                } catch (final Exception e) {
                    progressDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            filllist("تعريفات النظام", 0, 0);
                            chk_LookUp.setChecked(false);
                            Do_Trans_From_Server();
                        }
                    });
                }
            }
        }).start();

    }else if (chk_Unites.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            final ProgressDialog progressDialog;
            final Handler _handler = new Handler();
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setMessage(" الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات");
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            tv.setText(" الوحـــــدات");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetUnites();
                    try {
                        Integer i;
                        Integer count = 0;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray Unitno = js.getJSONArray("Unitno");
                        JSONArray UnitName = js.getJSONArray("UnitName");
                        JSONArray UnitEname = js.getJSONArray("UnitEname");

                        q = "Delete from Unites";
                        sqlHandler.executeQuery(q);

                        q = "delete from sqlite_sequence where name='Unites'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < Unitno.length(); i++) {
                            q = "Insert INTO Unites(Unitno,UnitName,UnitEname) values ('"
                                    + Unitno.get(i).toString()
                                    + "','" + UnitName.get(i).toString()
                                    + "','" + UnitEname.get(i).toString()
                                    + "')";
                            try {
                                sqlHandler.executeQuery(q);

                                System.out.println("Unites ");

                            } catch (SQLException ex) {

                                System.out.println("Unites " + ex.getMessage());
                            }
                            progressDialog.setMax(Unitno.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }

                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                String q = "DELETE  FROM Unites WHERE no NOT IN (SELECT MAX(no) FROM Unites GROUP BY Unitno )";
                                sqlHandler.executeQuery(q);
                                filllist("الوحدات", 1, total);
                                chk_Unites.setChecked(false);
                                Do_Trans_From_Server();

                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("الوحدات", 0, 0);
                                chk_Unites.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (Chk_Items_Unites.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            final Handler _handler = new Handler();
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setMessage(" الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات");
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            tv.setText(" وحــــدات بيـــع المــواد");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetUnitItems();
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray item_no = js.getJSONArray("item_no");
                        JSONArray barcode = js.getJSONArray("barcode");
                        JSONArray unitno = js.getJSONArray("unitno");
                        JSONArray Operand = js.getJSONArray("Operand");
                        JSONArray price = js.getJSONArray("price");
                        JSONArray Max = js.getJSONArray("Max");
                        JSONArray Min = js.getJSONArray("Min");
                        JSONArray posprice = js.getJSONArray("posprice");
                        JSONArray UnitSale = js.getJSONArray("UnitSale");
                        JSONArray Weight = js.getJSONArray("Weight");

                        q = "Delete from UnitItems";
                        sqlHandler.executeQuery(q);

                        q = "delete from sqlite_sequence where name='UnitItems'";
                        sqlHandler.executeQuery(q);
                        for (i = 0; i < item_no.length(); i++) {
                            q = "Insert INTO UnitItems(item_no,barcode,unitno,Operand,price,Max,Min,posprice,UnitSale,Weight) values ('"
                                    + item_no.get(i).toString()
                                    + "','" + barcode.get(i).toString()
                                    + "','" + unitno.get(i).toString()
                                    + "','" + Operand.get(i).toString()
                                    + "','" + price.get(i).toString()
                                    + "','" + Max.get(i).toString()
                                    + "','" + Min.get(i).toString()
                                    + "','" + posprice.get(i).toString()
                                    + "','" + UnitSale.get(i).toString()
                                    + "','" + Weight.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(item_no.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist("معاملات النحويل للمواد", 1, total);
                                Chk_Items_Unites.setChecked(false);
                                Do_Trans_From_Server();

                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist("معاملات النحويل للمواد", 0, 0);
                                Chk_Items_Unites.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (Chk_Curf.isChecked()) {


            final ProgressDialog progressDialog;
            final Handler _handler = new Handler();
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText(" العمــــلات");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);

            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Getcurf();
                    try {

                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray cur_no = js.getJSONArray("cur_no");
                        JSONArray cur = js.getJSONArray("cur");
                        JSONArray ename = js.getJSONArray("ename");
                        JSONArray dec = js.getJSONArray("dec");
                        JSONArray lrate = js.getJSONArray("lrate");


                        q = "Delete from curf";
                        sqlHandler.executeQuery(q);

                        q = "delete from sqlite_sequence where name='curf' ";
                        sqlHandler.executeQuery(q);


                        for (i = 0; i < cur_no.length(); i++) {
                            q = "Insert INTO curf(cur_no,cur,ename,dec,lrate) values ('"
                                    + cur_no.get(i).toString()
                                    + "','" + cur.get(i).toString()
                                    + "','" + ename.get(i).toString()
                                    + "','" + dec.get(i).toString()
                                    + "','" + lrate.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(cur_no.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist(" العملات ", 1, total);
                                Chk_Curf.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist(" العملات ", 0, 0);
                                Chk_Curf.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (Chk_deptf.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final ProgressDialog progressDialog;
            final Handler _handler = new Handler();
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("اصناف الـــمواد");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);

            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.deptf(UserID);
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray Type_No = js.getJSONArray("Type_No");
                        JSONArray Type_Name = js.getJSONArray("Type_Name");
                        JSONArray etname = js.getJSONArray("etname");
                        JSONArray route = js.getJSONArray("route");

                        q = "Delete from deptf";
                        sqlHandler.executeQuery(q);

                        q = " delete from sqlite_sequence where name='deptf'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < Type_No.length(); i++) {
                            q = "Insert INTO deptf(Type_No,Type_Name,etname,route) values ('"
                                    + Type_No.get(i).toString()
                                    + "','" + Type_Name.get(i).toString()
                                    + "','" + etname.get(i).toString()
                                    + "','" + route.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(Type_No.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist(" اصناف المواد ", 1, total);
                                Chk_deptf.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist(" اصناف المواد ", 0, 0);
                                Chk_deptf.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (Chk_Users.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            final Handler _handler = new Handler();
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("المستـــــــخدمين");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();
            GetManPermession();
            GetManExceptions();
            Get_Tab_Password();

            //GetSerials();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetManf();
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_man = js.getJSONArray("man");
                        JSONArray js_name = js.getJSONArray("name");
                        JSONArray js_MEName = js.getJSONArray("MEName");
                        JSONArray js_StoreNo = js.getJSONArray("StoreNo");
                        JSONArray js_Stoped = js.getJSONArray("Stoped");
                        JSONArray js_SupNo = js.getJSONArray("SupNo");
                        JSONArray js_UserName = js.getJSONArray("UserName");
                        JSONArray js_Password = js.getJSONArray("Password");
                        JSONArray js_Mobile1 = js.getJSONArray("MobileNo");
                        JSONArray js_Mobile2 = js.getJSONArray("MobileNo2");
                        JSONArray js_MANTYPE = js.getJSONArray("MANTYPE");
                        JSONArray js_TypeDesc= js.getJSONArray("TypeDesc");

                        JSONArray js_ManSupervisor= js.getJSONArray("ManSupervisor");
                        JSONArray js_BranchNo= js.getJSONArray("BranchNo");
                        JSONArray js_Email= js.getJSONArray("Email");
                        JSONArray js_SuperVisor_name= js.getJSONArray("SuperVisor_name");
                        JSONArray js_BranchArName= js.getJSONArray("BranchArName");
                        JSONArray js_Acc= js.getJSONArray("Acc");
                        JSONArray js_AccName= js.getJSONArray("AccName");
                        JSONArray js_PosAcc= js.getJSONArray("PosAcc");
                        JSONArray js_PosAccName= js.getJSONArray("PosAccName");


                        JSONArray js_AlternativeMan = js.getJSONArray("AlternativeMan");

                        q = "Delete from manf";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='manf'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_man.length(); i++) {
                            q = "Insert INTO manf(man,name,MEName,username,password,StoreNo,Stoped,SupNo,Mobile1,Mobile2" +
                                    " , ManType,TypeDesc,AlternativeMan,BranchName,SupervisorName,Email,Acc,AccName,PosAcc,PosAccName) values ("
                                    + js_man.get(i).toString()
                                    + ",'" + js_name.get(i).toString()
                                    + "','" + js_MEName.get(i).toString()
                                    + "','" + js_UserName.get(i).toString()
                                    + "','" + js_Password.get(i).toString()
                                    + "','" + js_StoreNo.get(i).toString()
                                    + "'," + js_Stoped.get(i).toString()
                                    + "," + js_SupNo.get(i).toString()
                                    + ",'" + js_Mobile1.get(i).toString()
                                    + "','" + js_Mobile2.get(i).toString()
                                    + "','" + js_MANTYPE.get(i).toString()
                                    + "','" + js_TypeDesc.get(i).toString()
                                    + "','" + js_AlternativeMan.get(i).toString()
                                    + "','" + js_BranchArName.get(i).toString()
                                    + "','" + js_SuperVisor_name.get(i).toString()
                                    + "','" + js_Email.get(i).toString()
                                    + "','" + js_Acc.get(i).toString()
                                    + "','" + js_AccName.get(i).toString()
                                    + "','" + js_PosAcc.get(i).toString()
                                    + "','" + js_PosAccName.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_man.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {

                                filllist("المستخدمين ", 1, total);
                                Chk_Users.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist("المستخدمين ", 0, 0);
                                progressDialog.dismiss();
                                Chk_Users.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        } else if (Chk_CustLastTrans.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            final Handler _handler = new Handler();
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("احدث حركات العميل");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetCusLastTrans("1", "1");
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Cust_No = js.getJSONArray("Cust_No");
                        JSONArray js_amt = js.getJSONArray("amt");
                        JSONArray js_doctype = js.getJSONArray("doctype");
                        JSONArray js_V_Type = js.getJSONArray("V_Type");
                        JSONArray js_date = js.getJSONArray("date");
                        JSONArray js_DayCount = js.getJSONArray("DayCount");
                        JSONArray js_Paymethod = js.getJSONArray("Paymethod");
                        JSONArray js_DocTypeDesc = js.getJSONArray("DocTypeDesc");

                        q = "Delete from CusLastTrans";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='CusLastTrans'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_Cust_No.length(); i++) {
                            q = "Insert INTO CusLastTrans( Cust_No,amt,doctype,V_Type,date,DayCount,Paymethod,DocTypeDesc) values ('"
                                    + js_Cust_No.get(i).toString()
                                    + "','" + js_amt.get(i).toString()
                                    + "','" + js_doctype.get(i).toString()
                                    + "','" + js_V_Type.get(i).toString()
                                    + "','" + js_date.get(i).toString()
                                    + "','" + js_DayCount.get(i).toString()
                                    + "','" + js_Paymethod.get(i).toString()
                                    + "','" + js_DocTypeDesc.get(i).toString()

                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_Cust_No.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {

                                filllist("حركات العميل", 1, total);
                                Chk_CustLastTrans.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist("حركات العميل ", 0, 0);
                                progressDialog.dismiss();
                                Chk_CustLastTrans.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (Chk_Drivers.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            final Handler _handler = new Handler();
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("السائقين");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            progressDialog.show();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetDrivers();
                    try {
                        Integer i;
                        String q;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_DriverNo = js.getJSONArray("DriverNo");
                        JSONArray js_DriverNm = js.getJSONArray("DriverNm");

                        q = "Delete from Driver";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='Driver'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_DriverNo.length(); i++) {
                            q = "Insert INTO Driver(DriverNo,DriverNm) values ('"
                                    + js_DriverNo.get(i).toString()
                                    + "','" + js_DriverNm.get(i).toString()

                                    + "')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_DriverNo.length());
                            progressDialog.incrementProgressBy(1);

                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {

                                filllist("السائقين ", 1, total);
                                Chk_Drivers.setChecked(false);
                                progressDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    } catch (final Exception e) {
                        progressDialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                filllist("السائقين ", 0, 0);
                                progressDialog.dismiss();
                                Chk_Drivers.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        } else if (Chk_TransQty.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final Handler _handler = new Handler();


            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("سنـــد التزويــــد");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();
            String MaxSeer = "1";
           /* query = "SELECT  COALESCE(MAX(ser), 0) +1 AS no from ManStore";
             Cursor c1 = sqlHandler.selectQuery(query);
            String Operand = "0";
            Double Order_qty = 0.0;
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    MaxSeer = String.valueOf(c1.getInt(0));
                }
                c1.close();
            }*/


            final String Ser = "1";
            q = "Delete from ManStore";
            sqlHandler.executeQuery(q);
            q = "delete from sqlite_sequence where name='ManStore'";
            sqlHandler.executeQuery(q);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.TrnsferQtyFromMobile(UserID, "0", "");
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_date = js.getJSONArray("date");
                        JSONArray js_fromstore = js.getJSONArray("fromstore");
                        JSONArray js_tostore = js.getJSONArray("tostore");
                        JSONArray js_des = js.getJSONArray("des");
                        JSONArray js_docno = js.getJSONArray("docno");
                        JSONArray js_itemno = js.getJSONArray("itemno");
                        JSONArray js_qty = js.getJSONArray("qty");
                        JSONArray js_UnitNo = js.getJSONArray("UnitNo");
                        JSONArray js_UnitRate = js.getJSONArray("UnitRate");
                        JSONArray js_myear = js.getJSONArray("myear");
                        JSONArray js_StoreName = js.getJSONArray("StoreName");
                        JSONArray js_RetailPrice = js.getJSONArray("RetailPrice");


                        for (i = 0; i < js_docno.length(); i++) {
                            q = "Insert INTO ManStore(SManNo,date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear,RetailPrice ,StoreName ,ser) values ("
                                    + UserID.toString()
                                    + ",'" + js_date.get(i).toString()
                                    + "','" + js_fromstore.get(i).toString()
                                    + "','" + js_tostore.get(i).toString()
                                    + "','" + js_des.get(i).toString()
                                    + "','" + js_docno.get(i).toString()
                                    + "','" + js_itemno.get(i).toString()
                                    + "','" + js_qty.get(i).toString()
                                    + "','" + js_UnitNo.get(i).toString()
                                    + "','" + js_UnitRate.get(i).toString()
                                    + "','" + js_myear.get(i).toString()
                                    + "','" + js_RetailPrice.get(i).toString()
                                    + "','" + js_StoreName.get(i).toString()
                                    + "'," + Ser.toString()
                                    + " )";
                            sqlHandler.executeQuery(q);
                            custDialog.setMax(js_docno.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {

                                filllist("كميات المستودع ", 1, total);
                                Chk_TransQty.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                custDialog.dismiss();
                                filllist("كميات المستودع ", 0, 0);
                                Chk_TransQty.setChecked(false);
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (chk_Pro.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final Handler _handler = new Handler();
            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText(" الــعــروض");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            q = "Delete from Offers_Hdr";
            sqlHandler.executeQuery(q);

            q = " delete from sqlite_sequence where name='Offers_Hdr'";
            sqlHandler.executeQuery(q);

          /*  Get_Offers_Groups();
            GetOffersDtlCond();
            Get_Offers_Dtl_Gifts();*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetOffers_Hdr(UserID);

                    try {
                        Integer i;
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        JSONArray js_Offer_No = js.getJSONArray("Offer_No");
                        JSONArray js_Offer_Name = js.getJSONArray("Offer_Name");
                        JSONArray js_Offer_Date = js.getJSONArray("Offer_Date");
                        JSONArray js_Offer_Type = js.getJSONArray("Offer_Type");
                        JSONArray js_Offer_Status = js.getJSONArray("Offer_Status");
                        JSONArray js_Offer_Begin_Date = js.getJSONArray("Offer_Begin_Date");
                        JSONArray js_Offer_Exp_Date = js.getJSONArray("Offer_Exp_Date");
                        JSONArray js_Offer_Result_Type = js.getJSONArray("Offer_Result_Type");
                        JSONArray js_Offer_Dis = js.getJSONArray("Offer_Dis");
                        JSONArray js_Offer_Amt = js.getJSONArray("Offer_Amt");
                        JSONArray js_TotalValue = js.getJSONArray("TotalValue");
                        JSONArray js_Offer_priority = js.getJSONArray("Offer_priority");
                        JSONArray js_gro_no = js.getJSONArray("gro_no");
                        JSONArray js_Allaw_Repet = js.getJSONArray("Allaw_Repet");
                        JSONArray js_total_item = js.getJSONArray("total_item");
                        JSONArray js_Gift_Iems_Count = js.getJSONArray("Gift_Items_Count");
                        JSONArray js_sum_Qty_item = js.getJSONArray("sum_Qty_item");
                        JSONArray js_Gift_Items_Sum_Qty = js.getJSONArray("Gift_Items_Sum_Qty");
                        JSONArray js_Cate_Offer = js.getJSONArray("Cate_Offer");
                        JSONArray js_Offer_Type_Item = js.getJSONArray("Offer_Type_Item");

                        for (i = 0; i < js_ID.length(); i++) {
                            q = "INSERT INTO Offers_Hdr(ID,Offer_No,Offer_Name,Offer_Date,Offer_Type,Offer_Status,Offer_Begin_Date,Offer_Exp_Date,Offer_Result_Type " +
                                    "  ,Offer_Dis,Offer_Amt,TotalValue,Offer_priority,gro_no,Allaw_Repet,total_item,Gift_Items_Count,sum_Qty_item,Gift_Items_Sum_Qty,Cate_Offer,Offer_Type_Item " +
                                    ") values ('"
                                    + js_ID.get(i).toString()
                                    + "','" + js_Offer_No.get(i).toString()
                                    + "','" + js_Offer_Name.get(i).toString()
                                    + "','" + js_Offer_Date.get(i).toString()
                                    + "','" + js_Offer_Type.get(i).toString()
                                    + "','" + js_Offer_Status.get(i).toString()
                                    + "','" + js_Offer_Begin_Date.get(i).toString()
                                    + "','" + js_Offer_Exp_Date.get(i).toString()
                                    + "','" + js_Offer_Result_Type.get(i).toString()
                                    + "','" + js_Offer_Dis.get(i).toString()
                                    + "','" + js_Offer_Amt.get(i).toString()
                                    + "','" + js_TotalValue.get(i).toString()
                                    + "','" + js_Offer_priority.get(i).toString()
                                    + "','" + js_gro_no.get(i).toString()
                                    + "','" + js_Allaw_Repet.get(i).toString()
                                    + "','" + js_total_item.get(i).toString()
                                    + "','" + js_Gift_Iems_Count.get(i).toString()
                                    + "','" + js_sum_Qty_item.get(i).toString()
                                    + "','" + js_Gift_Items_Sum_Qty.get(i).toString()
                                    + "','" + js_Cate_Offer.get(i).toString()
                                    + "','" + js_Offer_Type_Item.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            custDialog.setMax(js_ID.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("العروض", 1, total);
                                chk_Pro.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();


                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("العروض", 0, 0);
                                chk_Pro.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        } else if (chk_Gift.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final Handler _handler = new Handler();
            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText(" الهدايا");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            q = "Delete from Offers_Dtl_Gifts";
            sqlHandler.executeQuery(q);
            q = "delete from sqlite_sequence where name='Offers_Dtl_Gifts'";
            sqlHandler.executeQuery(q);


           /* Get_Offers_Groups();
            GetOffersDtlCond();*/

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Get_Offers_Dtl_Gifts();
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        JSONArray js_Trans_ID = js.getJSONArray("Trans_ID");
                        JSONArray js_Item_No = js.getJSONArray("Item_No");
                        JSONArray js_Unit_No = js.getJSONArray("Unit_No");
                        JSONArray js_Unit_Rate = js.getJSONArray("Unit_Rate");
                        JSONArray js_QTY = js.getJSONArray("QTY");
                        for (i = 0; i < js_ID.length(); i++) {
                            q = "INSERT INTO Offers_Dtl_Gifts(ID, Trans_ID , Item_No , Unit_No , Unit_Rate , QTY ) values ('"
                                    + js_ID.get(i).toString()
                                    + "','" + js_Trans_ID.get(i).toString()
                                    + "','" + js_Item_No.get(i).toString()
                                    + "','" + js_Unit_No.get(i).toString()
                                    + "','" + js_Unit_Rate.get(i).toString()
                                    + "','" + js_QTY.get(i).toString() + "')";

                            sqlHandler.executeQuery(q);
                            custDialog.setMax(js_ID.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("الهدايا", 1, total);
                                chk_Gift.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();


                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("الهدايا", 0, 0);
                                chk_Gift.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (chk_OfferGroups.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final Handler _handler = new Handler();
            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("شروط العروض");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            q = "Delete from Offers_Groups";
            sqlHandler.executeQuery(q);

            q = "delete from sqlite_sequence where name='Offers_Groups'";
            sqlHandler.executeQuery(q);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Get_Offers_Groups(UserID);
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        JSONArray js_grv_no = js.getJSONArray("grv_no");
                        JSONArray js_gro_name = js.getJSONArray("gro_name");
                        JSONArray js_gro_ename = js.getJSONArray("gro_ename");
                        JSONArray js_gro_type = js.getJSONArray("gro_type");
                        JSONArray js_item_no = js.getJSONArray("item_no");
                        JSONArray js_unit_no = js.getJSONArray("unit_no");
                        JSONArray js_unit_rate = js.getJSONArray("unit_rate");
                        JSONArray js_qty = js.getJSONArray("qty");
                        JSONArray js_SerNo = js.getJSONArray("SerNo");
                        JSONArray js_gro_qty = js.getJSONArray("gro_qty");
                        JSONArray js_gro_amt = js.getJSONArray("gro_amt");

                        for (i = 0; i < js_ID.length(); i++) {
                            q = "INSERT INTO Offers_Groups(ID ,grv_no , gro_name , gro_ename , gro_type , item_no , unit_no , unit_rate,qty , SerNo,gro_qty,gro_amt ) values ('"
                                    + js_ID.get(i).toString()
                                    + "','" + js_grv_no.get(i).toString()
                                    + "','" + js_gro_name.get(i).toString()
                                    + "','" + js_gro_ename.get(i).toString()
                                    + "','" + js_gro_type.get(i).toString()
                                    + "','" + js_item_no.get(i).toString()
                                    + "','" + js_unit_no.get(i).toString()
                                    + "','" + js_unit_rate.get(i).toString()
                                    + "','" + js_qty.get(i).toString()
                                    + "','" + js_SerNo.get(i).toString()
                                    + "','" + js_gro_qty.get(i).toString()
                                    + "','" + js_gro_amt.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);

                            custDialog.setMax(js_ID.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("شروط العروض", 1, total);
                                chk_OfferGroups.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();


                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("شروط العروض", 0, 0);
                                chk_OfferGroups.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (chkCompany.isChecked()) {
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            final Handler _handler = new Handler();
            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);


            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setIndeterminate(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("معلــومــات الـمؤسـسة");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetcompanyInfo();
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        JSONArray js_CompanyID = js.getJSONArray("CompanyID");
                        JSONArray js_CompanyNm = js.getJSONArray("CompanyNm");
                        JSONArray js_UserNm = js.getJSONArray("UserNm");
                        JSONArray js_TaxAcc1 = js.getJSONArray("TaxAcc1");
                        JSONArray js_TaxAcc2 = js.getJSONArray("TaxAcc2");
                        JSONArray js_Notes = js.getJSONArray("Notes");
                        JSONArray js_Address = js.getJSONArray("Address");
                        JSONArray js_Permession = js.getJSONArray("Permession");
                        JSONArray js_CompanyMobile = js.getJSONArray("CompanyMobile");
                        JSONArray js_CompanyMobile2 = js.getJSONArray("CompanyMobile2");
                        JSONArray js_SuperVisorMobile = js.getJSONArray("SuperVisorMobile");
                        JSONArray js_SalInvoiceUnit = js.getJSONArray("SalInvoiceUnit");
                        JSONArray js_PoUnit = js.getJSONArray("PoUnit");
                        JSONArray js_AllowSalInvMinus = js.getJSONArray("AllowSalInvMinus");
                        JSONArray js_GPSAccurent = js.getJSONArray("GPSAccurent");
                        JSONArray js_NumOfInvPerVisit = js.getJSONArray("NumOfInvPerVisit");
                        JSONArray js_NumOfPayPerVisit = js.getJSONArray("NumOfPayPerVisit");
                        JSONArray js_EnbleHdrDiscount = js.getJSONArray("EnbleHdrDiscount");
                        JSONArray js_AllowDeleteInvoice = js.getJSONArray("AllowDeleteInvoice");
                        JSONArray js_VisitWeekNo = js.getJSONArray("VisitWeekNo");


                        q = "Delete from ComanyInfo";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='ComanyInfo'";
                        sqlHandler.executeQuery(q);
                        for (i = 0; i < js_ID.length(); i++) {
                            q = "INSERT INTO ComanyInfo(ID,CompanyID,CompanyNm,UserNm,TaxAcc1,TaxAcc2,Notes,Address,Permession ,CompanyMobile,CompanyMobile2,SuperVisorMobile" +
                                    ",SalInvoiceUnit,PoUnit,AllowSalInvMinus,GPSAccurent,NumOfInvPerVisit,NumOfPayPerVisit,EnbleHdrDiscount,AllowDeleteInvoice,VisitWeekNo) values ('"
                                    + js_ID.get(i).toString()
                                    + "','" + js_CompanyID.get(i).toString()
                                    + "','" + js_CompanyNm.get(i).toString()
                                    + "','" + js_UserNm.get(i).toString()
                                    + "','" + js_TaxAcc1.get(i).toString()
                                    + "','" + js_TaxAcc2.get(i).toString()
                                    + "','" + js_Notes.get(i).toString()
                                    + "','" + js_Address.get(i).toString()
                                    + "','" + js_Permession.get(i).toString()
                                    + "','" + js_CompanyMobile.get(i).toString()
                                    + "','" + js_CompanyMobile2.get(i).toString()
                                    + "','" + js_SuperVisorMobile.get(i).toString()
                                    + "','" + js_SalInvoiceUnit.get(i).toString()
                                    + "','" + js_PoUnit.get(i).toString()
                                    + "','" + js_AllowSalInvMinus.get(i).toString()
                                    + "','" + js_GPSAccurent.get(i).toString()
                                    + "','" + js_NumOfInvPerVisit.get(i).toString()
                                    + "','" + js_NumOfPayPerVisit.get(i).toString()
                                    + "','" + js_EnbleHdrDiscount.get(i).toString()
                                    + "','" + js_AllowDeleteInvoice.get(i).toString()
                                    + "','" + js_VisitWeekNo.get(i).toString()
                                    + "')";
                            sqlHandler.executeQuery(q);
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UpdateDataToMobileActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("CompanyID", js_CompanyID.get(i).toString());
                            editor.putString("CompanyNm", js_CompanyNm.get(i).toString());
                            editor.putString("TaxAcc1", js_TaxAcc1.get(i).toString());
                            editor.putString("Address", js_Address.get(i).toString());
                            editor.putString("Notes", js_Notes.get(i).toString());
                            editor.putString("Permession", js_Permession.get(i).toString());
                            editor.putString("CompanyMobile", js_CompanyMobile.get(i).toString());
                            editor.putString("CompanyMobile2", js_CompanyMobile2.get(i).toString());
                            editor.putString("SuperVisorMobile", js_SuperVisorMobile.get(i).toString());

                            editor.commit();
                            custDialog.setMax(js_ID.length());
                            custDialog.incrementProgressBy(1);

                            if (custDialog.getProgress() == custDialog.getMax()) {
                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("معلومات المؤسسة", 1, total);
                                chkCompany.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("معلومات المؤسسة", 0, 0);
                                chkCompany.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();
        } else if (chkCashCust.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


            final Handler _handler = new Handler();

            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            custDialog.setProgress(0);
            custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText(" العمـــلاء النقدييــن");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    // ws.GetCASHCUST(UserID);
                    ws.GetCASHCUST("-1");
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_No = js.getJSONArray("No");
                        JSONArray js_Name = js.getJSONArray("Name");
                        JSONArray js_veName = js.getJSONArray("veName");
                        JSONArray js_Acc = js.getJSONArray("Acc");
                        JSONArray js_Person = js.getJSONArray("Person");
                        JSONArray js_Celing = js.getJSONArray("Celing");
                        JSONArray js_State = js.getJSONArray("State");
                        JSONArray js_UserID = js.getJSONArray("UserID");
                        q = "Delete from CASHCUST  where  ifnull(Type,0) = 1  ";
                        sqlHandler.executeQuery(q);
                       /* q = " delete from sqlite_sequence where name='CASHCUST'";
                        sqlHandler.executeQuery(q);*/

                        for (i = 0; i < js_No.length(); i++) {
                            q = "INSERT INTO CASHCUST (No,Name,veName,Acc,Person,Celing,State,UserID,Posted,Type ) values ('"
                                    + js_No.get(i).toString()
                                    + "','" + js_Name.get(i).toString()
                                    + "','" + js_veName.get(i).toString()
                                    + "','" + js_Acc.get(i).toString()
                                    + "','" + js_Person.get(i).toString()
                                    + "','" + js_Celing.get(i).toString()
                                    + "','" + js_State.get(i).toString()
                                    + "','" + js_UserID.get(i).toString()
                                    + "','-1','1')";
                            sqlHandler.executeQuery(q);

                            custDialog.setMax(js_No.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("العملاء النقديين", 1, total);
                                chkCashCust.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("العملاء النقديين", 0, 0);
                                chkCashCust.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        } else if (chk_Item_cat.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


            final Handler _handler = new Handler();

            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            //custDialog.setProgress(0);
            //custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("فئات اسعار بيع المواد");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();
            GetPriceList();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Get_Items_Categs(UserID);
                    try {
                        Integer i;
                        String q = "";


                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ItemCode = js.getJSONArray("ItemCode");
                        JSONArray js_CategNo = js.getJSONArray("CategNo");
                        JSONArray js_Price = js.getJSONArray("Price");
                        JSONArray js_MinPrice = js.getJSONArray("MinPrice");
                        JSONArray js_dis = js.getJSONArray("dis");


                        q = "Delete from Items_Categ";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='Items_Categ'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_ItemCode.length(); i++) {
                            q = "INSERT INTO Items_Categ(ItemCode, CategNo , Price , MinPrice , dis ) values ('"
                                    + js_ItemCode.get(i).toString()
                                    + "','" + js_CategNo.get(i).toString()
                                    + "','" + js_Price.get(i).toString()
                                    + "','" + js_MinPrice.get(i).toString()
                                    + "','" + js_dis.get(i).toString() + "')";
                            sqlHandler.executeQuery(q);


                            custDialog.setMax(js_ItemCode.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("فئات اسعار المواد", 1, total);
                                chk_Item_cat.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("فئات اسعار المواد", 0, 0);
                                chk_Item_cat.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        }
        else if (chk_Serial.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


            final Handler _handler = new Handler();

            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            //custDialog.setProgress(0);
            //custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("التسلسلات");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.GetOrdersSerials(UserID);
                    try {
                        Integer i;
                        String q = "";
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Sales = js.getJSONArray("Sales");
                        JSONArray js_Payment = js.getJSONArray("Payment");
                        JSONArray js_SalesOrder = js.getJSONArray("SalesOrder");
                        JSONArray js_PrepareQty = js.getJSONArray("PrepareQty");
                        JSONArray js_RetSales = js.getJSONArray("RetSales");
                        JSONArray js_PostDely = js.getJSONArray("PostDely");
                        JSONArray js_ReturnQty = js.getJSONArray("ReturnQty");
                        JSONArray js_CustCash = js.getJSONArray("CustCash");
                        JSONArray js_Visits = js.getJSONArray("Visits");
                        JSONArray js_EnterQty = js.getJSONArray("EnterQty");
                        JSONArray js_TransferQty = js.getJSONArray("TransferQty");
                        JSONArray js_ItemRecepit= js.getJSONArray("ItemRecepit");

                        q = "Delete from OrdersSitting";
                        sqlHandler.executeQuery(q);

                        q = " delete from sqlite_sequence where name='OrdersSitting'";
                        sqlHandler.executeQuery(q);


                        q = "INSERT INTO OrdersSitting(Sales, Payment , SalesOrder , PrepareQty , RetSales, PostDely , ReturnQty , CustCash,Visits,TransQtySerial,EnterQtySerial,ItemsRecepitSerial  ) values ('"
                                + js_Sales.get(0).toString()
                                + "','" + js_Payment.get(0).toString()
                                + "','" + js_SalesOrder.get(0).toString()
                                + "','" + js_PrepareQty.get(0).toString()
                                + "','" + js_RetSales.get(0).toString()
                                + "','" + js_PostDely.get(0).toString()
                                + "','" + js_ReturnQty.get(0).toString()
                                + "','" + js_CustCash.get(0).toString()
                                + "','" + js_Visits.get(0).toString()
                                + "','" + js_TransferQty.get(0).toString()
                                + "','" + js_EnterQty.get(0).toString()
                                + "','" + js_ItemRecepit.get(0).toString()

                                + "')";
                        sqlHandler.executeQuery(q);


                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("التسلسلات", 1, 1);
                                chk_Serial.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("التسلسلات", 0, 0);
                                chk_Serial.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();

        } else if (chk_Cust_Cat.isChecked()) {

            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


            final Handler _handler = new Handler();

            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
            custDialog.setCanceledOnTouchOutside(false);
            //custDialog.setProgress(0);
            //custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("فئات العملاء");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Get_Cust_Categs(UserID);
                    try {
                        Integer i;
                        String q = "";


                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_catno = js.getJSONArray("catno");
                        JSONArray js_catName = js.getJSONArray("catName");


                        q = "Delete from Categ";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='Categ'";
                        sqlHandler.executeQuery(q);

                        for (i = 0; i < js_catno.length(); i++) {
                            q = "INSERT INTO Categ(catno, catName   ) values ('"
                                    + js_catno.get(i).toString()
                                    + "','" + js_catName.get(i).toString() + "')";
                            sqlHandler.executeQuery(q);


                            custDialog.setMax(js_catno.length());
                            custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("فئات العملاء", 1, total);
                                chk_Cust_Cat.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("فئات العملاء", 0, 0);
                                chk_Cust_Cat.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        } else if (chk_LastPrice.isChecked()) {


            if (UserID == "-1") {
                return;
            }
            tv = new TextView(getApplicationContext());
            lp = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);
            tv.setLayoutParams(lp);
            tv.setPadding(10, 15, 10, 15);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.BLUE);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


            final Handler _handler = new Handler();

            final ProgressDialog custDialog = new ProgressDialog(UpdateDataToMobileActivity.this);
            custDialog.setProgressStyle(custDialog.STYLE_SPINNER);
            custDialog.setCanceledOnTouchOutside(false);
            //custDialog.setProgress(0);
            //custDialog.setMax(100);
            custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
            tv.setText("اخر سعر بيع للعميل");
            custDialog.setCustomTitle(tv);
            custDialog.setProgressDrawable(greenProgressbar);
            custDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Get_CustLastPrice(UserID);
                    try {
                        Integer i;
                        String q = "";


                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_Item_No = js.getJSONArray("Item_No");
                        JSONArray js_Cust_No = js.getJSONArray("Cust_No");
                        JSONArray js_Unit_No = js.getJSONArray("Unit_No");
                        JSONArray js_Price = js.getJSONArray("Price");


                        q = "Delete from CustLastPrice";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='CustLastPrice'";
                        sqlHandler.executeQuery(q);


                        for (i = 0; i < js_Item_No.length(); i++) {

                            q = "INSERT INTO CustLastPrice(Item_No, Cust_No , Unit_No , Price  ) values ('"
                                    + js_Item_No.get(i).toString()
                                    + "','" + js_Cust_No.get(i).toString()
                                    + "','" + js_Unit_No.get(i).toString()
                                    + "','" + js_Price.get(i).toString() + "')";
                            sqlHandler.executeQuery(q);


                            //  custDialog.setMax(js_Item_No.length());
                            // custDialog.incrementProgressBy(1);
                            if (custDialog.getProgress() == custDialog.getMax()) {

                                custDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("اخر سعر بيع للعميل", 1, total);
                                chk_LastPrice.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });

                    } catch (final Exception e) {
                        custDialog.dismiss();
                        _handler.post(new Runnable() {

                            public void run() {
                                filllist("اخر سعر بيع للعميل", 0, 0);
                                chk_LastPrice.setChecked(false);
                                custDialog.dismiss();
                                Do_Trans_From_Server();
                            }
                        });
                    }
                }
            }).start();


        }
// Check Duplicate Data


        q = " DELETE FROM UnitItems WHERE no NOT IN   (SELECT MAX(no) FROM UnitItems GROUP BY item_no,unitno )";
        sqlHandler.executeQuery(q);

        q = "DELETE FROM invf       WHERE no NOT IN   (SELECT MAX(no) FROM invf GROUP BY Item_No )";
        sqlHandler.executeQuery(q);

        q = "DELETE  FROM Unites WHERE no NOT IN (SELECT MAX(no) FROM Unites GROUP BY Unitno )";
        sqlHandler.executeQuery(q);

        q = "DELETE   FROM Customers WHERE ID NOT IN (SELECT MAX(ID) FROM Customers GROUP BY no )";
        sqlHandler.executeQuery(q);


        q = "DELETE   FROM Offers_Groups WHERE no NOT IN (SELECT MAX(no) FROM Offers_Groups GROUP BY grv_no ,Item_No)";
        sqlHandler.executeQuery(q);

        q = "DELETE   FROM Offers_Dtl_Gifts WHERE no NOT IN (SELECT MAX(no) FROM Offers_Dtl_Gifts GROUP BY Trans_ID,Item_No )";
        sqlHandler.executeQuery(q);


        q = "DELETE   FROM Offers_Hdr WHERE no NOT IN (SELECT MAX(no) FROM Offers_Hdr GROUP BY Offer_No )";
        sqlHandler.executeQuery(q);


        q = "DELETE   FROM Categ WHERE no NOT IN (SELECT MAX(no) FROM Categ GROUP BY Offer_No )";
        sqlHandler.executeQuery(q);


    }

    private void Get_ServerDateTime() {

        final Handler _handler = new Handler();
        q = "Delete from ServerDateTime";
        sqlHandler.executeQuery(q);

        q = "delete from sqlite_sequence where name='ServerDateTime'";
        sqlHandler.executeQuery(q);


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_ServerDateTime();
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);

                    String js_ServerDate = js.getString("ServerDate");
                    String js_ServerTime = js.getString("ServerTime");

                    q = "INSERT INTO ServerDateTime(ServerDate ,ServerTime ) values ('"
                            + js_ServerDate
                            + "','" + js_ServerTime
                            + "')";
                    sqlHandler.executeQuery(q);
                } catch (final Exception e) {
                }


            }
        }).start();
    }

    private void SetDeviceDateTime() {/*
try {
    Calendar c = Calendar.getInstance();
    c.set(2013, 8, 15, 12, 34, 56);
    AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    am.setTime(c.getTimeInMillis());
}catch ( Exception ex){
    Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_LONG).show();
}

            CommandResult result = Shell.SU.run("adb date -s 20120419.124012");
            if (result.isSuccessful()) {
                System.out.println(result.getStdout());
                // Example output on a rooted device:
                // uid=0(root) gid=0(root) groups=0(root) context=u:r:init:s0
            }*/
     /*  try{
           Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("adb date -s 20120419.124012");

            Process process;
            process = Runtime.getRuntime().exec("date -s 20120419.124012; \n");
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

        } catch (IOException ex) {
           ex.printStackTrace();
       }
*/
       /* String outp="";
        try {
            ShellExecuter exe = new ShellExecuter();
              outp = exe.Executer("YYYYMMDD.hhmmss");
              outp = exe.Executer("-s 20120423.130000");
           *//* Calendar c = Calendar.getInstance();
            c.set(2009, 9, 9, 12, 0, 0);
            AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            am.setTime(c.getTimeInMillis());*//*
        }catch ( Exception ex ){

             Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this,outp,Toast.LENGTH_LONG).show();*/
    }

    private void Get_Offers_Groups() {

        final Handler _handler = new Handler();
        q = "Delete from Offers_Groups";
        sqlHandler.executeQuery(q);

        q = "delete from sqlite_sequence where name='Offers_Groups'";
        sqlHandler.executeQuery(q);


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_Offers_Groups(UserID);
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID = js.getJSONArray("ID");
                    JSONArray js_grv_no = js.getJSONArray("grv_no");
                    JSONArray js_gro_name = js.getJSONArray("gro_name");
                    JSONArray js_gro_ename = js.getJSONArray("gro_ename");
                    JSONArray js_gro_type = js.getJSONArray("gro_type");
                    JSONArray js_item_no = js.getJSONArray("item_no");
                    JSONArray js_unit_no = js.getJSONArray("unit_no");
                    JSONArray js_unit_rate = js.getJSONArray("unit_rate");
                    JSONArray js_qty = js.getJSONArray("qty");
                    JSONArray js_SerNo = js.getJSONArray("SerNo");
                    JSONArray js_gro_qty = js.getJSONArray("gro_qty");
                    JSONArray js_gro_amt = js.getJSONArray("gro_amt");

                    for (i = 0; i < js_ID.length(); i++) {
                        q = "INSERT INTO Offers_Groups(ID ,grv_no , gro_name , gro_ename , gro_type , item_no , unit_no , unit_rate,qty , SerNo,gro_qty,gro_amt ) values ('"
                                + js_ID.get(i).toString()
                                + "','" + js_grv_no.get(i).toString()
                                + "','" + js_gro_name.get(i).toString()
                                + "','" + js_gro_ename.get(i).toString()
                                + "','" + js_gro_type.get(i).toString()
                                + "','" + js_item_no.get(i).toString()
                                + "','" + js_unit_no.get(i).toString()
                                + "','" + js_unit_rate.get(i).toString()
                                + "','" + js_qty.get(i).toString()
                                + "','" + js_SerNo.get(i).toString()
                                + "','" + js_gro_qty.get(i).toString()
                                + "','" + js_gro_amt.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                    }
                } catch (final Exception e) {
                }

            }
        }).start();
    }

    private void GetOffersDtlCond() {
        q = "Delete from Offers_Dtl_Cond";
        sqlHandler.executeQuery(q);
        q = "delete from sqlite_sequence where name='Offers_Dtl_Cond'";
        sqlHandler.executeQuery(q);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_Offers_Dtl_Cond();
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID = js.getJSONArray("ID");
                    JSONArray js_Trans_ID = js.getJSONArray("Trans_ID");
                    JSONArray js_Gro_Num = js.getJSONArray("Gro_Num");
                    JSONArray js_Allaw_Repet = js.getJSONArray("Allaw_Repet");
                    for (i = 0; i < js_ID.length(); i++) {
                        q = "INSERT INTO Offers_Dtl_Cond(ID, Trans_ID , Gro_Num , Allaw_Repet) values ('"
                                + js_ID.get(i).toString()
                                + "','" + js_Trans_ID.get(i).toString()
                                + "','" + js_Gro_Num.get(i).toString()
                                + "','" + js_Allaw_Repet.get(i).toString() + "')";
                        final String ss = q;
                        sqlHandler.executeQuery(q);
                    }

                } catch (final Exception e) {
                }

            }
        }).start();

    }

    private void Get_Offers_Dtl_Gifts() {
        q = "Delete from Offers_Dtl_Gifts";
        sqlHandler.executeQuery(q);
        q = "delete from sqlite_sequence where name='Offers_Dtl_Gifts'";
        sqlHandler.executeQuery(q);

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_Offers_Dtl_Gifts();
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID = js.getJSONArray("ID");
                    JSONArray js_Trans_ID = js.getJSONArray("Trans_ID");
                    JSONArray js_Item_No = js.getJSONArray("Item_No");
                    JSONArray js_Unit_No = js.getJSONArray("Unit_No");
                    JSONArray js_Unit_Rate = js.getJSONArray("Unit_Rate");
                    JSONArray js_QTY = js.getJSONArray("QTY");
                    for (i = 0; i < js_ID.length(); i++) {
                        q = "INSERT INTO Offers_Dtl_Gifts(ID, Trans_ID , Item_No , Unit_No , Unit_Rate , QTY ) values ('"
                                + js_ID.get(i).toString()
                                + "','" + js_Trans_ID.get(i).toString()
                                + "','" + js_Item_No.get(i).toString()
                                + "','" + js_Unit_No.get(i).toString()
                                + "','" + js_Unit_Rate.get(i).toString()
                                + "','" + js_QTY.get(i).toString() + "')";
                        sqlHandler.executeQuery(q);
                    }
                } catch (final Exception e) {
                }

            }
        }).start();

    }

    private void GetPriceList() {
        q = "Delete from PriceList";
        sqlHandler.executeQuery(q);
        q = "delete from sqlite_sequence where name='PriceList'";
        sqlHandler.executeQuery(q);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UpdateDataToMobileActivity.this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Get_Price_List(UserID);
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Cust_No = js.getJSONArray("Cust_No");
                    JSONArray js_Item_No = js.getJSONArray("Item_No");
                    JSONArray js_Unit_No = js.getJSONArray("Unit_No");
                    JSONArray js_Unit_Rate = js.getJSONArray("Unit_Rate");
                    JSONArray js_Price = js.getJSONArray("Price");
                    JSONArray js_Dis = js.getJSONArray("Dis");
                    //  (Cust_No,Item_No,Unit_No,Unit_Rate,Price,Dis )

                    for (i = 0; i < js_Cust_No.length(); i++) {
                        q = "INSERT INTO PriceList(Cust_No,Item_No,Unit_No,Unit_Rate,Price,Dis) values ('"
                                + js_Cust_No.get(i).toString()
                                + "','" + js_Item_No.get(i).toString()
                                + "','" + js_Unit_No.get(i).toString()
                                + "','" + js_Unit_Rate.get(i).toString()
                                + "','" + js_Price.get(i).toString()
                                + "','" + js_Dis.get(i).toString() + "')";
                        sqlHandler.executeQuery(q);
                    }
                } catch (final Exception e) {
                }

            }
        }).start();

    }

    private void FillSal_InvAdapter(String OrderNo) {
        String query = "";
        contactList = new ArrayList<Cls_Sal_InvItems>();
        contactList.clear();


        query = "  select distinct ifnull(pod.Operand,0) as Operand  ,  pod.bounce_qty,pod.dis_per , pod.dis_Amt , pod.OrgPrice , pod.tax_Amt , pod.total ,Unites.UnitName,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt    " +
                " from Sal_invoice_Det pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.OrderNo='" + OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                for (c1.move(0); c1.moveToNext(); c1.isAfterLast()) {
                    // do {
                    Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemNo")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("price")));
                    contactListItems.setItemOrgPrice(c1.getString(c1
                            .getColumnIndex("OrgPrice")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setBounce(c1.getString(c1
                            .getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1
                            .getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));
                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));
                    contactListItems.setOperand(c1.getString(c1
                            .getColumnIndex("Operand")));
                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));
                    contactListItems.setProID(c1.getString(c1
                            .getColumnIndex("ProID")));
                    contactListItems.setPro_bounce(c1.getString(c1
                            .getColumnIndex("Pro_bounce")));
                    contactListItems.setPro_dis_Per(c1.getString(c1
                            .getColumnIndex("Pro_dis_Per")));
                    contactListItems.setPro_amt(c1.getString(c1
                            .getColumnIndex("Pro_amt")));
                    contactListItems.setPro_Total(c1.getString(c1
                            .getColumnIndex("pro_Total")));
                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setDisAmtFromHdr("0");
                    contactList.add(contactListItems);
                } //while (c1.moveToNext());
            }
            try {
                c1.close();
            } catch (Exception ex) {
            }

        }
        Cls_Sal_InvItems Inv_Obj;
        for (int j = 0; j < contactList.size(); j++) {
            Inv_Obj = new Cls_Sal_InvItems();
            Inv_Obj = contactList.get(j);
            Inv_Obj.setDis_Amt(String.valueOf(Double.parseDouble(Inv_Obj.getDis_Amt()) + Double.parseDouble(Inv_Obj.getPro_amt()) + Double.parseDouble(Inv_Obj.getDisAmtFromHdr())));
            Inv_Obj.setDiscount(String.valueOf(Double.parseDouble(Inv_Obj.getDiscount()) + Double.parseDouble(Inv_Obj.getPro_dis_Per()) + Double.parseDouble(Inv_Obj.getDisPerFromHdr())));
            Inv_Obj.setBounce(String.valueOf(Double.parseDouble(Inv_Obj.getBounce()) + Double.parseDouble(Inv_Obj.getPro_bounce())));
        }

    }

    private void FillPayMents_Check_Adapter(String OrderNo) {
        String query = "";
        ChecklList.clear();
        query = "Select  distinct rc.CheckNo,rc.CheckDate,rc.BankNo,   IFNULL(rc.Amnt,0)as Amnt , b.Bank from  RecCheck rc  left join banks b on b.bank_num = rc.BankNo" +
                " where DocNo ='" + OrderNo.toString() + "'";
        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Check cls_check_obj = new Cls_Check();
                    cls_check_obj.setSer(i);
                    cls_check_obj.setCheckNo(c1.getString(c1
                            .getColumnIndex("CheckNo")));
                    cls_check_obj.setCheckDate(c1.getString(c1
                            .getColumnIndex("CheckDate")));
                    cls_check_obj.setBankName(c1.getString(c1
                            .getColumnIndex("Bank")));
                    cls_check_obj.setBankNo(c1.getString(c1
                            .getColumnIndex("BankNo")));
                    cls_check_obj.setAmnt(c1.getString(c1
                            .getColumnIndex("Amnt")));
                    ChecklList.add(cls_check_obj);
                    i = i + 1;
                } while (c1.moveToNext());
            }
            c1.close();
        }

    }

    private void Fill_Po_Adapter(String OrderNo) {
        String query = "";
        PoList = new ArrayList<ContactListItems>();
        PoList.clear();

        Integer i = 1;
        Cursor c1 = sqlHandler.selectQuery(query);


        query = "  select distinct Unites.UnitName, pod.OrgPrice ,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo ,pod.dis_Amt,pod.dis_per,pod.bounce_qty ,  pod.tax_Amt   , pod.total  " +
                "  , pod.pro_Total ,ifnull( pod.ExpDate,'') as ExpDate  ,ifnull( pod.Batch,'') as  Batch    , pod.ProID  " +
                "  ,  ifnull(pod.Pro_bounce ,0) AS Pro_bounce, ifnull(pod.Price_From_AB ,0) AS Price_From_AB  , ifnull(pod.Pro_dis_Per,0) AS Pro_dis_Per ,ifnull(pod.Pro_amt,0) AS  Pro_amt , ifnull(pod.pro_Total,0) AS pro_Total   from Po_dtl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + OrderNo.toString() + "'";
        c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("price")));
                    contactListItems.setItemOrgPrice(c1.getString(c1
                            .getColumnIndex("OrgPrice")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setBounce(c1.getString(c1
                            .getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1
                            .getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));

                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));

                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));

                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));

                    contactListItems.setProID("");


                    contactListItems.setPro_bounce(c1.getString(c1
                            .getColumnIndex("Pro_bounce")));

                    contactListItems.setPro_dis_Per(c1.getString(c1
                            .getColumnIndex("Pro_dis_Per")));

                    contactListItems.setPro_amt(c1.getString(c1
                            .getColumnIndex("Pro_amt")));

                    contactListItems.setPro_Total(c1.getString(c1
                            .getColumnIndex("pro_Total")));


                    contactListItems.setDisAmtFromHdr("0");

                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setBatch(c1.getString(c1
                            .getColumnIndex("Batch")));
                    contactListItems.setExpDate(c1.getString(c1
                            .getColumnIndex("ExpDate")));


                    contactListItems.setPrice_From_AB(c1.getString(c1
                            .getColumnIndex("Price_From_AB")));


                    PoList.add(contactListItems);

                } while (c1.moveToNext());

            }

            c1.close();
        }

    }

    private void Post_Sal_Inv(String OrderNo) {
        WriteTxtFile.MakeText("Post_Sal_Inv fun", OrderNo);

        final String pno = OrderNo;
        final ProgressDialog loading_dialog;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FillSal_InvAdapter(OrderNo);


        String json = "[{''}]";
        try {
            if (contactList.size() > 0) {
                json = new Gson().toJson(contactList);

            }
        } catch (Exception ex) {

        }


        try {
            final String str;

            String query = "SELECT distinct  V_OrderNo,OrderNo, acc,date,UserID, COALESCE(hdr_dis_per,0) as hdr_dis_per  , COALESCE(hdr_dis_value ,0) as  hdr_dis_value , COALESCE(Total ,0) as  Total , COALESCE(Net_Total ,0) as Net_Total , COALESCE( Tax_Total ,0) as Tax_Total , COALESCE(bounce_Total ,0) as bounce_Total , COALESCE( include_Tax ,0) as include_Tax" +
                    " ,Nm ,COALESCE( disc_Total ,0) as  disc_Total , COALESCE(inovice_type ,0)  as inovice_type  FROM Sal_invoice_Hdr where OrderNo  ='" + OrderNo.toString() + "'";
            Cursor c1 = sqlHandler.selectQuery(query);
            JSONObject jsonObject = new JSONObject();
            if (c1 != null && c1.getCount() != 0) {
                c1.moveToFirst();
                try {
                    jsonObject.put("Cust_No", c1.getString(c1.getColumnIndex("acc")));
                    jsonObject.put("Date", c1.getString(c1.getColumnIndex("date")));
                    jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));
                    jsonObject.put("OrderNo", c1.getString(c1.getColumnIndex("OrderNo")));
                    jsonObject.put("hdr_dis_per", c1.getString(c1.getColumnIndex("hdr_dis_per")));
                    if (c1.getString(c1.getColumnIndex("hdr_dis_value")).equals("")) {
                        jsonObject.put("hdr_dis_value", "0");
                    } else {
                        jsonObject.put("hdr_dis_value", c1.getString(c1.getColumnIndex("hdr_dis_value")));
                    }
                    jsonObject.put("Total", c1.getString(c1.getColumnIndex("Total")));
                    jsonObject.put("Net_Total", c1.getString(c1.getColumnIndex("Net_Total")));
                    jsonObject.put("Tax_Total", c1.getString(c1.getColumnIndex("Tax_Total")));
                    jsonObject.put("bounce_Total", c1.getString(c1.getColumnIndex("bounce_Total")));
                    jsonObject.put("include_Tax", c1.getString(c1.getColumnIndex("include_Tax")));
                    jsonObject.put("disc_Total", c1.getString(c1.getColumnIndex("disc_Total")));
                    jsonObject.put("inovice_type", c1.getString(c1.getColumnIndex("inovice_type")));
                    jsonObject.put("CashCustNm", c1.getString(c1.getColumnIndex("Nm")));
                    jsonObject.put("V_OrderNo", c1.getString(c1.getColumnIndex("V_OrderNo")));

                } catch (JSONException ex) {
                    WriteTxtFile.MakeText(" 1 Post_Sal_Inv No = " + OrderNo, ex.getMessage().toString());
                    ex.printStackTrace();
                } catch (Exception ex) {
                    WriteTxtFile.MakeText(" 2 Post_Sal_Inv No = " + OrderNo, ex.getMessage().toString());

                }
                c1.close();
            }


            str = jsonObject.toString() + json;

            loading_dialog = ProgressDialog.show(UpdateDataToMobileActivity.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد فاتورة المبيعات", true);
            loading_dialog.setCancelable(false);
            loading_dialog.setCanceledOnTouchOutside(false);
            loading_dialog.show();
            final Handler _handler = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                    ws.Save_Sal_Invoice(str);
                    try {
                        if (We_Result.ID > 0) {
                            ContentValues cv = new ContentValues();
                            cv.put("Post", We_Result.ID);
                            long i;
                            i = sqlHandler.Update("Sal_invoice_Hdr", cv, "OrderNo='" + pno + "'");
                            _handler.post(new Runnable() {
                                public void run() {
                                    //  WriteTxtFile.MakeText(" 3 Post_Sal_Inv No " + pno, We_Result.Msg.toString());
                                    filllist_post("فاتورة مبيعات رقم ", 1, pno);
                                    loading_dialog.dismiss();
                                }
                            });
                        } else {
                            _handler.post(new Runnable() {
                                public void run() {
                                    WriteTxtFile.MakeText(" 4 Post_Sal_Inv No= " + pno, We_Result.Msg.toString());
                                    filllist_post("فاتورة مبيعات رقم  ", 0, pno);
                                    loading_dialog.dismiss();
                                }
                            });
                        }

                    } catch (final Exception e) {
                        WriteTxtFile.MakeText(" 5 Post_Sal_Inv  No= " + pno, e.getMessage().toString());
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist_post("فاتورة مبيعات رقم  ", 0, pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }

                }
            }).start();
        } catch (Exception ex) {
            WriteTxtFile.MakeText("Post_Sal_Inv fun 1", ex.getMessage().toString());
        }

    }

    private void Post_Payments(String OrderNo) {

        final String pno = OrderNo;
        final ProgressDialog loading_dialog;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FillPayMents_Check_Adapter(OrderNo);


        String json = "[{''}]";
        try {
            if (ChecklList.size() > 0) {
                json = new Gson().toJson(ChecklList);
            }
        } catch (Exception ex) {
        }


        String query = "Select  distinct rc.V_OrderNo, rc.DocNo,  IFNULL(rc.CheckTotal,0) as CheckTotal, IFNULL(rc.Cash,0) as Cash, rc.Desc,rc.Amnt,rc.TrDate,rc.CustAcc  ,c.name , rc.curno  ,COALESCE(Post, -1)  as Post , " +
                "rc.UserID ,rc.VouchType  from RecVoucher rc   left join Customers c on c.no = rc.CustAcc  where rc.DocNo = '" + OrderNo.toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            try {
                jsonObject.put("OrderNo", c1.getString(c1.getColumnIndex("DocNo")));
                jsonObject.put("acc", c1.getString(c1.getColumnIndex("CustAcc")));
                jsonObject.put("Amt", c1.getString(c1.getColumnIndex("Amnt")));
                jsonObject.put("Date", c1.getString(c1.getColumnIndex("TrDate")));
                jsonObject.put("notes", c1.getString(c1.getColumnIndex("Desc")));
                jsonObject.put("VouchType", c1.getString(c1.getColumnIndex("VouchType")));
                jsonObject.put("CurNo", c1.getString(c1.getColumnIndex("curno")));
                if (c1.getString(c1.getColumnIndex("Cash")).toString().length() == 0) {
                    jsonObject.put("Cash", "0.0");
                } else {
                    jsonObject.put("Cash", c1.getString(c1.getColumnIndex("Cash")));
                }
                jsonObject.put("CheckTotal", c1.getString(c1.getColumnIndex("CheckTotal")));
                jsonObject.put("V_OrderNo", c1.getString(c1.getColumnIndex("V_OrderNo")));


                jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));

            } catch (JSONException ex) {
                WriteTxtFile.MakeText("Post_Payments  No= " + OrderNo, ex.getMessage().toString());
                ex.printStackTrace();
            } catch (Exception ex) {
                WriteTxtFile.MakeText("Post_Payments  No= " + OrderNo, ex.getMessage().toString());
                ex.printStackTrace();
            }


            c1.close();
        }


        str = jsonObject.toString() + json;

        loading_dialog = ProgressDialog.show(UpdateDataToMobileActivity.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد سندات القبض ", true);
        loading_dialog.setCancelable(false);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.show();
        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.SavePayment(str);

                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("Post", We_Result.ID);
                        long i;
                        i = sqlHandler.Update("RecVoucher", cv, "DocNo='" + pno + "'");

                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post("سند قبض رقم ", 1, pno);
                                loading_dialog.dismiss();
                            }
                        });
                    } else {


                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post("سند قبض رقم ", 0, pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }

                } catch (Exception e) {

                    _handler.post(new Runnable() {
                        public void run() {

                            filllist_post("سند قبض رقم ", 0, pno);
                            loading_dialog.dismiss();
                        }
                    });
                }

            }
        }).start();

    }

    private void Post_Purch_Order(String OrderNo) {
        final String pno = OrderNo;
        final ProgressDialog loading_dialog;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Fill_Po_Adapter(OrderNo);

        String json = "[{''}]";
        try {
            if (PoList.size() > 0) {
                json = new Gson().toJson(PoList);

            }
        } catch (Exception ex) {
        }

        final String str;


        String query = "SELECT  distinct V_OrderNo, acc, userid , Delv_day_count ,date" +
                "   , orderno ,  Total , Net_Total ,Tax_Total , disc_Total" +
                "  ,OrderType,OverCelling  ,include_Tax ,ifnull(DeliveryDate,'') as  DeliveryDate , ifnull (Notes,'') as Notes  , OrderType " +
                "    FROM Po_Hdr  where orderno  ='" + OrderNo.toString() + "'";

        Cursor c1 = sqlHandler.selectQuery(query);
        JSONObject jsonObject = new JSONObject();

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();

            try {
                jsonObject.put("Cust_No", c1.getString(c1.getColumnIndex("acc")));
                jsonObject.put("day_Count", c1.getString(c1.getColumnIndex("Delv_day_count")));
                jsonObject.put("Date", c1.getString(c1.getColumnIndex("date")));
                jsonObject.put("UserID", c1.getString(c1.getColumnIndex("userid")).replace(",", ""));
                jsonObject.put("OrderNo", c1.getString(c1.getColumnIndex("orderno")));
                jsonObject.put("OrderType", c1.getString(c1.getColumnIndex("OrderType")));
                jsonObject.put("OverCelling", c1.getString(c1.getColumnIndex("OverCelling")));
                jsonObject.put("Total", c1.getString(c1.getColumnIndex("Total")).replace(",", ""));
                jsonObject.put("Net_Total", c1.getString(c1.getColumnIndex("Net_Total")).replace(",", ""));
                jsonObject.put("Tax_Total", c1.getString(c1.getColumnIndex("Tax_Total")).replace(",", ""));
                jsonObject.put("bounce_Total", "0");
                jsonObject.put("disc_Total", c1.getString(c1.getColumnIndex("disc_Total")).replace(",", ""));
                jsonObject.put("include_Tax", c1.getString(c1.getColumnIndex("include_Tax")).replace(",", ""));
                jsonObject.put("V_OrderNo", c1.getString(c1.getColumnIndex("V_OrderNo")));
                jsonObject.put("DeliveryDate", c1.getString(c1.getColumnIndex("DeliveryDate")));
                jsonObject.put("Notes", c1.getString(c1.getColumnIndex("Notes")));
                jsonObject.put("OrderType", c1.getString(c1.getColumnIndex("OrderType")));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }


            c1.close();
        }

        str = jsonObject.toString() + json;


        loading_dialog = ProgressDialog.show(UpdateDataToMobileActivity.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد طلبات البيع", true);
        loading_dialog.setCancelable(false);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.show();
        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.Save_po(str, "Insert_PurshOrder");

                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("posted", We_Result.ID);
                        long i;
                        i = sqlHandler.Update("Po_Hdr", cv, "orderno='" + pno + "'");

                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post("طلب البيع", 1, pno);
                                loading_dialog.dismiss();
                            }
                        });
                    } else {


                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post(" طلب البيع ", 0, pno);
                                loading_dialog.dismiss();
                            }
                        });
                    }

                } catch (final Exception e) {

                    _handler.post(new Runnable() {
                        public void run() {

                            filllist_post(" طلب البيع ", 0, pno);
                            loading_dialog.dismiss();
                        }
                    });
                }

            }
        }).start();

    }

    public void btn_Post_Trans(View view) {
        final CheckBox Chk_Post_Inv = (CheckBox) findViewById(R.id.Chk_Post_Inv);
        final CheckBox Chk_Post_Payments = (CheckBox) findViewById(R.id.Chk_Post_Payments);
        final CheckBox chk_po_post = (CheckBox) findViewById(R.id.chk_po_post);

        List_Result.clear();
        Lv_Result.setAdapter(null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String query = "";

        try {
            SharcustLocation();
            SharManVisits();

            SharUseCode();
            SharCashCust();

        } catch (Exception ex) {
            WriteTxtFile.MakeText("UpdateDataToMobileActivity SharManVisits ", ex.getMessage().toString());
        }
        if (Chk_Post_Inv.isChecked()) {
            String SalesOrderNo = "0";

      /*  try{
           query = "Delete from  Sal_invoice_Det  where OrderNo in   " +
                   "( select OrderNo from  Sal_invoice_Hdr     where Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) and  OrderNo <= "+ SalesOrderNo+" )";
           sqlHandler.executeQuery(query);
           query = "Delete from  Sal_invoice_Hdr where Post !='-1' And ( (date)   <  ('" + currentDateandTime + "')) and  OrderNo <= "+ SalesOrderNo ;
           sqlHandler.executeQuery(query);
       } catch (Exception ex) {
           WriteTxtFile.MakeText("UpdateDataToMobileActivity Delete from  Sal_invoice_Det", ex.getMessage().toString()  );
       }*/
            query = "Select   distinct OrderNo from  Sal_invoice_Hdr where Post  ='-1'";
            Cursor c1 = sqlHandler.selectQuery(query);
            try {
                if (c1 != null && c1.getCount() != 0) {
                    c1.close();
                    new SalesInvoicePosting().execute();
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            this).create();
                    alertDialog.setTitle("ترحيل الحركات");
                    alertDialog.setMessage("لا يوجد فواتير غير مرحلة ");            // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();

                }
            } catch (Exception ex) {


            }

        }

        if (Chk_Post_Payments.isChecked()) {

            query = "Select   distinct DocNo from  RecVoucher  where ifnull(Post,-1)  =-1";
            Cursor c1 = sqlHandler.selectQuery(query);

            if (c1 != null && c1.getCount() != 0) {
                c1.close();
                new PaymentsPosting().execute();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("ترحيل الحركات");
                alertDialog.setMessage(" لا يوجد سندات قبض غير مرحلة");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();

            }


        }
        if (chk_po_post.isChecked()) { // طلبات البيع

            query = "Select   distinct orderno from  Po_Hdr   where ifnull(posted,-1)  ='-1'";
            Cursor c1 = sqlHandler.selectQuery(query);

            if (c1 != null && c1.getCount() != 0) {
               /* if (c1.moveToFirst()) {
                    do {
                        Post_Purch_Order(c1.getString(c1.getColumnIndex("orderno")));

                    } while (c1.moveToNext());
                }*/

                c1.close();
                new SalesOrderPosting().execute();

            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("ترحيل الحركات");
                alertDialog.setMessage("لا يوجد طلبات بيع  غير مرحلة");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();

            }


        }

    }

    private class SalesOrderPosting extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(UpdateDataToMobileActivity.this,
                    "استقبال وترحيل البيانات", "الرجاء الانتظار ، العمل جاري على اعتماد طلبات البيع", true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            PostSalesOrder obj = new PostSalesOrder(UpdateDataToMobileActivity.this);
            obj.DoTrans();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            filllist_post("", 0, "");
        }

    }

    private class SalesInvoicePosting extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(UpdateDataToMobileActivity.this,
                    "استقبال وترحيل البيانات", "الرجاء الانتظار ، العمل جاري على اعتماد فاتورة المبيعات", true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            PostSalesInvoice obj = new PostSalesInvoice(UpdateDataToMobileActivity.this);
            obj.DoTrans();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            filllist_post("", 0, "");
        }

    }

    private class PaymentsPosting extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(UpdateDataToMobileActivity.this,
                    "استقبال وترحيل البيانات", "الرجاء الانتظار ، العمل جاري على اعتماد سندات القبض", true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            PostPayments obj = new PostPayments(UpdateDataToMobileActivity.this);
            obj.DoTrans();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            filllist_post("", 0, "");
        }

    }

    public void SharcustLocation() {

        //sqlHandler=new SqlHandler(this);

        final Handler _handler = new Handler();
        String query = "  select distinct CusNo ,Lat ,Long ,Address, date, UserID from CustLocation   where posted = -1";
        Cursor c1 = sqlHandler.selectQuery(query);
        ArrayList<Cls_CustLocation> objlist;
        objlist = new ArrayList<Cls_CustLocation>();
        objlist.clear();


        if (c1 != null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_CustLocation obj = new Cls_CustLocation();
                    obj.setCusNo(c1.getString(c1
                            .getColumnIndex("CusNo")));
                    obj.setLat(c1.getString(c1
                            .getColumnIndex("Lat")));
                    obj.setLong(c1.getString(c1
                            .getColumnIndex("Long")));
                    obj.setAddress(c1.getString(c1
                            .getColumnIndex("Address")));
                    obj.setDate(c1.getString(c1
                            .getColumnIndex("date")));
                    obj.setUserID(c1.getString(c1
                            .getColumnIndex("UserID")));
                    obj.setPosted("-1");

                    objlist.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(objlist);
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.SaveCustLocation(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  CustLocation  set Posted='1'  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {

                                filllist_post("تحديد موقع العميل", 1, "");

                            }
                        });


                    }
                } catch (final Exception e) {

                }
            }
        }).start();
    }

    public void SharCashCust() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(UpdateDataToMobileActivity.this);
        String u = sharedPreferences.getString("UserID", "");


        final Handler _handler = new Handler();
        String query = " Select No,Name as ArName ,veName,Acc,Person,Celing,State,UserID  from CASHCUST where ifnull( Type,-1) = 0 and  UserID ='" + u + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        ArrayList<cls_CashCust> CashCust;
        CashCust = new ArrayList<cls_CashCust>();
        CashCust.clear();

        //query = " delete from   SaleManRounds   ";
        // sqlHandler.executeQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_CashCust obj = new cls_CashCust();
                    obj.setNo(c1.getString(c1
                            .getColumnIndex("No")));
                    obj.setArName(c1.getString(c1
                            .getColumnIndex("ArName")));
                    obj.setVeName(c1.getString(c1
                            .getColumnIndex("veName")));
                    obj.setAcc(c1.getString(c1
                            .getColumnIndex("Acc")));
                    obj.setPerson(c1.getString(c1
                            .getColumnIndex("Person")));
                    obj.setCeling(c1.getString(c1
                            .getColumnIndex("Celing")));
                    obj.setState(c1.getString(c1
                            .getColumnIndex("State")));
                    obj.setUserID(c1.getString(c1
                            .getColumnIndex("UserID")));

                    CashCust.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(CashCust);


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.SaveCashCust(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  CASHCUST  set Posted=2  where Posted = '-2' and Type = 0 ";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist_post("العملاء النقديين", 1, "");
                            }
                        });


                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }

    public void SharManVisits() {

        //sqlHandler=new SqlHandler(this);

        final Handler _handler = new Handler();
        String query = "select  distinct no,ManNo, CusNo, DayNum ,Tr_Data ,Start_Time,End_Time, Duration,OrderNo " +
                "  ,Note from SaleManRounds   where Posted = -1";
        Cursor c1 = sqlHandler.selectQuery(query);
        ArrayList<Cls_SaleManDailyRound> RoundList;
        RoundList = new ArrayList<Cls_SaleManDailyRound>();
        RoundList.clear();

        //query = " delete from   SaleManRounds   ";
        // sqlHandler.executeQuery(query);
        Cls_SaleManDailyRound cls_saleManDailyRound;

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_saleManDailyRound = new Cls_SaleManDailyRound();
                    cls_saleManDailyRound.setNo(c1.getString(c1
                            .getColumnIndex("no")));
                    cls_saleManDailyRound.setManNo(c1.getString(c1
                            .getColumnIndex("ManNo")));
                    cls_saleManDailyRound.setCusNo(c1.getString(c1
                            .getColumnIndex("CusNo")));
                    cls_saleManDailyRound.setDayNum(c1.getString(c1
                            .getColumnIndex("DayNum")));
                    cls_saleManDailyRound.setTr_Data(c1.getString(c1
                            .getColumnIndex("Tr_Data")));
                    cls_saleManDailyRound.setStart_Time(c1.getString(c1
                            .getColumnIndex("Start_Time")));
                    cls_saleManDailyRound.setEnd_Time(c1.getString(c1
                            .getColumnIndex("End_Time")));

                    cls_saleManDailyRound.setDuration(c1.getString(c1
                            .getColumnIndex("Duration")));

                    cls_saleManDailyRound.setOrderNo(c1.getString(c1
                            .getColumnIndex("OrderNo")));

                    cls_saleManDailyRound.setNote(c1.getString(c1
                            .getColumnIndex("Note")));



                    RoundList.add(cls_saleManDailyRound);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(RoundList);

        Calendar c = Calendar.getInstance();
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        //  final Handler _handler = new Handler();


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.SaveManVisits(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  SaleManRounds  set Posted=1  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist_post("جولات المندوب", 1, "");
                            }
                        });

                        query = " delete from   SaleManRounds   where Posted =1 and  DayNum < " + dayOfWeek;
                        sqlHandler.executeQuery(query);
                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }

    public void SharUseCode() {

        //sqlHandler=new SqlHandler(this);
        //q = "INSERT INTO UsedCode(Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo , Posted ) values ('1"


        final Handler _handler = new Handler();
        String query = "  select distinct Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo" +
                " from UsedCode   where Posted = -1";

        Cursor c1 = sqlHandler.selectQuery(query);
/*
        query = " delete from   UsedCode  ";
        sqlHandler.executeQuery(query);*/

        ArrayList<Cls_UsedCodes> CodeList;
        CodeList = new ArrayList<Cls_UsedCodes>();
        CodeList.clear();


        if (c1 != null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_UsedCodes obj = new Cls_UsedCodes();
                    obj.setStatus(c1.getString(c1
                            .getColumnIndex("Status")));
                    obj.setCode(c1.getString(c1
                            .getColumnIndex("Code")));
                    obj.setOrderNo(c1.getString(c1
                            .getColumnIndex("OrderNo")));
                    obj.setCustomerNo(c1.getString(c1
                            .getColumnIndex("CustomerNo")));
                    obj.setItemNo(c1.getString(c1
                            .getColumnIndex("ItemNo")));
                    obj.setTr_Date(c1.getString(c1
                            .getColumnIndex("Tr_Date")));
                    obj.setTr_Time(c1.getString(c1
                            .getColumnIndex("Tr_Time")));
                    obj.setUserNo(c1.getString(c1
                            .getColumnIndex("UserNo")));

                    CodeList.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(CodeList);
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(UpdateDataToMobileActivity.this);
                ws.ShareUsedCode(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  UsedCode  set Posted='1'  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {
                                filllist_post("الرموز المستخدمة", 1, "");
                            }
                        });
                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }

    private void filllist_post(String str, int f, String c) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);



        q = "Select distinct s.Post as posted , s.Net_Total as Amt, s.OrderNo ,s.acc ,s.date , 'فاتورة مبيعات' as type,  CASE s.inovice_type WHEN '-1' THEN  c.name ELSE s.Nm END as  name   " +
                "  from  Sal_invoice_Hdr s left join Customers c on c.no =s.acc where  UserID='" + sharedPreferences.getString("UserID", "") + "' ";


        q = q + " UNION ALL Select distinct RecVoucher.Post as posted, RecVoucher.Amnt as Amt ,RecVoucher.DocNo as OrderNo , RecVoucher.CustAcc as scc ,RecVoucher.TrDate as date, 'سند قبض' as type ,Customers.name as name  from RecVoucher left join Customers  on Customers.no =RecVoucher.CustAcc" +
                " where  RecVoucher.UserID ='" + sharedPreferences.getString("UserID", "") + "'  ";

        q = q + "UNION ALL  Select distinct po.posted as posted , COALESCE(po.Net_Total,0) as Amt,po.orderno  as OrderNo,po.acc , po.date , 'طلب بيع' as type ,c.name  from Po_Hdr po Left join Customers c on c.no = po.acc " +
                "where  userid='" + sharedPreferences.getString("UserID", "") + "' ";


        SqlHandler sqlHandler = new SqlHandler(this);
        Cursor c1 = sqlHandler.selectQuery(q);


        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_UpdateData obj = new Cls_UpdateData();

                    obj.setDocNo(c1.getString(c1.getColumnIndex("OrderNo")));
                    obj.setDocType(c1.getString(c1.getColumnIndex("type")));
                    obj.setDocPostFlag(c1.getString(c1.getColumnIndex("posted")));

                    List_Result.add(obj);
                } while (c1.moveToNext());
            }

            c1.close();

        }

       /* Cls_UpdateData obj = new Cls_UpdateData();
        String msg = "";
        if (f ==1){

            msg = "تمت عملية اعتماد " + str + c+ " بنجاح"   ;
        }
        else {
            msg = "عملية اعتماد "+ str +  c+ " لم تتم بنجاح"      ;

        }
*/


        listAdapter = new Cls_UpdateData_Adapter(UpdateDataToMobileActivity.this, List_Result);
        Lv_Result.setAdapter(listAdapter);

    }

    public void btn_back(View view) {
        Intent i = new Intent(this, JalMasterActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, JalMasterActivity.class);
        startActivity(i);




    }
}



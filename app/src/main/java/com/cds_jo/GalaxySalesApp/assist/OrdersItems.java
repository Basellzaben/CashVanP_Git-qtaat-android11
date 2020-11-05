package com.cds_jo.GalaxySalesApp.assist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.*;
import com.cds_jo.GalaxySalesApp.OrderApproval.Show_Orders_Need_Approval;
import com.cds_jo.GalaxySalesApp.PostTransActions.PostSalesOrder;
import com.cds_jo.GalaxySalesApp.XprinterDoc.Xprinter_SalesOrder;
import com.cds_jo.GalaxySalesApp.assist.Logtrans.InsertLogTrans;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.xml.transform.sax.SAXSource;

import Methdes.MyTextView;


import header.Header_Frag;
import header.SimpleSideDrawer;

import static java.security.AccessController.getContext;

public class OrdersItems extends FragmentActivity {
    private static AlertDialog alertDialog1;
    SqlHandler sqlHandler;
    ListView lv_Items;
    long PostResult = 0;
    ContentValues cvv;
    AlertDialog.Builder builder;
    ArrayList<ContactListItems> contactList;
    ArrayList<Cls_Offers_Groups> offer_groups_List;
    ArrayList<Cls_Offers_Groups> offer_groups_Effict_List;
    ArrayList<Cls_Offers_Dtl_Gifts> cls_offers_dtl_giftses;
    public ArrayList<Cls_Offers_Hdr> cls_offers_hdrs;
    public ArrayList<Cls_Offers_Hdr> cls_offers_hdrsNew;
    String UserID = "";
    MyTextView ApprovalOrdersCount;
    boolean checkItem, CheckGeroupQty, CheckGroupAmt = false;
    Double Hdr_Dis_A_Amt, Hdr_Dis_Per;
    public ProgressDialog loadingdialog;
    public String json;
    String q;
    Boolean IsNew;
    Long SqlRes;
    String Celing;
    String CatNo = "-1";
    public int f = 0;
    CharSequence[] symtoms;
    AlertDialog aDialog;
    private SimpleSideDrawer mNav;
    NumberFormat nf_out;
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    TextView tv_HowPay,accno,Order_no;
    Intent BackInt;
    CheckBox chk_Cash;
    ListView listView;
    private ImageView Img_Menu;
    GetPermession obj;

    MyTextView tv_TaxStatus, tv_CustStatus;
    String CustTaxStatus;
    ArrayList<AlertChoiceItem> itemList;
    String CelingAmt = "0";
    String Result, query;
    CharSequence[] values = {"رمز تحقق ", "طلب موافقة المشرف"};
    android.support.v7.app.AlertDialog alertDialog2;
    String State;
    ContentValues cv;
    String Stop = "0";
    LinearLayout lyt_Share, Home_layout;
    String ServerDate, DeviceDate;
    SimpleDateFormat sdf;
    MyTextView tv_CatDesc,     tv_Allow_Amt,tv_CellingException  ;
    MyTextView tv_PromotionStatus,tv_Celing,tv_CustTotal;
    String SCR_NO ="11003";
    EditText  et_OrdeNo ;
            TextView tv_acc   ;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_orders_items);
            ComInfo.ComNo = Integer.parseInt(DB.GetValue(this, "ComanyInfo", "CompanyID", "1=1"));

            ApprovalOrdersCount = (MyTextView) findViewById(R.id.ApprovalOrdersCount);
            ApprovalOrdersCount.setText("0");


            tv_TaxStatus = (MyTextView) findViewById(R.id.tv_TaxStatus);
            CustTaxStatus = "0";
              Order_no = (TextView) findViewById(R.id.et_OrdeNo);

            tv_CustStatus = (MyTextView) findViewById(R.id.tv_CustStatus);
            tv_CustStatus.setText("");


            obj = new GetPermession();
            mNav = new SimpleSideDrawer(this);
            mNav.setLeftBehindContentView(R.layout.po_nav_menu);

            offer_groups_List = new ArrayList<Cls_Offers_Groups>();
            offer_groups_List.clear();

            offer_groups_Effict_List = new ArrayList<Cls_Offers_Groups>();
            offer_groups_Effict_List.clear();

            cls_offers_dtl_giftses = new ArrayList<Cls_Offers_Dtl_Gifts>();
            cls_offers_dtl_giftses.clear();

            cls_offers_hdrs = new ArrayList<Cls_Offers_Hdr>();
            cls_offers_hdrs.clear();


            cls_offers_hdrsNew = new ArrayList<Cls_Offers_Hdr>();
            cls_offers_hdrsNew.clear();

            lv_Items = (ListView) findViewById(R.id.LstvItems);
            sqlHandler = new SqlHandler(this);
            IsNew = true;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            UserID = sharedPreferences.getString("UserID", "");
            Img_Menu = (ImageView) findViewById(R.id.imageView13);
            TextView acc = (TextView) findViewById(R.id.tv_acc);
            //   startService(new Intent(OrdersItems.this, AutoPostTrans.class));
            GetMaxPONo();


            nf_out = NumberFormat.getNumberInstance(Locale.ENGLISH);
            nf_out.setMaximumFractionDigits(3);
            nf_out.setMinimumFractionDigits(3);


            contactList = new ArrayList<ContactListItems>();
            contactList.clear();

            showList(0);
            CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);

            chk_Cash = (CheckBox) findViewById(R.id.chk_Cash);
            chk_Cash.setChecked(false);

            chk_Cash.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            chk_Cash.setEnabled(true);
            Tax_Include.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            Tax_Include.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CalcTotal();
                    //  showList(0);

                }
            });
      /*  lv_Items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                EditText Order_no = (EditText) findViewById(R.id.et_OrdeNo);
                ContactListItems contactListItems = (ContactListItems) arg0.getItemAtPosition(arg2);
                String slno = contactListItems.getno();
                String delQuery = "DELETE FROM Po_dtl where itemno='" + slno.toString() + "' and  orderno ='" + Order_no.getText().toString() + "'";
                sqlHandler.executeQuery(delQuery);
                showList(1);

                return false;
            }
        });*/


            ImageButton back = (ImageButton) findViewById(R.id.imageButton7);

        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(OrdersItems.this, JalMasterActivity.class);
                startActivity(k);
            }
        });
*/

            TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);

              accno = (TextView) findViewById(R.id.tv_acc);
            accno.setText(sharedPreferences.getString("CustNo", ""));
            CustNm.setText(sharedPreferences.getString("CustNm", ""));
            Get_CatNo(accno.getText().toString());
            FillLocation();

            final EditText OrdeNo = (EditText) findViewById(R.id.et_OrdeNo);

            tv_CustTotal = (MyTextView) mNav.findViewById(R.id.tv_CustTotal);

            MyTextView tv_Location = (MyTextView) mNav.findViewById(R.id.tv_Location);
            tv_CatDesc = (MyTextView) mNav.findViewById(R.id.tv_CatDesc);
            tv_PromotionStatus = (MyTextView) mNav.findViewById(R.id.tv_PromotionStatus);
            tv_Celing = (MyTextView) mNav.findViewById(R.id.tv_Celing);
            tv_Allow_Amt = (MyTextView) mNav.findViewById(R.id.tv_Allow_Amt);
            tv_CellingException = (MyTextView) mNav.findViewById(R.id.tv_CellingException);



            String TaxStatus = "0";
            String Location = " ";
            State = "0";
            q = " select distinct      Tax_Status , Location,State  " +
                    " from Customers where no='" + accno.getText().toString() + "'";


            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {
                c1.moveToFirst();
                TaxStatus = c1.getString(c1.getColumnIndex("Tax_Status"));
                Location = c1.getString(c1.getColumnIndex("Location"));
                State = c1.getString(c1.getColumnIndex("State"));
                c1.close();
            }

            String PromotionFlag = DB.GetValue(this, "Customers", "PromotionFlag", "no='" + accno.getText().toString() + "'");


            String CatNo = DB.GetValue(this, "Customers", "catno", "no='" + accno.getText().toString() + "'");
            String CatDesc = DB.GetValue(this, "Categ", "catName", "catno='" + CatNo + "'");
            if (CatDesc.equalsIgnoreCase("-1")) {
                CatDesc = "";
            }
            tv_CatDesc.setText(CatDesc);
            if (PromotionFlag.equalsIgnoreCase("1")) {
                tv_PromotionStatus.setText(" مستثنى من العروض ");
            }else{
                tv_PromotionStatus.setText(" نعم ");
            }





            tv_Location.setText(Location);
            tv_TaxStatus = (MyTextView) findViewById(R.id.tv_TaxStatus);
            if (TaxStatus.equalsIgnoreCase("1")) {
                tv_TaxStatus.setText("معفي الضريبة");
                tv_TaxStatus.setVisibility(View.VISIBLE);
                CustTaxStatus = "1";
            } else {
                tv_TaxStatus.setText(" ");
                tv_TaxStatus.setVisibility(View.INVISIBLE);
                CustTaxStatus = "0";
            }


            if (State.equalsIgnoreCase("1")) {
                if (ComInfo.ComNo == 3) {
                    tv_CustStatus.setText("  حالة العميل : فعال");
                } else {
                    tv_CustStatus.setText("  حالة العميل : مفتوح");
                }

                tv_CustStatus.setTextColor(Color.GREEN);

            } else if (State.equalsIgnoreCase("2")) {
                if (ComInfo.ComNo == 3) {
                    tv_CustStatus.setText("  حالة العميل : موقوف");
                } else {
                    tv_CustStatus.setText("  حالة العميل : معلق");
                }


                tv_CustStatus.setTextColor(Color.BLUE);
            } else {

                if (ComInfo.ComNo == 3) {
                    tv_CustStatus.setText("  حالة العميل : موقوف");
                } else {
                    tv_CustStatus.setText("حالة العميل : ملغي");
                }

                tv_CustStatus.setTextColor(Color.RED);
            }


            GetCelling();


            Button btn_DeliverDate, btn_Sig, btn_GetOffer, btn_ShowOffer,btn_GetCilingExp;
            btn_GetOffer = (Button) mNav.findViewById(R.id.btn_GetOffer);
            btn_GetCilingExp = (Button) mNav.findViewById(R.id.btn_GetCilingExp);
            btn_DeliverDate = (Button) mNav.findViewById(R.id.btn_DeliverDate);
            btn_Sig = (Button) mNav.findViewById(R.id.btn_Sig);
            btn_ShowOffer = (Button) mNav.findViewById(R.id.btn_ShowOffer);
            btn_DeliverDate.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            btn_Sig.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            btn_GetOffer.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            btn_ShowOffer.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            btn_GetCilingExp.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));


            btn_ShowOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNav.toggleLeftDrawer();
                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "po");
                    bundle.putString("CatNo", "");
                    FragmentManager Manager = getFragmentManager();
                    PopShowOffers popShowOffers = new PopShowOffers();
                    popShowOffers.setArguments(bundle);
                    popShowOffers.show(Manager, null);
                }
            });


            btn_GetOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateOffers();

                }
            });

            btn_GetCilingExp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateUserExceptions();

                }
            });


            btn_DeliverDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNav.toggleLeftDrawer();
                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "po");
                    bundle.putString("OrdeNo", OrdeNo.getText().toString());
                    FragmentManager Manager = getFragmentManager();
                    PopOrderSelesDetails obj = new PopOrderSelesDetails();
                    obj.setArguments(bundle);
                    obj.show(Manager, null);

                }
            });


            btn_Sig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNav.toggleLeftDrawer();
                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "po");
                    bundle.putString("OrdeNo", OrdeNo.getText().toString());
                    FragmentManager Manager = getFragmentManager();
                    PopOrderSing obj = new PopOrderSing();
                    obj.setArguments(bundle);
                    obj.show(Manager, null);

                }
            });


            Fragment frag = new Header_Frag();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();

            tv_HowPay = (TextView) findViewById(R.id.tv_HowPay);
            String HowPay = DB.GetValue(OrdersItems.this, "Customers", "Pay_How", "no ='" + acc.getText().toString() + "' ");

            if (HowPay.equalsIgnoreCase("1")) {
                tv_HowPay.setText(" نوع العميل :" + "نقــدي");
            } else {
                tv_HowPay.setText(" نوع العميل :" + "ذمـــم");
            }


    /*    if (ComInfo.ComNo == 3) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("  طلب بيع");
                alertDialog.setMessage("هل تريد تحديث  معلومات المستودع والباتش ؟");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Do_Trans_Server_Data();

                    }
                });


            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });


            alertDialog.show();


        } else {

        }
*/

            Img_Menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Bundle bundle = new Bundle();
                bundle.putString("Scr", "po");
                bundle.putString("OrdeNo", OrdeNo.getText().toString());
                FragmentManager Manager = getFragmentManager();
                PopOrderSelesDetails obj = new PopOrderSelesDetails();
                obj.setArguments(bundle);
                obj.show(Manager, null);*/
                    GetCelling();
                    mNav.toggleLeftDrawer();
                }
            });

            if (ComInfo.ComNo == 2) {
                Tax_Include.setChecked(false);
            }
            if (ComInfo.ComNo == 4  ) {
                Tax_Include.setChecked(false);
                Tax_Include.setVisibility(View.INVISIBLE);
                chk_Cash.setVisibility(View.INVISIBLE);
            }
            if (  ComInfo.ComNo==Companies.beutyLine.getValue() ) {
                Tax_Include.setChecked(true);
                Tax_Include.setVisibility(View.INVISIBLE);
                chk_Cash.setVisibility(View.INVISIBLE);
            }
            Fill_OffeNew();
            if (ComInfo.ComNo == 1 || ComInfo.ComNo == Companies.tariget.getValue()) {
                Tax_Include.setChecked(true);

            }
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            if (ComInfo.ComNo == 3 || ComInfo.ComNo == 4) {
                if (State.equalsIgnoreCase("2")) {
                    Stop = "1";
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("طلب البيع");
                    if (ComInfo.ComNo == 3) {
                        alertDialog.setMessage("هذا العميل موقوف ، يرجى مراجعة الدائرة المالية");
                    } else {
                        alertDialog.setMessage("هذا العميل معلق ، يرجى مراجعة الدائرة المالية");

                    }
                    alertDialog.setIcon(R.drawable.error_new);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            BackInt = new Intent(OrdersItems.this, JalMasterActivity.class);
                            startActivity(BackInt);
                        }
                    });
                    alertDialog.show();
                }
            }

            if (ComInfo.ComNo == 3 || ComInfo.ComNo == 4) {
                if (State.equalsIgnoreCase("3")) {
                    alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("طلب البيع");
                    Stop = "1";

                    if (ComInfo.ComNo == 3) {
                        alertDialog.setMessage("هذا العميل موقوف ، يرجى مراجعة الدائرة المالية");
                    } else {
                        alertDialog.setMessage("هذا العميل ملغي ، يرجى مراجعة الدائرة المالية");


                    }


                    alertDialog.setIcon(R.drawable.error_new);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            BackInt = new Intent(OrdersItems.this, JalMasterActivity.class);
                            startActivity(BackInt);
                        }
                    });
                    alertDialog.show();
                }
            }

            lyt_Share = (LinearLayout) findViewById(R.id.lyt_Share);
            Home_layout = (LinearLayout) findViewById(R.id.Home_layout);


            LinearLayout.LayoutParams Home_lay_weight = (LinearLayout.LayoutParams) Home_layout.getLayoutParams();
            LinearLayout.LayoutParams Share_lay_weight = (LinearLayout.LayoutParams) lyt_Share.getLayoutParams();

            if (ComInfo.ComNo == 4  ) {
                lyt_Share.setVisibility(View.INVISIBLE);
                Home_lay_weight.weight = 2;
                Share_lay_weight.weight = 0;

                Bundle bundle = new Bundle();
                FragmentManager Manager = getFragmentManager();
                PopShowOffers_Summery obj = new PopShowOffers_Summery();
                obj.setArguments(bundle);
                obj.show(Manager, null);

            }
            ServerDate = DB.GetValue(this, "ServerDateTime", "ServerDate", "1=1");

            sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            DeviceDate = sdf.format(new Date());
            String msg = "تاريخ التابلت غير مطابق لتاريخ السيرفر ، الرجاء تحديث بيانات المؤسسة" + "\r\n";
            msg = msg + "تاريخ التابلت  : " + DeviceDate + "\r\n" + "تاريخ السيرفر : " + ServerDate;
            if ((!ServerDate.equalsIgnoreCase(DeviceDate)) && ComInfo.ComNo == 4) {

                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("طلب البيع");
                alertDialog.setCancelable(false);
                alertDialog.setMessage(msg);
                alertDialog.setIcon(R.drawable.error_new);
                alertDialog.setButton("رجـــــــوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BackInt = new Intent(OrdersItems.this, JalMasterActivity.class);
                        startActivity(BackInt);
                    }
                });
                alertDialog.show();

            }

            et_OrdeNo =(EditText)findViewById(R.id.et_OrdeNo);
            tv_acc =(TextView)findViewById(R.id.tv_acc);

            InsertLogTrans obj=new InsertLogTrans(OrdersItems.this,SCR_NO , SCR_ACTIONS.open.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }
        NotifcationSitting();

    }
    public void NotifcationSitting() {

        q = "Select count(distinct orderno) as orderno  from  Po_Hdr ";
        Cursor c1 = sqlHandler.selectQuery(q);
        ApprovalOrdersCount.setText("0");
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                ApprovalOrdersCount.setText(c1.getString(c1.getColumnIndex("orderno")));
            }
            c1.close();
        }


    }
    public void Set_Order_For_Aproval(String No, String Nm, String acc) { // FillList

    }
    private  void FillDtl(String No ){



    }
    public void Set_OrderForApproval(String No, String Nm, String acc) { // FillList
        Set_Order(  No,   Nm,   acc);



    }
    public void btn_show_Approval_Pop(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("CatNo", CatNo);
        FragmentManager Manager = getFragmentManager();
        Show_Orders_Need_Approval obj = new Show_Orders_Need_Approval();
        obj.setArguments(bundle);
        obj.show(Manager, null);


    }
    private  void GetCelling() {

    tv_CellingException.setText("لا");
    String Celing, CUST_NET_BAL,ExtraAmt ;
    ExtraAmt ="0.000 ";
    q = " select distinct  CustNo ,ifnull(  ExtraAmt,'0') as ExtraAmt ,ifnull( ExtraPrecent,'0') as ExtraPrecent from ManExceptions where ExptCat='2'  and  CustNo='" + accno.getText().toString() + "'";
    Cursor c2 = sqlHandler.selectQuery(q);
    if (c2 != null && c2.getCount() != 0) {
        c2.moveToFirst();

        ExtraAmt= c2.getString(c2.getColumnIndex("ExtraAmt"));
        c2.close();
    }


    q = " select distinct  CustNo  from ManExceptions where ExptCat='3'  and  CustNo='" + accno.getText().toString() + "'";
    Cursor c4 = sqlHandler.selectQuery(q);
    if (c4 != null && c4.getCount() != 0) {
        c4.moveToFirst();
        tv_CellingException.setText("نعم");
        c4.close();
    }


    q = " select distinct      " +
            " ifnull(Celing,0) as Celing , ifnull (CUST_NET_BAL,0) as CUST_NET_BAL  from Customers where no='" + accno.getText().toString() + "'";


    CUST_NET_BAL = "0.000";
    Cursor c1 = sqlHandler.selectQuery(q);
    if (c1 != null && c1.getCount() != 0) {
        c1.moveToFirst();
        CelingAmt = c1.getString(c1.getColumnIndex("Celing"));
        CUST_NET_BAL = c1.getString(c1.getColumnIndex("CUST_NET_BAL"));

        c1.close();
    }


    String q = "Select distinct   ifnull( sum(ifnull(s.Net_Total,0.000)),0.000)   as Amt    " +
            "  from  Po_Hdr s    where         " +
            "  s.acc='" + accno.getText() + "' and  s.posted ='-1'  and   s.orderno !='"+Order_no.getText()+"'";

    String UnpostedSales="0.000";
    Cursor c3= sqlHandler.selectQuery(q);
    if (c3 != null && c3.getCount() != 0) {
        if (c3.moveToFirst()) {
            UnpostedSales = c3.getString(c3.getColumnIndex("Amt"));
        }
        c3.close();
    }


    Double  TATOAL_CUST_NET_BAL =   SToD(UnpostedSales)  + SToD(CUST_NET_BAL) ;
    Double TotalCelling =  (SToD(ExtraAmt)  + SToD(CelingAmt))  ;
    tv_CustTotal.setText( TATOAL_CUST_NET_BAL+"");
    tv_CustTotal.setText(SToD(tv_CustTotal.getText().toString())+"");


        tv_Celing.setText(TotalCelling+"");
    tv_Celing.setText(SToD(tv_Celing.getText().toString())+"");


    tv_Allow_Amt.setText(    TotalCelling -TATOAL_CUST_NET_BAL  + "");
    tv_Allow_Amt.setText(SToD(tv_Allow_Amt.getText().toString())+"");
}
    private void UpdateUserExceptions() {


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

        final Handler _handler = new Handler();
        final ProgressDialog custDialog = new ProgressDialog(OrdersItems.this);
        custDialog.setProgressStyle(custDialog.STYLE_SPINNER);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText(" الاستثناءات");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(OrdersItems.this);
                ws.Get_ManExceptions(UserID);

                try {

                    q = "Delete from ManExceptions";
                    sqlHandler.executeQuery(q);

                    q = " delete from sqlite_sequence where name='ManExceptions'";
                    sqlHandler.executeQuery(q);

                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_CustNo = js.getJSONArray("CustNo");
                    JSONArray js_ExptCat = js.getJSONArray("ExptCat");
                    JSONArray js_Freq = js.getJSONArray("Freq");
                    JSONArray js_ExtraAmt = js.getJSONArray("ExtraAmt");
                    JSONArray js_ExtraPrecent = js.getJSONArray("ExtraPrecent");

                    for (i = 0; i < js_CustNo.length(); i++) {

                        q = "INSERT INTO ManExceptions(CustNo, ExptCat  , Freq ,ExtraAmt,ExtraPrecent) values ('"
                                + js_CustNo.get(i).toString()
                                + "','" + js_ExptCat.get(i).toString()
                                + "','" + js_Freq.get(i).toString()
                                + "','" + js_ExtraAmt.get(i).toString()
                                + "','" + js_ExtraPrecent.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();

                        }
                    }

                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    OrdersItems.this).create();
                            alertDialog.setTitle("طلب  البيع");
                            alertDialog.setMessage("تمت عملية تحديث الاستثناءات بنجاح  "   );

                            GetCelling() ;

                            alertDialog.setIcon(R.drawable.tick);

                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    View view = null;

                                }
                            });


                           alertDialog.show();


                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();

                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();
                            GetCelling();
                        }
                    });
                }
            }
        }).start();

    }
    private void UpdateOffers() {


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

        final Handler _handler = new Handler();
        final ProgressDialog custDialog = new ProgressDialog(OrdersItems.this);
        custDialog.setProgressStyle(custDialog.STYLE_SPINNER);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText(" الــعــروض");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(OrdersItems.this);
                ws.GetOffers_Hdr(UserID);

                try {

                    q = "Delete from Offers_Hdr";
                    sqlHandler.executeQuery(q);

                    q = " delete from sqlite_sequence where name='Offers_Hdr'";
                    sqlHandler.executeQuery(q);

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
                        q = "INSERT INTO Offers_Hdr(ID,Offer_No,Offer_Name,Offer_Date,Offer_Type,Offer_Status,Offer_Begin_Date,Offer_Exp_Date,Offer_Result_Type" +
                                ",Offer_Dis,Offer_Amt,TotalValue,Offer_priority,gro_no,Allaw_Repet,total_item,Gift_Items_Count,sum_Qty_item,Gift_Items_Sum_Qty,Cate_Offer,Offer_Type_Item) values ('"
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

                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();

                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();
                            Fill_OffeNew();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    OrdersItems.this).create();
                            alertDialog.setTitle("طلب  البيع");
                            alertDialog.setMessage("تمت عملية تحديث العروض بنجاح  عدد العروض هو :" + total + "");
                            q = "DELETE   FROM Offers_Hdr WHERE no NOT IN (SELECT MAX(no) FROM Offers_Hdr GROUP BY Offer_No )";
                            sqlHandler.executeQuery(q);


                            Get_Offers_Groups();
                            Get_Offers_Dtl_Gifts();

                            alertDialog.setIcon(R.drawable.tick);

                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    View view = null;

                                }
                            });


                            alertDialog.show();


                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();

                        }
                    });
                }
            }
        }).start();

    }
    private void Get_Offers_Groups() {
        q = "Delete from Offers_Groups";
        sqlHandler.executeQuery(q);

        q = "delete from sqlite_sequence where name='Offers_Groups'";
        sqlHandler.executeQuery(q);
        final Handler _handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(OrdersItems.this);
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
                        q = "  INSERT INTO Offers_Groups(ID ,grv_no , gro_name , gro_ename , gro_type , item_no , unit_no , unit_rate,qty , SerNo,gro_qty,gro_amt ) values ('"
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

                    q = " DELETE   FROM Offers_Groups WHERE no NOT IN (SELECT MAX(no) FROM Offers_Groups " +
                            " GROUP BY grv_no ,Item_No)";
                    sqlHandler.executeQuery(q);
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
                CallWebServices ws = new CallWebServices(OrdersItems.this);
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
                    q = "DELETE   FROM Offers_Dtl_Gifts WHERE no NOT IN (SELECT MAX(no) FROM Offers_Dtl_Gifts GROUP BY Trans_ID,Item_No )";
                    sqlHandler.executeQuery(q);

                } catch (final Exception e) {
                }

            }
        }).start();

    }
    private void fill_Offers_GroupNew(String grv_no) {

        offer_groups_List.clear();
        String query = " select * from Offers_Groups where (gro_type='1' or gro_type='2'  )   and grv_no ='" + grv_no + "'  ";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Offers_Groups cls_offers_groups = new Cls_Offers_Groups();
                    cls_offers_groups.setID(c1.getString(c1
                            .getColumnIndex("ID")));
                    cls_offers_groups.setGrv_no(c1.getString(c1
                            .getColumnIndex("grv_no")));
                    cls_offers_groups.setGro_ename(c1.getString(c1
                            .getColumnIndex("gro_name")));
                    cls_offers_groups.setGro_type(c1.getString(c1
                            .getColumnIndex("gro_type")));
                    cls_offers_groups.setItem_no(c1.getString(c1
                            .getColumnIndex("item_no")));
                    cls_offers_groups.setUnit_no(c1.getString(c1
                            .getColumnIndex("unit_no")));
                    cls_offers_groups.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    cls_offers_groups.setSerNo(c1.getString(c1
                            .getColumnIndex("SerNo")));

                    cls_offers_groups.setGro_qty(c1.getString(c1
                            .getColumnIndex("gro_qty")));
                    cls_offers_groups.setGro_amt(c1.getString(c1
                            .getColumnIndex("gro_amt")));
                    cls_offers_groups.setUnit_rate(c1.getString(c1
                            .getColumnIndex("unit_rate")));
                    offer_groups_List.add(cls_offers_groups);

                } while (c1.moveToNext());
            }
            c1.close(); }

    }
    private void FillLocation() {
    }
    public void GetMaxPONo() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String u = sharedPreferences.getString("UserID", "");
            u = u.trim();
            String Login = sharedPreferences.getString("Login", "No");
            if (Login.toString().equals("No")) {
                Intent i = new Intent(this, NewLoginActivity.class);
                startActivity(i);
            }

            String query = "SELECT  Distinct  COALESCE(MAX(orderno), 0) +1 AS no FROM Po_Hdr where userid ='" + UserID + "'";
            Cursor c1 = sqlHandler.selectQuery(query);
            String max = "0";
            EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
            if (c1.getCount() > 0 && c1 != null) {
                c1.moveToFirst();
                max = c1.getString(c1.getColumnIndex("no")).replaceAll("[^\\d.]", "");
                c1.close();
            }

            String max1 = "0";
            max1 = DB.GetValue(OrdersItems.this, "OrdersSitting", "SalesOrder", "1=1").replaceAll("[^\\d.]", "");

            if (max1 == "") {
                max1 = "0";
            }
            max1 = String.valueOf(Integer.parseInt(max1) + 1);
            if (SToD(max1) > SToD(max)) {
                max = max1;
            }

            if (max.length() == 1) {
                String uu = intToString(Integer.parseInt(u.toString().trim().replaceAll("[^\\d.]", "") + ""), 2);
                Maxpo.setText(uu + intToString(Integer.parseInt(max), 5));
                Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "po");
                bundle.putString("msg", "الرجاء الانتباه ، سيتم تخزين  الطلب برقم " + Maxpo.getText().toString());
                FragmentManager Manager = getFragmentManager();
                Pop_Confirm_Serial_From_Zero obj = new Pop_Confirm_Serial_From_Zero();
                obj.setArguments(bundle);
                obj.show(Manager, null);
            } else {
                Maxpo.setText(intToString(Integer.valueOf(max), 7));

            }


            Maxpo.setFocusable(false);
            Maxpo.setEnabled(false);
            Maxpo.setCursorVisible(false);

            // Maxpo.setText(max);

        }
    public static String intToString(int num, int digits) {
        try {
            String output = Integer.toString(num);
            while (output.length() < digits) output = "0" + output;
            return output;
        } catch (Exception ex) {

        }
        return num + "";
    }
    private void UpDateMaxOrderNo() {

      //  String query = "SELECT  Distinct  COALESCE(MAX( orderno ), 0)   AS no FROM Po_Hdr";
        String query = "SELECT  Distinct  COALESCE(MAX( cast(orderno as integer)), 0)   AS no FROM Po_Hdr";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();//1
        }

      //  max = (intToString(Integer.valueOf(max), 7));

        query = "Update OrdersSitting SET SalesOrder ='" + max + "'";
        sqlHandler.executeQuery(query);

        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("m4", max);
        editor.commit();*/
    }
    private void showList(Integer f) {

        lv_Items.setAdapter(null);
        float Total = 0;
        float Total_Tax = 0;
        float TTemp = 0;
        float PTemp = 0;
        float PQty = 0;
        String query = "";
        TextView etTotal, et_Tottal_Tax;
        TextView ed_date;
        etTotal = (TextView) findViewById(R.id.et_Total);
        et_Tottal_Tax = (TextView) findViewById(R.id.et_TotalTax);
        // etTotal.setText(String.valueOf(Total));
        // et_Tottal_Tax.setText(String.valueOf(Total_Tax));
        ed_date = (TextView) findViewById(R.id.ed_date);
        ContactListAdapter contactListAdapter = new ContactListAdapter(OrdersItems.this, contactList);
        lv_Items.setAdapter(contactListAdapter);
        //  json = new Gson().toJson(contactList);
    }
    private void Get_CatNo(String ACC_NO) {
        SqlHandler sqlHandler = new SqlHandler(this);
        String q = "Select  distinct ifnull( CatNo,0) as catno from Customers where no = '" + ACC_NO + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                CatNo = c1.getString(c1.getColumnIndex("catno"));
            }
            c1.close();//2
        }

    }
    public void btn_Po(View view) {

        Intent k = new Intent(this, Convert_Layout_Img.class);
        //  Intent k = new Intent(this,BluetoothConnectMenu.class);


        startActivity(k);
    }
    @Override
    public void onBackPressed() {

        BackInt = new Intent(this, JalMasterActivity.class);

        if (contactList.size() > 0) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("طلب بيع");
            alertDialog.setMessage("هل تريد الاستمرار بعملية الخروج");
            alertDialog.setIcon(R.drawable.save);
            alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    startActivity(BackInt);
                }
            });


            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();
        } else {

            startActivity(BackInt);
        }


      /*  Intent k= new Intent(this, Main2Activity.class);
        startActivity(k);*/


    }
    public void btn_back(View view) {
        BackInt = new Intent(this, JalMasterActivity.class);

        if (contactList.size() > 0) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("طلب بيع");
            alertDialog.setMessage("هل تريد الاستمرار بعملية الخروج");
            alertDialog.setIcon(R.drawable.save);
            alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(BackInt);
                }
            });


            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();
        } else {

            startActivity(BackInt);
        }


      /*  Intent k= new Intent(this, Main2Activity.class);
        startActivity(k);*/

    }
    public void Save_Method(String ItemNo, String p, String q, String t, String u, String dis, String bounce) {

        TextView OrderNo = (TextView) findViewById(R.id.et_OrdeNo);
        String bounce_unitno, bounce_qty, dis_per, dis_value;
        bounce_unitno = bounce_qty = dis_per = dis_value = "";
        String query = "INSERT INTO Po_dtl(orderno,itemno,price,qty,tax,unitNo,dis_Amy,dis_per,bounce_qty,bounce_unitno) values " +
                "('" + OrderNo.getText().toString() + "','" + ItemNo + "'," + p + "," + q + "," + t + ",'" + u + "','" + dis_value + "','" + dis_value + "','" + bounce_qty + "','" + bounce_unitno + "')";

        sqlHandler.executeQuery(query);
        showList(1);
    }
    public Double GetOrderQty(String ItemNo, String Batchno, String Expdate) {
        Double Qty = 0.0;


        for (int x = 0; x < contactList.size(); x++) {
            ContactListItems contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);

            if (contactListItems.getNo().equals(ItemNo) && contactListItems.getBatch().equals(Batchno) && contactListItems.getExpDate().equals(Expdate)) {
                Qty = Qty + (SToD(contactListItems.getQty()));
            }
        }

        return Qty;
    }

    public void Save_List(final String ItemNo, final String p, final String q, final String t, final String u, final String dis, final String bounce, final String ItemNm, final String UnitName, final String dis_Amt, final String ExpDate, final String Batch, final String Operand, final String Price_From_AB) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        int ItemExists = 0;
        for (int x = 0; x < contactList.size(); x++) {
            ContactListItems contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);

            if ((contactListItems.getNo().equals(ItemNo)) && contactListItems.getProID().equalsIgnoreCase("0")) {
                ItemExists = 1;

            }
        }

      /*  if (ItemExists == 1 && ComInfo.ComNo != 3 ) {
            alertDialog.setTitle("طلب بيع مواد");
            alertDialog.setMessage("تم ادخال هذه المادة مسبقا   ");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });


            alertDialog.show();

        }*/
        if (ItemExists == 1) {
            if (ComInfo.ComNo == 3) {
                alertDialog.setTitle("طلب بيع مواد");
                alertDialog.setMessage("تم ادخال هذه المادة مسبقا ، هل تريد الاستمرار ؟");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Save_List_Po(ItemNo, p, q, t, u, dis, bounce, ItemNm, UnitName, dis_Amt, ExpDate, Batch, Operand, Price_From_AB);
                    }
                });
                alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                alertDialog.show();
            } else if (ComInfo.ComNo == 4) {

                alertDialog.setTitle("طلب البيع");
                alertDialog.setMessage("تم ادخال هذه المادة مسبقا  ");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        } else {

            Save_List_Po(ItemNo, p.replaceAll("[^\\d.]", ""), q.replaceAll("[^\\d.]", ""), t.replaceAll("[^\\d.]", ""), u.replaceAll("[^\\d.]", ""), dis, bounce.replaceAll("[^\\d.]", ""), ItemNm, UnitName, dis_Amt.replaceAll("[^\\d.]", ""), ExpDate, Batch, Operand.replaceAll("[^\\d.]", ""), Price_From_AB.replaceAll("[^\\d.]", ""));
        }


    }

    private void Save_List_Po(String ItemNo, String p, String q, String t, String u, String dis, String bounce, String ItemNm, String UnitName, String disAm, String ExpDate, String Batch, String Operand, String Price_From_AB) {

        if (bounce.toString().equals(""))
            bounce = "0";

        if (dis.toString().equals(""))
            dis = "0";

        if (disAm.toString().equals(""))
            disAm = "0";


        String ItemInOffer = DB.GetValue(this, "Offers_Hdr inner join Offers_Groups on Offers_Groups.grv_no=  Offers_Hdr.gro_no", "Offers_Groups.grv_no", "(Offers_Groups.gro_type='1' or  Offers_Groups.gro_type='2') and  Offers_Groups.item_no like '" + ItemNo + "' and  Offers_Hdr.Cate_Offer='"+CatNo+"'");

      /*  String q = "   SELECT    *   FROM Offers_Hdr inner join Offers_Groups on Offers_Groups.grv_no=  Offers_Hdr.gro_no " +
                "      WHERE  ( Offers_Groups.gro_type='1' or  Offers_Groups.gro_type='2' )  " +
                "      and     Offer_No not in ( Select ProID  From Po_dtl where   orderno='" + pono.getText() + "' )  and  Offers_Hdr.Cate_Offer='" + CatNo + "'" +
                "      Order by CAST( Offer_priority as INTEGER)   asc   ";

        */



        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;
        Item_Total = Double.parseDouble(q.replace(",", "")) * Double.parseDouble(p.toString().replace("'", ""));
        Price = Double.parseDouble(p.replace("'", ""));
        Tax = Double.parseDouble(t.replace(",", ""));
        Item_Total = Double.parseDouble(Item_Total.toString().replace("'", ""));
        //  Tax_Amt =Double.valueOf((Tax / 100))  * Item_Total;
        // Tax_Amt =(Double.parseDouble(Tax.toString()) /100)   *  ( Double.parseDouble(Item_Total.toString().replace(",","")) -  Double.parseDouble( dis_Amt.toString().replace(",","") ));
        double TaxFactor = 0.0;
        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);
        Double DisValue = Double.parseDouble(disAm.toString().replace(",", ""));


        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        ContactListItems contactListItems = new ContactListItems();
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        if (Tax_Include.isChecked()) {
            contactListItems.setprice(String.valueOf(Price / ((Tax / 100) + 1)));
        } else {
            contactListItems.setprice(String.valueOf(Price));

        }


        contactListItems.setItemOrgPrice(String.valueOf(Price));
        contactListItems.setQty(q);
        contactListItems.setTax(String.valueOf(Tax));
        contactListItems.setUnite(u);
        contactListItems.setBounce(bounce);
        contactListItems.setDiscount(dis);
        contactListItems.setDis_Amt(disAm);
        contactListItems.setUniteNm(UnitName);
        contactListItems.setPro_amt("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_Total("0");
        contactListItems.setDisAmtFromHdr("0");
        contactListItems.setDisPerFromHdr("0");
        contactListItems.setProID("0");
        contactListItems.setTax_Amt("0");
        contactListItems.setProType("0");
        contactListItems.setTotal(String.valueOf(df.format(Item_Total)));
        contactListItems.setBatch(Batch);
        contactListItems.setExpDate(ExpDate);
        contactListItems.setOperand(Operand);
        contactListItems.setPrice_From_AB(Price_From_AB);
        contactListItems.setItemInOffer(ItemInOffer);
        contactList.add(contactListItems);

        CalcTotal();

        showList(1);
    }

    private void CalcTax() {

        Double All_Dis = 0.0;
        Double RowTotal = 0.0;
        Double NetRow = 0.0;
        Double TaxAmt = 0.0;
        Double TaxFactor = 0.0;
        Double All_Dis_Per = 0.0;
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);


        ContactListItems contactListItems;
        for (int x = 0; x < contactList.size(); x++) {
            contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            All_Dis = Double.parseDouble(contactListItems.getDis_Amt().replace(",", "")) + Double.parseDouble(contactListItems.getDisAmtFromHdr().replace(",", "")) + Double.parseDouble(contactListItems.getPro_amt().replace(",", ""));
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());

            if (Tax_Include.isChecked()) {

                contactListItems.setprice(SToD(String.valueOf((SToD(contactListItems.getItemOrgPrice())) / ((SToD(contactListItems.getTax()) / 100) + 1))).toString());

            } else {
                contactListItems.setprice(String.valueOf(SToD(contactListItems.getItemOrgPrice())));
            }

            //  contactListItems.setDis_Amt( (SToD(contactListItems.getprice()) * SToD(contactListItems.getQty()))  * (100)   );
            RowTotal = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
            TaxFactor = (Double.parseDouble(contactListItems.getTax()) / 100);
            NetRow = RowTotal - (RowTotal * (All_Dis_Per / 100));
             /*if(Tax_Include.isChecked()) {
                 TaxAmt = NetRow - ( NetRow / (TaxFactor + 1)) ;
                  TaxAmt = NetRow - ( NetRow / (TaxFactor + 1)) ;
             }
             else {
                TaxAmt = NetRow  *  TaxFactor ;
           }*/
            TaxAmt = NetRow * TaxFactor;
            contactListItems.setTax_Amt(df.format(TaxAmt).toString());
        }
        showList(0);
    }

    private void CalcTotal() {

        Double Total, Tax_Total, Dis_Amt, Po_Total;
        ContactListItems contactListItems = new ContactListItems();
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        Double All_Dis = 0.0;
        Double All_Dis_Per = 0.0;
        Total = 0.0;
        Tax_Total = 0.0;
        Dis_Amt = 0.0;
        Po_Total = 0.0;
        TextView Subtotal = (TextView) findViewById(R.id.et_Total);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);
        Double TaxVal = 0.0;
        double TaxFactor = 0.0;
        CalcTax();
        Double RowTotal = 0.0;
        Double pq = 0.0;
        Double opq = 0.0;
        Double V_NetTotal = 0.0;
        for (int x = 0; x < contactList.size(); x++) {
            contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            All_Dis = SToD(contactListItems.getDis_Amt()) + SToD(contactListItems.getDisAmtFromHdr()) + SToD(contactListItems.getPro_amt());
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());
            pq = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
            opq = SToD(contactListItems.getItemOrgPrice()) * SToD(contactListItems.getQty());

            Tax_Total = Tax_Total + (SToD(contactListItems.getTax_Amt().toString()));
            Dis_Amt = Dis_Amt + (((pq) * (All_Dis_Per / 100)));

            if (Tax_Include.isChecked()) {
                RowTotal = opq - ((opq) * (All_Dis_Per / 100));//+ SToD(contactListItems.getTax_Amt());
               /* if( All_Dis_Per > 0) {
                    Total = Total + ((opq * (All_Dis_Per / 100)) - SToD(contactListItems.getTax_Amt()) + Dis_Amt);
                }else{
                    Total = Total + ((opq ) - SToD(contactListItems.getTax_Amt()) );

                }*/


            } else {
                RowTotal = pq - ((pq) * (All_Dis_Per / 100)) + SToD(contactListItems.getTax_Amt());
                Total = Total + pq;

            }

            V_NetTotal = V_NetTotal + SToD(RowTotal.toString().replace(",", ""));

            contactListItems.setTotal((SToD(RowTotal.toString().replace(",", ""))).toString());
            All_Dis = 0.0;

        }
        Total = V_NetTotal - Tax_Total + Dis_Amt;
        TotalTax.setText(String.valueOf(df.format(Tax_Total)).replace(",", ""));
        Subtotal.setText(String.valueOf(df.format(Total)).replace(",", ""));
        dis.setText(String.valueOf(df.format(Dis_Amt)).replace(",", ""));


        Po_Total = Po_Total + ((SToD(Subtotal.getText().toString()) - SToD(dis.getText().toString())) + SToD(TotalTax.getText().toString()));

        showList(0);
        NetTotal.setText(String.valueOf(df.format(V_NetTotal)).replace(",", ""));


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

    public void btn_Search_Cust(View view) {

        FragmentManager Manager = getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.show(Manager, null);
    }

    public void Set_Cust(String No, String Nm) {
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }

    public void Set_Order(String No, String Nm, String acc) {
        No=No.replace("\u202c","").replace("\u202d","");
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView Order_no = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);

        Order_no.setText(No);
        CustNm.setText(Nm);
        accno.setText(acc);
        contactList.clear();
        chk_Cash.setEnabled(true);

        showList(0);

        sqlHandler = new SqlHandler(this);

        Tax_Include.setChecked(false);
        String query = "  select   Distinct *  from Po_Hdr  where orderno ='" + Order_no.getText().toString().replaceAll("[^\\d.]", "") + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                Total.setText(c1.getString(c1.getColumnIndex("Total")).toString());
                dis.setText(c1.getString(c1.getColumnIndex("disc_Total")).toString());
                NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")).toString());
                TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")).toString());

                if (c1.getString(c1.getColumnIndex("include_Tax")).equals("0")) {
                    Tax_Include.setChecked(true);
                }

                if (c1.getString(c1.getColumnIndex("OrderType")).equals("0")) {
                    chk_Cash.setChecked(true);
                    chk_Cash.setEnabled(false);
                }


            }

            c1.close();//3
        }

        String PromotionFlag = DB.GetValue(this, "Customers", "PromotionFlag", "no='" + accno.getText().toString() + "'");
        String CatNo = DB.GetValue(this, "Customers", "catno", "no='" + accno.getText().toString() + "'");
        String CatDesc = DB.GetValue(this, "Categ", "catName", "catno='" + CatNo + "'");
        if (CatDesc.equalsIgnoreCase("-1")) {
            CatDesc = "";
        }
        tv_CatDesc.setText(CatDesc);
        if (PromotionFlag.equalsIgnoreCase("1")) {
            tv_PromotionStatus.setText(" مستثنى من العروض ");
        }else {
            tv_PromotionStatus.setText(" نعم ");
        }



        query = "  select Distinct  pod.no,   ifnull(ItemInOffer,'-1') as ItemInOffer , Unites.UnitName, pod.OrgPrice ,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo ,pod.dis_Amt,pod.dis_per,pod.bounce_qty ,  pod.tax_Amt   , pod.total  " +
                " , pod.pro_Total ,ifnull(Pro_Type,'0')as Pro_Type ,ifnull(pod.Price_From_AB,'0')  as Price_From_AB , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt  , ifnull( pod.Opraned,1) as Opraned  ,ifnull( pod.ExpDate,'') as ExpDate  ,ifnull( pod.Batch,'') as  Batch  from Po_dtl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + Order_no.getText().toString().replaceAll("[^\\d.]", "") + "'  " +
                "  order by pod.no asc";
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


                    contactListItems.setPro_bounce(c1.getString(c1.getColumnIndex("Pro_bounce")));
                    contactListItems.setProID(c1.getString(c1.getColumnIndex("ProID")));
                    contactListItems.setPro_dis_Per(c1.getString(c1.getColumnIndex("Pro_dis_Per")));
                    contactListItems.setPro_amt(c1.getString(c1.getColumnIndex("Pro_amt")));
                    contactListItems.setPro_Total(c1.getString(c1.getColumnIndex("pro_Total")));


                    contactListItems.setDisAmtFromHdr("0");

                    contactListItems.setDisPerFromHdr("0");

                    contactListItems.setBatch(c1.getString(c1
                            .getColumnIndex("Batch")));
                    contactListItems.setProType(c1.getString(c1
                            .getColumnIndex("Pro_Type")));

                    contactListItems.setExpDate(c1.getString(c1
                            .getColumnIndex("ExpDate")));

                    contactListItems.setOperand(c1.getString(c1
                            .getColumnIndex("Opraned")));


                    contactListItems.setPrice_From_AB(c1.getString(c1
                            .getColumnIndex("Price_From_AB")));

                    contactListItems.setItemInOffer(c1.getString(c1
                            .getColumnIndex("ItemInOffer")));

                    contactList.add(contactListItems);

                } while (c1.moveToNext());

            }

            c1.close();//4
        }
        CalcTotal();
        showList(0);
        IsNew = false;
    }

    public void btn_searchCustomer(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u = sharedPreferences.getString("UserID", "");
        if (u == "-1") {
            Bundle bundle = new Bundle();
            bundle.putString("Scr", "po");
            FragmentManager Manager = getFragmentManager();
            Select_Customer obj = new Select_Customer();
            obj.setArguments(bundle);
            obj.show(Manager, null);
        }
    }

    public void btn_show_Pop(View view) {

        if (Stop.equalsIgnoreCase("1")) {
            Toast.makeText(this, "لا يمكن إدخال مواد", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ComInfo.ComNo == 3) {
            Do_Trans_Server_Data();
        }
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("CustTaxStatus", CustTaxStatus);
        bundle.putString("CatNo", CatNo);
        FragmentManager Manager = getFragmentManager();
        Pop_Po_Select_Items obj = new Pop_Po_Select_Items();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }

    public void Do_Trans_Server_Data() {

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

        final Handler _handler = new Handler();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(OrdersItems.this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(OrdersItems.this);
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("تحديث كميات المستودع");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        String MaxSeer = "1";


        final String Ser = "1";


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getApplicationContext());
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

                    if (js_docno.length() > 0) {
                        q = "Delete from ManStore";
                        sqlHandler.executeQuery(q);
                        q = "delete from sqlite_sequence where name='ManStore'";
                        sqlHandler.executeQuery(q);
                    }

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


                            custDialog.dismiss();
                            //  Do_Trans_Server_Data1();

/*


                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    OrdersItems.this).create();
                            alertDialog.setTitle("تحديث كميات المستودع");
                            alertDialog.setMessage("تمت عملية التحديث بنجاح");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
*/


                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();

                         /*   AlertDialog alertDialog = new AlertDialog.Builder(
                                    OrdersItems.this).create();
                            alertDialog.setTitle("تحديث كميات المستودع");
                            alertDialog.setMessage("مشكلة في عملية الاتصال بالسيرفر الرئيسي");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();*/
                        }
                    });
                }
            }
        }).start();
    }

    public void Do_Trans_Server_Data1() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final Handler _handler = new Handler();
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

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(OrdersItems.this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("تاريخ الصلاحية");
        progressDialog.setCustomTitle(tv);
        progressDialog.setProgressDrawable(greenProgressbar);
        progressDialog.show();
        final String UserID = sharedPreferences.getString("UserID", "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(OrdersItems.this);

                ws.TrnsferQtyFromMobileBatch("1", "1");
                try {
                    Integer i;
                    String q;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Item_No = js.getJSONArray("Item_No");
                    JSONArray js_expdate = js.getJSONArray("expdate");
                    JSONArray js_batchno = js.getJSONArray("batchno");
                    JSONArray js_net = js.getJSONArray("net");
                    if (js_Item_No.length() > 0) {
                        q = "Delete from QtyBatch";
                        sqlHandler.executeQuery(q);
                        q = " delete from sqlite_sequence where name='QtyBatch'";
                        sqlHandler.executeQuery(q);
                    }
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


                            progressDialog.dismiss();

                        }
                    });
                } catch (final Exception e) {

                    _handler.post(new Runnable() {
                        public void run() {

                        }
                    });
                    progressDialog.dismiss();

                }
            }
        }).start();

    }

    public void btn_print(View view) {


        if (!obj.CheckAction(this, SCR_NO, SCR_ACTIONS.Print.getValue())) {
            com.cds_jo.GalaxySalesApp.ViewDialog alert = new com.cds_jo.GalaxySalesApp.ViewDialog();
            alert.showDialog(OrdersItems.this, "نأسف أنت لا تملك صلاحية الطباعة", "طلب البيع");
          //  return;
        }

        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        String q = " SELECT  Distinct COALESCE (orderno, 0)  AS no FROM Po_Hdr   where orderno = '" + DocNo.getText().toString().replaceAll("[^\\d.]", "") + "'";
        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 == null || c1.getCount() == 0) {
            btn_save_po(view);
            return;
        } else {
            c1.close();//5
        }



        Intent k = new Intent(this, Convert_Layout_Img.class);
        if (ComInfo.ComNo== Companies.Saad.getValue()) {
            k = new Intent(this, Convert_Layout_Img_Tsc.class);
        }else if (ComInfo.ComNo== Companies.Ukrania.getValue()) {
            k = new Intent(this, Convert_Layout_Img_Tsc.class);
        }else if (ComInfo.ComNo== Companies.beutyLine.getValue()){
            k = new Intent(this, Convert_Layout_Img_Tsc.class);
        } else{
            k = new Intent(this, Convert_Layout_Img.class);
        }

        //  Intent k = new Intent(this,BluetoothConnectMenu.class);
        k.putExtra("Scr", "po");
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        k.putExtra("cusnm", CustNm.getText().toString().replace("\u202c","").replace("\u202d",""));
        k.putExtra("OrderNo", OrdeNo.getText().toString().replaceAll("[^\\d.]", ""));
        k.putExtra("accno", accno.getText().toString().replaceAll("[^\\d.]", ""));

        startActivity(k);

    }

    public void Save_Recod_Po() {
        Integer Seq = 0;
        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);

        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);

        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);


        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        int dayOfWeek;

        String q1 = "Select * From Po_Hdr Where orderno='" + pono.getText().toString() + "'";
        Cursor c1;
        c1 = sqlHandler.selectQuery(q1);

        if (c1 != null && c1.getCount() != 0) {
            IsNew = false;
            c1.close();//6
        } else {
            IsNew = true;
        }


        if (IsNew == true) {
            if (!obj.CheckAction(this, SCR_NO, SCR_ACTIONS.Insert.getValue())) {
                com.cds_jo.GalaxySalesApp.ViewDialog alert = new com.cds_jo.GalaxySalesApp.ViewDialog();
                alert.showDialog(OrdersItems.this, "نأسف أنت لا تملك صلاحية الحفظ", "طلب البيع");
                return;
            }
        } else {
            if (!obj.CheckAction(this, SCR_NO, SCR_ACTIONS.Modify.getValue())) {
                com.cds_jo.GalaxySalesApp.ViewDialog alert = new com.cds_jo.GalaxySalesApp.ViewDialog();
                alert.showDialog(OrdersItems.this, "نأسف أنت لا تملك صلاحية التعديل", "طلب البيع");
                return;
            }

        }


        if (IsNew == true) {
            Seq = Integer.parseInt(DB.GetValue(this, "Po_Hdr", "ifnull(Max(Seq),0)+1", "date='" + DeviceDate + "'"));

        } else {
            Seq = Integer.parseInt(DB.GetValue(this, "Po_Hdr", "ifnull(Seq,0)", "orderno='" + pono.getText().toString() + "'"));

        }


        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long i;
        ContentValues cv = new ContentValues();
        cv.put("orderno", pono.getText().toString().replace("\u202c","").replace("\u202d",""));
        cv.put("acc", acc.getText().toString().replace("\u202c","").replace("\u202d",""));
        cv.put("date", DeviceDate.replace("\u202c","").replace("\u202d",""));
        cv.put("Time", currentDateandTime.replace("\u202c","").replace("\u202d",""));
        cv.put("userid", UserID.replace("\u202c","").replace("\u202d",""));
        cv.put("Total", Total.getText().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
        cv.put("Net_Total", NetTotal.getText().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
        cv.put("Tax_Total", TotalTax.getText().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
        cv.put("bounce_Total", "0");
        cv.put("posted", "-1");
        cv.put("DayNum", dayOfWeek+"".replace("\u202c","").replace("\u202d","").replace("\u202c","").replace("\u202d",""));
        cv.put("V_OrderNo", sharedPreferences.getString("V_OrderNo", "0").replace("\u202c","").replace("\u202d",""));
        cv.put("Seq", Seq);
        if (Tax_Include.isChecked()) {
            cv.put("include_Tax", "0");
        } else {
            cv.put("include_Tax", "-1");
        }
        if (chk_Cash.isChecked()) {
            cv.put("OrderType", "0");
            cv.put("OverCelling", "0");
        } else {
            cv.put("OrderType", "-1");
            cv.put("OverCelling", "-1");
        }
        cv.put("disc_Total", dis.getText().toString());


        if (IsNew == true) {
            i = sqlHandler.Insert("Po_Hdr", null, cv);
        } else {
            i = sqlHandler.Update("Po_Hdr", cv, "orderno ='" + pono.getText().toString() + "'");
        }

        if (i > 0) {
            String q = "Delete from  Po_dtl where orderno ='" + pono.getText().toString() + "'";
            sqlHandler.executeQuery(q);


            Double Price2 = 0.0;
            Double TaxAmt = 0.0;
            for (int x = 0; x < contactList.size(); x++) {
                try {
                    ContactListItems contactListItems = new ContactListItems();
                    contactListItems = contactList.get(x);


                    if (contactListItems.getProType().equalsIgnoreCase("3")) {
                        continue;
                    }
                    cv = new ContentValues();
                    cv.put("orderno", pono.getText().toString().replace("\u202c","").replace("\u202d",""));
                    cv.put("itemno", contactListItems.getNo().replace("\u202c","").replace("\u202d",""));
                    TaxAmt = (SToD(contactListItems.getTax_Amt().toString())) / (SToD(contactListItems.getQty().toString()));
                    TaxAmt = SToD(TaxAmt + "");
                    Price2 = SToD(contactListItems.getPrice().toString()) + TaxAmt;
                    Price2 = SToD(Price2 + "");
                    if (ComInfo.ComNo == 4) {
                        cv.put("price", Price2 + "");
                    } else {
                        cv.put("price", contactListItems.getPrice().toString().replace(",", ""));
                    }

                    cv.put("qty", contactListItems.getQty().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("tax", contactListItems.getTax().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("unitNo", contactListItems.getUnite().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("dis_per", contactListItems.getDiscount().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("dis_Amt", contactListItems.getDis_Amt().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("bounce_qty", contactListItems.getBounce().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("tax_Amt", contactListItems.getTax_Amt().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("total", contactListItems.getTotal().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("ProID", contactListItems.getProID().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("Pro_bounce", contactListItems.getPro_bounce().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("Pro_dis_Per", contactListItems.getPro_dis_Per().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("Pro_amt", contactListItems.getPro_amt().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("pro_Total", contactListItems.getPro_Total().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("OrgPrice", contactListItems.getItemOrgPrice().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("Opraned", contactListItems.getOperand().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));

                    if (contactListItems.getBatch().trim().equalsIgnoreCase("")) {
                        cv.put("Batch", "0");
                    } else {
                        cv.put("Batch", contactListItems.getBatch().trim());
                    }

                    if (contactListItems.getExpDate().trim().equalsIgnoreCase("")) {
                        cv.put("ExpDate", "0");
                    } else {
                        cv.put("ExpDate", contactListItems.getExpDate().trim());
                    }
                    cv.put("ProID", "0");
                    cv.put("Pro_bounce", "0");
                    cv.put("Pro_dis_Per", "0");
                    cv.put("Pro_amt", "0");
                    cv.put("Pro_Type", contactListItems.getProType());
                    cv.put("pro_Total", "0");
                    cv.put("Price_From_AB", contactListItems.getPrice_From_AB().toString().replace(",", "").replace("\u202c","").replace("\u202d",""));
                    cv.put("ItemInOffer", contactListItems.getItemInOffer());

                    i = sqlHandler.Insert("Po_dtl", null, cv);
                    if (i < 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("  الرجاء تبليغ مسؤول النظام  للضرورة  " + contactListItems.getName().toString());
                        alertDialog.setMessage(cv.toString());
                        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        alertDialog.show();
                        try {
                            File myFile = new File("/sdcard/Android/data/Galaxy/Save_Po_dtl.txt");
                            if (!myFile.exists()) {
                                myFile.createNewFile();
                            }
                            FileOutputStream fOut = new FileOutputStream(myFile, true);
                            OutputStreamWriter myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append("\n\r" + "--------------------START-----------------------------\r\n");
                            myOutWriter.append("DateTime" + DeviceDate + "--" + currentDateandTime + "\r\n");
                            myOutWriter.append("SQL :" + cv.toString() + "\n\r");
                            myOutWriter.append("\n\r" + "--------------------END------------------------------\r\n");
                            myOutWriter.close();
                            fOut.close();
                        } catch (Exception ex) {

                        }


                        break;
                    }
                } catch (Exception ex) {
                    if (IsNew) {
                        sqlHandler.executeQuery(" Delete from  Po_Hdr where orderno ='" + pono.getText().toString() + "'");
                        sqlHandler.executeQuery(" Delete from  Po_dtl where orderno ='" + pono.getText().toString() + "'");
                        i = -1;
                    }
                }
            }

            if (i <= 0) {
                if (IsNew) {
                    sqlHandler.executeQuery(" Delete from  Po_Hdr where orderno ='" + pono.getText().toString() + "'");
                    sqlHandler.executeQuery(" Delete from  Po_dtl where orderno ='" + pono.getText().toString() + "'");
                }
            }
        }

        if (i > 0) {

            if(IsNew== true){
                InsertLogTrans obj=new InsertLogTrans(OrdersItems.this,SCR_NO , SCR_ACTIONS.Insert.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");
            }else{
                InsertLogTrans obj=new InsertLogTrans(OrdersItems.this,SCR_NO , SCR_ACTIONS.Modify.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");
            }
            // GetMaxPONo();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("طلب  البيع");
            alertDialog.setMessage("تمت عملية الحفظ بنجاح ");
            UpDateMaxOrderNo();
            DeleteAllPromotions();
            Gf_Calc_Promotion();

            IsNew = false;
            //UpDateMaxOrderNo();
            //contactList.clear();
            //showList(0);
            //custNm.setText("");
            // acc.setText("");
            //Total.setText("");
            //dis.setText("");
            // NetTotal.setText("");
            // TotalTax.setText("");


            if (ComInfo.ComNo == 4) {
                EndRound();
            }
            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    View view = null;

                    // btn_print(view);
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

    public void btn_save_po(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT Distinct *  from  Po_Hdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("طلب مبيعات");
            alertDialog.setMessage("لا يمكن التعديل على طلب البيع لقد تم ترحيل الطلب");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            c1.close();//7
            return;
        }


        if (contactList.size() == 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("طلب مبيعات");
            alertDialog.setMessage("لا يمكن تخزين طلب مبيعات بدون مواد");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();

            return;
        }

        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);

        TextView acc = (TextView) findViewById(R.id.tv_acc);
        if (custNm.getText().toString().length() == 0) {
            custNm.setError("required!");
            custNm.requestFocus();
            return;
        }

        if (pono.getText().toString().length() == 0) {
            pono.setError("required!");
            pono.requestFocus();
            return;
        }

        AlertDialog Dialog = new AlertDialog.Builder(this).create();
        if (ComInfo.ComNo == 3 || ComInfo.ComNo == 4) {
            if (State.equalsIgnoreCase("2")) {
                Dialog = new AlertDialog.Builder(this).create();
                Dialog.setTitle("طلب البيع");
                Dialog.setMessage("هذا العميل معلق ، يرجى مراجعة الدائرة المالية");
                Dialog.setIcon(R.drawable.delete);
                Dialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                Dialog.show();
                return;
            }

        }


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("اضافة مادة");
        alertDialog.setMessage("هل تريد الاستمرار بعملية الحفظ");
        alertDialog.setIcon(R.drawable.save);
        alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CheckCeling();
                // Save_Recod_Po();
            }
        });


        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialog.show();


    }

    public void CreateAlertDialogWithRadioButtonGroup(final String amt) {


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(OrdersItems.this);

        builder.setTitle("الرجاء اختيار طريقة تفعيل  الموافقة على سقف التسهيلات ");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        CheckCelingOld(amt);

                        break;
                    case 1:
                        CheckCelingNew(amt);

                        break;

                }
                alertDialog2.dismiss();
            }
        });
        alertDialog2 = builder.create();

        alertDialog2.show();

    }

    private void Get_RequestPermission() {


        TextView acc = (TextView) findViewById(R.id.tv_acc);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        ContentValues cv = new ContentValues();


        final String ManNo, CustNo, OrderNo, Desc, Type;

        ManNo = UserID;
        CustNo = acc.getText().toString();
        OrderNo = pono.getText().toString();

        Type = "2";

        greenProgressbar = this.getResources().getDrawable(R.drawable.progrees_states);
        final Handler _handler = new Handler();
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

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(OrdersItems.this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("تفعيل سقف التهسيلات  ");
        progressDialog.setCustomTitle(tv);
        progressDialog.setProgressDrawable(greenProgressbar);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(OrdersItems.this);
                ws.Get_Request_Permission(ManNo, CustNo, OrderNo, "2");
                try {
                    Integer i;
                    String q;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Status = js.getJSONArray("Status");

                    Result = js_Status.get(0).toString();


                    progressDialog.setMax(1);
                    progressDialog.incrementProgressBy(1);
                    if (progressDialog.getProgress() == progressDialog.getMax()) {

                        progressDialog.dismiss();
                    }


                    _handler.post(new Runnable() {
                        public void run() {

                            if (Result.equalsIgnoreCase("0")) {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle(" طلب الموافقة على سقف التسهيلات");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage(" الطلب تحت المعالجة");

                            } else if (Result.equalsIgnoreCase("1")) {

                                query = "Update PermissionCode set Status =1   Where   ManNo='" + ManNo + "' and CustNo ='" + CustNo + "' " +
                                        "And OrderNo ='" + OrderNo + "'  AND Type ='2'   ";
                                sqlHandler.executeQuery(query);
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle(" طلب الموافقة على سقف التسهيلات");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Save_Recod_Po();
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setMessage("تمت الموافقة على الطلب" + "    ");


                            } else if (Result.equalsIgnoreCase("2")) {
                                query = "Update PermissionCode set Status =2   Where   ManNo='" + ManNo + "' and CustNo ='" + CustNo + "' " +
                                        "And OrderNo ='" + OrderNo + "'  AND Type ='2'";
                                sqlHandler.executeQuery(query);

                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle(" طلب الموافقة على سقف التسهيلات");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage("لم تتم الموافقة");
                            }

                        }
                    });
                } catch (final Exception e) {

                    progressDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {

                        }
                    });
                }
            }
        }).start();


    }

    private void CheckCelingNew(String Amt) {
        Long i;
        final SqlHandler sql_Handler = new SqlHandler(this);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        cv = new ContentValues();
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        String t = "";
        String Order_Status = "";
        final String ManNo, CustNo, OrderNo, Desc, Type;

        String q1 = "Select     ifnull(Status,-1) as  Status  From PermissionCode Where  Type='2' AND    OrderNo='" + pono.getText().toString() + "'   Order by no desc limit 1";
        Cursor c1;
        c1 = sqlHandler.selectQuery(q1);

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            Order_Status = c1.getString(c1.getColumnIndex("Status"));
            c1.close();//8
        } else {
            Order_Status = "-1";
        }

        if (Order_Status.equalsIgnoreCase("0")) {

            //  Toast.makeText(this,"00000",Toast.LENGTH_SHORT).show();
            Get_RequestPermission();
            return;

        } else if (Order_Status.equalsIgnoreCase("1")) {
            Save_Recod_Po();
            // Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
            return;

        }


        String UserNm = DB.GetValue(this, "manf", "name", "man='" + UserID + "'");//    "SELECT  name   from  manf where man = '" + email.toString() + "' And password='" + password.toString() + "'";


        ManNo = UserID;
        CustNo = acc.getText().toString();
        OrderNo = pono.getText().toString();
        t = " اسم المندوب : ";
        t = t + UserNm;
        t = t + " اسم العميل : ";
        t = t + custNm.getText().toString();
        t = t + "  رقم طلب البيع : ";
        t = t + OrderNo;
        t = t + "    مبلغ طلب البيع : ";
        t = t + NetTotal.getText().toString();
        final String Net_Total = NetTotal.getText().toString();
        Desc = t;
        Type = "2";
        cv.put("ManNo", ManNo);
        cv.put("CustNo", CustNo);
        cv.put("OrderNo", OrderNo);
        cv.put("Status", "0");
        cv.put("Desc", Desc);
        cv.put("Type", Type);


        if (1 > 0) {


            loadingdialog = ProgressDialog.show(OrdersItems.this, "الرجاء الانتظار ...", "العمل جاري على طلب تفعيل سقف التسهيلات", true);
            loadingdialog.setCancelable(false);
            loadingdialog.setCanceledOnTouchOutside(false);
            loadingdialog.show();
            final Handler _handler = new Handler();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    CallWebServices ws = new CallWebServices(OrdersItems.this);
                    ws.SendRequestPermission(ManNo, CustNo, OrderNo, Desc, "2", Net_Total);
                    try {

                        if (We_Result.ID > 0) {
                            sqlHandler.Insert("PermissionCode", null, cv);
                            ContentValues cv = new ContentValues();
                            TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                            cv.put("Post", We_Result.ID);
                            long i;
                            // i = sql_Handler.Update("PermissionCode", cv, "OrderNo='"+ DocNo.getText().toString()+"'");

                            _handler.post(new Runnable() {
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(
                                            OrdersItems.this).create();
                                    alertDialog.setTitle("طلب تفعيل سقف التسهيلات");
                                    alertDialog.setMessage("تمت عملية طلب تفعيل سقف التسهيلات بنجاح رقم الحركة هو :" + We_Result.ID);
                                    alertDialog.setIcon(R.drawable.tick);
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {


                                        }
                                    });
                                    loadingdialog.dismiss();
                                    alertDialog.show();


                                }
                            });
                        } else {

                            loadingdialog.dismiss();
                            _handler.post(new Runnable() {
                                public void run() {
                                    AlertDialog alertDialog = new AlertDialog.Builder(
                                            OrdersItems.this).create();
                                    alertDialog.setTitle("فشل في العملية  " + "   " + We_Result.ID + "");
                                    alertDialog.setMessage(We_Result.Msg.toString());
                                    alertDialog.setIcon(R.drawable.tick);
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    alertDialog.show();
                                    alertDialog.setIcon(R.drawable.delete);
                                    alertDialog.setMessage("طلب تفعيل سقف التسهيلات لم يتم بنجاح" + "    ");
                                }
                            });
                        }

                    } catch (final Exception e) {
                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("فشل في عمليه الاتصال");
                                alertDialog.setMessage(e.getMessage().toString());
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
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

    }

    private void CheckCelingOld(String Amt) {

        CheckBox chk_Type = (CheckBox) findViewById(R.id.chk_Type);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        String q;


        String Man = DB.GetValue(OrdersItems.this, "Tab_Password", "ManNo", "PassNo = 9 AND ManNo ='" + UserID + "'");
        final String pass = DB.GetValue(OrdersItems.this, "Tab_Password", "Password", "PassNo =9 and ManNo='" + Man + "'");


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrdersItems.this);
        alertDialog.setTitle("رمز التحقق ");
        alertDialog.setMessage("لقد تجاوزت سقف التسهيلات للعميل ، سقف تسهيلات العميل هو " + Amt);

        final EditText input = new EditText(OrdersItems.this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(new PasswordTransformationMethod());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("موافق",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();

                        if (pass.equals(password)) {
                            Save_Recod_Po();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "رمز التحقق غير صحيح", Toast.LENGTH_SHORT).show();

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


    }

    private void CheckCeling() {


        CheckBox chk_Cash = (CheckBox) findViewById(R.id.chk_Cash);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        String q;


        q = " select distinct  CustNo  from ManExceptions where ExptCat='3'  and  CustNo='" + acc.getText().toString() + "'";
        Cursor c2 = sqlHandler.selectQuery(q);
        if (c2 != null && c2.getCount() != 0) {
            c2.close();
            Save_Recod_Po();
            return;
        }
        if (chk_Cash.isChecked()) {
            Save_Recod_Po();
            return;
        }

        Celing = "0";


        GetCelling();
        Double Total =  SToD(tv_Allow_Amt.getText().toString())   ;
        if (Total < SToD(NetTotal.getText().toString())) {
            Celing = "1";
        } else {
            Celing = "0";
        }

        if (Celing.equalsIgnoreCase("1")) {

            if (ComInfo.ComNo == 3) {
                chk_Cash.setChecked(true);
                chk_Cash.setEnabled(false);
                Save_Recod_Po();
            } else {
                CreateAlertDialogWithRadioButtonGroup(CelingAmt);
            }
        } else {
            Save_Recod_Po();
        }
    }

    public void btn_delete(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT Distinct *  from  Po_Hdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("طلب مبيعات");
            alertDialog.setMessage("لا يمكن التعديل على طلب البيع لقد تم ترحيل الطلب");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            c1.close();//10
            return;
        } else {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("حذف طلب بيع");
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
                    // Write your code here to invoke NO event
                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

    public void Delete_Record_PO() {

        if (!obj.CheckAction(this, SCR_NO, SCR_ACTIONS.Delete.getValue())) {
            com.cds_jo.GalaxySalesApp.ViewDialog alert = new com.cds_jo.GalaxySalesApp.ViewDialog();
            alert.showDialog(OrdersItems.this, "نأسف أنت لا تملك صلاحية الحذف", "طلب البيع");
            return;
        }

        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());


        String query = "Delete from  Po_Hdr where orderno ='" + pono.getText().toString() + "'";
        sqlHandler.executeQuery(query);


        query = "Delete from  Po_dtl where orderno ='" + pono.getText().toString() + "'";
        sqlHandler.executeQuery(query);
        contactList.clear();
        GetMaxPONo();
        showList(0);
        InsertLogTrans obj=new InsertLogTrans(OrdersItems.this,SCR_NO , SCR_ACTIONS.Delete.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");
        IsNew = true;
        /*   new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("You clicked the button!")
                .show();*/
        //custNm.setText("");
        //acc.setText("");

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("Galaxy");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
 /*
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Sweet!")
                .setContentText("Here's a custom image.")
                //.setCustomImage(R.drawable.custom_img)
                .show();*/

    }

    public void btn_Search_Orders(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        FragmentManager Manager = getFragmentManager();
        SearchPoActivity obj = new SearchPoActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }


    public void btn_share(View view) {

            InsertLogTrans insertLogTrans=new InsertLogTrans(OrdersItems.this,SCR_NO , SCR_ACTIONS.Share.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");

        if (ComInfo.ComNo == 4) {
            return;
        }
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);

        final String DocNo = pono.getText().toString();
        if (!obj.CheckAction(this, SCR_NO+"", SCR_ACTIONS.Share.getValue())) {
            com.cds_jo.GalaxySalesApp.ViewDialog alert = new com.cds_jo.GalaxySalesApp.ViewDialog();
            alert.showDialog(OrdersItems.this, "نأسف أنت لا تملك صلاحية الاعتماد", "طلب البيع");
            return;
        }
        //new ExcutePosting().execute(pono.getText().toString());


        loadingdialog = ProgressDialog.show(OrdersItems.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد طلب البيع", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                PostSalesOrder obj = new PostSalesOrder(OrdersItems.this);
                PostResult = obj.Post_Purch_Order(DocNo);
                try {

                    if (PostResult > 0) {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("اعتماد طلب بيع");
                                alertDialog.setMessage("تمت عملية اعتماد طلب البيع بنجاح" + We_Result.ID + "");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                contactList.clear();
                                showList(0);
                                GetMaxPONo();
                                if (ComInfo.ComNo == 3) {
                                    Do_Trans_Server_Data();
                                }
                                chk_Cash.setEnabled(true);
                                chk_Cash.setChecked(false);

                    }
                });

                    } else if (PostResult == -5) {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("اعتماد طلب بيع");
                                alertDialog.setMessage("لا يمكن اعتماد الطلب ، بسبب اختلاف تاريخ الطلب  ");
                                alertDialog.setIcon(R.drawable.error_new);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();

                            }
                        });

                    } else if (PostResult == -9) {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("اعتماد طلب بيع");
                                alertDialog.setMessage("لا يمكن اعتماد الطلب ، تم اعتماده سابقا  ");
                                alertDialog.setIcon(R.drawable.error_new);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();

                            }

                    });

                    } else if (PostResult == -3) {
                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID + "");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.error_new);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage(We_Result.Msg);
                            }
                        });


                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID + "");
                                alertDialog.setMessage(We_Result.Msg.toString());
                                alertDialog.setIcon(R.drawable.error_new);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alertDialog.show();

                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setMessage("عملية الاعتماد لم تتم بنجاح" + "    ");
                            }
                        });
                    } else {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("اعتماد طلب بيع");
                                alertDialog.setMessage(" فشل في عملية الاعتماد" + PostResult + "");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                            }
                        });
                    }
                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    OrdersItems.this).create();
                            alertDialog.setTitle("فشل في عمليه الاتصال");
                            alertDialog.setMessage(e.getMessage().toString());
                            alertDialog.setIcon(R.drawable.error_new);
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


        /* final SqlHandler sql_Handler = new SqlHandler(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        final TextView acc = (TextView) findViewById(R.id.tv_acc);
        final TextView Total = (TextView) findViewById(R.id.et_Total);
        final TextView dis = (TextView) findViewById(R.id.et_dis);
        final TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        final TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        final CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);
        final CheckBox chk_Cash = (CheckBox) findViewById(R.id.chk_Cash);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String OverCelling = "";
        String DeliveryDate = "";
        String Notes = "";
        final String str;
        Fill_Po_Adapter(pono.getText().toString());
        String query = "SELECT  Distinct acc, DeliveryDate, date ,Delv_day_count,OrderType,OverCelling,Notes  FROM Po_Hdr where orderno  ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String Date, Cust_No, Delv_day_count;
        String  strOrderType="-1";
        Date = Cust_No = Delv_day_count = "";

        if (c1.getCount() > 0 && c1 != null) {
            c1.moveToFirst();
            Cust_No = c1.getString(c1.getColumnIndex("acc"));
            Delv_day_count = c1.getString(c1.getColumnIndex("Delv_day_count"));
            OverCelling = c1.getString(c1.getColumnIndex("OverCelling"));
            DeliveryDate = c1.getString(c1.getColumnIndex("DeliveryDate"));
            Notes = c1.getString(c1.getColumnIndex("Notes"));
            Date = c1.getString(c1.getColumnIndex("date"));
            strOrderType = c1.getString(c1.getColumnIndex("OrderType"));
            c1.close();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Cust_No", Cust_No.toString());
            jsonObject.put("day_Count", Delv_day_count == null ? "null" : Delv_day_count.toString());
            jsonObject.put("Date", Date.toString());
            jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));
            jsonObject.put("OrderNo", pono.getText().toString());
            jsonObject.put("OrderType", strOrderType);






            jsonObject.put("OverCelling", OverCelling);
            jsonObject.put("Total", Total.getText().toString().replace(",", ""));
            jsonObject.put("Net_Total", NetTotal.getText().toString().replace(",", ""));
            jsonObject.put("Tax_Total", TotalTax.getText().toString().replace(",", ""));
            jsonObject.put("bounce_Total", "0");
            jsonObject.put("DeliveryDate", DeliveryDate);
            jsonObject.put("Notes", Notes);
            jsonObject.put("disc_Total", dis.getText().toString().replace(",", ""));



            if (Tax_Include.isChecked()) {
                jsonObject.put("include_Tax", "0");
            } else {
                jsonObject.put("include_Tax", "-1");
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }


        String json = new Gson().toJson(contactList);
        str = jsonObject.toString() + json;


        loadingdialog = ProgressDialog.show(OrdersItems.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد طلب البيع", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(OrdersItems.this);
                ws.Save_po(str, "Insert_PurshOrder");
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("posted", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("Po_Hdr", cv, "orderno='" + DocNo.getText().toString() + "'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("اعتماد طلب بيع");
                                alertDialog.setMessage("تمت عملية اعتماد طلب البيع بنجاح" + We_Result.ID + "");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                alertDialog.show();
                                contactList.clear();
                                showList(0);
                                GetMaxPONo();
                                chk_Cash.setEnabled(true);
                                chk_Cash.setChecked(false );
                               *//* custNm.setText("");
                                acc.setText("");*//*
                                Total.setText("");
                                dis.setText("");
                                NetTotal.setText("");
                                TotalTax.setText("");
                            }
                        });
                    } else if (We_Result.ID == -3) {
                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        OrdersItems.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID.toString());
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
                                        OrdersItems.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID.toString());
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
                                    OrdersItems.this).create();
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

        */
    }

    public void btn_new(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("msg", "سيتم تفريغ جميع المدخلات التي على الشاشة ، هل  تريد الاستمرار ؟");
        FragmentManager Manager = getFragmentManager();
        Pop_Confirm_New obj = new Pop_Confirm_New();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void Do_New() {
        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);


        Total.setText("0.0");
        dis.setText("0.0");
        TotalTax.setText("0.0");
        NetTotal.setText("0.0");

        IsNew = true;
        custNm.setText("");
        pono.setText("");
        acc.setText("");
        GetMaxPONo();
        contactList.clear();
        showList(0);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        acc.setText(sharedPreferences.getString("CustNo", ""));
        custNm.setText(sharedPreferences.getString("CustNm", ""));
        chk_Cash.setEnabled(true);
        chk_Cash.setChecked(false);


    }

    int position;

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Cls_Sal_InvItems contactListItems =new Cls_Sal_InvItems();

        ComInfo.ComNo = Integer.parseInt(DB.GetValue(this, "ComanyInfo", "CompanyID", "1=1"));
        if (ComInfo.ComNo != 3) {
            menu.setHeaderTitle(contactList.get(position).getName());
            menu.add(Menu.NONE, 1, Menu.NONE, "تعديل الكمية");
            menu.add(Menu.NONE, 2, Menu.NONE, "حذف المادة");
        } else {
            menu.setHeaderTitle(contactList.get(position).getName());
            menu.add(Menu.NONE, 1, Menu.NONE, "حذف المادة");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        // TODO Auto-generated method stub

        if (ComInfo.ComNo != 3) {
            switch (item.getItemId()) {
                case 1: {

                    ArrayList<ContactListItems> ContactListItems = new ArrayList<ContactListItems>();

                    ContactListItems.add(contactList.get(position));
                    if (contactList.get(position).getProType().equals("3")) {
                        break;
                    }


                    TextView accno = (TextView) findViewById(R.id.tv_acc);

                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "SalesOrder");
                    bundle.putString("Qty", contactList.get(position).getQty().toString());
                    bundle.putString("Nm", contactList.get(position).getName().toString());
                    bundle.putString("OrderNo", pono.getText().toString());

                    FragmentManager Manager = getFragmentManager();
                    Pop_Update_Qty obj = new Pop_Update_Qty();
                    obj.setArguments(bundle);
                    obj.show(Manager, null);

                }
                break;
                case 2: {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("طلب البيع");
                    alertDialog.setMessage("هل انت متاكد من عملية الحذف");
                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (contactList.get(position).getProType().equals("3")) {
                                return;
                            }
                            contactList.remove(position);
                            CalcTotal();
                            showList(0);

                        }
                    });

                    alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();


                }
                break;
            }
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("طلب البيع");
            alertDialog.setMessage("هل انت متاكد من عملية الحذف");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if (contactList.get(position).getProType().equals("3")) {
                        return;
                    }
                    try {
                        contactList.remove(position);
                        CalcTotal();
                        showList(0);
                    } catch (Exception ex) {
                        ShowEx(ex.getMessage().toString());
                    }

                }
            });

            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();


        }


        return super.onContextItemSelected(item);
    }

    private void ShowEx(String ex) {
        Toast.makeText(this, ex, Toast.LENGTH_SHORT).show();
    }

    public void btn_Delete_Item(final View view) {
        position = lv_Items.getPositionForView(view);
        registerForContextMenu(view);
        openContextMenu(view);
    }

    public void UpdateQty(String qty) {
        contactList.get(position).setQty(qty);
        CalcTotal();
        showList(1);
    }
    ///////////////////////////////////////////////////
    private void Gf_Calc_Promotion() {
        //ResetPromotion();a


        q = " DELETE   FROM Offers_Groups WHERE no NOT IN (SELECT MAX(no) FROM Offers_Groups " +
                " GROUP BY grv_no ,Item_No)";
        sqlHandler.executeQuery(q);


        q = "DELETE   FROM Offers_Hdr WHERE no NOT IN (SELECT MAX(no) FROM Offers_Hdr GROUP BY Offer_No )";
        sqlHandler.executeQuery(q);


        q = "DELETE   FROM Offers_Dtl_Gifts WHERE no NOT IN (SELECT MAX(no) FROM Offers_Dtl_Gifts GROUP BY Trans_ID,Item_No )";
        sqlHandler.executeQuery(q);

        Fill_OffeNew();
        String ItemFactor = "1";
        String GroupFactor = "1";

        String q = "";
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        Cls_Offers_Hdr cls_offers_hdr;
        String OrderQty = "0";
        String OrderAmt = "0";
        String Offers_GroupsInOrder = "0";
        String Offers_GroupsItemsCount = "0";


        for (int x = 0; x < cls_offers_hdrsNew.size(); x++) {
            cls_offers_hdr = cls_offers_hdrsNew.get(x);

            fill_Offers_GroupNew(cls_offers_hdr.getGro_no());

            checkItem = CheckGeroupQty = CheckGroupAmt = false;

            Cursor c1;


            if (offer_groups_List.size() == 0)
                continue;
            //checkItem
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (offer_groups_List.get(0).getGro_qty().equalsIgnoreCase("0")) {
                checkItem = true;
            } else if (offer_groups_List.get(0).getItem_no().equalsIgnoreCase("0")) {
                checkItem = true;
            } else {

             /*   q = "Select  ifnull(Count(1),0) as Co  From    Po_dtl   inner  Join  Offers_Groups    on  " +
                        "    Offers_Groups.item_no = Po_dtl.itemno and " +
                        "    CAST( Po_dtl.qty as INTEGER)   >=   CAST(Offers_Groups.qty as INTEGER )" +
                        "    Where grv_no ='" + cls_offers_hdr.getGro_no() + "' and Po_dtl.orderno = '" + pono.getText().toString() + "' and cast(ifnull(Po_dtl.ProID, 0 ) as INTEGER )= 0" +
                        "    and CAST(Offers_Groups.qty as INTEGER )>0 ";
*/


                q = "Select  ifnull(Count(1),0) as Co  From    Po_dtl   inner  Join  Offers_Groups    on  " +
                        "    Offers_Groups.item_no = Po_dtl.itemno and " +
                        "  ( CAST( Po_dtl.qty as decimal) *  CAST( ifnull( Po_dtl.Opraned,1) as decimal))   >=   ( CAST( Offers_Groups.qty as INTEGER) *  CAST( ifnull( Offers_Groups.unit_rate,1) as decimal))  " +
                        "    Where grv_no ='" + cls_offers_hdr.getGro_no() + "' and Po_dtl.orderno = '" + pono.getText().toString() + "' and cast(ifnull(Po_dtl.ProID, 0 ) as INTEGER )= 0" +
                        "    and CAST(Offers_Groups.qty as INTEGER )>0 ";


                c1 = sqlHandler.selectQuery(q);
                if (c1 != null && c1.getCount() != 0) {
                    c1.moveToFirst();
                    Offers_GroupsInOrder = (c1.getString(c1.getColumnIndex("Co")));
                    //  checkItem = true;
                    c1.close();
                }

                q = "    Select   ifnull(Count(1),0) as Co  From Offers_Groups  Where grv_no ='" + cls_offers_hdr.getGro_no() + "'" +
                        "    and CAST(Offers_Groups.qty as INTEGER )>0 ";


                c1 = sqlHandler.selectQuery(q);
                if (c1 != null && c1.getCount() != 0) {
                    c1.moveToFirst();
                    Offers_GroupsItemsCount = (c1.getString(c1.getColumnIndex("Co")));
                    //  checkItem = true;
                    c1.close();
                }

                if (Integer.parseInt(Offers_GroupsInOrder) >= Integer.parseInt(Offers_GroupsItemsCount)) {
                    checkItem = true;
                }

            }

            // CheckOtherWise
                    /* q = "Select  distinct  *  From  Po_dtl    inner  Join  Offers_Groups   on  " +
                            "  Offers_Groups.item_no = Po_dtl.itemno and " +
                            "  Offers_Groups.item_no = Po_dtl.itemno and CAST( Po_dtl.qty as INTEGER)  <   CAST(Offers_Groups.qty as INTEGER )" +
                            "  Where grv_no ='" + cls_offers_hdr.getGro_no() + "' and Po_dtl.orderno = '" + pono.getText().toString() + "' and cast(ifnull(Po_dtl.ProID, 0 ) as INTEGER )= 0" +
                            "  and CAST(Offers_Groups.qty as INTEGER )>0 ";
                    */
            q = "Select  distinct  *  From  Po_dtl    inner  Join  Offers_Groups   on  " +
                    "  Offers_Groups.item_no = Po_dtl.itemno and " +
                    "  Offers_Groups.item_no = Po_dtl.itemno and " +
                    " ( CAST( Po_dtl.qty as decimal) *  CAST( ifnull( Po_dtl.Opraned,1) as decimal))   <   ( CAST( Offers_Groups.qty as INTEGER) *  CAST( ifnull( Offers_Groups.unit_rate,1) as decimal))  " +
                    "  Where grv_no ='" + cls_offers_hdr.getGro_no() + "' and Po_dtl.orderno = '" + pono.getText().toString() + "' and cast(ifnull(Po_dtl.ProID, 0 ) as INTEGER )= 0" +
                    "  and CAST(Offers_Groups.qty as INTEGER )>0 ";


            c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {
                checkItem = false;
                c1.close();
            }


            if (checkItem == false)
                continue;


            ////////////////////////////////////////////////////////////////////////////////////////
            //CheckGeroupQty
          /*  String q1 = "    Select   ifnull( sum(  CAST( Po_dtl.qty as INTEGER) *   CAST( ifnull( Po_dtl.Opraned,1) as decimal)   ),0) as qty " +
                    "  From    Offers_Groups    " +
                    "  Where  grv_no ='" + cls_offers_hdr.getGro_no() +"'";

*/

            if (offer_groups_List.get(0).getGro_qty().equalsIgnoreCase("0")) {
                CheckGeroupQty = true;
            } else if (offer_groups_List.get(0).getItem_no().equalsIgnoreCase("0")) {
                CheckGeroupQty = true;
            } else {
                q = "    Select   ifnull( sum(  CAST( Po_dtl.qty as INTEGER) *   CAST( ifnull( Po_dtl.Opraned,1) as decimal)   ),0) as qty " +
                        "  From   Po_dtl inner  Join  Offers_Groups   on   Offers_Groups.item_no = Po_dtl.itemno  " +
                        "  Where  grv_no ='" + cls_offers_hdr.getGro_no() + "' and Po_dtl.orderno = '" + pono.getText().toString() + "' and cast(ifnull(Po_dtl.ProID, 0 ) as INTEGER )= 0";


                c1 = sqlHandler.selectQuery(q);
                if (c1 != null && c1.getCount() != 0) {
                    c1.moveToFirst();
                    OrderQty = (c1.getString(c1.getColumnIndex("qty")));
                    c1.close();


                    if (Integer.parseInt(OrderQty) >= Integer.parseInt(offer_groups_List.get(0).getGro_qty()) * SToD(offer_groups_List.get(0).getUnit_rate() + "")) {
                        GroupFactor = (Integer.parseInt(OrderQty) / (Integer.parseInt(offer_groups_List.get(0).getGro_qty()) * SToD(offer_groups_List.get(0).getUnit_rate() + ""))) + "";
                        CheckGeroupQty = true;
                    }

                }
            }


            if (CheckGeroupQty == false)
                continue;


            //CheckGroupAmt
            if (offer_groups_List.get(0).getGro_amt().equalsIgnoreCase("0.000")) {
                CheckGroupAmt = true;
            } else {
                q = "    Select   ifnull( sum( distinct CAST( Po_dtl.total as real)),0)  as Amt From Po_dtl    " +
                        "  Where   Po_dtl.orderno = '" + pono.getText().toString() + "' and cast(ifnull(Po_dtl.ProID, 0 ) as INTEGER )= 0";


                c1 = sqlHandler.selectQuery(q);
                if (c1 != null && c1.getCount() != 0) {
                    c1.moveToFirst();
                    OrderAmt = (c1.getString(c1.getColumnIndex("Amt")));

                    c1.close();

                    if (SToD(OrderAmt) >= SToD(offer_groups_List.get(0).getGro_amt())) {
                        CheckGroupAmt = true;

               /* if(SToD(offer_groups_List.get(0).getGro_amt())>0) {
                    GroupAmtFactor = (SToD(OrderAmt) / SToD(offer_groups_List.get(0).getGro_amt())) + "";
                    GroupAmtFactorInt = Integer.parseInt(GroupAmtFactor);
                    Toast.makeText(this,GroupAmtFactorInt+"",Toast.LENGTH_SHORT).show();
                }*/
                    }
                }
            }

            if (CheckGroupAmt == false)
                continue;


            if (checkItem == true && CheckGeroupQty == true && CheckGroupAmt == true) {
                if (cls_offers_hdr.getAllaw_Repet().equalsIgnoreCase("0")) {
                    ItemFactor = "1";
                } else {
                    q = "  Select    CAST(  ifnull(min((CAST( Po_dtl.qty as INTEGER) * CAST( ifnull(Po_dtl.Opraned,1) as decimal)) /" +
                            "           ( CAST(Offers_Groups.qty as INTEGER)* CAST( ifnull( Offers_Groups.unit_rate,1) as decimal))" +
                            "            ),0)  as INTEGER  )as f   " +
                            "  From  Offers_Groups  inner  Join Po_dtl    on  " +
                            "  Offers_Groups.item_no = Po_dtl.itemno and " +
                            "  Offers_Groups.item_no = Po_dtl.itemno and CAST( Po_dtl.qty as INTEGER)   >=   CAST(Offers_Groups.qty as INTEGER )" +
                            "  Where grv_no ='" + cls_offers_hdr.getGro_no() + "' and Po_dtl.orderno = '" + pono.getText().toString() + "' and cast(ifnull(Po_dtl.ProID, 0 ) as INTEGER )= 0" +
                            "  and CAST(Offers_Groups.qty as INTEGER )>0 ";

                    c1 = sqlHandler.selectQuery(q);
                    if (c1 != null && c1.getCount() != 0) {
                        c1.moveToFirst();
                        ItemFactor = (c1.getString(c1.getColumnIndex("f")));
                        if (ItemFactor.equalsIgnoreCase("0")) {
                            ItemFactor = GroupFactor;
                        }
                        c1.close();
                    }
                }

                Double Group_Factor = SToD(GroupFactor);
                Integer i_Group_Factor = Group_Factor.intValue();

                Double Item_Factor = SToD(ItemFactor);
                Integer i_Item_Factor = Item_Factor.intValue();

                if (i_Group_Factor < i_Item_Factor) {
                    i_Item_Factor = i_Group_Factor;
                }
                ItemFactor = i_Item_Factor + "";

                if (cls_offers_hdr.getOffer_Result_Type().equalsIgnoreCase("1")) {
                    AppledOfferType1(cls_offers_hdr.getGro_no().toString(), ItemFactor, cls_offers_hdr.getOffer_Dis().toString());
                    continue;
                }
                if (cls_offers_hdr.getOffer_Result_Type().equalsIgnoreCase("2")) {
                    AppledOfferType2(cls_offers_hdr.getGro_no().toString(), ItemFactor, cls_offers_hdr.getOffer_Amt().toString());
                    continue;
                }

                if (cls_offers_hdr.getOffer_Result_Type().equalsIgnoreCase("3")) {
                    if ((cls_offers_hdr.getTotal_item().equalsIgnoreCase(cls_offers_hdr.getGift_Items_Count()))
                            && (cls_offers_hdr.getSum_Qty_item().equalsIgnoreCase(cls_offers_hdr.getGift_Items_Sum_Qty()))) {
                        AppledOfferType3(cls_offers_hdr.getOffer_No().toString(), ItemFactor, "1");
                    } else {
                        AppledOfferType32(cls_offers_hdr.getOffer_No().toString(), ItemFactor, cls_offers_hdr.getTotal_item(), cls_offers_hdr.getSum_Qty_item());
                        break;
                    }

                }

            }


        }// End loop
    }

    private void AppledOfferType1(String Grv_no, String ItemFactor, String dis_Per) {


        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);

        String s;
        s = " Update Po_dtl Set ProID='" + Grv_no + "' ,Pro_bounce ='0'  , pro_Total ='" + dis_Per + "' ,  " +
                "    Pro_amt =   (cast(  OrgPrice as real )*   cast(  qty as integer ) * " + (SToD(dis_Per) / 100) + ") , Pro_dis_Per='" + dis_Per + "'" +
                "    Where  itemno in ( select  item_no  from Offers_Groups where grv_no ='" + Grv_no + "')  and orderno='" + pono.getText() + "'  and ProID='0'";


             if (offer_groups_List.get(0).getItem_no().equalsIgnoreCase("0")) {
            s = " Update Po_dtl Set ProID='" + Grv_no + "' ,Pro_bounce ='0'  , pro_Total ='" + dis_Per + "' ,  " +
                    "    Pro_amt =   (cast(  OrgPrice as real )*   cast(  qty as integer ) * " + (SToD(dis_Per) / 100) + ") , Pro_dis_Per='" + dis_Per + "'" +
                    "    Where       orderno='" + pono.getText() + "'  and ProID='0'";

        }


        sqlHandler.executeQuery(s);

        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView Order_no = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        Set_Order(Order_no.getText().toString(), CustNm.getText().toString(), accno.getText().toString());

    }

    private void AppledOfferType2(String Grv_no, String ItemFactor, String dis_Amt) { //AMt

        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);

        String s = "";

        String GroupItem = "0";

        String q1 = "select  item_no  from Offers_Groups where grv_no ='" + Grv_no + "' and item_no !='0'";
        Cursor c1;
        c1 = sqlHandler.selectQuery(q1);
        if (c1 != null && c1.getCount() != 0) {
            GroupItem = "1";
            c1.close();
        } else {
            GroupItem = "0";
        }

        if (GroupItem.equalsIgnoreCase("0")) {

            s = " Update Po_dtl Set ProID='" + Grv_no + "' ,Pro_bounce ='0'  , pro_Total ='" + dis_Amt + "' ,  " +
                    " Pro_amt =" + (SToD(dis_Amt) + " *" + ItemFactor) + " , Pro_dis_Per=  ROUND (( " + ItemFactor + "*" + SToD(dis_Amt) + ")   /(cast(  OrgPrice as real )*   cast(  qty as integer ) ) ,3)" +
                    " Where   orderno='" + pono.getText() + "'  and ProID='0'";

        } else {


            s = " Update Po_dtl Set ProID='" + Grv_no + "' ,Pro_bounce ='0'  , pro_Total ='" + dis_Amt + "' ,  " +
                    " Pro_amt =" + (SToD(dis_Amt) + " *" + ItemFactor) + " , Pro_dis_Per=  ROUND (( " + ItemFactor + "*" + SToD(dis_Amt) + ")   /(cast(  OrgPrice as real )*   cast(  qty as integer ) ) ,3)" +
                    " Where  itemno in ( select  item_no  from Offers_Groups where grv_no ='" + Grv_no + "')  and orderno='" + pono.getText() + "'  and ProID='0'";
        }
        sqlHandler.executeQuery(s);


        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView Order_no = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        Set_Order(Order_no.getText().toString(), CustNm.getText().toString(), accno.getText().toString());
    }

    private void AppledOfferType3(String GroupNo, String ItemFactor, String TotalItem) {
        Long i;
        TextView Order_no = (TextView) findViewById(R.id.et_OrdeNo);
        ContactListItems Inv_Obj;
        //cls_offers_dtl_giftses.clear();


        String query = " Select distinct u.UnitName    , i.Item_Name ,  odg.Item_No , odg.Unit_No , odg.QTY , odg.Unit_Rate from Offers_Dtl_Gifts  odg" +
                "    Left  join invf i on i.Item_No =  odg.Item_No   " +
                "    left  join Unites  u on u.Unitno = odg.Unit_No" +
                "    where odg.Trans_ID = '" + GroupNo + "'"; //"  where odc.Gro_Num  = '"+GroupNo+"'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {


                    ContentValues cv = new ContentValues();

                    cv.put("orderno", Order_no.getText().toString());
                    cv.put("itemno", c1.getString(c1.getColumnIndex("Item_No")));
                    cv.put("price", "0");
                    cv.put("qty", "0");
                    cv.put("tax", "0");
                    cv.put("unitNo", c1.getString(c1.getColumnIndex("Unit_No")));
                    cv.put("dis_Amt", "0");
                    cv.put("dis_per", "0");
                    cv.put("OrgPrice", "0");
                    cv.put("bounce_qty", "0");
                    cv.put("bounce_unitno", "0");
                    cv.put("tax_Amt", "0");
                    cv.put("total", "0");
                    cv.put("net_total", "0");
                    cv.put("ProID", GroupNo);
                    cv.put("Pro_bounce", String.valueOf(Integer.parseInt(ItemFactor) * Double.parseDouble(c1.getString(c1.getColumnIndex("QTY")))));
                    cv.put("Pro_dis_Per", "0");
                    cv.put("Pro_amt", "0");
                    cv.put("Pro_Type", "3");
                    cv.put("pro_Total", "0");
                    cv.put("Batch", "0");
                    cv.put("ExpDate", "0");
                    cv.put("Opraned", c1.getString(c1.getColumnIndex("Unit_Rate")));
                    i = sqlHandler.Insert("Po_dtl", null, cv);


                } while (c1.moveToNext());

            }
            c1.close();
        }
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);

        String grv_no = DB.GetValue(this, "Offers_Hdr", "gro_no", "Offer_No='" + GroupNo + "'");
        String s = " Update Po_dtl Set ProID='" + grv_no + "' ,Pro_bounce ='0'  , pro_Total ='0',  " +
                "    Pro_amt =   '0' , Pro_dis_Per='0'  Where  Pro_Type !='3' " +
                "  and  itemno in ( select  item_no  from Offers_Groups where grv_no ='" + grv_no + "')  and orderno='" + pono.getText() + "'  ";


        if (offer_groups_List.get(0).getItem_no().equalsIgnoreCase("0")) {
              s = " Update Po_dtl Set ProID='" + grv_no + "' ,Pro_bounce ='0'  , pro_Total ='0',  " +
                    "    Pro_amt =   '0' , Pro_dis_Per='0'  Where  ProID  ='0' " +
                    "    and orderno='" + pono.getText() + "'  ";

        }
        sqlHandler.executeQuery(s);



        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);

        TextView accno = (TextView) findViewById(R.id.tv_acc);
        Set_Order(Order_no.getText().toString(), CustNm.getText().toString(), accno.getText().toString());
    }

    public void AppledOfferType32(String GroupNo, final String ItemFactor, final String TotalItem, String SumQty) {

 /*String Total_Item = DB.GetValue(this, "Offers_Hdr", "total_item", "Offer_No='" + GroupNo + "'");
  String Sum_Qty = DB.GetValue(this, "Offers_Hdr", "sum_Qty_item", "Offer_No='" + GroupNo + "'");*/
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("GroupNo", GroupNo);
        bundle.putString("ItemFactor", ItemFactor);
        bundle.putString("TotalItem", TotalItem);
        bundle.putString("SumQty", SumQty);
        bundle.putString("TbItemGift_Count", "1");
        FragmentManager Manager = getFragmentManager();
        PopSelectItemFromPackage obj = new PopSelectItemFromPackage();
        obj.setArguments(bundle);
        obj.show(Manager, null);




      /*  String q = "delete from TempGifts  ";
        sqlHandler.executeQuery(q);
        builder = new AlertDialog.Builder(OrdersItems.this);
        cvv = new ContentValues();

        LayoutInflater li = LayoutInflater.from(this);
        View view = li.inflate(R.layout.listview_dialog, null);
        listView = (ListView) view.findViewById(R.id.lvAlertList);
        final int q1 = Integer.parseInt(TotalItem);
        final String GrNo = GroupNo;

        Button btn_InsertItem = (Button) view.findViewById(R.id.btn_InsertItem);

        btn_InsertItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String q = " select * from  TempGifts ";
                sqlHandler.executeQuery(q);
                Cursor c1 = sqlHandler.selectQuery(q);
                if (c1 != null && c1.getCount() != 0) {
                    if (c1.getCount() == q1) {
                        alertDialog1.dismiss();
                        AppledOfferType33(GrNo, ItemFactor, TotalItem);
                    }
                }
            }
        });
        itemList = new ArrayList<AlertChoiceItem>();


        String query = " Select distinct u.UnitName , odc.Allaw_Repet,   i.Item_Name ,  odg.Item_No , odg.Unit_No , odg.QTY , odg.Unit_Rate from Offers_Dtl_Gifts  odg" +
                " Left join invf i on i.Item_No =  odg.Item_No   " +
                " left join Unites  u on u.Unitno = odg.Unit_No" +
                " left join  Offers_Dtl_Cond odc on odc.Trans_ID =  odg.Trans_ID" +
                " where odg.Trans_ID = '" + GroupNo + "'"; //"  where odc.Gro_Num  = '"+GroupNo+"'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {

                    itemList.add(new AlertChoiceItem(false, "", 5, c1.getString(c1.getColumnIndex("Item_No")), c1.getString(c1.getColumnIndex("Item_Name")), "1"));

                } while (c1.moveToNext());

            }
            c1.close();
        }
        boolean bl[] = new boolean[itemList.size()];

        symtoms = new CharSequence[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            symtoms[i] = itemList.get(i).getItemNo() + " | " + itemList.get(i).getItemNm();
        }

        builder.setTitle(" عدد المواد المطلوب تحديدها هو : " + TotalItem);
        builder.setCancelable(false);
        builder.setMultiChoiceItems(symtoms, bl, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                // TODO Auto-generated method stub
                itemList.get(arg1).setFlg("1");
                StringTokenizer tokens = new StringTokenizer(symtoms[arg1].toString(), "|");
                String first = tokens.nextToken();// this will contain "Fruit"
                first = first.trim();
                if (arg2) {
                    cvv.put("ItemNo", first);
                    cvv.put("OfferNo", "GroupNo");
                    cvv.put("Qty", "1");
                    cvv.put("UnitNo", "");

                    SqlRes = sqlHandler.Insert("TempGifts", null, cvv);
                } else // if (selList.contains(arg1))
                {
                    String q = "delete from TempGifts where ItemNo='" + first + "'";
                    sqlHandler.executeQuery(q);
                    // if the item is already selected then remove it
                    // selList.remove(Integer.valueOf(arg1));
                }
            }
        });


        builder.setView(view);
        alertDialog1 = builder.show();*/
    }

    public void AppledOfferType33(String GroupNo, String ItemFactor, String TotalItem) {
        Long i;
        TextView Order_no = (TextView) findViewById(R.id.et_OrdeNo);
        ContactListItems Inv_Obj;


        String query = " Select distinct u.UnitName ,TempGifts.Qty as GiftsQty   , i.Item_Name ,  odg.Item_No , odg.Unit_No , odg.QTY , odg.Unit_Rate from Offers_Dtl_Gifts  odg" +
                "  inner join  TempGifts on TempGifts.ItemNo=  odg.Item_No" +
                "    Left  join invf i on i.Item_No =  odg.Item_No   " +
                "    left  join Unites  u on u.Unitno = odg.Unit_No" +
                "    where odg.Trans_ID = '" + GroupNo + "'"; //"  where odc.Gro_Num  = '"+GroupNo+"'";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {


                    ContentValues cv = new ContentValues();

                    cv.put("orderno", Order_no.getText().toString());
                    cv.put("itemno", c1.getString(c1.getColumnIndex("Item_No")));
                    cv.put("price", "0");
                    cv.put("qty", "0");
                    cv.put("tax", "0");
                    cv.put("unitNo", c1.getString(c1.getColumnIndex("Unit_No")));
                    cv.put("dis_Amt", "0");
                    cv.put("dis_per", "0");
                    cv.put("OrgPrice", "0");
                    cv.put("bounce_qty", "0");
                    cv.put("bounce_unitno", "0");
                    cv.put("tax_Amt", "0");
                    cv.put("total", "0");
                    cv.put("net_total", "0");
                    cv.put("ProID", GroupNo);
                    //     cv.put("Pro_bounce",String.valueOf( Integer.parseInt(ItemFactor)  * Double.parseDouble(c1.getString(c1.getColumnIndex("QTY")))));
                    cv.put("Pro_bounce", (c1.getString(c1.getColumnIndex("GiftsQty"))));
                    cv.put("Pro_dis_Per", "0");
                    cv.put("Pro_amt", "0");
                    cv.put("Pro_Type", "3");
                    cv.put("pro_Total", "0");
                    cv.put("Batch", "0");
                    cv.put("ExpDate", "0");
                    cv.put("Opraned", c1.getString(c1.getColumnIndex("Unit_Rate")));
                    cv.put("Price_From_AB", "0");

                    i = sqlHandler.Insert("Po_dtl", null, cv);


                } while (c1.moveToNext());

            }
            c1.close();
        }
        String grv_no = DB.GetValue(this, "Offers_Hdr", "gro_no", "Offer_No='" + GroupNo + "'");
        String s = " Update Po_dtl Set ProID='" + grv_no + "' ,Pro_bounce ='0'  , pro_Total ='0',  " +
                "    Pro_amt =   '0' , Pro_dis_Per='0'  Where  Pro_Type !='3'  AND  itemno in" +
                " ( select  item_no  from Offers_Groups where grv_no ='" + grv_no + "')  and orderno='" + Order_no.getText() + "'  ";

        if (offer_groups_List.get(0).getItem_no().equalsIgnoreCase("0")) {
            s = " Update Po_dtl Set ProID='" + grv_no + "' ,Pro_bounce ='0'  , pro_Total ='0',  " +
                    "    Pro_amt =   '0' , Pro_dis_Per='0'  Where  ProID  ='0' " +
                    "    and orderno='" + Order_no.getText() + "'  ";

        }


        sqlHandler.executeQuery(s);


        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);

        TextView accno = (TextView) findViewById(R.id.tv_acc);
        Set_Order(Order_no.getText().toString(), CustNm.getText().toString(), accno.getText().toString());

        Gf_Calc_Promotion();
    }

    private void Fill_OffeNew() {
        cls_offers_hdrsNew.clear();
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView tv_acc = (TextView) findViewById(R.id.tv_acc);

        String CatNo = DB.GetValue(this, "Customers", "catno", "no='" + tv_acc.getText().toString() + "'");
        String PromotionFlag = DB.GetValue(this, "Customers", "PromotionFlag", "no='" + tv_acc.getText().toString() + "'");

        if (PromotionFlag.equalsIgnoreCase("1")) {
            cls_offers_hdrsNew.clear();
        } else {


            String q = "   SELECT    *   FROM Offers_Hdr inner join Offers_Groups on Offers_Groups.grv_no=  Offers_Hdr.gro_no " +
                    "      WHERE   ( Offers_Groups.gro_type='1' or  Offers_Groups.gro_type='2' )  " +
                    "      and     Offer_No not in ( Select ProID  From Po_dtl where    orderno='" + pono.getText() + "' )  and  Offers_Hdr.Cate_Offer='" + CatNo + "'" +
                    "      Order by CAST( Offer_priority as INTEGER)   asc   ";


            Cursor c1 = sqlHandler.selectQuery(q);
            Cls_Offers_Hdr cls_offers_hdr;
            cls_offers_hdrsNew.clear();
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    do {
                        cls_offers_hdr = new Cls_Offers_Hdr();
                        cls_offers_hdr.setOffer_Name(c1.getString(c1.getColumnIndex("Offer_Name")));
                        cls_offers_hdr.setID(c1.getString(c1.getColumnIndex("ID")));
                        cls_offers_hdr.setOffer_No(c1.getString(c1.getColumnIndex("Offer_No")));
                        cls_offers_hdr.setOffer_Type(c1.getString(c1.getColumnIndex("Offer_Type")));
                        cls_offers_hdr.setOffer_Dis(c1.getString(c1.getColumnIndex("Offer_Dis")));
                        cls_offers_hdr.setOffer_Amt(c1.getString(c1.getColumnIndex("Offer_Amt")));
                        cls_offers_hdr.setOffer_Result_Type(c1.getString(c1.getColumnIndex("Offer_Result_Type")));
                        cls_offers_hdr.setTotalValue(c1.getString(c1.getColumnIndex("TotalValue")));
                        cls_offers_hdr.setOffer_priority(c1.getString(c1.getColumnIndex("Offer_priority")));
                        cls_offers_hdr.setGro_no(c1.getString(c1.getColumnIndex("gro_no")));
                        cls_offers_hdr.setAllaw_Repet(c1.getString(c1.getColumnIndex("Allaw_Repet")));
                        cls_offers_hdr.setTotal_item(c1.getString(c1.getColumnIndex("total_item")));
                        cls_offers_hdr.setGift_Items_Count(c1.getString(c1.getColumnIndex("Gift_Items_Count")));
                        cls_offers_hdr.setSum_Qty_item(c1.getString(c1.getColumnIndex("sum_Qty_item")));
                        cls_offers_hdr.setGift_Items_Sum_Qty(c1.getString(c1.getColumnIndex("Gift_Items_Sum_Qty")));

                        cls_offers_hdrsNew.add(cls_offers_hdr);
                    } while (c1.moveToNext());
                }
                c1.close();
            }
            //  Bundle bundle = new Bundle();
            // bundle.putParcelableArrayList("Scr", cls_offers_hdrs);
            // FragmentManager Manager =  getFragmentManager();
            /// PromotionEffict obj = new PromotionEffict();
            //  obj.setArguments(bundle);
            //  obj.show(Manager, null);

        }
    }

    public void btn_Calc_Promotion(View view) {
        Gf_Calc_Promotion();
    }

    public void btn_Delete_Promotion(View view) {
        DeleteAllPromotions();
    }

    private void DeleteAllPromotions() {

        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String s = " Update Po_dtl Set ProID='0' ,Pro_bounce ='0'  , pro_Total ='0'   " +
                " ,Pro_amt =  '0' , Pro_dis_Per='0'  Where orderno='" + pono.getText() + "'";
        sqlHandler.executeQuery(s);
        s = " Delete  from  Po_dtl  Where  ifnull(Pro_Type ,'0')='3'    and orderno='" + pono.getText() + "'";
        sqlHandler.executeQuery(s);

        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView Order_no = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        Set_Order(Order_no.getText().toString(), CustNm.getText().toString(), accno.getText().toString());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void changeLayout(String v) {


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (v.equalsIgnoreCase("0")) {
            editor.putString("Po_Select_Items_Method", "1");
        } else {
            editor.putString("Po_Select_Items_Method", "0");
        }
        editor.commit();


        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("CatNo", CatNo);
        bundle.putString("CustTaxStatus", CustTaxStatus);
        bundle.putString("Method", "0");
        FragmentManager Manager = getFragmentManager();
        Pop_Po_Select_Items obj = new Pop_Po_Select_Items();
        obj.setArguments(bundle);
        obj.show(Manager, null);


    }

    public void EndRound() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler = new SqlHandler(this);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);


        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());

        String TrDate = sdf.format(new Date());
        String EndTime = StartTime.format(new Date());


        ContentValues cv = new ContentValues();

        cv.put("Tr_Data", TrDate);
        cv.put("End_Time", EndTime);
        cv.put("Closed", "1");


        long i;
        i = sqlHandler.Update("SaleManRounds", cv, "Closed =0");

        if (i > 0) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CustNo", "");
            editor.putString("CustNm", "");
            editor.putString("CustAdd", "");
            editor.putString("V_OrderNo", "-1");
            editor.commit();
            Toast.makeText(this, "تمت عملية نهاية الجولة بنجاح", Toast.LENGTH_SHORT).show();

        }


    }
}

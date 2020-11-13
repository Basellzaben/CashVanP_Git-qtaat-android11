package com.cds_jo.GalaxySalesApp.warehouse;

import android.app.AlertDialog;
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
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.AlertChoiceItem;
import com.cds_jo.GalaxySalesApp.Cls_Offers_Dtl_Gifts;
import com.cds_jo.GalaxySalesApp.Cls_Offers_Groups;
import com.cds_jo.GalaxySalesApp.Cls_Offers_Hdr;
import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.ContactListAdapter;
import com.cds_jo.GalaxySalesApp.ContactListItems;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.GetPermession;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.NewLoginActivity;
import com.cds_jo.GalaxySalesApp.OrderApproval.Show_Orders_Need_Approval;
import com.cds_jo.GalaxySalesApp.PopOrderSelesDetails;
import com.cds_jo.GalaxySalesApp.PopOrderSing;
import com.cds_jo.GalaxySalesApp.PopSelectItemFromPackage;
import com.cds_jo.GalaxySalesApp.PopShowOffers;
import com.cds_jo.GalaxySalesApp.PopShowOffers_Summery;
import com.cds_jo.GalaxySalesApp.Pop_Confirm_New;
import com.cds_jo.GalaxySalesApp.Pop_Po_Select_Items;
import com.cds_jo.GalaxySalesApp.Pop_Update_Qty;
import com.cds_jo.GalaxySalesApp.PostTransActions.PostItempRecepit;
import com.cds_jo.GalaxySalesApp.PostTransActions.PostSalesOrder;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SCR_ACTIONS;
import com.cds_jo.GalaxySalesApp.SearchPoActivity;
import com.cds_jo.GalaxySalesApp.Select_Customer;
import com.cds_jo.GalaxySalesApp.Select_StoreNo;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.ViewDialog;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Convert_Layout_Img;
import com.cds_jo.GalaxySalesApp.assist.Convert_Layout_Img_Tsc;
import com.cds_jo.GalaxySalesApp.assist.Logtrans.InsertLogTrans;
import com.cds_jo.GalaxySalesApp.assist.Pop_Confirm_Serial_From_Zero;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;
import header.Header_Frag;
import header.SimpleSideDrawer;

public class ItemsRecepit extends FragmentActivity {

    SqlHandler sqlHandler;
    ListView lv_Items;
    long PostResult = 0;
    ArrayList<ContactListItems> contactList;
    String UserID = "";
    MyTextView ApprovalOrdersCount;
    public ProgressDialog loadingdialog;
    public String json;
    Boolean IsNew;
    String CatNo = "-1";
    public int f = 0;
    private SimpleSideDrawer mNav;
    NumberFormat nf_out;
    TextView tv,   dis,NetTotal;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    TextView  accno,Order_no;
    Intent BackInt;
    private ImageView Img_Menu;
    GetPermession obj;
    String CustTaxStatus;
    String Stop = "0";
    LinearLayout lyt_Share, Home_layout;
    String ServerDate, DeviceDate;
    SimpleDateFormat sdf;
    MyTextView tv_CatDesc,     tv_Allow_Amt,tv_CellingException  ;
    MyTextView tv_PromotionStatus,tv_Celing,tv_CustTotal;
    String SCR_NO ="11003";
    EditText  et_OrdeNo ;
    TextView tv_acc,tv_SerialNm ,tv_SerialNo,tv_VendorNo,tv_VendorNm,tv_PurchesOrder  ;
    EditText tv_PurchesOrderYear;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.items_recepit);
            ComInfo.ComNo = Integer.parseInt(DB.GetValue(this, "ComanyInfo", "CompanyID", "1=1"));

            ApprovalOrdersCount = (MyTextView) findViewById(R.id.ApprovalOrdersCount);
            ApprovalOrdersCount.setText("0");


            CustTaxStatus = "0";
              Order_no = (TextView) findViewById(R.id.et_OrdeNo);



            obj = new GetPermession();
            mNav = new SimpleSideDrawer(this);
            mNav.setLeftBehindContentView(R.layout.po_nav_menu);



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




            Tax_Include.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
            Tax_Include.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CalcTotal();
                    //  showList(0);

                }
            });


            ImageButton back = (ImageButton) findViewById(R.id.imageButton7);





              accno = (TextView) findViewById(R.id.tv_acc);
            accno.setText(sharedPreferences.getString("CustNo", ""));

            Get_CatNo(accno.getText().toString());
            FillLocation();

            final EditText OrdeNo = (EditText) findViewById(R.id.et_OrdeNo);

            tv_SerialNo = (TextView)  findViewById(R.id.tv_SerialNo);
            tv_VendorNo = (TextView)  findViewById(R.id.tv_VendorNo);
            tv_VendorNm = (TextView)  findViewById(R.id.tv_VendorNm);
            tv_SerialNm = (TextView)  findViewById(R.id.tv_SerialNm);
            tv_PurchesOrder = (TextView)  findViewById(R.id.tv_PurchesOrder);
            tv_PurchesOrderYear = (EditText)  findViewById(R.id.tv_PurchesOrderYear);
            dis = (TextView) findViewById(R.id.et_dis);
            tv_SerialNm.setText(sharedPreferences.getString("CustNm", ""));
            tv_CustTotal = (MyTextView) mNav.findViewById(R.id.tv_CustTotal);
             NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
            MyTextView tv_Location = (MyTextView) mNav.findViewById(R.id.tv_Location);
            tv_CatDesc = (MyTextView) mNav.findViewById(R.id.tv_CatDesc);
            tv_PromotionStatus = (MyTextView) mNav.findViewById(R.id.tv_PromotionStatus);
            tv_Celing = (MyTextView) mNav.findViewById(R.id.tv_Celing);
            tv_Allow_Amt = (MyTextView) mNav.findViewById(R.id.tv_Allow_Amt);
            tv_CellingException = (MyTextView) mNav.findViewById(R.id.tv_CellingException);




            String Location = " ";



            tv_Location.setText(Location);









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


            String HowPay = DB.GetValue(ItemsRecepit.this, "Customers", "Pay_How", "no ='" + acc.getText().toString() + "' ");




            Img_Menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mNav.toggleLeftDrawer();
                }
            });





            AlertDialog alertDialog = new AlertDialog.Builder(this).create();



            lyt_Share = (LinearLayout) findViewById(R.id.lyt_Share);
            Home_layout = (LinearLayout) findViewById(R.id.Home_layout);


            LinearLayout.LayoutParams Home_lay_weight = (LinearLayout.LayoutParams) Home_layout.getLayoutParams();
            LinearLayout.LayoutParams Share_lay_weight = (LinearLayout.LayoutParams) lyt_Share.getLayoutParams();


            ServerDate = DB.GetValue(this, "ServerDateTime", "ServerDate", "1=1");

            sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            DeviceDate = sdf.format(new Date());


            et_OrdeNo =(EditText)findViewById(R.id.et_OrdeNo);
            tv_acc =(TextView)findViewById(R.id.tv_acc);

            InsertLogTrans obj=new InsertLogTrans(ItemsRecepit.this,SCR_NO , SCR_ACTIONS.open.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }


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

            String query = "SELECT  Distinct  COALESCE(MAX(orderno), 0) +1 AS no FROM ItemsReceipthdr where userid ='" + UserID + "'";
            Cursor c1 = sqlHandler.selectQuery(query);
            String max = "0";
            EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
            if (c1.getCount() > 0 && c1 != null) {
                c1.moveToFirst();
                max = c1.getString(c1.getColumnIndex("no")).replaceAll("[^\\d.]", "");
                c1.close();
            }

            String max1 = "0";
            max1 = DB.GetValue(ItemsRecepit.this, "OrdersSitting", "ItemsRecepitSerial", "1=1");

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
                bundle.putString("msg", "الرجاء الانتباه ، سيتم تخزين  المستند برقم " + Maxpo.getText().toString());
                FragmentManager Manager = getFragmentManager();
                Pop_Confirm_Serial_From_Zero obj = new Pop_Confirm_Serial_From_Zero();
                obj.setArguments(bundle);
                //obj.show(Manager, null);
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


        String query = "SELECT  Distinct  COALESCE(MAX( cast(orderno as integer)), 0)   AS no FROM ItemsReceipthdr";
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
        ItemRecepitListAdapter contactListAdapter = new ItemRecepitListAdapter(ItemsRecepit.this, contactList);
        lv_Items.setAdapter(contactListAdapter);

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
        startActivity(BackInt);





    }
    public void btn_back(View view) {
        BackInt = new Intent(this, JalMasterActivity.class);

        if (contactList.size() > 0) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("سند استلام المواد");
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
        String query = "INSERT INTO ItemsReceiptdtl(orderno,itemno,price,qty,tax,unitNo,dis_Amy,dis_per,bounce_qty,bounce_unitno) values " +
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
    public void Save_List(final String ItemNo, final String p, final String q, final String t, final String u, final String dis, final String bounce, final String ItemNm, final String UnitName, final String dis_Amt, final String ExpDate, final String Batch, final String Operand, final String Price_From_AB,String StoreNo, String StoreNm) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        int ItemExists = 0;
        for (int x = 0; x < contactList.size(); x++) {
            ContactListItems contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);

            if ((contactListItems.getNo().equals(ItemNo)) && contactListItems.getProID().equalsIgnoreCase("0")) {
                ItemExists = 1;

            }
        }


        if (ItemExists == 1) {
                alertDialog.setTitle("سند إستلام المواد");
                alertDialog.setMessage("تم ادخال هذه المادة مسبقا  ");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();

        } else {

            Save_List_Po(ItemNo, p , q , t , u , dis, bounce , ItemNm, UnitName, dis_Amt , ExpDate, Batch, Operand , Price_From_AB ,StoreNo,StoreNm);
        }


    }
    private void Save_List_Po(String ItemNo, String p, String q, String t, String u, String dis, String bounce, String ItemNm, String UnitName, String disAm, String ExpDate, String Batch, String Operand, String Price_From_AB ,String StroreNo,String StoreNm) {

        if (bounce.toString().equals(""))
            bounce = "0";

        if (dis.toString().equals(""))
            dis = "0";

        if (disAm.toString().equals(""))
            disAm = "0";




        Double Item_Total, Price, Tax;
        Item_Total = Double.parseDouble(q.replace(",", "")) * Double.parseDouble(p.toString().replace("'", ""));
        Price = Double.parseDouble(p.replace("'", ""));
        Tax = Double.parseDouble(t.replace(",", ""));
        Item_Total = Double.parseDouble(Item_Total.toString().replace("'", ""));

        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);



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
        contactListItems.setItemInOffer("0");
        contactListItems.setStoreNo(StroreNo);
        contactListItems.setStoreNm(StoreNm);
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

        Double Total,  Po_Total;
        ContactListItems contactListItems = new ContactListItems();
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        Total = 0.0;



        TextView Subtotal = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);

         Double pq = 0.0;


        for (int x = 0; x < contactList.size(); x++) {
            contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            pq = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
            Total = Total + pq;
            contactListItems.setTotal((SToD(Total.toString().replace(",", ""))).toString());

        }
        Subtotal.setText(Total+"");

        Po_Total =    (SToD(Subtotal.getText().toString()) - SToD(dis.getText().toString())) ;
        NetTotal.setText(Po_Total.toString().replace(",", ""));
        showList(0);



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

        TextView acc = (TextView) findViewById(R.id.tv_acc);
        acc.setText(No);
        tv_SerialNm.setText(Nm);
        tv_SerialNm.setError(null);
    }
    public void Set_Order(String No, String Nm, String acc) {


        TextView Order_no = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);


        Order_no.setText(No);


        contactList.clear();


        showList(0);

        sqlHandler = new SqlHandler(this);


        String query = "  select   Distinct vendor_nm,vendor_no , purches_serial_no,ifnull(purches_year_no,'') as purches_year_no ,purches_serial_nm ,purches_order_no, Total,ifnull(hdr_dis_per,'') as hdr_dis_per ,hdr_dis_value , Net_Total from ItemsReceipthdr  where orderno ='" + Order_no.getText().toString().replaceAll("[^\\d.]", "") + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                Total.setText(c1.getString(c1.getColumnIndex("Total")).toString());
                dis.setText(c1.getString(c1.getColumnIndex("hdr_dis_value")).toString());
                NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")).toString());
                tv_SerialNo.setText(c1.getString(c1.getColumnIndex("purches_serial_no")).toString());
                tv_SerialNm.setText(c1.getString(c1.getColumnIndex("purches_serial_nm")).toString());
                tv_PurchesOrder.setText(c1.getString(c1.getColumnIndex("purches_order_no")).toString());
                tv_PurchesOrderYear.setText(c1.getString(c1.getColumnIndex("purches_year_no")).toString());
                tv_VendorNm.setText(c1.getString(c1.getColumnIndex("vendor_nm")).toString());
                tv_VendorNo.setText(c1.getString(c1.getColumnIndex("vendor_no")).toString());



                TotalTax.setText("0");




            }

            c1.close();
        }

        query = "   select Distinct stores.sname as store_nm, pod.no,pod.store_no, Unites.UnitName, pod.OrgPrice ,  invf.Item_Name, pod.itemno ,pod.qty,pod.tax ,pod.unitNo ,pod.dis_per,pod.bounce_qty ,  pod.tax_Amt   , pod.total  " +
                " , ifnull(pod.Price_From_AB,'0')  as Price_From_AB  , ifnull( pod.Opraned,1) as Opraned     " +
                "   from ItemsReceiptdtl pod left join invf on invf.Item_No =  pod.itemno  " +
                "    left join stores on stores.sno=  pod.store_no   left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + Order_no.getText().toString()  + "'  " +
                "  order by pod.no asc";
        c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName(c1.getString(c1.getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1.getColumnIndex("OrgPrice")));
                    contactListItems.setItemOrgPrice(c1.getString(c1.getColumnIndex("OrgPrice")));
                    contactListItems.setQty(c1.getString(c1.getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1.getColumnIndex("tax")));
                    contactListItems.setUniteNm(c1.getString(c1.getColumnIndex("UnitName")));
                    contactListItems.setBounce(c1.getString(c1.getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1.getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt("0");

                    contactListItems.setUnite(c1.getString(c1.getColumnIndex("unitNo")));
                    contactListItems.setTax_Amt(c1.getString(c1.getColumnIndex("tax_Amt")));
                    contactListItems.setTotal(c1.getString(c1.getColumnIndex("total")));
                    contactListItems.setDisAmtFromHdr("0");
                    contactListItems.setDisPerFromHdr("0");
                    contactListItems.setBatch("");
                    contactListItems.setProType("");
                    contactListItems.setExpDate("");
                    contactListItems.setOperand(c1.getString(c1.getColumnIndex("Opraned")));
                    contactListItems.setPrice_From_AB(c1.getString(c1.getColumnIndex("Price_From_AB")));
                    contactListItems.setStoreNo(c1.getString(c1.getColumnIndex("store_no")));
                    contactListItems.setStoreNm(c1.getString(c1.getColumnIndex("store_nm")));
                    contactListItems.setItemInOffer("");
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

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("CustTaxStatus", CustTaxStatus);
        bundle.putString("CatNo", CatNo);
        FragmentManager Manager = getFragmentManager();
        Pop_ItemRecepit_Select_Items obj = new Pop_ItemRecepit_Select_Items();
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ItemsRecepit.this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(ItemsRecepit.this);
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
    public void btn_print(View view) {


        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
        String q = " SELECT  Distinct COALESCE (orderno, 0)  AS no FROM " +
                "  ItemsReceipthdr   where orderno = '" + DocNo.getText().toString()  + "'";
        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 == null || c1.getCount() == 0) {
            btn_save_po(view);
            return;
        } else {
            c1.close();
        }




        Intent k = new Intent(this, PrintItemRecepit.class);
        k.putExtra("Scr", "po");
        TextView OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        k.putExtra("cusnm", tv_SerialNm.getText().toString());
        k.putExtra("OrderNo", OrdeNo.getText().toString());
        k.putExtra("accno", accno.getText().toString());
        startActivity(k);

    }
    public void Save_Recod_Po() {
        Integer Seq = 0;

        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);

        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);



        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        String q1 = "Select * From ItemsReceipthdr Where orderno='" + pono.getText().toString() + "'";
        Cursor c1;
        c1 = sqlHandler.selectQuery(q1);

        if (c1 != null && c1.getCount() != 0) {
            IsNew = false;
            c1.close();//6
        } else {
            IsNew = true;
        }

        if (IsNew == true) {
            Seq = Integer.parseInt(DB.GetValue(this, "ItemsReceipthdr", "ifnull(Max(Seq),0)+1", "date='" + DeviceDate + "'"));

        } else {
            Seq = Integer.parseInt(DB.GetValue(this, "Po_Hdr", "ifnull(Seq,0)", "orderno='" + pono.getText().toString() + "'"));

        }


        Calendar c = Calendar.getInstance();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long i;
        ContentValues cv = new ContentValues();
        cv.put("orderno", pono.getText().toString());

        cv.put("date", DeviceDate);
        cv.put("Time", currentDateandTime);
        cv.put("userid", UserID);
        cv.put("Total", Total.getText().toString());
        cv.put("Net_Total", NetTotal.getText().toString());
        cv.put("Tax_Total", "0.000");
        cv.put("posted", "-1");
        cv.put("Seq", Seq);
        cv.put("vendor_no", tv_VendorNo.getText().toString());
        cv.put("vendor_nm", tv_VendorNm.getText().toString());
        cv.put("purches_serial_no", tv_SerialNo.getText().toString());
        cv.put("purches_serial_nm", tv_SerialNm.getText().toString());
        cv.put("purches_order_no", tv_PurchesOrder.getText().toString());
        cv.put("purches_year_no", tv_PurchesOrderYear.getText().toString());
        cv.put("Notes","");
        cv.put("hdr_dis_per", "0");
        cv.put("hdr_dis_value", dis.getText().toString());

        if (IsNew == true) {
            i = sqlHandler.Insert("ItemsReceipthdr", null, cv);
        } else {
            i = sqlHandler.Update("ItemsReceipthdr", cv, "orderno ='" + pono.getText().toString() + "'");
        }

        if (i > 0) {
            String q = "Delete from  ItemsReceiptdtl where orderno ='" + pono.getText().toString() + "'";
            sqlHandler.executeQuery(q);


            Double Price2 = 0.0;
            Double TaxAmt = 0.0;
            for (int x = 0; x < contactList.size(); x++) {
                try {
                    ContactListItems contactListItems = new ContactListItems();
                    contactListItems = contactList.get(x);


                    cv = new ContentValues();
                    cv.put("orderno", pono.getText().toString() );
                    cv.put("itemno", contactListItems.getNo() );
                    cv.put("Price_From_AB", contactListItems.getPrice().toString() );
                    cv.put("qty", contactListItems.getQty().toString() );
                    cv.put("tax",  "0");
                    cv.put("unitNo", contactListItems.getUnite().toString() );
                    cv.put("dis_per", "0");
                    cv.put("bounce_qty", contactListItems.getBounce().toString()  );
                    cv.put("tax_Amt",  "0");
                    cv.put("total", contactListItems.getTotal().toString()  );
                    cv.put("OrgPrice", contactListItems.getItemOrgPrice().toString() );
                    cv.put("Opraned", contactListItems.getOperand().toString()  );
                    cv.put("Net_Total", NetTotal.getText().toString());
                    cv.put("store_no", contactListItems.getStoreNo().toString());
                    cv.put("store_nm", contactListItems.getStoreNm().toString());

                    i = sqlHandler.Insert("ItemsReceiptdtl", null, cv);
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
                        sqlHandler.executeQuery(" Delete from  ItemsReceipthdr where orderno ='" + pono.getText().toString() + "'");
                        sqlHandler.executeQuery(" Delete from  ItemsReceiptdtl where orderno ='" + pono.getText().toString() + "'");
                        i = -1;
                    }
                }
            }

            if (i <= 0) {
                if (IsNew) {
                    sqlHandler.executeQuery(" Delete from  ItemsReceipthdr where orderno ='" + pono.getText().toString() + "'");
                    sqlHandler.executeQuery(" Delete from  ItemsReceiptdtl where orderno ='" + pono.getText().toString() + "'");
                }
            }
        }

        if (i > 0) {

            if(IsNew== true){
                InsertLogTrans obj=new InsertLogTrans(ItemsRecepit.this,SCR_NO , SCR_ACTIONS.Insert.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");
            }else{
                InsertLogTrans obj=new InsertLogTrans(ItemsRecepit.this,SCR_NO , SCR_ACTIONS.Modify.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");
            }
            // GetMaxPONo();
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("استلام المواد");
            alertDialog.setMessage("تمت عملية الحفظ بنجاح ");
            UpDateMaxOrderNo();


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
        String q = "SELECT Distinct *  from  ItemsReceipthdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("إستلام المواد");
            alertDialog.setMessage("لا يمكن التعديل على المستند   تم ترحيلة");            // Setting Icon to Dialog
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
            alertDialog.setTitle("إستلام المواد");
            alertDialog.setMessage("لا يمكن تخزين استلام المواد بدون مواد");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();

            return;
        }

        tv_VendorNo.setError(null);


        if (tv_VendorNo.getText().toString().length() == 0) {
            tv_VendorNo.setError("required!");
            tv_VendorNo.requestFocus();
            return;
        }

        if (pono.getText().toString().length() == 0) {
            pono.setError("required!");
            pono.requestFocus();
            return;
        }

        AlertDialog Dialog = new AlertDialog.Builder(this).create();



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("إستلام المواد");
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
    public void btn_delete(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT Distinct *  from  ItemsReceipthdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("سند استلام المواد");
            alertDialog.setMessage("المستند مرحل لا يمكن حذفه ");            // Setting Icon to Dialog
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
            alertDialog.setTitle("سند استلام المواد");
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




        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());


        String query = "Delete from  ItemsReceipthdr where orderno ='" + pono.getText().toString() + "'";
        sqlHandler.executeQuery(query);


        query = "Delete from  ItemsReceiptdtl where orderno ='" + pono.getText().toString() + "'";
        sqlHandler.executeQuery(query);
        contactList.clear();
        GetMaxPONo();
        showList(0);
        InsertLogTrans obj=new InsertLogTrans(ItemsRecepit.this,SCR_NO , SCR_ACTIONS.Delete.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");
        IsNew = true;
        /*   new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("You clicked the button!")
                .show();*/
        //custNm.setText("");
        //acc.setText("");

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("سند استلام مواد");
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
        SearchItemsRecepitOrders obj = new SearchItemsRecepitOrders();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_share(View view) {

            InsertLogTrans insertLogTrans=new InsertLogTrans(ItemsRecepit.this,SCR_NO , SCR_ACTIONS.Share.getValue(),et_OrdeNo.getText().toString(),tv_acc.getText().toString(),"");


        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);

        final String DocNo = pono.getText().toString();



        loadingdialog = ProgressDialog.show(ItemsRecepit.this, "الرجاء الانتظار ...", "العمل جاري على اعتماد سند الإستلام", true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();



        new Thread(new Runnable() {
            @Override
            public void run() {
                PostItempRecepit obj = new PostItempRecepit(ItemsRecepit.this);
                PostResult = obj.Post_Item_Recepit_Order(DocNo);
                try {

                    if (PostResult > 0) {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        ItemsRecepit.this).create();
                                alertDialog.setTitle("اعتماد سند الإستلام");
                                alertDialog.setMessage("تمت عملية اعتماد المستند"  );
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("رجوع", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();
                                contactList.clear();
                                Do_New();
                                GetMaxPONo();


                            }
                        });

                    }  else if (PostResult == -9) {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        ItemsRecepit.this).create();
                                alertDialog.setTitle("اعتماد سند الإستلام");
                                alertDialog.setMessage("لا يمكن اعتماد المستند ، تم اعتماده سابقا  ");
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
                                        ItemsRecepit.this).create();
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
                                        ItemsRecepit.this).create();
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
                                        ItemsRecepit.this).create();
                                alertDialog.setTitle("اعتماد سند الإستلام");
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
                                    ItemsRecepit.this).create();
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


    }
    public void btn_new(View view) {
        Do_New();
    }
    public void Do_New() {

        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);



        Total.setText("0.0");
        dis.setText("0.0");
        TotalTax.setText("0.0");
        NetTotal.setText("0.0");
        tv_PurchesOrder.setText("");
        tv_SerialNo.setText("");
        tv_SerialNm.setText("");
        tv_VendorNm.setText("");
        IsNew = true;
        pono.setText("");
        acc.setText("");
        GetMaxPONo();
        contactList.clear();
        showList(0);



    }
    int position;
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(contactList.get(position).getName());
        menu.add(Menu.NONE, 1, Menu.NONE, "حذف المادة");
        menu.add(Menu.NONE, 2, Menu.NONE, "تعديل الكمية");
        menu.add(Menu.NONE, 3, Menu.NONE, "تعديل البونص");
        menu.add(Menu.NONE, 4, Menu.NONE, "تعديل السعر");
        menu.add(Menu.NONE, 5, Menu.NONE, "تعديل المستودع");

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);



            switch (item.getItemId()) {
                case 1:{

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("سند إستلام المواد");
                    alertDialog.setMessage("هل انت متاكد من عملية الحذف");
                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
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

                }break;
                case 2: {

                    ArrayList<ContactListItems> ContactListItems = new ArrayList<ContactListItems>();
                    ContactListItems.add(contactList.get(position));
                    TextView accno = (TextView) findViewById(R.id.tv_acc);
                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "ItemRecepitQty");
                    bundle.putString("Qty", contactList.get(position).getQty().toString());
                    bundle.putString("Nm", contactList.get(position).getName().toString());
                    bundle.putString("OrderNo", pono.getText().toString());
                    FragmentManager Manager = getFragmentManager();
                    Pop_Update_Qty obj = new Pop_Update_Qty();
                    obj.setArguments(bundle);
                    obj.show(Manager, null);

                }

                break;
                case 3: {

                    ArrayList<ContactListItems> ContactListItems = new ArrayList<ContactListItems>();
                    ContactListItems.add(contactList.get(position));
                    TextView accno = (TextView) findViewById(R.id.tv_acc);
                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "ItemRecepit");
                    bundle.putString("Qty", contactList.get(position).getBounce().toString());
                    bundle.putString("Nm", contactList.get(position).getName().toString());
                    bundle.putString("OrderNo", pono.getText().toString());
                    FragmentManager Manager = getFragmentManager();
                    Pop_Update_Qty obj = new Pop_Update_Qty();
                    obj.setArguments(bundle);
                    obj.show(Manager, null);

                }
                break;
                case 4:
                {
                        ArrayList<ContactListItems> ContactListItems = new ArrayList<ContactListItems>();
                        ContactListItems.add(contactList.get(position));
                        TextView accno = (TextView) findViewById(R.id.tv_acc);
                        Bundle bundle = new Bundle();
                        bundle.putString("Scr", "ItemRecepit");
                        bundle.putString("Qty", contactList.get(position).getprice().toString());
                        bundle.putString("Nm", contactList.get(position).getName().toString());
                        bundle.putString("OrderNo", pono.getText().toString());
                        FragmentManager Manager = getFragmentManager();
                         Pop_Price_Qty obj = new Pop_Price_Qty();
                        obj.setArguments(bundle);
                        obj.show(Manager, null);


                }
                break;
                case 5:
                {
                    ArrayList<ContactListItems> ContactListItems = new ArrayList<ContactListItems>();
                    ContactListItems.add(contactList.get(position));
                    TextView accno = (TextView) findViewById(R.id.tv_acc);
                  /*  Bundle bundle = new Bundle();
                    bundle.putString("Scr", "ItemRecepit");
                    bundle.putString("Qty", contactList.get(position).getprice().toString());
                    bundle.putString("Nm", contactList.get(position).getName().toString());
                    bundle.putString("OrderNo", pono.getText().toString());
                    FragmentManager Manager = getFragmentManager();
                    Pop_Price_Qty obj = new Pop_Price_Qty();
*/
                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "ItemRecepit");
                    FragmentManager Manager = getFragmentManager();
                    Select_StoreNo obj = new Select_StoreNo()  ;
                    obj.setArguments(bundle);
                    obj.show(Manager, null);






                }
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
    public void UpdateBounce(String qty) {
        contactList.get(position).setBounce(qty);
        //  CalcTotal();
        showList(1);
    }
    public void Set_Store(String No, String Nm , int f) {
        contactList.get(position).setStoreNo(No);
        contactList.get(position).setStoreNm(Nm);
        //  CalcTotal();
        showList(1);
    }
    public void UpdatePrice(String Price) {
        contactList.get(position).setPrice(Price);
        contactList.get(position).setPrice_From_AB(Price);
        contactList.get(position).setItemOrgPrice(Price);
         CalcTotal();
        showList(1);
    }
    public void Set_SystemCode(String ITEM_NO, String Desc_A, String ID) {
        /*if (ID.equalsIgnoreCase("1")) {
            tv_Maintenance_type.setText(ITEM_NO);
            tv_Maintenance_type_Desc.setText(Desc_A);
        }

        if (ID.equalsIgnoreCase("2")) {
            tv_Employee_No.setText(ITEM_NO);
            tv_Employee_Desc.setText(Desc_A);
        }*/
    }
    private void SearchSerial(){
        Bundle bundle = new Bundle();
        bundle.putString("TABLE_NO", "1");
        bundle.putString("ID", "1");
        bundle.putString("Scr", "ItemRecepit");
        FragmentManager Manager = getFragmentManager();
        SearchLookup obj = new SearchLookup();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_Search_Serial(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("TABLE_NO", "1");
        bundle.putString("ID", "1");
        bundle.putString("Scr", "ItemRecepitSerial");
        FragmentManager Manager = getFragmentManager();
        SearchLookup obj = new SearchLookup();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void btn_Get_Vendor(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("TABLE_NO", "2");
        bundle.putString("ID", "1");
        bundle.putString("Scr", "ItemRecepitVendor");
        FragmentManager Manager = getFragmentManager();
        SearchLookup obj = new SearchLookup();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void Set_SetVendor(String ITEM_NO, String Desc_A, String ID) {
            tv_VendorNo.setText(ITEM_NO.toString());
          tv_VendorNm.setText(Desc_A.toString());
    }
    public void Set_SetSerail(String ITEM_NO, String Desc_A, String ID) {
        tv_SerialNo.setError(null);
        tv_SerialNo.setText(ITEM_NO);
        tv_SerialNm.setText(Desc_A);
    }
    public void btn_PurchesOrders(View view) {
        contactList.clear();
        showList(0);

        if (tv_SerialNm.getText().toString().length() == 0) {
            tv_SerialNm.setError("required!");
            tv_SerialNm.requestFocus();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("V_type", tv_SerialNo.getText().toString() );
        bundle.putString("Myear",tv_PurchesOrderYear.getText().toString());
        bundle.putString("ID", "1");
        bundle.putString("Scr", "ItemRecepit");
        FragmentManager Manager = getFragmentManager();
        SearchPurchesOrders obj = new SearchPurchesOrders();
        obj.setArguments(bundle);
        obj.show(Manager, null);

    }
    public void Set_PurchesOrder(String OrderNo  ) {
    tv_PurchesOrder.setText(OrderNo);
    String q =  "SELECT distinct  Po_Total, order_no,odate,ven,br_no,tot,dis,item_no,UnitNo,UnitRate,qty,StoreNo ,item_name ,cost,OrderMyear,Order_V_TYPE,StoreNm,br_nm,venNm,UnitName " +
                " FROM PurchesOrderTemp where  order_no ='"+OrderNo+"' AND Order_V_TYPE ='"+tv_SerialNo.getText().toString()+"' AND OrderMyear='"+tv_PurchesOrderYear.getText().toString() +"'";


        Cursor c1 = sqlHandler.selectQuery(q);
       String discount="0";
       String Po_Total="0";
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_VendorNm.setText(c1.getString(c1.getColumnIndex("venNm")));
                discount= c1.getString(c1.getColumnIndex("dis"));
                Po_Total= c1.getString(c1.getColumnIndex("Po_Total"));
                tv_VendorNo.setText( c1.getString(c1.getColumnIndex("ven")));
                tv_VendorNm.setText( c1.getString(c1.getColumnIndex("venNm")));

                do {
                   Save_List(c1.getString(c1.getColumnIndex("item_no")),c1.getString(c1.getColumnIndex("cost")),c1.getString(c1.getColumnIndex("qty")),"0",c1.getString(c1.getColumnIndex("UnitNo")),"0","0",c1.getString(c1.getColumnIndex("item_name")),c1.getString(c1.getColumnIndex("UnitName")),"0","","",c1.getString(c1.getColumnIndex("UnitRate")),c1.getString(c1.getColumnIndex("cost")),c1.getString(c1.getColumnIndex("StoreNo")),c1.getString(c1.getColumnIndex("StoreNm")));

                } while (c1.moveToNext());
            }

            c1.close();
        }
        dis.setText(discount+"");
        NetTotal.setText(Po_Total+"");
    }

}

package com.cds_jo.GalaxySalesApp.assist;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cds_jo.GalaxySalesApp.Cls_Sal_InvItems;
import com.cds_jo.GalaxySalesApp.Cls_Sal_Inv_Adapter;
import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Companies;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.PopEnterInvoiceHeaderDiscount;
import com.cds_jo.GalaxySalesApp.PopSal_return_Select_Items;
import com.cds_jo.GalaxySalesApp.Pos.Pos_Activity;
import com.cds_jo.GalaxySalesApp.PostSalesreturn;
//import com.cds_jo.GalaxySalesApp.PostTransActions.PostSalesreturn;
import com.cds_jo.GalaxySalesApp.R;
//import com.cds_jo.GalaxySalesApp.Sal_Inv_return_SearchActivity;
import com.cds_jo.GalaxySalesApp.Sal_return_SearchActivity;
import com.cds_jo.GalaxySalesApp.SearchManBalanceQty;
import com.cds_jo.GalaxySalesApp.Select_Cash_Customer;
import com.cds_jo.GalaxySalesApp.Select_Customer;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;

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
import cn.pedant.SweetAlert.SweetAlertDialog;
import header.Header_Frag;
import header.SimpleSideDrawer;

import static com.cds_jo.GalaxySalesApp.We_Result.Msg;


public class Sale_ReturnActivity extends FragmentActivity {
    int ExistAfterSacve = 0;
    SqlHandler sqlHandler;
    ListView lvCustomList;
    Integer DoPrint = 0 ,DocType = 2,DocTypeinv=1;
    String CatNo = "-1";
    String   FinalDiscountType="0" ;// 1 نسبة    2 مبلغ
    double FinalDiscountpercent=0.0,FinalDiscountAmt ;
    ArrayList<Cls_Sal_InvItems> contactList;
    Boolean IsNew;
    Boolean IsChange, BalanceQtyTrans;
    String UserID = "";
    public ProgressDialog loadingdialog;
    Double Hdr_Dis_A_Amt, Hdr_Dis_Per;
    EditText hdr_Disc;
    int po;
    CheckBox chk_hdr_disc;
    String query;
    String f;
    long PostResult = 0;
    CheckBox IncludeTax_Flag;
    private SimpleSideDrawer mNav;
    TextView tv_CustCelling;
    MyTextView tv_ScrTitle ;
    EditText OrderNo ;
    TextView tv_NetTotal,etTotal,tv_CustNetTotal1;
    private Double SToD(String str) {
        str=str ;
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(
                ",", "");
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
    public void GetMaxPONo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u = sharedPreferences.getString("UserID", "");
        EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
        query = "SELECT   COALESCE(MAX( cast(OrderNo as integer)+1), 0)  as  no FROM Sal_return_Hdr ";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        String max1 = "0";

        String q =" SELECT  COALESCE(MAX( cast(Sales as integer)+1), 0) as RetSales   from OrdersSitting    ";
        Cursor c = sqlHandler.selectQuery(q);
        if(c!=null && c.getCount()>0){
            c.moveToFirst();
            max1= c.getString(c.getColumnIndex("RetSales"));
            c.close();
        }
        if (max1 == "") {
            max1 = "0";
        }

        if (SToD(max1) > SToD(max)) {
            max = max1;
        }

        if (max.length() == 1) {

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
        // max1 = DB.GetValue(Sale_InvoiceActivity.this, "OrdersSitting", "Sales", "1=1");
        //Maxpo.setText(intToString(Integer.valueOf(max), 7));
        Maxpo.setFocusable(false);
        Maxpo.setEnabled(false);
        Maxpo.setCursorVisible(false);




        contactList.clear();
    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }


        return dir.delete();
    }
    EditText   et_OrdeNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale__return);
        etTotal = (TextView) findViewById(R.id.et_Total);
        hideSoftKeyboard();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.setTitle(sharedPreferences.getString("CompanyNm", "") + "/" + sharedPreferences.getString("Address", ""));
        hideSoftKeyboard();
        OrderNo = (EditText) findViewById(R.id.et_OrdeNo);
        tv_CustNetTotal1 = (TextView) findViewById(R.id.tv_CustNetTotal1);

        try {
            trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fragment frag = new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();
        sqlHandler = new SqlHandler(this);
        tv_CustCelling = (TextView) findViewById(R.id.tv_CustCelling);
        CheckBox chk_showTax = (CheckBox) findViewById(R.id.chk_showTax);
        chk_showTax.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));

        et_OrdeNo = (EditText) findViewById(R.id.et_OrdeNo);
        IncludeTax_Flag = (CheckBox) findViewById(R.id.chk_Tax_Include);
        IncludeTax_Flag.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));

        lvCustomList = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);
        ComInfo.ComNo = Integer.parseInt(DB.GetValue(this, "ComanyInfo", "CompanyID", "1=1"));
        contactList = new ArrayList<Cls_Sal_InvItems>();
        contactList.clear();


        CheckBox chk_Type = (CheckBox) findViewById(R.id.chk_Type);
        chk_Type.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        if (ComInfo.ComNo == 2) {
            chk_Type.setChecked(true);
            IncludeTax_Flag.setChecked(false);
            IncludeTax_Flag.setVisibility(View.INVISIBLE);
        }


        IsNew = true;
        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.sal_inv_menu);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        IsNew = true;
        IsChange = false;


        hdr_Disc = (EditText) findViewById(R.id.et_hdr_Disc);
        hdr_Disc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hdr_Disc.setText("", TextView.BufferType.EDITABLE);
                }
            }
        });
        Hdr_Dis_A_Amt = 0.0;
        Hdr_Dis_Per = 0.0;

        chk_hdr_disc = (CheckBox) findViewById(R.id.chk_hdr_disc);
        final TextView et_Total = (TextView) findViewById(R.id.et_Total);
        IncludeTax_Flag.setChecked(true);
        IncludeTax_Flag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CalcTotal();
                showList();

            }
        });
        CustAmtDt();
        GetMaxPONo();
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));
        Get_CatNo(accno.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        tv_NetTotal = (TextView) mNav.findViewById(R.id.tv_NetTotal);
        /*if (ComInfo.ComNo == Companies.beutyLine.getValue() ) {
            IncludeTax_Flag.setChecked(false);
            IncludeTax_Flag.setVisibility(View.INVISIBLE);
            chk_showTax.setChecked(true);
            chk_showTax.setVisibility(View.INVISIBLE);
        }*/

    }
    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) Sale_ReturnActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.RESULT_HIDDEN);
        }
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
            c1.close();
        }

    }
    private void   showList() {

        lvCustomList.setAdapter(null);
        Cls_Sal_Inv_Adapter contactListAdapter = new Cls_Sal_Inv_Adapter(
                Sale_ReturnActivity.this, contactList);
        lvCustomList.setAdapter(contactListAdapter);

    }
    private void FillAdapter() {
        contactList.clear();
        float Total = 0;
        float Total_Tax = 0;
        float TTemp = 0;
        float PTemp = 0;
        float PQty = 0;
        String query = "";
        TextView etTotal, et_Tottal_Tax;

        etTotal = (TextView) findViewById(R.id.et_Total);
        et_Tottal_Tax = (TextView) findViewById(R.id.et_TotalTax);
        etTotal.setText(String.valueOf(Total));
        et_Tottal_Tax.setText(String.valueOf(Total_Tax));


        sqlHandler = new SqlHandler(this);


        EditText Order_no = (EditText) findViewById(R.id.et_OrdeNo);
        query = "  select distinct  ifnull(pod.DisAmtFromHdr,0)   as   DisAmtFromHdr , ifnull(pod.DisPerFromHdr,0)   as DisPerFromHdr  , ifnull(pod.weight,0)     as weight,    ifnull(pod.Pro_Type,0)     as Pro_Type,        ifnull(pod.Operand,0) as Operand  ,  ifnull(pod.bounce_qty,0) as bounce_qty    ,pod.dis_per , pod.dis_Amt , pod.OrgPrice , pod.Tax_Amt  as Tax_Amt, pod.Total as Total  ,Unites.UnitName,  invf.Item_Name, pod.ItemNo as ItemNo ,pod.price,pod.qty,pod.tax ,pod.unitNo,pod.Damaged,pod.Note  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt    " +
                " from Sal_return_Det pod left join invf on invf.Item_No =  pod.itemno   " +
                " left join Unites on Unites.Unitno=  pod.unitNo  Where   pod.Orderno='" + Order_no.getText().toString() + "'  order by pod.itemno ";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("ItemNo")));
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

                    contactListItems.setWeight(c1.getString(c1
                            .getColumnIndex("weight")));
                    contactListItems.setOperand(c1.getString(c1
                            .getColumnIndex("Operand")));
                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("Tax_Amt")));
                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("Total")));

                    contactListItems.setProID(c1.getString(c1
                            .getColumnIndex("ProID")));

                    contactListItems.setPro_bounce(c1.getString(c1
                            .getColumnIndex("Pro_bounce")));

                    contactListItems.setPro_dis_Per(c1.getString(c1
                            .getColumnIndex("Pro_dis_Per")));

                    contactListItems.setPro_amt("0");

                    contactListItems.setPro_Total("0");

                    contactListItems.setProType("0");

                    contactListItems.setDisPerFromHdr(c1.getString(c1
                            .getColumnIndex("DisPerFromHdr")));
                    contactListItems.setDisAmtFromHdr(c1.getString(c1
                            .getColumnIndex("DisAmtFromHdr")));
                    contactListItems.setDamaged(c1.getString(c1
                            .getColumnIndex("Damaged")));
                    contactListItems.setNote(c1.getString(c1
                            .getColumnIndex("Note")));
                    contactList.add(contactListItems);

                    TTemp = Float.valueOf(c1.getString(c1.getColumnIndex("tax")));
                    PTemp = Float.valueOf(c1.getString(c1.getColumnIndex("price")));
                    PQty = Float.valueOf(c1.getString(c1.getColumnIndex("qty")));

                    PTemp = PTemp * PQty;

                    TTemp = TTemp / 100;
                    TTemp = TTemp * PTemp;

                    Total = Total + PTemp;
                    Total_Tax = Total_Tax + TTemp;
                    PTemp = 0;
                    PQty = 0;
                    TTemp = 0;
                } while (c1.moveToNext());


                etTotal.setText(String.valueOf(Total));
                et_Tottal_Tax.setText(String.valueOf(Total_Tax));
            }
            c1.close();
        }

    }
    public void btn_searchCustomer(View view) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u = sharedPreferences.getString("UserID", "");


        CheckBox chk_Type = (CheckBox) findViewById(R.id.chk_Type);

        if (chk_Type.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putString("Scr", "Sale_Inv");
            FragmentManager Manager = getFragmentManager();
            Select_Cash_Customer obj = new Select_Cash_Customer();
            obj.setArguments(bundle);
            obj.show(Manager, null);

        } else {
            if (u == "-1" || ComInfo.ComNo == 2) {
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "Sale_Inv");
                FragmentManager Manager = getFragmentManager();
                Select_Customer obj = new Select_Customer();
                obj.setArguments(bundle);
                obj.show(Manager, null);
            }

        }
    }
    public void Set_Cust(String No, String Nm) {
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }
    public void Save_Recod_Po() {
        Integer Seq = 0;
        CheckBox chk_Type = (CheckBox) findViewById(R.id.chk_Type);
        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        EditText et_hdr_Disc = (EditText) findViewById(R.id.et_hdr_Disc);

        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        TextView dis = (TextView) findViewById(R.id.et_dis);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDate = sdf.format(new Date());

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        long i;
        int dayOfWeek;
        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String q1 = "Select * From Sal_return_Hdr Where  OrderNo='" + pono.getText().toString() + "'";
        Cursor c1;
        c1 = sqlHandler.selectQuery(q1);

        if (c1 != null && c1.getCount() != 0) {
            IsNew = false;
            c1.close();
        } else {
            IsNew = true;
        }

        if (IsNew == true) {
            Seq = Integer.parseInt(DB.GetValue(this, "Sal_return_Hdr", "ifnull(Max(Seq),0)+1", "  date='" + currentDate + "'"));

        } else {
            Seq = Integer.parseInt(DB.GetValue(this, "Sal_return_Hdr", "ifnull(Seq,0)", "  OrderNo='" + pono.getText().toString() + "'"));

        }
        ContentValues cv = new ContentValues();
        cv.put("OrderNo", pono.getText().toString());
        cv.put("acc", acc.getText().toString());
        cv.put("date", currentDate);
        cv.put("Time", currentDateandTime);
        cv.put("UserID", UserID);
        cv.put("Total", SToD(Total.getText().toString()));
        cv.put("Net_Total", SToD(NetTotal.getText().toString()));
        cv.put("Tax_Total", SToD(TotalTax.getText().toString()));
        cv.put("Post", "-1");
        cv.put("QtyStoreSer", "1");
        cv.put("Nm", custNm.getText().toString());
        cv.put("V_OrderNo", sharedPreferences.getString("V_OrderNo", "0"));
        cv.put("DayNum", dayOfWeek);
        cv.put("Seq", Seq);
        cv.put("bounce_Total", "0");
        cv.put("doctype", "2");
        cv.put("TotalWithoutDiscount", SToD(NetTotal.getText().toString()  ));

        if (IncludeTax_Flag.isChecked()) {
            cv.put("include_Tax", "0");
        } else {
            cv.put("include_Tax", "-1");
        }
        if (chk_Type.isChecked()) {
            cv.put("return_type", "0");
        } else {
            cv.put("return_type", "-1");
        }

        cv.put("disc_Total", dis.getText().toString());

        if (IsNew == true) {
            i = sqlHandler.Insert("Sal_return_Hdr", null, cv);
        } else {
            i = sqlHandler.Update("Sal_return_Hdr", cv, "   OrderNo ='" + pono.getText().toString()  + "'");
        }

        if (i > 0) {
            String q = "Delete from  Sal_return_Det where     OrderNo ='" + pono.getText().toString()  + "'";
            sqlHandler.executeQuery(q);

            for (int x = 0; x < contactList.size(); x++) {
                try {
                    Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
                    contactListItems = contactList.get(x);
                    cv = new ContentValues();
                    cv.put("OrderNo", pono.getText().toString() );
                    cv.put("itemno", contactListItems.getNo()  );
                    cv.put("price", contactListItems.getPrice().toString() );
                    cv.put("qty", SToD(contactListItems.getQty().toString() ));
                    cv.put("tax", SToD(contactListItems.getTax().toString() ));
                    cv.put("unitNo", contactListItems.getUnite().toString() );
                    cv.put("dis_per", "0");
                    cv.put("dis_Amt","0");
                    cv.put("bounce_qty", "0");
                    cv.put("tax_Amt", SToD(contactListItems.getTax_Amt().toString()));
                    cv.put("total", SToD(contactListItems.getTotal().toString()));
                    cv.put("ProID", "0");
                    cv.put("Pro_bounce", SToD(contactListItems.getBounce().toString()));
                    cv.put("Pro_dis_Per", SToD(contactListItems.getDiscount().toString()));
                    cv.put("Pro_amt", "0");
                    cv.put("pro_Total", "0");
                    cv.put("OrgPrice", SToD(contactListItems.getItemOrgPrice().toString()));
                    cv.put("Pro_Type", contactListItems.getProType() );
                    cv.put("Operand", SToD(contactListItems.getOperand().toString() ));
                    cv.put("weight", SToD(contactListItems.getWeight().toString() ));
                    cv.put("DisAmtFromHdr", SToD(contactListItems.getDisAmtFromHdr().toString() ));
                    cv.put("DisPerFromHdr", SToD(contactListItems.getDisPerFromHdr().toString() ));
                    cv.put("doctype", DocType.toString());
                    cv.put("Damaged", contactListItems.getDamaged().toString());
                    cv.put("Note", contactListItems.getNote().toString());


                    if (i > 0) {
                        i = sqlHandler.Insert(this, "Sal_return_Det", null, cv);
                        if (i < 0) {
                            alertDialog.setTitle("  الرجاء تبليغ مسؤول النظام  للضرورة  " + contactListItems.getName().toString());
                            alertDialog.setMessage(cv.toString());
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertDialog.show();
                            try {
                                File myFile = new File("/sdcard/Android/data/Galaxy/Sal_return_Det.txt");
                                if (!myFile.exists()) {
                                    myFile.createNewFile();
                                }
                                FileOutputStream fOut = new FileOutputStream(myFile, true);
                                OutputStreamWriter myOutWriter =
                                        new OutputStreamWriter(fOut);
                                myOutWriter.append("\n\r" + "--------------------START-----------------------------\r\n");
                                myOutWriter.append("DateTime" + currentDate + "--" + currentDateandTime + "\r\n");
                                myOutWriter.append("SQL :" + cv.toString() + "\n\r");
                                myOutWriter.append("\n\r" + "--------------------END------------------------------\r\n");
                                myOutWriter.close();
                                fOut.close();
                            } catch (Exception ex) {
                            }
                            break;
                        }

                    }
                } catch (Exception ex) {
                    if (IsNew) {
                        sqlHandler.executeQuery(" Delete from  Sal_return_Hdr where   OrderNo ='" + pono.getText().toString() + "'");
                        sqlHandler.executeQuery(" Delete from  Sal_return_Det where   OrderNo ='" + pono.getText().toString() + "'");
                        i = -1;
                    }
                }
            }

            if (i <= 0) {
                if (IsNew) {
                    sqlHandler.executeQuery(" Delete from  Sal_return_Hdr where    OrderNo ='" + pono.getText().toString() + "'");
                    sqlHandler.executeQuery(" Delete from  Sal_return_Det where   OrderNo ='" + pono.getText().toString() + "'");
                }
            }

        }
        CustAmtDt();
        if (i > 0) {
            UpDateMaxOrderNo();
            tv_ScrTitle=(MyTextView) findViewById(R.id.tv_ScrTitle);
            alertDialog.setTitle(tv_ScrTitle.getText().toString());
            alertDialog.setMessage("تمت عمليةالتخزين  بنجاح");
            IsChange = false;
            // chk_Type.setEnabled(false);
            IsNew = false;
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                   /* Intent k = new Intent(Sale_ReturnActivity.this, JalMasterActivity.class);
                    startActivity(k);*/

                }
            });
            alertDialog.show();
        }
    }
    private void UpDateMaxOrderNo() {
        String query = " SELECT  Distinct  COALESCE(MAX( cast(OrderNo as integer)), 0)   AS no   " +
                " FROM Sal_return_Hdr   ";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }
        query = "Update OrdersSitting SET RetSales ='" + max + "'";
        sqlHandler.executeQuery(query);
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


        Cls_Sal_InvItems contactListItems;
        for (int x = 0; x < contactList.size(); x++) {
            contactListItems = new Cls_Sal_InvItems();
            contactListItems = contactList.get(x);
            All_Dis = Double.parseDouble(contactListItems.getDis_Amt().replace(",", "")) + Double.parseDouble(contactListItems.getDisAmtFromHdr().replace(",", "")) + Double.parseDouble(contactListItems.getPro_amt().replace(",", ""));
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());

            if (IncludeTax_Flag.isChecked()) {

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
        showList();
    }
    private void CalcTotal() {
        Double Total, Tax_Total, Dis_Amt, Po_Total;
        Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
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

        Double TaxVal = 0.0;
        double TaxFactor = 0.0;
        CalcTax();
        Double RowTotal = 0.0;
        Double pq = 0.0;
        Double opq = 0.0;
        Double V_NetTotal = 0.0;
        for (int x = 0; x < contactList.size(); x++) {
            contactListItems = new Cls_Sal_InvItems();
            contactListItems = contactList.get(x);
            All_Dis = SToD(contactListItems.getDis_Amt()) + SToD(contactListItems.getDisAmtFromHdr()) + SToD(contactListItems.getPro_amt());
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());
            pq = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
            opq = SToD(contactListItems.getItemOrgPrice()) * SToD(contactListItems.getQty());

            Tax_Total = Tax_Total + (SToD(contactListItems.getTax_Amt().toString()));
            Dis_Amt = Dis_Amt + (((opq) * (All_Dis_Per / 100)));

            if (IncludeTax_Flag.isChecked()) {
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

            contactListItems.setTotal((SToD(RowTotal.toString())).toString().replace(",", ""));
            All_Dis = 0.0;

        }
        Total = V_NetTotal - Tax_Total + Dis_Amt;
        TotalTax.setText(String.valueOf(df.format(Tax_Total)).replace(",", ""));
        Subtotal.setText(String.valueOf(df.format(Total)).replace(",", ""));
        dis.setText(String.valueOf(df.format(Dis_Amt)).replace(",", ""));


       /* if (Tax_Include.isChecked()){
            Po_Total = Po_Total + ((Double.parseDouble(Subtotal.getText().toString().replace(",", "")) - Double.parseDouble(dis.getText().toString().replace(",", "")) ) + Double.parseDouble(TotalTax.getText().toString().replace(",", ""))    );
        }
        else{
            Po_Total = Po_Total + ((Double.parseDouble(Subtotal.getText().toString().replace(",", "")) - Double.parseDouble(dis.getText().toString().replace(",","")) )   + Double.parseDouble(TotalTax.getText().toString().replace(",", "")) );
        }*/
        Po_Total = Po_Total + ((SToD(Subtotal.getText().toString()) - SToD(dis.getText().toString())) + SToD(TotalTax.getText().toString()));

        showList();
        NetTotal.setText(String.valueOf(df.format(V_NetTotal)));
        CustAmtDt();
    }
    /* private void CalcTax() {
         Double All_Dis = 0.0;
         Double RowTotal = 0.0;
         Double NetRow = 0.0;
         Double TaxAmt = 0.0;
         Double TaxFactor = 0.0;
         Double All_Dis_Per = 0.0;
         NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
         DecimalFormat df = (DecimalFormat) nf;
         Cls_Sal_InvItems contactListItems;
         for (int x = 0; x < contactList.size(); x++) {
             contactListItems = new Cls_Sal_InvItems();
             contactListItems = contactList.get(x);
             All_Dis = Double.parseDouble(contactListItems.getDis_Amt().replace(",", "")) + Double.parseDouble(contactListItems.getDisAmtFromHdr().replace(",", "")) + Double.parseDouble(contactListItems.getPro_amt().replace(",", ""));
             All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());
             if (IncludeTax_Flag.isChecked()) {
                 contactListItems.setprice(SToD(String.valueOf((SToD(contactListItems.getItemOrgPrice())) / ((SToD(contactListItems.getTax()) / 100) + 1))).toString());
             } else {
                 contactListItems.setprice(String.valueOf(SToD(contactListItems.getItemOrgPrice())));
             }
             RowTotal = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
             TaxFactor = (Double.parseDouble(contactListItems.getTax()) / 100);
             NetRow = RowTotal - (RowTotal * (All_Dis_Per / 100));
             TaxAmt = NetRow * TaxFactor;
             contactListItems.setTax_Amt(df.format(TaxAmt).toString());
         }
         showList();
     }
     private void CalcTotal() {
         Double Total, Tax_Total, Dis_Amt, Po_Total;
         Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
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
         Double TaxVal = 0.0;
         double TaxFactor = 0.0;
         CalcTax();
         Double RowTotal = 0.0;
         Double pq = 0.0;
         Double opq = 0.0;
         Double V_NetTotal = 0.0;
         for (int x = 0; x < contactList.size(); x++) {
             contactListItems = new Cls_Sal_InvItems();
             contactListItems = contactList.get(x);
             All_Dis = SToD(contactListItems.getDis_Amt()) + SToD(contactListItems.getDisAmtFromHdr()) + SToD(contactListItems.getPro_amt());
             All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());
             pq = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
             opq = SToD(contactListItems.getItemOrgPrice()) * SToD(contactListItems.getQty());
             Tax_Total = Tax_Total + (SToD(contactListItems.getTax_Amt().toString()));
             Dis_Amt = Dis_Amt + (((pq) * (All_Dis_Per / 100)));
             if (IncludeTax_Flag.isChecked()) {
                 RowTotal = opq - ((opq) * (All_Dis_Per / 100));//+ SToD(contactListItems.getTax_Amt());
             } else {
                 RowTotal = pq - ((pq) * (All_Dis_Per / 100)) + SToD(contactListItems.getTax_Amt());
                 Total = Total + pq;
             }
             V_NetTotal = V_NetTotal + SToD(RowTotal.toString().replace(",", ""));
             contactListItems.setTotal((SToD(RowTotal.toString())).toString().replace(",", ""));
             All_Dis = 0.0;
         }
         Total = V_NetTotal - Tax_Total + Dis_Amt;
         TotalTax.setText(String.valueOf(df.format(Tax_Total)).replace(",", ""));
         Subtotal.setText(String.valueOf(df.format(Total)).replace(",", ""));
         dis.setText(String.valueOf(df.format(Dis_Amt)).replace(",", ""));
        *//* if (Tax_Include.isChecked()){
            Po_Total = Po_Total + ((Double.parseDouble(Subtotal.getText().toString().replace(",", "")) - Double.parseDouble(dis.getText().toString().replace(",", "")) ) + Double.parseDouble(TotalTax.getText().toString().replace(",", ""))    );
        }
        else{
            Po_Total = Po_Total + ((Double.parseDouble(Subtotal.getText().toString().replace(",", "")) - Double.parseDouble(dis.getText().toString().replace(",","")) )   + Double.parseDouble(TotalTax.getText().toString().replace(",", "")) );
        }*//*
        Po_Total = Po_Total + ((SToD(Subtotal.getText().toString()) - SToD(dis.getText().toString())) + SToD(TotalTax.getText().toString()));
        showList();
        NetTotal.setText(String.valueOf(df.format(V_NetTotal)));
        CustAmtDt();
    }*/
    public void btn_show_Pop(View view) {
        showPop();
    }
    public void showPop() {
        EditText Order = (EditText) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Sal_return");
        bundle.putString("IsNew", IsNew.toString());
        bundle.putString("CatNo", CatNo);
        bundle.putString("OrderNo", Order.getText().toString());
        bundle.putString("CustomerNo", accno.getText().toString());

        //Bundle bundle=new Bundle();
        FragmentManager Manager = getFragmentManager();
        PopSal_return_Select_Items obj = new PopSal_return_Select_Items();
        obj.setArguments(bundle);
        obj.show(Manager, null);
        IsChange = true;
    }
    private void CustAmtDt() {
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        EditText Maxpo = (EditText) findViewById(R.id.et_OrdeNo);
        TextView tv_NetTotal = (TextView) findViewById(R.id.tv_CustNetTotal);
        String NetTotal = DB.GetValue(Sale_ReturnActivity.this, "Customers", "CUST_NET_BAL", "no ='" + accno.getText() + "' ");

        String CustCelling = DB.GetValue(Sale_ReturnActivity.this, "Customers", "Celing", "no ='" + accno.getText() + "' ");
        String CUST_NET_BAL = NetTotal;

        if (CustCelling.equalsIgnoreCase("-1")) {
            CustCelling = "0";
        }

        if (CUST_NET_BAL.equalsIgnoreCase("-1")) {
            CUST_NET_BAL = "0";
        }






        if (NetTotal.equalsIgnoreCase("-1")) {
            NetTotal = "غير مدخله";
        }
        tv_NetTotal.setText(NetTotal + "");


        String q = "Select distinct   ifnull( sum(ifnull(s.Net_Total,0.000)),0.000)   as Amt    " +
                "  from  Sal_return_Hdr s    where " +
                "  s.acc='" + accno.getText() + "'    and  s.Post ='-1'";// and   s.OrderNo !='"+Maxpo.getText()+"'"; //and s.inovice_type = '-1'

        String UnpostedSales="0.000";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                UnpostedSales = c1.getString(c1.getColumnIndex("Amt"));
            }
            c1.close();
        }


        q = "  Select distinct    ifnull( sum(ifnull(RecVoucher.Amnt,0.000)),0.000)     as Amt   from RecVoucher " +
                " where  RecVoucher.CustAcc ='" + accno.getText() + "' and  RecVoucher.Post ='-1'";

        String UnpostedRecVoucher ="0.000";;
        c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                UnpostedRecVoucher = c1.getString(c1.getColumnIndex("Amt"));
            }
            c1.close();
        }



        Double Total = SToD(CustCelling) - SToD(CUST_NET_BAL)+SToD(UnpostedRecVoucher )- SToD(UnpostedSales) ;
        if (Total < 0)
            Total = 0.0 ; // Total * -1;

        tv_CustCelling.setText( SToD(Total+"")+"");

        MyTextView Nav_CustTotal = (MyTextView) mNav.findViewById(R.id.tv_CustTotal);
        MyTextView Nav_Celing = (MyTextView) mNav.findViewById(R.id.tv_Celing);
        MyTextView Nav_UnpostedSales = (MyTextView) mNav.findViewById(R.id.tv_UnpostedSales);
        MyTextView Nav_UnpostedPayment = (MyTextView) mNav.findViewById(R.id.tv_UnpostedPayment);
        MyTextView Nav_NetCelling = (MyTextView) mNav.findViewById(R.id.tv_NetCelling);





        Nav_CustTotal.setText(CUST_NET_BAL+"");
        Nav_Celing.setText(CustCelling+"");
        Nav_UnpostedSales.setText(UnpostedSales+"");
        Nav_UnpostedPayment.setText(UnpostedRecVoucher+"");
        Nav_NetCelling.setText( SToD(Total+"")+"");


    }
    public void btn_save_po(final View view) {
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);

        Double TempTotal = 0.0;
        for (int x = 0; x < contactList.size(); x++) {
            TempTotal = TempTotal + SToD(contactList.get(x).getTotal());
        }
        TempTotal = SToD(TempTotal + "");
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        if (Double.parseDouble(TempTotal + "") != Double.parseDouble(SToD(NetTotal.getText().toString()) + "")) {

            alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(tv_ScrTitle.getText().toString() + TempTotal.toString());
            alertDialog.setMessage("لا يمكن تخزين المستند ، الرجاء إعادة ادخال المواد   ");

            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return;
        }

        final TextView tv_acc = (TextView) findViewById(R.id.tv_acc);
        ///////////////////////////////////////////////////

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Count = sharedPreferences.getString("InvCount", "0");
        String NumOfInvPerVisit = DB.GetValue(Sale_ReturnActivity.this, "ComanyInfo", "NumOfInvPerVisit  ", "1=1");

        String q = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        String RecVoucher_DocNo = DB.GetValue(Sale_ReturnActivity.this, "RecVoucher", "DocNo", "CustAcc ='" + tv_acc.getText() + "' AND ((TrDate)=('" + currentDateandTime + "'))");
        String PAMENT_PERIOD_NO = DB.GetValue(Sale_ReturnActivity.this, "Customers", "PAMENT_PERIOD_NO", "no ='" + tv_acc.getText() + "' ");

        if (RecVoucher_DocNo == "-1" && PAMENT_PERIOD_NO.equals("0")) {
            alertDialog.setTitle(tv_ScrTitle.getText().toString());
            alertDialog.setMessage("يجب عمل سند قبض اولاَ ، لان نوع العميل فاتورة بفاتورة");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();
            return;
        }
        //////////////////////////////////////////////////////////////////////////////////////

        final TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        q = "SELECT distinct *  from  Sal_return_Hdr where Post >0 AND   Orderno ='" + pono.getText().toString() + "'";
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {


            alertDialog.setTitle("ارجاع المواد");
            alertDialog.setMessage("لقد تم ترحيل المستند لايمكن التعديل");
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (ExistAfterSacve == 1) {
                        ExistAfterSacve = 0;
                        Intent k = new Intent(Sale_ReturnActivity.this, JalMasterActivity.class);
                        startActivity(k);
                    }
                    if (DoPrint == 1) {
                        View view = null;
                        btn_print(view);
                    }

                }
            });

            alertDialog.show();


            c1.close();
            return;
        } else {


            String Msg = "";

            final TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
            AlertDialog.Builder alertDialogYesNo = new AlertDialog.Builder(this);

            if (ComInfo.ComNo == 1) {
                q = "SELECT distinct *  from  Sal_return_Hdr where acc  ='" + acc.getText() + "'   AND   date  ='" + currentDateandTime + "' " +
                        " And   Orderno !='" + pono.getText().toString() + "'";

                c1 = sqlHandler.selectQuery(q);
                if (c1 != null && c1.getCount() != 0) {
                    Msg = "يوجد فاتورة لنفس هذا العميل في نفس هذا اليوم  " + "\n\r";
                    c1.close();

                }

            }
            if (contactList.size() == 0) {


                alertDialog.setTitle("ارجاع المواد");
                alertDialog.setMessage("لا يمكن تخزين المستند بدون مواد");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (ExistAfterSacve == 1) {
                            ExistAfterSacve = 0;
                            Intent k = new Intent(Sale_ReturnActivity.this, JalMasterActivity.class);
                            startActivity(k);
                        }


                    }
                });

                alertDialog.show();

                return;

            }

            tv_ScrTitle=(MyTextView) findViewById(R.id.tv_ScrTitle);
            alertDialogYesNo.setTitle(tv_ScrTitle.getText().toString());
            alertDialogYesNo.setMessage(Msg + "  " + "هل  تريد الاستمرار بعملية الحفظ " + "؟");

            // Setting Icon to Dialog
            alertDialogYesNo.setIcon(R.drawable.save);

            alertDialogYesNo.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (ExistAfterSacve == 1) {
                        ExistAfterSacve = 0;
                        Intent k = new Intent(Sale_ReturnActivity.this, JalMasterActivity.class);
                        startActivity(k);
                    }
                    if (DoPrint == 1) {
                        View view = null;
                        btn_print(view);
                    }

                    return;
                }
            });

            alertDialogYesNo.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
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

                    if(DocType==2 || ComInfo.ComNo == Companies.beutyLine.getValue() ){
                        Save_Recod_Po();
                    }
                }
            });


            alertDialogYesNo.show();


        }

    }
    public void Save_List(String ItemNo, String p, String q, String t, String u, String dis, String bounce, String ItemNm, String UnitName, String dis_Amt, String Operand,String Weight,String Damaged,String Note,String dis1 ) {
        if (bounce.toString().equals(""))
            bounce = "0";
        if (dis.toString().equals(""))
            dis = "0";

        if (dis_Amt.toString().equals(""))
            dis_Amt = "0";

        if (p.toString().equals(""))
            p = "0";

        if (q.toString().equals(""))
            q = "0";

        if (Weight.toString().equals(""))
            Weight = "0";

        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;
        for (int x = 0; x < contactList.size(); x++) {
            Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
            contactListItems = contactList.get(x);
            if (contactListItems.getNo().equals(ItemNo) && contactListItems.getProType() != "3") {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle(tv_ScrTitle.getText().toString());
                alertDialog.setMessage("المادة موجودة");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();
                return;
            }
        }
        Item_Total = Double.parseDouble(q) * Double.parseDouble(p);
        Price = Double.parseDouble(p);
        Tax = Double.parseDouble(t);
        Item_Total = Double.parseDouble(Item_Total.toString());
        Double DisValue = Double.parseDouble(dis_Amt.toString().replace(",", ""));
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        if (IncludeTax_Flag.isChecked()) {
            contactListItems.setprice(String.valueOf(Price / ((Tax / 100) + 1)));
        } else {
            contactListItems.setprice(String.valueOf(Price));
        }
        contactListItems.setItemOrgPrice(String.valueOf(Price));
        contactListItems.setQty(q);
        contactListItems.setTax(String.valueOf(Tax));
        contactListItems.setUnite(u);
        contactListItems.setBounce(bounce);
        contactListItems.setDiscount(dis1);
        contactListItems.setProID("0");
        contactListItems.setDis_Amt(dis_Amt);
        contactListItems.setUniteNm(UnitName);
        contactListItems.setPro_amt("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_Total("0");
        contactListItems.setDisAmtFromHdr("0");
        contactListItems.setDisPerFromHdr("0");
        contactListItems.setTax_Amt("0");
        contactListItems.setProType("0");
        contactListItems.setOperand(Operand);
        contactListItems.setWeight(Weight);
        contactListItems.setTotal(String.valueOf(df.format(Item_Total)));
        contactListItems.setDamaged(Damaged);
        contactListItems.setNote(Note);
        contactList.add(contactListItems);
        CalcTotal();
        showList();
    }
    public void Save_Method(String m, String p, String q, String t, String u,String d, String n) {
        EditText Order = (EditText) findViewById(R.id.et_OrdeNo);
        String query = "INSERT INTO Sal_return_Det(doctype,OrderNo,itemNo,unitNo,price,qty,tax,Damaged,Note) values ("+DocType.toString() +","+ Order.getText().toString() + ",'" + m + "','" + u + "','" + p + "','" + q + "','" + t + "','" + d + "','" + n + "')";
        sqlHandler.executeQuery(query);
        showList();
    }
    public void btn_delete(View view) {

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("ارجاع المواد");
        alertDialog.setMessage("لا يمكن الحذف");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        alertDialog.show();
    }
    public void Delete_Record_PO() {

        TextView OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());


        String query = "Delete from  Sal_return_Hdr where     OrderNo ='" + OrdeNo.getText().toString() + "'";
        sqlHandler.executeQuery(query);


        query = "Delete from  Sal_return_Det where    OrderNo ='" + OrdeNo.getText().toString() + "'";
        sqlHandler.executeQuery(query);

        GetMaxPONo();
        showList();
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        CustNm.setText("");
        accno.setText("");
        CheckBox chk_Type = (CheckBox) findViewById(R.id.chk_Type);
        IncludeTax_Flag.setChecked(false);
        chk_Type.setChecked(false);
        EditText et_hdr_Disc = (EditText) findViewById(R.id.et_hdr_Disc);
        et_hdr_Disc.setText("0");
        CheckBox chk_hdr_disc = (CheckBox) findViewById(R.id.chk_hdr_disc);
        chk_hdr_disc.setChecked(false);
        IsNew = true;
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle(tv_ScrTitle.getText().toString());
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
    public void btn_Search_Orders(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Sal_return");
        bundle.putString("doctype", DocType.toString());
        FragmentManager Manager = getFragmentManager();
        Sal_return_SearchActivity obj = new Sal_return_SearchActivity();
        obj.setArguments(bundle);
        obj.show(Manager,null);
    }
    public void Set_Order(String No) {
        No=No ;
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView no = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);


        TextView tv_NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView et_TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        TextView et_dis = (TextView) findViewById(R.id.et_dis);
        no.setText(No);


        FillAdapter();
        showList();
        String q = " Select  distinct  ifnull( s.hdr_dis_per,'0')  as   hdr_dis_per, ifnull( s.hdr_dis_value,0) as hdr_dis_value ,s.Nm   ,s.include_Tax ,  s.disc_Total, s.OrderNo,s.Net_Total,s.Tax_Total ,s.acc ,s.date , c.name,s.doctype , s.return_type " +
                "    from  Sal_return_Hdr s   " +
                "    left join Customers c on c.no =s.acc   " +
                "    where     s.Orderno = '" + No + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
        CustNm.setText("");
        accno.setText("");

        CheckBox chk_Type = (CheckBox) findViewById(R.id.chk_Type);


        CheckBox chk_hdrdiscount = (CheckBox) findViewById(R.id.chk_hdr_disc);
        chk_Type.setChecked(true);
        IncludeTax_Flag.setChecked(false);
        chk_hdrdiscount.setChecked(false);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                try {
                    CustNm.setText(c1.getString(c1.getColumnIndex("name")).toString());

                } catch (Exception ex) {

                    Toast.makeText(this, "لا يمكن استرجاع الحساب", Toast.LENGTH_SHORT).show();
                }
                //CustNm.setText(c1.getString(c1.getColumnIndex("name")).toString());
                accno.setText(c1.getString(c1.getColumnIndex("acc")));
                tv_NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")));
                et_TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")));



                if (c1.getString(c1.getColumnIndex("include_Tax")).equals("0")) {
                    IncludeTax_Flag.setChecked(true);
                }
                if (c1.getString(c1.getColumnIndex("return_type")).equals("-1")) {
                    chk_Type.setChecked(true);
                }


            }
            c1.close();
        }
        IsChange = false;
        IsNew = false;
    }
    public void btn_print(View view) {
        EditText OrderNo =(EditText)findViewById(R.id.et_OrdeNo) ;
        String q  = "SELECT distinct *  from  Sal_return_Hdr where    " +
                "   Post <= 0 AND   OrderNo ='" + OrderNo.getText().toString().trim() + "'";

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            new SweetAlertDialog(Sale_ReturnActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("مرتجع المبيعات")
                    .setContentText("لا يمكن الطباعة الا بعد الاعتماد")
                    .setCustomImage(R.drawable.error_new)
                    .show();

            c1.close();
            return;
        }
        TextView et_TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        TextView et_dis = (TextView) findViewById(R.id.et_dis);
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        //TextView tv_OrderNo = (TextView) findViewById(R.id.tv_OrderNo);
        Intent k;
        k = new Intent(Sale_ReturnActivity.this, Xprinter_SalesReturn.class);
        k.putExtra("Orderno", OrderNo.getText().toString().replace("\u202c","").replace("\u202d",""));
        k.putExtra("nettotal", tv_NetTotal.getText().toString().replace("\u202c","").replace("\u202d",""));
        k.putExtra("total", etTotal.getText().toString().replace("\u202c","").replace("\u202d",""));
        k.putExtra("totaltax", et_TotalTax.getText().toString().replace("\u202c","").replace("\u202d",""));
        k.putExtra("totaldis", et_dis.getText().toString().replace("\u202c","").replace("\u202d",""));
        k.putExtra("cusname", CustNm.getText().toString().replace("\u202c","").replace("\u202d",""));
        //   k.putExtra("tOrderNo", tv_OrderNo.getText().toString().replace("\u202c","").replace("\u202d",""));
        startActivity(k);



    }
    public void btn_new(View view) {

        ExistAfterSacve = 0;
        GetMaxPONo();
        showList();
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        TextView et_Total=(TextView) findViewById(R.id.et_Total);
        TextView et_dis=(TextView) findViewById(R.id.et_dis);
        TextView et_TotalTax=(TextView) findViewById(R.id.et_TotalTax);
        TextView tv_NetTotal=(TextView) findViewById(R.id.tv_NetTotal);
        EditText et_hdr_Disc = (EditText) findViewById(R.id.et_hdr_Disc);
        CheckBox chk_showTax = (CheckBox) findViewById(R.id.chk_showTax);
        et_hdr_Disc.setText("0");
        CustNm.setText("");
        accno.setText("");
        et_Total.setText("");
        et_dis.setText("");
        tv_NetTotal.setText("");
        et_TotalTax.setText("");
        CheckBox chk_hdr_disc = (CheckBox) findViewById(R.id.chk_hdr_disc);
        CheckBox chk_Type = (CheckBox) findViewById(R.id.chk_Type);
        IsNew = true;
        IncludeTax_Flag.setChecked(true);
        chk_Type.setChecked(true);
        //  chk_Type.setEnabled(true);
        chk_hdr_disc.setChecked(false);
        BalanceQtyTrans = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));

        if (ComInfo.ComNo == 2) {
            accno.setText("");
            CustNm.setText("");
        }

        if (ComInfo.ComNo == 3) {
            IncludeTax_Flag.setChecked(false);
            chk_showTax.setChecked(true);
            IncludeTax_Flag.setVisibility(View.INVISIBLE);
        }
    }
    public void btn_back(View view) {
        //   RemoveAnmation();
        ImageButton imageButton7 = (ImageButton) findViewById(R.id.imageButton7);
        // imageButton7.startAnimation(shake);
        ExistAfterSacve = 1;
        if (contactList.size() > 0 && IsChange == true) {
            btn_save_po(view);
        } else {
            Intent k = new Intent(this, JalMasterActivity.class);
            startActivity(k);
        }


    }
    int position;
    public void btn_Delete_Item(final View view) {
        position = lvCustomList.getPositionForView(view);
        registerForContextMenu(view);
        openContextMenu(view);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Cls_Sal_InvItems contactListItems =new Cls_Sal_InvItems();


        menu.setHeaderTitle(contactList.get(position).getName());
        po=position;
        menu.add(Menu.NONE, 1, Menu.NONE, "تعديل");
        menu.add(Menu.NONE, 2, Menu.NONE, "حذف");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1: {

                ArrayList<Cls_Sal_InvItems> Itemlist = new ArrayList<Cls_Sal_InvItems>();

                Itemlist.add(contactList.get(position));
                if (contactList.get(position).getProType().equals("3")) {
                    break;
                }


                TextView accno = (TextView) findViewById(R.id.tv_acc);

                Bundle bundle = new Bundle();
                bundle.putString("Scr", "Sal_return");
                bundle.putString("CatNo", CatNo);
                bundle.putString("OrderNo", pono.getText().toString());
                bundle.putString("IsNew", IsNew.toString());
                bundle.putString("CustomerNo", accno.getText().toString());
                bundle.putSerializable("List", Itemlist);
                FragmentManager Manager = getFragmentManager();
                PopSal_return_Select_Items obj = new PopSal_return_Select_Items();
                obj.setArguments(bundle);
                obj.show(Manager, null);

            }
            break;
            case 2: {
                String q1 = "Select * From Sal_return_Hdr Where     OrderNo='" + pono.getText().toString() + "'";
                Cursor c1;
                c1 = sqlHandler.selectQuery(q1);

                if (c1 != null && c1.getCount() != 0) {
                    IsNew = false;
                    c1.close();
                } else {
                    IsNew = true;
                }

                if (ComInfo.ComNo == 1 && IsNew == false) {


                    AlertDialog Dialog = new AlertDialog.Builder(
                            this).create();
                    Dialog.setTitle(tv_ScrTitle.getText().toString());
                    Dialog.setMessage("لا يمكن حذف  المواد ");
                    Dialog.setIcon(R.drawable.tick);
                    Dialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            return;

                        }
                    });

                    Dialog.show();
                } else {


                    if (contactList.get(position).getProType().equals("3")) {
                        break;
                    }


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("إرجاع المواد");
                    alertDialog.setMessage("هل انت متاكد من عملية الحذف");
                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            contactList.remove(position);

                            CalcTotal();
                            showList();

                        }
                    });

                    alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();

                }
            }
            break;

        }

        return super.onContextItemSelected(item);
    }
    public void btn_share(View view) {
        final SqlHandler sql_Handler = new SqlHandler(this);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        final String Doc_No = pono.getText().toString();

        loadingdialog = ProgressDialog.show(Sale_ReturnActivity.this, "الرجاء الانتظار ...",    "العمل جاري على اعتماد  " , true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();




        new Thread(new Runnable() {
            @Override
            public void run() {

                PostSalesreturn obj = new PostSalesreturn(Sale_ReturnActivity.this);
                PostResult = obj.Post_Sal_return(Doc_No,DocType+"");
                try {

                    if (PostResult < 0) {
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        Sale_ReturnActivity.this).create();
                                alertDialog.setTitle("ارجاع المواد");
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


                    } else if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("Post", We_Result.ID);

                        long i;
                        i= sql_Handler.Update("Sal_return_Hdr", cv, "   OrderNo='" + DocNo.getText().toString() + "'");

                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        Sale_ReturnActivity.this).create();
                                alertDialog.setTitle("ارجاع المواد");
                                alertDialog.setMessage("تمت عملية الترحيل بنجاح");
                                alertDialog.setIcon(R.drawable.tick);
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                loadingdialog.dismiss();
                                alertDialog.show();



                            }
                        });
                        //btn_new(view);
                    } else {



                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        Sale_ReturnActivity.this).create();
                                alertDialog.setTitle("فشل في عملية الاعتماد  " + "   " + We_Result.ID + "");
                                alertDialog.setMessage(Msg.toString());
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
                                    Sale_ReturnActivity.this).create();
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
    @Override
    public void onBackPressed() {
        Intent k = new Intent(this, JalMasterActivity.class);
        startActivity(k);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sal_invoice_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.BalanceQty:
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "SalInvoice");
                FragmentManager Manager = getFragmentManager();
                SearchManBalanceQty obj = new SearchManBalanceQty();
                obj.setArguments(bundle);
                obj.show(Manager, null);
                break;
            case R.id.Exist:
                View view = null;
                btn_back(view);
                break;

        }

        return true;
    }
    public void Update_List(String ItemNo, String p, String q, String t, String u, String ItemNm, String UnitName, String dis_Amt, String Operand,String Weight,String Damaged,String Note) {

        if (dis_Amt.toString().equals(""))
            dis_Amt = "0";

        if (p.toString().equals(""))
            p = "0";

        if (q.toString().equals(""))
            q = "0";

        if (Weight.toString().equals(""))
            Weight = "0";

        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;

        Item_Total = Double.parseDouble(q) * Double.parseDouble(p);
        Price = Double.parseDouble(p);
        Tax = Double.parseDouble(t);
        Item_Total = Double.parseDouble(Item_Total.toString());
        Double DisValue = Double.parseDouble(dis_Amt.toString().replace(",", ""));


        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        Cls_Sal_InvItems contactListItems = new Cls_Sal_InvItems();
        contactListItems = contactList.get(po);
        contactListItems.setno(ItemNo);
        contactListItems.setName(contactListItems.getName());
        if (IncludeTax_Flag.isChecked()) {
            contactListItems.setprice(String.valueOf(Price / ((Tax / 100) + 1)));
        } else {
            contactListItems.setprice(String.valueOf(Price));

        }
        //contactListItems.setprice(String.valueOf(Price));
        contactListItems.setItemOrgPrice(String.valueOf(Price));
        contactListItems.setQty(q);
        contactListItems.setTax(String.valueOf(Tax));
        contactListItems.setUnite(u);
        contactListItems.setDis_Amt(DisValue.toString());
        contactListItems.setProID("");
        contactListItems.setDis_Amt(dis_Amt);
        contactListItems.setUniteNm(UnitName);
        contactListItems.setPro_amt("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_Total("0");
        contactListItems.setDisAmtFromHdr("0");
        contactListItems.setDisPerFromHdr("0");
        contactListItems.setTax_Amt("0");
        contactListItems.setProType("0");
        contactListItems.setOperand(Operand);
        contactListItems.setWeight(Weight);
        contactListItems.setTotal(String.valueOf(df.format(Item_Total)));
        contactListItems.setDamaged(Damaged);
        contactListItems.setNote(Note);
        // contactList.add(contactListItems);

        CalcTotal();
        showList();


        // Gf_Calc_Promotion();
    }
    public   void InsertDiscount(String DiscountAmt , String DiscountType   ){
        double ItemWieght=0.0;

        for (int x = 0; x < contactList.size(); x++) {
            // ItemWieght=((SToD( contactList.get(x).getTotal())/SToD(tv_NetTotal.getText().toString()))*100);
            // contactList.get(x).setDisPerFromHdr((((ItemWieght*FinalDiscountpercent)/100))+"");
            contactList.get(x).setDisPerFromHdr("0");
            contactList.get(x).setDisAmtFromHdr("0");
        }

        CalcTotal();
        showList();
        if(DiscountType.equalsIgnoreCase("1")){
            FinalDiscountpercent=SToD(DiscountAmt);
            FinalDiscountAmt=((SToD(DiscountAmt)/100) * SToD(etTotal.getText().toString())) ;
            FinalDiscountAmt=SToD(FinalDiscountAmt+"");
        }else {
            if (IncludeTax_Flag.isChecked()) {
                FinalDiscountpercent = SToD(DiscountAmt) / SToD(tv_NetTotal.getText().toString());
                FinalDiscountpercent = FinalDiscountpercent * 100;
                FinalDiscountAmt = SToD(DiscountAmt);
                FinalDiscountpercent = SToD(FinalDiscountpercent + "");
            } else
            {
                FinalDiscountpercent = SToD(DiscountAmt) / SToD(etTotal.getText().toString());
                FinalDiscountpercent = FinalDiscountpercent * 100;
                FinalDiscountAmt = SToD(DiscountAmt);
                FinalDiscountpercent = SToD(FinalDiscountpercent + "");
            }
        }
        FinalDiscountType=DiscountType;
        tv_CustNetTotal1.setText(SToD((FinalDiscountpercent)+"")+"%");


        for (int x = 0; x < contactList.size(); x++) {

            contactList.get(x).setDisPerFromHdr(FinalDiscountpercent+"" );
            contactList.get(x).setDisAmtFromHdr(( (FinalDiscountpercent*(SToD(contactList.get(x).getTotal())    ))/100)+"" );

        }

        CalcTotal();
        showList();

        Save_Recod_Po();
    }
    public void show_dis(View v)
    {
        Bundle bundle = new Bundle();
        bundle.putString("OrederNo",OrderNo.getText().toString());
        bundle.putString("Discount","0");
        if(IncludeTax_Flag.isChecked()) {
            bundle.putString("NetTotal", tv_NetTotal.getText().toString());
        }
        else
        {
            bundle.putString("NetTotal", etTotal.getText().toString());
        }
        FragmentManager Manager =  getFragmentManager();
        PopEnterReHeaderDiscount1 obj = new PopEnterReHeaderDiscount1();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
}

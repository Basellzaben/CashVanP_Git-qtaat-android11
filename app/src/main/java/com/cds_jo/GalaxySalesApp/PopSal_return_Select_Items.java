package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Bill_Info;
import com.cds_jo.GalaxySalesApp.assist.Cls_Bill_info_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Inv_Deptf_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Methdes.MyTextView;


public class PopSal_return_Select_Items extends DialogFragment implements View.OnClickListener{

    public static final String CALCULATOR_PACKAGE = "com.android.calculator2";
    public static final String CALCULATOR_CLASS = "com.android.calculator2.Calculator";
    View form;
    ImageButton OpenCal;
    Button btn_Inc, btn_Dec, add, cancel;
    ListView items_Lsit;
    TextView itemnm;
    TextView Store_Qty;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0;
    ArrayList<Cls_Bill_Info> Bill_Info_List;
    int ResultCode;
    Double min_price = 0.0;
    Double Custdis = 0.0;
    Double Custprice = 0.0;
    EditText filter;
    ImageButton btn_filter_search;
    String UnitNo = "";
    String Operand, ItemWieght = "";
    String pass = "", TotalWeight = "0";
    String UnitName = "";
    String str = "";
    RadioButton rad_Per;
    RadioButton rad_Amt;

    List<Cls_Sal_InvItems> UpdateItem;
    int AllowSalInvMinus;
    EditText input, Weight, Price;

    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    String Cust_No = "";
    MyTextView tv_SelectedItem;
    RadioButton chkDamaged,chkNotDamaged;
    EditText et_Note;
    String Damaged;
    MyTextView  tv_AllowQty;
    MyTextView tv_Avg_price;
    ListView lst_Bill_Info ;
    private Double SToD(String str) {
        str = str.replaceAll("[^\\d.]", "");
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

    @Override
    public void onStart() {
        super.onStart();

// safety check
        if (getDialog() == null)
            return;

        int dialogWidth = (WindowManager.LayoutParams.WRAP_CONTENT);//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {

        form = inflater.inflate(R.layout.fragment_pop_sal_return__select__items, container, false);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP | Gravity.LEFT);

        getDialog().setTitle("Galaxy");
        FillDeptf();
        ComInfo.ComNo = Integer.parseInt(DB.GetValue(getActivity(), "ComanyInfo", "CompanyID", "1=1"));
        final Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);

        item_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FillItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        LinearLayout LytDis = (LinearLayout) form.findViewById(R.id.LytDis);


        add = (Button) form.findViewById(R.id.btn_add_item);
        add.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        add.setOnClickListener(this);

        cancel = (Button) form.findViewById(R.id.btn_cancel_item);
        cancel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        cancel.setOnClickListener(this);

           tv_AllowQty = (MyTextView) form.findViewById(R.id.tv_AllowQty);
          tv_Avg_price = (MyTextView) form.findViewById(R.id.tv_Avg_price);


        OpenCal = (ImageButton) form.findViewById(R.id.btn_OpenCal);


        OpenCal.setOnClickListener(this);

        btn_Inc = (Button) form.findViewById(R.id.btn_Inc);
        btn_Inc.setOnClickListener(this);


        btn_Dec = (Button) form.findViewById(R.id.btn_Dec);
        btn_Dec.setOnClickListener(this);
        Weight = (EditText) form.findViewById(R.id.et_Weight);
        tv_SelectedItem = (MyTextView) form.findViewById(R.id.tv_SelectedItem);
        tv_SelectedItem.setText("إدخال المواد ");
        AllowSalInvMinus = Integer.parseInt(DB.GetValue(this.getActivity(), "ComanyInfo", "AllowSalInvMinus", "1=1"));

        chkDamaged=(RadioButton)form.findViewById(R.id.chkDamaged);
        chkNotDamaged=(RadioButton)form.findViewById(R.id.chkNotDamaged);

        et_Note=(EditText)form.findViewById(R.id.et_Note);
        items_Lsit = (ListView) form.findViewById(R.id.listView2);
        lst_Bill_Info = (ListView) form.findViewById(R.id.lst_Bill_Info);

        items_Lsit.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
// Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
// Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

// Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        lst_Bill_Info.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
// Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
// Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

// Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        Price = (EditText) form.findViewById(R.id.et_price);
        Price.setEnabled(false);
        final EditText qty = (EditText) form.findViewById(R.id.et_qty);


        final EditText bo = (EditText) form.findViewById(R.id.et_bo);
        final EditText dis_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);



        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);


        sqlHandler = new SqlHandler(getActivity());


        dis_Amt.setFocusable(false);
        dis_Amt.setEnabled(false);
        dis_Amt.setCursorVisible(false);














        Price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Price.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });
        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                }
            }

        });

        bo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    bo.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });


        filter = (EditText) form.findViewById(R.id.et_Search_filter);

        filter.setInputType(InputType.TYPE_NULL);

        filter.setInputType(InputType.TYPE_CLASS_TEXT);
        filter.requestFocus();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                FillItems();


            }
        });
        Price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                get_total();
            }
        });

        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                get_total();

            }
        });


        bo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        FillItems();

        final Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);
        sp_unite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(position);

                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;
                Double LastPrice = 0.0;
                LastPrice = GetCustLastPrice(ItemNo, o.getUnitno().toString());
             /*   if (ComInfo.ComNo == 1 || ComInfo.ComNo == Companies.tariget.getValue() || ComInfo.ComNo == Companies.Ukrania.getValue() || ComInfo.ComNo == Companies.Arabian.getValue()) {
                    if (LastPrice > 0) {
                        Price.setText(LastPrice.toString().replace(",", ""));
                    } else {
                        Price.setText(String.valueOf(df.format(SToD(o.getPrice().toString()))).replace(",", ""));
                    }
                }*/

                UnitNo = o.getUnitno().toString();
                UnitName = o.getUnitDesc().toString();
                Operand = o.getOperand().toString();
                min = Float.valueOf(o.getMin());
                Weight.setText(o.getWeight().toString());
                TotalWeight = o.getWeight().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Double Qty = 0.0;
                arg1.setBackgroundColor(Color.GRAY);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;

                Cls_Invf o = (Cls_Invf) items_Lsit.getItemAtPosition(position);


                EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);

                EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
                EditText et_totl = (EditText) form.findViewById(R.id.et_totl);
                et_totl.setError(null);
                et_qty.setText("1");
                et_qty.clearFocus();
                et_Discount.setText("");


                EditText tax = (EditText) form.findViewById(R.id.et_tax);
                tax.setText(o.getTax().toString());


                str = (String) o.getItem_Name();


                getDialog().setTitle(str);
                tv_SelectedItem.setText(str);
                fillUnit(o.getItem_No().toString());
                ItemNo = o.getItem_No().toString();



                Price.setError(null);
                Price.clearFocus();
                et_qty.setError(null);
                tax.setError(null);
                get_min_price();

                Do_Trans_Server_Data();

            }
        });

        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    qty.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        Price.requestFocus();
        if (ComInfo.ComNo != 1) {

            bo.setEnabled(true);

        }


        if (ComInfo.ComNo == 3) {

            rad_Per.setChecked(true);
            rad_Per.setEnabled(false);
            rad_Amt.setEnabled(false);
            Price.setEnabled(false);

        }

        if (getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    return false;
                }
            });
        }
        Cust_No = getArguments().getString("CustomerNo");
        return form;
    }
private  void fill_Bill_info_list(){

    EditText Price = (EditText) form.findViewById(R.id.et_price);
    if (Bill_Info_List.size()>0) {
        tv_AllowQty.setText(Bill_Info_List.get(0).getAll_qty());
        tv_Avg_price.setText(Bill_Info_List.get(0).getAll_price());
        Price.setText(tv_Avg_price.getText().toString());
    }else{
        tv_AllowQty.setText("0");
    }
    Cls_Bill_info_Adapter cls_bill_info_adapter = new Cls_Bill_info_Adapter(
            getActivity(), Bill_Info_List);

    lst_Bill_Info.setAdapter(cls_bill_info_adapter);
}
    public void EnbledPrice() {
//Price


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
        Button create, show, setting;

        String Man = DB.GetValue(getActivity(), "Tab_Password", "ManNo", "PassNo = 11 AND ManNo ='" + UserID + "'");

        final String pass = DB.GetValue(getActivity(), "Tab_Password", "Password", "PassNo = 11  and ManNo='" + Man + "'");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(DB.GetValue(getActivity(), "Tab_Password", "PassDesc", "PassNo = 11 and ManNo='" + Man + "'"));
        alertDialog.setMessage("ادخل رمز التحقق");

        final EditText input = new EditText(getActivity());
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

                        if (pass.equalsIgnoreCase(password)) {
                            Price.setEnabled(true);
                        } else {
                            Price.setEnabled(false);
                            Toast.makeText(getActivity(),
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
    public void Do_Trans_Server_Data() {
        Bill_Info_List = new ArrayList<Cls_Bill_Info>();
        Bill_Info_List.clear();
        fill_Bill_info_list();
        tv = new TextView(getActivity());
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(getActivity());
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("مبيعات العميل");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        String MaxSeer = "1";


        final String Ser = "1";

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                ws.Get_Cust_Bill_Info(UserID, Cust_No, ItemNo);
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_bill = js.getJSONArray("bill");
                    JSONArray js_Tr_Date = js.getJSONArray("Tr_Date");
                    JSONArray js_tot = js.getJSONArray("tot");
                    JSONArray js_price2 = js.getJSONArray("price2");
                    JSONArray js_qty = js.getJSONArray("qty");
                    JSONArray js_bonus= js.getJSONArray("bonus");
                    JSONArray js_price = js.getJSONArray("price");
                    JSONArray js_All_price = js.getJSONArray("All_price");
                    JSONArray js_retuen_qty = js.getJSONArray("retuen_qty");
                    JSONArray js_SumWithOutTax = js.getJSONArray("SumWithOutTax");
                    JSONArray js_All_qty = js.getJSONArray("All_qty");


                    Bill_Info_List = new ArrayList<Cls_Bill_Info>();
                    Bill_Info_List.clear();
                    Cls_Bill_Info obj;
                    for (i = 0; i < js_bill.length(); i++) {

                        obj=new Cls_Bill_Info();
                        obj.setBill(js_bill.get(i).toString());
                        obj.setTr_Date(js_Tr_Date.get(i).toString());
                        obj.setTot(js_tot.get(i).toString());
                        obj.setPrice2(js_price2.get(i).toString());
                        obj.setQty(js_qty.get(i).toString());
                        obj.setBonus(js_bonus.get(i).toString());
                        obj.setPrice(js_price.get(i).toString());
                        obj.setAll_price(js_All_price.get(i).toString());
                        obj.setRetuen_qty(js_retuen_qty.get(i).toString());
                        obj.setSumWithOutTax(js_SumWithOutTax.get(i).toString());
                        obj.setAll_qty(js_All_qty.get(i).toString());

                        Bill_Info_List.add(obj);
                        custDialog.setMax(js_bill.length());
                        custDialog.incrementProgressBy(1);
                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {


                            custDialog.dismiss();
                            fill_Bill_info_list();




                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            try{
                            custDialog.dismiss();

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();

                            alertDialog.setTitle("مبيعات العميل");
                            if (We_Result.ID == -1) {
                                alertDialog.setMessage("لا يوجد مبيعات للعميل ");
                            } else {
                                alertDialog.setMessage("مشكلة في عملية الاتصال بالسيرفر الرئيسي");
                            }
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                        }catch ( Exception t){}
                        }
                    });
                }
            }
        }).start();
    }
    private double GetCustLastPrice(String ItemNo, String UnitNo) {
        Double r = 0.0;

        if (ComInfo.ComNo != Companies.Ukrania.getValue()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String CustNo = sharedPreferences.getString("CustNo", "");
            String q = "select ifnull(Price,0) as price  from CustLastPrice where " +
                    " Item_No = '" + ItemNo + "' and Cust_No ='" + CustNo + "' and  Unit_No = '" + UnitNo + "'";
            Cursor c = sqlHandler.selectQuery(q);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                r = SToD(c.getString(c.getColumnIndex("price")));
                c.close();
            }
        }
        return r;
    }
    private void get_min_price() {

        if (ComInfo.ComNo == Companies.Arabian.getValue()) {
            Cust_No = getArguments().getString("CustomerNo");
            String q = " Select  ifnull( Price,0) as min_price ,ifnull(Price,0) as Price  , ifnull(Dis,0) as dis " +
                    "   from PriceList where Item_No = '" + ItemNo + "'   " +
                    "   And Cust_No = '" + Cust_No + "' and Unit_No ='" + UnitNo + "'";
            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    min_price = SToD(Operand) * SToD(c1.getString(c1.getColumnIndex("min_price")));
                    min_price = SToD(min_price.toString());
                    Custdis = SToD(c1.getString(c1.getColumnIndex("dis")));
                    Custdis = SToD(Custdis.toString());
                    Custprice = SToD(c1.getString(c1.getColumnIndex("Price")));

                    if (Custprice > 0) {
                        Price.setText(SToD(Custprice.toString()) + "");
                    }


                }
                c1.close();
            }
        } else {


            min_price = 0.0;
            String CatNo = "";
            CatNo = getArguments().getString("CatNo");


            if (ComInfo.ComNo == 2) {
                CatNo = "3";
            }

            if (CatNo != "0") {
                String q = " Select  ifnull( MinPrice,0) as min_price ,ifnull(Price,0) as Price  , ifnull(dis,0) as dis " +
                        "   from Items_Categ where ItemCode = '" + ItemNo + "'   " +
                        "   And CategNo = '" + CatNo + "'";

                Cursor c1 = sqlHandler.selectQuery(q);
                if (c1 != null && c1.getCount() != 0) {
                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        if (Operand == null) {
                            Operand = "1";
                        }
                        min_price = SToD(Operand) * SToD(c1.getString(c1.getColumnIndex("min_price")));
                        min_price = SToD(min_price.toString());
                        Custdis = SToD(c1.getString(c1.getColumnIndex("dis")));
                        Custdis = SToD(Custdis.toString());
                        Custprice = SToD(c1.getString(c1.getColumnIndex("Price")));

                        if (ComInfo.ComNo == 2) {

                            if (Custprice > 0) {
                                Price.setText(SToD(Custprice.toString()) + "");
                            }
                            Toast.makeText(getActivity(), "سعر التجزئة :" + ":" + String.valueOf(Custprice), Toast.LENGTH_SHORT).show();
                        } else if (ComInfo.ComNo == 3) {
                            if (Custprice > 0) {
                                Price.setText(SToD(Custprice.toString()) + "");
                            }

                        } else {
                            Price.setText(SToD(Custprice.toString()) + "");
                        }
                    }
                    c1.close();
                } else {

                    String LocalPrice = "0.0";
                    LocalPrice = DB.GetValue(getActivity(), "UnitItems", "price", "item_no='" + ItemNo + "' And Min='1'");
                    Toast.makeText(getActivity(), "الفئة العميل غير معرفة :" + " " + String.valueOf(LocalPrice), Toast.LENGTH_SHORT).show();
                    min_price = SToD(LocalPrice.toString());
                    Custdis = SToD("0");
                    Custprice = SToD(LocalPrice.toString());
                    Price.setText(SToD(LocalPrice.toString()) + "");
                }
            }
        }
        EditText bo = (EditText) form.findViewById(R.id.et_bo);
        bo.requestFocus();
    }
    private void FillDeptf() {
        final Spinner sp_items_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        sqlHandler = new SqlHandler(getActivity());

        String query = "Select   distinct Type_No , Type_Name , etname from deptf";
        ArrayList<Cls_Deptf> cls_deptfs = new ArrayList<Cls_Deptf>();
        cls_deptfs.clear();

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Deptf clsDeptfs = new Cls_Deptf();

                    clsDeptfs.setType_No(c1.getString(c1
                            .getColumnIndex("Type_No")));
                    clsDeptfs.setType_Name(c1.getString(c1
                            .getColumnIndex("Type_Name")));

                    cls_deptfs.add(clsDeptfs);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        Cls_Inv_Deptf_Adapter cls_unitItems_adapter = new Cls_Inv_Deptf_Adapter(
                getActivity(), cls_deptfs);

        sp_items_cat.setAdapter(cls_unitItems_adapter);
    }
    private void FillItems() {
        filter = (EditText) form.findViewById(R.id.et_Search_filter);
        String query = "";
        sqlHandler = new SqlHandler(getActivity());


        Bundle bundle = this.getArguments();
        UpdateItem = (List<Cls_Sal_InvItems>) bundle.getSerializable("List");

        if (UpdateItem != null) {

            if (UpdateItem.size() > 0) {
                String s = UpdateItem.get(0).getno().toString();
                query = "Select  distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  where   invf.Item_No ='" + UpdateItem.get(0).getno().toString() + "'";

                EditText qty = (EditText) form.findViewById(R.id.et_qty);
                EditText tax = (EditText) form.findViewById(R.id.et_tax);

                EditText bo = (EditText) form.findViewById(R.id.et_bo);

                EditText bounce = (EditText) form.findViewById(R.id.et_bo);
                EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
                EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);

                EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);
                et_Note=(EditText)form.findViewById(R.id.et_Note);

                EditText net_total = (EditText) form.findViewById(R.id.et_totl);

                String Damaged="";
                chkDamaged=(RadioButton)form.findViewById(R.id.chkDamaged);
                chkNotDamaged=(RadioButton)form.findViewById(R.id.chkNotDamaged);

                ItemNo = UpdateItem.get(0).getno();
                Price.setText(UpdateItem.get(0).getprice());
                qty.setText(UpdateItem.get(0).getQty());
                tax.setText(UpdateItem.get(0).getTax());
                getDialog().setTitle(UpdateItem.get(0).getName());
                str = UpdateItem.get(0).getName();
                bounce.setText(UpdateItem.get(0).getBounce());
                disc_per.setText(UpdateItem.get(0).getDiscount());
                disc_Amt.setText(UpdateItem.get(0).getDis_Amt());
                et_Discount.setText(UpdateItem.get(0).getDiscount());
                net_total.setText(UpdateItem.get(0).getTotal());
                Damaged.equals(UpdateItem.get(0).getDamaged());
                if(Damaged.equalsIgnoreCase("1")){
                    chkDamaged.isChecked();
                }
                else{
                    chkNotDamaged.isChecked();
                }
                et_Note.setText(UpdateItem.get(0).getNote());


                fillUnit(UpdateItem.get(0).getno());

                Cls_UnitItems cls_unitItems = new Cls_UnitItems();
                Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);

                Cls_UnitItems_Adapter UnitItems = (Cls_UnitItems_Adapter) sp_unite.getAdapter();
                for (int i = 0; i < UnitItems.getCount(); i++) {
                    cls_unitItems = (Cls_UnitItems) UnitItems.getItem(i);


                    if (cls_unitItems.getUnitno().equals(UpdateItem.get(0).getUnite())) {
                        sp_unite.setSelection(i);
                        break;
                    }
                }

            }
        } else {


            if (getArguments().getString("Scr") == "Sal_return") {

                if (AllowSalInvMinus == 1 || ComInfo.DocType == 2) {
                    if (filter.getText().toString().equals("")) {
                        query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  ";

                    } else {
                        query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                                "    where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
                    }
                } else {
                    if (filter.getText().toString().equals("")) {
                        query = "Select   distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                                " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where 1=1 ";
                    } else {
                        query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                                " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
                    }
                }

                Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
                Integer indexValue = item_cat.getSelectedItemPosition();

                if (indexValue > 0) {

                    Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

                    query = query + "and    Type_No = '" + o.getType_No().toString() + "'";

                }


            }

        }

        Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        Integer indexValue = item_cat.getSelectedItemPosition();

        if (indexValue > 0) {

            Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

            query = query + "and    Type_No = '" + o.getType_No().toString() + "'";

        }





        ArrayList<Cls_Invf> cls_invf_List = new ArrayList<Cls_Invf>();
        cls_invf_List.clear();
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Invf cls_invf = new Cls_Invf();

                    cls_invf.setItem_No(c1.getString(c1
                            .getColumnIndex("Item_No")));
                    cls_invf.setItem_Name(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    cls_invf.setPrice(c1.getString(c1
                            .getColumnIndex("Price")));
                    cls_invf.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                   /* cls_invf.setDamaged(c1.getString(c1
                            .getColumnIndex("Damaged")));
                    cls_invf.setNote(c1.getString(c1
                            .getColumnIndex("Note")));*/
                    cls_invf_List.add(cls_invf);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        Cls_Invf_Adapter cls_invf_adapter = new Cls_Invf_Adapter(
                getActivity(), cls_invf_List);
        items_Lsit.setAdapter(cls_invf_adapter);


    }
    private void UpdateWrongCode(String Code) {

        if (Code.toString().equals("")) {

            return;

        }
        String q;
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u = sharedPreferences.getString("UserID", "");

        q = "INSERT INTO UsedCode(Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo , Posted ) values ('0"
                + "','" + Code
                + "','" + getArguments().getString("OrderNo")
                + "','" + getArguments().getString("CustomerNo")
                + "','" + ItemNo
                + "','" + currentDateandTime.toString()
                + "','" + StringTime.toString()
                + "','" + u
                + "','-1" + "')";
        sqlHandler.executeQuery(q);


    }


    public void fillUnit(String item_no) {


        Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);
        sqlHandler = new SqlHandler(getActivity());


        String Strorder = "";
        if (ComInfo.ComNo == 3) { // salasel
            Strorder = " UnitItems.unitno desc ";
        } else if (ComInfo.ComNo == 2) {
            Strorder = " UnitItems.Min desc ";
        } else {
            Strorder = " UnitItems.unitno desc ";
        }


        String query = "Select  distinct  ifnull(UnitItems.Weight,0) as weight,    UnitItems.unitno ,UnitItems.Min ,UnitItems.price,Unites.UnitName " +
                " , ifnull(Operand,1) as Operand from UnitItems  left join  Unites on Unites.Unitno =UnitItems.unitno  " +
                "   where UnitItems.item_no ='" + item_no + "' order by  " + Strorder;
        ArrayList<Cls_UnitItems> cls_unitItemses = new ArrayList<Cls_UnitItems>();
        cls_unitItemses.clear();

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_UnitItems cls_unitItems = new Cls_UnitItems();

                    cls_unitItems.setUnitno(c1.getString(c1
                            .getColumnIndex("unitno")));
                    cls_unitItems.setUnitDesc(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    cls_unitItems.setPrice(c1.getString(c1
                            .getColumnIndex("price")));
                    cls_unitItems.setMin(c1.getString(c1
                            .getColumnIndex("Min")));
                    cls_unitItems.setOperand(c1.getString(c1
                            .getColumnIndex("Operand")));
                    cls_unitItems.setWeight(c1.getString(c1
                            .getColumnIndex("weight")));

                    cls_unitItemses.add(cls_unitItems);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        Cls_UnitItems_Adapter cls_unitItems_adapter = new Cls_UnitItems_Adapter(
                getActivity(), cls_unitItemses);

        sp_unite.setAdapter(cls_unitItems_adapter);

        if (cls_unitItemses.size() > 0) {
            Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(0);
            UnitNo = o.getUnitno().toString();
            UnitName = o.getUnitDesc().toString();
            Operand = o.getOperand().toString();
            min = Float.valueOf(o.getMin());
            Weight.setText(o.getWeight());
        }

    }

    @Override
    public void onClick(View v) {
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText Weight = (EditText) form.findViewById(R.id.et_Weight);
        if (v == OpenCal) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(
                    CALCULATOR_PACKAGE,
                    CALCULATOR_CLASS));

            try {

                startActivity(intent);
            } catch (Exception noSuchActivity) {
                Toast.makeText(getActivity(), noSuchActivity.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }

        }


        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        if (v == cancel) {
            this.dismiss();
        } else if (v == btn_Inc) {


            qty.setText((Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) + 1) + "");
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) > 1) {
                btn_Dec.setVisibility(View.VISIBLE);
            }
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) < 1) {
                qty.setText(SToD(qty.getText().toString().replaceAll("[^\\d.]", "")) + "");
            }

            get_total();

        } else if (v == btn_Dec) {
            qty.setText((Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) - 1) + "");
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) < 1) {
                qty.setText("1");
                btn_Dec.setVisibility(View.INVISIBLE);
            }

            get_total();

        } else if (v == add) {

            EditText Price = (EditText) form.findViewById(R.id.et_price);




            EditText tax = (EditText) form.findViewById(R.id.et_tax);

            EditText bo = (EditText) form.findViewById(R.id.et_bo);

            EditText bounce = (EditText) form.findViewById(R.id.et_bo);
            EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
            EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);

            EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);
            TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
            TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);


            EditText net_total = (EditText) form.findViewById(R.id.et_totl);

            RadioButton chkDamaged=(RadioButton)form.findViewById(R.id.chkDamaged);
            RadioButton chkNotDamaged=(RadioButton)form.findViewById(R.id.chkNotDamaged);

            et_Note=(EditText)form.findViewById(R.id.et_Note);
            Double PriceAftertAx = 0.0;
            net_total.setError(null);
            net_total.clearFocus();
            Price.setError(null);
            net_total.setError(null);
            qty.setError(null);
            tax.setError(null);


           // get_min_price();
           /* if (Price.getText().toString().length() > 0 && SToD(Price.getText().toString().replaceAll("[^\\d.]", "")) > 0 && (SToD(Price.getText().toString()) < min_price)) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("الحد الادني لبيع المادة ");

                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setMessage("لقد تجاوزت الحد الادنى للسعر");//+ "   " + String.valueOf(min_price));

                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });



                alertDialog.show();
                return;
            }*/



            if (SToD(qty.getText().toString())> SToD(tv_AllowQty.getText().toString())   ) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("لقد تجاوزت الكمية المسموحة");

                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setMessage("الكمية المسموحة"+ tv_AllowQty.getText().toString());//+ "   " + String.valueOf(min_price));
                alertDialog.setButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                return;
            }



                if (SToD(net_total.getText().toString()) <= 0) {
                    net_total.setError("الرجاء ادخال القيمة");
                    if (SToD(bounce.getText().toString()) == 0) {
                        net_total.requestFocus();
                        return;
                    }
                }

                if (SToD(qty.getText().toString()) >= 9000) {
                    qty.setError("الرجاء ادخال القيمة");
                    if (SToD(bounce.getText().toString()) == 0) {
                        qty.requestFocus();
                        return;
                    }
                }

                if ((SToD(Price.getText().toString()) == 100000) || Price.getText().toString().length() > 10 || (SToD(Price.getText().toString())).toString().contains("E")) {
                    Price.setError("الرجاء التأكد من القيمة");
                    if (SToD(bounce.getText().toString()) == 0) {
                        Price.requestFocus();
                        return;
                    }
                }
                if (Price.getText().toString().length() == 0) {
                    Price.setError("الرجاء ادخال القيمة");
                    if (SToD(bounce.getText().toString()) == 0) {
                        Price.requestFocus();
                        return;
                    }
                }

                if (qty.getText().toString().length() == 0) {
                    qty.setError("الرجاء ادخال القيمة");
                    if (SToD(bounce.getText().toString()) == 0) {
                        qty.requestFocus();
                        return;
                    }
                }
                if (tax.getText().toString().length() == 0) {
                    tax.setError("الرجاء ادخال القيمة");
                    if (SToD(bounce.getText().toString()) == 0) {
                        tax.requestFocus();
                        return;
                    }
                }


                if (SToD(net_total.getText().toString()) <= 0) {
                    net_total.setError("الرجاء ادخال القيمة");
                    net_total.requestFocus();
                    return;
                }

                if (SToD(qty.getText().toString()) >= 9000) {
                    qty.setError("الرجاء ادخال القيمة");
                    qty.requestFocus();
                    return;
                }

                if ((SToD(Price.getText().toString()) == 100000) || Price.getText().toString().length() > 10 || (SToD(Price.getText().toString())).toString().contains("E")) {
                    Price.setError("الرجاء التأكد من القيمة");
                    Price.requestFocus();
                    return;
                }
                if (Price.getText().toString().length() == 0) {
                    Price.setError("الرجاء ادخال القيمة");
                    Price.requestFocus();
                    return;
                }

                if (qty.getText().toString().length() == 0) {
                    qty.setError("الرجاء ادخال القيمة");
                    qty.requestFocus();
                    return;
                }
                if (tax.getText().toString().length() == 0) {
                    tax.setError("الرجاء ادخال القيمة");
                    tax.requestFocus();
                    return;
                }
                if (chkDamaged.isChecked() || chkNotDamaged.isChecked() ) {
                    if (chkDamaged.isChecked()==true){
                        Damaged="1";
                    }
                    if (chkNotDamaged.isChecked()==true){
                        Damaged="0";
                    }
                }
                else {
                    chkDamaged.setError("الرجاء ادخال حالة المادة");
                    chkDamaged.requestFocus();
                    return;
                }



            if (ItemNo.toString().length() == 0) {
                return;
            }





            if (getArguments().getString("Scr") == "Sal_return") {

                    ((Sale_ReturnActivity) getActivity()).Save_List(ItemNo, Price.getText().toString().replaceAll("[^\\d.]", "").replace(",", ""), qty.getText().toString() , tax.getText().toString() , UnitNo, disc_per.getText().toString() , bounce.getText().toString().replace(",", ""), str, UnitName, disc_Amt.getText().toString() , Operand , Weight.getText().toString() ,Damaged,et_Note.getText().toString());


            }

            try {
                min = 0;
                Price.requestFocus();
                Price.setText("0");
                qty.setText("1");
                tax.setText("");
                bo.setText("");
                dis.setText("0");
                disc_per.setText("0");
                disc_Amt.setText("0");
                net_total.setText("");
                ItemNo = "";
                UnitNo = "";
                Operand = "";
                tv_qty_perc.setText("");
                tv_StoreQty.setText("");
                et_Discount.setText("");


                Price.clearFocus();
                net_total.clearFocus();
                qty.clearFocus();
                tax.clearFocus();
                Weight.setText("");
                TotalWeight = "0";
                et_Note.setText("");
                chkDamaged.setChecked(false);
                chkNotDamaged.setChecked(false);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                EditText et_Search_filter = (EditText) form.findViewById(R.id.et_Search_filter);

            } catch (Exception ex) {

            }
        }

    }


    public void get_total() {
        Double Perc = 0.0;
        EditText Price = (EditText) form.findViewById(R.id.et_price);
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText tax = (EditText) form.findViewById(R.id.et_tax);
        EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        EditText bo = (EditText) form.findViewById(R.id.et_bo);
        EditText net_total = (EditText) form.findViewById(R.id.et_totl);


        String str_p, str_q;


        TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);


        str_p = Price.getText().toString().replaceAll("[^\\d.]", "");
        str_q = qty.getText().toString().replaceAll("[^\\d.]", "");

        if (Price.getText().toString().length() == 0) {
            str_p = "0";
        }

        if (qty.getText().toString().length() == 0) {
            str_q = "0";
        }

        Double p = SToD(str_p);
        Double q = SToD(str_q);
        Double w = SToD(TotalWeight);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        net_total.setText(String.valueOf(df.format(p * q)).replace(",", ""));
        Weight.setText(String.valueOf(df.format(w * q)).replace(",", ""));

    }



    private void UnitItems() {

        tv = new TextView(getActivity());
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
        final Handler _handler = new Handler();
        progressDialog = new ProgressDialog(getActivity());
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
                CallWebServices ws = new CallWebServices(getActivity());
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
                    JSONArray js_Weight = js.getJSONArray("Weight");

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
                                + "','" + js_Weight.get(i).toString()
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

    private void UpdateCode(String Code) {
        String q;
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u = sharedPreferences.getString("UserID", "");
        Toast.makeText(getActivity(), "تم تفعيل الخصم  والبونص", Toast.LENGTH_SHORT).show();
        q = " Update RndNum set Flg = '1' where ifnull(Flg,'0') = '0' and Value = '" + Code + "'";
        sqlHandler.executeQuery(q);

        q = "INSERT INTO UsedCode(Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo , Posted ) values ('1"
                + "','" + Code
                + "','" + getArguments().getString("OrderNo")
                + "','" + getArguments().getString("CustomerNo")
                + "','" + ItemNo
                + "','" + currentDateandTime.toString()
                + "','" + StringTime.toString()
                + "','" + u
                + "','-1" + "')";
        sqlHandler.executeQuery(q);


        final EditText bo = (EditText) form.findViewById(R.id.et_bo);
        final EditText Discount = (EditText) form.findViewById(R.id.et_Discount);

        bo.setEnabled(true);
        Discount.setEnabled(true);

    }
}




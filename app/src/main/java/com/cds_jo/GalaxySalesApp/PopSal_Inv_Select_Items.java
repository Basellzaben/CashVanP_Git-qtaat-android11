
package com.cds_jo.GalaxySalesApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Inv_Deptf_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;
import com.google.gson.Gson;
import com.romainpiel.shimmer.Shimmer;

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
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class PopSal_Inv_Select_Items extends DialogFragment implements View.OnClickListener {
    public static final String CALCULATOR_PACKAGE = "com.android.calculator2";
    public static final String CALCULATOR_CLASS = "com.android.calculator2.Calculator";
    View form;
    com.romainpiel.shimmer.ShimmerTextView LiveCode;
    ImageButton OpenCal;
    Button btn_Inc, btn_Dec, add, cancel, btn_ShowCodePop;
    ListView items_Lsit;
    TextView itemnm;
    TextView Store_Qty;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0;
    String Manpassword = "";
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
    CheckBox chk_EnableDis, chkEnbledPrice;
    List<Cls_Sal_InvItems> UpdateItem;
    int AllowSalInvMinus;
    EditText input, Weight, Price;

    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    String Cust_No = "";
    MyTextView tv_SelectedItem, tv_MaxBounce, tv_MaxDiscount, tv_CodeLiveNote;
    SharedPreferences sharedPreferences;
    String UserID;

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

        form = inflater.inflate(R.layout.layout_pop_sal_inv_select_items, container, false);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.TOP | Gravity.LEFT);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        UserID = sharedPreferences.getString("UserID", "");


        getDialog().setTitle("Galaxy");
        FillDeptf();
        ComInfo.ComNo = Integer.parseInt(DB.GetValue(getActivity(), "ComanyInfo", "CompanyID", "1=1"));
        final Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        Manpassword = "";
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

        chkEnbledPrice = (CheckBox) form.findViewById(R.id.chkEnbledPrice);
        //chkEnbledPrice.setVisibility(View.INVISIBLE);
        add = (Button) form.findViewById(R.id.btn_add_item);
        add.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        add.setOnClickListener(this);

        cancel = (Button) form.findViewById(R.id.btn_cancel_item);
        cancel.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        cancel.setOnClickListener(this);

        btn_ShowCodePop = (Button) form.findViewById(R.id.btn_ShowCodePop);
        btn_ShowCodePop.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        btn_ShowCodePop.setOnClickListener(this);

        OpenCal = (ImageButton) form.findViewById(R.id.btn_OpenCal);


        OpenCal.setOnClickListener(this);

        btn_Inc = (Button) form.findViewById(R.id.btn_Inc);
        btn_Inc.setOnClickListener(this);


        btn_Dec = (Button) form.findViewById(R.id.btn_Dec);
        btn_Dec.setOnClickListener(this);
        Weight = (EditText) form.findViewById(R.id.et_Weight);
        tv_SelectedItem = (MyTextView) form.findViewById(R.id.tv_SelectedItem);

        tv_MaxBounce = (MyTextView) form.findViewById(R.id.tv_MaxBounce);
        tv_MaxDiscount = (MyTextView) form.findViewById(R.id.tv_MaxDiscount);
        tv_CodeLiveNote = (MyTextView) form.findViewById(R.id.tv_CodeLiveNote);
        tv_MaxDiscount.setText("0");
        tv_MaxBounce.setText("0");
        tv_CodeLiveNote.setText("");
        tv_SelectedItem.setText("إدخال المواد ");
        AllowSalInvMinus = Integer.parseInt(DB.GetValue(this.getActivity(), "ComanyInfo", "AllowSalInvMinus", "1=1"));

        items_Lsit = (ListView) form.findViewById(R.id.listView2);
        Cust_No = getArguments().getString("CustomerNo");
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
        Price = (EditText) form.findViewById(R.id.et_price);
        if (ComInfo.ComNo == 8) {
            Price.setEnabled(false);

        }

        final List<String> promotion_ls = new ArrayList<String>();


        final EditText qty = (EditText) form.findViewById(R.id.et_qty);


        final EditText bo = (EditText) form.findViewById(R.id.et_bo);
        final EditText dis_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);

        final EditText Discount = (EditText) form.findViewById(R.id.et_Discount);
        RadioGroup radGrp = (RadioGroup) form.findViewById(R.id.radDisc);

        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);


        sqlHandler = new SqlHandler(getActivity());


        dis_Amt.setFocusable(false);
        dis_Amt.setEnabled(false);
        dis_Amt.setCursorVisible(false);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                CalcDiscount();
            }


        });
        rad_Per = (RadioButton) form.findViewById(R.id.rad_Per);
        rad_Amt = (RadioButton) form.findViewById(R.id.rad_Amt);


        rad_Per.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        rad_Amt.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));


        bo.setEnabled(true);
        Discount.setEnabled(true);
        LinearLayout LytTransferQty = (LinearLayout) form.findViewById(R.id.LytTransferQty);
        LytTransferQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Do_Trans_Server_Data();
            }
        });


        chk_EnableDis = (CheckBox) form.findViewById(R.id.chkEnbleDis);
        chk_EnableDis.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));

        chk_EnableDis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         if (chk_EnableDis.isChecked()) {
                                                             if (ComInfo.ComNo == 2) {
                                                                 bo.setEnabled(true);
                                                                 Discount.setEnabled(true);
                                                                 Toast.makeText(getActivity(), "تم تفعيل الخصم  والبونص", Toast.LENGTH_SHORT).show();
                                                             } else {
                                                                 CheckCode();
                                                             }
                                                         } else {
                                                             bo.setEnabled(false);
                                                             Discount.setEnabled(false);
                                                         }
                                                     }
                                                 }
        );


        chkEnbledPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                      @Override
                                                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                          if (chkEnbledPrice.isChecked()) {
                                                              EnbledPrice();
                                                          }
                                                      }
                                                  }

        );


        Discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                CalcDiscount();

            }


        });
        Discount.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Discount.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });
        Discount.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Discount.setText("", TextView.BufferType.EDITABLE);
                return false;
            }
        });


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
//    qty.setText("", TextView.BufferType.EDITABLE);
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
                GetQtyPerc();
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
                GetQtyPerc();
            }
        });

        ListView lst_Promotion = (ListView) form.findViewById(R.id.lst_Bill_Info);

        String q = "Select  distinct * from Offers_Hdr ";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    promotion_ls.add(c1.getString(c1.getColumnIndex("Offer_Name")));

                } while (c1.moveToNext());

            }
            c1.close();
        }

        ArrayAdapter<String> promotion_ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, promotion_ls);

        lst_Promotion.setAdapter(promotion_ad);

        FillItems();
// fillUnit("-1");


        final Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);
        sp_unite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(position);

                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;
                Double LastPrice = 0.0;
                LastPrice = GetCustLastPrice(ItemNo, o.getUnitno().toString());
                if (ComInfo.ComNo == 1 || ComInfo.ComNo == Companies.tariget.getValue() || ComInfo.ComNo == Companies.Ukrania.getValue() || ComInfo.ComNo == Companies.Arabian.getValue()) {
                    if (LastPrice > 0) {
                        Price.setText(LastPrice.toString().replace(",", ""));
                    } else {
                        Price.setText(String.valueOf(df.format(SToD(o.getPrice().toString()))).replace(",", ""));
                    }
                }

                UnitNo = o.getUnitno().toString();
                UnitName = o.getUnitDesc().toString();
                Operand = o.getOperand().toString();
                min = Float.valueOf(o.getMin());
                Weight.setText(o.getWeight().toString());
                TotalWeight = o.getWeight().toString();
//get_min_price();
                checkStoreQty();
                if (ComInfo.ComNo == Companies.Arabian.getValue() || ComInfo.ComNo == Companies.beutyLine.getValue()) {
                    get_min_price();
                }
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
                Manpassword = "";

                EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);

                EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
                EditText et_totl = (EditText) form.findViewById(R.id.et_totl);
                et_totl.setError(null);
                et_qty.setText("1");
                et_qty.clearFocus();
                et_Discount.setText("");

//rad_Amt.setChecked(true);//Here
//Price.setText(df.format(Double.valueOf( o.getPrice())).toString());
                EditText tax = (EditText) form.findViewById(R.id.et_tax);
                tax.setText(o.getTax().toString());


                str = (String) o.getItem_Name();

// Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
                getDialog().setTitle(str);
                tv_SelectedItem.setText(str);
                fillUnit(o.getItem_No().toString());
                ItemNo = o.getItem_No().toString();

// itemnm.setText(str);

                Price.setError(null);
                Price.clearFocus();
                et_qty.setError(null);
                tax.setError(null);
                get_min_price();
                checkStoreQty();
                Price.setEnabled(false);

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
//rad_Amt.setChecked(true);//Here


        if (getArguments().getString("IsNew").equalsIgnoreCase("false") && UpdateItem != null && ComInfo.ComNo == 1) {

            chkEnbledPrice.setVisibility(View.VISIBLE);
            Price.setEnabled(false);

        }
        Price.requestFocus();
        if (ComInfo.ComNo != 1) {
            chk_EnableDis.setVisibility(View.INVISIBLE);
            bo.setEnabled(true);
            Discount.setEnabled(true);
        }
        //chkEnbledPrice.setVisibility(View.INVISIBLE);
        if (UpdateItem != null && ComInfo.ComNo == 1) {
            chkEnbledPrice.setVisibility(View.VISIBLE);
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
        if (ComInfo.ComNo == Companies.beutyLine.getValue() || ComInfo.ComNo == 1) {
            chk_EnableDis.setVisibility(View.GONE);
        }

        LiveCode = (com.romainpiel.shimmer.ShimmerTextView) form.findViewById(R.id.LiveCode);
        Shimmer shimmer = new Shimmer();
        shimmer.setRepeatCount(-1)
                .setDuration(2000)
                .setStartDelay(1500)
                .setDirection(Shimmer.ANIMATION_DIRECTION_RTL);
        LiveCode.setText("");
        shimmer.start(LiveCode);
        GetDefualtPercent();
        GetCodeForCustomer();
        return form;
    }

    public void EnbledPrice() {

        final EditText et_price = (EditText) form.findViewById(R.id.et_price);
        if (chkEnbledPrice.isChecked()) {
            CheckCode();
        } else {
            et_price.setEnabled(false);

        }


    }

    public void Do_Trans_Server_Data() {

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
        tv.setText("سنـــد التزويــــد");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        String MaxSeer = "1";


        final String Ser = "1";


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
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

                    q = "Delete from ManStore";
                    sqlHandler.executeQuery(q);
                    q = "delete from sqlite_sequence where name='ManStore'";
                    sqlHandler.executeQuery(q);

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


                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث كميات المستودع");
                            alertDialog.setMessage("تمت عملية التحديث بنجاح");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Do_Trans_Server_Data1();
                                    UnitItems();

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

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث كميات المستودع");
                            alertDialog.setMessage("مشكلة في عملية الاتصال بالسيرفر الرئيسي");
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

    private void Do_Trans_Server_Data1() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
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

        final ProgressDialog custDialog = new ProgressDialog(getActivity());
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("فئات اسعار بيع المواد");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
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

                            custDialog.dismiss();


                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث فئات الاسعار");
                            alertDialog.setMessage("تمت عملية التحديث بنجاح");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

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
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث فئات الاسعار");
                            alertDialog.setMessage("مشكلة في عملية الاتصال بالسيرفر الرئيسي");
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
/* if(ComInfo.ComNo ==1 ) {
Toast.makeText(getActivity(), "ادنى سعر للبيع " + ":" + String.valueOf(min_price), Toast.LENGTH_SHORT).show();
}*/
                    }
                    c1.close();
                } else {

                    String LocalPrice = "0.0";
                    LocalPrice = DB.GetValue(getActivity(), "UnitItems", "price", "item_no='" + ItemNo + "' And Min='1'");
                    //Toast.makeText(getActivity(), "الفئة العميل غير معرفة :" + " " + String.valueOf(LocalPrice), Toast.LENGTH_SHORT).show();
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

    private void checkStoreQty() {

        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());


        Double Order_qty = 0.0;
        Double Res = 0.0;


        String query = "SELECT   ifnull( qty,0)   as  qty   from ManStore where  itemno = '" + ItemNo + "'  ";
        Cursor c1 = sqlHandler.selectQuery(query);

        Double Store_qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            try {
                if (c1.getCount() > 0) {

                    c1.moveToFirst();
                    Store_qty = SToD(c1.getString(c1.getColumnIndex("qty")));
                    c1.close();
                }
            } catch (Exception exception) {
                Store_qty = 0.0;
            }
            c1.close();
        }


        query = "SELECT       (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                " where   sih.Post = -1  and ui.item_no ='" + ItemNo + "'  and sih.UserID='" + sharedPreferences.getString("UserID", "-1") + "'";
//   "    where  QtyStoreSer = "+MaxStoreQtySer+" and ui.item_no ='"+ItemNo+"'";


        if (UpdateItem != null && UpdateItem.size() > 0) {
            query = query + "And sid.OrderNo !='" + getArguments().getString("OrderNo") + "'";
        }
        c1 = sqlHandler.selectQuery(query);

        Double Sal_Qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                Sal_Qty = SToD((c1.getString(c1.getColumnIndex("Sal_Qty"))).toString());
            }

            c1.close();
        }
        Res = Store_qty - Sal_Qty - Order_qty;
        if (Operand == null) {
            Operand = "1";
        }
        Res = SToD(Res + "");
        if (SToD(Operand) == 0) {
            Res = 0.0;
        } else {
            Res = Res / SToD(Operand);
        }

        Store_Qty.setText(SToD(Res.toString()) + "");
/*  if(ComInfo.ComNo ==1 ) {
Toast.makeText(getActivity(), "كمية التزويد " + ":" + String.valueOf(Store_qty) +"    "  + "الكمية المباعة" + ":" + String.valueOf(Sal_Qty)  +"   "  + "Operand" + ":" + String.valueOf(Operand)
+ "   المسموحة "+ ":"+ String.valueOf(Res) , Toast.LENGTH_SHORT).show();
}*/


        GetQtyPerc();
    }


    private void CalcDiscount() {
        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        final EditText totl = (EditText) form.findViewById(R.id.et_totl);
        final EditText Discount = (EditText) form.findViewById(R.id.et_Discount);
        final EditText dis_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);
        final EditText et_totl = (EditText) form.findViewById(R.id.et_totl);

        final EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
        final EditText et_price = (EditText) form.findViewById(R.id.et_price);

        Double pq = 0.0;
        pq = SToD(et_price.getText().toString().replaceAll("[^\\d.]", "")) * SToD(et_qty.getText().toString().replaceAll("[^\\d.]", ""));
        if (rad_Per.isChecked()) {


            Double Total = ((SToD(Discount.getText().toString().replaceAll("[^\\d.]", "").replace(",", "")) / 100)) * pq;

            Total = SToD(Total.toString());

            dis_Amt.setText(String.valueOf((Total)));
            dis.setText(String.valueOf(Discount.getText().toString().replaceAll("[^\\d.]", "")));

        } else {
            Double Total = 0.0;
            if (pq == 0) {
                Total = 0.0;
            } else {
                Total = (SToD(Discount.getText().toString().replaceAll("[^\\d.]", "")) / pq) * 100;
            }

            Total = SToD(Total.toString());


            dis.setText(Total.toString());
            dis_Amt.setText(String.valueOf(SToD(Discount.getText().toString().replaceAll("[^\\d.]", ""))));

        }
        if (totl.getText().toString().equals("0")) {
            dis.setText("0");
            dis_Amt.setText("0");
        }

        et_totl.setText(String.valueOf(SToD(String.valueOf(pq - SToD(dis_Amt.getText().toString().replaceAll("[^\\d.]", ""))))));
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
                query = "Select  distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf    where   invf.Item_No ='" + UpdateItem.get(0).getno().toString() + "'";

                EditText qty = (EditText) form.findViewById(R.id.et_qty);
                EditText tax = (EditText) form.findViewById(R.id.et_tax);

                EditText bo = (EditText) form.findViewById(R.id.et_bo);
// Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);
                EditText bounce = (EditText) form.findViewById(R.id.et_bo);
                EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
                EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);

                EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);


                EditText net_total = (EditText) form.findViewById(R.id.et_totl);
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


            if (getArguments().getString("Scr") == "Sal_inv") {

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




/*  AlertView.showAlert( query,
getResources().getString(R.string.dev_check_msg), getActivity());*/
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
//  Toast.makeText(getActivity(), "يجب ادخال رمز التفعيل ",Toast.LENGTH_SHORT).show();
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


    private void CheckCode() {
        final EditText et_price = (EditText) form.findViewById(R.id.et_price);
        et_price.setEnabled(false);
        Manpassword = "";
        if (ItemNo.toString().equals("")) {
            Toast.makeText(getActivity(), "الرجاء اختيار المادة اولا", Toast.LENGTH_SHORT).show();
            chk_EnableDis.setChecked(false);
            return;
        }
        pass = "";
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.setContentView(R.layout.custom_dialog);

        dialog.setTitle("الرجاء إدخال الكود");
        dialog.setCancelable(true);
        input = (EditText) dialog.findViewById(R.id.et_Input);

        Button Accept = (Button) dialog.findViewById(R.id.Accept);
        Accept.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));

        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = DB.GetValue(getActivity(), "RndNum", "Value", " ifnull(Flg,'0') = '0' and Value = '" + input.getText().toString() + "'");
                String password = input.getText().toString();
                if (pass.equals(password)) {
                    Manpassword = password;
                    et_price.setEnabled(true);
                    Toast.makeText(getActivity(), "تم تفعيل تعديل السعر", Toast.LENGTH_SHORT).show();
                    // UpdateCode_ForPrice(password);
                    ResultCode = 1;
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    HideKeypad();
                    dialog.dismiss();
                } else {
                    chk_EnableDis.setChecked(false);
                    chk_EnableDis.setChecked(true);
                    UpdateWrongCode(password);
                    Toast.makeText(getActivity(), "رمز التحقق غير مقبول", Toast.LENGTH_SHORT).show();
                    ResultCode = 0;
                    input.setText("");
                    dialog.dismiss();
                    HideKeypad();

                }
            }

        });

        Button btnCancel = (Button) dialog.findViewById(R.id.Cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    HideKeypad();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {

                }
                chkEnbledPrice.setChecked(false);
               /* EditText d = (EditText) form.findViewById(R.id.et_disc_per);
                d.setFocusable(true);
                d.setInputType(InputType.TYPE_NULL);*/
                dialog.dismiss();
            }
        });
        dialog.show();
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
//startActivity(intent);

/*  Intent i = new Intent();
i.setAction(Intent.ACTION_MAIN);
i.addCategory(Intent.CATEGORY_APP_CALCULATOR);*/
            try {

                startActivity(intent);
            } catch (Exception noSuchActivity) {
                Toast.makeText(getActivity(), noSuchActivity.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }

        }


        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        if (v == cancel) {
            this.dismiss();
        } else if (v == btn_ShowCodePop) {
            ShowCodePop();
        } else if (v == btn_Inc) {

            try {
                qty.setText((Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) + 1) + "");
            } catch (Exception ex) {
                qty.setText("1");
            }
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) > 1) {
                btn_Dec.setVisibility(View.VISIBLE);
            }
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) < 1) {
                qty.setText(SToD(qty.getText().toString().replaceAll("[^\\d.]", "")) + "");
            }
            CalcDiscount();
            get_total();
            GetQtyPerc();
        } else if (v == btn_Dec) {
            qty.setText((Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) - 1) + "");
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) < 1) {
                qty.setText("1");
                btn_Dec.setVisibility(View.INVISIBLE);
            }
            CalcDiscount();
            get_total();
            GetQtyPerc();
        } else if (v == add) {
            Price = (EditText) form.findViewById(R.id.et_price);
            EditText bounce = (EditText) form.findViewById(R.id.et_bo);
            EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);
            double BouncePercent, DiscountPercent;
            BouncePercent = 0;
            DiscountPercent = 0;

            EditText et_disc_per = (EditText) form.findViewById(R.id.et_disc_per);

            final EditText final_P = (EditText) form.findViewById(R.id.et_price);

            EditText tax = (EditText) form.findViewById(R.id.et_tax);

            EditText bo = (EditText) form.findViewById(R.id.et_bo);
// Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);

            EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
            EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);


            TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
            TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);


            EditText net_total = (EditText) form.findViewById(R.id.et_totl);

            Double PriceAftertAx = 0.0;
            net_total.setError(null);
            net_total.clearFocus();
            Price.setError(null);
            net_total.setError(null);
            qty.setError(null);
            tax.setError(null);

            if (ComInfo.ComNo != 8) {
                get_min_price();
            }

            if (Price.getText().toString().length() > 0 && SToD(Price.getText().toString().replaceAll("[^\\d.]", "")) > 0 && (SToD(Price.getText().toString()) < min_price)) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("الحد الادني لبيع المادة ");

                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setMessage("لقد تجاوزت الحد الادنى للسعر");//+ "   " + String.valueOf(min_price));
// Setting OK Button
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//   final_P.setText(min_price.toString());


                    }
                });


// Showing Alert Message

                if (ComInfo.ComNo != 8) {
                    alertDialog.show();
                    return;
                }
            }
            if (ComInfo.ComNo != Companies.beutyLine.getValue()) {
                if (rad_Per.isChecked()) {
                    if (et_disc_per.getText().toString().length() > 0 && SToD(et_disc_per.getText().toString()) > 0 && (SToD(et_disc_per.getText().toString()) > Custdis)) {

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                getActivity()).create();
                        alertDialog.setTitle("الخصم حسب فئة العميل");

                        alertDialog.setIcon(R.drawable.delete);
                        alertDialog.setMessage("لقد تجاوزت الحد  الاعلى للخصم  ");//+ "   " + String.valueOf(min_price));
// Setting OK Button
                        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//   final_P.setText(min_price.toString());


                            }
                        });


                        alertDialog.show();
                        return;
                    }
                }
            }
            AllowSalInvMinus = Integer.parseInt(DB.GetValue(this.getActivity(), "ComanyInfo", "AllowSalInvMinus", "1=1"));
            if (AllowSalInvMinus != 1) {
                if (SToD(tv_qty_perc.getText().toString()) > 100) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("فاتورة مبيعات");
                    alertDialog.setMessage("الكمية المطلوبة غير متوفرة" + " %");            // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();
                    return;
                }
            }


            if (ComInfo.ComNo == 2) {

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
            } else {

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
            }


            if (ItemNo.toString().length() == 0) {

                return;
            }

            int i = 1;

            if (ComInfo.ComNo == Companies.beutyLine.getValue()) {


                BouncePercent = (SToD(bounce.getText() + "") / SToD(qty.getText() + "")) * 100;
                if (rad_Per.isChecked()) {
                    DiscountPercent = SToD(et_Discount.getText() + "");
                } else {
                    DiscountPercent = (SToD(et_Discount.getText() + "") / SToD(net_total.getText() + "")) * 100;
                }


                if (BouncePercent > SToD(tv_MaxBounce.getText() + "")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("لقد تجاوزت البونص المسموح به");
                    alertDialog.setMessage("النسبة المسموح بها" + ": " + tv_MaxBounce.getText().toString() + " " + "النسبة  الممنوحة" + ":" + BouncePercent);
                    alertDialog.setIcon(R.drawable.error_new);
                    alertDialog.setButton("رجوع", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                   /* alertDialog.show();
                    return;*/
                }

                if (DiscountPercent > SToD(tv_MaxDiscount.getText() + "")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("لقد تجاوزت الخصم المسموح به");
                    alertDialog.setMessage("النسبة المسموح بها" + ": " + tv_MaxDiscount.getText().toString() + "" + "النسبة  الممنوحة" + ":" + DiscountPercent);
                    alertDialog.setIcon(R.drawable.error_new);
                    alertDialog.setButton("رجوع", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                   /* alertDialog.show();
                    return;*/
                }

            }

            BouncePercent = SToD(BouncePercent + "");
            DiscountPercent = SToD(DiscountPercent + "");

            if (getArguments().getString("Scr") == "Sal_inv") {


                if (UpdateItem != null) {
                    if (UpdateItem.size() > 0) {
                        ((Sale_InvoiceActivity) getActivity()).Update_List(ItemNo, Price.getText().toString().replace(",", ""), qty.getText().toString().replace(",", ""), tax.getText().toString().replace(",", ""), UnitNo, disc_per.getText().toString().replace(",", ""), bounce.getText().toString().replace(",", ""), str, UnitName, disc_Amt.getText().toString().replace(",", ""), Operand, Weight.getText().toString());
                        this.dismiss();
                    }
                } else {
                    ((Sale_InvoiceActivity) getActivity()).Save_List(ItemNo, Price.getText().toString().replaceAll("[^\\d.]", "").replace(",", ""), qty.getText().toString().replaceAll("[^\\d.]", "").replace(",", ""), tax.getText().toString().replaceAll("[^\\d.]", "").replace(",", ""), UnitNo, disc_per.getText().toString().replaceAll("[^\\d.]", "").replace(",", ""), bounce.getText().toString().replace(",", ""), str, UnitName, disc_Amt.getText().toString().replaceAll("[^\\d.]", "").replace(",", ""), Operand.replaceAll("[^\\d.]", ""), Weight.getText().toString().replaceAll("[^\\d.]", ""));

                }
                if (!Manpassword.equalsIgnoreCase("")) {
                    UpdateCode_ForPrice(Manpassword);

                }
                Manpassword = "";
                Price.setEnabled(false);
                chkEnbledPrice.setChecked(false);
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

                UnitNo = "";
                Operand = "";
                tv_qty_perc.setText("");
                tv_StoreQty.setText("");
                et_Discount.setText("");
//rad_Amt.setChecked(true);
                chk_EnableDis.setChecked(false);
                Price.clearFocus();
                net_total.clearFocus();
                qty.clearFocus();
                tax.clearFocus();
                Weight.setText("");
                TotalWeight = "0";
                UpdateCodeNew(ItemNo, LiveCode.getText().toString(), BouncePercent + "", DiscountPercent + "");
                ItemNo = "";
                if(!LiveCode.getText().toString().equalsIgnoreCase("")){
                    String q = " Update  CodeDefinition set Code_Status='1',InvoiceNo='" + getArguments().getString("OrderNo") + "' where Code='" + LiveCode.getText().toString() + "'";
                    sqlHandler.executeQuery(q);
                }
                GetDefualtPercent();
                UpdateServerCode();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                EditText et_Search_filter = (EditText) form.findViewById(R.id.et_Search_filter);
// et_Search_filter.setText("");
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
/* if( dis.getText().toString().length() == 0 ) {
dis.setText("0");
}
if( bo.getText().toString().length() == 0 ) {
bo.setText("0");
}*/
        Double p = SToD(str_p);
        Double q = SToD(str_q);
        Double w = SToD(TotalWeight);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        net_total.setText(String.valueOf(df.format(p * q)).replace(",", ""));
        Weight.setText(String.valueOf(df.format(w * q)).replace(",", ""));

    }

    private void GetQtyPerc() {

        Double Perc = 0.0;
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText bo = (EditText) form.findViewById(R.id.et_bo);
        TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);

        if (tv_StoreQty.getText().toString().replaceAll("[^\\d.]", "") == "") {
            Perc = 0.0;
        } else {
            if (SToD(tv_StoreQty.getText().toString().replaceAll("[^\\d.]", "")) == 0) {
                Perc = 0.0;
            } else {
                Perc = (SToD(qty.getText().toString().replaceAll("[^\\d.]", "")) + SToD(bo.getText().toString().replaceAll("[^\\d.]", ""))) / SToD(tv_StoreQty.getText().toString().replaceAll("[^\\d.]", ""));
            }
        }
        Perc = (Perc * 100);
        tv_qty_perc.setText(String.valueOf(SToD(Perc.toString().replaceAll("[^\\d.]", ""))));
        if (AllowSalInvMinus == 1 || ComInfo.DocType == 2) {
            tv_qty_perc.setText("1");
        }
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

    private void HideKeypad() {

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        } catch (Exception ex) {

        }


    }

    private void UpdateCode_ForPrice(String Code) {
        final EditText et_price = (EditText) form.findViewById(R.id.et_price);
        String q;
        String Tr_Desc = "تفعيل تعديل السعر على فاتورة المبيعات";
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u = sharedPreferences.getString("UserID", "");

        q = " Update RndNum set Flg = '1' where ifnull(Flg,'0') = '0' and Value = '" + Code + "'";
        sqlHandler.executeQuery(q);

        q = "INSERT INTO UsedCode(Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo,Tr_Desc,NewValue , Posted ) values ('1"
                + "','" + Code
                + "','" + getArguments().getString("OrderNo")
                + "','" + getArguments().getString("CustomerNo")
                + "','" + ItemNo
                + "','" + currentDateandTime.toString()
                + "','" + StringTime.toString()
                + "','" + u
                + "','" + Tr_Desc
                + "','" + et_price.getText().toString()
                + "','-1" + "')";
        sqlHandler.executeQuery(q);
        SharUseCode();

        et_price.setEnabled(true);

        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

    public void SharUseCode() {

        //sqlHandler=new SqlHandler(this);
        //q = "INSERT INTO UsedCode(Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo , Posted ) values ('1"


        final Handler _handler = new Handler();
        String query = "  select distinct  NewValue , Tr_Desc, Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo" +
                " from UsedCode   where Posted = '-1'";
        Cursor c1 = sqlHandler.selectQuery(query);


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
                    obj.setTr_Desc(c1.getString(c1
                            .getColumnIndex("Tr_Desc")));

                    obj.setNewValue(c1.getString(c1
                            .getColumnIndex("NewValue")));

                    CodeList.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(CodeList);
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(getActivity());
                ws.ShareUsedCode(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  UsedCode  set Posted='1'  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {

                            }
                        });
                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }

    private void ShowCodePop() {
        Manpassword = "";
        pass = "";
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.setContentView(R.layout.shwo_enter_code);

        dialog.setCancelable(true);
        input = (EditText) dialog.findViewById(R.id.et_Input);

        Button Update = (Button) dialog.findViewById(R.id.Update);
        Update.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Get_Code_From_Server();
            }
        });


        Button Show = (Button) dialog.findViewById(R.id.Show);
        Show.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass="";
                String query = "  select distinct  Code  from    CodeDefinition   where ifnull(Code_Status,'0') = '0' and CustomerId = '" + Cust_No + "'";
                Cursor c1 = sqlHandler.selectQuery(query);
                if (c1 != null && c1.getCount() > 0) {
                    if (c1.moveToFirst()) {
                        pass=c1.getString(c1.getColumnIndex("Code"));
                        c1.close();
                        Manpassword = pass;
                        UpdateBounceAndDiscountCode(pass);
                        Toast.makeText(getActivity(), "تم تعديل سقف الخصم والبونص", Toast.LENGTH_SHORT).show();
                        ResultCode = 1;
                        dialog.dismiss();
                    }

                }else{
                    Toast.makeText(getActivity(), "لا يوجد كود تفعيل خاص بهذا العميل", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button Accept = (Button) dialog.findViewById(R.id.Accept);
        Accept.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = DB.GetValue(getActivity(), "CodeDefinition", "Code", " ifnull(Code_Status,'0') = '0' and lower(Code) = '" + input.getText().toString() + "'");
                String password = input.getText().toString().toLowerCase();
                if (pass.toLowerCase().equalsIgnoreCase(password)) {
                    Manpassword = password;
                    UpdateBounceAndDiscountCode(password);
                    Toast.makeText(getActivity(), "تم تعديل سقف الخصم والبونص", Toast.LENGTH_SHORT).show();
                    ResultCode = 1;
                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                    HideKeypad();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "رمز التحقق غير مقبول", Toast.LENGTH_SHORT).show();
                    ResultCode = 0;
                    input.setText("");
                    dialog.dismiss();
                    ShowCodePop();
                    HideKeypad();

                }
            }

        });

        Button btnCancel = (Button) dialog.findViewById(R.id.Cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    HideKeypad();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {

                }
                chkEnbledPrice.setChecked(false);
               /* EditText d = (EditText) form.findViewById(R.id.et_disc_per);
                d.setFocusable(true);
                d.setInputType(InputType.TYPE_NULL);*/
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void Get_Code_From_Server() {
        HideKeypad();
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
        tv.setText("كود الخصم والبونص");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();


        final String Ser = "1";


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                ws.GetCodeDefinition(UserID);
                try {
                    Integer i;
                    String q = "";
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


                    q = "Delete from CodeDefinition";
                    sqlHandler.executeQuery(q);
                    q = "Delete from sqlite_sequence where name='CodeDefinition'";
                    sqlHandler.executeQuery(q);

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
                        custDialog.setMax(js_Id.length());
                        custDialog.incrementProgressBy(1);
                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {


                            custDialog.dismiss();


                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث كود الخصم والبونص");
                            alertDialog.setMessage("تمت عملية التحديث بنجاح");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
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

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث كود الخصم والبونص");
                            alertDialog.setMessage("مشكلة في عملية الاتصال بالسيرفر الرئيسي");
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

    private void UpdateBounceAndDiscountCode(String password) {
        tv_MaxDiscount.setText("");
        tv_MaxBounce.setText("");
        tv_CodeLiveNote.setText("");
        LiveCode.setText("");

       /* String q = " Update  CodeDefinition set Code_Status='1',InvoiceNo='" + getArguments().getString("OrderNo") + "' where Code='" + password + "'";
        sqlHandler.executeQuery(q);*/
        ResultCode = 1;
     String   q = "select MaxBouns ,MaxDiscount,Note ,AllBillMaterial " +
                "from CodeDefinition where lower(Code)='" + password + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_MaxDiscount.setText(c1.getString(c1.getColumnIndex("MaxDiscount")));
                tv_MaxBounce.setText(c1.getString(c1.getColumnIndex("MaxBouns")));
                tv_CodeLiveNote.setText(c1.getString(c1.getColumnIndex("Note")));
                if (c1.getString(c1.getColumnIndex("AllBillMaterial")).equalsIgnoreCase("1")) {
                    tv_CodeLiveNote.setText(tv_CodeLiveNote.getText() + " " + "تطبق على كل مواد الفاتورة");
                }
                LiveCode.setText(password);
            }
            c1.close();
        }

    }

    private void GetDefualtPercent() {
        tv_MaxDiscount.setText("");
        tv_MaxBounce.setText("");
        tv_CodeLiveNote.setText("");
        LiveCode.setText("");

        String q = "";
        String ManType = DB.GetValue(this.getActivity(), "manf", "ManType", "man='" + UserID + "'");
        // Toast.makeText(getActivity(), ManType, Toast.LENGTH_SHORT).show();
        if (ManType.equalsIgnoreCase("-1") || ManType.equalsIgnoreCase("") || ManType.equalsIgnoreCase("0")) {
            ManType = "4";
        }
        q = "select Code, MaxBouns ,MaxDiscount,Note ,AllBillMaterial " +
                "from CodeDefinition where  AllBillMaterial='1' AND InvoiceNo='" + getArguments().getString("OrderNo") + "'  and (CustomerId = '" + Cust_No + "' or CustomerId='-1' )";

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                ResultCode = 1;
                tv_MaxDiscount.setText(c1.getString(c1.getColumnIndex("MaxBouns")));
                tv_MaxBounce.setText(c1.getString(c1.getColumnIndex("MaxDiscount")));
                tv_CodeLiveNote.setText(c1.getString(c1.getColumnIndex("Note")));
                if (c1.getString(c1.getColumnIndex("AllBillMaterial")).equalsIgnoreCase("1")) {
                    tv_CodeLiveNote.setText(tv_CodeLiveNote.getText() + " " + "تطبق على كل مواد الفاتورة");
                }
                LiveCode.setText(c1.getString(c1.getColumnIndex("Code")));
            }
            c1.close();
        } else {
            ResultCode = 0;
            if (ManType.equalsIgnoreCase("4")) {
                tv_MaxDiscount.setText("100");
                tv_MaxBounce.setText("100");
                tv_CodeLiveNote.setText("");
                LiveCode.setText("");
            } else if (ManType.equalsIgnoreCase("2")) {
                tv_MaxDiscount.setText("5");
                tv_MaxBounce.setText("5");
                tv_CodeLiveNote.setText("");
                LiveCode.setText("");
            }

        }



    }
   public void UpdateServerCode() {


        final Handler _handler = new Handler();
        String query = "  select distinct  BouncePercent,DiscountPercent,  NewValue , Tr_Desc, Status, Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo" +
                "  from    UsedCode   where Posted = '-1'";
        Cursor c1 = sqlHandler.selectQuery(query);


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
                    obj.setTr_Desc(c1.getString(c1
                            .getColumnIndex("Tr_Desc")));

                    obj.setNewValue(c1.getString(c1
                            .getColumnIndex("NewValue")));

                    obj.setBouncePercent(c1.getString(c1
                            .getColumnIndex("BouncePercent")));
                    obj.setDiscountPercent(c1.getString(c1
                            .getColumnIndex("DiscountPercent")));


                    CodeList.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(CodeList);
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(getActivity());
                ws.ShareUsedCodeNew(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  UsedCode  set Posted='1'  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {

                            }
                        });
                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }

    private void UpdateCodeNew(String Item, String Code, String BouncePercent, String DiscountPercent) {
        String q;
        if (LiveCode.getText().toString().equalsIgnoreCase("")) {
            return;
        }
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u = sharedPreferences.getString("UserID", "");

        // sqlHandler.executeQuery("delete from UsedCode");

        q = "INSERT INTO UsedCode( Status , BouncePercent,DiscountPercent   , Code , OrderNo , CustomerNo ,ItemNo , Tr_Date, Tr_Time , UserNo , Posted ) " +
                "values ('1"
                + "','" + BouncePercent
                + "','" + DiscountPercent
                + "','" + Code
                + "','" + getArguments().getString("OrderNo")
                + "','" + getArguments().getString("CustomerNo")
                + "','" + Item
                + "','" + currentDateandTime.toString()
                + "','" + StringTime.toString()
                + "','" + u
                + "','-1" + "')";
        sqlHandler.executeQuery(q);
    }

        private void GetCodeForCustomer() {
        pass="";
        String query = "  select distinct  Code  from    CodeDefinition   where ifnull(Code_Status,'0') = '0' and CustomerId = '" + Cust_No + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                pass=c1.getString(c1.getColumnIndex("Code"));
                 c1.close();
                Manpassword = pass;
                UpdateBounceAndDiscountCode(pass);
                Toast.makeText(getActivity(), "تم تعديل سقف الخصم والبونص", Toast.LENGTH_SHORT).show();
                ResultCode = 1;

              /*  new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setCustomImage(R.drawable.error_new)
                        .setContentText("يجب خصم  وبونص استثنائي للعميل ")
                        .setConfirmText("موافق")
                        .show();*/

            }
        }
}


}
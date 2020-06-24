
package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MotionEventCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf_adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Po_Item_List_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems_Adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Methdes.MyTextView;


public class Pop_Po_Select_Items extends DialogFragment implements View.OnClickListener {
    public static final String CALCULATOR_PACKAGE = "com.android.calculator2";
    public static final String CALCULATOR_CLASS = "com.android.calculator2.Calculator";
    View form;
    Button add, cancel, btn_Inc, btn_Dec, btn_Change_Menu;
    ImageButton OpenCal;
    ListView items_Lsit, lstCat;
    TextView itemnm;
    Double Custdis = 0.0;
    TextView Store_Qty;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0;
    Double min_price = 0.0;
    Double price = 0.0;
    EditText filter;
    ImageButton btn_filter_search;
    String UnitNo = "";
    String Operand = "";
    Cls_Qty_Batch cls_qty_batch;
    ArrayList<Cls_Qty_Batch> cls_qty_batches;
    TextView ftv_StoreQty, fqty;
    String UnitName = "";
    String  str = "";
    String  ItemNm = "";
    RadioButton rad_Per;
    RadioButton rad_Amt;
    List<Cls_Sal_InvItems> UpdateItem;
    MyTextView tv_ExpDate, tv_Batch, tv_Counter;
    int AllowSalInvMinus = 0;
    int Type_No = -1;
    File imgFile;
    int ItemIdentity = -1;
    String FillMethod = "1";
    ImageView ItemImage, img1, btn_Search_By_image;
    LinearLayout Prv, Nxt;

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

    SharedPreferences sharedPreferences;
    float initialX, initialY;
    float startXValue = 1;
    int TotalItemNumber, ItemCounter, Search_By_image_Flag;
    String PriceFromAB = "0";
    String MenuType = "1";
    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (sharedPreferences.getString("Po_Select_Items_Method", "").equalsIgnoreCase("1")) {
            form = inflater.inflate(R.layout.layout_pop_po_select_items_image_port, container, false);
            MenuType = "1";
        } else {
            form = inflater.inflate(R.layout.layout_pop_po_select_items_new, container, false);
            MenuType = "2";
        }
        Window window = getDialog().getWindow();

        FillMethod = sharedPreferences.getString("Po_Select_Items_Method", "");
        TotalItemNumber = 0;
        ItemCounter = 0;
        Search_By_image_Flag = 0;
       /* Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config, null);*/

        getDialog().setTitle("إضافة المواد");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        add = (Button) form.findViewById(R.id.btn_add_item);
        add.setOnClickListener(this);
        cancel = (Button) form.findViewById(R.id.btn_cancel_item);
        cancel.setOnClickListener(this);
        OpenCal = (ImageButton) form.findViewById(R.id.btn_OpenCal);


        btn_Inc = (Button) form.findViewById(R.id.btn_Inc);
        btn_Inc.setOnClickListener(this);


        btn_Dec = (Button) form.findViewById(R.id.btn_Dec);
        btn_Dec.setOnClickListener(this);


        FillDeptf();

        OpenCal.setOnClickListener(this);

        AllowSalInvMinus = Integer.parseInt(DB.GetValue(this.getActivity(), "ComanyInfo", "AllowSalInvMinus", "1=1"));
        cls_qty_batches = new ArrayList<Cls_Qty_Batch>();

        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);
        ftv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);
        fqty = (EditText) form.findViewById(R.id.et_qty);
        ItemImage = (ImageView) form.findViewById(R.id.ItemImage);
        img1 = (ImageView) form.findViewById(R.id.img1);


        ItemImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float endXValue = 0;
                float x1 = event.getAxisValue(MotionEvent.AXIS_X);
                int action = MotionEventCompat.getActionMasked(event);
                switch (action) {

                    case (MotionEvent.ACTION_DOWN):
                        startXValue = event.getAxisValue(MotionEvent.AXIS_X);
                       if(  MenuType.equalsIgnoreCase("2") ){
                        Bundle bundle = new Bundle();
                        bundle.putString("Scr", "po");
                        bundle.putString("ItemNo", ItemNo);
                        bundle.putString("ItemNm", ItemNm);
                        FragmentManager Manager = getFragmentManager();
                        PopItemImagePreview obj = new PopItemImagePreview();
                        obj.setArguments(bundle);
                        obj.show(Manager, null);
                    }
                        return true;

                    case (MotionEvent.ACTION_UP):
                        endXValue = event.getAxisValue(MotionEvent.AXIS_X);
                        if (endXValue > startXValue) {
                            if (endXValue - startXValue > 100) {
                                System.out.println("Left-Right");
                                NextItem();
                            }
                        } else {
                            if (startXValue - endXValue > 100) {
                                System.out.println("Right-Left");
                                PrvItem();

                            }
                        }
                        return true;


                }
                return true;
            }

        });


        items_Lsit = (ListView) form.findViewById(R.id.listView2);
        lstCat = (ListView) form.findViewById(R.id.lstCat);


        final List<String> items_ls = new ArrayList<String>();
        final List<String> promotion_ls = new ArrayList<String>();

        final EditText Price = (EditText) form.findViewById(R.id.et_price);
        if (ComInfo.ComNo == 1) {

            Price.setFocusable(true);
            Price.setClickable(true);
            Price.setInputType(Configuration.KEYBOARD_12KEY);
            Price.setKeyListener(new DigitsKeyListener());
        } else {


            Price.setFocusable(false);
            Price.setClickable(false);
            Price.setInputType(Configuration.KEYBOARD_12KEY);
            Price.setKeyListener(new DigitsKeyListener());
        }
        //Price.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Price.setKeyListener(new DigitsKeyListener());
        // Price.setRawInputType(Configuration.KEYBOARD_QWERTY);
      /*  InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(Price, InputMethodManager.SHOW_IMPLICIT);
*/

        final EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText tax = (EditText) form.findViewById(R.id.et_tax);
        TextView textView26 = (TextView) form.findViewById(R.id.textView26);
        TextView textView25 = (TextView) form.findViewById(R.id.textView25);
        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        final EditText bo = (EditText) form.findViewById(R.id.et_bo);
        final EditText dis_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);
        final EditText totl = (EditText) form.findViewById(R.id.et_totl);


        tax.setFocusable(false);
        tax.setClickable(false);

        totl.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital7.ttf"));

        final EditText Discount = (EditText) form.findViewById(R.id.et_Discount);
        RadioGroup radGrp = (RadioGroup) form.findViewById(R.id.radDisc);
        int checkedRadioButtonID = radGrp.getCheckedRadioButtonId();
        final Double Total = 0.0;

        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        final DecimalFormat df = (DecimalFormat) nf;

        sqlHandler = new SqlHandler(getActivity());
        tv_ExpDate = (MyTextView) form.findViewById(R.id.tv_ExpDate);
        tv_Batch = (MyTextView) form.findViewById(R.id.tv_Batch);

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
        LinearLayout BounceLyt = (LinearLayout) form.findViewById(R.id.BounceLyt);
        LinearLayout DiscountLyt = (LinearLayout) form.findViewById(R.id.DiscountLyt);

        if (ComInfo.ComNo == 3) { // salasel or good system
            BounceLyt.setVisibility(View.INVISIBLE);
            BounceLyt.getLayoutParams().height = 0;
            bo.setVisibility(View.INVISIBLE);
            bo.getLayoutParams().height = 0;
            textView26.setVisibility(View.INVISIBLE);
            textView26.getLayoutParams().height = 0;
        }

        if (ComInfo.ComNo == 4) { // Good System
            BounceLyt.setVisibility(View.INVISIBLE);
            BounceLyt.getLayoutParams().height = 0;
            bo.setVisibility(View.INVISIBLE);
            bo.getLayoutParams().height = 0;
            textView26.setVisibility(View.INVISIBLE);
            textView26.getLayoutParams().height = 0;


            DiscountLyt.setVisibility(View.INVISIBLE);
            DiscountLyt.getLayoutParams().height = 0;
            Discount.setVisibility(View.INVISIBLE);
            Discount.getLayoutParams().height = 0;
            textView25.setVisibility(View.INVISIBLE);
            textView25.getLayoutParams().height = 0;
        }
        rad_Per.setChecked(true);
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
                    // Price.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });
        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    qty.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        qty.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       if (ComInfo.ComNo != 3) {
                                           qty.setText("", TextView.BufferType.EDITABLE);
                                       }
                                   }
                               }

        );


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
                // Toast.makeText(getActivity(),s.toString(),Toast.LENGTH_SHORT).show();
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


              /*  if (s.toString().length()>0 &&  SToD(s.toString())>0 &&    (SToD(s.toString()) < min_price)){

                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("الحد الادني لبيع المادة ");

                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setMessage("لقد تجاوزت الحد الادنى للسعر" + "   " + String.valueOf(min_price));
                    // Setting OK Button
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Price.setText(min_price.toString());

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
*/

          /*      EditText Price = (EditText) form.findViewById(R.id.et_price);
                String t = Price.getText().toString();
                Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);
                Integer indexValue = sp_unite.getSelectedItemPosition();

                if (indexValue > -1) {
                    Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(indexValue);
                    if ( min != 0 && (Float.valueOf(o.getMin()) > Float.valueOf(Price.getText().toString()))) {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                getActivity()).create();
                        alertDialog.setTitle("Galaxy");

                        alertDialog.setIcon(R.drawable.delete);
                        alertDialog.setMessage("لقد تجاوزت الحد الادني للسعر" + "   " + String.valueOf(min));
                        // Setting OK Button
                        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                }*/
                // Price.setText("15");

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
                CalcDiscount();
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
        fillUnit("-1");

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


        final Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);

        sp_unite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PriceFromAB = "0";
                Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(position);
                EditText Price = (EditText) form.findViewById(R.id.et_price);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;

                Double CusPrice = 0.0;
                CusPrice = GetCustLastPrice(ItemNo, o.getUnitno().toString());
                if (CusPrice > 0) {
                    Price.setText(CusPrice.toString().replace(",", ""));
                } else {
                    Price.setText(String.valueOf(df.format(SToD(o.getPrice().toString()))).replace(",", ""));
                }

                PriceFromAB = Price.getText().toString();
                //Toast.makeText(getActivity(),PriceFromAB,Toast.LENGTH_SHORT).show();
                if (ComInfo.ComNo == 4) {
                    String Price2 = (DB.GetValue(getActivity(), "invf", "ifnull((tax/100),1) ", "Item_No='" + ItemNo + "'"));
                    Price2 = ((SToD(Price.getText().toString()) / (1 + SToD(Price2))) + "");
                    Price.setText(SToD(Price2) + "");

                }
                UnitNo = o.getUnitno().toString();
                UnitName = o.getUnitDesc().toString();
                Operand = o.getOperand().toString();
                min = Float.valueOf(o.getMin());

                //  if(ComInfo.ComNo!=4) {
                get_min_price();
                //  }
                if (ComInfo.ComNo == 3) {
                   /*checkStoreQtyBatch();
                   checkStoreQtyAllStore();*/
                    checkStoreQty();
                } else {
                    checkStoreQty();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Type_No = -1;
        lstCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_Deptf o = (Cls_Deptf) lstCat.getItemAtPosition(position);
                Type_No = Integer.parseInt(o.getType_No());
                ItemIdentity = -1;
                if (FillMethod.equalsIgnoreCase("1")) {
                    FillItemsByImage();
                } else {
                    FillItems();
                }

            }
        });
        lstCat.setOnTouchListener(new ListView.OnTouchListener() {
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
        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                OnItemListClickbyUser(position);

            }
        });

        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // qty.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rad_Per.setChecked(true);//Here


        window = this.getDialog().getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        lp.x = 0;
        lp.y = 0;
        window.setAttributes(lp);
        btn_Change_Menu = (Button) form.findViewById(R.id.btn_Change_Menu);
        btn_Change_Menu.setOnClickListener(this);

        try {

            if (FillMethod.equalsIgnoreCase("1")) {


                btn_Search_By_image = (ImageView) form.findViewById(R.id.Search_By_image);
                btn_Search_By_image.setOnClickListener(this);
                Prv = (LinearLayout) form.findViewById(R.id.LytPrv);
                Nxt = (LinearLayout) form.findViewById(R.id.LytNext);
                tv_Counter = (MyTextView) form.findViewById(R.id.tv_Counter);
                tv_Counter.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "digital7.ttf"));
                Prv.setOnClickListener(this);
                Nxt.setOnClickListener(this);

                Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.no_image);
                Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                ItemImage.setImageBitmap(imageRounded);
            }
            SetSearchImage();
        } catch (Exception ex) {

        }
        window.setGravity(Gravity.TOP | Gravity.LEFT);
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
        return form;
    }

    private void OnItemListClickbyUser(int position) {

        Double Qty = 0.0;
        //arg1.setBackgroundColor(Color.GRAY);
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        Cls_Invf o = (Cls_Invf) items_Lsit.getItemAtPosition(position);


        EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);
        EditText Price = (EditText) form.findViewById(R.id.et_price);
        EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
        EditText et_totl = (EditText) form.findViewById(R.id.et_totl);
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);
        TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
        et_totl.setError(null);
        et_qty.setText("1");
        if (ComInfo.ComNo == 3) {
            et_qty.setText("");
        }
        et_qty.clearFocus();
        et_Discount.setText("");
        tv_StoreQty.setText("");
        tv_qty_perc.setText("");

        rad_Per.setChecked(true);//Here
        //Price.setText(df.format(Double.valueOf( o.getPrice())).toString());
        EditText tax = (EditText) form.findViewById(R.id.et_tax);

        if (getArguments().getString("CustTaxStatus").equalsIgnoreCase("1")) {
            tax.setText("0");
        } else {
            tax.setText(o.getTax().toString());
        }

        str = (String) o.getItem_Name();
        ItemNm = (String) o.getItem_Name();
        // Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
        getDialog().setTitle(str);
        fillUnit(o.getItem_No().toString());
        ItemNo = o.getItem_No().toString();
        get_min_price();
        showImage(o.getItem_No().toString());
        // itemnm.setText(str);

        Price.setError(null);
        Price.clearFocus();
        et_qty.setError(null);
        tax.setError(null);
        if (ComInfo.ComNo == 3) {
            //checkStoreQtyBatch();
            checkStoreQty();

        } else {
            checkStoreQty();
        }
        showImage(o.getItem_No().toString());
        if (ComInfo.ComNo == 3) {
            et_qty.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            et_qty.requestFocus();
        }


    }

    private void showImage(String ItemNo) {
        imgFile = new File("//sdcard/Android/Cv_Images/" + ItemNo + ".jpg");
        try {
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                ItemImage.setImageBitmap(imageRounded);


            } else {

                Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.no_image);
                Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 0, 0, mpaint);// Round Image Corner 100 100 100 100
                ItemImage.setImageBitmap(imageRounded);
            }

        } catch (Exception ex) {
            ItemImage.setImageDrawable(null);
            ItemImage.setImageResource(0);
        }


    }

    private double GetCustLastPrice(String ItemNo, String UnitNo) {
        Double r = 0.0;
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

        return r;
    }

    private void get_min_price() {

        min_price = 0.0;
        price = 0.0;
        String CatNo = "";
        EditText Price = (EditText) form.findViewById(R.id.et_price);
        CatNo = getArguments().getString("CatNo");
        if (CatNo != "0") {
            String q = "Select  ifnull( MinPrice,0) as min_price , ifnull(Price,0) as Price  , ifnull(dis,0) as dis  from Items_Categ where ItemCode = '" + ItemNo + "'   " +
                    "  And CategNo = '" + CatNo + "'";

            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    if (ComInfo.ComNo == 3) {
                        min_price = SToD(c1.getString(c1.getColumnIndex("min_price")));
                        // if(SToD(Operand) >0 ) { // salasel
                        price = SToD(c1.getString(c1.getColumnIndex("Price")));// / SToD(Operand);
                        // }
                    }
                    if (ComInfo.ComNo == 4) {
                        String Price2 = (DB.GetValue(getActivity(), "invf", "ifnull((tax/100),1) ", "Item_No='" + ItemNo + "'"));


                        min_price = SToD(Operand) * SToD(c1.getString(c1.getColumnIndex("min_price")));
                        price = SToD(Operand) * SToD(c1.getString(c1.getColumnIndex("Price")));
                        price = SToD(price + "") / (1 + SToD(Price2));
                    } else {
                        min_price = SToD(Operand) * SToD(c1.getString(c1.getColumnIndex("min_price")));
                        price = SToD(Operand) * SToD(c1.getString(c1.getColumnIndex("Price")));
                    }
                    //  Toast.makeText(getActivity(),price+"",Toast.LENGTH_SHORT).show();
                    if (price > 0) {
                        Price.setText(SToD(price.toString()) + "");
                    }

                    min_price = SToD(min_price.toString());
                    Custdis = SToD(c1.getString(c1.getColumnIndex("dis")));
                    Custdis = SToD(Custdis.toString());
                    //Toast.makeText(getActivity(), "    الخصم " +":" +String.valueOf(Custdis),Toast.LENGTH_SHORT).show();
                }
                c1.close();
            }
        }

    }

    private void checkStoreQtyAllStore() {

        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_AllStoreQty);

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
        }

        if (ComInfo.ComNo == 3) {
            query = "SELECT       (ifnull( sum  ( ifnull( sid.qty,0)  / (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  / (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  / (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                    " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                    "    where   sih.Post = -1  and ui.item_no ='" + ItemNo + "'  and sih.UserID='" + sharedPreferences.getString("UserID", "-1") + "'";
            //   "    where  QtyStoreSer = "+MaxStoreQtySer+" and ui.item_no ='"+ItemNo+"'";
        } else {
            query = "SELECT       (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                    " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                    "    where   sih.Post = -1  and ui.item_no ='" + ItemNo + "'  and sih.UserID='" + sharedPreferences.getString("UserID", "-1") + "'";

        }

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
       /* if (SToD(Operand) == 0) {
            Res = 0.0;
        } else {
            Res = Res ;// SToD(Operand);
        }*/

        Store_Qty.setText(SToD(Res.toString()).toString());


    }

    private void checkStoreQty() {

        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);
        TextView tv_AllStoreQty = (TextView) form.findViewById(R.id.tv_AllStoreQty);

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
        }

        if (ComInfo.ComNo == 3) {
            query = " SELECT   (ifnull( sum  ( ifnull( sid.qty,0) / (ifnull( sid.Opraned,1))) ,0)  +   " +
                    "  ifnull( sum  ( ifnull( sid.bounce_qty,0)  / (ifnull( sid.Opraned,1))) ,0) +  " +
                    "  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  / (ifnull( sid.Opraned,1))) ,0))  as Sal_Qty" +
                    "  from  Po_Hdr  sih inner join Po_dtl sid on  sid.OrderNo = sih.OrderNo" +
                    "  inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                    "  where   sih.posted = -1  and ui.item_no ='" + ItemNo + "'  " +
                    " and sih.UserID='" + sharedPreferences.getString("UserID", "-1") + "'";


        } else {
            query = "SELECT       (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                    " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                    "    where   sih.Post = -1  and ui.item_no ='" + ItemNo + "'  and sih.UserID='" + sharedPreferences.getString("UserID", "-1") + "'";

        }
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
        if (SToD(Operand) == 0) {
            Res = 0.0;
        } else {
            Res = Res / SToD(Operand);
        }

        Store_Qty.setText(SToD(Res.toString()).toString());
        tv_AllStoreQty.setText(SToD(Res.toString()).toString());
        GetQtyPerc();
    }

    private void checkStoreQtyBatch() {


        // final Handler _handler = new Handler();
        String q = "";
        q = " select Item_No,expdate, batchno,net from QtyBatch where Item_No='" + ItemNo + "'  ORDER BY no";
        Cursor c1 = sqlHandler.selectQuery(q);
        cls_qty_batches.clear();
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_qty_batch = new Cls_Qty_Batch();
                    cls_qty_batch.setItem_No(c1.getString(c1.getColumnIndex("Item_No")));
                    cls_qty_batch.setExpdate(c1.getString(c1.getColumnIndex("expdate")));
                    cls_qty_batch.setBatchno(c1.getString(c1.getColumnIndex("batchno")));
                    cls_qty_batch.setNet(c1.getString(c1.getColumnIndex("net")));

                    cls_qty_batches.add(cls_qty_batch);

                } while (c1.moveToNext());

            }
            c1.close();

        }
        CalcBatchQty();
        GetQtyPerc();
     /*   new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                ws.TrnsferQtyFromMobileBatch( ItemNo,"1");
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Item_No = js.getJSONArray("Item_No");
                    JSONArray js_expdate = js.getJSONArray("expdate");
                    JSONArray js_batchno = js.getJSONArray("batchno");
                    JSONArray js_net = js.getJSONArray("net");




                    for (i = 0; i < js_Item_No.length(); i++)  {
                        cls_qty_batch = new Cls_Qty_Batch();

                        cls_qty_batch.setItem_No(js_Item_No.get(i).toString());
                        cls_qty_batch.setExpdate(js_expdate.get(i).toString());
                        cls_qty_batch.setBatchno(js_batchno.get(i).toString());
                        cls_qty_batch.setNet(js_net.get(i).toString());

                        cls_qty_batches.add(cls_qty_batch) ;

                    }

                    _handler.post(new Runnable() {
                        public void run() {
                            cls_qty_batch = new Cls_Qty_Batch();
                            if(cls_qty_batches.size()>0) {
                                cls_qty_batch = cls_qty_batches.get(0);
                               // Toast.makeText(getActivity(), cls_qty_batch.getBatchno(), Toast.LENGTH_SHORT).show();
                                CalcBatchQty();
                            }
                         *//*   else
                            {
                               Toast.makeText(getActivity(), " حدث خطأ اثناء استرجاع تاريخ الصلاحية", Toast.LENGTH_SHORT).show();
                            }*//*
                        }
                    });


                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity()," لا يمكن استرجاع تاريخ الصلاحية بسبب عدم الاتصال بالسيرفر",Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        }).start();*/


    }

    private void CalcBatchQty() {
        Double OrderQty = 0.0;


        Double StoreQty = 0.0;
        tv_ExpDate.setText("");
        tv_Batch.setText("");
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);
        Double Qty = 0.0;
        for (int i = 0; i < cls_qty_batches.size(); i++) {
            cls_qty_batch = new Cls_Qty_Batch();
            cls_qty_batch = cls_qty_batches.get(i);
            Qty = SToD(cls_qty_batch.getNet()) / SToD(Operand);
            OrderQty = ((OrdersItems) getActivity()).GetOrderQty(ItemNo, cls_qty_batch.getBatchno(), cls_qty_batch.getExpdate());
            StoreQty = Qty - OrderQty;
            StoreQty = SToD(StoreQty + "");
            if (StoreQty > 0) {
                tv_ExpDate.setText(cls_qty_batch.getExpdate());
                tv_Batch.setText(cls_qty_batch.getBatchno());
                tv_StoreQty.setText(StoreQty + "");
                //  Toast.makeText(getActivity(),(Qty - OrderQty) + "" , Toast.LENGTH_SHORT).show();
                break;
            }


        }


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
        pq = SToD(et_price.getText().toString()) * SToD(et_qty.getText().toString());
        if (rad_Per.isChecked()) {


            Double Total = ((SToD(Discount.getText().toString().replaceAll("[^\\d.]", "").replace(",", "")) / 100)) * pq;

            Total = SToD(Total.toString());

            dis_Amt.setText(String.valueOf((Total)));
            dis.setText(String.valueOf(Discount.getText()));
            if (SToD(dis_Amt.getText().toString().replaceAll("[^\\d.]", "")) > 0) {
                Toast.makeText(getActivity(), "قيمة الخصم" + dis_Amt.getText().toString(), Toast.LENGTH_SHORT).show();
            }
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
            if (SToD(dis_Amt.getText().toString().replaceAll("[^\\d.]", "")) > 0) {
                Toast.makeText(getActivity(), "قيمة الخصم" + dis_Amt.getText().toString(), Toast.LENGTH_SHORT).show();
            }

        }
        if (totl.getText().toString().equals("0")) {
            dis.setText("0");
            dis_Amt.setText("0");
        }

        et_totl.setText(String.valueOf(SToD(String.valueOf(pq - SToD(dis_Amt.getText().toString().replaceAll("[^\\d.]", ""))))).replace(",", ""));
    }

    private void FillDeptf() {
        //final Spinner sp_items_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        ListView lstCat = (ListView) form.findViewById(R.id.lstCat);
        sqlHandler = new SqlHandler(getActivity());
        String query = "";
        if (FillMethod.equalsIgnoreCase("1")) {
            query = "Select   distinct Type_No , Type_Name , etname from deptf where Type_No!='-1'";
        } else {
            query = "Select   distinct Type_No , Type_Name , etname from deptf";
        }

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
        }

        Cls_Deptf_adapter cls_unitItems_adapter = new Cls_Deptf_adapter(
                getActivity(), cls_deptfs);

        //sp_items_cat.setAdapter(cls_unitItems_adapter);
        lstCat.setAdapter(cls_unitItems_adapter);
    }

    private void FillItemsByImage() {

        if (Type_No < 1) {
            Toast.makeText(getActivity(), "الرجاء اختيار الصنف", Toast.LENGTH_SHORT).show();
            return;
        }
        filter = (EditText) form.findViewById(R.id.et_Search_filter);
        String query = "";
        sqlHandler = new SqlHandler(getActivity());
        Bundle bundle = this.getArguments();
        UpdateItem = (List<Cls_Sal_InvItems>) bundle.getSerializable("List");

        String q = "";

        if (ItemIdentity == -1) {
            q = " Select   ifnull( min(no),0)  as ID   From  invf Where Type_No  ='" + Type_No + "'    ";
            Cursor cc = sqlHandler.selectQuery(q);
            if (cc != null && cc.getCount() != 0) {
                cc.moveToFirst();
                ItemIdentity = Integer.parseInt(cc.getString(cc.getColumnIndex("ID")));

                ItemCounter = 1;
                cc.close();
            }
        }

        if (Type_No > 0) {
            q = " Select   ifnull( count(no),0)  as c  From  invf Where Type_No  ='" + Type_No + "'    ";
            Cursor cc = sqlHandler.selectQuery(q);
            if (cc != null && cc.getCount() != 0) {
                cc.moveToFirst();
                TotalItemNumber = Integer.parseInt(cc.getString(cc.getColumnIndex("c")));

                cc.close();
            }
        }

        tv_Counter.setText(TotalItemNumber + "" + " / " + ItemCounter + "");

        if (Search_By_image_Flag == 0) {
            if (ComInfo.ComNo == 3) {
                query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                        " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where   Type_No = '" + Type_No + "'  and no=" + ItemIdentity;
            } else {
                query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  where      Type_No = '" + Type_No + "'  and no=" + ItemIdentity;
            }
        } else {

            if (ComInfo.ComNo == 3) {
                query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                        " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where   Type_No = '" + Type_No + "'  ";
            } else {
                query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  where      Type_No = '" + Type_No + "'  ";
            }

        }


        ListView item_cat = (ListView) form.findViewById(R.id.lstCat);
        Integer indexValue = 0;//item_cat.getSelectedItemPosition();

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
        }

        Cls_Invf_Adapter cls_invf_adapter = new Cls_Invf_Adapter(
                getActivity(), cls_invf_List);
        items_Lsit.setAdapter(cls_invf_adapter);


        if (cls_invf_List.size() > 0) {
            OnItemListClickbyUser(0);
        }
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
                EditText Price = (EditText) form.findViewById(R.id.et_price);
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


            if (getArguments().getString("Scr") == "po") {
                if (filter.getText().toString().equals("")) {

                    if (ComInfo.ComNo == -1) {
                        query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                                " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where 1=1 ";
                    } else {
                        query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                                "   where 1=1 ";
                    }

                } else {
                    if (ComInfo.ComNo == -1) {
                        query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                                " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where Item_No  = '" + filter.getText().toString() + "' or  invf.Item_Name like '%" + filter.getText().toString() + "%'";

                    } else {
                        String Temp_Filter = "";
                        Temp_Filter = filter.getText().toString().replace("١", "1").replace("٢", "2").replace("٣", "3").replace("٤", "4").replace("٥", "5").replace("٦", "6").replace("٧", "7").replace("٨", "8").replace("٩", "9").replace("٠", "0");

                        query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                                "    where   Item_No like '%" + Temp_Filter + "%' or    Item_No like '%" + filter.getText().toString() + "%'      or  invf.Item_Name like '%" + filter.getText().toString() + "%'";
                    }

                }

                ListView item_cat = (ListView) form.findViewById(R.id.lstCat);
                Integer indexValue = item_cat.getSelectedItemPosition();

                if (indexValue > 0) {

                    Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

                    query = query + "and    Type_No = '" + o.getType_No().toString() + "'";

                }

                if (Type_No > 0) {
                    query = query + "and    Type_No = '" + Type_No + "" + "'";
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
        }

        Cls_Po_Item_List_Adapter cls_invf_adapter = new Cls_Po_Item_List_Adapter(
                getActivity(), cls_invf_List);
        items_Lsit.setAdapter(cls_invf_adapter);


    }

    public void fillUnit(String item_no) {


        Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);
        sqlHandler = new SqlHandler(getActivity());

        String Strorder = "";
       /* if(ComInfo.ComNo ==3) { // salasel
            Strorder =" UnitItems.MAX desc ";
        } */


        String query = "";

      /*  if (ComInfo.ComNo ==2){
            Strorder = " UnitItems.Min desc ";
        }
        else{
            Strorder = " UnitItems.Min desc ";
        }*/


        if (ComInfo.ComNo == 3) { // salasel
            query = "Select  distinct ifnull( UnitItems.unitno,'-1') as unitno ,ifnull (UnitItems.Min,'0') as Min  , ifnull (UnitItems.price,'0') price, ifnull(Unites.UnitName,'لا يوجد')  as UnitName " +
                    " , ifnull(Operand,1) as Operand from UnitItems  " +
                    "   left join  Unites on Unites.Unitno =UnitItems.unitno " +
                    "   where UnitItems.item_no like '" + item_no + "' and  UnitItems.MAX =1  order by  UnitItems.MAX desc";

        } else if (ComInfo.ComNo == 4) {
            query = "Select  distinct UnitItems.unitno ,UnitItems.Min ,UnitItems.price,Unites.UnitName " +
                    " , ifnull(Operand,1) as Operand from UnitItems  " +
                    "   left join  Unites on Unites.Unitno =UnitItems.unitno " +
                    "   where UnitItems.item_no like '" + item_no + "' order by UnitItems.UnitSale desc ";
        } else {
            query = "Select  distinct UnitItems.unitno ,UnitItems.Min ,UnitItems.price,Unites.UnitName " +
                    " , ifnull(Operand,1) as Operand from UnitItems  " +
                    "   left join  Unites on Unites.Unitno =UnitItems.unitno " +
                    "   where UnitItems.item_no like '" + item_no + "' order by UnitItems.Min desc ";
        }


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
                    cls_unitItems.setOperand(c1.getString(c1.getColumnIndex("Operand")));

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
        }
    }

    private void SetSearchImage() {
        filter.setText("");
        items_Lsit = (ListView) form.findViewById(R.id.listView2);
        if (Search_By_image_Flag == 1) {
            btn_Search_By_image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.search_enabled));
            filter.setVisibility(View.VISIBLE);
            items_Lsit.setVisibility(View.VISIBLE);

        } else {
            btn_Search_By_image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.search_disabled));
            filter.setVisibility(View.INVISIBLE);
            items_Lsit.setVisibility(View.INVISIBLE);
        }
        // FillItemsByImage();
    }

    @Override
    public void onClick(View v) {
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);

        if (qty.getText().toString().trim().equalsIgnoreCase(""))
            qty.setText("0");
        if (v == btn_Search_By_image) {

            if (Search_By_image_Flag == 0)
                Search_By_image_Flag = 1;
            else
                Search_By_image_Flag = 0;


            SetSearchImage();
        } else if (v == Prv) {
            PrvItem();
        } else if (v == Nxt) {

            NextItem();
        } else if (v == OpenCal) {
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

        } else if (v == btn_Inc) {


            qty.setText((Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) + 1) + "");
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) > 1) {
                btn_Dec.setVisibility(View.VISIBLE);
            }
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) < 1) {
                qty.setText(SToD(qty.getText().toString().replaceAll("[^\\d.]", "")) + "");
            }
            CalcDiscount();
            get_total();
            GetQtyPerc();
        } else if (v == btn_Change_Menu) {
            this.dismiss();
            ((OrdersItems) getActivity()).changeLayout(sharedPreferences.getString("Po_Select_Items_Method", ""));
        } else if (v == btn_Dec) {
            qty.setText((Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) - 1) + "");
            if (Double.parseDouble(qty.getText().toString().replaceAll("[^\\d.]", "")) < 1) {
                qty.setText("1");
                btn_Dec.setVisibility(View.INVISIBLE);
            }
            CalcDiscount();
            get_total();
            GetQtyPerc();

        } else if (v == cancel) {
            this.dismiss();
        } else if (v == add) {

            EditText Price = (EditText) form.findViewById(R.id.et_price);
            final EditText final_P = (EditText) form.findViewById(R.id.et_price);

            EditText tax = (EditText) form.findViewById(R.id.et_tax);

            EditText bo = (EditText) form.findViewById(R.id.et_bo);
            // Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);
            EditText bounce = (EditText) form.findViewById(R.id.et_bo);
            EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
            EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);

            EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);
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


            if (Price.getText().toString().length() > 0 && SToD(Price.getText().toString()) > 0 && (SToD(Price.getText().toString()) < min_price)) {
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
                alertDialog.show();
                return;
            }

            EditText et_disc_per = (EditText) form.findViewById(R.id.et_disc_per);

            //et_Discount

            // if (rad_Per.isChecked()) {
            if (ComInfo.ComNo == 3) {
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
            // }

            if (ComInfo.ComNo == 3) {
                if (SToD(tv_StoreQty.getText().toString()) == 0.0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("طلب مبيعات");
                    alertDialog.setMessage("الكمية المطلوبة غير متوفرة");            // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alertDialog.show();
                    return;
                }

                if (SToD(tv_qty_perc.getText().toString()) > 100) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("طلب مبيعات");
                    alertDialog.setMessage("الكمية المطلوبة غير متوفرة");            // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            fqty.setText(ftv_StoreQty.getText());

                        }
                    });

                    alertDialog.show();
                    return;
                }

            }


            if (qty.getText().toString().trim().equalsIgnoreCase("")) {
                qty.setText("0");
            }

            if (SToD(net_total.getText().toString().replaceAll("[^\\d.]", "")) <= 0) {
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
                tax.requestFocus();
                return;
            }


        /* if( dis.getText().toString().length() == 0 ) {
             dis.setError("required!");
             dis.requestFocus();
             return;
         }*/

            if (ItemNo.toString().length() == 0) {

                return;
            }


            // PriceAftertAx=     (SToD(Price.getText().toString()) / ((SToD(tax.getText().toString()) / 100) + 1));
            // Price.setText    (  String.valueOf(  SToD(  PriceAftertAx.toString())));


            if (getArguments().getString("Scr") == "po") {
                ((OrdersItems) getActivity()).Save_List(ItemNo, Price.getText().toString().replace(",", ""), qty.getText().toString().replace(",", ""), tax.getText().toString().replace(",", ""), UnitNo, disc_per.getText().toString().replace(",", ""), bounce.getText().toString().replace(",", ""), str, UnitName, disc_Amt.getText().toString().replace(",", ""), tv_ExpDate.getText().toString(), tv_Batch.getText().toString(), Operand, PriceFromAB);

            }

            try {
                PriceFromAB = "0";
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
                rad_Per.setChecked(true);

                disc_Amt.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }

            EditText et_Search_filter = (EditText) form.findViewById(R.id.et_Search_filter);
            if (et_Search_filter.getText().toString().trim().length() > 1) {
                et_Search_filter.setText("");
            }
        }

    }

    public void onListItemClick(ListView l, View v, int position, long id) {


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        // v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

    }

    public void get_total() {
        Double Perc = 0.0;
        EditText Price = (EditText) form.findViewById(R.id.et_price);
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText tax = (EditText) form.findViewById(R.id.et_tax);
        EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        EditText bo = (EditText) form.findViewById(R.id.et_bo);
        EditText net_total = (EditText) form.findViewById(R.id.et_totl);
        EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);


        String str_p, str_q, str_Discount;


        TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);


        str_p = Price.getText().toString().replaceAll("[^\\d.]", "");
        str_q = qty.getText().toString().replaceAll("[^\\d.]", "");
        str_Discount = et_Discount.getText().toString().replaceAll("[^\\d.]", "");
        if (Price.getText().toString().trim().length() == 0) {
            str_p = "0";
        }

        if (qty.getText().toString().trim().length() == 0) {
            str_q = "0";
        }

        if (et_Discount.getText().toString().trim().length() == 0) {
            str_Discount = "0";
        }
         /* if( dis.getText().toString().length() == 0 ) {
            dis.setText("0");
        }
        if( bo.getText().toString().length() == 0 ) {
            bo.setText("0");
        }*/
        Double p = SToD(str_p);
        Double q = SToD(str_q);
        Double d = SToD(str_Discount);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        // net_total.setText(String.valueOf(df.format(  (p * q)-(d/100) )).replace(",", ""));
        net_total.setText(String.valueOf(df.format(((p - (p * (d / 100))) * q))).replace(",", ""));

    }

    private void GetQtyPerc() {

        Double Perc = 0.0;
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText bo = (EditText) form.findViewById(R.id.et_bo);
        TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);

        if (tv_StoreQty.getText().toString() == "") {
            Perc = 0.0;
        } else {
            if (SToD(tv_StoreQty.getText().toString()) == 0) {
                Perc = 0.0;
            } else {
                Perc = (SToD(qty.getText().toString()) + SToD(bo.getText().toString().replaceAll("[^\\d.]", ""))) / SToD(tv_StoreQty.getText().toString().replaceAll("[^\\d.]", ""));
            }
        }
        Perc = (Perc * 100);
        tv_qty_perc.setText(String.valueOf(SToD(Perc.toString().replaceAll("[^\\d.]", ""))));

    }

    private void NextItem() {
        String q = " Select  no  From  invf Where no >  " + ItemIdentity + "  AND Type_No = '" + Type_No + "'  Order by no asc  limit 1";
        Cursor cc = sqlHandler.selectQuery(q);
        if (cc != null && cc.getCount() != 0) {
            cc.moveToFirst();
            ItemIdentity = Integer.parseInt(cc.getString(cc.getColumnIndex("no")));
            cc.close();
            ItemCounter = ItemCounter + 1;
        }
        FillItemsByImage();
    }

    private void PrvItem() {
        String q = " Select   no  From  invf Where no <  " + ItemIdentity + "  AND Type_No = '" + Type_No + "'  Order by no Desc  limit 1";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            ItemIdentity = Integer.parseInt(c1.getString(c1.getColumnIndex("no")));
            c1.close();
            ItemCounter = ItemCounter - 1;
        }
        FillItemsByImage();
    }

}



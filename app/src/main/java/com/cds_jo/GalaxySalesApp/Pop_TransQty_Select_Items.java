
package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Deptf_adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf;
import com.cds_jo.GalaxySalesApp.assist.Cls_Invf_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems;
import com.cds_jo.GalaxySalesApp.assist.Cls_UnitItems_Adapter;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Methdes.MethodToUse;


public class Pop_TransQty_Select_Items extends DialogFragment implements View.OnClickListener {

    View form;
    Button add, cancel,BtnScan,btnClear , btn_Inc,btn_Dec;

    ListView items_Lsit;
    TextView itemnm;
     Methdes.MyTextView tv_ItemName ;

    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0;
    Double min_price = 0.0;
    EditText filter;
    ImageButton btn_filter_search;
    String UnitNo = "";
    String Operand = "";

    String UnitName = "";
    String str = "";
    RadioButton rad_Per;
    RadioButton rad_Amt;
    List<Cls_Sal_InvItems> UpdateItem;
    String Kind ;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        form = inflater.inflate(R.layout.n_layout_pop_po_select_items, container, false);
        tv_ItemName = (Methdes.MyTextView)form.findViewById(R.id.tv_ItemName) ;



        add = (Button) form.findViewById(R.id.btn_add_item);
        add.setOnClickListener(this);
        cancel = (Button) form.findViewById(R.id.btn_cancel_item);


        add.setTypeface(MethodToUse.SetTFace(getActivity()));
        cancel.setTypeface(MethodToUse.SetTFace(getActivity()));



        btn_Inc = (Button) form.findViewById(R.id.btn_Inc);
        btn_Inc.setOnClickListener(this);


        btn_Dec = (Button) form.findViewById(R.id.btn_Dec);
        btn_Dec.setOnClickListener(this);

        FillDeptf();
        cancel.setOnClickListener(this);


        Kind = getArguments().getString("Kind");

        items_Lsit = (ListView) form.findViewById(R.id.listView2);



        final EditText qty = (EditText) form.findViewById(R.id.et_qty);
        sqlHandler = new SqlHandler(getActivity());
         qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    qty.setText("", TextView.BufferType.EDITABLE);
                }
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

                GetQtyPerc();
            }
        });

        qty.setOnClickListener(new View.OnClickListener() {                                   @Override
                                   public void onClick(View v) {
                                       qty.setText("", TextView.BufferType.NORMAL);
                                        qty.setText("", TextView.BufferType.NORMAL);
                                   }
                               }
        );

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

                Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(position);
                EditText Price = (EditText) form.findViewById(R.id.et_price);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;




                UnitNo = o.getUnitno().toString();
                UnitName = o.getUnitDesc().toString();
                Operand = o.getOperand().toString();
                min = Float.valueOf(o.getMin());

                checkStoreQty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


                Cls_Invf o = (Cls_Invf) items_Lsit.getItemAtPosition(position);
                EditText qty = (EditText) form.findViewById(R.id.et_qty);
                qty.setText("1");
                qty.clearFocus();
                tv_ItemName.setText(  o.getItem_No().toString() + "  "+   o.getItem_Name().toString() );
                str =  o.getItem_Name().toString();
                fillUnit(o.getItem_No().toString());
                ItemNo = o.getItem_No().toString();
                checkStoreQty();


            }
        });

        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                 //   qty.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });



        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return form;
    }

    public void BtnScan(View view) {
        if (view.getId() == R.id.scan_button) {


        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        //Toast.makeText(getActivity(),"scanContent.toString(1111111111111)",Toast.LENGTH_SHORT).show();

        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            filter = (EditText) form.findViewById(R.id.et_Search_filter);
            EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
            filter.setText("");

            if (scanningResult.getContents() != null) {
                // Toast.makeText(getActivity(),scanContent.toString(),Toast.LENGTH_SHORT).show();

                String Item_No = DB.GetValue(getActivity(), "tblCounterKey", "Item_No", "CounterKey='"+scanContent.toString()+"'");

                String Item_Nm = DB.GetValue(getActivity(), "invf", "Item_Name", "Item_No='"+Item_No+"'");
                String Qty = DB.GetValue(getActivity(), "tblCounterKey", "A_QTY", "CounterKey='"+scanContent.toString()+"'");
                String UnitNo = DB.GetValue(getActivity(), "tblCounterKey", "UnitNo", "CounterKey='"+scanContent.toString()+"'");
                et_qty.setText(Qty);
                et_qty.clearFocus();
                tv_ItemName.setText( Item_No + "  "+ Item_Nm );
                str =  Item_Nm;
                fillUnit(Item_No );
                ItemNo = Item_No;

                Cls_UnitItems obj = new Cls_UnitItems();
                Spinner sp_units = (Spinner)form.findViewById(R.id.sp_units);
                Cls_UnitItems_Adapter UnitItem = (Cls_UnitItems_Adapter) sp_units.getAdapter();
                for (int i = 0; i < UnitItem.getCount(); i++) {
                    obj = (Cls_UnitItems) UnitItem.getItem(i);
                    if (obj.getUnitno().equalsIgnoreCase(UnitNo)) {
                        sp_units.setSelection(i);
                        break;
                    }
                }


                filter.setText(Item_No);
                FillItems();
                checkStoreQty();

            }



        }

    }

    private void checkStoreQty() {

        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());


        Double Order_qty = 0.0;
        Double Res = 0.0;


        String query = "SELECT   ifnull( qty,0)   as  qty   from ManStore where  itemno = '" + ItemNo + "'   ";
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


        query = "SELECT    ifnull( sum  ( ifnull( d.qty,0)  * (ifnull( d.UnitRate,1))) ,0)     as Qty  " +
                "from  TransferQtyhdr   h inner join TransferQtydtl d on  d.OrderNo = h.OrderNo  AND d.Kind = h.Kind " +
                " inner join  UnitItems ui on ui.item_no  = d.itemno and ui.unitno = d.UnitNo" +
                " where   h.Posted = -1  and ui.item_no ='"+ItemNo+"' ";
        c1 = sqlHandler.selectQuery(query);

        Double Sal_Qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                Sal_Qty = SToD((c1.getString(c1.getColumnIndex("Qty"))).toString());
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
        GetQtyPerc();
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
        }

        Cls_Deptf_adapter cls_unitItems_adapter = new Cls_Deptf_adapter(
                getActivity(), cls_deptfs);

        sp_items_cat.setAdapter(cls_unitItems_adapter);
    }

    private void FillItems() {
        filter = (EditText) form.findViewById(R.id.et_Search_filter);
        String query = "";
        sqlHandler = new SqlHandler(getActivity());


        Bundle bundle = this.getArguments();
        UpdateItem = (List<Cls_Sal_InvItems>) bundle.getSerializable("List");




                if (filter.getText().toString().equals("")) {
                    query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                            " inner join ManStore   on ManStore.itemno =   invf.Item_No  " +
                            "  And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where 1=1  ";

                } else {
                    query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                            " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0 "    +
                            "    where invf.Item_Name  like '%" + filter.getText().toString() + "%'  or  invf.Item_No = '" + filter.getText().toString() + "'  ";
                }

                Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
                Integer indexValue = item_cat.getSelectedItemPosition();

                if (indexValue > 0) {

                    Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

                    query = query + "and    Type_No = '" + o.getType_No().toString() + "'";

                }




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

        Cls_Invf_Adapter cls_invf_adapter = new Cls_Invf_Adapter(
                getActivity(), cls_invf_List);
        items_Lsit.setAdapter(cls_invf_adapter);


    }

    public void fillUnit(String item_no) {


        Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);
        sqlHandler = new SqlHandler(getActivity());

        String query = "Select  distinct UnitItems.unitno ,UnitItems.Min ,UnitItems.price,Unites.UnitName " +
                " , ifnull(Operand,1) as Operand from UnitItems  left join  Unites on Unites.Unitno =UnitItems.unitno where UnitItems.item_no ='" + item_no + "' order by   UnitItems.unitno desc";
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
private  void  Clear(){

    EditText qty = (EditText) form.findViewById(R.id.et_qty);
    TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
    TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);

    qty.setText("");
    ItemNo = "";
    UnitNo = "";
    Operand = "";
    tv_qty_perc.setText("");
    tv_StoreQty.setText("");
    tv_ItemName.setText("");


    EditText et_Search_filter = (EditText) form.findViewById(R.id.et_Search_filter);
    et_Search_filter.setText("");
        FillItems();
}
    @Override
    public void onClick(View v) {

        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        if (v == btnClear) {
           Clear();
        }

        if (v == cancel) {
            this.dismiss();
        }
        if (v == BtnScan  ) {
            /*IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
            scanIntegrator.initiateScan();*/

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "CODE_39");
            startActivityForResult(intent, IntentIntegrator.REQUEST_CODE);

        }
        else if (v == btn_Inc) {



            qty.setText( (Double.parseDouble(qty.getText().toString())+1)   +"" );
            if( Double.parseDouble( qty.getText().toString()) > 1 ) {
                btn_Dec.setVisibility(View.VISIBLE);
            }
            if( Double.parseDouble( qty.getText().toString()) < 1 ) {
                qty.setText(SToD( qty.getText().toString())+"");
            }

            GetQtyPerc();
        }


        else   if (v == btn_Dec) {
            qty.setText( (Double.parseDouble(qty.getText().toString())-1)   + "" );
            if( Double.parseDouble( qty.getText().toString()) < 1 ) {
                qty.setText("1");
                btn_Dec.setVisibility(View.INVISIBLE);
            }

            GetQtyPerc();

        }

        if (v == add) {


            TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
            TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);

            qty.setError(null);
            qty.clearFocus();





            if (SToD(tv_qty_perc.getText().toString()) > 100) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("اختيار المواد");
                alertDialog.setMessage("الكمية المطلوبة غير متوفرة");            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();
                return;
            }





            if (qty.getText().toString().length() == 0) {
                qty.setError("الرجاء ادخال القيمة");
                qty.requestFocus();
                return;
            }

            if (ItemNo.toString().length() == 0) {

                return;
            }



            if (getArguments().getString("Scr") == "po") {
              ((TransferQty) getActivity()).Save_List(ItemNo, str,  qty.getText().toString(),  UnitNo,UnitName ,Operand );



            }

            try {


                qty.setText("");
                ItemNo = "";
                UnitNo = "";
                Operand = "";
                tv_qty_perc.setText("");
                tv_StoreQty.setText("");

            } catch (Exception e) {
                e.printStackTrace();
            }

            EditText et_Search_filter = (EditText) form.findViewById(R.id.et_Search_filter);
            et_Search_filter.setText("");
        }

    }

    public void onListItemClick(ListView l, View v, int position, long id) {



        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);



    }



    private void GetQtyPerc() {

        Double Perc = 0.0;
        EditText qty = (EditText) form.findViewById(R.id.et_qty);

        TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);

        if (tv_StoreQty.getText().toString() == "") {
            Perc = 0.0;
        } else {
            if (SToD(tv_StoreQty.getText().toString()) == 0) {
                Perc = 0.0;
            } else {
                Perc = (SToD(qty.getText().toString()) ) / SToD(tv_StoreQty.getText().toString());
            }
        }
        Perc = (Perc * 100);
        tv_qty_perc.setText(String.valueOf(SToD(Perc.toString())));

    }


}



package com.cds_jo.GalaxySalesApp.warehouse;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Methdes.MyTextView;


public class SearchPurchesOrders extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
     TextView Search_filter;
     MyTextView tv_Title ;
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    SqlHandler sqlHandler;
    String V_type,MYear;
    @Override
    public View onCreateView(final LayoutInflater inflater   , ViewGroup container  , Bundle savestate){
        try {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            form = inflater.inflate(R.layout.search_purches_order, container, false);
            sqlHandler = new SqlHandler(getActivity());

            sqlHandler.executeQuery("Delete from PurchesOrderTemp");
            tv_Title = (MyTextView) form.findViewById(R.id.tv_Title);
            tv_Title.setText("");
            items_Lsit = (ListView) form.findViewById(R.id.listView2);
            Search_filter = (TextView) form.findViewById(R.id.et_Search_filter);
            V_type=  getArguments().getString("V_type");
            MYear =  getArguments().getString("Myear");
            Search_filter.clearFocus();
            Search_filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    FillList(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Cls_PurchesOrderSearch po_obj = (Cls_PurchesOrderSearch) arg0.getItemAtPosition(position);

                    if (getArguments().getString("Scr") == "ItemRecepit") {
                        ((ItemsRecepit) getActivity()).Set_PurchesOrder(po_obj.getOrderNo());
                    }

                    Exist_Pop();

                }


            });

        }
        catch (Exception ex){
            Toast.makeText(getActivity(),ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Do_Trans_Server_Data();
        return  form;
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
                ws.Search_PurchesOrders(V_type ,MYear, "1");
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_order_no = js.getJSONArray("order_no");
                    JSONArray js_odate = js.getJSONArray("odate");
                    JSONArray js_ven = js.getJSONArray("ven");
                    JSONArray js_br_no = js.getJSONArray("br_no");
                    JSONArray js_tot = js.getJSONArray("tot");
                    JSONArray js_dis = js.getJSONArray("dis");
                    JSONArray js_item_no = js.getJSONArray("item_no");
                    JSONArray js_UnitNo = js.getJSONArray("UnitNo");
                    JSONArray js_UnitRate = js.getJSONArray("UnitRate");
                    JSONArray js_qty = js.getJSONArray("qty");
                    JSONArray js_StoreNo = js.getJSONArray("StoreNo");
                    JSONArray js_item_name = js.getJSONArray("item_name");
                    JSONArray js_cost = js.getJSONArray("cost");
                    JSONArray js_OrderMyear = js.getJSONArray("OrderMyear");
                    JSONArray js_Order_V_TYPE = js.getJSONArray("Order_V_TYPE");
                    JSONArray js_StoreNm = js.getJSONArray("StoreNm");
                    JSONArray js_br_nm = js.getJSONArray("br_nm");
                    JSONArray js_venNm = js.getJSONArray("venNm");
                    JSONArray js_UnitName = js.getJSONArray("UnitName");
                    JSONArray js_Po_Total = js.getJSONArray("Po_Total");
                    JSONArray js_dispercent = js.getJSONArray("dispercent");
                    JSONArray js_LineDiscount = js.getJSONArray("LineDiscount");

                    q = "Delete from PurchesOrderTemp";
                    sqlHandler.executeQuery(q);
                    q = "delete from sqlite_sequence where name='PurchesOrderTemp'";
                    sqlHandler.executeQuery(q);

                    for (i = 0; i < js_order_no.length(); i++) {
                        q = "Insert INTO PurchesOrderTemp(order_no,odate,ven,br_no,tot,dis,item_no,UnitNo,UnitRate,qty,StoreNo ,item_name ,cost,OrderMyear,Order_V_TYPE,StoreNm,br_nm,venNm,UnitName,Po_Total,dispercent,LineDiscount) values('"+
                                          js_order_no.get(i).toString()
                                + "','" + js_odate.get(i).toString()
                                + "','" + js_ven.get(i).toString()
                                + "','" + js_br_no.get(i).toString()
                                + "','" + js_tot.get(i).toString()
                                + "','" + js_dis.get(i).toString()
                                + "','" + js_item_no.get(i).toString()
                                + "','" + js_UnitNo.get(i).toString()
                                + "','" + js_UnitRate.get(i).toString()
                                + "','" + js_qty.get(i).toString()
                                + "','" + js_StoreNo.get(i).toString()
                                + "','" + js_item_name.get(i).toString()
                                + "','" + js_cost.get(i).toString()
                                + "','" + js_OrderMyear.get(i).toString()
                                + "','" + js_Order_V_TYPE.get(i).toString()
                                + "','" + js_StoreNm.get(i).toString()
                                + "','" + js_br_nm.get(i).toString()
                                + "','" + js_venNm.get(i).toString()
                                + "','" + js_UnitName.get(i).toString()
                                + "','" + js_Po_Total.get(i).toString()
                                + "','" + js_dispercent.get(i).toString()
                                + "','" + js_LineDiscount.get(i).toString()
                                + "' )";
                        sqlHandler.executeQuery(q);
                        custDialog.setMax(js_order_no.length());
                        custDialog.incrementProgressBy(1);
                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }
                    }
                    final int total = i;
                    _handler.post(new Runnable() {

                        public void run() {
                            FillList("");
                            custDialog.dismiss();


                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("طلبات الشراء");
                            alertDialog.setMessage("تمت عملية التحديث بنجاح");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                            //alertDialog.show();


                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("طلبات الشراء");
                            alertDialog.setMessage("لا يوجد بيانات ");
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
    private  void  FillList(String f){
    ArrayList<Cls_PurchesOrderSearch> cls_maint_card_searches = new ArrayList<Cls_PurchesOrderSearch>();
    cls_maint_card_searches.clear();


    String q = " Select distinct  order_no  , odate  From  PurchesOrderTemp " +
            "  Where (order_no like '%"+f+"%'  or odate like '%"+f+"%')   " +
            " Order By Cast( order_no as integer ) asc";

    SqlHandler sqlHandler = new SqlHandler(getActivity());
    Cursor c1 = sqlHandler.selectQuery(q);

    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            do {
                Cls_PurchesOrderSearch obj = new Cls_PurchesOrderSearch();
                obj.setOrderNo(c1.getString(c1.getColumnIndex("order_no")));
                obj.setDate(c1.getString(c1.getColumnIndex("odate")));
                cls_maint_card_searches.add(obj);
            } while (c1.moveToNext());
        }

        c1.close();
    }

        PurchesOrderSearch_Adapter cls_search_po_adapter = new PurchesOrderSearch_Adapter(
            this.getActivity(), cls_maint_card_searches);
    items_Lsit.setAdapter(cls_search_po_adapter);



}
    public void Exist_Pop ()
    {
        this.dismiss();
    }
    @Override
    public void onClick(View v) {
        Button bu = (Button) v ;
        if (bu.getText().toString().equals("Cancel")){
            this.dismiss();
        }
        else  if (bu.getText().toString().equals("Add")){

        }


    }



}


package com.cds_jo.GalaxySalesApp.OrderApproval;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_LEFT;
import static com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_RIGHT;


public class Show_Orders_Need_Approval extends DialogFragment implements View.OnClickListener {

    View form;
    SimpleDateFormat sdf;
    SwipeMenuListView lv_Items;
    ProgressDialog progressDialog;
    SqlHandler sqlHandler;
    ArrayList<Cls_SalesOrder_H> Lists;
    Cls_SalesOrder_H obj;
    SwipeMenuCreator creator;
    RelativeLayout.LayoutParams lp;
    Drawable greenProgressbar;
    TextView tv;
    String q = "";
    String UserID, ManLevel;
    SharedPreferences sharedPreferences;
    long PostID;
     Context context;
    @Override
    public void onStart()
    {
        super.onStart();

        if (getDialog() == null)
            return;


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom( getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(lp);

        getDialog().getWindow().setWindowAnimations(0);


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        form = inflater.inflate(R.layout.show_orders_need_aproval, container, false);
        context=getActivity();
        sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        lv_Items = (SwipeMenuListView) form.findViewById(R.id.lv_Items);


        Calendar c = Calendar.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        UserID= sharedPreferences.getString("LoginAccount", "");
        ManLevel = DB.GetValue(context,"manf","ManLevel","where LOWER(man)  like'"+UserID.toString().toLowerCase()+"'");

        sqlHandler = new SqlHandler(context);
        Lists   =new ArrayList<>();
        obj = new Cls_SalesOrder_H();

          //GetOrders();
         // UpdateDateDtl();
          ShowList();


        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu  ) {



               SwipeMenuItem details= new SwipeMenuItem(context);
                details.setBackground(R.color.Blue);
                details.setWidth(lv_Items.getWidth() / 5);
                details.setTitle("استعراض");
                details.setTitleSize(25);
                details.setTitleColor(Color.WHITE);
                menu.addMenuItem(details);

                SwipeMenuItem del = new SwipeMenuItem(context);
                del.setBackground(R.color.Red);
                del.setWidth(lv_Items.getWidth() /5 );
                del.setTitle("عدم الموافقة");
                del.setTitleSize(25);
                del.setTitleColor(Color.WHITE);
                menu.addMenuItem(del);

                SwipeMenuItem Post= new SwipeMenuItem(context);
                Post.setBackground(R.color.Green);
                Post.setWidth(lv_Items.getWidth() / 5);
                Post.setTitle("موافق");
                Post.setTitleSize(25);
                Post.setTitleColor(Color.WHITE);
                menu.addMenuItem(Post);



            }
        };
        ImageView RefreshImg = (ImageView)form.findViewById(R.id.RefreshImg);
        // set a onclick listener for when the button gets clicked
        RefreshImg.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                GetOrders();
            }
        });



        lv_Items.setMenuCreator(creator);

        lv_Items.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                //LsvCars.setMenuCreator(creator);
                lv_Items.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
        lv_Items.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                obj = (Cls_SalesOrder_H) lv_Items.getItemAtPosition(position);
                switch (index) {
                    case 2:

                        PostID = 0;
                        new UpdateOrderStatus().execute(UserID,obj.getOrderno(),"1","تمت من قبل المشرف");
                        break;
                    case 1:

                        new UpdateOrderStatus().execute(UserID,obj.getOrderno(),"2","تمت من قبل المشرف");
                        break;
                    case 0:
                        String q = "select orderno from Po_dtl  where   orderno ='"+obj.getOrderno()+"'";
                        Cursor c1 = sqlHandler.selectQuery(q);

                        if (c1 != null && c1.getCount() != 0) {
                            c1.close();
                            ((OrdersItems) context).Set_OrderForApproval(obj.getOrderno(), obj.getCustNm(), obj.getAcc());
                            dismiss();
                        }else{
                            GetSalesOrder(obj.getOrderno());
                        }


                        break;
                }

                return false;
            }
        });

        lv_Items.setSwipeDirection(DIRECTION_RIGHT);


        lv_Items.setSwipeDirection(DIRECTION_LEFT);


        lv_Items.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });


        lv_Items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                obj = (Cls_SalesOrder_H) lv_Items.getItemAtPosition(position);

                String q = "select orderno from Po_dtl  where   orderno ='"+obj.getOrderno()+"'";
                Cursor c1 = sqlHandler.selectQuery(q);

                if (c1 != null && c1.getCount() != 0) {
                    c1.close();
                    ((OrdersItems) context).Set_OrderForApproval(obj.getOrderno(), obj.getCustNm(), obj.getAcc());
                    dismiss();
                }else{
                    GetSalesOrder(obj.getOrderno());
                }



               /* ((OrdersItems) context).Set_OrderForApproval(obj.getOrderno(), obj.getCustNm(), obj.getAcc());
                dismiss();*/
            }
        });



        return form;
    }
    private  void GetSalesOrder(final String OrderNO){


        greenProgressbar = this.getResources().getDrawable(R.drawable.progrees_states);
        final Handler _handler = new Handler();
        tv = new TextView(context);
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
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("طلبات البيع");
        progressDialog.setCustomTitle(tv);
        progressDialog.setProgressDrawable(greenProgressbar);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(context);
               // ws.GetSalesOrderForApproval(OrderNO,UserID,ManLevel,"0","");
                try {
                    Integer i;
                    String q;
                    if(We_Result.ID>0) {

                        q = "Delete from Po_dtl  where   orderno ='"+OrderNO+"'";
                        sqlHandler.executeQuery(q);
                        q = "Delete from Po_Hdr where OrderNo='"+OrderNO+"'";
                        sqlHandler.executeQuery(q);


                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        JSONArray js_OrderNo = js.getJSONArray("OrderNo");
                        JSONArray js_CustNo = js.getJSONArray("CustNo");
                        JSONArray js_Cust_Nm = js.getJSONArray("Cust_Nm");
                        JSONArray js_Tax_Total = js.getJSONArray("TaxTotal");
                        JSONArray js_PoDate = js.getJSONArray("PoDate");
                        JSONArray js_Posted = js.getJSONArray("Posted");
                        JSONArray js_Notes = js.getJSONArray("Notes");
                        JSONArray js_Total = js.getJSONArray("Total");
                        JSONArray js_TransDate = js.getJSONArray("TransDate");
                        JSONArray js_V_OrderNo = js.getJSONArray("V_OrderNo");
                        JSONArray js_SuperVisorNotes = js.getJSONArray("V_OrderNo");
                        JSONArray js_NetTotal = js.getJSONArray("NetTotal");
                        JSONArray js_UserID = js.getJSONArray("UserID");
                        JSONArray js_MobileOrder = js.getJSONArray("MobileOrder");


                        JSONArray js_ItemNo = js.getJSONArray("ItemNo");
                        JSONArray js_Price = js.getJSONArray("Price");
                        JSONArray js_Qty = js.getJSONArray("Qty");
                        JSONArray js_Tax = js.getJSONArray("Tax");
                        JSONArray js_Unit = js.getJSONArray("Unit");
                        JSONArray js_Bonus = js.getJSONArray("Bonus");
                        JSONArray js_TaxAmt = js.getJSONArray("TaxAmt");
                        JSONArray js_LineTotal = js.getJSONArray("LineTotal");
                        JSONArray js_include_Tax = js.getJSONArray("IncludeTax");
                        JSONArray js_ManNm = js.getJSONArray("ManNm");

                        for (i = 0; i < js_ID.length(); i++) {
                            q = "INSERT INTO Po_Hdr(ID,OrderNo,acc,Nm,date,userid,posted,Notes,date,Net_Total" +
                                    "  ,MobileOrder,Total,Tax_Total,V_OrderNo,SuperVisorNotes,ForApproval,include_Tax,OwnerSalesManNm,OwnerSalesManNo) values ('"
                                    + js_ID.get(i).toString()
                                    + "','" + js_OrderNo.get(i).toString()
                                    + "','" + js_CustNo.get(i).toString()
                                    + "','" + js_Cust_Nm.get(i).toString()
                                    + "','" + js_PoDate.get(i).toString()
                                    + "','" + js_UserID.get(i).toString()
                                    + "','" + js_Posted.get(i).toString()
                                    + "','" + js_Notes.get(i).toString()
                                    + "','" + js_TransDate.get(i).toString()
                                    + "','" + js_NetTotal.get(i).toString()
                                    + "','" + js_MobileOrder.get(i).toString()
                                    + "','" + js_Total.get(i).toString()
                                    + "','" + js_Tax_Total.get(i).toString()
                                    + "','" + js_V_OrderNo.get(i).toString()
                                    + "','" + js_SuperVisorNotes.get(i).toString()
                                    + "','1','" + js_include_Tax.get(i).toString()
                                    + "','" + js_ManNm.get(i).toString()
                                    + "','" + js_UserID.get(i).toString() + "'   )";

                            sqlHandler.executeQuery(q);


                            q = "INSERT INTO Po_dtl( orderno,itemno,price,qty,tax,unitNo,bounce_qty,tax_Amt,total,ForApproval " +
                                    "  ,dis_Amt,dis_per,OrgPrice,bounce_unitno,net_total,ProID,Pro_bounce,Pro_dis_Per,Pro_amt,pro_Total,ID) values ('"
                                    + js_OrderNo.get(i).toString()
                                    + "','" + js_ItemNo.get(i).toString()
                                    + "','" + js_Price.get(i).toString()
                                    + "','" + js_Qty.get(i).toString()
                                    + "','" + js_Tax.get(i).toString()
                                    + "','" + js_Unit.get(i).toString()
                                    + "','" + js_Bonus.get(i).toString()
                                    + "','" + js_TaxAmt.get(i).toString()
                                    + "','" + js_LineTotal.get(i).toString()
                                    + "','1','0','0','" +
                                    js_Price.get(i).toString() + "','0','0','0','0','0','0','0','0')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_ID.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                /*sqlHandler.executeQuery("DELETE  FROM Po_Hdr WHERE   ForApproval = '1' and   no   NOT IN (SELECT MAX(no) FROM Po_Hdr GROUP BY OrderNo )");
                                ShowList();
                                ((OrdersItems) context).NotifcationSitting();
                                progressDialog.dismiss();*/
                                ((OrdersItems) context).Set_OrderForApproval(obj.getOrderno(), obj.getCustNm(), obj.getAcc());
                                dismiss();

                            }
                        });




                    }else {
                        Toast.makeText(context,"لا يوجد بيانات",Toast.LENGTH_SHORT).show();
                    }
                } catch (final Exception e) {
                    progressDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                         /*   ShowList();
                            ((OrdersItems) context).NotifcationSitting();*/
                            Toast.makeText(context,"لا يوجد بيانات",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }
    private  void GetOrders(){
      //  Toast.makeText(getActivity(),"GetOrders",Toast.LENGTH_LONG).show();
        q = "Delete from Po_dtl  where   orderno in  (select orderno from Po_Hdr where ForApproval='1')";
        sqlHandler.executeQuery(q);
        q = "Delete from Po_Hdr where ForApproval='1'";

        sqlHandler.executeQuery(q);
/*

 */
        greenProgressbar = this.getResources().getDrawable(R.drawable.progrees_states);
        final Handler _handler = new Handler();
        tv = new TextView(context);
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
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("طلبات البيع");
        progressDialog.setCustomTitle(tv);
        progressDialog.setProgressDrawable(greenProgressbar);
        try {
            progressDialog.show();
        }catch ( Exception df){}
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(context);
             //   ws.GetSalesOrderForApproval("-1",UserID,ManLevel,"0","" );
                try {
                    Integer i;
                    String q;
                    if(We_Result.ID>0) {
                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        JSONArray js_OrderNo = js.getJSONArray("OrderNo");
                        JSONArray js_CustNo = js.getJSONArray("CustNo");
                        JSONArray js_Cust_Nm = js.getJSONArray("Cust_Nm");
                        JSONArray js_Tax_Total = js.getJSONArray("TaxTotal");
                        JSONArray js_PoDate = js.getJSONArray("PoDate");
                        JSONArray js_Posted = js.getJSONArray("Posted");
                        JSONArray js_Notes = js.getJSONArray("Notes");
                        JSONArray js_Total = js.getJSONArray("Total");
                        JSONArray js_TransDate = js.getJSONArray("TransDate");
                        JSONArray js_V_OrderNo = js.getJSONArray("V_OrderNo");
                        JSONArray js_SuperVisorNotes = js.getJSONArray("V_OrderNo");
                        JSONArray js_NetTotal = js.getJSONArray("NetTotal");
                        JSONArray js_UserID = js.getJSONArray("UserID");
                        JSONArray js_MobileOrder = js.getJSONArray("MobileOrder");


                        JSONArray js_ItemNo = js.getJSONArray("ItemNo");
                        JSONArray js_Price = js.getJSONArray("Price");
                        JSONArray js_Qty = js.getJSONArray("Qty");
                        JSONArray js_Tax = js.getJSONArray("Tax");
                        JSONArray js_Unit = js.getJSONArray("Unit");
                        JSONArray js_Bonus = js.getJSONArray("Bonus");
                        JSONArray js_TaxAmt = js.getJSONArray("TaxAmt");
                        JSONArray js_LineTotal = js.getJSONArray("LineTotal");
                        JSONArray js_include_Tax = js.getJSONArray("IncludeTax");
                        JSONArray js_ManNm = js.getJSONArray("ManNm");

                        for (i = 0; i < js_ID.length(); i++) {
                            q = "INSERT INTO Po_Hdr(ID,OrderNo,acc,Nm,date,userid,posted,Notes,date,Net_Total" +
                                    "  ,MobileOrder,Total,Tax_Total,V_OrderNo,SuperVisorNotes,ForApproval,include_Tax,OwnerSalesManNm,OwnerSalesManNo) values ('"
                                    + js_ID.get(i).toString()
                                    + "','" + js_OrderNo.get(i).toString()
                                    + "','" + js_CustNo.get(i).toString()
                                    + "','" + js_Cust_Nm.get(i).toString()
                                    + "','" + js_PoDate.get(i).toString()
                                    + "','" + js_UserID.get(i).toString()
                                    + "','" + js_Posted.get(i).toString()
                                    + "','" + js_Notes.get(i).toString()
                                    + "','" + js_TransDate.get(i).toString()
                                    + "','" + js_NetTotal.get(i).toString()
                                    + "','" + js_MobileOrder.get(i).toString()
                                    + "','" + js_Total.get(i).toString()
                                    + "','" + js_Tax_Total.get(i).toString()
                                    + "','" + js_V_OrderNo.get(i).toString()
                                    + "','" + js_SuperVisorNotes.get(i).toString()
                                    + "','1','" + js_include_Tax.get(i).toString()
                                    + "','" + js_ManNm.get(i).toString()
                                    + "','" + js_UserID.get(i).toString() + "'   )";

                            sqlHandler.executeQuery(q);


                            q = "INSERT INTO Po_dtl( orderno,itemno,price,qty,tax,unitNo,bounce_qty,tax_Amt,total,ForApproval " +
                                    "  ,dis_Amt,dis_per,OrgPrice,bounce_unitno,net_total,ProID,Pro_bounce,Pro_dis_Per,Pro_amt,pro_Total,ID) values ('"
                                    + js_OrderNo.get(i).toString()
                                    + "','" + js_ItemNo.get(i).toString()
                                    + "','" + js_Price.get(i).toString()
                                    + "','" + js_Qty.get(i).toString()
                                    + "','" + js_Tax.get(i).toString()
                                    + "','" + js_Unit.get(i).toString()
                                    + "','" + js_Bonus.get(i).toString()
                                    + "','" + js_TaxAmt.get(i).toString()
                                    + "','" + js_LineTotal.get(i).toString()
                                    + "','1','0','0','" +
                                    js_Price.get(i).toString() + "','0','0','0','0','0','0','0','0')";
                            sqlHandler.executeQuery(q);
                            progressDialog.setMax(js_ID.length());
                            progressDialog.incrementProgressBy(1);
                            if (progressDialog.getProgress() == progressDialog.getMax()) {
                                progressDialog.dismiss();
                            }
                        }
                        final int total = i;
                        _handler.post(new Runnable() {
                            public void run() {
                                try {
                                    sqlHandler.executeQuery("DELETE  FROM Po_Hdr WHERE   ForApproval = '1' and   no   NOT IN (SELECT MAX(no) FROM Po_Hdr GROUP BY OrderNo )");
                                    ShowList();
                                   // ((OrdersItems) context).NotifcationSitting();
                                    progressDialog.dismiss();
                                }catch (Exception df){}
                            }
                        });
                    }else {
                        _handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    progressDialog.dismiss();
                                   // ((OrdersItems) context).HideIcon ();
                                    dismiss();

                                }catch (Exception df){}
                            }
                        });

                    }
                } catch (final Exception e) {
                    progressDialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            try {
                                ShowList();
                               // ((OrdersItems) context).NotifcationSitting();
                                Toast.makeText(context, "لا يوجد بيانات", Toast.LENGTH_SHORT).show();
                            }catch (Exception ds){}
                        }
                    });
                }
            }
        }).start();

    }
    private  void ShowList(){
        Lists.clear();
        q= "  Select  distinct  ID,OwnerSalesManNm ,Net_Total , orderno , acc  , date  , userid , manf.name as ManNm ,Notes ,Total, Nm " +
                "   From Po_Hdr   left join manf on manf.username=  Po_Hdr.userid  where ForApproval='1' ";

        q = "Select distinct po.orderno,po.date ,po.Net_Total   , c.name  , po.acc from Po_Hdr po Left join Customers c on c.no = po.acc ";

        Cursor c1=sqlHandler.selectQuery(q);
        if(c1!=null && c1.getCount()>0){
            c1.moveToFirst();
            do{
                obj = new Cls_SalesOrder_H();
                obj.setOrderno(c1.getString(c1.getColumnIndex("orderno")));
                obj.setAcc(c1.getString(c1.getColumnIndex("acc")));
                obj.setDate(c1.getString(c1.getColumnIndex("date")));
                obj.setUserid("14");
                obj.setManNm("أشرف");
                obj.setCustNm(c1.getString(c1.getColumnIndex("name")));
                obj.setNotes("");
                obj.setNet_Total(c1.getString(c1.getColumnIndex("Net_Total")));
                obj.setID("1");
                Lists.add(obj);
            } while (c1.moveToNext());
            c1.close();
        }

        Cls_Sales_Order_Hdr_Adapter contactListAdapter= new Cls_Sales_Order_Hdr_Adapter(
                context, Lists);
        lv_Items.setAdapter(contactListAdapter);

    }
    @Override
    public void onClick(View v) {

    }

    private class UpdateOrderStatus extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {

            tv = new TextView(context);
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
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على تعديل حالة الطلب  ");
            tv.setText("الموافقات على طلب البيع");
            progressDialog.setCustomTitle(tv);
            progressDialog.setProgressDrawable(greenProgressbar);
            try {
                progressDialog.show();
            }catch (Exception df){}
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                CallWebServices ws = new CallWebServices(context);
                //PostID = ws.UpdateSalesOrders_Status(params[0], params[1], params[2], params[3]);
            }catch (Exception sdf){}
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                tv.setText("الموافقات على طلب البيع");
                alertDialog.setMessage("تمت عملية الحفظ بنجاح");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setPositiveButton("رجوع", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setTitle("الموافقات على طلب البيع");

                if (PostID > 0) {

                    GetOrders();
                    alertDialog.setMessage("تمت العملية  بنجاح");
                    alertDialog.setIcon(R.drawable.tick);
                } else {
                    alertDialog.setMessage(" العملية لم تتم بنجاح");
                    alertDialog.setIcon(R.mipmap.icon_delete);
                }
                progressDialog.dismiss();
                alertDialog.show();
            }catch (Exception sdf){}
        }

    }


}



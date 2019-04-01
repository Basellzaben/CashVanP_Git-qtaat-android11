
package com.cds_jo.GalaxySalesApp;

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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Methdes.MyTextView;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopShowOffers_Summery extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel,acc,invoice,rec,order;
    Button back,btn_update;
    SqlHandler sqlHandler;
    ListView Lst1,Lst2,Lst3 ;
    ArrayList<Cls_Offers_Hdrs> ItemsList1 ;
    ArrayList<Cls_Offers_Groups> ItemsList2 ;
    ArrayList<Cls_Offer_Gift> ItemsList3;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    TextView tv;
    String GroupNo = "";
    String  q;
    String UserID;
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
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(0);

    }


    @Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container,Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        form =inflater.inflate(R.layout.show_offer_summery,container,false);
        getDialog().setTitle("معلومات العروض");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UserID = sharedPreferences.getString("UserID", "-1");
        ItemsList1 = new ArrayList<Cls_Offers_Hdrs>();
        ItemsList2 = new ArrayList<Cls_Offers_Groups>();
        ItemsList3 = new ArrayList<Cls_Offer_Gift>();
        back = (Button)form.findViewById(R.id.btn_back);
        btn_update = (Button)form.findViewById(R.id.btn_update);
        Lst1 = (ListView)form.findViewById(R.id.Lst1);
        Lst2 = (ListView)form.findViewById(R.id.Lst2);
        Lst3 = (ListView)form.findViewById(R.id.Lst3);
        FillList1("");
        back.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        Lst1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View argu, int position, long arg3) {
                try{

                    for (int i = 0; i < Lst1.getChildCount(); i++) {
                        View listItem = Lst1.getChildAt(i);
                        if(i%2==0)
                            listItem.setBackgroundColor(Color.WHITE);
                        if(i%2==1)
                            listItem.setBackgroundColor(getActivity().getResources().getColor(R.color.Gray2));
                    }

                    argu.setBackgroundColor(Color.GRAY);



                    Cls_Offers_Hdrs o = (Cls_Offers_Hdrs) Lst1.getItemAtPosition(position);
                    getDialog().setTitle(" معلومات العروض " +" / "+ o.getDesc());
                    FillList2(o.getOfferNo());
                    FillList3(o.getOfferNo());

                }catch (Exception ex) {
                    Toast.makeText(getActivity(),ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }




            }
        });



       /* FillList2("0");
        FillList3("0");*/


         getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        return  form;
    }
private  void FillList1(String Gro_No){
    ComInfo.ComNo = Integer.parseInt(DB.GetValue(getActivity(), "ComanyInfo", "CompanyID", "1=1"));

         sqlHandler = new SqlHandler(getActivity());
    String query ="";
    if (ComInfo.ComNo == 4) {
          query = "  Select  distinct d.Type_Name , c.catName,ofh.Offer_Type_Item,ofh.Offer_Exp_Date,  ofh.Offer_No, ofh.Offer_Name,ofh.Offer_Date  from Offers_Hdr ofh" +
                "    inner  join Offers_Groups og on ofh.gro_no =og.grv_no "+ //"  where odc.Gro_Num  = '"+GroupNo+"'";
                "    inner Join  invf on  invf.Item_No = og.item_no" +
                "  left Join  deptf d on  d.Type_No = ofh.Offer_Type_Item "+
                "  left Join  Categ c on c.catno = ofh.Cate_Offer ";

    }else {
        query = " Select  ofh.*,  d.Type_Name , c.catName  from Offers_Hdr ofh    " +
                " left Join  deptf d on  d.Type_No = ofh.Offer_Type_Item  "+
                " left Join  Categ c on c.catno = ofh.Cate_Offer ";


    }


    Cursor c1 = sqlHandler.selectQuery(query);
    Cls_Offers_Hdrs obj ;
    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            do {
                  obj = new Cls_Offers_Hdrs();
                  obj.setOfferNo(c1.getString(c1.getColumnIndex("Offer_No")));
                  obj.setDesc(c1.getString(c1.getColumnIndex("Offer_Name")));
                  obj.setOfferDate(c1.getString(c1.getColumnIndex("Offer_Date")));
                  obj.setCate_Offer(c1.getString(c1.getColumnIndex("catName")));
                  obj.setOffer_Type_Item(c1.getString(c1.getColumnIndex("Type_Name")));
                obj.setEnd_Date(c1.getString(c1.getColumnIndex("Offer_Exp_Date")));
             ItemsList1.add(obj);

            } while (c1.moveToNext());

        }
        c1.close();
            }

    Item_Offer_Hdrs_Adapter cls_unitItems_adapter = new Item_Offer_Hdrs_Adapter(
            getActivity(), ItemsList1);

    Lst1.setAdapter(cls_unitItems_adapter);



}
private  void FillList2(String Gro_No){
    ItemsList2.clear();
    MyTextView  tv_Backagtotal  = (MyTextView)form.findViewById(R.id.tv_Backagtotal);
    MyTextView  tv_totalAmt  = (MyTextView)form.findViewById(R.id.tv_totalAmt);
    tv_Backagtotal.setText("");
    tv_totalAmt.setText("");
    sqlHandler = new SqlHandler(getActivity());
    String query = " Select  og.item_no,og.qty ,invf.Item_Name ,og.gro_qty,og.gro_amt ,og.gro_amt, og.gro_qty  " +
            "  from Offers_Groups og " +
            "      Left Join  invf on  invf.Item_No = og.item_no  " +
            "      inner  join Offers_Hdr ofh on ofh.gro_no =og.grv_no " +
            "      where ofh.Offer_No   = '"+Gro_No+"'";

    Cursor c1 = sqlHandler.selectQuery(query);
    Cls_Offers_Groups obj ;

    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            tv_Backagtotal.setText(c1.getString(c1.getColumnIndex("gro_qty")));
            tv_totalAmt.setText(c1.getString(c1.getColumnIndex("gro_amt")));

            do {
                obj = new Cls_Offers_Groups();
                obj.setItem_no(c1.getString(c1.getColumnIndex("item_no")));
                obj.setGro_ename(c1.getString(c1.getColumnIndex("Item_Name")));
                obj.setQty(c1.getString(c1.getColumnIndex("qty")));
                obj.setGro_qty(c1.getString(c1.getColumnIndex("gro_qty")));
                obj.setGro_amt(c1.getString(c1.getColumnIndex("gro_amt")));

                ItemsList2.add(obj);

            } while (c1.moveToNext());

        }
        c1.close();
    }

    Item_Offers_Groups_Adapter cls_unitItems_adapter = new Item_Offers_Groups_Adapter(
            getActivity(), ItemsList2);

    Lst2.setAdapter(cls_unitItems_adapter);


    }
private  void FillList3(String Gro_No){
    ItemsList3.clear();
    LinearLayout RR=(LinearLayout)form.findViewById(R.id.RR3);
    sqlHandler = new SqlHandler(getActivity());
    String query = " Select  ofh.sum_Qty_item, odg.Item_No,odg.Unit_No,odg.QTY ,invf.Item_Name  , ofh.Offer_Result_Type,ofh.Offer_Amt,ofh.Offer_Dis,ofh.total_item " +
            "     from  Offers_Hdr ofh    " +
            "     Left join Offers_Dtl_Gifts odg on ofh.Offer_No =odg.Trans_ID " +
            "     left Join  invf on  invf.Item_No = odg.Item_No     where ofh.Offer_No  = '"+Gro_No+"'";

    Cursor c1 = sqlHandler.selectQuery(query);
    Cls_Offer_Gift obj ;

    MyTextView  tv_EffictType  = (MyTextView)form.findViewById(R.id.tv_EffictType);
    MyTextView  Tv_lblAmt  = (MyTextView)form.findViewById(R.id.Tv_lblAmt);
    MyTextView  tv_Amt  = (MyTextView)form.findViewById(R.id.tv_Amt);
    MyTextView  tv_TotalPackage  = (MyTextView)form.findViewById(R.id.tv_TotalPackage);
    tv_EffictType.setText("  ");
    Tv_lblAmt.setText(" ");
    tv_Amt.setText( "");
    Lst3.setVisibility(View.INVISIBLE);
    RR.setVisibility(View.INVISIBLE);
    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            if(c1.getString(c1.getColumnIndex("Offer_Result_Type")).equalsIgnoreCase("1")){
                tv_EffictType.setText("خصم نسبة  ");
                Tv_lblAmt.setText("النسبة :");
                tv_Amt.setText( SToD( c1.getString(c1.getColumnIndex("Offer_Dis"))).toString());
                tv_TotalPackage.setVisibility(View.INVISIBLE);
            }else if (c1.getString(c1.getColumnIndex("Offer_Result_Type")).equalsIgnoreCase("2")){
                tv_EffictType.setText("خصم مبلغ  ");
                Tv_lblAmt.setText("المبلغ :");
                tv_Amt.setText(SToD(c1.getString(c1.getColumnIndex("Offer_Amt"))).toString());
                tv_TotalPackage.setVisibility(View.INVISIBLE);
            }else
            {
                Lst3.setVisibility(View.VISIBLE);
                tv_TotalPackage.setVisibility(View.VISIBLE);
                RR.setVisibility(View.VISIBLE);
                tv_EffictType.setText("هدية ");
                Tv_lblAmt.setText("عدد المواد :");
                tv_Amt.setText(SToD(c1.getString(c1.getColumnIndex("total_item"))).toString());
                tv_TotalPackage.setText(SToD(c1.getString(c1.getColumnIndex("sum_Qty_item"))).toString());


            }

            do {
                obj = new Cls_Offer_Gift();
                obj.setItemNo(c1.getString(c1.getColumnIndex("Item_No")));
                obj.setItemNm(c1.getString(c1.getColumnIndex("Item_Name")));
                obj.setQy(c1.getString(c1.getColumnIndex("QTY")));
                ItemsList3.add(obj);
            } while (c1.moveToNext());

        }
        c1.close();
    }

    Item_Offer_Gift_Adapter cls_unitItems_adapter = new Item_Offer_Gift_Adapter(
            getActivity(), ItemsList3);

    Lst3.setAdapter(cls_unitItems_adapter);
    }


    public void onClick(View v) {

         if (v == back) {
             this.dismiss();
        }

      if (v == btn_update) {
          UpdateOffers();
        }



    }
    private void UpdateOffers() {


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
                CallWebServices ws = new CallWebServices(getActivity());
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
                        //custDialog.setMax(js_ID.length());
                        //  custDialog.incrementProgressBy(1);
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
                            alertDialog.setTitle("طلب  البيع");
                            alertDialog.setMessage("تمت عملية تحديث العروض بنجاح  عدد العروض هو :" + total + "");
                            q = "DELETE   FROM Offers_Hdr WHERE no NOT IN (SELECT MAX(no) FROM Offers_Hdr GROUP BY Offer_No )";
                            sqlHandler.executeQuery(q);

                            FillList1("");
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
                CallWebServices ws = new CallWebServices(getActivity());
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
                CallWebServices ws = new CallWebServices(getActivity());
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
}



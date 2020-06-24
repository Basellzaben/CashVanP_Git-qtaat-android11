
package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Methdes.MyTextView;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopShowOffers extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel,acc,invoice,rec,order;
    Button back;
    SqlHandler sqlHandler;
    ListView Lst1,Lst2,Lst3 ;
    ArrayList<Cls_Offers_Hdrs> ItemsList1 ;
    ArrayList<Cls_Offers_Groups> ItemsList2 ;
    ArrayList<Cls_Offer_Gift> ItemsList3;
String GroupNo = "";
    @Override
    public void onStart()
    {
        super.onStart();


        if (getDialog() == null)
            return;


       WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom( getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(lp);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(0);

    }


    @Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container,Bundle savestate){

       form =inflater.inflate(R.layout.show_offers,container,false);
        getDialog().setTitle("معلومات العروض");
        ItemsList1 = new ArrayList<Cls_Offers_Hdrs>();
        ItemsList2 = new ArrayList<Cls_Offers_Groups>();
        ItemsList3 = new ArrayList<Cls_Offer_Gift>();
        back = (Button)form.findViewById(R.id.button42);
        Lst1 = (ListView)form.findViewById(R.id.Lst1);
        Lst2 = (ListView)form.findViewById(R.id.Lst2);
        Lst3 = (ListView)form.findViewById(R.id.Lst3);
        FillList1("");
        back.setOnClickListener(this);
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



        FillList2("0");
        FillList3("0");


         getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        return  form;
    }
private  void FillList1(String Gro_No){
    ComInfo.ComNo = Integer.parseInt(DB.GetValue(getActivity(), "ComanyInfo", "CompanyID", "1=1"));

         sqlHandler = new SqlHandler(getActivity());
    String query ="";
    if (ComInfo.ComNo == 4) {
          query = "  Select  distinct d.Type_Name , c.catName,ofh.Offer_Type_Item, ofh.Offer_Exp_Date,ofh.Offer_No, ofh.Offer_Name,ofh.Offer_Date  from Offers_Hdr ofh" +
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



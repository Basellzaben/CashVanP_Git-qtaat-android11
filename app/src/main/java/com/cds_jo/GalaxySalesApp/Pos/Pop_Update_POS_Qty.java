package com.cds_jo.GalaxySalesApp.Pos;

import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.assist.Convert_Sal_Return_To_ImgActivity;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
import com.cds_jo.GalaxySalesApp.warehouse.ItemsRecepit;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import Methdes.MyTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Hp on 21/08/2017.
 */

public class Pop_Update_POS_Qty extends DialogFragment implements View.OnClickListener {
    Button Inc, Dec,btn_Clear,btn_Back;
    View form;
    EditText tv_qty;
    MyTextView   Title;
    String OrderNo ,ItemNo="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        form = inflater.inflate(R.layout.update_pos__qty, container, false);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER|Gravity.TOP);
        Inc = (Button) form.findViewById(R.id.btn_Increment);
        Inc.setOnClickListener(this);

        btn_Clear = (Button) form.findViewById(R.id.btn_Clear);
        btn_Clear.setOnClickListener(this);

        btn_Back = (Button) form.findViewById(R.id.btn_Back);
        btn_Back.setOnClickListener(this);

        Dec = (Button) form.findViewById(R.id.btn_Dicrement);
        Dec.setOnClickListener(this);
        Title = (MyTextView)form.findViewById(R.id.Title);
        Title.setText(getArguments().getString("Nm"));
        OrderNo= getArguments().getString("OrderNo") ;
        ItemNo= getArguments().getString("ItemNo") ;


        tv_qty = (EditText)form.findViewById(R.id.tv_qty);
        tv_qty.setText(getArguments().getString("Qty"));
        tv_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tv_qty.getText().toString().equalsIgnoreCase("")){
                        ((Pos_Activity) getActivity()).UpdateQty("1");
                }else{
                    double AvailableQty =  GetStoreQty(ItemNo);
                 if( SToD( tv_qty.getText().toString()) >  (AvailableQty )){
                     new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                             .setTitleText("فاتورة المبيعات")
                             .setContentText("الكمية غير متوفرة")
                             .setCustomImage(R.drawable.error_new)
                             .show();
                   //    tv_qty.setText(SToD( AvailableQty+"")+"");
                     return;
                    }
                        ((Pos_Activity) getActivity()).UpdateQty(tv_qty.getText().toString());
                }
            }


        });

        getDialog().setTitle(getArguments().getString("Nm"));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        hideKeyboard(form);
        return form;
    }
    private Double GetStoreQty(String ItemNo) {
        Double Order_qty = 0.0;
        Double Res = 0.0;
        SqlHandler sqlHandler =new SqlHandler(getActivity()) ;

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


        query = "SELECT       (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  " +
                "    from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                " where   sih.Post = -1  and ui.item_no ='" + ItemNo + "'  and sih.OrderNo != '" +OrderNo+ "'";



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



        return  Res;



    }
    private Double SToD(String str) {
        str=str .replaceAll("[^\\d.]", "");
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
    public   void hideKeyboard(View v ) {

        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
    @Override
    public void onClick(View v) {

        if (tv_qty.getText().toString().equalsIgnoreCase("")){
            tv_qty.setText("1");
            return;
        }
        if(v==btn_Back){
            this.dismiss();
        }
        if(v==btn_Clear){
            tv_qty.setText("");
            tv_qty.requestFocus();
        }
        if (v==Inc){
            tv_qty.setText( (Double.parseDouble(tv_qty.getText().toString() )+1)   +"" );
          /*  ((Pos_Activity) getActivity()).UpdateQty(tv_qty.getText().toString() );*/
         if( Double.parseDouble( tv_qty.getText().toString() ) > 1 ) {
                Dec.setVisibility(View.VISIBLE);
            }

        }

        if(v==Dec){

            tv_qty.setText( (Double.parseDouble(tv_qty.getText().toString() )-1)   +"" );
            if( Double.parseDouble( tv_qty.getText().toString() ) < 1 ) {
                tv_qty.setText("1");
                Dec.setVisibility(View.INVISIBLE);
            }

           /* if(getArguments().getString("Scr").equalsIgnoreCase("Pos_Activity")) {
                      ((Pos_Activity) getActivity()).UpdateQty(tv_qty.getText().toString());
                  }*/
        }
  }
}


package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.assist.Cls_Cust_Last_Trans;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class PopShowCustDiscountDept extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel,acc,invoice,rec,order;
    Button back;
    SqlHandler sqlHandler;
    ListView Lst1 ;
    ArrayList<Cls_DeptDiscount> ItemsList1 ;

String   CustNo,CustNm;
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
       form =inflater.inflate(R.layout.pop_show_cust_last_trans,container,false);
        getDialog().setTitle("فئات خصم العميل ");
        ItemsList1 = new ArrayList<Cls_DeptDiscount>();

        back = (Button)form.findViewById(R.id.button42);
        Lst1 = (ListView)form.findViewById(R.id.Lst1);

        FillList1("");
        back.setOnClickListener(this);


       getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        return  form;
    }
private  void FillList1(String Gro_No){
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    ComInfo.ComNo = Integer.parseInt(DB.GetValue(getActivity(), "ComanyInfo", "CompanyID", "1=1"));


    CustNo = sharedPreferences.getString("CustNo", "");
    CustNm =sharedPreferences.getString("CustNm", "") ;


         sqlHandler = new SqlHandler(getActivity());
        String query ="";

          query = "  Select Distinct CustNo,From_Value,To_Value,Discount_Value,Notes FROM DeptDiscount " +
                  "  where  CustNo ='" +CustNo+"'";


    Cursor c1 = sqlHandler.selectQuery(query);
    Cls_DeptDiscount obj ;
    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            do {
                  obj = new Cls_DeptDiscount();
                 // obj.setCustNo(c1.getString(c1.getColumnIndex("Cust_No")));
                  obj.setCustNo(CustNm);
                  obj.setFrom_Value(c1.getString(c1.getColumnIndex("From_Value")));
                  obj.setTo_Value(c1.getString(c1.getColumnIndex("To_Value")));
                  obj.setDiscount_Value(c1.getString(c1.getColumnIndex("Discount_Value")));
                  obj.setNotes(c1.getString(c1.getColumnIndex("Notes")));


             ItemsList1.add(obj);

            } while (c1.moveToNext());

        }
        c1.close();
            }

    Cust_Discount_Dept_Adapter cls_unitItems_adapter = new Cust_Discount_Dept_Adapter(
            getActivity(), ItemsList1);

    Lst1.setAdapter(cls_unitItems_adapter);



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




package com.cds_jo.GalaxySalesApp;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.cds_jo.GalaxySalesApp.assist.Cls_Cust_Last_Trans;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Methdes.MyTextView;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopShowCustLastTrans extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel,acc,invoice,rec,order;
    Button back;
    SqlHandler sqlHandler;
    ListView Lst1,Lst2,Lst3 ;
    ArrayList<Cls_Cust_Last_Trans> ItemsList1 ;

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
        getDialog().setTitle("أخر حركات العميل");
        ItemsList1 = new ArrayList<Cls_Cust_Last_Trans>();

        back = (Button)form.findViewById(R.id.button42);
        Lst1 = (ListView)form.findViewById(R.id.Lst1);

        FillList1("");
        back.setOnClickListener(this);
      /*  Lst1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
*/




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

          query = "  Select Distinct Cust_No,amt,doctype,V_Type,date,DayCount,Paymethod,DocTypeDesc FROM CusLastTrans " +
                  "  where  Cust_No='" +CustNo+"'";


    Cursor c1 = sqlHandler.selectQuery(query);
    Cls_Cust_Last_Trans obj ;
    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            do {
                  obj = new Cls_Cust_Last_Trans();
                  obj.setCust_No(c1.getString(c1.getColumnIndex("Cust_No")));
                  obj.setCust_Nm(CustNm);
                  obj.setAmt(c1.getString(c1.getColumnIndex("amt")));
                  obj.setDoctype(c1.getString(c1.getColumnIndex("doctype")));
                  obj.setV_Type(c1.getString(c1.getColumnIndex("V_Type")));
                  obj.setDate(c1.getString(c1.getColumnIndex("date")));
                  obj.setDayCount(c1.getString(c1.getColumnIndex("DayCount")));
                  obj.setPaymethod(c1.getString(c1.getColumnIndex("Paymethod")));
                  obj.setDocTypeDesc(c1.getString(c1.getColumnIndex("DocTypeDesc")));

             ItemsList1.add(obj);

            } while (c1.moveToNext());

        }
        c1.close();
            }

    Cust_Last_Trnas_Adapter cls_unitItems_adapter = new Cust_Last_Trnas_Adapter(
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




package com.cds_jo.GalaxySalesApp.assist;

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

import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.Cust_Last_Trnas_Adapter;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by Hp on 07/02/2016.
 */

public class PopShowCode extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel,acc,invoice,rec,order;
    Button back;
    String UserID;
    SqlHandler sqlHandler;
    ListView Lst1,Lst2,Lst3 ;
    ArrayList<Cls_Code> ItemsList1 ;

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
       form =inflater.inflate(R.layout.pop_show_code,container,false);
        getDialog().setTitle("كود السقوفات");
        ItemsList1 = new ArrayList<Cls_Code>();

        back = (Button)form.findViewById(R.id.button42);
        Lst1 = (ListView)form.findViewById(R.id.Lst1);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        UserID = sharedPreferences.getString("UserID", "-1");
        FillList1("");
        back.setOnClickListener(this);





       getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        return  form;
    }
private  void FillList1(String Gro_No){
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    ComInfo.ComNo = Integer.parseInt(DB.GetValue(getActivity(), "ComanyInfo", "CompanyID", "1=1"));



         sqlHandler = new SqlHandler(getActivity());
        String query ="";

          query = "  Select Distinct Code, MaxBouns,MaxDiscount,CustomerId ,ItemId,AllBillMaterial,Note" +
                  " FROM CodeDefinition  where  ManId='" +UserID+"'";


    Cursor c1 = sqlHandler.selectQuery(query);
    Cls_Code obj ;
    if (c1 != null && c1.getCount() != 0) {
        if (c1.moveToFirst()) {
            do {
                  obj = new Cls_Code();
                  obj.setCode(c1.getString(c1.getColumnIndex("Code")));
                  obj.setMaxBouns(c1.getString(c1.getColumnIndex("MaxBouns")));
                  obj.setMaxDiscount(c1.getString(c1.getColumnIndex("MaxDiscount")));
                  obj.setCustomerId(c1.getString(c1.getColumnIndex("CustomerId")));
                  obj.setItemId(c1.getString(c1.getColumnIndex("ItemId")));
                  obj.setAllBillMaterial(c1.getString(c1.getColumnIndex("AllBillMaterial")));
                  obj.setNote(c1.getString(c1.getColumnIndex("Note")));

             ItemsList1.add(obj);

            } while (c1.moveToNext());

        }
        c1.close();
            }

    Code_Adapter cls_unitItems_adapter = new Code_Adapter(
            getActivity(), ItemsList1);

    Lst1.setAdapter(cls_unitItems_adapter);



}

    public void onClick(View v) {

         if (v == back) {
             this.dismiss();
        }



    }


}



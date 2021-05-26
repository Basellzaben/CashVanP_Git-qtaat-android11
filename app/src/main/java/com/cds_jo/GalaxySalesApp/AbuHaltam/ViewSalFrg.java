package com.cds_jo.GalaxySalesApp.AbuHaltam;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.GlobaleVar;
import com.cds_jo.GalaxySalesApp.Pos.PopSavePosInvoice;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSalFrg  extends DialogFragment {
    View form ;
    SqlHandler sqlHandler;

    ArrayList<GetDoneBarCode> list1;

    ListView lv;
    GetDoneBarCodeAdapter adapter;
    GetDoneBarCode cls_barCode;

    ProgressDialog progressdialog;


    /*  @Override
      public void onStart()
      {
          super.onStart();

          // safety check
          if (getDialog() == null)
              return;
          int dialogWidth =  WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
          int dialogHeight =  WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here
          getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

      }*/
    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form= inflater.inflate(R.layout.fragment_view_sal_frg, container, false);
        lv=(ListView) form.findViewById(R.id.lv);
        sqlHandler=new SqlHandler(getActivity());
        list1 = new ArrayList<>();
        FilterItems1();
lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cls_barCode =list1.get(position);
        String    query = "delete from Inv_Sal_Done where Item_No ='"+cls_barCode.getItemNote()+"' and sal ='"+cls_barCode.getBarCode()+"'" ;
        sqlHandler.executeQuery("update  Inv_Sal set status ='0' where sal ='"+cls_barCode.getBarCode()+"'");

        sqlHandler.executeQuery(query);
        list1.remove(position);
        adapter.notifyDataSetChanged();

    }
});
        return form;
    }



    public void FilterItems1( ) {
        String    query = "Select   *   from Inv_Sal_Done where Item_No ='"+getArguments().getString("Item_no")+"' and oreder_no ='"+getArguments().getString("orderno")+"'" ;

        //  "  where "+
        //  "  Item_No   like '%" + Filter + "%'  or Item_Name like  '%" + Filter + "%' ";



        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_barCode = new GetDoneBarCode();
                    cls_barCode.setItemNo(" ");
                    cls_barCode.setItemNote(c1.getString(c1
                            .getColumnIndex("Item_No")));
                    cls_barCode.setBarCode(c1.getString(c1
                            .getColumnIndex("Sal")));
                    cls_barCode.setType(" ");
                    cls_barCode.setDate(" ");


                    list1.add(cls_barCode);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        adapter = new GetDoneBarCodeAdapter(getActivity(), list1);
        lv.setAdapter(adapter);
        //rec_Item_id.setLayoutManager(new GridLayoutManager(this,4));
        //  rec_Item_id.setAdapter(myAdapter);



    }

}

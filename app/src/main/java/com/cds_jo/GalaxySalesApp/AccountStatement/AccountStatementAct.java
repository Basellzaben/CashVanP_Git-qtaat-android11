package com.cds_jo.GalaxySalesApp.AccountStatement;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountStatementAct extends AppCompatDialogFragment {
    ArrayList<cls_AccountStatement> TList;
    AccountStatementAdapter adapter;
    ListView lv;
    final Handler _handler = new Handler();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_account_statement, null);

        builder.setView(v)
                .setTitle("كشف حساب تفصيلي")

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        lv=(ListView)v.findViewById(R.id.k) ;
        getData();

        return builder.create();
    }

    private void getData() {
        TList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_CustReportAccountStatement("1");
                try {

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray Item_Name = js.getJSONArray("Item_Name");
                    JSONArray bill = js.getJSONArray("bill");
                    JSONArray A_Qty = js.getJSONArray("A_Qty");
                    JSONArray price = js.getJSONArray("price");
                    JSONArray Bonus = js.getJSONArray("Bonus");
                    JSONArray SumWithOutTax = js.getJSONArray("SumWithOutTax");
                    for (int i = 0; i < Item_Name.length(); i++) {
                        TList.add(new cls_AccountStatement(Item_Name.get(i).toString(), bill.get(i).toString(), A_Qty.get(i).toString(), price.get(i).toString(),Bonus.get(i).toString(),SumWithOutTax.get(i).toString()));
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            adapter = new AccountStatementAdapter(getActivity(), TList);
                            lv.setAdapter(adapter);
                        }
                    });


                    //  mo(TList);
                } catch (Exception e) {

                }


            }

        }).start();
    }
}



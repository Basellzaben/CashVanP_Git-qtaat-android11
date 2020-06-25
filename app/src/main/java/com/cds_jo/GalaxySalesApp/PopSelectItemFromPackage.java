
package com.cds_jo.GalaxySalesApp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;


import com.cds_jo.GalaxySalesApp.assist.Cls_Select_Item_From_Pakage;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;

import java.util.ArrayList;

import Methdes.MyTextView;


public class PopSelectItemFromPackage extends DialogFragment implements View.OnClickListener {
    View form;
    ImageButton add, cancel   ;
    Button back ,btn_Approved;
    SqlHandler sqlHandler;
    ListView Lstview;
    ArrayList<Cls_Select_Item_From_Pakage> ItemsList;
    String GroupNo = "";
    Button Add,Sub;
    MyTextView WindwoTitle, tv_total_item,tv_Factor,tv_Sum_Qty_item,tv_QtyWithFactor;
    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        //  int dialogWidth =410; // specify a value here
        //  int dialogHeight = 300; // specify a value here


        //  int dialogWidth = 420; // specify a value here
        //  int dialogHeight =WindowManager.LayoutParams.WRAP_CONTENT;
        //  getDialog().getWindow().setLayout(dialogWidth, dialogHeight);


    /*    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(lp);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(0);
*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
         getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        form = inflater.inflate(R.layout.select_item_from_pakge, container, false);

        GroupNo = getArguments().getString("GroupNo");
        getDialog().setTitle(GroupNo);
        ItemsList = new ArrayList<Cls_Select_Item_From_Pakage>();
        Lstview = (ListView) form.findViewById(R.id.Lstview);
        back = (Button) form.findViewById(R.id.button22);
        back.setOnClickListener(this);
        back.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Hacen Tunisia Lt.ttf"));



        getDialog().setCanceledOnTouchOutside(false);

        Lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });

            tv_total_item= (MyTextView) form.findViewById(R.id.tv_total_item);
            tv_Factor=(MyTextView) form.findViewById(R.id.tv_Factor);
            tv_Sum_Qty_item=(MyTextView) form.findViewById(R.id.tv_Sum_Qty_item);
            tv_QtyWithFactor=(MyTextView) form.findViewById(R.id.tv_QtyWithFactor);
            WindwoTitle=(MyTextView) form.findViewById(R.id.WindwoTitle);


        WindwoTitle.setText( GroupNo);
        tv_total_item.setText( getArguments().getString("TotalItem"));;
        tv_Factor.setText(getArguments().getString("ItemFactor"));;
        tv_Sum_Qty_item.setText(getArguments().getString("SumQty"));
        tv_QtyWithFactor.setText( (Integer.parseInt(tv_Factor.getText().toString())  *   Integer.parseInt(tv_Sum_Qty_item.getText().toString())       ) +"");
        sqlHandler = new SqlHandler(getActivity());
        FillList(GroupNo);


        return form;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed(){
                // dismiss();
                // activityReference.finish();
            }
        };
    }
    private void FillList(String Gro_No) {

       String OfferNm ="";
        String query = " Select distinct  oh.Offer_Name, u.UnitName , odc.Allaw_Repet,   i.Item_Name ,  odg.Item_No , odg.Unit_No , odg.QTY " +
                       " , odg.Unit_Rate from Offers_Dtl_Gifts  odg Left join Offers_Hdr oh   on  Offer_No =  odg.Trans_ID "+
                       " Left join invf i on i.Item_No =  odg.Item_No   " +
                       " left join Unites  u on u.Unitno = odg.Unit_No" +
                       " left join  Offers_Dtl_Cond odc on odc.Trans_ID =  odg.Trans_ID" +
                       " where odg.Trans_ID = '" + Gro_No + "'"; //"  where odc.Gro_Num  = '"+GroupNo+"'";


        Cursor c1 = sqlHandler.selectQuery(query);
        Cls_Select_Item_From_Pakage obj;

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    OfferNm = c1.getString(c1.getColumnIndex("Offer_Name")) ;
                    obj = new Cls_Select_Item_From_Pakage();
                    obj.setItemNo(c1.getString(c1.getColumnIndex("Item_No")));
                    obj.setItemNm(c1.getString(c1.getColumnIndex("Item_Name")));
                    obj.setMinQty( (Integer.parseInt(tv_Factor.getText().toString())   *     Integer.parseInt(    c1.getString(c1.getColumnIndex("QTY")) .toString()) )  +"");
                    obj.setFlg("");
                    obj.setUnitNm(c1.getString(c1.getColumnIndex("UnitName")));
                    obj.setUnitNo(c1.getString(c1.getColumnIndex("Unit_No")));
                    obj.setGiftQty( (Integer.parseInt(tv_Factor.getText().toString())   *     Integer.parseInt(    c1.getString(c1.getColumnIndex("QTY")) .toString()) )  +"");
                    obj.setTotalItems(tv_total_item.getText().toString());
                    obj.setSumQty(tv_QtyWithFactor.getText().toString());
                    obj.setGiftQty(c1.getString(c1.getColumnIndex("QTY")));
                    ItemsList.add(obj);
                } while (c1.moveToNext());

            }
            c1.close();
        }
        getDialog().setTitle(OfferNm);
        WindwoTitle.setText(Gro_No +" : " + OfferNm   );

        obj = new Cls_Select_Item_From_Pakage();
        obj.setItemNo("");
        obj.setItemNm("المجموع");
        obj.setMinQty("");
        obj.setFlg("-1");
        obj.setUnitNm("" );
        obj.setUnitNo("0");
        obj.setGiftQty("0");
        obj.setTotalItems("3");
        obj.setSumQty("3");
        ItemsList.add(obj);

        Item_Package_Items cls_unitItems_adapter = new Item_Package_Items(
                getActivity(), ItemsList);

        Lstview.setAdapter(cls_unitItems_adapter);


    }
    public void onClick(View v) {
        Integer Count = 0;
        Integer qty = 0;
        if (v == back) {

            for(int i = 0 ;i<(ItemsList.size()-1);i++){
                if(Integer.parseInt(ItemsList.get(i).getGiftQty())>0 ){
                    Count = Count + 1 ;
                    qty = qty + Integer.parseInt(ItemsList.get(i).getGiftQty());
                }
           }

            if (  Integer.parseInt(tv_total_item.getText().toString() )>0 &&     (Integer.parseInt(  Count+"") !=   Integer.parseInt(tv_total_item.getText().toString()))){
                ShowMsg( "يجب ان يكون عدد مواد الهدية :"+tv_total_item.getText().toString()  );
                return;
            }
            if ( Integer.parseInt( qty+"") !=   Integer.parseInt(tv_QtyWithFactor.getText().toString())){
                ShowMsg( "مجموع كمية الهدية  هو  :"+ tv_QtyWithFactor.getText().toString()   );
                return;
            }

            String q ="delete from TempGifts  ";
            sqlHandler.executeQuery(q);
            Long SqlRes  ;
            ContentValues cv;

            for (int i = 0; i < (ItemsList.size() - 1); i++) {
                if (Integer.parseInt(ItemsList.get(i).getGiftQty()) > 0) {

                    cv = new ContentValues();
                    cv.put("ItemNo", ItemsList.get(i).getItemNo());
                    cv.put("OfferNo", "GroupNo");
                    cv.put("Qty", ItemsList.get(i).getGiftQty());
                    cv.put("UnitNo", "");
                    SqlRes = sqlHandler.Insert("TempGifts", null, cv);
                }
            }

            if (getArguments().getString("Scr") == "po") {
                ((OrdersItems) getActivity()).AppledOfferType33(  GroupNo ,tv_Factor.getText().toString() ,"1");
            }


            this.dismiss();
        }
    }


    private   void ShowMsg(String msg){

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("تحدد مواد وكمية الهدية");
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.error_new);
        alertDialog.setButton("رجوع", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        alertDialog.show();

    }



}



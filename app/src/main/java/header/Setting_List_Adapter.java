package header;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.CusfCard;
import com.cds_jo.GalaxySalesApp.CustomerQty;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.DoctorReportActivity;
import com.cds_jo.GalaxySalesApp.EditeTransActivity;

import com.cds_jo.GalaxySalesApp.GetPermession;

import com.cds_jo.GalaxySalesApp.ManBalanceQtyActivity;
import com.cds_jo.GalaxySalesApp.ManLocationsReport;
import com.cds_jo.GalaxySalesApp.ManSummeryActivity;

import com.cds_jo.GalaxySalesApp.NewLoginActivity;
import com.cds_jo.GalaxySalesApp.NotificationActivity;
import com.cds_jo.GalaxySalesApp.PopShowCustLastTrans;
import com.cds_jo.GalaxySalesApp.PopShowOffers;

import com.cds_jo.GalaxySalesApp.QuestneerActivity;
import com.cds_jo.GalaxySalesApp.R;

import com.cds_jo.GalaxySalesApp.ScheduleManActivity;
import com.cds_jo.GalaxySalesApp.TransQtyReportActivity;
import com.cds_jo.GalaxySalesApp.TransferQty;
import com.cds_jo.GalaxySalesApp.UpdateDataToMobileActivity;
import com.cds_jo.GalaxySalesApp.VisitImges;

import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;
import com.cds_jo.GalaxySalesApp.imgtest;

import java.util.ArrayList;

import Methdes.MethodToUse;
import Methdes.MyTextView;
import port.bluetooth.BluetoothConnectMenu;


public class Setting_List_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Main_List_Itme> myList;
    private LayoutInflater layoutInflater;
    private Typeface typeface;

    public Setting_List_Adapter(Context context, ArrayList<Main_List_Itme> myList) {
        this.context = context;
        this.myList = myList;
        layoutInflater = LayoutInflater.from(context);
        typeface = MethodToUse.SetTFace(context);

    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.setting_item1, parent, false);
        MyTextView Title = (MyTextView) convertView.findViewById(R.id.textView69);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView6);
        LinearLayout RR = (LinearLayout) convertView.findViewById(R.id.RR_Set);
        Title.setText(myList.get(position).getTitle());
        img.setImageResource(myList.get(position).getImg());
        // Title.setTypeface(typeface);

        // Title.setTypeface(Typeface.createFromAsset(context.getAssets(), "Hacen Tunisia Lt.ttf"));


        RR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,myList.get(position).getTitle(),Toast.LENGTH_LONG).show();
                // Toast.makeText(context,position+"",Toast.LENGTH_LONG).show();
                GetPermession obj = new GetPermession();
                //Toast.makeText(context.getApplicationContext(), position +"", Toast.LENGTH_SHORT).show();


                if (position == 0) {
                  //  Intent intent = new Intent(context.getApplicationContext(), imgtest.class);
                  Intent intent = new Intent(context.getApplicationContext(), CusfCard.class);
                    // Intent intent = new Intent(context.getApplicationContext(), RR3Printer.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 1) {


                    //       Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                    Intent intent = new Intent(context.getApplicationContext(), TransferQty.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 2) {


                        /*Intent intent = new Intent(context.getApplicationContext(), ItemGalleryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity)context).finish();*/

                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "po");
                    bundle.putString("CatNo", "");
                    FragmentManager Manager = ((Activity) context).getFragmentManager();
                    PopShowOffers popShowOffers = new PopShowOffers();
                    popShowOffers.setArguments(bundle);
                    popShowOffers.show(Manager, null);


                } else if (position == 3)

                {
                    Bundle bundle = new Bundle();
                    bundle.putString("Scr", "po");
                    bundle.putString("CatNo", "");
                    FragmentManager Manager = ((Activity) context).getFragmentManager();
                    PopShowCustLastTrans popShowOffers = new PopShowCustLastTrans();
                    popShowOffers.setArguments(bundle);
                    popShowOffers.show(Manager, null);
                } else if (position == 4) {
                    try {
                        final String pass = DB.GetValue(context.getApplicationContext(), "Tab_Password", "Password", "PassNo = 2");
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle(DB.GetValue(context.getApplicationContext(), "Tab_Password", "PassDesc", "PassNo = 2"));
                        alertDialog.setMessage("الرجاء ادخال كلمة المرور الخاصة بفتح هذه الشاشة");

                        final EditText input = new EditText(context.getApplicationContext());
                        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        input.setTransformationMethod(new PasswordTransformationMethod());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);
                        alertDialog.setIcon(R.drawable.key);

                        alertDialog.setPositiveButton("موافق",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String password = input.getText().toString();

                                        if (pass.equals(password)) {
                                            Intent intent = new Intent(context.getApplicationContext(), EditeTransActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            context.startActivity(intent);
                                            ((Activity) context).finish();

                                        } else {
                                            Toast.makeText(context.getApplicationContext(),
                                                    "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                        alertDialog.setNegativeButton("لا",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();
                    } catch (Exception ex) {
                        Toast.makeText(context.getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 5) {
                    Intent intent = new Intent(context.getApplicationContext(), VisitImges.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else if (position == 6) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Login", "No");
                    editor.commit();

                    Intent intent = new Intent(context.getApplicationContext(), NewLoginActivity.class);
                    // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                } else if (position == 42) {


                    Intent intent = new Intent(context.getApplicationContext(), OrdersItems.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 5) {


                    Intent intent = new Intent(context.getApplicationContext(), TransQtyReportActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 6) {
                    Intent intent = new Intent(context.getApplicationContext(), UpdateDataToMobileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();

                } else if (position == 7) {

                    Intent intent = new Intent(context.getApplicationContext(), ScheduleManActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 8) {


//
                    Intent intent = new Intent(context.getApplicationContext(), CustomerQty.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 100) {

                    Intent intent = new Intent(context.getApplicationContext(), NotificationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 9) {

                    Intent intent = new Intent(context.getApplicationContext(), QuestneerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 10) {


                    Intent intent = new Intent(context.getApplicationContext(), BluetoothConnectMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 11) {

                    Intent intent = new Intent(context.getApplicationContext(), EditeTransActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 12) {

                    Intent intent = new Intent(context.getApplicationContext(), ManBalanceQtyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 13) {


                    Intent intent = new Intent(context.getApplicationContext(), ManSummeryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 14) {

                    Intent intent = new Intent(context.getApplicationContext(), VisitImges.class);
                    // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 15) {

                    Intent intent = new Intent(context.getApplicationContext(), EditeTransActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();

                } else if (position == 16) {


                    Intent intent = new Intent(context.getApplicationContext(), ManBalanceQtyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 17) {


                    Intent intent = new Intent(context.getApplicationContext(), ManSummeryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();

//                        Intent k = new Intent(v.getContext(),ManSummeryActivity.class);
//                        context.startActivity(k);


                } else if (position == 19) {

//                        Intent k = new Intent(v.getContext(),DoctorReportActivity.class);
//                        context.startActivity(k);

                    Intent intent = new Intent(context.getApplicationContext(), VisitImges.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                } else if (position == 20) {

//                        Intent k = new Intent(v.getContext(),DoctorReportActivity.class);
//                        context.startActivity(k);

                    Intent intent = new Intent(context.getApplicationContext(), DoctorReportActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();


                }
            }
        });
        return convertView;
    }
}

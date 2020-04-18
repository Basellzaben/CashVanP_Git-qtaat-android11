package com.cds_jo.GalaxySalesApp;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cds_jo.GalaxySalesApp.ManLocation.AutoPostLocation;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.ESCPSample3;

import com.cds_jo.GalaxySalesApp.assist.Logtrans.InsertLogTrans;
import com.cds_jo.GalaxySalesApp.assist.OrdersItems;
import com.cds_jo.GalaxySalesApp.assist.Sale_InvoiceActivity;
import com.cds_jo.GalaxySalesApp.assist.Sale_ReturnActivity;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.zebra.zq110.ZQ110;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Methdes.MyTextView;
import header.Header_Frag;

public class MainActivity extends AppCompatActivity implements LocationListener {
    ImageView imglogo;
    RelativeLayout scanBtn;
    LocationManager locationmanager;
    private TextView contentTxt;
    private ESCPOSPrinter posPtr;
    ESCPSample3 obj_print = new ESCPSample3();
    int dayOfWeek;
    SqlHandler sqlHandler;
    TextView TrDate, et_Day, et_StartTime, et_EndTime;
    RelativeLayout EndRound, StartRound;
    String Week_Num = "1";
    ArrayList<Cls_SaleManDailyRound> RoundList;
    public ProgressDialog loadingdialog;
    static ZQ110 mZQ110;
    boolean isGPSEnabled = false;
    LocationManager locationManager;
    String OrderNo = "";
    String Msg = "";
    String UserID;
    String q;
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    MyTextView tv_VisitWeekNm;
    EditText tv_Note;
    TextView tv_x , tv_y, tv_Loc ;
    // Minimum time fluctuation for next update (in milliseconds)
    private static final long TIME = 30000;
    // Minimum distance fluctuation for next update (in meters)
    private static final long DISTANCE = 20;

    // Declaring a Location Manager
    protected LocationManager mLocationManager;
    GetPermession UserPermission;
   String SCR_NO="11001";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_main);
        InsertLogTrans obj=new InsertLogTrans(MainActivity.this,SCR_NO , SCR_ACTIONS.open.getValue(),"","","");
        hideSoftKeyboard();
          tv_x = (TextView) findViewById(R.id.tv_x);
          tv_y = (TextView) findViewById(R.id.tv_y);
          tv_Loc = (TextView) findViewById(R.id.tv_Loc);
        ComInfo.ComNo = Integer.parseInt(DB.GetValue(this, "ComanyInfo", "CompanyID", "1=1").replaceAll("[^\\d.]", ""));
        RoundList = new ArrayList<Cls_SaleManDailyRound>();
        RoundList.clear();
        UserPermission = new GetPermession();
        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria cri = new Criteria();
        String provider = locationmanager.getBestProvider(cri, false);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        tv_VisitWeekNm = (MyTextView) findViewById(R.id.tv_VisitWeekNm);
        TrDate = (TextView) findViewById(R.id.et_Date);
        TrDate.setText(currentDateandTime);
        tv_Note=(EditText) findViewById(R.id.tv_Note);

        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        et_Day = (TextView) findViewById(R.id.et_Day);
        et_Day.setText(GetDayName(dayOfWeek));


        contentTxt = (TextView) findViewById(R.id.scan_content);
        scanBtn = (RelativeLayout) findViewById(R.id.scan_button);

        et_StartTime = (TextView) findViewById(R.id.et_StartTime);
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        et_StartTime.setText(StringTime);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        try {
            Getlocation();
            ShowRecord();
        } catch (Exception ex) {
        }

        Week_Num = DB.GetValue(this, "ComanyInfo", "VisitWeekNo", "1=1");
        if (Week_Num.equalsIgnoreCase("1")) {
            tv_VisitWeekNm.setText("الاسبوع الأول");
        } else {
            tv_VisitWeekNm.setText("الأسبوع الثاني");
        }
        // GetlocationNew();
        Fragment frag = new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();
        //Toast.makeText(this, sharedPreferences.getString("UserID", ""),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    public void GetStreetName() {
        try {
            final TextView tv_x = (TextView) findViewById(R.id.tv_x);
            final TextView tv_y = (TextView) findViewById(R.id.tv_y);

            final TextView tv_Loc = (TextView) findViewById(R.id.tv_Loc);
            Geocoder myLocation = new Geocoder(this, Locale.getDefault());
            List<Address> myList = null;
            try {
                myList = myLocation.getFromLocation(Double.parseDouble(tv_x.getText().toString()), Double.parseDouble(tv_y.getText().toString()), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address1 = (Address) myList.get(0);
            String addressStr = "";
            addressStr += address1.getAddressLine(0) + ", ";
            addressStr += address1.getAddressLine(1) + ", ";
            addressStr += address1.getAddressLine(2);
            tv_Loc.setText(addressStr.replace("null", "").replace(",", ""));
        } catch (Exception ex) {
         //   Toast.makeText(this, ex.getMessage().toString() + "  GetStreetName", Toast.LENGTH_SHORT).show();
        }
    }

    public void GetlocationNew() {
  try {
      String result;

      String address = "";
      GetLocation mGPSService = new GetLocation();
      Location l = mGPSService.CurrentLocation(MainActivity.this);

      final TextView tv_x = (TextView) findViewById(R.id.tv_x);
      final TextView tv_y = (TextView) findViewById(R.id.tv_y);

      final TextView tv_Loc = (TextView) findViewById(R.id.tv_Loc);


      tv_x.setText("");
      tv_y.setText("");
      tv_Loc.setText("");


      double latitude = l.getLatitude();
      double longitude = l.getLongitude();

      tv_x.setText(String.valueOf(latitude));
      tv_y.setText(String.valueOf(longitude));

      Toast.makeText(this, tv_x.getText().toString() + "," + tv_y.getText().toString(), Toast.LENGTH_SHORT).show();
      Cls_HttpGet_Location task = new Cls_HttpGet_Location();
      try {
          result = task.execute(tv_x.getText().toString(), tv_y.getText().toString()).get();
          tv_Loc.setText(result);
      } catch (Exception ex) {
          tv_Loc.setText("الموقع غير متوفر");
      }

      GetStreetName();
  }catch (Exception ex){

  }
    }

    public void Getlocation() {


        String address = "";
        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();


        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);

        final TextView tv_CustName = (TextView) findViewById(R.id.tv_CustName);
        final TextView tv_Acc = (TextView) findViewById(R.id.tv_Acc);
        final TextView tv_Loc = (TextView) findViewById(R.id.tv_Loc);
        final TextView tv_Address = (TextView) findViewById(R.id.tv_Loc);
        contentTxt = (TextView) findViewById(R.id.scan_content);
        // mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            // Toast.makeText(this, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();

            address = mGPSService.getLocationAddress();

           /* Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(latitude,longitude, 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    address = addresses.get(0).getAddressLine(1);
                }
            }
            catch
                    (IOException e) {
                e.printStackTrace();
            }*/

            int precision = (int) Math.pow(10, 6);
            //tv_x.setText(String.format("%.4f", latitude, Locale.US));

            try {
           /* tv_x.setText(String.valueOf(latitude).substring(0, String.valueOf(latitude).indexOf(".") + 7));
            tv_y.setText(String.valueOf(longitude).substring(0, String.valueOf(longitude).indexOf(".") + 7));
*/
                //         tv_x.setText(String.valueOf(latitude));
//                tv_y.setText(String.valueOf(longitude));


            } catch (Exception ex) {
                tv_x.setText("0.0");
                tv_y.setText("0.0");
            }
            tv_x.setText(String.valueOf(latitude));
            tv_y.setText(String.valueOf(longitude));
            tv_Loc.setText(address);

        }
        GetStreetName();
        // mGPSService.closeGPS();


 /*       String provider;
        Criteria criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        String mprovider="";
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 1500, 1, this);


            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
            tv_y.setText("114");

            // Setting Current Latitude
            // tv_x.setText("Latitude:" + location.getLatitude() );

        }*/
    }

    @Override
    public void onLocationChanged(Location location) {


        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);


        // Getting reference to TextView tv_longitude
        // TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude);

        // Getting reference to TextView tv_latitude
        //TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude);

        // Setting Current Longitude
        tv_y.setText("Longitude:" + location.getLongitude());

        // Setting Current Latitude
        tv_x.setText("Latitude:" + location.getLatitude());
    }

    public void btn_SearchCust_dis(View v) {

        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Gps");
        FragmentManager Manager = getFragmentManager();
        Select_Customer_Dis obj = new Select_Customer_Dis();
        obj.setArguments(bundle);
        obj.show(Manager, null);


    }

    public void btn_SearchCust(View v) {
      if (ComInfo.ComNo == Companies.beutyLine.getValue() || ComInfo.ComNo == 1){
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Gps");
        FragmentManager Manager = getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }else if (ComInfo.ComNo > 1) {


            Button create, show, setting;

            String Man = DB.GetValue(MainActivity.this, "Tab_Password", "ManNo", "PassNo = 3 AND ManNo ='" + UserID + "'");

            final String pass = DB.GetValue(MainActivity.this, "Tab_Password", "Password", "PassNo = 3  and ManNo='" + Man + "'");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle(DB.GetValue(MainActivity.this, "Tab_Password", "PassDesc", "PassNo = 3 and ManNo='" + Man + "'"));
            alertDialog.setMessage("ادخل رمز التحقق");

            final EditText input = new EditText(MainActivity.this);
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

                            if (pass.equalsIgnoreCase(password)) {
                                //  Toast.makeText(getApplicationContext(), "رمز التحقق صحيح", Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putString("Scr", "Gps");
                                FragmentManager Manager = getFragmentManager();
                                Select_Customer obj = new Select_Customer();
                                obj.setArguments(bundle);
                                obj.show(Manager, null);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "رمز التحقق غير صحيح", Toast.LENGTH_SHORT).show();
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


        } else {

            if (!UserPermission.CheckAction(this, "30023", SCR_ACTIONS.open.getValue())) {
                com.cds_jo.GalaxySalesApp.ViewDialog alert = new com.cds_jo.GalaxySalesApp.ViewDialog();
                alert.showDialog(MainActivity.this, "نأسف أنت لا تملك الصلاحية  ", "البحث عن العملاء");
                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "Gps");
                FragmentManager Manager = getFragmentManager();
                Select_Customer obj = new Select_Customer();
                obj.setArguments(bundle);
                obj.show(Manager, null);
            }


        }


    }


    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.RESULT_HIDDEN);
        }
    }

    static int id = 1;
    MsgNotification noti = new MsgNotification();

    public void btn_StartRound(View view) {

        TextView CustNo = (TextView) findViewById(R.id.tv_Acc);
        if (CustNo.getText().toString().length() == 0) {
            CustNo.setError("required!");
            CustNo.requestFocus();
            return;
        }
        OrderNo = GetMaxPONo();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler = new SqlHandler(this);

        TextView CustNm = (TextView) findViewById(R.id.tv_CustName);
        TextView Custadd = (TextView) findViewById(R.id.tv_Loc);

        TrDate = (TextView) findViewById(R.id.et_Date);
        et_StartTime = (TextView) findViewById(R.id.et_StartTime);

        ContentValues cv = new ContentValues();
        cv.put("CusNo", CustNo.getText().toString());
        cv.put("ManNo", sharedPreferences.getString("UserID", ""));
        cv.put("DayNum", String.valueOf(dayOfWeek));
        cv.put("Tr_Data", TrDate.getText().toString());
        cv.put("Start_Time", et_StartTime.getText().toString());
        cv.put("Closed", "0");
        cv.put("Posted", "-1");
        cv.put("OrderNo", OrderNo);
        cv.put("Note", tv_Note.getText().toString());
        cv.put("X_Lat", tv_x.getText().toString());
        cv.put("Y_Long", tv_y.getText().toString());
        cv.put("Loct", tv_Loc.getText().toString());

        final String CusNm = CustNm.getText().toString();
        final String CusNo = CustNo.getText().toString();
        long i;
        i = sqlHandler.Insert("SaleManRounds", null, cv);
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();


        alertDialog.setTitle("المجرة الدولية");
        if (i > 0) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CustNo", CustNo.getText().toString());
            editor.putString("CustNm", CustNm.getText().toString());
            editor.putString("CustAdd", Custadd.getText().toString());
            editor.putString("PayCount", "0");
            editor.putString("InvCount", "0");
            editor.putString("V_OrderNo", OrderNo);
            editor.commit();
            InsertLogTrans obj=new InsertLogTrans(MainActivity.this,SCR_NO , SCR_ACTIONS.StartVisit.getValue(),OrderNo, CustNo.getText().toString(),"");
            alertDialog.setMessage("عملية بداية الجولة تمت بنجاح، رقم الزيارة :" + OrderNo);
            alertDialog.setIcon(R.drawable.tick);
            StartRound.setVisibility(View.INVISIBLE);
            StartRound = (RelativeLayout) findViewById(R.id.btnStartRound);
            UpDateMaxOrderNo();
            /*if (ComInfo.ComNo != 4) {
                stopService(new Intent(MainActivity.this, AutoPostTrans.class));
            }*/
           /* if(ComInfo.ComNo==4) {
                stopService(new Intent(MainActivity.this,AutoPostLocation.class));
            }*/

            stopService(new Intent(MainActivity.this,AutoPostLocation.class));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String currentDateandTime = sdf.format(new Date());


            String Amt ;
            Msg = DB.GetValue(MainActivity.this, "CustomersMsg", "msg", "  SaleManFlg ='2' and  Cusno=" + CustNo.getText().toString()).toString();
            //  Tr_date custNo Amt orderDate

          //  Amt=DB.GetValue(MainActivity.this, "InvoicePaymentSchedule", "sum( cast( Amt as float))", "  Tr_date ='"+currentDateandTime +"' and  custNo='" + CustNo.getText().toString()+"'").toString();
//sum( cast( Amt as float))  AS no

            String query = "SELECT   ifnull(Amt,'0')  AS no  , ifnull(New_Amt,'0') as New_Amt " +
                    "  ,ifnull(SupervisorNutes,' ') as SupervisorNutes ,ifnull(New_Tr_date,' ') as New_Tr_date " +
                    " FROM InvoicePaymentSchedule Where ( Tr_date ='"+currentDateandTime +"' or New_Tr_date ='"+ currentDateandTime +"' )and  custNo='" + CustNo.getText().toString()+"'";
            Cursor c1 = sqlHandler.selectQuery(query);


            if (c1 != null && c1.getCount() != 0) {
                c1.moveToFirst();
                if (Msg.toString().length() > 2) {
                    Msg = Msg +"\r\n"  +"  يوجد دفعات مستحقة "+"القيمة الأصلية" +":"+     c1.getString(c1.getColumnIndex("no"));
                    Msg=Msg   +"\r\n"  + "تعديل القيمة من المشرف " +":"+     c1.getString(c1.getColumnIndex("New_Amt"));
                    Msg=Msg   +"\r\n"  + "تعديل التاريخ من المشرف " +":"+     c1.getString(c1.getColumnIndex("New_Tr_date"));
                    Msg=Msg   +"\r\n"  + "ملاحظات  المشرف " +":"+     c1.getString(c1.getColumnIndex("SupervisorNutes"));
                }else {
                    Msg =  "  يوجد دفعات مستحقة "+"القيمة" +":"+ c1.getString(c1.getColumnIndex("no"));
                    Msg=Msg   +"\r\n"  + "تعديل القيمة من المشرف " +":"+     c1.getString(c1.getColumnIndex("New_Amt"));
                    Msg=Msg   +"\r\n"  + "تعديل التاريخ من المشرف " +":"+     c1.getString(c1.getColumnIndex("New_Tr_date"));
                    Msg=Msg   +"\r\n"  + "ملاحظات  المشرف " +":"+     c1.getString(c1.getColumnIndex("SupervisorNutes"));
                }
            }





        } else {

            alertDialog.setMessage("عملية  الحفظ لم تتم ");
            alertDialog.setIcon(R.drawable.delete);
        }


        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (Msg.toString().length() > 3) {
                    noti.notify(MainActivity.this, "\r\n" + Msg, " الرجاء تبليغ العميل بالملاحظات التالية", CusNm, id);
                    id++;
                }


                if (ComInfo.ComNo != Companies.Arabian.getValue()) {
                    Bundle bundle = new Bundle();
                    FragmentManager Manager = getFragmentManager();
                    PopSmallMenue obj = new PopSmallMenue();
                    bundle.putString("Msg", CusNo + "   " + CusNm);
                    obj.setArguments(bundle);
                    obj.show(Manager, null);
                }
            }
        });


        alertDialog.show();


    }

    private void UpDateMaxOrderNo() {

        String query = "SELECT   Distinct  COALESCE(MAX( cast(OrderNo as integer)), 0)   AS no " +
                " FROM SaleManRounds";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        //max = (intToString(Integer.valueOf(max), 7));

        query = "Update OrdersSitting SET Visits ='" + max + "'";
        sqlHandler.executeQuery(query);

        /*SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("m4", max);
        editor.commit();*/
    }

    public void btn_EndRound(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (ComInfo.ComNo == Companies.beutyLine.getValue()) {

            q = "Select * from VisitImagesHdr where V_OrderNo='" + sharedPreferences.getString("V_OrderNo", "0") + "'";
            Cursor c = sqlHandler.selectQuery(q);
            if (c != null && c.getCount() > 0) {
                c.close();
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle("المجرة الدولية");
                alertDialog.setMessage("لا يمكن إنهاء الجولة دون اخذ صور");
                alertDialog.setIcon(R.drawable.error_new);
                alertDialog.setButton("موافق ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
              //  alertDialog.show();
              //  return;
            }
        }

        sqlHandler = new SqlHandler(this);
        TextView CustNo = (TextView) findViewById(R.id.tv_Acc);
        TextView CustNm = (TextView) findViewById(R.id.tv_CustName);
        TextView Custadd = (TextView) findViewById(R.id.tv_Loc);
        TrDate = (TextView) findViewById(R.id.et_Date);
        et_EndTime = (TextView) findViewById(R.id.et_EndTime);
        tv_Note=(EditText) findViewById(R.id.tv_Note);

        ContentValues cv = new ContentValues();

        cv.put("Tr_Data", TrDate.getText().toString());
        cv.put("End_Time", et_EndTime.getText().toString());
        cv.put("Closed", "1");
        cv.put("Note", tv_Note.getText().toString());


        long i;
        i = sqlHandler.Update("SaleManRounds", cv, "Closed =0");
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();


        alertDialog.setTitle("المجرة الدولية");
        if (i > 0) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CustNo", "");
            editor.putString("CustNm", "");
            editor.putString("CustAdd", "");
            editor.putString("V_OrderNo", "-1");
            editor.putString("Note", "");
            editor.commit();

            alertDialog.setMessage("نهاية الجولة تمت بنجاح");
            alertDialog.setIcon(R.drawable.tick);
            noti.cancel(this);
            InsertLogTrans obj=new InsertLogTrans(MainActivity.this,SCR_NO , SCR_ACTIONS.EndVisit.getValue(),OrderNo, CustNo.getText().toString(),"");

            DoNew();
            SharManVisits();
            stopService(new Intent(MainActivity.this, AutoPostLocation.class));
            startService(new Intent(MainActivity.this, AutoPostLocation.class));
        } else {

            alertDialog.setMessage("عملية  الحفظ لم تتم ");
            alertDialog.setIcon(R.drawable.error_new);
        }


        alertDialog.setButton("موافق ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // if(ComInfo.ComNo==4) {
                   /* stopService(new Intent(MainActivity.this, AutoPostLocation.class));
                    startService(new Intent(MainActivity.this, AutoPostLocation.class));*/
                //}
                // stopService(new Intent(MainActivity.this, AutoPostTrans.class));


               /* if (ComInfo.ComNo != 4) {
                    startService(new Intent(MainActivity.this, AutoPostTrans.class));
                }*/
            }
        });


        alertDialog.show();


    }
    public void SharManVisits() {

        //sqlHandler=new SqlHandler(this);

        final Handler _handler = new Handler();
        String query = "select  distinct no,ManNo, CusNo, DayNum ,Tr_Data ,Start_Time,End_Time, Duration,OrderNo " +
                "  ,Note,  X_Lat,Y_Long,Loct from SaleManRounds   where Posted = -1";
        Cursor c1 = sqlHandler.selectQuery(query);
        ArrayList<Cls_SaleManDailyRound> RoundList;
        RoundList = new ArrayList<Cls_SaleManDailyRound>();
        RoundList.clear();

        //query = " delete from   SaleManRounds   ";
        // sqlHandler.executeQuery(query);
        Cls_SaleManDailyRound cls_saleManDailyRound;

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_saleManDailyRound = new Cls_SaleManDailyRound();
                    cls_saleManDailyRound.setNo(c1.getString(c1
                            .getColumnIndex("no")));
                    cls_saleManDailyRound.setManNo(c1.getString(c1
                            .getColumnIndex("ManNo")));
                    cls_saleManDailyRound.setCusNo(c1.getString(c1
                            .getColumnIndex("CusNo")));
                    cls_saleManDailyRound.setDayNum(c1.getString(c1
                            .getColumnIndex("DayNum")));
                    cls_saleManDailyRound.setTr_Data(c1.getString(c1
                            .getColumnIndex("Tr_Data")));
                    cls_saleManDailyRound.setStart_Time(c1.getString(c1
                            .getColumnIndex("Start_Time")));
                    cls_saleManDailyRound.setEnd_Time(c1.getString(c1
                            .getColumnIndex("End_Time")));

                    cls_saleManDailyRound.setDuration(c1.getString(c1
                            .getColumnIndex("Duration")));

                    cls_saleManDailyRound.setOrderNo(c1.getString(c1
                            .getColumnIndex("OrderNo")));

                    cls_saleManDailyRound.setNote(c1.getString(c1
                            .getColumnIndex("Note")));


                    cls_saleManDailyRound.setX_Lat(c1.getString(c1
                            .getColumnIndex("X_Lat")));


                    cls_saleManDailyRound.setY_Long(c1.getString(c1
                            .getColumnIndex("Y_Long")));


                    cls_saleManDailyRound.setLoct(c1.getString(c1
                            .getColumnIndex("Loct")));



                    RoundList.add(cls_saleManDailyRound);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(RoundList);

        Calendar c = Calendar.getInstance();
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        //  final Handler _handler = new Handler();


        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(MainActivity.this);
                ws.SaveManVisits(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  SaleManRounds  set Posted=1  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {

                            }
                        });

                        query = " delete from   SaleManRounds   where Posted =1 and  DayNum < " + dayOfWeek;
                        sqlHandler.executeQuery(query);
                    }
                } catch (final Exception e) {
                }
            }
        }).start();
    }

    public void Set_Cust(String No, String Nm) {
        TextView CustNm = (TextView) findViewById(R.id.tv_CustName);
        TextView acc = (TextView) findViewById(R.id.tv_Acc);
        acc.setText(No);
        CustNm.setText(Nm);
        acc.setError(null);
        HidKeybad();
    }
    private  void HidKeybad(){
        try{
            if (this.getCurrentFocus() != null) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }catch (Exception ex){}
    }
    public void GetCustomer() {

        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);

        final TextView tv_CustName = (TextView) findViewById(R.id.tv_CustName);
        final TextView tv_Acc = (TextView) findViewById(R.id.tv_Acc);
        final TextView tv_Loc = (TextView) findViewById(R.id.tv_Loc);
        final TextView tv_Address = (TextView) findViewById(R.id.tv_Loc);

        String q = "";

        if (Week_Num.equalsIgnoreCase("1")) {

            if (dayOfWeek == 7)
                q = " sat = 1 ";

            if (dayOfWeek == 1)
                q = " sun = 1 ";

            if (dayOfWeek == 2)
                q = " mon = 1 ";


            if (dayOfWeek == 3)
                q = " tues = 1 ";

            if (dayOfWeek == 4)
                q = " wens = 1 ";

            if (dayOfWeek == 5)
                q = " thurs = 1 ";
        }
        if (Week_Num.equalsIgnoreCase("2")) {
            if (dayOfWeek == 7)
                q = " sat1 = 1 ";

            if (dayOfWeek == 1)
                q = " sun1 = 1 ";

            if (dayOfWeek == 2)
                q = " mon1 = 1 ";


            if (dayOfWeek == 3)
                q = " tues1 = 1 ";

            if (dayOfWeek == 4)
                q = " wens1 = 1 ";

            if (dayOfWeek == 5)
                q = " thurs1 = 1 ";

        }


        // Toast.makeText(this,contentTxt.getText().toString().substring(1,6),Toast.LENGTH_SHORT).show();
        q = "Select   c.no,c.name ,c.Latitude,c.Longitude,c.Address,c.barCode, c.SMan, c.State from Customers  c" +
                "     Where (  c.barCode='-1' or    c.barCode = '" + contentTxt.getText().toString() + "' ) And ( c.Latitude = '-1' or   c.Latitude = '" + tv_x.getText().toString() + "' )" +
                " And (  c.Longitude = '-1' or   c.Longitude = '" + tv_y.getText().toString() + "') And  c." + q;

        SqlHandler sqlHandler = new SqlHandler(this);

        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                tv_CustName.setText(c1.getString(c1.getColumnIndex("name")));
                tv_Acc.setText(c1.getString(c1.getColumnIndex("no")));

                tv_Acc.setError(null);
            }
            c1.close();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    MainActivity.this).create();
            alertDialog.setTitle("خط بيع مندوب");

            alertDialog.setMessage("لا يوجد بيانات عميل لهذا المندوب");
            alertDialog.setIcon(R.drawable.delete);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            // progressDialog.incrementProgressBy(1);
        }

    }

    public String GetDayName(Integer Day) {


        String DayNm = "";
        if (Day == 1) DayNm = "الاحد";
        else if (Day == 2) DayNm = "الاثنين";
        else if (Day == 3) DayNm = "الثلاثاء";
        else if (Day == 4) DayNm = "الاربعاء";
        else if (Day == 5) DayNm = "الخميس";
        else if (Day == 6) DayNm = "الجمعة";
        else if (Day == 7) DayNm = "السبت";


        return DayNm;

    }

    public void BtnScan(View view) {
        if (view.getId() == R.id.scan_button) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
         /*   String address = "";
            GPSService mGPSService = new GPSService(view.getContext());
            mGPSService.getLocation();*/

        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();


            contentTxt = (TextView) findViewById(R.id.scan_content);


            contentTxt.setText("");


            if (scanningResult.getContents() != null) {
                contentTxt.setText(scanContent.toString());

                GetCustomer();
            }
            //contentTxt.setText( "6253803400018");
            //  GetCustomer();


        }
        // GetCustomer();
    }

    public void btn_GetLocation(View view) {
        try {
            GetlocationNew();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        //Getlocation();

    }

    public void btn_back(View view) {
        Intent i = new Intent(this, JalMasterActivity.class);
        startActivity(i);
    }

    private void DoNew() {
        // ShowRecord();
        TextView CustNo = (TextView) findViewById(R.id.tv_Acc);
        TextView CustNm = (TextView) findViewById(R.id.tv_CustName);
        TextView tv_Address = (TextView) findViewById(R.id.tv_Loc);
        CustNo.setText("");
        CustNm.setText("");


        StartRound = (RelativeLayout) findViewById(R.id.btnStartRound);
        StartRound.setVisibility(View.VISIBLE);

        EndRound = (RelativeLayout) findViewById(R.id.btnEndRound);
        EndRound.setVisibility(View.INVISIBLE);

        TextView tv_Duration = (TextView) findViewById(R.id.tv_Duration);
        tv_Duration.setText("");


        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria cri = new Criteria();
        String provider = locationmanager.getBestProvider(cri, false);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        TrDate = (TextView) findViewById(R.id.et_Date);
        TrDate.setText(currentDateandTime);

        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        et_Day = (TextView) findViewById(R.id.et_Day);
        et_Day.setText(GetDayName(dayOfWeek));


        et_StartTime = (TextView) findViewById(R.id.et_StartTime);
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        et_StartTime.setText(StringTime);


        et_EndTime = (TextView) findViewById(R.id.et_EndTime);
        et_EndTime.setText("");
        tv_Note.setText("");
        Getlocation();

    }

    public void ShowRecord() {


        TextView CustNo = (TextView) findViewById(R.id.tv_Acc);
        TextView CustNm = (TextView) findViewById(R.id.tv_CustName);


        sqlHandler = new SqlHandler(this);
        String query = "SELECT  SaleManRounds.no ,SaleManRounds.Note  , SaleManRounds.CusNo,SaleManRounds.Note ,Customers.name ,Tr_Data,DayNum,Start_Time  " +
                "FROM SaleManRounds Left join Customers on Customers.no =SaleManRounds.CusNo  where Closed = 0";
        Cursor c1 = sqlHandler.selectQuery(query);

        et_StartTime = (TextView) findViewById(R.id.et_StartTime);

        et_Day = (TextView) findViewById(R.id.et_Day);
        EndRound = (RelativeLayout) findViewById(R.id.btnEndRound);
        StartRound = (RelativeLayout) findViewById(R.id.btnStartRound);
        et_EndTime = (TextView) findViewById(R.id.et_EndTime);
        TextView tv_Duration = (TextView) findViewById(R.id.tv_Duration);

        tv_Duration.setText("");
        EndRound.setVisibility(View.INVISIBLE);
        StartRound.setVisibility(View.VISIBLE);
        if (c1.getCount() > 0 && c1 != null) {
            //Toast.makeText(this,"يوجد ملف  مفتوح",Toast.LENGTH_SHORT).show();
            c1.moveToFirst();
            CustNo.setText(c1.getString(c1.getColumnIndex("CusNo")));
            CustNm.setText(c1.getString(c1.getColumnIndex("name")));
            TrDate.setText(c1.getString(c1.getColumnIndex("Tr_Data")));
            et_StartTime.setText(c1.getString(c1.getColumnIndex("Start_Time")));
            tv_Note.setText(c1.getString(c1.getColumnIndex("Note")));

            et_Day.setText(GetDayName(Integer.valueOf(c1.getString(c1.getColumnIndex("DayNum")))));
            EndRound.setVisibility(View.VISIBLE);
            StartRound.setVisibility(View.INVISIBLE);
            c1.close();
            // OrderNo = c1.getInt(c1.getColumnIndex("no"));

        }
        tv_Duration.setText("");
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String StringTime = StartTime.format(new Date());
        et_EndTime.setText(StringTime);


        // et_StartTime
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

            Date date1 = simpleDateFormat.parse(et_StartTime.getText().toString());
            Date date2 = simpleDateFormat.parse(et_EndTime.getText().toString());

            long diff = date2.getTime() - date1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            tv_Duration.setText(String.valueOf(diffHours) + ":" + String.valueOf(diffMinutes) + ":" + String.valueOf(diffSeconds));
        } catch (Exception ex) {
        }

    }

    public void btn_delete(View view) {
        sqlHandler.Delete("SaleManRounds", "");
    }

    public void btm_delete(View view) {
        sqlHandler = new SqlHandler(this);

        String query = " delete from   SaleManRounds   ";
        sqlHandler.executeQuery(query);

    }
    public void Save_Cust_Location(View view) {
        if (ComInfo.ComNo == 4) {
            if (!UserPermission.CheckAction(this, "30024", SCR_ACTIONS.Insert.getValue())) {
                com.cds_jo.GalaxySalesApp.ViewDialog alert = new com.cds_jo.GalaxySalesApp.ViewDialog();
                alert.showDialog(MainActivity.this, "نأسف أنت لا تملك الصلاحية  ", "حفظ موقع العميل");
                return;
            } else {
                SaveCustLocation();
            }


        } else {


            if (ComInfo.ComNo == Companies.Ukrania.getValue() ||   (ComInfo.ComNo == Companies.beutyLine.getValue()) || ComInfo.ComNo == Companies.Ma8bel.getValue()) {
                SaveCustLocation();
            } else {
                //    if (ComInfo.ComNo == 1 || ComInfo.ComNo==4) {

                String Man = DB.GetValue(MainActivity.this, "Tab_Password", "ManNo", "PassNo = 10 AND ManNo ='" + UserID + "'");
                final String pass = DB.GetValue(MainActivity.this, "Tab_Password", "Password", "PassNo = 10  and ManNo='" + Man + "'");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle(DB.GetValue(MainActivity.this, "Tab_Password", "PassDesc", "PassNo = 10 and ManNo='" + Man + "'"));
                alertDialog.setMessage("ادخل رمز التحقق");

                final EditText input = new EditText(MainActivity.this);
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

                                if (pass.equalsIgnoreCase(password)) {
                                    // Toast.makeText(getApplicationContext(),"رمز التحقق صحيح", Toast.LENGTH_SHORT).show();
                                    SaveCustLocation();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "رمز التحقق غير صحيح", Toast.LENGTH_SHORT).show();
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
            }
        }

     /*    else{

            SaveCustLocation();
        }*/
    }
    private void SaveCustLocation() {
        final TextView tv_x = (TextView) findViewById(R.id.tv_x);
        final TextView tv_y = (TextView) findViewById(R.id.tv_y);
        TextView CustNo = (TextView) findViewById(R.id.tv_Acc);


        if (CustNo.getText().toString().length() == 0) {
            CustNo.setError("required!");
            CustNo.requestFocus();
            return;
        }

        if (tv_y.getText().toString().length() == 0) {
            tv_y.setError("required!");
            tv_y.requestFocus();
            return;
        }

        if (tv_x.getText().toString().length() == 0) {
            tv_x.setError("required!");
            tv_x.requestFocus();
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sqlHandler = new SqlHandler(this);

        TextView CustNm = (TextView) findViewById(R.id.tv_CustName);
        TextView Custadd = (TextView) findViewById(R.id.tv_Loc);

        TrDate = (TextView) findViewById(R.id.et_Date);
        et_StartTime = (TextView) findViewById(R.id.et_StartTime);
        ContentValues cv = new ContentValues();
        cv.put("CusNo", CustNo.getText().toString());
        cv.put("Lat", tv_x.getText().toString());
        cv.put("Long", tv_y.getText().toString());
        cv.put("Address", Custadd.getText().toString());
        cv.put("date", TrDate.getText().toString());
        cv.put("UserID", sharedPreferences.getString("UserID", "-1"));
        cv.put("posted", "-1");
        long i;


        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("تحديد موقع العميل");

        String q = "select * from CustLocation where CusNo ='" + CustNo.getText().toString() + "'";
        Cursor c = sqlHandler.selectQuery(q);

        if (c != null && c.getCount() > 0) {
            i = sqlHandler.Update("CustLocation", cv, "CusNo ='" + CustNo.getText().toString() + "'");
            String s = "Update  Customers set Latitude = '" + tv_x.getText().toString() + "',  Longitude ='" + tv_y.getText().toString() + "' where no = '" + CustNo.getText().toString() + "'";
            sqlHandler.executeQuery(s);
            alertDialog.setMessage("تمت عملية تعديل موقع العميل بنجاح");
            c.close();
        } else {
            i = sqlHandler.Insert("CustLocation", null, cv);
            String s = "Update  Customers set Latitude = '" + tv_x.getText().toString() + "',  Longitude ='" + tv_y.getText().toString() + "' where no = '" + CustNo.getText().toString() + "'";
            sqlHandler.executeQuery(s);
            alertDialog.setMessage("عملية تخزين موقع العميل تمت بنجاح");


        }


        if (i > 0) {
            alertDialog.setIcon(R.drawable.tick);
        } else {
            alertDialog.setMessage("عملية  الحفظ لم تتم ");
            alertDialog.setIcon(R.drawable.delete);
        }

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharcustLocation();
            }
        });


        alertDialog.show();


    }

    public void SharcustLocation() {

        //sqlHandler=new SqlHandler(this);

        final Handler _handler = new Handler();
        String query = "  select distinct CusNo ,Lat ,Long ,Address, date, UserID from CustLocation   where posted = -1";
        Cursor c1 = sqlHandler.selectQuery(query);
        ArrayList<Cls_CustLocation> objlist;
        objlist = new ArrayList<Cls_CustLocation>();
        objlist.clear();


        if (c1 != null && c1.getCount() > 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_CustLocation obj = new Cls_CustLocation();
                    obj.setCusNo(c1.getString(c1
                            .getColumnIndex("CusNo")));
                    obj.setLat(c1.getString(c1
                            .getColumnIndex("Lat")));
                    obj.setLong(c1.getString(c1
                            .getColumnIndex("Long")));
                    obj.setAddress(c1.getString(c1
                            .getColumnIndex("Address")));
                    obj.setDate(c1.getString(c1
                            .getColumnIndex("date")));
                    obj.setUserID(c1.getString(c1
                            .getColumnIndex("UserID")));
                    obj.setPosted("-1");

                    objlist.add(obj);

                } while (c1.moveToNext());

            }
            c1.close();
        }
        final String json = new Gson().toJson(objlist);
        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(MainActivity.this);
                ws.SaveCustLocation(json);
                try {
                    if (We_Result.ID > 0) {
                        String query = " Update  CustLocation  set Posted='1'  where Posted = '-1'";
                        sqlHandler.executeQuery(query);
                        _handler.post(new Runnable() {
                            public void run() {



                            }
                        });


                    }
                } catch (final Exception e) {

                }
            }
        }).start();
    }

    public String GetMaxPONo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u = sharedPreferences.getString("UserID", "");
        u = u.trim();
        String query = "SELECT  ifnull(MAX(OrderNo), 0) +1 AS no FROM SaleManRounds where  ManNo ='" + u.toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        String max1 = "0";
        max1 = DB.GetValue(MainActivity.this, "OrdersSitting", "Visits", "1=1").replaceAll("[^\\d.]", "");


        if (max1 == "") {
            max1 = "0";
        }

        max1 = String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max)) {
            max = max1;
        }


        if (max.length() == 1) {
            max = intToString(Integer.parseInt(u), 2) + intToString(Integer.parseInt(max), 5);
        } else {
            max = (intToString(Integer.parseInt(max), 7));
        }
        return max;
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

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    public void btn_Home(View view) {

        Intent k = new Intent(this, JalMasterActivity.class);
        startActivity(k);
    }

    public void SearchArea(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "Gps");
        FragmentManager Manager = getFragmentManager();
        Select_Customer_Dis obj = new Select_Customer_Dis();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void SearchExceptions(View view) {
        UpdateUserExceptions();

    }

    private void ShowExceptionPop() {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "ExpGps");
        FragmentManager Manager = getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    private void UpdateUserExceptions() {


        tv = new TextView(getApplicationContext());
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
        final ProgressDialog custDialog = new ProgressDialog(MainActivity.this);
        custDialog.setProgressStyle(custDialog.STYLE_SPINNER);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText(" الاستثناءات");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();

        q = "Delete from ManExceptions";
        sqlHandler.executeQuery(q);
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(MainActivity.this);
                ws.Get_ManExceptions(UserID);

                try {

                    q = "Delete from ManExceptions";
                    sqlHandler.executeQuery(q);

                    q = " delete from sqlite_sequence where name='ManExceptions'";
                    sqlHandler.executeQuery(q);

                    Integer i;
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_CustNo = js.getJSONArray("CustNo");
                    JSONArray js_ExptCat = js.getJSONArray("ExptCat");
                    JSONArray js_Freq = js.getJSONArray("Freq");
                    JSONArray js_ExtraAmt = js.getJSONArray("ExtraAmt");
                    JSONArray js_ExtraPrecent = js.getJSONArray("ExtraPrecent");

                    for (i = 0; i < js_CustNo.length(); i++) {

                        q = "INSERT INTO ManExceptions(CustNo, ExptCat  , Freq ,ExtraAmt,ExtraPrecent) values ('"
                                + js_CustNo.get(i).toString()
                                + "','" + js_ExptCat.get(i).toString()
                                + "','" + js_Freq.get(i).toString()
                                + "','" + js_ExtraAmt.get(i).toString()
                                + "','" + js_ExtraPrecent.get(i).toString()
                                + "')";
                        sqlHandler.executeQuery(q);
                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();

                        }
                    }

                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();

                            ShowExceptionPop();


                        }
                    });

                } catch (final Exception e) {

                    _handler.post(new Runnable() {

                        public void run() {
                            custDialog.dismiss();
                            ShowExceptionPop();
                        }
                    });
                }
            }
        }).start();

    }

}

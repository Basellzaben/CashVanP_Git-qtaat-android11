package com.cds_jo.GalaxySalesApp.Tracking;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.cds_jo.GalaxySalesApp.ComInfo;
import com.cds_jo.GalaxySalesApp.ContactListItems;
import com.cds_jo.GalaxySalesApp.DB;
import com.cds_jo.GalaxySalesApp.JalMasterActivity;
import com.cds_jo.GalaxySalesApp.MyApplication;
import com.cds_jo.GalaxySalesApp.PopSmallMenue;
import com.cds_jo.GalaxySalesApp.Pop_Update_Qty;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.We_Result;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Methdes.MyTextView;
import header.SimpleSideDrawer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    ListView items_Lsit;
    private GoogleMap mMap;
    private LocationManager mLocationManager = null;
    private String provider = null;
    private Marker mCurrentPosition = null;
    ProgressDialog PD;
    private List<Cities> cityList;
    private ArrayList<LatLng> traceOfMe = null;
    private Polyline mPolyline = null;
    private LatLng mSourceLatLng = null;
    private LatLng mDestinationLatLng = null;
    private TextView tv = null;
    private RelativeLayout mFooterLayout = null;
    private ImageView Img_Menu;
    private SimpleSideDrawer mNav;
    ArrayList<Cls_ManLocationReport> LocationList;
    LatLng ManLoc ;
    int GoogleMapType=1;
    MyTextView tv_DayNm,tv_date,tv_FromTime,tv_ToTime;
    private int year, month, day;
    String Man,tv_UserNm;
    Circle circle;
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year,month, day);
        }
        return null;
    }
    public void setDate(View view) {
        showDialog(999);

    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {

        tv_date.setText(new StringBuilder().append(intToString(Integer.valueOf(day), 2)).append("/")
                    .append(intToString(Integer.valueOf(month), 2)).append("/").append(year));
        onProgressUpdate();
    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tv = (TextView) findViewById(R.id.tv);
        mFooterLayout = (RelativeLayout) findViewById(R.id.footer_layout);
        Man = "";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Man = sharedPreferences.getString("UserID", "");
        LocationList = new ArrayList<Cls_ManLocationReport>();

        // mNav = new SimpleSideDrawer(this);
        // mNav.setLeftBehindContentView(R.layout.po_nav_google_map);


        Button btn_Get_Data;
        btn_Get_Data = (Button) findViewById(R.id.btn_Get_Data);
        btn_Get_Data.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        int dayOfWeek;
        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


        tv_DayNm = (MyTextView) findViewById(R.id.tv_DayNm);
        tv_date = (MyTextView) findViewById(R.id.tv_date);
        tv_FromTime = (MyTextView) findViewById(R.id.tv_FromTime);
        tv_ToTime = (MyTextView) findViewById(R.id.tv_ToTime);


        tv_FromTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_FromTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("وقت بداية التتبع");
                mTimePicker.show();

            }
        });

        tv_ToTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_ToTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("وقت نهاية التتبع");
                mTimePicker.show();

            }
        });

        year = c.get(Calendar.YEAR);

        month = c.get(Calendar.MONTH);

        day = c.get(Calendar.DAY_OF_MONTH);

        tv_date.setText(currentDateandTime);
        tv_DayNm.setText(GetDayName(dayOfWeek));
        btn_Get_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mNav.toggleLeftDrawer();
                onProgressUpdate();
            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
            }
        });
        items_Lsit = (ListView) findViewById(R.id.items_Lsit);

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

               /* for (int i = 0; i < items_Lsit.getChildCount(); i++) {
                    View listItem = items_Lsit.getChildAt(i);
                    if(i%2==0)
                        listItem.setBackgroundColor(Color.WHITE);
                    if(i%2==1)
                        listItem.setBackgroundColor(MapsActivity.this.getResources().getColor(R.color.Gray2));

                  //  ((TextView)listItem).setTextColor(Color.WHITE);
                }*/

                arg1.setBackgroundColor(Color.GRAY);
                Cls_ManLocationReport o = (Cls_ManLocationReport) items_Lsit.getItemAtPosition(position);
                ShowMapWithTitle(o.getX(), o.getY(),o.getMan_Name());

            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //  onProgressUpdate();
        //addMarks(mMap);


        // Add a marker in Sydney and move the camera

      /*  try {
             circle.remove();
            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(32.0178761, 35.8637203))
                    .radius(200 * 1609.34) // Converting Miles into Meters...
                    .strokeColor(Color.RED)
                    .strokeWidth(50));
           circle.isVisible();
        } catch (Exception ex){
*/
         // Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
       // }

    }
    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(20);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }
    public String GetDayName(Integer Day){


        String DayNm ="" ;
        if (Day == 1) DayNm = "الاحد";
        else if ( Day == 2) DayNm = "الاثنين";
        else if (Day == 3) DayNm = "الثلاثاء";
        else if (Day == 4) DayNm = "الاربعاء";
        else if (Day == 5) DayNm = "الخميس";
        else if (Day == 6) DayNm = "الجمعة";
        else if (Day == 7) DayNm = "السبت";



        return  DayNm;

    }

    private  void ShowMapWithTitle(String x , String y , String t){

        Double Lat = Double.parseDouble(x);
        Double Long = Double.parseDouble(y);

        ManLoc= new LatLng(Lat, Long);
      //  mMap.clear();

        // Add a marker in Sydney and move the camera


        mMap.addMarker(new MarkerOptions().position(ManLoc).title(t)).showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ManLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));


    }
    private  void ShowMap(String x , String y){

           Double Lat = Double.parseDouble(x);
           Double Long = Double.parseDouble(y);

        ManLoc= new LatLng(Lat, Long);
         mMap.clear();

        // Add a marker in Sydney and move the camera


        mMap.addMarker(new MarkerOptions().position(ManLoc).title("Marker in ManLoc")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ManLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
      //  mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
      //   Toast.makeText(this, x +" xs, "+ y , Toast.LENGTH_SHORT).show();

    }
    public void onProgressUpdate( ){



        final List<String> items_ls = new ArrayList<String>();

        items_Lsit.setAdapter(null);
       LocationList.clear();
        final Handler _handler = new Handler();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(MapsActivity.this);

        custDialog.setTitle("الرجاء الانتظار");
        custDialog.setMessage("العمل جاري على نسخ البيانات");
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.show();




        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(MapsActivity.this);
                ws.GetManVisitDtl(Man,tv_date.getText().toString());


                try {

                    Integer i;
                    String q = "";


                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID= js.getJSONArray("ID");
                    JSONArray js_TransDate= js.getJSONArray("TransDate");
                    JSONArray js_LocX= js.getJSONArray("LocX");
                    JSONArray js_LoxY= js.getJSONArray("LoxY");
                    JSONArray js_LocOrder= js.getJSONArray("LocOrder");
                    JSONArray js_CustNm= js.getJSONArray("CustNm");


                    Cls_ManLocationReport obj = new Cls_ManLocationReport();



                    for( i =0 ; i<js_ID.length();i++)
                    {
                        obj = new Cls_ManLocationReport();


                        obj.setID(js_ID.get(i).toString());
                        obj.setMan_Name(js_CustNm.get(i).toString());

                        obj.setTime(js_TransDate.get(i).toString());

                        obj.setX(js_LocX.get(i).toString());
                        obj.setY(js_LoxY.get(i).toString());

                        LocationList.add(obj);


                        custDialog.setMax(js_ID.length());
                        custDialog.incrementProgressBy(1);

                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }

                    }


                    _handler.post(new Runnable() {

                        public void run() {

                            Cls_AllManLocation_Adapter Location_adapter = new Cls_AllManLocation_Adapter(
                                    MapsActivity.this, LocationList);

                            items_Lsit.setAdapter(Location_adapter);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    MapsActivity.this).create();
                            alertDialog.setTitle("تحديث البيانات");

                            alertDialog.setMessage("تمت عملية استرجاع  البيانات بنجاح ");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            // alertDialog.show();
                            drawPoints();
                            custDialog.dismiss();
                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    MapsActivity.this).create();
                            alertDialog.setTitle("جولات المندوبيين");
                            if (We_Result.ID == -404) {
                                alertDialog.setMessage(We_Result.Msg);
                            } else {
                                alertDialog.setMessage("لا يوجد بيانات" );
                            }
                            alertDialog.setIcon(R.drawable.tick);

                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();

                        }
                    });
                }
            }
        }).start();




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.setMapType(GoogleMapType);

      /*  if (isProviderAvailable() && (provider != null)) {
            locateCurrentPosition();
        }*/

        addMarks(mMap);
        drawCircle(new LatLng(32.0178761, 35.8637203));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ShowMenu(marker.getPosition().latitude+"");
                return false;
            }
        });


    }         /*for (int i = 0; i < cityList.size(); i++) {
            Cities city = cityList.get(i);
            LatLng latLng = new LatLng(city.getLatitude(), city.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Location");
            markerOptions.snippet("عدد الجولات ");
            markerOptions.icon(icon);
            markerOptions.title(city.getName());

            mMap.addMarker(markerOptions);


        } */
private void ShowMenu(String rr){
    final View view;
    Bundle bundle = new Bundle();
    FragmentManager Manager = getFragmentManager();
    PopSmallMenue obj = new PopSmallMenue();
    bundle.putString("Msg", rr);
    obj.setArguments(bundle);
    obj.show(Manager, null);

}
    @Override
    public void onBackPressed() {
         Intent k= new Intent(this, JalMasterActivity.class);
        startActivity(k);
    }

    private void addMarks(GoogleMap googleMap) {
       Double Lat = Double.parseDouble( "31.931656");
       Double Long = Double.parseDouble("35.870689");
//31.931656, 35.870689
        //initCities();


        mMap = googleMap;
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( 35.87698936004177 , 32.02167852621059),1));
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.goodsystem_icon);
     /*   CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(32.02167852621059,
                        35.87698936004177));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
*/



        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(32.016414, 35.866777);
        LatLng sydney = new LatLng(Lat, Long);

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        mMap.addMarker(new MarkerOptions().position(sydney).title(" "));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(zoom);




        //mMap.addMarker(markerOptions);


        // traceMe();
    }

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void locateCurrentPosition() {

        int status = getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                getPackageName());

        if (status == PackageManager.PERMISSION_GRANTED) {
            Location location = mLocationManager.getLastKnownLocation(provider);
            updateWithNewLocation(location);
            //  mLocationManager.addGpsStatusListener(this);
            long minTime = 5000;// ms
            float minDist = 5.0f;// meter
           /* mLocationManager.requestLocationUpdates(provider, minTime, minDist,
                    this);*/
        }
    }
    private boolean isProviderAvailable() {
        mLocationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        provider = mLocationManager.getBestProvider(criteria, true);
        if (mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;

            return true;
        }

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        }

        if (provider != null) {
            return true;
        }
        return false;
    }
    private void updateWithNewLocation(Location location) {
//31.931656, 35.870689
        Double lat = Double.parseDouble( "31.931656");
        Double lng = Double.parseDouble("35.870689");





     //   if (location != null && provider != null) {
          //  double lng = location.getLongitude();
          //  double lat = location.getLatitude();

            mSourceLatLng = new LatLng(lat, lng);

            addBoundaryToCurrentPosition(lat, lng);

            CameraPosition camPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(50f).build();

            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camPosition));
       /* } else {
            Log.d("Location error", "Something went wrong");
        }*/

    }
    private void addBoundaryToCurrentPosition(double lat, double lang) {

        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(new LatLng(lat, lang));
        mMarkerOptions.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.marker_current));
        mMarkerOptions.anchor(0.5f, 0.5f);

        CircleOptions mOptions = new CircleOptions()
                .center(new LatLng(lat, lang)).radius(500)
                .strokeColor(0x110000FF).strokeWidth(1).fillColor(0x110000FF);
        mMap.addCircle(mOptions);
        if (mCurrentPosition != null)
            mCurrentPosition.remove();
        mCurrentPosition = mMap.addMarker(mMarkerOptions);
    }
    private void traceMe(LatLng srcLatLng, LatLng destLatLng) {

        PD = new ProgressDialog(MapsActivity.this);
        PD.setMessage("Loading..");
        PD.show();

        String srcParam = srcLatLng.latitude + "," + srcLatLng.longitude;
        String destParam = destLatLng.latitude + "," + destLatLng.longitude;

        String modes[] = {"driving", "walking", "bicycling", "transit"};

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + srcParam + "&destination=" + destParam + "&sensor=false&units=metric&mode=walking";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        MapDirectionsParser parser = new MapDirectionsParser();
                        List<List<HashMap<String, String>>> routes = parser.parse(response);
                        ArrayList<LatLng> points = null;

                        for (int i = 0; i < routes.size(); i++) {
                            points = new ArrayList<LatLng>();
                            // lineOptions = new PolylineOptions();

                            // Fetching i-th route
                            List<HashMap<String, String>> path = routes.get(i);

                            // Fetching all the points in i-th route
                            for (int j = 0; j < path.size(); j++) {
                                HashMap<String, String> point = path.get(j);

                                double lat = Double.parseDouble(point.get("lat"));
                                double lng = Double.parseDouble(point.get("lng"));
                                LatLng position = new LatLng(lat, lng);

                                points.add(position);
                            }
                        }

                        drawPoints( mMap);
                        PD.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PD.dismiss();
                    }
                });

     MyApplication.getInstance().addToReqQueue(jsonObjectRequest);

    }
    private void drawPoints( ) {




        ArrayList<LatLng> points =new ArrayList<>();
        double lat=0 ;
        double lng=0 ;
        int i =0;
        try {
            for (i = 0; i < LocationList.size(); i++) {

                lat = Double.parseDouble(LocationList.get(i).getX());
                lng = Double.parseDouble(LocationList.get(i).getY());
                LatLng position = new LatLng(lat, lng);
                points.add(position);
                ShowMapWithTitle(LocationList.get(i).getX(), LocationList.get(i).getY(),LocationList.get(i).getMan_Name());

            }
        }catch (Exception x  ){}
        /*if(i>0) {
            ShowMap(LocationList.get(i - 1).getX(), LocationList.get(i - 1).getY());
        }
*/
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);

        mMap.animateCamera(zoom);


        if (points == null) {
            return;
        }
        traceOfMe = points;
        PolylineOptions polylineOpt = new PolylineOptions();
        for (LatLng latlng : traceOfMe) {
            polylineOpt.add(latlng);
            polylineOpt.width(20);
        }
        polylineOpt.color(Color.BLUE);


        if (mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
        if (mMap != null) {
            mPolyline = mMap.addPolyline(polylineOpt);

        } else {

        }
        if (mPolyline != null)
            mPolyline.setWidth(10);

        mFooterLayout.setVisibility(View.GONE);
    }
    private void drawPoints( GoogleMap mMaps) {
        ArrayList<LatLng> points =new ArrayList<>();
        double lat ;
        double lng ;
        for( int i =0 ; i< LocationList.size();i++) {

              lat = Double.parseDouble(LocationList.get(i).getX());
              lng = Double.parseDouble(LocationList.get(i).getY());
            LatLng position = new LatLng(lat, lng);
            points.add(position);

        }







        if (points == null) {
            return;
        }
        traceOfMe = points;
        PolylineOptions polylineOpt = new PolylineOptions();
        for (LatLng latlng : traceOfMe) {
            polylineOpt.add(latlng);
        }
        polylineOpt.color(Color.GREEN);
        if (mPolyline != null) {
            mPolyline.remove();
            mPolyline = null;
        }
        if (mMap != null) {
            mPolyline = mMap.addPolyline(polylineOpt);

        } else {

        }
        if (mPolyline != null)
            mPolyline.setWidth(10);

        mFooterLayout.setVisibility(View.GONE);
    }
    public void getDirection(View view) {
        //    tv.setText("SourceText: " + mSourceLatLng + "\n" + "DestinationText" + mDestinationLatLng);

        if (mSourceLatLng != null && mDestinationLatLng != null) {
            traceMe(mSourceLatLng, mDestinationLatLng);
        }
    }
    public void BtnBack(View view) {
        Intent k= new Intent(this, JalMasterActivity.class);
        startActivity(k);
    }

    public void btn_Hybrid(View view) {
        GoogleMapType=4;
        mMap.setMapType(GoogleMapType);
    }

    public void btn_Satellite(View view) {
        GoogleMapType=2;
        mMap.setMapType(GoogleMapType);
    }


    public void btn_Normal(View view) {
        GoogleMapType=1;
        mMap.setMapType(GoogleMapType);
    }

    public void btn_Terrain(View view) {
        GoogleMapType=3;
        mMap.setMapType(GoogleMapType);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {


            menu.setHeaderTitle("");
            menu.add(Menu.NONE, 1, Menu.NONE, "تعديل الكمية");
            menu.add(Menu.NONE, 2, Menu.NONE, "حذف المادة");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {



        return super.onContextItemSelected(item);
    }
}
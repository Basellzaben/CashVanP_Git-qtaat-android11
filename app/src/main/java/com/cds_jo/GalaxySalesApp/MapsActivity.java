package com.cds_jo.GalaxySalesApp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cds_jo.GalaxySalesApp.assist.Acc_ReportActivity;
import com.cds_jo.GalaxySalesApp.assist.CallWebServices;
import com.cds_jo.GalaxySalesApp.assist.Cls_Acc_Report;
import com.cds_jo.GalaxySalesApp.assist.Cls_Acc_Report_Adapter;
import com.cds_jo.GalaxySalesApp.assist.Cls_ManLocation_Adapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import header.Header_Frag;
import header.SimpleSideDrawer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {
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
    ArrayList<Cls_ManLocationReport>  LocationList;
    LatLng ManLoc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tv = (TextView) findViewById(R.id.tv);
        mFooterLayout = (RelativeLayout) findViewById(R.id.footer_layout);
        Img_Menu=(ImageView)findViewById(R.id.Img_Menu);
        LocationList = new ArrayList<Cls_ManLocationReport>();

       // mNav = new SimpleSideDrawer(this);
       // mNav.setLeftBehindContentView(R.layout.po_nav_google_map);

        Img_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mNav.toggleLeftDrawer();
            }
        });
        Button btn_Get_Data ;
        btn_Get_Data = (Button)findViewById(R.id.btn_Get_Data);
        btn_Get_Data.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));


        btn_Get_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mNav.toggleLeftDrawer();
                onProgressUpdate();
            }
        });

        items_Lsit=(ListView)findViewById(R.id.items_Lsit);

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
                ShowMap(o.getX(),o.getY());

            }
        });

        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      //  onProgressUpdate();
        //addMarks(mMap);
    }
    private  void ShowMap(String x ,String y){

           Double Lat = Double.parseDouble(x);
           Double Long =Double.parseDouble(y);

        ManLoc= new LatLng(Lat, Long);
        mMap.clear();

        // Add a marker in Sydney and move the camera

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        mMap.addMarker(new MarkerOptions().position(ManLoc).title("Marker in ManLoc"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ManLoc));
        mMap.animateCamera(zoom);
       // Toast.makeText(this, x +" , "+ y , Toast.LENGTH_SHORT).show();

    }
    public void onProgressUpdate( ){



        final List<String> items_ls = new ArrayList<String>();

        items_Lsit.setAdapter(null);



        final   TextView acc = (TextView)findViewById(R.id.tv_acc);




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
                ws.CallAllManLocation("1","1","1","1","1");


                try {

                    Integer i;
                    String q = "";


                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_ID= js.getJSONArray("ID");
                   // JSONArray js_ManNo= js.getJSONArray("ManNo");
                    JSONArray js_Tr_time= js.getJSONArray("Tr_time");
                 //   JSONArray js_Tr_date= js.getJSONArray("Tr_date");
                 //   JSONArray js_no= js.getJSONArray("no");
                    JSONArray js_X= js.getJSONArray("X");
                    JSONArray js_Y= js.getJSONArray("Y");
                 //   JSONArray js_Loct= js.getJSONArray("Loct");
                  //  JSONArray js_V_OrderNo= js.getJSONArray("V_OrderNo");
                    //JSONArray js_man_name= js.getJSONArray("man_name");



                    Cls_ManLocationReport obj = new Cls_ManLocationReport();

                    obj.setID("");
                    obj.setManNo("رقم المندوب");
                    obj.setTime("الوقت");
                    obj.setDate("التاريخ");
                    obj.setX("احداثيات X");
                    obj.setY("إحداثيات Y");
                    obj.setLoct("الموقع");
                    obj.setV_OrderNo(" ");
                    obj.setNo("  ");
                    obj.setMan_Name("اسم المندوب");
                    LocationList.add(obj);

                    // date,fromstore,tostore,des,docno,itemno,qty,UnitNo,UnitRate,myear


                    for( i =0 ; i<js_ID.length();i++)
                    {
                        obj = new Cls_ManLocationReport();


                        obj.setID(js_ID.get(i).toString());
                      //  obj.setManNo(js_ManNo.get(i).toString());
                        obj.setTime(js_Tr_time.get(i).toString());
                      //  obj.setDate(js_Tr_date.get(i).toString());
                        obj.setX(js_X.get(i).toString());
                        obj.setY(js_Y.get(i).toString());
                      //  obj.setLoct(js_Loct.get(i).toString());
                     //   obj.setV_OrderNo(js_V_OrderNo.get(i).toString());
                      //  obj.setNo(js_no.get(i).toString());
                      //  obj.setMan_Name(js_man_name.get(i).toString());
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (isProviderAvailable() && (provider != null)) {
            locateCurrentPosition();
        }

        addMarks(mMap);

    }
    @Override
    public void onBackPressed() {
         Intent k= new Intent(this, ManLocationsReport.class);
        startActivity(k);
    }

    private void addMarks(GoogleMap googleMap) {
       Double Lat = Double.parseDouble( getIntent().getExtras().getString("Lat"));
       Double Long =Double.parseDouble(getIntent().getExtras().getString("Lon"));

        //initCities();


        mMap = googleMap;
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( 35.87698936004177 , 32.02167852621059),1));
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.goodsystem_icon);
         /*for (int i = 0; i < cityList.size(); i++) {
            Cities city = cityList.get(i);
            LatLng latLng = new LatLng(city.getLatitude(), city.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Location");
            markerOptions.snippet("عدد الجولات ");
            markerOptions.icon(icon);
            markerOptions.title(city.getName());

            mMap.addMarker(markerOptions);

            mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        } */
     /*   CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(32.02167852621059,
                        35.87698936004177));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
*/



        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(32.016414, 35.866777);
        LatLng sydney = new LatLng(Lat, Long);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(zoom);




        //mMap.addMarker(markerOptions);


        // traceMe();
    }
    public void initCities() {
        cityList = new ArrayList<Cities>();
        String json = loadJSONFromAsset("city.json");

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("cities");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject city = jsonArray.getJSONObject(i);
                Cities cities = new Cities(city.getString("name"), city.getDouble("lat"), city.getDouble("long"));
                cityList.add(cities);
            }

        } catch (Exception e) {

        }
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

        if (location != null && provider != null) {
            double lng = location.getLongitude();
            double lat = location.getLatitude();

            mSourceLatLng = new LatLng(lat, lng);

            addBoundaryToCurrentPosition(lat, lng);

            CameraPosition camPosition = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(10f).build();

            if (mMap != null)
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(camPosition));
        } else {
            Log.d("Location error", "Something went wrong");
        }

    }
    private void addBoundaryToCurrentPosition(double lat, double lang) {

        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(new LatLng(lat, lang));
        mMarkerOptions.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.marker_current));
        mMarkerOptions.anchor(0.5f, 0.5f);

        CircleOptions mOptions = new CircleOptions()
                .center(new LatLng(lat, lang)).radius(10000)
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

                        drawPoints(points, mMap);
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
    private void drawPoints(ArrayList<LatLng> points, GoogleMap mMaps) {
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
        Intent k= new Intent(this, ManLocationsReport.class);
        startActivity(k);
    }
}
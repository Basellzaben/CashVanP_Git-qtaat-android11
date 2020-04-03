package com.cds_jo.GalaxySalesApp.CustomerSummary;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cds_jo.GalaxySalesApp.R;
import com.cds_jo.GalaxySalesApp.SqlHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomerLocationFrag extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    public CustomerLocationFrag() {

    }

    double latitude = 0.0;
    double longitude =0.0;
    String CusNm ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.cust_location, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u = sharedPreferences.getString("UserID", "");

        String q = "SELECT Latitude,Longitude,name from Customers where no='"+u+"'";
        SqlHandler sqlHandler =new SqlHandler(getActivity());
        Cursor c=sqlHandler.selectQuery(q);
        if (c != null && c.getCount() != 0) {
            c.moveToFirst();
            try {
                latitude = Double.parseDouble(c.getString(c.getColumnIndex("Latitude")));
                longitude = Double.parseDouble(c.getString(c.getColumnIndex("Longitude")));
                CusNm = c.getString(c.getColumnIndex("name"));
            }catch (Exception s){}
            c.close();
        }
          latitude = 17.385044;
          longitude = 78.486671;


        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Hello Maps");


        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));


        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(17.385044, 78.486671)).zoom(20).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        googleMap.setMapType(2);

        return v;
    }

}

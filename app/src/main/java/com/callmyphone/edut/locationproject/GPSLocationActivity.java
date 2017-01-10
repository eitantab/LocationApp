package com.callmyphone.edut.locationproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GPSLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    LocationManager locationManager;
    LocationListener locationListener;
    Location location = new Location("GPS");



    String locationString;
    Double lat;
    Double lng;
    int type;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        type = getIntent().getIntExtra("type", 0);

        switch (type) {
            case 1:

                locationString = getIntent().getStringExtra("SCANRESULT");
                String[] latlog = locationString.split(",");
                lat = Double.valueOf(latlog[0]);
                lng = Double.valueOf(latlog[1]);

                break;
            case 2:
                //init gps
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new MyLocationListener();
                break;
            case 3:
                break;
            default:

                break;
        }



        /*//init gps
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();*/


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            //TODO
        }

     //   System.out.println("map ready");



        switch (type) {
            case 1:
                // Add a marker in Sydney and move the camera
                LatLng myLocation = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                //saveLocation();
                break;
            case 2:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
                break;
            default:

                break;
        }




        // Add a marker in my location and move the camera
        /*LatLng myLocation = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));*/



    }































    //inner class listener for gps
    private class MyLocationListener implements LocationListener {

        //Time tenSecCount = new Time(10000);
        private double lat;
        private double lng;
        LatLng myLocation;


        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            myLocation = new LatLng(lat, lng);

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(myLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

            //setMyLocationToWEB(lat, lng);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


}

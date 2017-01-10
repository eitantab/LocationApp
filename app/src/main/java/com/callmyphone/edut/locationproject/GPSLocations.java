package com.callmyphone.edut.locationproject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Time;

public class GPSLocations extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Location location = new Location("GPS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_locations);
    }






    private class MyLocationListener implements LocationListener{

        Time tenSecCount = new Time(10000);

        @Override
        public void onLocationChanged(Location location) {
            location.getLatitude();
            location.getLongitude();
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

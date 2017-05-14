package com.akshaybhoge.fireassist.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * Created by akshaybhoge on 5/8/17.
 */

public class GpsTool {

    String locationProvider = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public GpsTool(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.turnOnGps(context);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Location Change
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
        };
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }

    public Location getLocation(){
        return locationManager.getLastKnownLocation(locationProvider);
    }

    public void startGpsUpdate(){
        locationManager.requestLocationUpdates(locationProvider,0,0,locationListener);
    }
    public void stopGpsUpdates(){
        locationManager.removeUpdates(locationListener);
    }

    public void turnOnGps(Context context){
        boolean isEnabled = locationManager.isProviderEnabled(locationProvider);
        if(!isEnabled){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

    }
}

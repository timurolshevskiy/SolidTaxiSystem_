package com.gtest.myapplicationmap;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by дима on 23.09.2015.
 */

public class User {
    public Location imHere;

    private String Login;
    private String Password;

    private void startLocListener(Context context) {
        LocationManager locManager = context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new LocationListener() {                     // проверить живет ли лисенер после выполнения функции
            @Override
            public void onLocationChanged(Location location) {
                imHere = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                imHere = null;                                  // надо ли
            }
        };
    }

    User() {
        startLocListener();
    }

    public LatLng getLatLng() {
        return new LatLng(imHere.getLatitude(), imHere.getLongitude());
    }
}

package com.gtest.myapplicationmap;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MainActivity extends Activity {

    final int DEFAULT_ZOOM = 10;

    GoogleMap googleMap;

    EditText query;
    Button search;
    Button setMarker;
    Button fromMyLocation;

    User user = new User();
    LatLng latLngTaxist;                                // с сервака
    //User user = new Passenger/Taxist луйк

    //MarkerOptions markerOptions = new MarkerOptions().draggable(true).title("").position(myCity);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setMarker = (Button) findViewById(R.id.setMarker);
        fromMyLocation = (Button) findViewById(R.id.fromMyLocation);
        search = (Button) findViewById(R.id.search);
        query = (EditText) findViewById(R.id.query);

        createMapView();

        setMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMarker(user.getLatLng());
            }
        });

        fromMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.imHere != null) {
                    addMarker(user.getLatLng());
                    gotoLocation(user.getLatLng(), DEFAULT_ZOOM);
                }
                else Toast.makeText(getApplicationContext(), "Location unavailable", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gotoLocation (double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        googleMap.moveCamera(update);
    }

    private void gotoLocation (LatLng ll, float zoom) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        googleMap.moveCamera(update);
    }

    public void geoLocate (View view) {
        hideSoftKeyboard(view);                                                                     // use in overrided onclick
        String desiredPlace = query.getText().toString();
        Geocoder gc = new Geocoder(this);

        try {
            List<Address> addresses = gc.getFromLocationName(desiredPlace, 1);                      // what if no connection
            Address addr = addresses.get(0);
            if (addr.getLocality() == null)
                Toast.makeText(this, "unknown place", Toast.LENGTH_LONG).show();
            else {
                double lat = addr.getLatitude();
                double lng = addr.getLongitude();
                gotoLocation(lat, lng, DEFAULT_ZOOM);                                               // make dynamic zoom
                Toast.makeText(this, addr.getAddressLine(1), Toast.LENGTH_LONG).show();
            }
        }
        catch (IOException exception) {
            Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void hideSoftKeyboard (View view) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void addMarker(LatLng pos) {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions()
                                    .position(pos)
                                    .title("Marker")
                                    .draggable(true)
            );
        }
    }

    private void createMapView() {
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap();

                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(), "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }

        if (user.imHere != null) {
            // nullPointerEx, imHere can be null
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(user.getLatLng(), DEFAULT_ZOOM);
            googleMap.moveCamera(update);
        }
        else Toast.makeText(this, "cannot locate", Toast.LENGTH_LONG).show();
    }

}

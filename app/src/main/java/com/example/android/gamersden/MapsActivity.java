package com.example.android.gamersden;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        Intent intent = getIntent();
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng coord = null;

        String completeAddress = intent.getStringExtra("completeAddress");

        try {
            address = coder.getFromLocationName(completeAddress, 5);
            if (address != null) {

                Address location = address.get(0);

                coord = new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        mMap = googleMap;

        MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(coord);
        mMap.addMarker(mMarkerOptions.title(completeAddress));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 14.0f));

        //Toast.makeText(this, completeAddress + coord,Toast.LENGTH_LONG).show();
    }


}

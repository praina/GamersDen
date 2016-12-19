package com.example.android.gamersden;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private EditText findRouteEdittext;
    private Button findRouteButton;

    private String searchString;

    private Geocoder coder, coder2;
    private List<Address> addressList, addressList2;
    private LatLng coord,coord2;
    private MarkerOptions mMarkerOptions,mMarkerOptions2,mMarkerOptions3;
    private TextInputLayout findRouteEditTextInputLayout;
    private String completeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findRouteEdittext = (EditText)findViewById(R.id.findRouteEdittext);
        findRouteButton = (Button)findViewById(R.id.findRouteButton);
        findRouteEditTextInputLayout = (TextInputLayout) findViewById(R.id.findRouteEdittextInputLayout);

//        findRouteEdittext.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!findRouteEdittext.getText().toString().trim().equals(""))
//                {
//                    if (findRouteEditTextInputLayout != null) {
//                        findRouteEditTextInputLayout.setError(null);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        findRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchString = findRouteEdittext.getText().toString().toLowerCase().trim();


                if (searchString.equals("")){
//                    findRouteEditTextInputLayout.setErrorEnabled(true);
//                    findRouteEditTextInputLayout.setError("Enter a location or City!");
                    //use snackbar to show something.

                }
                else{

                    coder2 = new Geocoder(MapsActivity.this);
                    coord2 = null;

                    try {
                        addressList2 = coder2.getFromLocationName(searchString, 5);
                        if (addressList2 != null) {

                            Address location = addressList2.get(0);

                            coord2 = new LatLng(location.getLatitude(), location.getLongitude());
                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }

//                    Log.e(TAG, "This is coord2 : " + coord2 + " " + searchString);
//
//                    Toast.makeText(MapsActivity.this, "This is coord2 + ET VALUE : " + coord2 + " " + searchString,Toast.LENGTH_LONG).show();

                    if (coord2!=null && coord!=null) {
                        mMap.clear();
                        mMarkerOptions2 = new MarkerOptions();
                        mMarkerOptions2.position(coord2);
                        mMarkerOptions2.title(searchString); //this is the address of the new string entered by the user
                        mMap.addMarker(mMarkerOptions2).showInfoWindow();

                        mMarkerOptions3 = new MarkerOptions();
                        mMarkerOptions3.position(coord);
                        mMarkerOptions3.title(completeAddress); //this is the address of the original string received from intent
                        mMap.addMarker(mMarkerOptions3).showInfoWindow();

//                        PolylineOptions polylineOptions = new PolylineOptions().add(coord,coord2).width(5)
//                                .color(ContextCompat.getColor(MapsActivity.this,R.color.colorPrimary));
//                        mMap.addPolyline(polylineOptions);
                        //Instead of polyline we need route!

                    }
                    else {
                        //Snackbar : cannot find the given location
                    }

                }
            }
        });

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
        coder = new Geocoder(this);
        coord = null;

        completeAddress = intent.getStringExtra("completeAddress");

        try {
            addressList = coder.getFromLocationName(completeAddress, 5);
            if (addressList != null) {

                Address location = addressList.get(0);

                coord = new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        mMap = googleMap;

        if (coord!=null) {

            mMarkerOptions = new MarkerOptions();
            mMarkerOptions.position(coord);
            mMarkerOptions.title(completeAddress);
            mMap.addMarker(mMarkerOptions).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 14.0f));
        }
        else{
            //Snackbar : cannot find the given location
        }
        //Toast.makeText(this, completeAddress + coord,Toast.LENGTH_LONG).show();
    }


}

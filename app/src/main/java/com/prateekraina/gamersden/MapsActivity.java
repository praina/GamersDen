package com.prateekraina.gamersden;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    Double maxLatitude, minLatitude, minLongitude, maxLongitude;
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

        maxLatitude=null;
        maxLongitude=null;
        minLatitude=null;
        minLongitude=null;


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

                        LatLng origin = coord2;
                        LatLng dest = coord;

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);


                    }
                    else {
                        //Snackbar : cannot find the given location
                    }

                }
            }
        });

    }

    //################### GOOGLE MAP ROUTE METHODS ###################################

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;


        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Download URL TAG","Exception while downloading url " + e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }


        return data;
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
        setTitle(intent.getStringExtra("title"));

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

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    //################################################################################################

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                com.prateekraina.gamersden.DirectionsJSONParser parser = new com.prateekraina.gamersden.DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            MarkerOptions markerOptions = new MarkerOptions();


            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(ContextCompat.getColor(MapsActivity.this, R.color.colorPrimary));
            }

            if (lineOptions!=null) {
                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);

                if (coord.latitude < coord2.latitude) {
                    minLatitude = coord.latitude;
                    maxLatitude = coord2.latitude;
                } else {
                    minLatitude = coord2.latitude;
                    maxLatitude = coord.latitude;
                }

                if (coord.longitude < coord2.longitude) {
                    minLongitude = coord.longitude;
                    maxLongitude = coord2.longitude;
                } else {
                    minLongitude = coord2.longitude;
                    maxLongitude = coord.longitude;
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(maxLatitude, maxLongitude));
                builder.include(new LatLng(minLatitude, minLongitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 48));

            }

        }



    }


}

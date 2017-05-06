package com.prateekraina.gamersden;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private com.prateekraina.gamersden.RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressDialog loading;

    private ArrayList<com.prateekraina.gamersden.DataObject> data = new ArrayList<com.prateekraina.gamersden.DataObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (checkInternetConnection()){

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, com.prateekraina.gamersden.AddDetailActivity.class);
                        startActivity(intent);
                    }
                });
            }

            getData();
        }
        else{
            mAdapter = new com.prateekraina.gamersden.RecyclerViewAdapter(data);
            mRecyclerView.setAdapter(mAdapter);

            String message = "No Internet Connection!";
            displayNetworkSnackbar(MainActivity.this,message);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setHint("Search"); //This changes the text which is set as the hint in searchview
        searchEditText.setHintTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorText)); //This changes the color of the hint
        searchEditText.setBackgroundResource(R.drawable.abc_textfield_search_default_mtrl_alpha); //Gives the underline on the edittext
//        searchEditText.setCursorVisible(true);
//        searchEditText.getBackground().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorText), PorterDuff.Mode.MULTIPLY);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //mAdapter.getFilter().filter(query);
                mAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //mAdapter.getFilter().filter(newText);
                mAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //This is where the JSON response is received by making a call to the server
    private void getData() {

        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = com.prateekraina.gamersden.MyConfig.GET_DATA_URL;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Log.d(TAG, "Response received :"+response);
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,"ONERRORRESPONSE : " + error.getMessage().toString());
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //This is where the JSON response is Parsed
    private void showJSON(String response) {

        data.clear();

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray result = jsonObj.getJSONArray(com.prateekraina.gamersden.MyConfig.JSON_ARRAY);
            for(int i=0;i<result.length();i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                com.prateekraina.gamersden.DataObject dataObject = new com.prateekraina.gamersden.DataObject();

                dataObject.setNAME(jsonObject.getString(com.prateekraina.gamersden.MyConfig.NAME_KEY));
                dataObject.setLOCATION(jsonObject.getString(com.prateekraina.gamersden.MyConfig.LOCATION_KEY));
                dataObject.setADDRESS(jsonObject.getString(com.prateekraina.gamersden.MyConfig.ADDRESS_KEY));
                dataObject.setCITY(jsonObject.getString(com.prateekraina.gamersden.MyConfig.CITY_KEY));
                dataObject.setHOURLY_RATE(jsonObject.getString(com.prateekraina.gamersden.MyConfig.HOURLY_RATE_KEY));

                data.add(i,dataObject);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(data.size()==0){

            String message = "No matching results found!";
            displayNetworkSnackbar(MainActivity.this,message);

        }

        mAdapter = new com.prateekraina.gamersden.RecyclerViewAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    //This method displays the Snackbar
    public void displayNetworkSnackbar(Activity activity, String message) {
        //View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }).show();

    }

    //This method checks if there is an internet connection or not!
    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

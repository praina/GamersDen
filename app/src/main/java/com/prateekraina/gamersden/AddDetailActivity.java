package com.prateekraina.gamersden;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddDetailActivity extends AppCompatActivity {

    private EditText cafeNameET;
    private EditText cafeAddressET;
    private EditText cafeLocationET;
    private EditText cafeCityET;
    private EditText cafeHourlyRateET;

    private Button cafeSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cafeNameET = (EditText)findViewById(R.id.cafeName);
        cafeAddressET = (EditText)findViewById(R.id.cafeAddress);
        cafeLocationET = (EditText)findViewById(R.id.cafeLocation);
        cafeCityET = (EditText)findViewById(R.id.cafeCity);
        cafeHourlyRateET = (EditText)findViewById(R.id.cafeHourlyRate);
        cafeSubmitBtn = (Button)findViewById(R.id.cafeSubmitDataButton);


        final TextInputLayout cafeNameInputLayout = (TextInputLayout) findViewById(R.id.input_layout_cafeName);
        final TextInputLayout cafeAddressInputLayout = (TextInputLayout) findViewById(R.id.input_layout_address);
        final TextInputLayout cafeLocationInputLayout = (TextInputLayout) findViewById(R.id.input_layout_location);
        final TextInputLayout cafeCityInputLayout = (TextInputLayout) findViewById(R.id.input_layout_city);
        final TextInputLayout cafeHourlyRateInputLayout = (TextInputLayout) findViewById(R.id.input_layout_hourlyRate);

        cafeNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!cafeNameET.getText().toString().trim().equals(""))
                {
                    if (cafeNameInputLayout != null) {
                        cafeNameInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cafeAddressET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!cafeAddressET.getText().toString().trim().equals(""))
                {
                    if (cafeAddressInputLayout != null) {
                        cafeAddressInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cafeLocationET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!cafeLocationET.getText().toString().trim().equals(""))
                {
                    if (cafeLocationInputLayout != null) {
                        cafeLocationInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cafeCityET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!cafeCityET.getText().toString().trim().equals(""))
                {
                    if (cafeCityInputLayout != null) {
                        cafeCityInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cafeHourlyRateET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!cafeHourlyRateET.getText().toString().trim().equals(""))
                {
                    if (cafeHourlyRateInputLayout != null) {
                        cafeHourlyRateInputLayout.setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cafeSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int checkValue=1; //to check if some field is empty or not we keep check value.


                if (cafeNameET.getText().toString().trim().equals(""))
                {
                    if (cafeNameInputLayout != null) {
                        cafeNameInputLayout.setErrorEnabled(true);
                        cafeNameInputLayout.setError("Cafe Name not entered");
                    }

                    checkValue=0;
                }

                if (cafeAddressET.getText().toString().trim().equals(""))
                {
                    if (cafeAddressInputLayout != null) {
                        cafeAddressInputLayout.setErrorEnabled(true);
                        cafeAddressInputLayout.setError("Address not entered");
                    }

                    checkValue=0;
                }

                if (cafeLocationET.getText().toString().trim().equals(""))
                {
                    if (cafeLocationInputLayout != null) {
                        cafeLocationInputLayout.setErrorEnabled(true);
                        cafeLocationInputLayout.setError("Location not entered");
                    }

                    checkValue=0;
                }

                if (cafeCityET.getText().toString().trim().equals(""))
                {
                    if (cafeCityInputLayout != null) {
                        cafeCityInputLayout .setErrorEnabled(true);
                        cafeCityInputLayout .setError("City not entered");
                    }

                    checkValue=0;
                }

                if (cafeHourlyRateET.getText().toString().trim().equals(""))
                {
                    if (cafeHourlyRateInputLayout != null) {
                        cafeHourlyRateInputLayout .setErrorEnabled(true);
                        cafeHourlyRateInputLayout .setError("Hourly Rate not entered");
                    }

                    checkValue=0;
                }

                if(checkValue==1)
                {
                    addData();

                    Intent intent = new Intent(AddDetailActivity.this, com.prateekraina.gamersden.MainActivity.class);
                    intent.putExtra("sentFrom","AddDetailActivity");
                    startActivity(intent);
                }

            }
        });

    }

    private void addData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, com.prateekraina.gamersden.MyConfig.ADD_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(AddDetailActivity.this,response,Toast.LENGTH_LONG).show();
                        View rootView = AddDetailActivity.this.getWindow().getDecorView().findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(rootView, response, Snackbar.LENGTH_LONG);
                        snackbar.show();

//                        Intent intent = new Intent(AddDetailActivity.this,MainActivity.class);
//                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(AddDetailActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        View rootView = AddDetailActivity.this.getWindow().getDecorView().findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(rootView, error.toString(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(com.prateekraina.gamersden.MyConfig.NAME_KEY,cafeNameET.getText().toString().trim());
                params.put(com.prateekraina.gamersden.MyConfig.ADDRESS_KEY,cafeAddressET.getText().toString().trim());
                params.put(com.prateekraina.gamersden.MyConfig.LOCATION_KEY,cafeLocationET.getText().toString().trim());
                params.put(com.prateekraina.gamersden.MyConfig.CITY_KEY,cafeCityET.getText().toString().trim());
                params.put(com.prateekraina.gamersden.MyConfig.HOURLY_RATE_KEY,cafeHourlyRateET.getText().toString().trim());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}

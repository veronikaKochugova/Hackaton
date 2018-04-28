package com.spbpu.hackaton.httpclient;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static private final String URL_GET_ALL = "allCountries";
    //static private final String URL = "http://192.168.3.234:8090/";
    static private final String URL = "http://10.20.0.95:8090/";

    TextView txtMessage;
    Button btnGet;
    String[] allCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMessage = findViewById(R.id.txtMsg);
        btnGet = findViewById(R.id.btnGet);

        btnGet.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            // JSONObject request
            /*case R.id.btnGet:

                JsonObjectRequest getRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        URL_GET, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                txtMessage.setText(response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                txtMessage.setText("ERROR: " + error.toString());
                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(getRequest);

                break;*/

            // String request
            case R.id.btnGet:
                StringRequest getRequest = new StringRequest(
                        Request.Method.GET,
                        URL + URL_GET_ALL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                txtMessage.setText("OK : ");
                                String tmp = response;
                                allCountries = tmp.split(",");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                txtMessage.setText("ERROR: " + error.toString());
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                getRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(getRequest);
                break;
        }

    }
}

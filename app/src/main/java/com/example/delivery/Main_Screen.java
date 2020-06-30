package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.delivery.adapters.DeliveryAdapter;
import com.example.delivery.models.Delivery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main_Screen extends AppCompatActivity {
    final String TAG = "Main_Screen";
    Button btnLogout;
    SharedPreferences sharedpreferences;
    ArrayList<Delivery> deliveryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);

        getDeliveries();

        sharedpreferences = getSharedPreferences(BuildConfig.APPLICATION_ID + ".credentials", Context.MODE_PRIVATE);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void getDeliveries() {
        String URL = "http://" + getString(R.string.host_port) + "/api/deliveries";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest objectRequest = new JsonArrayRequest(
            Request.Method.GET,
            URL,
            null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            deliveryArrayList.clear();
                            for (int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    Delivery delivery = new Delivery(
                                        Integer.parseInt(jsonObject.getString("id")),
                                        Integer.parseInt(jsonObject.getString("courier_id")),
                                        Double.parseDouble(jsonObject.getString("latitude")),
                                        Double.parseDouble(jsonObject.getString("longitude")),
                                        jsonObject.getString("addressee_name"),
                                        jsonObject.getString("address"),
                                        Integer.parseInt(jsonObject.getString("id"))
                                    );

                                    deliveryArrayList.add(delivery);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        setDeliveryList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);
    }

    private void setDeliveryList() {
        DeliveryAdapter adapter = new DeliveryAdapter(Main_Screen.this, deliveryArrayList);
        ListView listView = findViewById(R.id.lvDelivery);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Delivery delivery = deliveryArrayList.get(i);
                Log.d(TAG, delivery.getAddreseeName());
                Toast.makeText(getApplicationContext(), delivery.getAddreseeName(), Toast.LENGTH_SHORT);
                Intent donut = new Intent(Main_Screen.this, DeliveryDetailsActivity.class);
                startActivity(donut);
            }
        });
    }

    private void logout() {
        sharedpreferences.edit().clear().commit();
        Log.d(TAG, sharedpreferences.getString("client_code", "null"));
        Log.d(TAG, sharedpreferences.getString("company", "null"));
        Log.d(TAG, sharedpreferences.getString("user", "null"));
        Log.d(TAG, sharedpreferences.getString("password", "null"));

        Intent mySuperIntent = new Intent(Main_Screen.this, MainActivity.class);
        startActivity(mySuperIntent);
        finish();
    }
}

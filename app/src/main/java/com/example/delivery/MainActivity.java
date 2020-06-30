package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.delivery.adapters.DeliveryAdapter;
import com.example.delivery.models.Delivery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String TAG = "Main_Screen";
    Button btnLogout;
    SharedPreferences sharedpreferences;
    ArrayList<Delivery> deliveryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(BuildConfig.APPLICATION_ID + ".credentials", Context.MODE_PRIVATE);

        getDeliveries();
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void getDeliveries() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", sharedpreferences.getString("company", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest objectRequest = new JsonArrayRequest(
            Request.Method.GET,
            Constant.GET_DELIVERIES,
            null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
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
        ){

            // provide token in header

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedpreferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer " + token);
                return map;
            }
        };

        requestQueue.add(objectRequest);
    }

    private void setDeliveryList() {
        DeliveryAdapter adapter = new DeliveryAdapter(MainActivity.this, deliveryArrayList);
        ListView listView = findViewById(R.id.lvDelivery);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Delivery delivery = deliveryArrayList.get(i);
                Log.d(TAG, delivery.getAddreseeName());
                Toast.makeText(getApplicationContext(), delivery.getAddreseeName(), Toast.LENGTH_SHORT);
                Intent donut = new Intent(MainActivity.this, DeliveryDetailsActivity.class);
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

        Intent mySuperIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(mySuperIntent);
        finish();
    }
}

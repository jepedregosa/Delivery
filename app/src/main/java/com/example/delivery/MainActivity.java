package com.example.delivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.delivery.adapters.DeliveryAdapter;
import com.example.delivery.models.Delivery;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    Button btnLogout, btnTest, btnTest2;
    static SharedPreferences sharedpreferences;
    static ArrayList<Delivery> deliveryArrayList = new ArrayList<>();

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

//        btnTest = findViewById(R.id.btnTest);
//        btnTest.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                uploadSignature();
//            }
//        });

        btnTest2 = findViewById(R.id.btnTest2);
        btnTest2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToSignature();
            }
        });
    }

    private void goToSignature() {
        Intent intent = new Intent(MainActivity.this, Signature_Pad.class);
        startActivity(intent);
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
                                        Integer.parseInt(jsonObject.getString("signed"))
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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
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

    static DeliveryAdapter deliveryAdapter;

    private void setDeliveryList() {
        deliveryAdapter = new DeliveryAdapter(MainActivity.this, deliveryArrayList);
        final ListView listView = findViewById(R.id.lvDelivery);
        listView.setAdapter(deliveryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Delivery delivery = deliveryArrayList.get(i);
                final View selectedView = listView.getChildAt(i);
                View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.show();

                Button btnDirection = dialogView.findViewById(R.id.btnDirection);
                btnDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, Delivery_Location.class);
                        intent.putExtra("id", delivery.getId());
                        intent.putExtra("address", delivery.getAddress());
                        intent.putExtra("latitude", delivery.getLatitude());
                        intent.putExtra("longitude", delivery.getLongitude());
                        startActivity(intent);
                    }
                });

                Button btnDeliver = dialogView.findViewById(R.id.btnDeliver);
                if (delivery.getSigned() == 1) {
                    btnDeliver.setText("Undelivered");
                }

                btnDeliver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView ivSigned = selectedView.findViewById(R.id.ivSigned);
                        if (delivery.getSigned() == 1) {
                            deliveryArrayList.get(i).setSigned(0);
                            ivSigned.setImageResource(R.drawable.ic_baseline_check_box_outline_blank_24);
                        } else {
                            deliveryArrayList.get(i).setSigned(1);
                            ivSigned.setImageResource(R.drawable.ic_baseline_check_box_24);
                        }

                        updateDeliverySigned(delivery.getId());
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });
    }

    private void logout() {
        sharedpreferences.edit().clear().apply();
        Intent mySuperIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(mySuperIntent);
        finish();
    }

    private void updateDeliverySigned(final int delivery_id) {
        StringRequest request = new StringRequest(
            Request.Method.POST,
            Constant.UPDATE_DELIVERIES_SIGNED,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Rest Response", response.toString());
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("success")){
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Rest Response", error.toString());
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedpreferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("delivery_id", Integer.toString(delivery_id));
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

//    private Bitmap bitmap = null;
//
//    private void uploadSignature(){
//        StringRequest request = new StringRequest(
//            Request.Method.POST,
//            Constant.UPLOAD_DELIVERY_SIGNATURE,
//            new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.d("Rest Response", response.toString());
//                    try {
//                        JSONObject object = new JSONObject(response);
//                        if (object.getBoolean("success")){
//                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                        }
//                        Log.e("Rest Response", object.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("Rest Response", error.toString());
//                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                String token = sharedpreferences.getString("token","");
//                HashMap<String,String> map = new HashMap<>();
//                map.put("Authorization","Bearer "+token);
//                return map;
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> map = new HashMap<>();
//                map.put("desc", "description_test");
//                map.put("photo", bitmapToString(bitmap));
//                return map;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(request);
//    }
//
//    private String bitmapToString(Bitmap bitmap) {
//        if (bitmap != null){
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//            byte [] array = byteArrayOutputStream.toByteArray();
//            return Base64.encodeToString(array,Base64.DEFAULT);
//        }
//        return "";
//    }
}

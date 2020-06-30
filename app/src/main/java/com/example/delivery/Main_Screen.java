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

public class Main_Screen extends AppCompatActivity {
    final String TAG = "Main_Screen";
    Button btnLogout, map;
    SharedPreferences sharedpreferences;

    ListView deliveryListView;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__screen);

        sharedpreferences = getSharedPreferences(BuildConfig.APPLICATION_ID + ".credentials", Context.MODE_PRIVATE);

        btnLogout = findViewById(R.id.btnLogout);
        map = findViewById(R.id.map);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logout();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Main_Screen.this, Delivery_Location.class));
            }
        });

        deliveryListView = (ListView)findViewById(R.id.deliveryListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.delivery_list, countryList);
        deliveryListView.setAdapter(arrayAdapter);
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



//        deliveryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object o = deliveryListView.getItemAtPosition(position);
//
////                prestationEco str = (prestationEco)o; //As you are using Default String Adapter
////                Toast.makeText(getBaseContext(),str.getTitle(),Toast.LENGTH_SHORT).show();
//            }
//        });



    }


}

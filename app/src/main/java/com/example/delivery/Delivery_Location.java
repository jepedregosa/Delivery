package com.example.delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Delivery_Location extends AppCompatActivity {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    int id;
    String address;
    Double latitude;
    Double longitude;
    Button btnSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__location);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        address = intent.getStringExtra("address");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        btnSignature = findViewById(R.id.btnSignature);
        btnSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToSignature();
            }
        });

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map_location);
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(Delivery_Location.this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(Delivery_Location.this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    private void goToSignature() {
        Intent intent = new Intent(this, Signature_Pad.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
//                            My Location
                            LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions myLocation = new MarkerOptions()
                                .position(myLatLng)
                                .title("I am here");
                            googleMap.addMarker(myLocation);

//                            Delivery Location
                            LatLng locationLatLng = new LatLng(latitude, longitude);
                            MarkerOptions deliveryLocation = new MarkerOptions()
                                .position(locationLatLng)
                                .title(address);
                            googleMap.addMarker(deliveryLocation);

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 10));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
}
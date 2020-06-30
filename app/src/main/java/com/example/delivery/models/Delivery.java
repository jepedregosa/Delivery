package com.example.delivery.models;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

//public class Delivery  implements Serializable {
//    @SerializedName("id")
//    public Integer id;
//    @SerializedName("courier_id")
//    public Integer courier_id;
//    @SerializedName("latitude")
//    public Double latitude;
//    @SerializedName("longitude")
//    public Double longitude;
//    @SerializedName("addresee_name")
//    public String addresee_name;
//    @SerializedName("address")
//    public String address;
//    @SerializedName("signed")
//    public Integer signed;
//}

public class Delivery {
//    public Delivery(JSONObject object){
//        try {
//            this.name = object.getString("name");
//            this.hometown = object.getString("hometown");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static ArrayList<Delivery> fromJson(JSONArray jsonObjects) {
//        ArrayList<Delivery> users = new ArrayList<Delivery>();
//        for (int i = 0; i < jsonObjects.length(); i++) {
//            try {
//                users.add(new Delivery(jsonObjects.getJSONObject(i)));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return users;
//    }

    public Integer id;
    public Integer courierId;
    public Double latitude;
    public Double longitude;
    public String addreseeName;
    public String address;
    public Integer signed;

    public Delivery(Integer id, Integer courierId, Double latitude, Double longitude, String addreseeName, String address, Integer signed) {
        this.id = id;
        this.courierId = courierId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addreseeName = addreseeName;
        this.address = address;
        this.signed = signed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddreseeName() {
        return addreseeName;
    }

    public void setAddreseeName(String addreseeName) {
        this.addreseeName = addreseeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSigned() {
        return signed;
    }

    public void setSigned(Integer signed) {
        this.signed = signed;
    }
}

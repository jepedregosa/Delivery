package com.example.delivery.models;

public class Delivery {
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

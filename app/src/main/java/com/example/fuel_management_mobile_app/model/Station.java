package com.example.fuel_management_mobile_app.model;

import androidx.annotation.NonNull;

public class Station {

    private Id id;
    private int fuelStationId;
    private String fuelStationName;
    private String location;
    private String opentime;
    private String closetime;

    public Station() {

    }

    public Station(Id id, int fuelStationId, String fuelStationName, String location, String opentime, String closetime) {
        this.id = id;
        this.fuelStationId = fuelStationId;
        this.fuelStationName = fuelStationName;
        this.location = location;
        this.opentime = opentime;
        this.closetime = closetime;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public int getFuelStationId() {
        return fuelStationId;
    }

    public void setFuelStationId(int fuelStationId) {
        this.fuelStationId = fuelStationId;
    }

    public String getFuelStationName() {
        return fuelStationName;
    }

    public void setFuelStationName(String fuelStationName) {
        this.fuelStationName = fuelStationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", fuelStationId=" + fuelStationId +
                ", fuelStationName='" + fuelStationName + '\'' +
                ", location='" + location + '\'' +
                ", opentime='" + opentime + '\'' +
                ", closetime='" + closetime + '\'' +
                '}';
    }

}

package com.connorhenke.mcts3000.models;

public class Stop {

    public static final String ID = "stpid";
    public static final String NAME = "stpnm";
    public static final String LAT = "lat";
    public static final String LON = "lon";

    private String stopId;
    private String stopName;
    private double latitude;
    private double longitude;

    public Stop(double latitude, double longitude, String stopId, String stopName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.stopId = stopId;
        this.stopName = stopName;
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}

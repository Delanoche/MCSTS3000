package com.connorhenke.mcts3000.models;

import com.google.android.gms.maps.model.LatLng;

public class Bus {

    public static final String VID = "vid";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String HEADING = "hdg";

    private LatLng location;
    private int heading;
    private int vid;

    public Bus(LatLng location, int heading, int vid) {
        this.location = location;
        this.heading = heading;
        this.vid = vid;
    }

    public LatLng getLocation() {
        return location;
    }

    public int getHeading() {
        return heading;
    }

    public int getVid() {
        return vid;
    }
}

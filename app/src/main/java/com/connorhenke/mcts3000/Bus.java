package com.connorhenke.mcts3000;

import com.google.android.gms.maps.model.LatLng;

public class Bus {

    private LatLng location;
    private int heading;
    private String vid;

    public Bus(LatLng location, int heading, String vid) {
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

    public String getVid() {
        return vid;
    }
}

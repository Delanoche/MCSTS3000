package com.connorhenke.mcts3000;

public class Stop {

    private String stopId;
    private String stopName;
    private String pointDistance;
    private String sequence;
    private String type;
    private String latitude;
    private String longitude;

    public Stop(String sequence, String type, String latitude, String longitude, String stopId, String stopName, String pointDistance) {
        this.sequence = sequence;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stopId = stopId;
        this.stopName = stopName;
        this.pointDistance = pointDistance;
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public String getPointDistance() {
        return pointDistance;
    }

    public String getSequence() {
        return sequence;
    }

    public String getType() {
        return type;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}

package com.connorhenke.mcts3000.models;

public class Favorite {

    private int stopId;
    private String stopName;
    private String routeId;

    public Favorite(int stopId, String stopName, String routeId) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.routeId = routeId;
    }

    public int getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public String getRouteId() {
        return routeId;
    }
}

package com.connorhenke.mcts3000.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {

    private String stopId;
    private String stopName;
    private String routeId;
    private String direction;

    public Favorite(String stopId, String stopName, String routeId, String direction) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.routeId = routeId;
        this.direction = direction;
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stopId);
        dest.writeString(this.stopName);
        dest.writeString(this.routeId);
        dest.writeString(this.direction);
    }

    protected Favorite(Parcel in) {
        this.stopId = in.readString();
        this.stopName = in.readString();
        this.routeId = in.readString();
        this.direction = in.readString();
    }

    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}

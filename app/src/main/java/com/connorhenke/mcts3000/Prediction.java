package com.connorhenke.mcts3000;

import org.json.JSONException;
import org.json.JSONObject;

public class Prediction {

	private String timeStamp;
	private String type;
	private String stopName;
	private String stopId;
	private String vid;
	private int dstp;
	private String route;
	private String routeDir;
	private String destination;
	private String predictionTime;
	private boolean delay;
	private String zone;
	private String prdctdn;

	public Prediction() {
	}

	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public String getStopId() {
		return stopId;
	}
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public int getDstp() {
		return dstp;
	}
	public void setDstp(int dstp) {
		this.dstp = dstp;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getRouteDir() {
		return routeDir;
	}
	public void setRouteDir(String routeDir) {
		this.routeDir = routeDir;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getPredictionTime() {
		return predictionTime;
	}
	public void setPredictionTime(String predictionTime) {
		this.predictionTime = predictionTime;
	}
	public boolean isDelay() {
		return delay;
	}
	public void setDelay(boolean delay) {
		this.delay = delay;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getPrdctdn() {
		return prdctdn;
	}
	public void setPrdctdn(String prdctdn) {
		this.prdctdn = prdctdn;
	}

	public void loadJSON(JSONObject obj) throws JSONException {
		if(obj.has("delay"))
			setDelay(obj.getBoolean("delay"));
		if(obj.has("des"))
			setDestination(obj.getString("des"));
		if(obj.has("dstp"))
			setDstp(obj.getInt("dstp"));
		if(obj.has("prdctdn"))
			setPrdctdn(obj.getString("prdctdn"));
		if(obj.has("prdtm"))
			setPredictionTime(obj.getString("prdtm"));
		if(obj.has("rt"))
			setRoute(obj.getString("rt"));
		if(obj.has("stpid"))
			setStopId(obj.getString("stpid"));
		if(obj.has("stpnm"))
			setStopName(obj.getString("stpnm"));
		if(obj.has("tmstmp"))
			setTimeStamp(obj.getString("tmstmp"));
		if(obj.has("typ"))
			setType(obj.getString("typ"));
		if(obj.has("vid"))
			setVid(obj.getString("vid"));
		if(obj.has("zone"))
			setZone(obj.getString("zone"));

	}
}

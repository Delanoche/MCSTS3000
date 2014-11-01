package com.connorhenke.mcts;

public class PatternTurn {

	private int sequenceNumber;
	private String latitude;
	private String longitude;
	private String type;
	private double patternDistance;
	
	public PatternTurn(int sequenceNumber, String latitude, String longitude, String type, double patternDistance) {
		this.sequenceNumber = sequenceNumber;
		this.latitude = latitude;
		this.longitude = longitude;
		this.type = type;
		this.patternDistance = patternDistance;
	}
	
	public int getsequenceNumber() {
		return sequenceNumber;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getType() {
		return type;
	}
	
	public double getPatternDistance() {
		return patternDistance;
	}
}

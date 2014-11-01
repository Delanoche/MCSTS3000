package com.connorhenke.mcts;

import java.util.List;

public class Pattern {
	
	private String patternId;
	private double length;
	private String routeDirection;
	private List<PatternTurn> patternTurns;
	
	public Pattern(String patternId, double length, String routeDirection, List<PatternTurn> patternTurns) {
		this.patternId = patternId;
		this.length = length;
		this.routeDirection = routeDirection;
		this.patternTurns = patternTurns;
	}
	
	public String getPatternId() {
		return patternId;
	}
	
	public double getLength() {
		return length;
	}
	
	public String getRouteDirection() {
		return routeDirection;
	}
	
	public List<PatternTurn> getPatternTurns() {
		return patternTurns;
	}
}

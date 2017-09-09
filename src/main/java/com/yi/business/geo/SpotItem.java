package com.yi.business.geo;

public class SpotItem {
	private String srcStr = "";
	private SpotUnit spotUnit = new SpotUnit();
	private int spotStart = 0;
	private int spotEnd = 0;

	public String getSrcStr() {
		return srcStr;
	}

	public void setSrcStr(String srcStr) {
		this.srcStr = srcStr;
	}

	public SpotUnit getSpotUnit() {
		return spotUnit;
	}

	public void setSpotUnit(SpotUnit spotUnit) {
		this.spotUnit = spotUnit;
	}

	public int getSpotStart() {
		return spotStart;
	}

	public void setSpotStart(int spotStart) {
		this.spotStart = spotStart;
	}

	public int getSpotEnd() {
		return spotEnd;
	}

	public void setSpotEnd(int spotEnd) {
		this.spotEnd = spotEnd;
	}

	public SpotItem() {
		super();
	}

	public SpotItem(String srcStr, SpotUnit spotUnit, int spotStart, int spotEnd) {
		super();
		this.srcStr = srcStr;
		this.spotUnit = spotUnit;
		this.spotStart = spotStart;
		this.spotEnd = spotEnd;
	}
}

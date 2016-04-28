package com.example.androidxmldemopull;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EarthquakeEntry implements Serializable {
	public String earthQuakeName;
	public String time;
	public String longitude;
	public String latitude;
	public String depth;
	public String uncertainty;
	public String getEarthQuakeName() {
		return earthQuakeName;
	}
	public void setEarthQuakeName(String earthQuakeName) {
		this.earthQuakeName = earthQuakeName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public String getUncertainty() {
		return uncertainty;
	}
	public void setUncertainty(String uncertainty) {
		this.uncertainty = uncertainty;
	}

	
}

package com.distributie.beans;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class BeanMapRoute {

	private List<LatLng> routePoints;
	private String distance;

	public List<LatLng> getRoutePoints() {
		return routePoints;
	}

	public void setRoutePoints(List<LatLng> routePoints) {
		this.routePoints = routePoints;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

}

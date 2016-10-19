package com.distributie.utils;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Geocoder;

import com.distributie.beans.Address;
import com.google.android.gms.maps.model.LatLng;

public class MapUtils {

	public static LatLng geocodeAddress(Address address, Context context) throws Exception {

		LatLng coords = null;

		double latitude = 0;
		double longitude = 0;

		StringBuilder strAddress = new StringBuilder();

		if (address.getStreetName() != null && !address.getStreetName().trim().equals("")) {
			strAddress.append(address.getStreetName());
			strAddress.append(",");
		}

		if (address.getStreetNo() != null && !address.getStreetNo().trim().equals("")) {
			strAddress.append(address.getStreetNo());
			strAddress.append(",");
		}

		if (address.getRegion() != null && !address.getRegion().trim().equals("")) {
			strAddress.append(address.getRegion());
			strAddress.append(",");
		}

		if (address.getCity() != null && !address.getCity().trim().equals("")) {
			strAddress.append(address.getCity());
			strAddress.append(",");
		}

		if (address.getCountry() != null && !address.getCountry().equals("") && !address.getCountry().trim().equals("RO")) {
			strAddress.append(address.getCountry());

		}

		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		List<android.location.Address> addresses = geoCoder.getFromLocationName(strAddress.toString(), 1);

		if (addresses.size() > 0) {

			latitude = addresses.get(0).getLatitude();
			longitude = addresses.get(0).getLongitude();

		}

		coords = new LatLng(latitude, longitude);

		return coords;
	}

	public static LatLng geocodeSimpleAddress(String address, Context context) throws Exception {

		LatLng coords = null;

		double latitude = 0;
		double longitude = 0;

		StringBuilder strAddress = new StringBuilder(address);

		if (strAddress.toString().endsWith(",")) {
			strAddress.append("ROMANIA");
		} else {
			strAddress.append(",");
			strAddress.append("ROMANIA");
		}

		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		List<android.location.Address> addresses = geoCoder.getFromLocationName(strAddress.toString(), 1);

		if (addresses.size() > 0) {

			latitude = addresses.get(0).getLatitude();
			longitude = addresses.get(0).getLongitude();

		}

		coords = new LatLng(latitude, longitude);

		return coords;
	}

	public static double distanceXtoY(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

}

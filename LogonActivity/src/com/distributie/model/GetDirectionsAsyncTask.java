package com.distributie.model;

import java.util.Map;

import org.w3c.dom.Document;

import com.distributie.beans.BeanMapRoute;
import com.distributie.listeners.MapListener;
import com.distributie.maps.GMapV2Direction;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, BeanMapRoute> {
	public static final String DIRECTIONS_MODE = "directions_mode";
	private Activity activity;
	private Exception exception;
	private ProgressDialog progressDialog;
	private MapListener mapListener;

	public GetDirectionsAsyncTask(Activity activity) {
		super();
		this.activity = activity;
	}

	public void onPreExecute() {
		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage("Calculare ruta");
		progressDialog.show();
	}

	public void onPostExecute(BeanMapRoute result) {
		progressDialog.dismiss();
		if (exception == null) {

			if (mapListener != null)
				mapListener.mapComlete(result);

		} else {
			processException();
		}
	}

	@Override
	protected BeanMapRoute doInBackground(Map<String, String>... params) {
		Map<String, String> paramMap = params[0];
		try {
			LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get("startPointLat")), Double.valueOf(paramMap.get("startPointLng")));
			LatLng toPosition = new LatLng(Double.valueOf(paramMap.get("endPointLat")), Double.valueOf(paramMap.get("endPointLng")));
			GMapV2Direction md = new GMapV2Direction();
			Document doc = md.getDocument(fromPosition, toPosition, paramMap.get("directionsMode"));

			BeanMapRoute mapRoute = new BeanMapRoute();

			mapRoute.setRoutePoints(md.getDirection(doc));
			mapRoute.setDistance(md.getDistanceText(doc));

			return mapRoute;
		} catch (Exception e) {
			exception = e;
			return null;
		}
	}

	public void setMapListener(MapListener mapListener) {
		this.mapListener = mapListener;
	}

	private void processException() {
		Toast.makeText(activity, "Eroare citire date", Toast.LENGTH_SHORT).show();
	}
}

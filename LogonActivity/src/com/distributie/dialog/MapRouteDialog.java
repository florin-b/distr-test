package com.distributie.dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.distributie.beans.BeanMapRoute;
import com.distributie.beans.BeanRouteBounds;
import com.distributie.beans.InitStatus;
import com.distributie.enums.EnumOperatiiAdresa;
import com.distributie.listeners.MapListener;
import com.distributie.listeners.OperatiiAdresaListener;
import com.distributie.maps.GMapV2Direction;
import com.distributie.model.GetDirectionsAsyncTask;
import com.distributie.model.OperatiiAdresa;
import com.distributie.model.OperatiiAdresaImpl;
import com.distributie.utils.MapUtils;
import com.distributie.view.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapRouteDialog extends Dialog implements OperatiiAdresaListener, MapListener {

	private Context context;
	private TextView textDistanta;
	private Button btnClose;
	private FragmentManager fm;
	private GoogleMap map;
	private String codAdresaDest;
	private OperatiiAdresa opAdresa;

	public MapRouteDialog(Context context, String codAdresaDest) {
		super(context);
		this.context = context;
		this.codAdresaDest = codAdresaDest;
		fm = ((Activity) context).getFragmentManager();
		setContentView(R.layout.map_route_dialog);
		setCancelable(true);
		setupLayout();

	}

	private void setupLayout() {
		textDistanta = (TextView) findViewById(R.id.textDistanta);
		btnClose = (Button) findViewById(R.id.btnClose);
		setBtnCloseListener();

		map = ((MapFragment) fm.findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomGesturesEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);

		opAdresa = new OperatiiAdresaImpl(context);
		opAdresa.setOperatiiAdresaListener(MapRouteDialog.this);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codAdresa", codAdresaDest);
		params.put("nrDocument", InitStatus.getInstance().getDocument());

		opAdresa.getRouteBounds(params);

	}

	private void setBtnCloseListener() {
		btnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	@Override
	public void dismiss() {
		super.dismiss();
		removeMap();
	}

	private void removeMap() {
		MapFragment f = (MapFragment) fm.findFragmentById(R.id.map);
		if (f != null)
			fm.beginTransaction().remove(f).commit();

	}

	private void drawMap(BeanRouteBounds routeBounds) {

		LatLng coord = null;
		try {
			coord = MapUtils.geocodeAddress(routeBounds.getAdresaDest(), context);
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));

		findDirections(routeBounds.getPozMasina(), coord, GMapV2Direction.MODE_DRIVING);

	}

	@SuppressWarnings("unchecked")
	private void findDirections(LatLng startPosition, LatLng stopPosition, String mode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("startPointLat", String.valueOf(startPosition.latitude));
		map.put("startPointLng", String.valueOf(startPosition.longitude));
		map.put("endPointLat", String.valueOf(stopPosition.latitude));
		map.put("endPointLng", String.valueOf(stopPosition.longitude));
		map.put("directionsMode", mode);

		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask((Activity) context);
		asyncTask.setMapListener(this);
		asyncTask.execute(map);
	}

	public void handleGetDirectionsResult(List<LatLng> directionPoints) {

		LatLng mapCenter = directionPoints.get(directionPoints.size() / 2);

		CameraUpdate center = CameraUpdateFactory.newLatLngZoom(mapCenter, 11);

		map.moveCamera(center);

		PolylineOptions polyOptions = new PolylineOptions();
		polyOptions.addAll(directionPoints);
		polyOptions.color(Color.RED);
		polyOptions.width(4);

		map.addPolyline(polyOptions);

		addMapMarkers(map, directionPoints);

	}

	private void addMapMarkers(GoogleMap map, List<LatLng> directionPoints) {
		MarkerOptions marker = new MarkerOptions();
		marker.position(directionPoints.get(0));
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.truck));
		map.addMarker(marker);

		marker = new MarkerOptions();
		marker.position(directionPoints.get(directionPoints.size() - 1));
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag));
		map.addMarker(marker);

	}

	@Override
	public void opAdresaComplete(EnumOperatiiAdresa methodName, String result) {

		switch (methodName) {
		case GET_ROUTE_BOUNDS:
			drawMap(opAdresa.deserializeRouteBounds(result));
			break;
		default:
			break;
		}

	}

	@Override
	public void mapComlete(BeanMapRoute result) {
		handleGetDirectionsResult(result.getRoutePoints());
		textDistanta.setText(result.getDistance());
	}

}

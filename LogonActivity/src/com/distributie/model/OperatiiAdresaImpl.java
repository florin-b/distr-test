package com.distributie.model;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.distributie.beans.Address;
import com.distributie.beans.BeanRouteBounds;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiAdresa;
import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.OperatiiAdresaListener;
import com.distributie.utils.UtilsAddress;
import com.google.android.gms.maps.model.LatLng;

public class OperatiiAdresaImpl implements OperatiiAdresa, AsyncTaskListener {

	private Context context;
	private OperatiiAdresaListener listener;
	private EnumOperatiiAdresa numeOperatie;

	public OperatiiAdresaImpl(Context context) {
		this.context = context;
	}

	public void getRouteBounds(HashMap<String, String> params) {
		numeOperatie = EnumOperatiiAdresa.GET_ROUTE_BOUNDS;
		performOperation(numeOperatie, params);

	}

	private void performOperation(EnumOperatiiAdresa numeOperatie, HashMap<String, String> params) {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeOperatie.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResults();
	}

	public void setOperatiiAdresaListener(OperatiiAdresaListener listener) {
		this.listener = listener;
	}

	@Override
	public void onTaskComplete(String methodName, String result, EnumNetworkStatus networkStatus) {
		if (listener != null)
			listener.opAdresaComplete(numeOperatie, result);
	}

	public BeanRouteBounds deserializeRouteBounds(String result) {
		BeanRouteBounds routeBonds = new BeanRouteBounds();
		Address address = new Address();
		LatLng pozMasina = null;

		try {

			JSONObject jsonObject = new JSONObject((String) result);

			JSONObject jsonAdresa = new JSONObject(jsonObject.getString("adresaDest"));
			JSONObject jsonPozitie = new JSONObject(jsonObject.getString("pozMasina"));

			if (jsonAdresa instanceof JSONObject) {

				address.setCountry(jsonAdresa.getString("country"));
				address.setRegion(UtilsAddress.getNumeJudet(jsonAdresa.getString("region")));
				address.setCity(jsonAdresa.getString("city"));
				address.setStreetName(jsonAdresa.getString("streetName"));
				address.setStreetNo(jsonAdresa.getString("streetNo"));

			}

			if (jsonPozitie instanceof JSONObject) {
				pozMasina = new LatLng(Double.valueOf(jsonPozitie.getString("latitude")), Double.valueOf(jsonPozitie
						.getString("longitude")));
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		routeBonds.setAdresaDest(address);
		routeBonds.setPozMasina(pozMasina);

		return routeBonds;
	}
}

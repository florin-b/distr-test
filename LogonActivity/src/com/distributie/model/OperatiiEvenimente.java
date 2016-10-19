package com.distributie.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.distributie.beans.BeanClientAlarma;
import com.distributie.beans.BeanEvenimentStop;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiEvenimente;
import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.JavaWSListener;
import com.distributie.listeners.OperatiiEvenimenteListener;

public class OperatiiEvenimente implements AsyncTaskListener, JavaWSListener {

	private EnumOperatiiEvenimente numeOperatie;
	private Context context;
	private OperatiiEvenimenteListener listener;
	private HashMap<String, String> params;

	public OperatiiEvenimente(Context context) {
		this.context = context;
	}

	public void saveNewStop(HashMap<String, String> params) {
		numeOperatie = EnumOperatiiEvenimente.SAVE_NEW_STOP;
		performOperation(numeOperatie, params);
	}

	public void getEvenimentStop(HashMap<String, String> params) {
		numeOperatie = EnumOperatiiEvenimente.CHECK_STOP;
		this.params = params;
		callJavaWS();
	}

	public void setSfarsitIncarcare(HashMap<String, String> params) {
		numeOperatie = EnumOperatiiEvenimente.SET_SFARSIT_INC;
		performOperation(numeOperatie, params);
	}

	public void getSfarsitIncarcare(HashMap<String, String> params) {
		numeOperatie = EnumOperatiiEvenimente.GET_SFARSIT_INC;
		performOperation(numeOperatie, params);
	}

	private void performOperation(EnumOperatiiEvenimente numeOperatie, HashMap<String, String> params) {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeOperatie.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResults();
	}

	private void callJavaWS() {

		JavaWSCall call = new JavaWSCall(numeOperatie.toString(), params, (AsyncTaskListener) this, context);
		call.getCallResults();
	}

	public void setOperatiiEvenimenteListener(OperatiiEvenimenteListener listener) {
		this.listener = listener;
	}

	@Override
	public void onTaskComplete(String methodName, String result, EnumNetworkStatus networkStatus) {
		if (listener != null)
			listener.opEventComplete(result, numeOperatie);

	}

	public void setEventListener(OperatiiEvenimenteListener event) {
		this.listener = event;
	}

	@Override
	public void onJWSComplete(String methodName, String result) {
		if (listener != null) {
			listener.opEventComplete(result, numeOperatie);

		}

	}

	public BeanEvenimentStop deserializeEvenimentStop(String serializedEvent) {
		BeanEvenimentStop evenimentStop = new BeanEvenimentStop();

		try {
			JSONObject jsonObject = new JSONObject(serializedEvent);

			evenimentStop.setIdEveniment(Long.valueOf(jsonObject.getString("idEveniment")));
			evenimentStop.setEvenimentSalvat(Boolean.valueOf(jsonObject.getString("evenimentSalvat")));

			JSONArray jsonArray = new JSONArray(jsonObject.getString("clienti"));
			List<BeanClientAlarma> listClienti = new ArrayList<BeanClientAlarma>();

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				BeanClientAlarma client = new BeanClientAlarma();
				client.setCodClient(object.getString("codClient"));
				client.setCodAdresa(object.getString("codAdresa"));
				client.setCodBorderou(object.getString("codBorderou"));
				client.setNume(object.getString("numeClient"));
				listClienti.add(client);

			}

			evenimentStop.setClientiAlarma(listClienti);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return evenimentStop;

	}

}

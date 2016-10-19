package com.distributie.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.distributie.beans.Etapa;
import com.distributie.beans.EvenimentNou;
import com.distributie.enums.TipEveniment;

public class JSONOperations {

	private HashMap<String, String> paramData;
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public JSONOperations(Context context) {
		this.context = context;
	}

	public JSONOperations(Context context, HashMap<String, String> paramData) {
		this.context = context;
		this.paramData = paramData;
	}

	public String encodeNewEventData() {
		String jsonData = "";

		if (paramData.size() > 0) {

			JSONObject tempObject = new JSONObject();

			for (Entry<String, String> entry : paramData.entrySet()) {

				try {
					tempObject.put(entry.getKey(), entry.getValue());
				} catch (JSONException e) {
					Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
				}

			}

			jsonData = tempObject.toString();

		}

		return jsonData;
	}

	public String serializeOrdineLivrare(List<Etapa> listEtape) {

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;

		Iterator<Etapa> iterator = listEtape.iterator();
		Etapa etapa = null;
		while (iterator.hasNext()) {

			try {

				jsonObject = new JSONObject();
				etapa = iterator.next();

				if (etapa.getCodClient() != null) {
					jsonObject.put("borderou", etapa.getDocument());
					jsonObject.put("client", etapa.getCodClient());
					jsonObject.put("codAdresa", etapa.getCodAdresaClient());
					jsonObject.put("document", etapa.getFactura().getNrFactura());
					jsonObject.put("pozitie", etapa.getPozitie());
					jsonArray.put(jsonObject);
				}
			} catch (JSONException ex) {

			}

		}

		return jsonArray.toString();
	}

	public String encodeNewEventData(EvenimentNou newEvent) {

		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("codSofer", newEvent.getCodSofer());
			jsonObject.put("document", newEvent.getDocument());
			jsonObject.put("client", newEvent.getClient());
			jsonObject.put("codAdresa", newEvent.getCodAdresa());
			jsonObject.put("eveniment", newEvent.getEveniment());
			jsonObject.put("tipEveniment", newEvent.getTipEveniment());
			jsonObject.put("data", newEvent.getData());
			jsonObject.put("ora", newEvent.getOra());

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	public EvenimentNou decodeEventData(String object) {

		EvenimentNou eveniment = new EvenimentNou();

		if (!object.contains("{"))
			object = "{" + object;

		if (!object.contains("}"))
			object = object + "}";

		try {
			JSONObject jsonObject = new JSONObject(object);

			if (jsonObject instanceof JSONObject) {
				eveniment.setCodSofer(jsonObject.getString("codSofer"));
				eveniment.setDocument(jsonObject.getString("document"));
				eveniment.setClient(jsonObject.getString("client"));
				eveniment.setCodAdresa(jsonObject.getString("codAdresa"));
				eveniment.setEveniment(jsonObject.getString("eveniment"));
				eveniment.setTipEveniment(TipEveniment.valueOf(jsonObject.getString("tipEveniment")));
				eveniment.setData(jsonObject.getString("data"));
				eveniment.setOra(jsonObject.getString("ora"));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return eveniment;

	}

	public String serializeListEvents(List<EvenimentNou> listEvenimente) {

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;

		Iterator<EvenimentNou> iterator = listEvenimente.iterator();
		EvenimentNou event = null;
		while (iterator.hasNext()) {

			try {

				jsonObject = new JSONObject();
				event = iterator.next();

				jsonObject.put("codSofer", event.getCodSofer());
				jsonObject.put("document", event.getDocument());
				jsonObject.put("client", event.getClient());
				jsonObject.put("codAdresa", event.getCodAdresa());
				jsonObject.put("eveniment", event.getEveniment());
				jsonObject.put("tipEveniment", event.getTipEveniment());
				jsonObject.put("data", event.getData());
				jsonObject.put("ora", event.getOra());
				jsonObject.put("bordParent", event.getBordParent());
				jsonArray.put(jsonObject);

			} catch (JSONException ex) {

			}

		}

		return jsonArray.toString();
	}

}

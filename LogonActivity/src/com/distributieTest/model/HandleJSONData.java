package com.distributieTest.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class HandleJSONData {

	private String JSONString;
	private JSONArray jsonObject;
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public HandleJSONData(Context context, String JSONString) {
		this.context = context;
		this.JSONString = JSONString;
	}

	public ArrayList<BeanBorderou> decodeJSONBorderouri() {

		BeanBorderou unBorderou = null;

		ArrayList<BeanBorderou> objectsList = new ArrayList<BeanBorderou>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject borderouObject = jsonObject.getJSONObject(i);

				unBorderou = new BeanBorderou();
				unBorderou.setNumarBorderou(borderouObject.getString("numarBorderou"));
				unBorderou.setDataEmiterii(borderouObject.getString("dataEmiterii"));
				unBorderou.setEvenimentBorderou(borderouObject.getString("evenimentBorderou"));
				unBorderou.setTipBorderou(borderouObject.getString("tipBorderou"));
				objectsList.add(unBorderou);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanFacturiBorderou> decodeJSONFacturiBorderou() {

		BeanFacturiBorderou oFactura = null;

		ArrayList<BeanFacturiBorderou> objectsList = new ArrayList<BeanFacturiBorderou>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject facturaObject = jsonObject.getJSONObject(i);

				oFactura = new BeanFacturiBorderou();

				oFactura.setCodFurnizor(facturaObject.getString("codFurnizor"));
				oFactura.setNumeFurnizor(facturaObject.getString("numeFurnizor"));
				oFactura.setAdresaFurnizor(facturaObject.getString("adresaFurnizor"));
				oFactura.setSosireFurnizor(facturaObject.getString("sosireFurnizor"));
				oFactura.setPlecareFurnizor(facturaObject.getString("plecareFurnizor"));

				oFactura.setCodClient(facturaObject.getString("codClient"));
				oFactura.setNumeClient(facturaObject.getString("numeClient"));
				oFactura.setAdresaClient(facturaObject.getString("adresaClient"));
				oFactura.setSosireClient(facturaObject.getString("sosireClient"));
				oFactura.setPlecareClient(facturaObject.getString("plecareClient"));

				objectsList.add(oFactura);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanEvenimentBorderou> decodeJSONEvenimentBorderou() {
		BeanEvenimentBorderou eveniment = null;
		ArrayList<BeanEvenimentBorderou> objectList = new ArrayList<BeanEvenimentBorderou>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject evenimentObject = jsonObject.getJSONObject(i);

				eveniment = new BeanEvenimentBorderou();

				eveniment.setNumeClient(evenimentObject.getString("numeClient"));
				eveniment.setCodClient(evenimentObject.getString("codClient"));
				eveniment.setOraStartCursa(evenimentObject.getString("oraStartCursa"));
				eveniment.setKmStartCursa(evenimentObject.getString("kmStartCursa"));
				eveniment.setOraSosireClient(evenimentObject.getString("oraSosireClient"));
				eveniment.setKmSosireClient(evenimentObject.getString("kmSosireClient"));
				eveniment.setOraPlecare(evenimentObject.getString("oraPlecare"));

				objectList.add(eveniment);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectList;

	}

	public ArrayList<BeanEveniment> decodeJSONEveniment() {
		BeanEveniment eveniment = null;
		ArrayList<BeanEveniment> objectList = new ArrayList<BeanEveniment>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject evenimentObject = jsonObject.getJSONObject(i);

				eveniment = new BeanEveniment();

				eveniment.setEveniment(evenimentObject.getString("eveniment"));
				eveniment.setData(evenimentObject.getString("data"));
				eveniment.setOra(evenimentObject.getString("ora"));
				eveniment.setDistantaKM(evenimentObject.getString("distantaKM"));

				objectList.add(eveniment);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectList;

	}

}
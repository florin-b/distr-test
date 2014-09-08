package com.distributie.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.distributie.beans.ArticoleFactura;
import com.distributie.beans.Borderou;
import com.distributie.beans.Eveniment;
import com.distributie.beans.EvenimentBorderou;
import com.distributie.beans.Factura;
import com.distributie.beans.InitStatus;

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

	public ArrayList<Borderou> decodeJSONBorderouri() {

		Borderou unBorderou = null;

		ArrayList<Borderou> objectsList = new ArrayList<Borderou>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject borderouObject = jsonObject.getJSONObject(i);

				unBorderou = new Borderou();
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

	public ArrayList<Factura> decodeJSONFacturiBorderou() {

		Factura oFactura = null;

		ArrayList<Factura> objectsList = new ArrayList<Factura>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject facturaObject = jsonObject.getJSONObject(i);

				oFactura = new Factura();

				oFactura.setCodFurnizor(facturaObject.getString("codFurnizor"));
				oFactura.setNumeFurnizor(facturaObject.getString("numeFurnizor"));
				oFactura.setAdresaFurnizor(facturaObject.getString("adresaFurnizor"));
				oFactura.setSosireFurnizor(facturaObject.getString("sosireFurnizor"));
				oFactura.setPlecareFurnizor(facturaObject.getString("plecareFurnizor"));
				oFactura.setCodAdresaFurnizor(facturaObject.getString("codAdresaFurnizor"));

				oFactura.setCodClient(facturaObject.getString("codClient"));
				oFactura.setNumeClient(facturaObject.getString("numeClient"));
				oFactura.setAdresaClient(facturaObject.getString("adresaClient"));
				oFactura.setSosireClient(facturaObject.getString("sosireClient"));
				oFactura.setPlecareClient(facturaObject.getString("plecareClient"));
				oFactura.setCodAdresaClient(facturaObject.getString("codAdresaClient"));

				oFactura.setDataStartCursa(facturaObject.getString("dataStartCursa"));

				objectsList.add(oFactura);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<EvenimentBorderou> decodeJSONEvenimentBorderou() {
		EvenimentBorderou eveniment = null;
		ArrayList<EvenimentBorderou> objectList = new ArrayList<EvenimentBorderou>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject evenimentObject = jsonObject.getJSONObject(i);

				eveniment = new EvenimentBorderou();

				eveniment.setNumeClient(evenimentObject.getString("numeClient"));
				eveniment.setCodClient(evenimentObject.getString("codClient"));
				eveniment.setOraStartCursa(evenimentObject.getString("oraStartCursa"));
				eveniment.setKmStartCursa(evenimentObject.getString("kmStartCursa"));
				eveniment.setOraSosireClient(evenimentObject.getString("oraSosireClient"));
				eveniment.setKmSosireClient(evenimentObject.getString("kmSosireClient"));
				eveniment.setOraPlecare(evenimentObject.getString("oraPlecare"));
				eveniment.setCodAdresa(evenimentObject.getString("codAdresa"));
				eveniment.setAdresa(evenimentObject.getString("adresa"));

				objectList.add(eveniment);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectList;

	}

	public ArrayList<Eveniment> decodeJSONEveniment() {
		Eveniment eveniment = null;
		ArrayList<Eveniment> objectList = new ArrayList<Eveniment>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject evenimentObject = jsonObject.getJSONObject(i);

				eveniment = new Eveniment();

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

	public ArrayList<ArticoleFactura> decodeJSONArticoleFactura() {
		ArticoleFactura articol = null;
		ArrayList<ArticoleFactura> objectList = new ArrayList<ArticoleFactura>();

		try {
			jsonObject = new JSONArray(JSONString);

			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject articolObject = jsonObject.getJSONObject(i);

				articol = new ArticoleFactura();

				articol.setNume(articolObject.getString("nume"));
				articol.setCantitate(articolObject.getString("cantitate"));
				articol.setUmCant(articolObject.getString("umCant"));
				articol.setTipOperatiune(articolObject.getString("tipOperatiune"));
				articol.setDepartament(articolObject.getString("departament"));
				articol.setGreutate(articolObject.getString("greutate"));
				articol.setUmGreutate(articolObject.getString("umGreutate"));

				objectList.add(articol);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectList;

	}

	public void decodeLogonInfo() {

		UserInfo userInfo = UserInfo.getInstance();
		try {

			JSONObject jsonObject = new JSONObject(JSONString);

			userInfo.setLogonStatus(jsonObject.get("status").toString());
			userInfo.setDepartament(jsonObject.get("departament").toString());
			userInfo.setFiliala(jsonObject.get("filiala").toString());
			userInfo.setTipAcces(jsonObject.get("tipAcces").toString());

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 8 - jsonObject.get("id").toString().length(); i++) {
				sb.append('0');
			}
			sb.append(jsonObject.get("id"));

			userInfo.setId(sb.toString());
			userInfo.setNume(jsonObject.get("nume").toString());
			userInfo.setUnitLog(jsonObject.get("filiala").toString());

			InitStatus initStatus = InitStatus.getInstance();

			if (!jsonObject.get("initStatus").equals(null)) {
				JSONObject jsonStatus = new JSONObject(jsonObject.get("initStatus").toString());
				initStatus.setClient(jsonStatus.get("client").toString());
				initStatus.setDocument(jsonStatus.get("document").toString());
				initStatus.setEveniment(jsonStatus.get("eveniment").toString());
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

}

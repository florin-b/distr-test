package com.distributie.model;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.distributie.beans.Etapa;
import com.distributie.beans.EvenimentNou;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiEvenimente;
import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.JavaWSListener;
import com.distributie.listeners.OperatiiBorderouriListener;

public class OperatiiBorderouriDAOImpl implements OperatiiBorderouriDAO, AsyncTaskListener, JavaWSListener {

	private Context context;
	private OperatiiBorderouriListener event;
	private EnumOperatiiEvenimente numeComanda;
	private HashMap<String, String> params;
	private EvenimentNou eveniment;

	public OperatiiBorderouriDAOImpl(Context context) {
		this.context = context;
	}

	@Override
	public void getDocEvents(String nrDoc, String tipEv) {
		params = new HashMap<String, String>();
		params.put("nrDoc", nrDoc);
		params.put("tipEv", tipEv);
		numeComanda = EnumOperatiiEvenimente.GET_DOC_EVENTS;
		performOperation();

	}

	@Override
	public void saveNewEventBorderou(HashMap<String, String> newEventData, List<Etapa> listEtape) {
		JSONOperations jsonEvLivrare = new JSONOperations(context, newEventData);
		String serializedData = jsonEvLivrare.encodeNewEventData();
		String serializedEtape = "";
		if (listEtape != null)
			serializedEtape = jsonEvLivrare.serializeOrdineLivrare(listEtape);

		params = new HashMap<String, String>();
		params.put("serializedEvent", serializedData);
		params.put("serializedEtape", serializedEtape);
		numeComanda = EnumOperatiiEvenimente.SAVE_NEW_EVENT;

		performOperation();

	}

	@Override
	public void isBorderouStarted() {
		params = new HashMap<String, String>();
		params.put("codSofer", UserInfo.getInstance().getId());
		numeComanda = EnumOperatiiEvenimente.CHECK_BORD_STARTED;
		callJavaWS();

	}

	@Override
	public void saveNewEventClient(HashMap<String, String> newEventData) {
		JSONOperations jsonEvLivrare = new JSONOperations(context, newEventData);
		String serializedData = jsonEvLivrare.encodeNewEventData();

		params = new HashMap<String, String>();
		params.put("serializedEvent", serializedData);
		numeComanda = EnumOperatiiEvenimente.SAVE_NEW_EVENT;

		performOperation();

	}

	@Override
	public void cancelEvent(HashMap<String, String> params) {
		this.params = params;
		numeComanda = EnumOperatiiEvenimente.CANCEL_EVENT;
		performOperation();
	}

	@Override
	public void getPozitieCurenta(HashMap<String, String> params) {
		this.params = params;
		numeComanda = EnumOperatiiEvenimente.GET_POZITIE;
		performOperation();

	}

	@Override
	public void saveLocalObjects() {

		LocalStorage ls = new LocalStorage(context);

		String serData = ls.getSerializedEvents();

		if (serData.length() > 0) {
			params = new HashMap<String, String>();
			params.put("serializedEvent", serData);
			numeComanda = EnumOperatiiEvenimente.SAVE_ARCHIVED_OBJECTS;

			performOperation();
		}
	}

	@Override
	public void saveNewEventClient(EvenimentNou newEvent) {

		JSONOperations jsonEvLivrare = new JSONOperations(context);

		LocalStorage storage = new LocalStorage(context);
		String serEvents = "";
		try {
			List<EvenimentNou> storedObjects = storage.getSavedEvents();
			storedObjects.add(newEvent);
			serEvents = jsonEvLivrare.serializeListEvents(storedObjects);

		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		params = new HashMap<String, String>();

		params.put("serializedEvent", serEvents);

		numeComanda = EnumOperatiiEvenimente.SAVE_NEW_EVENT;
		this.eveniment = newEvent;
		performOperation();

	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);

		if (numeComanda == EnumOperatiiEvenimente.SAVE_NEW_EVENT) {
			call.setEveniment(eveniment);
		}

		call.getCallResults();
	}

	private void callJavaWS() {

		JavaWSCall call = new JavaWSCall(numeComanda.toString(), params, (AsyncTaskListener) this, context);
		call.getCallResults();
	}

	@Override
	public void onTaskComplete(String methodName, String result, EnumNetworkStatus networkStatus) {
		if (event != null) {
			event.eventComplete((String) result, numeComanda, networkStatus);
		}

	}

	public void setEventListener(OperatiiBorderouriListener event) {
		this.event = event;
	}

	@Override
	public void onJWSComplete(String methodName, String result) {
		if (event != null) {
			event.eventComplete((String) result, numeComanda, EnumNetworkStatus.ON);
		}

	}

}

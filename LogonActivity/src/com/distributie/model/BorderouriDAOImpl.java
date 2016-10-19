package com.distributie.model;

import java.util.HashMap;

import android.content.Context;

import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiBorderou;
import com.distributie.enums.TipBorderou;
import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.BorderouriDAOListener;

public class BorderouriDAOImpl implements BorderouriDAO, AsyncTaskListener {

	private Context context;
	private BorderouriDAOListener borderouriEvents;
	private EnumOperatiiBorderou numeComanda;
	private HashMap<String, String> params;

	public BorderouriDAOImpl(Context context) {
		this.context = context;
	}

	public static BorderouriDAOImpl getInstance(Context context) {
		return new BorderouriDAOImpl(context);
	}

	@Override
	public void getBorderouri(String codSofer, String tipOp, String interval) {
		params = new HashMap<String, String>();
		params.put("codSofer", codSofer);
		params.put("tip", tipOp);
		params.put("interal", interval);
		numeComanda = EnumOperatiiBorderou.GET_BORDEROURI;
		performOperation();

	}

	@Override
	public void getFacturiBorderou(String nrBorderou, TipBorderou tipBorderou) {
		params = new HashMap<String, String>();
		params.put("nrBorderou", nrBorderou);
		params.put("tipBorderou", tipBorderou.toString());
		numeComanda = EnumOperatiiBorderou.GET_FACTURI_BORDEROU;
		performOperation();

	}

	@Override
	public void getArticoleBorderou(String nrBorderou, String codClient, String codAdresa) {
		params = new HashMap<String, String>();
		params.put("nrBorderou", nrBorderou);
		params.put("codClient", codClient);
		params.put("codAdresa", codAdresa);
		numeComanda = EnumOperatiiBorderou.GET_ARTICOLE_BORDEROU;
		performOperation();

	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this,
				context);
		call.getCallResults();
	}

	public void setBorderouEventListener(BorderouriDAOListener borderouriEvents) {
		this.borderouriEvents = borderouriEvents;
	}

	public void onTaskComplete(String methodName, String result, EnumNetworkStatus networkStatus) {
		if (borderouriEvents != null) {
			borderouriEvents.loadComplete(result, numeComanda);
		}

	}

}

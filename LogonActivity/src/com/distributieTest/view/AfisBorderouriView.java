package com.distributieTest.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.distributieTest.listeners.AsyncTaskListener;
import com.distributieTest.listeners.CustomSpinnerListener;
import com.distributieTest.model.AsyncTaskWSCall;
import com.distributieTest.model.BeanBorderou;
import com.distributieTest.model.BeanFacturiBorderou;
import com.distributieTest.model.FacturiBorderou;
import com.distributieTest.model.HandleJSONData;
import com.distributieTest.model.InfoStrings;
import com.distributieTest.model.UserInfo;

public class AfisBorderouriView implements AsyncTaskListener, CustomSpinnerListener {

	Button eventButton, showDetBtn;

	ProgressBar progressBarEvent;

	private SimpleAdapter adapterBorderouri, adapterEvenimente;
	private static ArrayList<HashMap<String, String>> listBorderouri = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> listEvenimente = new ArrayList<HashMap<String, String>>();
	Spinner spinnerBorderouri;

	ProgressWheel pw;

	private static String selectedBorderou = "0", selectedTip = "0";
	private ListView listViewEvenimente;
	private TextView textStartBorderou;

	private CustomSpinnerClass borderouClass;
	private Activity context;

	private CustomAdapter adapterFacturi;

	public AfisBorderouriView(Activity context) {
		this.context = context;
		InitialUISetup();
	}

	private void InitialUISetup() {

		try {

			textStartBorderou = (TextView) context.findViewById(R.id.textStartBorderou);
			textStartBorderou.setVisibility(View.GONE);

			pw = (ProgressWheel) context.findViewById(R.id.pw_spinner);
			pw.setVisibility(View.INVISIBLE);

			borderouClass = new CustomSpinnerClass();

			spinnerBorderouri = (Spinner) context.findViewById(R.id.spinnerBorderouri);
			listBorderouri.clear();
			adapterBorderouri = new SimpleAdapter(context, listBorderouri, R.layout.custom_row_list_borderouri,
					new String[] { "nrCrt", "codBorderou", "dataBorderou", "tipBorderou", "eveniment" }, new int[] {
							R.id.textNrCrt, R.id.textCodBorderou, R.id.textDataBorderou, R.id.textTipBorderou,
							R.id.textEvenimentBorderou });

			spinnerBorderouri.setAdapter(adapterBorderouri);
			spinnerBorderouri.setVisibility(View.INVISIBLE);
			spinnerBorderouri.setOnItemSelectedListener(borderouClass);
			borderouClass.setListener(this);

			listViewEvenimente = (ListView) context.findViewById(R.id.listEvenimente);
			listEvenimente.clear();
			adapterEvenimente = new SimpleAdapter(context, listEvenimente, R.layout.custom_row_list_evenimente,
					new String[] { "nrCrt", "numeClient", "ev1", "timpEv1", "ev2", "timpEv2", "adresaClient" },
					new int[] { R.id.textNrCrt, R.id.textNumeClient, R.id.textEv1, R.id.textTimpEv1, R.id.textEv2,
							R.id.textTimpEv2, R.id.textAdresaClient });

			listViewEvenimente.setAdapter(adapterEvenimente);

			performIncarcaBorderouri("0");

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void startSpinner() {
		pw.setVisibility(View.VISIBLE);
		pw.spin();

	}

	private void stopSpinner() {
		pw.setVisibility(View.INVISIBLE);
		pw.stopSpinning();
	}

	public void performIncarcaBorderouri(String intervalAfisare) {
		try {

			startSpinner();

			selectedBorderou = "0";
			listEvenimente.clear();
			listViewEvenimente.setAdapter(adapterEvenimente);

			listEvenimente.clear();
			listViewEvenimente.setAdapter(adapterEvenimente);
			listViewEvenimente.setVisibility(View.INVISIBLE);

			HashMap<String, String> params = new HashMap<String, String>();

			params.put("codSofer", UserInfo.getInstance().getCod());
			params.put("interval", intervalAfisare);
			params.put("tip", "t");

			AsyncTaskWSCall call = new AsyncTaskWSCall(context, (AsyncTaskListener) this, "getBorderouri", params);
			call.getCallResults2();

		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void populateListBorderouri(String borderouri) {

		HandleJSONData objListBorderouri = new HandleJSONData(context, borderouri);
		ArrayList<BeanBorderou> borderouriArray = objListBorderouri.decodeJSONBorderouri();

		listBorderouri.clear();

		if (borderouriArray.size() > 0) {

			spinnerBorderouri.setVisibility(View.VISIBLE);

			spinnerBorderouri.setEnabled(true);

			HashMap<String, String> temp;

			for (int i = 0; i < borderouriArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("nrCrt", String.valueOf(i + 1) + ".");
				temp.put("codBorderou", borderouriArray.get(i).getNumarBorderou());
				temp.put("dataBorderou", borderouriArray.get(i).getDataEmiterii());
				temp.put("tipBorderou", InfoStrings.getStringTipBorderou(borderouriArray.get(i).getTipBorderou()));
				temp.put("eveniment", borderouriArray.get(i).getEvenimentBorderou());

				listBorderouri.add(temp);

			}

		} else {
			textStartBorderou.setText("");
			Toast.makeText(context, "Nu exista borderouri!", Toast.LENGTH_SHORT).show();
		}

		spinnerBorderouri.setAdapter(adapterBorderouri);

	}

	public void performGetBorderouEvents() {

		try {
			startSpinner();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("nrBorderou", selectedBorderou);
			params.put("tipBorderou", selectedTip);
			AsyncTaskWSCall call = new AsyncTaskWSCall(context, (AsyncTaskListener) this, "getFacturiBorderou", params);
			call.getCallResults2();

		} catch (Exception ex) {

			Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();

		}

	}

	private void populateEventsList(String eventsData) {

		try {

			HandleJSONData objListFacturi = new HandleJSONData(context, eventsData);
			ArrayList<BeanFacturiBorderou> facturiArray = objListFacturi.decodeJSONFacturiBorderou();

			textStartBorderou.setVisibility(View.GONE);

			if (facturiArray.size() > 0) {

				listEvenimente.clear();
				listViewEvenimente.setVisibility(View.VISIBLE);
				String tempDataStartCursa = facturiArray.get(0).getDataStartCursa();

				FacturiBorderou facturi = new FacturiBorderou(context);
				listViewEvenimente.setAdapter(facturi.getFacturiBorderouAdapter(facturiArray, selectedTip));

				if (tempDataStartCursa.length() > 0) {
					String[] varTokStartBord = tempDataStartCursa.split(":");
					String strStartBorderou = varTokStartBord[0].substring(6, 8) + "-"
							+ varTokStartBord[0].substring(4, 6) + "-" + varTokStartBord[0].substring(0, 4) + " "
							+ varTokStartBord[1].substring(0, 2) + ":" + varTokStartBord[1].substring(2, 4);

					textStartBorderou.setText("Start borderou " + strStartBorderou);
					textStartBorderou.setVisibility(View.VISIBLE);
				}

			}

		} catch (Exception ex) {
			Toast.makeText(context, "eroare " + ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (methodName.equals("getBorderouri")) {
			stopSpinner();
			populateListBorderouri(result);
		}


		if (methodName.equals("getFacturiBorderou")) {
			stopSpinner();
			populateEventsList(result);
		}

	}

	@Override
	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map) {

		if (spinnerId == R.id.spinnerBorderouri) {
			selectedBorderou = map.get("codBorderou");
			selectedTip = map.get("tipBorderou");
			performGetBorderouEvents();
		}

	}
}
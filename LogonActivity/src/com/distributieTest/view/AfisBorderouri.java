package com.distributieTest.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.distributieTest.beans.Borderou;
import com.distributieTest.beans.Factura;

import com.distributieTest.listeners.AsyncTaskListener;
import com.distributieTest.listeners.CustomSpinnerListener;
import com.distributieTest.model.AsyncTaskWSCall;
import com.distributieTest.model.FacturiBorderou;
import com.distributieTest.model.HandleJSONData;
import com.distributieTest.model.InfoStrings;
import com.distributieTest.model.UserInfo;

public class AfisBorderouri extends Activity implements AsyncTaskListener, CustomSpinnerListener {

	private Dialog dialogSelInterval;
	private String intervalAfisare = "0";
	private AfisBorderouri afisBorderouriView;

	private SimpleAdapter adapterBorderouri, adapterEvenimente;
	private static ArrayList<HashMap<String, String>> listBorderouri = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> listEvenimente = new ArrayList<HashMap<String, String>>();

	private static String selectedBorderou = "0", selectedTip = "0";

	@InjectView(R.id.pw_spinner)
	ProgressWheel pw;
	@InjectView(R.id.listEvenimente)
	ListView listViewEvenimente;
	@InjectView(R.id.textStartBorderou)
	TextView textStartBorderou;
	@InjectView(R.id.spinnerBorderouri)
	Spinner spinnerBorderouri;

	private CustomSpinnerClass borderouClass;

	private Activity context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afiseaza_borderou);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Afisare borderou");
		actionBar.setDisplayHomeAsUpEnabled(true);

		ButterKnife.inject(this);
		InitialUISetup();

		context = this;

	}

	private void InitialUISetup() {

		try {

			textStartBorderou.setVisibility(View.GONE);
			pw.setVisibility(View.INVISIBLE);

			borderouClass = new CustomSpinnerClass();

			listBorderouri.clear();
			adapterBorderouri = new SimpleAdapter(this, listBorderouri, R.layout.custom_row_list_borderouri,
					new String[] { "nrCrt", "codBorderou", "dataBorderou", "tipBorderou", "eveniment" }, new int[] {
							R.id.textNrCrt, R.id.textCodBorderou, R.id.textDataBorderou, R.id.textTipBorderou,
							R.id.textEvenimentBorderou });

			spinnerBorderouri.setAdapter(adapterBorderouri);
			spinnerBorderouri.setVisibility(View.INVISIBLE);
			spinnerBorderouri.setOnItemSelectedListener(borderouClass);
			borderouClass.setListener(this);

			listEvenimente.clear();
			adapterEvenimente = new SimpleAdapter(this, listEvenimente, R.layout.custom_row_list_evenimente,
					new String[] { "nrCrt", "numeClient", "ev1", "timpEv1", "ev2", "timpEv2", "adresaClient" },
					new int[] { R.id.textNrCrt, R.id.textNumeClient, R.id.textEv1, R.id.textTimpEv1, R.id.textEv2,
							R.id.textTimpEv2, R.id.textAdresaClient });

			listViewEvenimente.setAdapter(adapterEvenimente);

			performIncarcaBorderouri("0");

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		createMenu(menu);
		return true;
	}

	private void createMenu(Menu menu) {

		MenuItem mnu1 = menu.add(0, 0, 0, "Interval");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:

			String[] options = { "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile" };

			dialogSelInterval = new Dialog(this);
			dialogSelInterval.setContentView(R.layout.selintervaldialogafiscmd);
			dialogSelInterval.setTitle("Afiseaza borderourile livrate ");

			Spinner spinnerSelInterval = (Spinner) dialogSelInterval.findViewById(R.id.spinnerSelInterval);

			ArrayList<HashMap<String, String>> listOptInterval = new ArrayList<HashMap<String, String>>();
			SimpleAdapter adapterOptions = new SimpleAdapter(this, listOptInterval, R.layout.customrowselinterval,
					new String[] { "optInterval" }, new int[] { R.id.textTipInterval });

			HashMap<String, String> temp;

			for (int ii = 0; ii < options.length; ii++) {
				temp = new HashMap<String, String>();
				temp.put("optInterval", options[ii]);
				listOptInterval.add(temp);
			}

			spinnerSelInterval.setAdapter(adapterOptions);

			spinnerSelInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

					if (0 == pos) {
						intervalAfisare = "0";
					}

					if (1 == pos) {
						intervalAfisare = "1";
					}

					if (2 == pos) {
						intervalAfisare = "2";
					}

				}

				public void onNothingSelected(AdapterView<?> parent) {
				}
			});

			Button btnOkInterval = (Button) dialogSelInterval.findViewById(R.id.btnOkInterval);
			btnOkInterval.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					performIncarcaBorderouri(intervalAfisare);
					dialogSelInterval.dismiss();

				}
			});

			dialogSelInterval.show();
			return true;

		case android.R.id.home:

			afisBorderouriView = null;
			Intent nextScreen = new Intent(this, MainMenu.class);
			startActivity(nextScreen);
			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onBackPressed() {

		afisBorderouriView = null;
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
		return;
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

			params.put("codSofer", UserInfo.getInstance().getId());
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
		ArrayList<Borderou> borderouriArray = objListBorderouri.decodeJSONBorderouri();

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
			ArrayList<Factura> facturiArray = objListFacturi.decodeJSONFacturiBorderou();

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
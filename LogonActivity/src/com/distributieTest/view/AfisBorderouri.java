/**
 * @author florinb
 *
 */
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
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.distributieTest.listeners.AsyncTaskListener;
import com.distributieTest.listeners.CustomSpinnerListener;
import com.distributieTest.model.AsyncTaskWSCall;
import com.distributieTest.model.BeanBorderou;
import com.distributieTest.model.BeanEvenimentBorderou;
import com.distributieTest.model.HandleJSONData;
import com.distributieTest.model.UserInfo;
import com.distributieTest.model.Utils;

public class AfisBorderouri extends Activity implements AsyncTaskListener, CustomSpinnerListener {

	Button eventButton, showDetBtn;

	ProgressBar progressBarEvent;

	private SimpleAdapter adapterBorderouri, adapterEvenimente;
	private static ArrayList<HashMap<String, String>> listBorderouri = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> listEvenimente = new ArrayList<HashMap<String, String>>();
	Spinner spinnerBorderouri;

	ProgressWheel pw;

	private Dialog dialogSelInterval;
	private String intervalAfisare = "0";
	private static String selectedBorderou = "0";
	private ListView listViewEvenimente;
	private TextView textStartBorderou;

	private CustomSpinnerClass borderouClass = new CustomSpinnerClass();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afiseaza_borderou);

		InitialUISetup();

	}

	private void InitialUISetup() {

		try {

			ActionBar actionBar = getActionBar();
			actionBar.setTitle("Afisare borderou");
			actionBar.setDisplayHomeAsUpEnabled(true);

			textStartBorderou = (TextView) findViewById(R.id.textStartBorderou);
			textStartBorderou.setVisibility(View.GONE);

			pw = (ProgressWheel) findViewById(R.id.pw_spinner);
			pw.setVisibility(View.INVISIBLE);

			spinnerBorderouri = (Spinner) findViewById(R.id.spinnerBorderouri);
			adapterBorderouri = new SimpleAdapter(this, listBorderouri, R.layout.custom_row_list_borderouri,
					new String[] { "nrCrt", "codBorderou", "dataBorderou", "nrPozitii", "eveniment" }, new int[] {
							R.id.textNrCrt, R.id.textCodBorderou, R.id.textDataBorderou, R.id.textTipBorderou,
							R.id.textEvenimentBorderou });

			spinnerBorderouri.setAdapter(adapterBorderouri);
			spinnerBorderouri.setVisibility(View.INVISIBLE);

			spinnerBorderouri.setOnItemSelectedListener(borderouClass);

			borderouClass.setListener(this);

			listViewEvenimente = (ListView) findViewById(R.id.listEvenimente);
			adapterEvenimente = new SimpleAdapter(this, listEvenimente, R.layout.custom_row_list_evenimente,
					new String[] { "nrCrt", "numeClient", "ev1", "timpEv1", "ev2", "timpEv2" }, new int[] {
							R.id.textNrCrt, R.id.textNumeClient, R.id.textEv1, R.id.textTimpEv1, R.id.textEv2,
							R.id.textTimpEv2 });

			listViewEvenimente.setAdapter(adapterEvenimente);

			performIncarcaBorderouri();

		} catch (Exception ex) {
			Toast.makeText(AfisBorderouri.this, ex.toString(), Toast.LENGTH_SHORT).show();
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

	private void startSpinner() {
		pw.setVisibility(View.VISIBLE);
		pw.spin();

	}

	private void stopSpinner() {
		pw.setVisibility(View.INVISIBLE);
		pw.stopSpinning();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:

			String[] options = { "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile" };

			dialogSelInterval = new Dialog(AfisBorderouri.this);
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

					performIncarcaBorderouri();
					dialogSelInterval.dismiss();

				}
			});

			dialogSelInterval.show();
			return true;

		case android.R.id.home:

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);
			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void performIncarcaBorderouri() {
		try {

			startSpinner();

			HashMap<String, String> params = new HashMap<String, String>();

			params.put("codSofer", UserInfo.getInstance().getCod());
			params.put("interval", intervalAfisare);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getEvenimenteBorderouri", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void populateListBorderouri(String borderouri) {

		HandleJSONData objListBorderouri = new HandleJSONData(this, borderouri);
		ArrayList<BeanBorderou> borderouriArray = objListBorderouri.decodeJSONBorderouri();

		if (borderouriArray.size() > 0) {

			spinnerBorderouri.setVisibility(View.VISIBLE);
			listBorderouri.clear();
			spinnerBorderouri.setEnabled(true);

			HashMap<String, String> temp;

			for (int i = 0; i < borderouriArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("nrCrt", String.valueOf(i + 1) + ".");
				temp.put("codBorderou", borderouriArray.get(i).getNumarBorderou());
				temp.put("dataBorderou", borderouriArray.get(i).getDataEmiterii());
				temp.put("nrPozitii", " ");
				temp.put("eveniment", borderouriArray.get(i).getEvenimentBorderou());

				listBorderouri.add(temp);
			}

			spinnerBorderouri.setAdapter(adapterBorderouri);

		} else {

			listBorderouri.clear();
			textStartBorderou.setText("");
			listEvenimente.clear();
			listViewEvenimente.setAdapter(adapterEvenimente);
			Toast.makeText(getApplicationContext(), "Nu exista borderouri!", Toast.LENGTH_SHORT).show();

		}

	}

	public void performGetBorderouEvents() {

		try {
			startSpinner();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("nrBorderou", selectedBorderou);
			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getEvenimenteBorderou", params);
			call.getCallResults();

		} catch (Exception ex) {

			Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();

		}

	}

	private void populateEventsList(String eventsData) {

		try {

			HandleJSONData objListEvenimente = new HandleJSONData(this, eventsData);
			ArrayList<BeanEvenimentBorderou> evenimenteArray = objListEvenimente.decodeJSONEvenimentBorderou();

			listEvenimente.clear();
			textStartBorderou.setVisibility(View.GONE);

			if (evenimenteArray.size() > 0) {

				HashMap<String, String> temp;

				String[] tokenStartClient, varTokStartBord;
				String strStartBorderou = "";
				double tripDistance = 0;

				for (int i = 0; i < evenimenteArray.size(); i++) {

					temp = new HashMap<String, String>();

					temp.put("nrCrt", String.valueOf(i + 1) + ".");
					temp.put("numeClient", evenimenteArray.get(i).getNumeClient());
					temp.put("codClient", evenimenteArray.get(i).getCodClient());

					temp.put("ev1", "Sosire:");
					temp.put("ev2", "Plecare:");

					if (!evenimenteArray.get(i).getOraStartCursa().equals("0")) {
						varTokStartBord = evenimenteArray.get(i).getOraStartCursa().split(":");
						strStartBorderou = varTokStartBord[0].substring(6, 8) + "-"
								+ varTokStartBord[0].substring(4, 6) + "-" + varTokStartBord[0].substring(0, 4) + " "
								+ varTokStartBord[1].substring(0, 2) + ":" + varTokStartBord[1].substring(2, 4);
					}

					if (!evenimenteArray.get(i).getOraSosireClient().equals("0")) {
						tokenStartClient = evenimenteArray.get(i).getOraSosireClient().split(":");
						tripDistance = Double.valueOf(evenimenteArray.get(i).getKmSosireClient())
								- Double.valueOf(evenimenteArray.get(i).getKmStartCursa());

						temp.put(
								"timpEv1",
								tokenStartClient[1].substring(0, 2)
										+ ":"
										+ tokenStartClient[1].substring(2, 4)
										+ " , "
										+ Utils.getDuration2(evenimenteArray.get(i).getOraStartCursa(), evenimenteArray
												.get(i).getOraSosireClient()) + " , "
										+ String.format("%.2f", tripDistance) + " km");
					}

					if (!evenimenteArray.get(i).getOraPlecare().equals("0")) {
						temp.put("timpEv2", evenimenteArray.get(i).getOraPlecare().substring(0, 2) + ":"
								+ evenimenteArray.get(i).getOraPlecare().substring(2, 4));
					}

					listEvenimente.add(temp);
				}

				textStartBorderou.setText("Start borderou " + strStartBorderou);
				textStartBorderou.setVisibility(View.VISIBLE);

				listViewEvenimente.setAdapter(adapterEvenimente);

			} else {

			}

		} catch (Exception ex) {
			Toast.makeText(AfisBorderouri.this, "eroare " + ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onBackPressed() {

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
		return;
	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (methodName.equals("getEvenimenteBorderouri")) {
			stopSpinner();
			populateListBorderouri(result);
		}

		if (methodName.equals("getEvenimenteBorderou")) {
			stopSpinner();
			populateEventsList(result);
		}

	}

	@Override
	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map) {

		if (spinnerId == R.id.spinnerBorderouri) {
			selectedBorderou = map.get("codBorderou");
			performGetBorderouEvents();
		}

	}

}

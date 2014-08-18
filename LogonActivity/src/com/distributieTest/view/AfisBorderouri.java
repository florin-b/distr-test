/**
 * @author florinb
 *
 */
package com.distributieTest.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;



import java.util.concurrent.TimeUnit;

import com.distributieTest.listeners.AsyncTaskListener;
import com.distributieTest.listeners.CustomSpinnerListener;
import com.distributieTest.model.AsyncTaskWSCall;
import com.distributieTest.model.UserInfo;
import com.distributieTest.view.R;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

		if (borderouri.contains("@@")) {

			spinnerBorderouri.setVisibility(View.VISIBLE);

			listBorderouri.clear();
			spinnerBorderouri.setEnabled(true);

			HashMap<String, String> temp;
			String[] tokenLinie = borderouri.split("@@");
			String[] tokenBorderou;
			String client = "";

			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>();
				client = tokenLinie[i];
				tokenBorderou = client.split("#");

				temp.put("nrCrt", String.valueOf(i + 1) + ".");
				temp.put("codBorderou", tokenBorderou[0]);
				temp.put("dataBorderou", tokenBorderou[1]);
				temp.put("nrPozitii", " ");
				temp.put("eveniment", tokenBorderou[2]);

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

			listEvenimente.clear();
			textStartBorderou.setVisibility(View.GONE);

			if (eventsData.contains("@@")) {

				HashMap<String, String> temp;
				String[] tokenLinie = eventsData.split("@@");
				String[] tokenEveniment, tokenStartClient, varTokStartBord;
				String eveniment = "", strStartBorderou = "";
				double tripDistance = 0;

				for (int i = 0; i < tokenLinie.length; i++) {
					temp = new HashMap<String, String>();
					eveniment = tokenLinie[i];
					tokenEveniment = eveniment.split("#");

					temp.put("nrCrt", String.valueOf(i + 1) + ".");
					temp.put("numeClient", tokenEveniment[0]);
					temp.put("codClient", tokenEveniment[1]);

					temp.put("ev1", "Sosire:");
					temp.put("ev2", "Plecare:");

					if (!tokenEveniment[2].equals("0")) {
						varTokStartBord = tokenEveniment[2].split(":");
						strStartBorderou = varTokStartBord[0].substring(6, 8) + "-"
								+ varTokStartBord[0].substring(4, 6) + "-" + varTokStartBord[0].substring(0, 4) + " "
								+ varTokStartBord[1].substring(0, 2) + ":" + varTokStartBord[1].substring(2, 4);
					}

					if (!tokenEveniment[4].equals("0")) {
						tokenStartClient = tokenEveniment[4].split(":");
						tripDistance = Double.valueOf(tokenEveniment[5]) - Double.valueOf(tokenEveniment[3]);

						temp.put(
								"timpEv1",
								tokenStartClient[1].substring(0, 2) + ":" + tokenStartClient[1].substring(2, 4) + " , "
										+ getDuration2(tokenEveniment[2], tokenEveniment[4]) + " , "
										+ String.format("%.2f", tripDistance) + " km");
					}

					if (!tokenEveniment[6].equals("0")) {
						temp.put("timpEv2", tokenEveniment[6].substring(0, 2) + ":" + tokenEveniment[6].substring(2, 4));
					}

					listEvenimente.add(temp);
				}

				textStartBorderou.setText("Start borderou " + strStartBorderou);
				textStartBorderou.setVisibility(View.VISIBLE);

				listViewEvenimente.setAdapter(adapterEvenimente);

			} else {

			}

		} catch (Exception ex) {
			Toast.makeText(AfisBorderouri.this, "erorare " + ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private static String getDuration2(String dataStart, String dataStop) {
		String strDuration = "";

		String[] tokenStart = dataStart.split(":");
		String[] tokenStop = dataStop.split(":");

		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, Integer.valueOf(tokenStart[0].substring(0, 4)));
		cal1.set(Calendar.MONTH, getMonthNumber(tokenStart[0].substring(4, 6)));
		cal1.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tokenStart[0].substring(6, 8)));
		cal1.set(Calendar.HOUR, Integer.valueOf(tokenStart[1].substring(0, 2)));
		cal1.set(Calendar.MINUTE, Integer.valueOf(tokenStart[1].substring(2, 4)));

		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.YEAR, Integer.valueOf(tokenStop[0].substring(0, 4)));
		cal2.set(Calendar.MONTH, getMonthNumber(tokenStop[0].substring(4, 6)));
		cal2.set(Calendar.DAY_OF_MONTH, Integer.valueOf(tokenStop[0].substring(6, 8)));
		cal2.set(Calendar.HOUR, Integer.valueOf(tokenStop[1].substring(0, 2)));
		cal2.set(Calendar.MINUTE, Integer.valueOf(tokenStop[1].substring(2, 4)));

		long milisecs = cal2.getTimeInMillis() - cal1.getTimeInMillis();

		strDuration = getDuration(milisecs);

		return strDuration;
	}

	private static int getMonthNumber(String monthName) {
		int monthNumber = 0;

		if (monthName.equals("JAN")) {
			monthNumber = 1;
		}

		if (monthName.equals("FEB")) {
			monthNumber = 2;
		}

		if (monthName.equals("MAR")) {
			monthNumber = 3;
		}

		if (monthName.equals("APR")) {
			monthNumber = 4;
		}

		if (monthName.equals("MAY")) {
			monthNumber = 5;
		}

		if (monthName.equals("JUN")) {
			monthNumber = 6;
		}

		if (monthName.equals("JUL")) {
			monthNumber = 7;
		}

		if (monthName.equals("AUG")) {
			monthNumber = 8;
		}

		if (monthName.equals("SEP")) {
			monthNumber = 9;
		}

		if (monthName.equals("OCT")) {
			monthNumber = 10;
		}

		if (monthName.equals("NOV")) {
			monthNumber = 11;
		}

		if (monthName.equals("DEC")) {
			monthNumber = 12;
		}

		return monthNumber;

	}

	public static String getDuration(long millis) {

		StringBuilder sb = new StringBuilder(64);

		if (millis > 0) {

			long days = TimeUnit.MILLISECONDS.toDays(millis);
			millis -= TimeUnit.DAYS.toMillis(days);
			long hours = TimeUnit.MILLISECONDS.toHours(millis);
			millis -= TimeUnit.HOURS.toMillis(hours);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
			millis -= TimeUnit.MINUTES.toMillis(minutes);

			if (days > 0) {
				sb.append(days);
				sb.append(" Zile ");
			}
			if (hours > 0) {
				sb.append(hours);
				sb.append(" Ore ");
			}
			if (minutes > 0) {
				sb.append(minutes);
				sb.append(" Minute ");
			}
		}
		return (sb.toString());
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

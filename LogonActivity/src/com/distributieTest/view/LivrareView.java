/**
 * @author florinb
 *
 */
package com.distributieTest.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

import com.distributieTest.listeners.AsyncTaskListener;
import com.distributieTest.model.AsyncTaskWSCall;
import com.distributieTest.model.BeanFacturiBorderou;

import com.distributieTest.model.EncodeJSONData;
import com.distributieTest.model.HandleJSONData;
import com.distributieTest.model.InfoStrings;
import com.distributieTest.model.UserInfo;

public class LivrareView implements AsyncTaskListener {

	Button showDetBtn, saveEventClienti, showArticoleLivrareBtn;

	ProgressBar progressBar;
	private Timer myEventTimer;
	private int progressVal = 0;
	private Handler eventHandler = new Handler();

	private CustomAdapter adapterFacturi;
	private static ArrayList<HashMap<String, String>> arrayListFacturi = new ArrayList<HashMap<String, String>>();
	ListView listFacturi, listViewArtLivrare;
	TextView textSelectedBorderou, textSelectedClient, textSelectedClientArt, textAdresaClient;
	ProgressWheel pw;

	public static ArrayList<HashMap<String, String>> arrayListArtLivr = null;

	int selectedPosition = -1;
	LinearLayout selectedClientLayout;

	ArticoleLivrareAdapter adapterArtLivr;

	private HashMap<String, String> artMap = null;
	SlidingDrawer drawerArtLivrare;
	private ArrayList<BeanFacturiBorderou> facturiArray;

	private String selectedClientCode = "", selectedClientName = "", selectedClientAddr = "";

	private Activity context;

	/*
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * 
	 * setTheme(R.style.LRTheme); setContentView(R.layout.livrare);
	 * 
	 * InitialUISetup();
	 * 
	 * }
	 */

	public LivrareView(Activity context) {
		this.context = context;
		InitialUISetup();
	}

	private void InitialUISetup() {

		try {

			pw = (ProgressWheel) context.findViewById(R.id.pw_spinner);
			pw.setVisibility(View.INVISIBLE);

			textSelectedClient = (TextView) context.findViewById(R.id.textSelectedClient);
			textSelectedClient.setText("");
			textSelectedClient.setVisibility(View.INVISIBLE);

			textSelectedClientArt = (TextView) context.findViewById(R.id.textSelectedClientArt);
			textSelectedClientArt.setText("");

			textAdresaClient = (TextView) context.findViewById(R.id.textAdresaClient);
			textAdresaClient.setText("");

			progressBar = (ProgressBar) context.findViewById(R.id.progress_bar_event);
			progressBar.setVisibility(View.INVISIBLE);

			saveEventClienti = (Button) context.findViewById(R.id.saveEventClienti);
			saveEventClienti.setVisibility(View.INVISIBLE);
			// saveEventClienti.setOnTouchListener(new
			// myEvtClientiOnTouchListener());

			selectedClientLayout = (LinearLayout) context.findViewById(R.id.selectedClientLayout);
			selectedClientLayout.setVisibility(View.INVISIBLE);

			listFacturi = (ListView) context.findViewById(R.id.listFacturiBorderou);

			listFacturi.setVisibility(View.INVISIBLE);

			showArticoleLivrareBtn = (Button) context.findViewById(R.id.showArticoleLivrareBtn);

			if (InfoStrings.getTipBorderou(context).toLowerCase(Locale.getDefault()).equals("aprovizionare")) {
				showArticoleLivrareBtn.setVisibility(View.VISIBLE);
				showArticoleLivrareBtn.setOnClickListener(new myArtLivrBtnOnClickListener());

			} else {
				showArticoleLivrareBtn.setVisibility(View.GONE);
			}

			if (InfoStrings.getNrBorderou(context).equals("0")) {
				Toast.makeText(context, "Selectati un borderou!", Toast.LENGTH_LONG).show();

			} else {

				if (InfoStrings.getEveniment(context).equals("0")) {
					Toast.makeText(context, "Marcati plecarea in cursa pentru un borderou!", Toast.LENGTH_LONG).show();
				} else {
					performLoadFacturiBorderou();
					listFacturi.setOnItemClickListener(new myListFacturiOnItemClickListener());

					textSelectedBorderou = (TextView) context.findViewById(R.id.textSelectedBorderou);
					textSelectedBorderou.setText(" Borderou " + InfoStrings.getNrBorderou(context) + " ");
				}
			}

			arrayListArtLivr = new ArrayList<HashMap<String, String>>();

			adapterArtLivr = new ArticoleLivrareAdapter(context, arrayListArtLivr, R.layout.custom_row_list_articole,
					new String[] { "nrCrt", "numeArticol", "cantitate", "unitMas", "tipOp", "greutate", "umGreutate" },
					new int[] { R.id.textNrCrt, R.id.textNumeArticol, R.id.textCantitate, R.id.textUnitMas,
							R.id.textTipOp, R.id.textGreutate, R.id.textUnitMasGreutate });

			drawerArtLivrare = (SlidingDrawer) context.findViewById(R.id.articoleLivrareDrawer);
			drawerArtLivrare.setVisibility(View.GONE);

			drawerArtLivrareListener();
			listViewArtLivrare = (ListView) context.findViewById(R.id.listViewArtLivrare);
			listViewArtLivrare.setAdapter(adapterArtLivr);

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public void addEventListener(OnTouchListener touchListener) {
		saveEventClienti.setOnTouchListener(touchListener);
	}

	private void startSpinner() {
		saveEventClienti.setEnabled(false);
		pw.setVisibility(View.VISIBLE);
		pw.spin();

	}

	private void stopSpinner() {
		pw.setVisibility(View.INVISIBLE);
		pw.stopSpinning();
		saveEventClienti.setEnabled(true);
	}

	public void drawerArtLivrareListener() {

		drawerArtLivrare.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				drawerArtLivrare.setVisibility(View.VISIBLE);

			}
		});

		drawerArtLivrare.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				drawerArtLivrare.setVisibility(View.GONE);

			}
		});

	}

	private void performLoadFacturiBorderou() {

		startSpinner();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrBorderou", InfoStrings.getNrBorderou(context));
		params.put("tipBorderou", InfoStrings.getTipBorderou(context));
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, (AsyncTaskListener) LivrareView.this, "getFacturiBorderou",
				params);
		call.getCallResults2();

	}

	private void populateListFacturi(String facturi) {

		HandleJSONData objListFacturi = new HandleJSONData(context, facturi);
		facturiArray = objListFacturi.decodeJSONFacturiBorderou();

		if (facturiArray.size() > 0) {

			arrayListFacturi.clear();
			int locatIntSelectedClient = -1;

			textSelectedClient.setText("");
			textAdresaClient.setText("");
			saveEventClienti.setVisibility(View.INVISIBLE);

			listFacturi.setVisibility(View.VISIBLE);

			HashMap<String, String> temp = null;

			if (InfoStrings.getTipBorderou(context).toLowerCase(Locale.getDefault()).equals("distributie")) {

				adapterFacturi = new CustomAdapter(context, arrayListFacturi, R.layout.custom_row_list_facturi,
						new String[] { "nrCrt", "numeClient", "codClient", "adresaClient", "ev1", "timpEv1", "ev2",
								"timpEv2" }, new int[] { R.id.textNrCrt, R.id.textNumeClient, R.id.textAdresaClient,
								R.id.textCodClient, R.id.textEv1, R.id.textTimpEv1, R.id.textEv2, R.id.textTimpEv2 });
				listFacturi.setAdapter(adapterFacturi);

				for (int i = 0; i < facturiArray.size(); i++) {
					temp = new HashMap<String, String>();

					temp.put("nrCrt", String.valueOf(i + 1) + ".");
					temp.put("numeClient", facturiArray.get(i).getNumeClient());
					temp.put("codClient", facturiArray.get(i).getCodClient());
					temp.put("adresaClient", facturiArray.get(i).getAdresaClient());

					if (!facturiArray.get(i).getSosireClient().equals("0")) {
						temp.put("ev1", "Sosire:");
						temp.put("timpEv1", facturiArray.get(i).getSosireClient().substring(0, 2) + ":"
								+ facturiArray.get(i).getSosireClient().substring(2, 4) + ":"
								+ facturiArray.get(i).getSosireClient().substring(4, 6));
					} else {
						temp.put("ev1", " ");
						temp.put("timpEv1", " ");
					}

					if (!facturiArray.get(i).getPlecareClient().equals("0")) {
						temp.put("ev2", "Plecare:");
						temp.put("timpEv2", facturiArray.get(i).getPlecareClient().substring(0, 2) + ":"
								+ facturiArray.get(i).getPlecareClient().substring(2, 4) + ":"
								+ facturiArray.get(i).getPlecareClient().substring(4, 6));
					} else {

						temp.put("ev2", " ");
						temp.put("timpEv2", " ");
					}

					if (isClientSelected(facturiArray, i)) {
						locatIntSelectedClient = i;
					}

					arrayListFacturi.add(temp);
				}

			}// sf. distributie

			// aprovizionare
			if (InfoStrings.getTipBorderou(context).toLowerCase(Locale.getDefault()).equals("aprovizionare")) {

				adapterFacturi = new CustomAdapter(context, arrayListFacturi, R.layout.custom_row_list_facturi_aprov,
						new String[] { "nrCrt", "numeClient", "codClient", "ev1", "timpEv1", "ev2", "timpEv2",
								"adresaClient" }, new int[] { R.id.textNrCrt, R.id.textNumeClient, R.id.textCodClient,
								R.id.textEv1, R.id.textTimpEv1, R.id.textEv2, R.id.textTimpEv2, R.id.textAdresaClient });
				listFacturi.setAdapter(adapterFacturi);

				int lastIndex = 1;

				for (int i = 0; i < facturiArray.size(); i++) {
					temp = new HashMap<String, String>();

					if (0 == i) {

						temp.put("nrCrt", String.valueOf(lastIndex) + ".");
						lastIndex++;
						temp.put("numeClient", facturiArray.get(i).getNumeFurnizor());
						temp.put("codClient", facturiArray.get(i).getCodFurnizor());
						temp.put("adresaClient", facturiArray.get(i).getAdresaFurnizor());

						if (!facturiArray.get(i).getSosireFurnizor().equals("0")) {
							temp.put("ev1", "Sosire:");
							temp.put("timpEv1",
									facturiArray.get(i).getSosireFurnizor().substring(0, 2) + ":"
											+ facturiArray.get(i).getSosireFurnizor().substring(2, 4) + ":"
											+ facturiArray.get(i).getSosireFurnizor().substring(4, 6));
						} else {
							temp.put("ev1", " ");
							temp.put("timpEv1", " ");
						}

						if (!facturiArray.get(i).getPlecareFurnizor().equals("0")) {
							temp.put("ev2", "Plecare:");
							temp.put("timpEv2", facturiArray.get(i).getPlecareFurnizor().substring(0, 2) + ":"
									+ facturiArray.get(i).getPlecareFurnizor().substring(2, 4) + ":"
									+ facturiArray.get(i).getPlecareFurnizor().substring(4, 6));
						} else {

							temp.put("ev2", " ");
							temp.put("timpEv2", " ");
						}

						arrayListFacturi.add(temp);

						temp = new HashMap<String, String>();

						temp.put("nrCrt", String.valueOf(lastIndex) + ".");
						lastIndex++;
						temp.put("numeClient", facturiArray.get(i).getNumeClient());
						temp.put("codClient", facturiArray.get(i).getCodClient());
						temp.put("adresaClient", facturiArray.get(i).getAdresaClient());

						if (!facturiArray.get(i).getSosireClient().equals("0")) {
							temp.put("ev1", "Sosire:");
							temp.put("timpEv1", facturiArray.get(i).getSosireClient().substring(0, 2) + ":"
									+ facturiArray.get(i).getSosireClient().substring(2, 4) + ":"
									+ facturiArray.get(i).getSosireClient().substring(4, 6));
						} else {
							temp.put("ev1", " ");
							temp.put("timpEv1", " ");
						}

						if (!facturiArray.get(i).getPlecareClient().equals("0")) {
							temp.put("ev2", "Plecare:");
							temp.put("timpEv2", facturiArray.get(i).getPlecareClient().substring(0, 2) + ":"
									+ facturiArray.get(i).getPlecareClient().substring(2, 4) + ":"
									+ facturiArray.get(i).getPlecareClient().substring(4, 6));
						} else {

							temp.put("ev2", " ");
							temp.put("timpEv2", " ");
						}

						arrayListFacturi.add(temp);

					} else {

						temp.put("nrCrt", String.valueOf(lastIndex) + ".");
						lastIndex++;

						temp.put("numeClient", facturiArray.get(i).getNumeClient());
						temp.put("codClient", facturiArray.get(i).getCodClient());
						temp.put("adresaClient", facturiArray.get(i).getAdresaClient());

						if (!facturiArray.get(i).getSosireClient().equals("0")) {
							temp.put("ev1", "Sosire:");
							temp.put("timpEv1", facturiArray.get(i).getSosireClient().substring(0, 2) + ":"
									+ facturiArray.get(i).getSosireClient().substring(2, 4) + ":"
									+ facturiArray.get(i).getSosireClient().substring(4, 6));
						} else {
							temp.put("ev1", " ");
							temp.put("timpEv1", " ");
						}

						if (!facturiArray.get(i).getPlecareClient().equals("0")) {
							temp.put("ev2", "Plecare:");
							temp.put("timpEv2", facturiArray.get(i).getPlecareClient().substring(0, 2) + ":"
									+ facturiArray.get(i).getPlecareClient().substring(2, 4) + ":"
									+ facturiArray.get(i).getPlecareClient().substring(4, 6));
						} else {

							temp.put("ev2", " ");
							temp.put("timpEv2", " ");
						}

						arrayListFacturi.add(temp);

					}

					if (InfoStrings.getCurentClient(context).equals(facturiArray.get(i).getCodFurnizor())) {
						locatIntSelectedClient = i;
					}

				}

			}// sf. aprovizionare

			listFacturi.setAdapter(adapterFacturi);

			if (locatIntSelectedClient != -1) {

				listFacturi.setItemChecked(locatIntSelectedClient, true);
				listFacturi.performItemClick(listFacturi, locatIntSelectedClient,
						listFacturi.getItemIdAtPosition(locatIntSelectedClient));

			}

		} else {
			Toast.makeText(context, "Nu exista facturi!", Toast.LENGTH_LONG).show();
		}

	}

	boolean isClientSelected(ArrayList<BeanFacturiBorderou> facturiArray, int pos) {
		return InfoStrings.getCurentClient(context).equals(facturiArray.get(pos).getCodClient())
				&& InfoStrings.getCurentClientAddr(context).equals(facturiArray.get(pos).getCodAdresaClient());
	}

	class myListFacturiOnItemClickListener implements ListView.OnItemClickListener {

		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			try {

				if (isClientSelectable(position)) {

					selectedClientLayout.setVisibility(View.VISIBLE);

					artMap = (HashMap<String, String>) adapterFacturi.getItem(position);

					if (selectedPosition != -1) {
						if (parent.getChildAt(selectedPosition) != null)
							parent.getChildAt(selectedPosition).setBackgroundColor(
									context.getResources().getColor(R.color.rowColor9));
					}

					selectedClientCode = artMap.get("codClient");
					selectedClientName = artMap.get("numeClient");
					selectedClientAddr = facturiArray.get(position).getCodAdresaClient();

					InfoStrings.setCurentClient(context, selectedClientCode);
					InfoStrings.setCurentClientAddr(context, selectedClientAddr);
					InfoStrings.setCurentClientName(context, selectedClientName);

					selectedPosition = position;

					view.setBackgroundColor(context.getResources().getColor(R.color.rowColor8));

					textSelectedClient.setVisibility(View.VISIBLE);

					String localStrEvClnt = "0";
					if (artMap.get("ev1").trim().length() > 0)
						localStrEvClnt = artMap.get("ev1").substring(0, 1);

					InfoStrings.setEvenimentClient(context, localStrEvClnt);

					if (localStrEvClnt.equals("0")) {
						saveEventClienti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.in1, 0, 0, 0);
						saveEventClienti.setText("SOSIRE");
					} else {
						saveEventClienti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.out1, 0, 0, 0);
						saveEventClienti.setText("PLECARE");
					}

					textAdresaClient.setText(facturiArray.get(position).getAdresaClient());
					textSelectedClient.setText(artMap.get("numeClient"));
					saveEventClienti.setVisibility(View.VISIBLE);

				}

			} catch (Exception ex) {
				Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
			}

		}

	}

	@SuppressWarnings("unchecked")
	private boolean isClientSelectable(int position) {

		int ii = 0;

		boolean allOpen = true, isSelectable = false;
		boolean varClearPos = false, varOpenedPos = false;

		ArrayList<Integer> clearPos = new ArrayList<Integer>();
		ArrayList<Integer> openedPos = new ArrayList<Integer>();
		ArrayList<Integer> closedPos = new ArrayList<Integer>();

		for (ii = 0; ii < arrayListFacturi.size(); ii++) {

			artMap = (HashMap<String, String>) adapterFacturi.getItem(ii);

			// toate pozitiile sunt deschise
			if (artMap.get("ev1").toString().trim().length() == 0 && artMap.get("ev2").toString().trim().length() == 0) {
				if (allOpen)
					allOpen = true;
			} else {
				allOpen = false;
			}

			// pozitii libere
			if (artMap.get("ev1").toString().trim().length() == 0 && artMap.get("ev2").toString().trim().length() == 0) {
				clearPos.add(ii);
			}

			// pozitii deschise
			if (artMap.get("ev1").toString().trim().length() != 0 && artMap.get("ev2").toString().trim().length() == 0) {
				openedPos.add(ii);
			}

			// pozitii inchise
			if (artMap.get("ev1").toString().trim().length() != 0 && artMap.get("ev2").toString().trim().length() != 0) {
				closedPos.add(ii);
			}

		}

		if (clearPos.size() > 0) {
			for (int i = 0; i < clearPos.size(); i++)
				if (position == clearPos.get(i)) {
					varClearPos = true;
				}
		}

		if (openedPos.size() > 0) {
			for (int i = 0; i < openedPos.size(); i++)
				if (position == openedPos.get(i)) {
					varOpenedPos = true;
				}

		}

		// toate pozitiile sunt libere
		if (allOpen) {
			isSelectable = true;
		}

		// pozitia selectata este deschisa
		if (varOpenedPos) {
			isSelectable = true;
		}

		// pozitia selectata este pozitie libera si nu exista pozitii deschise
		if (varClearPos && openedPos.size() == 0) {
			isSelectable = true;
		}

		return isSelectable;

	}

	class myEvtClientiOnTouchListener implements Button.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			try {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					if (!InfoStrings.isGPSEnabled(v.getContext())) {
						InfoStrings.showGPSDisabledAlert(v.getContext());
					} else {

						buttonActionUp();
					}
					return true;

				case MotionEvent.ACTION_UP:
					buttonActionDown();
					return true;

				}
			} catch (Exception ex) {
				Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
			}

			return false;
		}

	}

	public void buttonActionUp() {
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setProgress(0);
		progressVal = 0;
		myEventTimer = new Timer();
		myEventTimer.schedule(new UpdateProgress(), 40, 25);
	}

	public void buttonActionDown() {
		if (progressBar.getVisibility() == View.VISIBLE) {
			myEventTimer.cancel();
			progressBar.setVisibility(View.INVISIBLE);

		}
	}

	private String getTruckServiceData() {
		String truckParams = "0@0";

		try {
			Context con;
			SharedPreferences pref;

			con = context.createPackageContext("com.android.dataservice", 0);
			pref = con.getSharedPreferences("TRUCK_DATA", Context.MODE_MULTI_PROCESS);

			truckParams = pref.getString("params", "0@0");

		} catch (Exception ex) {
			truckParams = "0@0";
			Log.e("Error", ex.toString());
		}

		return truckParams;

	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (progressBar.getProgress() == 50) {
				eventHandler.post(new Runnable() {
					public void run() {

						progressBar.setVisibility(View.INVISIBLE);
						performSaveNewEventClienti();

					}
				});

				myEventTimer.cancel();
			} else {
				progressBar.setProgress(progressVal);

			}

		}
	}

	private void performSaveNewEventClienti() {

		try {

			startSpinner();

			HashMap<String, String> newEventData = new HashMap<String, String>();
			newEventData.put("codSofer", UserInfo.getInstance().getCod());
			newEventData.put("document", InfoStrings.getNrBorderou(context));
			newEventData.put("client", InfoStrings.getCurentClient(context));
			newEventData.put("codAdresa", InfoStrings.getCurentClientAddr(context));
			newEventData.put("eveniment", InfoStrings.getEvenimentClient(context));
			newEventData.put("truckData", getTruckServiceData());

			EncodeJSONData jsonEvLivrare = new EncodeJSONData(context, newEventData);
			String serializedData = jsonEvLivrare.encodeNewEventData();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("serializedEvent", serializedData);

			AsyncTaskWSCall call = new AsyncTaskWSCall(context, (AsyncTaskListener) LivrareView.this, "saveNewEvent",
					params);
			call.getCallResults2();

		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			stopSpinner();
		}

	}

	class myArtLivrBtnOnClickListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			startSpinner();
			performGetArticoleDocument();
		}

	}

	private void performGetArticoleDocument() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			params.put("nrBorderou", InfoStrings.getNrBorderou(context));
			params.put("codClient", InfoStrings.getCurentClient(context));

			AsyncTaskWSCall call = new AsyncTaskWSCall(context, (AsyncTaskListener) LivrareView.this,
					"getArticoleBorderou", params);
			call.getCallResults2();

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void displayArticoleData(String articoleData) {

		if (!articoleData.equals("-1")) {
			arrayListArtLivr.clear();

			boolean isDescarcat = false, isIncarcat = false;

			String[] mainToken = articoleData.split("@@");
			String[] tokenLinie;

			HashMap<String, String> temp = null;

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			double totalMasaElectriceDescarcare = 0, totalMasaFeronerieDescarcare = 0;
			double totalMasaElectriceIncarcare = 0, totalMasaFeronerieIncarcare = 0;
			double valMasa = 0, valMasaIncarcare = 0, valMasaDescarcare = 0;
			int nrCrt = 1;

			for (int i = 0; i < mainToken.length; i++) {

				tokenLinie = mainToken[i].split("#");
				valMasa = 0;

				if (tokenLinie[3].equals("descarcare")) {

					if (!isDescarcat) {
						temp = new HashMap<String, String>();
						temp.put("nrCrt", " ");
						temp.put("numeArticol", " DESCARCARE ");
						temp.put("cantitate", " ");
						temp.put("unitMas", " ");
						temp.put("tipOp", "descarcare");
						temp.put("greutate", " ");
						temp.put("umGreutate", " ");
						arrayListArtLivr.add(temp);

						isDescarcat = true;
					}

					if (tokenLinie[6].toUpperCase(Locale.getDefault()).equals("G")) {
						valMasa = Double.parseDouble(tokenLinie[5]) / 1000;
					}

					if (tokenLinie[6].toUpperCase(Locale.getDefault()).equals("KG")) {
						valMasa = Double.parseDouble(tokenLinie[5]);
					}

					if (tokenLinie[6].toUpperCase(Locale.getDefault()).equals("T")) {
						valMasa = Double.parseDouble(tokenLinie[5]) * 1000;
					}

					// depart. feronerie
					if (tokenLinie[4].equals("02")) {
						totalMasaFeronerieDescarcare += valMasa;
					}

					// depart electrice
					if (tokenLinie[4].equals("05")) {
						totalMasaElectriceDescarcare += valMasa;
					}

					if (!tokenLinie[4].equals("05") && !tokenLinie[4].equals("02")) {
						temp = new HashMap<String, String>();
						temp.put("nrCrt", String.valueOf(nrCrt) + ".");
						temp.put("numeArticol", tokenLinie[0]);
						temp.put("cantitate", tokenLinie[1]);
						temp.put("unitMas", tokenLinie[2]);
						temp.put("tipOp", tokenLinie[3]);
						temp.put("greutate", nf2.format(Double.valueOf(tokenLinie[5])));
						temp.put("umGreutate", tokenLinie[6]);

						arrayListArtLivr.add(temp);

						valMasaDescarcare += valMasa;
						nrCrt++;
					}
				}

				if (tokenLinie[3].equals("incarcare")) {

					if (!isIncarcat) {

						temp = new HashMap<String, String>();
						temp.put("nrCrt", " ");
						temp.put("numeArticol", " INCARCARE ");
						temp.put("cantitate", " ");
						temp.put("unitMas", " ");
						temp.put("tipOp", "incarcare");
						temp.put("greutate", " ");
						temp.put("umGreutate", " ");

						arrayListArtLivr.add(temp);

						isIncarcat = true;

					}

					if (tokenLinie[6].toUpperCase(Locale.getDefault()).equals("G")) {
						valMasa = Double.parseDouble(tokenLinie[5]) / 1000;
					}

					if (tokenLinie[6].toUpperCase(Locale.getDefault()).equals("KG")) {
						valMasa = Double.parseDouble(tokenLinie[5]);
					}

					if (tokenLinie[6].toUpperCase(Locale.getDefault()).equals("T")) {
						valMasa = Double.parseDouble(tokenLinie[5]) * 1000;
					}

					// depart. feronerie
					if (tokenLinie[4].equals("02")) {
						totalMasaFeronerieIncarcare += valMasa;
					}

					// depart electrice
					if (tokenLinie[4].equals("05")) {
						totalMasaElectriceIncarcare += valMasa;
					}

					if (!tokenLinie[4].equals("05") && !tokenLinie[4].equals("02")) {
						temp = new HashMap<String, String>();
						temp.put("nrCrt", String.valueOf(nrCrt) + ".");
						temp.put("numeArticol", tokenLinie[0]);
						temp.put("cantitate", tokenLinie[1]);
						temp.put("unitMas", tokenLinie[2]);
						temp.put("tipOp", tokenLinie[3]);
						temp.put("greutate", nf2.format(Double.valueOf(tokenLinie[5])));
						temp.put("umGreutate", tokenLinie[6]);
						arrayListArtLivr.add(temp);

						valMasaIncarcare += valMasa;
						nrCrt++;
					}
				}

			}

			// adaugare linie total feronerie incarcare
			if (totalMasaFeronerieIncarcare > 0) {
				temp = new HashMap<String, String>();
				temp.put("nrCrt", " ");
				temp.put("numeArticol", "Total masa incarcare feronerie: ");
				temp.put("cantitate", " ");
				temp.put("unitMas", " ");
				temp.put("tipOp", " ");
				temp.put("greutate", nf2.format(totalMasaFeronerieIncarcare));
				temp.put("umGreutate", "KG");
				arrayListArtLivr.add(temp);
			}

			// adaugare linie total electrice incarcare
			if (totalMasaElectriceIncarcare > 0) {
				temp = new HashMap<String, String>();
				temp.put("nrCrt", " ");
				temp.put("numeArticol", "Total masa incarcare electrice: ");
				temp.put("cantitate", " ");
				temp.put("unitMas", " ");
				temp.put("tipOp", " ");
				temp.put("greutate", nf2.format(totalMasaElectriceIncarcare));
				temp.put("umGreutate", "KG");
				arrayListArtLivr.add(temp);
			}

			// adaugare linie total masa incarcare
			if (valMasaIncarcare > 0) {
				temp = new HashMap<String, String>();
				temp.put("nrCrt", " ");
				temp.put("numeArticol", "Total masa incarcare: ");
				temp.put("cantitate", " ");
				temp.put("unitMas", " ");
				temp.put("tipOp", " ");
				temp.put("greutate",
						nf2.format(valMasaIncarcare + totalMasaFeronerieIncarcare + totalMasaElectriceIncarcare));
				temp.put("umGreutate", "KG");
				arrayListArtLivr.add(temp);
			}

			// adaugare linie total feronerie descarcare
			if (totalMasaFeronerieDescarcare > 0) {
				temp = new HashMap<String, String>();
				temp.put("nrCrt", " ");
				temp.put("numeArticol", "Total masa descarcare feronerie: ");
				temp.put("cantitate", " ");
				temp.put("unitMas", " ");
				temp.put("tipOp", " ");
				temp.put("greutate", nf2.format(totalMasaFeronerieDescarcare));
				temp.put("umGreutate", "KG");
				arrayListArtLivr.add(temp);
			}

			// adaugare linie total electrice descarcare
			if (totalMasaElectriceDescarcare > 0) {
				temp = new HashMap<String, String>();
				temp.put("nrCrt", " ");
				temp.put("numeArticol", "Total masa descarcare electrice: ");
				temp.put("cantitate", " ");
				temp.put("unitMas", " ");
				temp.put("tipOp", " ");
				temp.put("greutate", nf2.format(totalMasaElectriceDescarcare));
				temp.put("umGreutate", "KG");
				arrayListArtLivr.add(temp);
			}

			// adaugare linie total masa descarcare
			if (valMasaDescarcare > 0) {
				temp = new HashMap<String, String>();
				temp.put("nrCrt", " ");
				temp.put("numeArticol", "Total masa descarcare: ");
				temp.put("cantitate", " ");
				temp.put("unitMas", " ");
				temp.put("tipOp", " ");
				temp.put("greutate",
						nf2.format(valMasaDescarcare + totalMasaFeronerieDescarcare + totalMasaElectriceDescarcare));
				temp.put("umGreutate", "KG");
				arrayListArtLivr.add(temp);
			}

			listViewArtLivrare.setAdapter(adapterArtLivr);

		} else {

			arrayListArtLivr.clear();
			listViewArtLivrare.setAdapter(adapterArtLivr);
		}

		textSelectedClientArt.setText(InfoStrings.getCurentClientName(context));

		drawerArtLivrare.animateOpen();

	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (methodName.equals("getFacturiBorderou")) {
			stopSpinner();
			populateListFacturi(result);
		}

		if (methodName.equals("saveNewEvent")) {
			stopSpinner();

			if (InfoStrings.getEvenimentClient(context).equals("S")) {
				// plecare de la client, reset client curent
				selectedClientCode = "0";
				selectedClientName = "0";
				selectedClientAddr = "0";
				selectedClientLayout.setVisibility(View.INVISIBLE);
			}
			InfoStrings.setCurentClient(context, selectedClientCode);
			InfoStrings.setCurentClientAddr(context, selectedClientAddr);
			InfoStrings.setCurentClientName(context, selectedClientName);

			performLoadFacturiBorderou();
		}

		if (methodName.equals("getArticoleBorderou")) {
			displayArticoleData(result);
			stopSpinner();
		}

	}

}
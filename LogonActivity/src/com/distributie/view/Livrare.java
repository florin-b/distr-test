/**
 * @author florinb
 *
 */
package com.distributie.view;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.distributie.beans.ArticoleFactura;
import com.distributie.beans.BeanAlarma;
import com.distributie.beans.BeanClientAlarma;
import com.distributie.beans.BeanEvenimentStop;
import com.distributie.beans.EvenimentNou;
import com.distributie.beans.Factura;
import com.distributie.dialog.SugestieEvenimentDialog;
import com.distributie.enums.EnumMotivOprire;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiBorderou;
import com.distributie.enums.EnumOperatiiEvenimente;
import com.distributie.enums.EnumTipAlarma;
import com.distributie.enums.TipBorderou;
import com.distributie.enums.TipEveniment;
import com.distributie.listeners.BorderouriDAOListener;
import com.distributie.listeners.EvenimentDialogListener;
import com.distributie.listeners.OperatiiBorderouriListener;
import com.distributie.listeners.OperatiiEvenimenteListener;
import com.distributie.model.BorderouriDAOImpl;
import com.distributie.model.CurrentStatus;
import com.distributie.model.FacturiBorderou;
import com.distributie.model.HandleJSONData;
import com.distributie.model.LocalStorage;
import com.distributie.model.OperatiiBorderouriDAOImpl;
import com.distributie.model.OperatiiEvenimente;
import com.distributie.model.UserInfo;
import com.distributie.utils.DateUtils;

@SuppressWarnings("deprecation")
public class Livrare extends Activity implements BorderouriDAOListener, OperatiiBorderouriListener, OperatiiEvenimenteListener, EvenimentDialogListener {

	@InjectView(R.id.saveEventClienti)
	Button saveEventClienti;

	@InjectView(R.id.showArticoleLivrareBtn)
	Button showArticoleLivrareBtn;

	@InjectView(R.id.progress_bar_event)
	ProgressBar progressBar;

	@InjectView(R.id.listFacturiBorderou)
	ListView listFacturi;

	@InjectView(R.id.listViewArtLivrare)
	ListView listViewArtLivrare;

	@InjectView(R.id.textSelectedBorderou)
	TextView textSelectedBorderou;

	@InjectView(R.id.textSelectedClient)
	TextView textSelectedClient;

	@InjectView(R.id.textSelectedClientArt)
	TextView textSelectedClientArt;

	@InjectView(R.id.textAdresaClient)
	TextView textAdresaClient;

	@InjectView(R.id.selectedClientLayout)
	LinearLayout selectedClientLayout;

	@InjectView(R.id.livrareLayout)
	LinearLayout livrareLayout;

	@InjectView(R.id.articoleLivrareDrawer)
	SlidingDrawer drawerArtLivrare;

	private Timer myEventTimer;
	private int progressVal = 0;
	private Handler eventHandler = new Handler();

	private CustomAdapter adapterFacturi;
	private static ArrayList<HashMap<String, String>> arrayListFacturi = new ArrayList<HashMap<String, String>>();

	public static ArrayList<HashMap<String, String>> arrayListArtLivr = null;
	int selectedPosition = -1;
	ArticoleLivrareAdapter adapterArtLivr;
	private HashMap<String, String> artMap = null;
	private ArrayList<Factura> facturiArray;
	private String selectedClientCode = "", selectedClientName = "", selectedClientAddr = "";
	private Activity context;
	private Timer timer;
	private TimerTask timerTask;
	private Handler handler = new Handler();
	private OperatiiEvenimente opEvenimente;
	private EnumNetworkStatus networkStatus = EnumNetworkStatus.ON;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.livrare);

		context = this;
		ButterKnife.inject(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Livrare");
		actionBar.setDisplayHomeAsUpEnabled(true);

		InitialUISetup();

		opEvenimente = new OperatiiEvenimente(this);
		opEvenimente.setOperatiiEvenimenteListener(this);

		startTimerTask();

	}

	private void startTimerTask() {

		timer = new Timer();
		initializeTimerTask();
		timer.schedule(timerTask, 300000, 300000);

	}

	public void stopTimerTask() {

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {

						getEvenimente();

					}
				});
			}
		};
	}

	private void getEvenimente() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codBorderou", CurrentStatus.getInstance().getNrBorderou());

		opEvenimente.getEvenimentStop(params);

	}

	private void incarcaEvenimente(BeanEvenimentStop evenimentStop) {

		if (!evenimentStop.isEvenimentSalvat()) {
			List<BeanAlarma> listEv = new ArrayList<BeanAlarma>();

			for (BeanClientAlarma client : evenimentStop.getClientiAlarma()) {
				BeanAlarma b = new BeanAlarma();
				b.setTipAlarma(EnumTipAlarma.CLIENT);
				b.setNumeAlarma("Sosire " + client.getNume());
				b.setCodAlarma(client.getCodClient());
				b.setCodAdresa(client.getCodAdresa());
				listEv.add(b);
			}

			for (EnumMotivOprire motiv : EnumMotivOprire.values()) {

				BeanAlarma b = new BeanAlarma();
				b.setTipAlarma(EnumTipAlarma.EVENIMENT);
				b.setNumeAlarma(motiv.getNume());
				b.setCodAlarma(String.valueOf(motiv.getCod()));
				b.setIdEveniment(evenimentStop.getIdEveniment());
				listEv.add(b);
			}

			stopTimerTask();

			SugestieEvenimentDialog sugestie = new SugestieEvenimentDialog(Livrare.this, listEv);
			sugestie.setEvenimentDialogListener(this);
			sugestie.show();

		}
	}

	private void InitialUISetup() {

		try {

			livrareLayout.setVisibility(View.INVISIBLE);

			textSelectedClient.setText("");
			textSelectedClient.setVisibility(View.INVISIBLE);
			textSelectedClientArt.setText("");
			textAdresaClient.setText("");
			progressBar.setVisibility(View.INVISIBLE);
			saveEventClienti.setVisibility(View.INVISIBLE);
			saveEventClienti.setOnTouchListener(new myEvtClientiOnTouchListener());

			selectedClientLayout.setVisibility(View.INVISIBLE);
			listFacturi.setVisibility(View.INVISIBLE);

			if (hasArticole()) {
				showArticoleLivrareBtn.setVisibility(View.VISIBLE);
				showArticoleLivrareBtn.setOnClickListener(new myArtLivrBtnOnClickListener());

			} else {
				showArticoleLivrareBtn.setVisibility(View.GONE);
			}

			if (hasNoDocuments()) {
				Toast.makeText(context, "Selectati un borderou!", Toast.LENGTH_LONG).show();

			} else {

				if (CurrentStatus.getInstance().getEveniment().equals("0")) {
					Toast.makeText(context, "Marcati plecarea in cursa pentru un borderou!", Toast.LENGTH_LONG).show();
				} else {
					performLoadFacturiBorderou();
					listFacturi.setOnItemClickListener(new myListFacturiOnItemClickListener());

					textSelectedBorderou.setText(" Borderou " + CurrentStatus.getInstance().getNrBorderou() + " ");
				}
			}

			selectedPosition = -1;
			arrayListArtLivr = new ArrayList<HashMap<String, String>>();

			adapterArtLivr = new ArticoleLivrareAdapter(context, arrayListArtLivr, R.layout.custom_row_list_articole, new String[] { "nrCrt", "numeArticol",
					"cantitate", "unitMas", "tipOp", "greutate", "umGreutate" }, new int[] { R.id.textNrCrt, R.id.textNumeArticol, R.id.textCantitate,
					R.id.textUnitMas, R.id.textTipOp, R.id.textGreutate, R.id.textUnitMasGreutate });

			drawerArtLivrare.setVisibility(View.GONE);
			drawerArtLivrareListener();
			listViewArtLivrare.setAdapter(adapterArtLivr);

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private boolean hasNoDocuments() {
		return CurrentStatus.getInstance().getEveniment() == null || CurrentStatus.getInstance().getEveniment().equals("0");
	}

	private boolean hasArticole() {
		return CurrentStatus.getInstance().getTipBorderou() == TipBorderou.APROVIZIONARE
				|| CurrentStatus.getInstance().getTipBorderou() == TipBorderou.INCHIRIERE;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);
			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onBackPressed() {

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
		return;
	}

	public void addEventListener(OnTouchListener touchListener) {
		saveEventClienti.setOnTouchListener(touchListener);
	}

	@SuppressWarnings("deprecation")
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

		saveEventClienti.setEnabled(true);
		BorderouriDAOImpl borderouri = new BorderouriDAOImpl(this);
		borderouri.setBorderouEventListener(this);
		borderouri.getFacturiBorderou(CurrentStatus.getInstance().getNrBorderou(), CurrentStatus.getInstance().getTipBorderou());

	}

	private void populateListFacturi(String facturi) {

		saveEventClienti.setEnabled(true);

		HandleJSONData objListFacturi = new HandleJSONData(context, facturi);
		facturiArray = objListFacturi.decodeJSONFacturiBorderou();

		if (facturiArray.size() > 0) {

			displayListFacturi();

		} else {
			livrareLayout.setVisibility(View.INVISIBLE);
		}

	}

	private void displayListFacturi() {

		saveEventClienti.setEnabled(true);

		livrareLayout.setVisibility(View.VISIBLE);
		arrayListFacturi.clear();

		textSelectedClient.setText("");
		textAdresaClient.setText("");
		saveEventClienti.setVisibility(View.INVISIBLE);

		listFacturi.setVisibility(View.VISIBLE);

		FacturiBorderou facturiBorderou = new FacturiBorderou(context);
		adapterFacturi = facturiBorderou.getFacturiBorderouAdapter(facturiArray, CurrentStatus.getInstance().getTipBorderou(), true);

		arrayListFacturi = facturiBorderou.getArrayListFacturi();
		listFacturi.setAdapter(adapterFacturi);

		listFacturi.setSelection(selectedPosition);
		if (facturiBorderou.getSelectedClientIndex() >= 0) {

			listFacturi.setItemChecked(facturiBorderou.getSelectedClientIndex(), true);
			listFacturi.performItemClick(listFacturi, facturiBorderou.getSelectedClientIndex(),
					listFacturi.getItemIdAtPosition(facturiBorderou.getSelectedClientIndex()));

		}

		checkForItemsLeft();
	}

	private void updateFacturiStatus() {
		LocalStorage local = new LocalStorage(this);

		try {
			List<EvenimentNou> storedEvs = local.getSavedEvents();

			for (Factura factura : facturiArray) {

				for (EvenimentNou storedEvent : storedEvs) {
					if (factura.getCodClient().equals(storedEvent.getClient()) && factura.getCodAdresaClient().equals(storedEvent.getCodAdresa())) {
						if (storedEvent.getEveniment().equals("0") && factura.getSosireClient().equals("0"))
							factura.setSosireClient(storedEvent.getOra());

						if (storedEvent.getEveniment().equalsIgnoreCase("S") && factura.getPlecareClient().equals("0"))
							factura.setPlecareClient(storedEvent.getOra());

					}
					if (factura.getCodFurnizor().equals(storedEvent.getClient()) && factura.getCodAdresaFurnizor().equals(storedEvent.getCodAdresa())) {
						if (storedEvent.getEveniment().equals("0") && factura.getSosireFurnizor().equals("0"))
							factura.setSosireFurnizor(storedEvent.getOra());

						if (storedEvent.getEveniment().equalsIgnoreCase("S") && factura.getPlecareFurnizor().equals("0"))
							factura.setPlecareFurnizor(storedEvent.getOra());

					}

				}

			}

		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		displayListFacturi();

	}

	boolean isClientSelected(ArrayList<Factura> facturiArray, int pos) {
		return CurrentStatus.getInstance().getCurrentClient().equals(facturiArray.get(pos).getCodClient())
				&& CurrentStatus.getInstance().getCurentClientAddr().equals(facturiArray.get(pos).getCodAdresaClient());
	}

	@SuppressWarnings("unchecked")
	private boolean checkForItemsLeft() {

		boolean areItemsLeft = false;
		HashMap<String, String> artMap = null;

		for (int i = 0; i < arrayListFacturi.size(); i++) {

			artMap = (HashMap<String, String>) adapterFacturi.getItem(i);

			if (artMap.get("ev1").toString().trim().length() == 0 || artMap.get("ev2").toString().trim().length() == 0) {
				areItemsLeft = true;
				break;
			}
		}

		if (!areItemsLeft && networkStatus == EnumNetworkStatus.ON) {
			Intent nextScreen = new Intent(getApplicationContext(), BorderouriView.class);
			startActivity(nextScreen);
			finish();
		}

		return areItemsLeft;

	}

	@Override
	public void finish() {
		stopTimerTask();
		super.finish();
	}

	class myListFacturiOnItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			performEvent(position);

		}

	}

	@SuppressWarnings("unchecked")
	private void performEvent(int position) {
		try {

			if (isClientSelectable(position)) {

				selectedClientLayout.setVisibility(View.VISIBLE);

				artMap = (HashMap<String, String>) adapterFacturi.getItem(position);

				selectedClientCode = artMap.get("codClient");
				selectedClientName = artMap.get("numeClient");
				selectedClientAddr = artMap.get("codAdresa");

				CurrentStatus.getInstance().setCurrentClient(selectedClientCode);
				CurrentStatus.getInstance().setCurentClientAddr(selectedClientAddr);
				CurrentStatus.getInstance().setCurrentClientName(selectedClientName);

				selectedPosition = position;

				textSelectedClient.setVisibility(View.VISIBLE);

				String localStrEvClnt = "0";
				if (artMap.get("ev1").trim().length() > 0)
					localStrEvClnt = artMap.get("ev1").substring(0, 1);

				CurrentStatus.getInstance().setEvenimentClient(localStrEvClnt);

				if (localStrEvClnt.equals("0")) {
					saveEventClienti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.in1, 0, 0, 0);
					saveEventClienti.setText("SOSIRE");
				} else {
					saveEventClienti.setCompoundDrawablesWithIntrinsicBounds(R.drawable.out1, 0, 0, 0);
					saveEventClienti.setText("PLECARE");
				}

				textAdresaClient.setText(artMap.get("adresaClient"));
				textSelectedClient.setText(artMap.get("numeClient"));
				saveEventClienti.setVisibility(View.VISIBLE);

			}

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
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
					buttonActionDown();
					return true;

				case MotionEvent.ACTION_UP:
					buttonActionUp();
					return true;

				}
			} catch (Exception ex) {
				Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
			}

			return false;
		}

	}

	private void buttonActionDown() {
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setProgress(0);
		progressVal = 0;
		myEventTimer = new Timer();
		myEventTimer.schedule(new UpdateProgress(), 35, 15);
	}

	private void buttonActionUp() {
		if (progressBar.getVisibility() == View.VISIBLE) {
			myEventTimer.cancel();
			myEventTimer = null;
			progressBar.setVisibility(View.INVISIBLE);

		}
	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (progressBar.getProgress() == 50) {
				myEventTimer.cancel();
				myEventTimer = null;
				eventHandler.post(new Runnable() {
					public void run() {

						saveEventClienti.setEnabled(false);
						progressBar.setVisibility(View.INVISIBLE);
						performSaveNewEventClienti();

					}
				});

			} else {
				progressBar.setProgress(progressVal);

			}

		}
	}

	private void performSaveNewEventClienti() {

		EvenimentNou ev = new EvenimentNou();
		ev.setCodSofer(UserInfo.getInstance().getId());
		ev.setDocument(CurrentStatus.getInstance().getNrBorderou());
		ev.setClient(CurrentStatus.getInstance().getCurrentClient());
		ev.setCodAdresa(CurrentStatus.getInstance().getCurentClientAddr());
		ev.setEveniment(CurrentStatus.getInstance().getEvenimentClient());
		ev.setTipEveniment(TipEveniment.NOU);
		ev.setData(DateUtils.getCurrentDate());
		ev.setOra(DateUtils.getCurrentTime());

		OperatiiBorderouriDAOImpl bordStatus = new OperatiiBorderouriDAOImpl(this);
		bordStatus.setEventListener(this);
		bordStatus.saveNewEventClient(ev);

	}

	class myArtLivrBtnOnClickListener implements Button.OnClickListener {
		public void onClick(View v) {
			performGetArticoleDocument();
		}

	}

	private void performGetArticoleDocument() {
		try {
			BorderouriDAOImpl borderou = new BorderouriDAOImpl(this);
			borderou.setBorderouEventListener(this);
			borderou.getArticoleBorderou(CurrentStatus.getInstance().getNrBorderou(), CurrentStatus.getInstance().getCurrentClient(), CurrentStatus
					.getInstance().getCurentClientAddr());

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void displayArticoleData(String articoleData) {

		HandleJSONData objListArticole = new HandleJSONData(context, articoleData);
		ArrayList<ArticoleFactura> articoleArray = objListArticole.decodeJSONArticoleFactura();

		if (articoleArray.size() > 0) {
			arrayListArtLivr.clear();

			boolean isDescarcat = false, isIncarcat = false;

			HashMap<String, String> temp = null;

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			double totalMasaElectriceDescarcare = 0, totalMasaFeronerieDescarcare = 0;
			double totalMasaElectriceIncarcare = 0, totalMasaFeronerieIncarcare = 0;
			double valMasa = 0, valMasaIncarcare = 0, valMasaDescarcare = 0;
			int nrCrt = 1;

			for (int i = 0; i < articoleArray.size(); i++) {

				valMasa = 0;

				if (articoleArray.get(i).getTipOperatiune().equals("descarcare")) {

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

					if (articoleArray.get(i).getUmGreutate().toUpperCase(Locale.getDefault()).equals("G")) {
						valMasa = Double.parseDouble(articoleArray.get(i).getGreutate()) / 1000;
					}

					if (articoleArray.get(i).getUmGreutate().toUpperCase(Locale.getDefault()).equals("KG")) {
						valMasa = Double.parseDouble(articoleArray.get(i).getGreutate());
					}

					if (articoleArray.get(i).getUmGreutate().toUpperCase(Locale.getDefault()).equals("T")) {
						valMasa = Double.parseDouble(articoleArray.get(i).getGreutate()) * 1000;
					}

					// depart. feronerie
					if (articoleArray.get(i).getDepartament().equals("02")) {
						totalMasaFeronerieDescarcare += valMasa;
					}

					// depart electrice
					if (articoleArray.get(i).getDepartament().equals("05")) {
						totalMasaElectriceDescarcare += valMasa;
					}

					if (!articoleArray.get(i).getDepartament().equals("05") && !articoleArray.get(i).getDepartament().equals("02")) {
						temp = new HashMap<String, String>();
						temp.put("nrCrt", String.valueOf(nrCrt) + ".");
						temp.put("numeArticol", articoleArray.get(i).getNume());
						temp.put("cantitate", articoleArray.get(i).getCantitate());
						temp.put("unitMas", articoleArray.get(i).getUmCant());
						temp.put("tipOp", articoleArray.get(i).getTipOperatiune());
						temp.put("greutate", nf2.format(Double.valueOf(articoleArray.get(i).getGreutate())));
						temp.put("umGreutate", articoleArray.get(i).getUmGreutate());

						arrayListArtLivr.add(temp);

						valMasaDescarcare += valMasa;
						nrCrt++;
					}
				}

				if (articoleArray.get(i).getTipOperatiune().equals("incarcare")) {

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

					if (articoleArray.get(i).getUmGreutate().toUpperCase(Locale.getDefault()).equals("G")) {
						valMasa = Double.parseDouble(articoleArray.get(i).getGreutate()) / 1000;
					}

					if (articoleArray.get(i).getUmGreutate().toUpperCase(Locale.getDefault()).equals("KG")) {
						valMasa = Double.parseDouble(articoleArray.get(i).getGreutate());
					}

					if (articoleArray.get(i).getUmGreutate().toUpperCase(Locale.getDefault()).equals("T")) {
						valMasa = Double.parseDouble(articoleArray.get(i).getGreutate()) * 1000;
					}

					// depart. feronerie
					if (articoleArray.get(i).getDepartament().equals("02")) {
						totalMasaFeronerieIncarcare += valMasa;
					}

					// depart electrice
					if (articoleArray.get(i).getDepartament().equals("05")) {
						totalMasaElectriceIncarcare += valMasa;
					}

					if (!articoleArray.get(i).getDepartament().equals("05") && !articoleArray.get(i).getDepartament().equals("02")) {
						temp = new HashMap<String, String>();
						temp.put("nrCrt", String.valueOf(nrCrt) + ".");
						temp.put("numeArticol", articoleArray.get(i).getNume());
						temp.put("cantitate", articoleArray.get(i).getCantitate());
						temp.put("unitMas", articoleArray.get(i).getUmCant());
						temp.put("tipOp", articoleArray.get(i).getTipOperatiune());
						temp.put("greutate", nf2.format(Double.valueOf(articoleArray.get(i).getGreutate())));
						temp.put("umGreutate", articoleArray.get(i).getUmGreutate());
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
				temp.put("greutate", nf2.format(valMasaIncarcare + totalMasaFeronerieIncarcare + totalMasaElectriceIncarcare));
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
				temp.put("greutate", nf2.format(valMasaDescarcare + totalMasaFeronerieDescarcare + totalMasaElectriceDescarcare));
				temp.put("umGreutate", "KG");
				arrayListArtLivr.add(temp);
			}

			listViewArtLivrare.setAdapter(adapterArtLivr);

		} else {
			arrayListArtLivr.clear();
			listViewArtLivrare.setAdapter(adapterArtLivr);
		}

		textSelectedClientArt.setText(CurrentStatus.getInstance().getCurrentClientName());
		drawerArtLivrare.animateOpen();

	}

	@Override
	public void loadComplete(String result, EnumOperatiiBorderou methodName) {

		switch (methodName) {
		case GET_FACTURI_BORDEROU:
			populateListFacturi(result);
			break;
		case GET_ARTICOLE_BORDEROU:
			displayArticoleData(result);
			break;
		default:
			break;

		}

	}

	@Override
	public void eventComplete(String result, EnumOperatiiEvenimente methodName, EnumNetworkStatus networkStatus) {

		switch (methodName) {
		case SAVE_NEW_EVENT:
			if (CurrentStatus.getInstance().getEvenimentClient().equals("S")) {
				// plecare de la client, reset client curent
				selectedClientCode = "0";
				selectedClientName = "0";
				selectedClientAddr = "0";
				selectedClientLayout.setVisibility(View.INVISIBLE);
			}

			CurrentStatus.getInstance().setCurrentClient(selectedClientCode);
			CurrentStatus.getInstance().setCurentClientAddr(selectedClientAddr);
			CurrentStatus.getInstance().setCurrentClientName(selectedClientName);

			this.networkStatus = networkStatus;

			if (networkStatus == EnumNetworkStatus.ON)
				performLoadFacturiBorderou();
			else
				updateFacturiStatus();

			break;

		default:
			break;

		}

	}

	@Override
	public void opEventComplete(String result, EnumOperatiiEvenimente methodName) {
		switch (methodName) {
		case CHECK_STOP:
			incarcaEvenimente(opEvenimente.deserializeEvenimentStop(result));
			break;
		default:
			break;
		}

	}

	@Override
	public void evenimentDialogProduced() {
		startTimerTask();

	}

}

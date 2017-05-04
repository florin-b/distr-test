/**
 * @author florinb
 *
 */
package com.distributie.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.distributie.adapters.EtapeAdapter;
import com.distributie.beans.Borderou;
import com.distributie.beans.Etapa;
import com.distributie.beans.Factura;
import com.distributie.dialog.CustomInfoDialog;
import com.distributie.enums.EnumOperatiiBorderou;
import com.distributie.enums.EnumOperatiiEvenimente;
import com.distributie.enums.EnumTipEtapa;
import com.distributie.enums.TipBorderou;
import com.distributie.listeners.ArticoleEtapaListener;
import com.distributie.listeners.BorderouriDAOListener;
import com.distributie.listeners.BorderouriListener;
import com.distributie.listeners.InfoDialogListener;
import com.distributie.listeners.OperatiiEtapeListener;
import com.distributie.listeners.OperatiiEvenimenteListener;
import com.distributie.model.BorderouriDAOImpl;
import com.distributie.model.CurrentStatus;
import com.distributie.model.EtapeDTI;
import com.distributie.model.ExceptionHandler;
import com.distributie.model.HandleJSONData;
import com.distributie.model.OperatiiEvenimente;
import com.distributie.model.UserInfo;
import com.distributie.utils.Messages;

public class AfisEtape extends Activity implements BorderouriDAOListener, OperatiiEvenimenteListener, OperatiiEtapeListener, BorderouriListener,
		InfoDialogListener, ArticoleEtapaListener {

	private ListView listViewEtape;
	private OperatiiEvenimente opEvenimente;
	private List<Etapa> listEtape;
	private EtapeAdapter etapeAdapter;
	private Borderou borderouCurent;
	private TextView textInfo;

	private Timer timer;
	private TimerTask timerTask;
	private String ultimaEtapaId;
	private Date sfarsitBordDate;
	private Button refreshListEtape;

	private Handler handler = new Handler();
	private boolean borderouDTI;
	private EtapeDTI etapeDTI;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.etape);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Etape");
		actionBar.setDisplayHomeAsUpEnabled(true);

		opEvenimente = new OperatiiEvenimente(this);
		opEvenimente.setOperatiiEvenimenteListener(this);

		listEtape = new ArrayList<Etapa>();

		etapeAdapter = new EtapeAdapter(this, listEtape);
		etapeAdapter.setOperatiiEtapeListener(this);
		etapeAdapter.setBorderouriListener(this);
		etapeAdapter.setArticoleEtapaListener(this);

		etapeDTI = new EtapeDTI();

		setupLayout();

		getBorderouri();

		if (UserInfo.getInstance().isDti())
			startTimerTask();

	}

	private void setupLayout() {
		listViewEtape = (ListView) findViewById(R.id.listEtape);
		listViewEtape.setVisibility(View.GONE);

		textInfo = (TextView) findViewById(R.id.textInfo);
		textInfo.setVisibility(View.GONE);

		refreshListEtape = (Button) findViewById(R.id.refreshListEtape);
		refreshListEtape.setVisibility(View.GONE);

		setListenerRefreshEtapeBtn();

	}

	private void setListenerRefreshEtapeBtn() {
		refreshListEtape.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getBorderouri();

			}
		});
	}

	private void startTimerTask() {

		if (timer == null) {
			timer = new Timer();
			initializeTimerTask();
			// timer.schedule(timerTask, 300000, 300000);

			timer.schedule(timerTask, 30000, 30000);

		}

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

						getBorderouri();

					}
				});
			}
		};
	}

	public void getBorderouri() {

		if (UserInfo.getInstance().isDti())
			etapeDTI.getEtapeFromListView(listViewEtape);

		listEtape.clear();

		if (!UserInfo.getInstance().isDti())
			checkBordScanningTime();

		BorderouriDAOImpl bord = BorderouriDAOImpl.getInstance(this);
		bord.setBorderouEventListener(AfisEtape.this);
		bord.getBorderouri(UserInfo.getInstance().getId(), "d", "-1");

	}

	private void checkBordScanningTime() {
		if (sfarsitBordDate != null) {
			Date currentDate = new Date();
			long diff = currentDate.getTime() - sfarsitBordDate.getTime();
			long diffMinutes = diff / (60 * 1000) % 60;

			if (diffMinutes > 10)
				System.exit(0);

		}
	}

	private void getSfarsitIncarcareEv(String codBorderou) {

		if (CurrentStatus.getInstance().getNrBorderou() != "") {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("document", codBorderou);
			params.put("codSofer", UserInfo.getInstance().getId());
			opEvenimente.getSfarsitIncarcare(params);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == 0) {
			stopTimerTask();
			System.exit(0);
		}

		return super.onOptionsItemSelected(item);

	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "IESIRE");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	private void populateListBorderouri(String borderouri) {

		HandleJSONData objListBorderouri = new HandleJSONData(this, borderouri);
		ArrayList<Borderou> listBorderouri = objListBorderouri.decodeJSONBorderouri();

		if (!listBorderouri.isEmpty() && listBorderouri.get(0).getNumarBorderou() != "null") {

			listViewEtape.setVisibility(View.VISIBLE);
			textInfo.setVisibility(View.GONE);

			borderouCurent = listBorderouri.get(0);
			borderouDTI = listBorderouri.get(0).isAgentDTI();

			getSfarsitIncarcareEv(listBorderouri.get(0).getNumarBorderou());

		} else {
			listViewEtape.setVisibility(View.GONE);
			textInfo.setVisibility(View.VISIBLE);

			if (!listBorderouri.isEmpty() && listBorderouri.get(0).getNumarBorderou() == "null")
				borderouDTI = listBorderouri.get(0).isAgentDTI();

			textInfo.setText(Messages.getSfarsitBordMessage(borderouDTI));
			refreshListEtape.setVisibility(View.VISIBLE);

		}

	}

	private void handleSfarsitIncarcareEvent(String result) {

		boolean isEtapaSfarsit = false;

		Etapa etapa = new Etapa();
		etapa.setTipEtapa(EnumTipEtapa.SFARSIT_INCARCARE);
		etapa.setNume("Sfarsit incarcare");
		etapa.setPos(listEtape.size());
		etapa.setDocument(borderouCurent.getNumarBorderou());
		etapa.setTipBorderou(borderouCurent.getStandardTipBorderou());
		etapa.setFactura(new Factura());

		if (result.contains("SOF")) {
			etapa.setSalvata(true);
			isEtapaSfarsit = true;
		}

		else if (result.contains("LOG")) {
			isEtapaSfarsit = false;
		} else if (!result.contains("-1")) {
			isEtapaSfarsit = true;
		}

		if (borderouCurent.getStandardTipBorderou() == TipBorderou.DISTRIBUTIE && isEtapaSfarsit) {
			listEtape.add(etapa);

		}

		if (borderouCurent.getBordParent().equals("-1") && borderouCurent.getStandardTipBorderou() != TipBorderou.APROVIZIONARE) {
			etapa = new Etapa();
			etapa.setTipEtapa(EnumTipEtapa.START_BORD);
			etapa.setNume("Inceput cursa");
			etapa.setPos(listEtape.size());
			etapa.setTipBorderou(borderouCurent.getStandardTipBorderou());
			etapa.setDocument(borderouCurent.getNumarBorderou());
			etapa.setFactura(new Factura());

			listEtape.add(etapa);
		}

		getFacturiBorderou();

	}

	private void getFacturiBorderou() {

		BorderouriDAOImpl borderouri = new BorderouriDAOImpl(this);
		borderouri.setBorderouEventListener(this);
		borderouri.getFacturiBorderou(borderouCurent.getNumarBorderou(), borderouCurent.getStandardTipBorderou());

	}

	private void populateListFacturi(String facturi) {

		HandleJSONData objListFacturi = new HandleJSONData(AfisEtape.this, facturi);
		ArrayList<Factura> listFacturi = objListFacturi.decodeJSONFacturiBorderou();

		if (borderouCurent.getStandardTipBorderou() == TipBorderou.DISTRIBUTIE) {
			loadEtapeDistributie(listFacturi);

		} else {

			loadEtapeRest(listFacturi);
		}

	}

	private void loadEtapeRest(ArrayList<Factura> listFacturi) {
		if (!listFacturi.isEmpty()) {

			listEtape.clear();

			for (int i = 0; i < listFacturi.size(); i++) {

				Etapa etapa = new Etapa();
				etapa.setFactura(listFacturi.get(i));
				etapa.setPos(listEtape.size());
				etapa.setTipBorderou(borderouCurent.getStandardTipBorderou());
				etapa.setDocument(borderouCurent.getNumarBorderou());
				etapa.setTipEtapa(i == 0 ? EnumTipEtapa.START_BORD : EnumTipEtapa.SOSIRE);
				etapa.setObservatii(i == 0 ? "INCEPUT CURSA" : "");
				etapa.setPozitie(listFacturi.get(i).getPozitie());
				listEtape.add(etapa);

			}

			int pozitieEtapaNoua = -1;

			if (UserInfo.getInstance().isDti())
				pozitieEtapaNoua = etapeDTI.verificaEtapeNoi(listEtape);

			// actualizare start cursa
			for (Etapa etapa : listEtape) {
				if (etapa.getTipEtapa() == EnumTipEtapa.START_BORD) {
					etapa.setSalvata(!listFacturi.get(0).getDataStartCursa().equals(""));
					break;
				}
			}

			if (ultimaEtapaId == null)
				ultimaEtapaId = listEtape.get(listEtape.size() - 1).getCodClient();

			setAutoNrEtapa(listEtape);

			Collections.sort(listEtape);

			listEtape.get(listEtape.size() - 1).setTipEtapa(EnumTipEtapa.STOP_BORD);
			listEtape.get(listEtape.size() - 1).setObservatii("Sfarsit cursa");

			etapeAdapter.setListEtape(listEtape);
			etapeAdapter.setBorderou(borderouCurent);
			listViewEtape.setAdapter(etapeAdapter);

			int pozitieEtapaNesalvata = etapeDTI.getEtapaNesalvata(listEtape);

			if (pozitieEtapaNesalvata != -1)
				listViewEtape.setSelection(pozitieEtapaNesalvata);

			if (pozitieEtapaNoua != -1) {
				showInfoDialogEtapaNoua();
				listViewEtape.setSelection(pozitieEtapaNoua);
			}

		}

	}

	private void showInfoDialogEtapaNoua() {
		stopTimerTask();
		CustomInfoDialog infoDialog = new CustomInfoDialog(this);
		infoDialog.setInfoText("AU APARUT ELEMENTE NOI IN CURSA PE CARE O EFECTUATI!");
		infoDialog.setInfoDialogListener(this);
		infoDialog.show();

	}

	private void loadEtapeDistributie(ArrayList<Factura> listFacturi) {
		if (!listFacturi.isEmpty()) {

			sfarsitBordDate = null;
			stopTimerTask();

			for (int i = 0; i < listFacturi.size(); i++) {
				Etapa etapa = new Etapa();
				etapa.setFactura(listFacturi.get(i));
				etapa.setPos(listEtape.size());
				etapa.setTipBorderou(borderouCurent.getStandardTipBorderou());
				etapa.setDocument(borderouCurent.getNumarBorderou());
				etapa.setTipEtapa(EnumTipEtapa.SOSIRE);
				etapa.setPozitie(listFacturi.get(i).getPozitie().equals("-1") ? null : listFacturi.get(i).getPozitie());
				listEtape.add(etapa);
			}

			// actualizare start cursa
			for (Etapa etapa : listEtape) {
				if (etapa.getTipEtapa() == EnumTipEtapa.START_BORD) {
					etapa.setSalvata(!listFacturi.get(0).getDataStartCursa().equals(""));
					break;
				}
			}

			if (ultimaEtapaId == null)
				ultimaEtapaId = listEtape.get(listEtape.size() - 1).getCodClient();
			else {
				if (!ultimaEtapaId.equals(listEtape.get(listEtape.size() - 1).getCodClient()))
					stopTimerTask();
			}

			setAutoNrEtapa(listEtape);

			Collections.sort(listEtape);

			listEtape.get(listEtape.size() - 1).setTipEtapa(EnumTipEtapa.STOP_BORD);
			listEtape.get(listEtape.size() - 1).setObservatii("Sfarsit cursa");

			etapeAdapter.setListEtape(listEtape);
			etapeAdapter.setBorderou(borderouCurent);
			listViewEtape.setAdapter(etapeAdapter);
		}

	}

	private void setAutoNrEtapa(List<Etapa> listEtape) {

		if (listEtape.size() == 1) {
			listEtape.get(0).setPozitie("1");
			listEtape.get(0).setTipEtapa(EnumTipEtapa.STOP_BORD);
		}

		if (listEtape.size() == 2) {
			if (listEtape.get(0).getTipEtapa() == EnumTipEtapa.START_BORD) {
				listEtape.get(1).setPozitie("1");
				listEtape.get(1).setTipEtapa(EnumTipEtapa.STOP_BORD);
			}
		}

		if (listEtape.size() == 3) {
			if (listEtape.get(1).getTipEtapa() == EnumTipEtapa.START_BORD) {
				listEtape.get(2).setPozitie("1");
				listEtape.get(2).setTipEtapa(EnumTipEtapa.STOP_BORD);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	@Override
	public void loadComplete(String result, EnumOperatiiBorderou methodName) {
		switch (methodName) {
		case GET_BORDEROURI:
			populateListBorderouri(result);
			break;
		case GET_FACTURI_BORDEROU:
			populateListFacturi(result);
			break;
		default:
			break;
		}

	}

	@Override
	public void opEventComplete(String result, EnumOperatiiEvenimente methodName) {
		switch (methodName) {

		case SET_SFARSIT_INC:
		case GET_SFARSIT_INC:
			handleSfarsitIncarcareEvent(result);
			break;
		default:
			break;
		}

	}

	@Override
	public void borderouTerminat() {

		getBorderouri();
		sfarsitBordDate = new Date();
		startTimerTask();

	}

	@Override
	public void verificaBordeoruri() {
		startTimerTask();

	}

	@Override
	public void infoDialogTextRead() {
		startTimerTask();
	}

	@Override
	public void articoleEtapaOpened() {
		stopTimerTask();

	}

	@Override
	public void articoleEtapaClosed() {
		if (borderouCurent.getStandardTipBorderou() == TipBorderou.APROVIZIONARE) {
			startTimerTask();
		}

	}

}
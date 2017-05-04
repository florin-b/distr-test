package com.distributie.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.distributie.beans.Borderou;
import com.distributie.beans.Etapa;
import com.distributie.beans.EvenimentNou;
import com.distributie.dialog.ArticoleEtapaDialog;
import com.distributie.dialog.CustomAlertDialog;
import com.distributie.dialog.CustomInfoDialog;
import com.distributie.dialog.PozitieLivrareDialog;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOpConfirm;
import com.distributie.enums.EnumOperatiiEvenimente;
import com.distributie.enums.EnumTipEtapa;
import com.distributie.enums.TipBorderou;
import com.distributie.enums.TipEveniment;
import com.distributie.helpers.BorderouriHelper;
import com.distributie.listeners.AlertDialogListener;
import com.distributie.listeners.ArticoleEtapaListener;
import com.distributie.listeners.BorderouriListener;
import com.distributie.listeners.OperatiiBorderouriListener;
import com.distributie.listeners.OperatiiEtapeListener;
import com.distributie.listeners.OperatiiEvenimenteListener;
import com.distributie.listeners.PozitieLivrareListener;
import com.distributie.model.OperatiiBorderouriDAOImpl;
import com.distributie.model.OperatiiEvenimente;
import com.distributie.model.UserInfo;
import com.distributie.utils.Constants;
import com.distributie.utils.DateUtils;
import com.distributie.utils.HelperEtape;
import com.distributie.utils.MapUtils;
import com.distributie.view.R;
import com.google.android.gms.maps.model.LatLng;

public class EtapeAdapter extends BaseAdapter implements OperatiiEvenimenteListener, OperatiiBorderouriListener, AlertDialogListener, PozitieLivrareListener,
		ArticoleEtapaListener {

	private List<Etapa> listEtape;
	private Context context;
	private OperatiiEvenimente opEvenimente;
	private ViewHolder currentView;
	private int currentPosition;
	private int btnPositionPosition;
	private Etapa etapaCurenta;
	private OperatiiBorderouriDAOImpl opBorderouri;
	private CustomAlertDialog alertDialog;
	private OperatiiEtapeListener etapeListener;
	private BorderouriListener borderouriListener;
	private PozitieLivrareDialog pozitieLivrareDialog;
	private List<String> pozitiiDisponibile;
	private Borderou borderou;
	private ArticoleEtapaListener articoleListener;

	public EtapeAdapter(Context context, List<Etapa> listEtape) {
		this.context = context;
		this.listEtape = listEtape;

		int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.40);
		int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.60);

		pozitieLivrareDialog = new PozitieLivrareDialog(context);
		pozitieLivrareDialog.setPozitieLivrareListener(EtapeAdapter.this);
		pozitieLivrareDialog.getWindow().setLayout(width, height);

		opEvenimente = new OperatiiEvenimente(context);
		opEvenimente.setOperatiiEvenimenteListener(EtapeAdapter.this);

		opBorderouri = new OperatiiBorderouriDAOImpl(context);
		opBorderouri.setEventListener(this);

		alertDialog = new CustomAlertDialog(context);
		alertDialog.setAlertDialogListener(this);

		checkEtapeRamase();

		setPozitiiLivrare();

	}

	static class ViewHolder {
		public TextView textNrCrt, textNumeEtapa, textDescEtapa, textDocument;
		public Button btnSalvareEveniment, btnAnulareEveniment, btnArticole;
		public ImageView checkedIcon, clearIcon;
		public TextView textPozitie, textDescPozitie;

	}

	public void setListEtape(List<Etapa> listEtape) {
		this.listEtape = listEtape;
		notifyDataSetChanged();
	}

	public void setBorderou(Borderou borderou) {
		this.borderou = borderou;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_etapa, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNrCrt = (TextView) convertView.findViewById(R.id.textNrCrt);
			viewHolder.textNumeEtapa = (TextView) convertView.findViewById(R.id.textNumeEtapa);
			viewHolder.textDescEtapa = (TextView) convertView.findViewById(R.id.textDescEtapa);
			viewHolder.btnSalvareEveniment = (Button) convertView.findViewById(R.id.btnSalvareEveniment);
			viewHolder.btnAnulareEveniment = (Button) convertView.findViewById(R.id.btnAnulareEveniment);
			viewHolder.textDocument = (TextView) convertView.findViewById(R.id.textDocument);
			viewHolder.checkedIcon = (ImageView) convertView.findViewById(R.id.checkedIcon);
			viewHolder.textPozitie = (TextView) convertView.findViewById(R.id.textPozitie);
			viewHolder.clearIcon = (ImageView) convertView.findViewById(R.id.clearIcon);
			viewHolder.textDescPozitie = (TextView) convertView.findViewById(R.id.textDescPozitie);
			viewHolder.btnArticole = (Button) convertView.findViewById(R.id.btnArticole);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Etapa etapa = getItem(position);

		viewHolder.btnSalvareEveniment.setText(HelperEtape.getNumeEveniment(etapa));

		currentView = viewHolder;
		currentPosition = position;

		setSaveButtonsStatus(currentPosition, currentView);

		viewHolder.textNumeEtapa.setText(etapa.getNume());
		viewHolder.textDescEtapa.setText(etapa.getDescriere());

		viewHolder.textDocument.setText("Nr. borderou " + etapa.getDocument());
		viewHolder.textPozitie.setText(etapa.getPozitie() == null ? "?" : etapa.getPozitie());

		setVisibilityOrdineEtape(viewHolder, etapa);
		setVisibilityArticoleEtapa(viewHolder, etapa);

		viewHolder.textPozitie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				btnPositionPosition = position;
				setPozitiiLivrare();
				pozitieLivrareDialog.show();

			}
		});

		viewHolder.btnSalvareEveniment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saveEvent(position, viewHolder);
			}
		});

		viewHolder.btnAnulareEveniment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cancelEvent(position, viewHolder);
			}
		});

		viewHolder.clearIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				listEtape.get(position).setPozitie(null);
				notifyDataSetChanged();
				setPozitiiLivrare();

			}
		});

		viewHolder.btnArticole.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getArticole(position);
			}
		});

		if (etapa.isEtapaNoua()) {
			convertView.setBackgroundResource(R.drawable.shadow_red);
		} else {
			if (position % 2 == 0)
				convertView.setBackgroundResource(R.drawable.shadow_dark);
			else
				convertView.setBackgroundResource(R.drawable.shadow_light);
		}

		return convertView;
	}

	private void setVisibilityOrdineEtape(ViewHolder viewHolder, Etapa etapa) {

		if (etapa.getTipBorderou() == TipBorderou.DISTRIBUTIE) {

			if (etapa.getTipEtapa() != EnumTipEtapa.SFARSIT_INCARCARE && etapa.getTipEtapa() != EnumTipEtapa.START_BORD) {
				viewHolder.textPozitie.setVisibility(View.VISIBLE);
				viewHolder.textDescPozitie.setVisibility(View.VISIBLE);
				viewHolder.clearIcon.setVisibility(View.VISIBLE);
			} else {
				viewHolder.textPozitie.setVisibility(View.INVISIBLE);
				viewHolder.textDescPozitie.setVisibility(View.INVISIBLE);
				viewHolder.clearIcon.setVisibility(View.INVISIBLE);
			}

		} else {
			viewHolder.textPozitie.setVisibility(View.INVISIBLE);
			viewHolder.textDescPozitie.setVisibility(View.INVISIBLE);
			viewHolder.clearIcon.setVisibility(View.INVISIBLE);
		}

	}

	public void setArticoleEtapaListener(ArticoleEtapaListener articoleListener) {
		this.articoleListener = articoleListener;
	}

	private void getArticole(int position) {

		ArticoleEtapaDialog articoleDialog = new ArticoleEtapaDialog(context, listEtape.get(position));
		articoleDialog.setArticoleListener(this);
		articoleDialog.show();

		if (articoleListener != null)
			articoleListener.articoleEtapaOpened();

	}

	private void saveEvent(int position, ViewHolder viewHolder) {

		currentView = viewHolder;
		currentPosition = position;
		etapaCurenta = listEtape.get(position);

		hasStartCursa();

		if (etapaCurenta.getTipEtapa() == EnumTipEtapa.SOSIRE) {
			if (hasStartCursa())
				getPozitieCurenta();
			else
				showStartCursaDialog();
		} else {
			dealSaveEvent();
		}

	}

	private void setVisibilityArticoleEtapa(ViewHolder viewHolder, Etapa etapa) {
		if (etapa.getTipBorderou() == TipBorderou.APROVIZIONARE)
			viewHolder.btnArticole.setVisibility(View.VISIBLE);
		else
			viewHolder.btnArticole.setVisibility(View.INVISIBLE);
	}

	
	private void getPozitieCurenta() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrBorderou", etapaCurenta.getDocument());

		opBorderouri.getPozitieCurenta(params);
	}

	private void showStartCursaDialog() {
		String infoText = "Marcati mai intai evenimentul Inceput cursa.";
		showInfoDialog(infoText);
	}

	private void dealSaveEvent() {
		switch (etapaCurenta.getTipEtapa()) {
		case SFARSIT_INCARCARE:
			setSfarsitIncarcareEv(etapaCurenta.getDocument());
			break;
		case START_BORD:
			checkConditiiEtape();
			break;
		case STOP_BORD:
			showSfarsitCursaAlertDialog();
			break;
		case SOSIRE:
			performSaveNewEventClienti();
			break;
		default:
			break;

		}

	}

	private void checkConditiiEtape() {

		boolean hasEtapeOrdonate = BorderouriHelper.hasEtapeOrdonate(listEtape);
		boolean hasSfarsitIncarcare = BorderouriHelper.hasSfarsitIncarcare(listEtape);

		if (hasEtapeOrdonate && hasSfarsitIncarcare)
			saveStartStopEvent(etapaCurenta.getTipEtapa());
		else if (!hasSfarsitIncarcare) {
			CustomInfoDialog infoDialog = new CustomInfoDialog(context);
			infoDialog.setInfoText("Marcati mai intai Sfarsit incarcare.");
			infoDialog.show();
		} else if (!hasEtapeOrdonate) {
			CustomInfoDialog infoDialog = new CustomInfoDialog(context);
			infoDialog.setInfoText("Completati mai intai ordinea livrarilor.");
			infoDialog.show();
		}

	}

	private void checkEtapeRamase() {
		int etapeRamase = 0;

		for (Etapa etapa : listEtape) {

			if (etapa.getTipEtapa() == EnumTipEtapa.SFARSIT_INCARCARE && !etapa.isSalvata()) {
				etapeRamase = 100;

			}

			if (etapa.getTipEtapa() == EnumTipEtapa.START_BORD && !etapa.isSalvata()) {
				etapeRamase = 100;

			}

			if (etapa.getTipEtapa() == EnumTipEtapa.SOSIRE && !etapa.isSalvata())
				etapeRamase++;

		}

		// de verificat in noul context de ordonare a etapelor
		/*
		 * if (etapeRamase <= 2 && listEtape.size() > 0) { if
		 * (borderouriListener != null) borderouriListener.verificaBordeoruri();
		 * }
		 */

	}

	private void dealCancelEvent() {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("tipEveniment", etapaCurenta.getTipEtapa().toString());
		params.put("nrDocument", etapaCurenta.getDocument());
		params.put("codClient", etapaCurenta.getCodClient());
		params.put("codSofer", UserInfo.getInstance().getId());

		opBorderouri.cancelEvent(params);

	}

	private void performSaveNewEventClienti() {

		EvenimentNou ev = new EvenimentNou();
		ev.setCodSofer(UserInfo.getInstance().getId());
		ev.setDocument(etapaCurenta.getDocument());
		ev.setClient(etapaCurenta.getCodClient());
		ev.setCodAdresa(etapaCurenta.getCodAdresaClient());
		ev.setEveniment("0");
		ev.setTipEveniment(TipEveniment.NOU);
		ev.setData(DateUtils.getCurrentDate());
		ev.setOra(DateUtils.getCurrentTime());
		ev.setBordParent(borderou.getBordParent());

		opBorderouri.saveNewEventClient(ev);

	}

	private void showAnuleazaAlertDialog() {

		alertDialog.setTipOperatie(EnumOpConfirm.SOSIRE);
		alertDialog.setAlertText("Confirmati anularea ?");
		alertDialog.setOKButtonText("Da");
		alertDialog.setCancelButtonText("Nu");
		alertDialog.show();

	}

	private void showSfarsitCursaAlertDialog() {

		if (hasStartCursa()) {
			if (areToateDescarcarile()) {
				alertDialog.setTipOperatie(EnumOpConfirm.SFARSIT_CURSA);
				alertDialog.setAlertText("Aceasta operatie nu poate fi anulata. Borderoul curent nu va mai putea fi modificat. Confirmati sfarsit cursa ?");
				alertDialog.setOKButtonText("Da");
				alertDialog.setCancelButtonText("Nu");
				alertDialog.show();
			} else {
				alertDialog.setTipOperatie(EnumOpConfirm.CURSA_INCOMPLETA);
				alertDialog.setAlertText("Ati ajuns la ultimul client dar nu ati livrat la toti clientii. Continuati livrarea ?");
				alertDialog.setOKButtonText("Da");
				alertDialog.setCancelButtonText("Nu. Confirm sfarsit cursa.");
				alertDialog.show();
			}
		}

	}

	private boolean areToateDescarcarile() {

		for (Etapa etapa : listEtape) {
			if (etapa.getTipEtapa() == EnumTipEtapa.SOSIRE)
				if (!etapa.isSalvata())
					return false;
		}

		return true;

	}

	private boolean hasStartCursa() {

		if (borderou.getBordParent().equals("-1"))
			for (Etapa etapa : listEtape) {
				if (etapa.getTipEtapa() == EnumTipEtapa.START_BORD && !etapa.isSalvata())
					return false;
			}
		else
			for (Etapa etapa : listEtape) {
				if (etapa.getPozitie() == null)
					return false;
			}
		return true;
	}

	private void saveStartStopEvent(EnumTipEtapa tipEtapa) {

		String tipEveniment = "";
		String client = etapaCurenta.getDocument();
		String codAdresa = " ";

		if (tipEtapa == EnumTipEtapa.START_BORD)
			tipEveniment = "0";
		else if (tipEtapa == EnumTipEtapa.STOP_BORD) {
			tipEveniment = "P";
			client = etapaCurenta.getCodClient();
			codAdresa = etapaCurenta.getCodAdresaClient();
		}

		OperatiiBorderouriDAOImpl newEvent = new OperatiiBorderouriDAOImpl(context);
		newEvent.setEventListener(EtapeAdapter.this);

		HashMap<String, String> newEventData = new HashMap<String, String>();
		newEventData.put("codSofer", UserInfo.getInstance().getId());
		newEventData.put("document", etapaCurenta.getDocument());
		newEventData.put("client", client);
		newEventData.put("codAdresa", codAdresa);
		newEventData.put("eveniment", tipEveniment);
		newEventData.put("bordParent", borderou.getBordParent());
		newEventData.put("evBord", tipEtapa.toString());

		newEvent.saveNewEventBorderou(newEventData, listEtape);

	}

	private void setSfarsitIncarcareEv(String codBorderou) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("document", codBorderou);
		params.put("codSofer", UserInfo.getInstance().getId());
		opEvenimente.setSfarsitIncarcare(params);

	}

	private void cancelEvent(final int position, final ViewHolder viewHolder) {
		currentView = viewHolder;
		currentPosition = position;
		etapaCurenta = listEtape.get(position);
		showAnuleazaAlertDialog();
	}

	private void setSaveButtonsStatus(int position, ViewHolder currentView) {
		if (listEtape.get(position).isSalvata()) {
			currentView.btnSalvareEveniment.setVisibility(View.GONE);
			currentView.checkedIcon.setVisibility(View.VISIBLE);
			currentView.btnAnulareEveniment.setVisibility(View.VISIBLE);
		} else {
			currentView.btnSalvareEveniment.setVisibility(View.VISIBLE);
			currentView.btnAnulareEveniment.setVisibility(View.INVISIBLE);
			currentView.checkedIcon.setVisibility(View.GONE);
		}

	}

	private void handleSfarsitIncarcareEvent(String result) {

		// sf. incarcare produs
		if (result.contains("SOF")) {
			etapaCurenta.setSalvata(true);
			setSaveButtonsStatus(currentPosition, currentView);

		} else {

		}

	}

	private void computeClientDistance(String coords) {

		boolean rangeOk = false;
		double distClient = 0;

		String[] strCoords = coords.split("x");
		LatLng latlngCurent = new LatLng(Double.valueOf(strCoords[0]), Double.valueOf(strCoords[1]));

		if (latlngCurent.latitude != 0) {
			LatLng latlngClient = null;

			try {
				latlngClient = MapUtils.geocodeSimpleAddress(etapaCurenta.getFactura().getAdresaClient(), context);
			} catch (Exception e) {
				rangeOk = true;
			}

			if (latlngClient != null && latlngClient.latitude != 0) {
				distClient = MapUtils.distanceXtoY(latlngCurent.latitude, latlngCurent.longitude, latlngClient.latitude, latlngClient.longitude, "K");

				if (distClient > Constants.RAZA_SOSIRE_CLIENT_KM) {
					rangeOk = false;
				} else
					rangeOk = true;
			} else
				rangeOk = true;
		} else {
			rangeOk = true;
		}

		if (rangeOk)
			dealSaveEvent();
		else {
			String infoText = "Evenimentul nu poate fi salvat. Aceasta adresa se afla la " + Math.round(distClient) + " km fata de pozitia curenta.";
			showInfoDialog(infoText);
		}

	}

	private void showInfoDialog(String infoText) {
		CustomInfoDialog infoDialog = new CustomInfoDialog(context);
		infoDialog.setInfoText(infoText);
		infoDialog.show();

	}

	private void setPozitiiLivrare() {

		int totalPozitii = listEtape.size();

		pozitiiDisponibile = new ArrayList<String>();

		int count = 1;
		for (Etapa etapa : listEtape) {
			if (!etapa.getTipEtapa().equals(EnumTipEtapa.SFARSIT_INCARCARE) && !etapa.getTipEtapa().equals(EnumTipEtapa.START_BORD)) {
				pozitiiDisponibile.add(String.valueOf(count));
				count++;
			}

		}

		Iterator<String> iterator = pozitiiDisponibile.iterator();

		while (iterator.hasNext()) {
			String currentPos = iterator.next();

			for (int j = 0; j < totalPozitii; j++) {
				if (currentPos.equals(listEtape.get(j).getPozitie())) {
					iterator.remove();
					break;
				}

			}
		}

		pozitieLivrareDialog.setPozitiiLivrare(pozitiiDisponibile);

	}

	private void handleSavedEvent() {

		if (etapaCurenta.getTipEtapa() == EnumTipEtapa.STOP_BORD) {
			if (etapeListener != null)
				etapeListener.borderouTerminat();
		} else {
			etapaCurenta.setSalvata(true);
			setSaveButtonsStatus(currentPosition, currentView);

			checkEtapeRamase();

		}

		notifyDataSetChanged();
	}

	private void handleCancelEvent() {
		etapaCurenta.setSalvata(false);
		setSaveButtonsStatus(currentPosition, currentView);
	}

	public void setOperatiiEtapeListener(OperatiiEtapeListener etapeListener) {
		this.etapeListener = etapeListener;
	}

	public void setBorderouriListener(BorderouriListener listener) {
		this.borderouriListener = listener;
	}

	@Override
	public int getCount() {
		return listEtape.size();
	}

	@Override
	public Etapa getItem(int index) {
		return listEtape.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	private void setPozitieEtapa(String pozitie) {
		listEtape.get(btnPositionPosition).setPozitie(pozitie);

		pozitiiDisponibile.remove(pozitie);

		// alocare automata a ultimei etape
		if (pozitiiDisponibile.size() == 1) {
			for (Etapa etapa : listEtape) {

				if (!etapa.getTipEtapa().equals(EnumTipEtapa.SFARSIT_INCARCARE) && !etapa.getTipEtapa().equals(EnumTipEtapa.START_BORD)) {

					if (etapa.getPozitie() == null) {
						etapa.setPozitie(pozitiiDisponibile.get(0));
						break;
					}
				}
			}

			setPozitieSfarsitCursa();

		}

		Collections.sort(listEtape);
		notifyDataSetChanged();

	}

	private void setPozitieSfarsitCursa() {
		Collections.sort(listEtape);

		for (Etapa etapa : listEtape) {
			if (etapa.getTipEtapa() == EnumTipEtapa.STOP_BORD) {
				etapa.setTipEtapa(EnumTipEtapa.SOSIRE);
				etapa.setObservatii("");
			}
		}

		listEtape.get(listEtape.size() - 1).setTipEtapa(EnumTipEtapa.STOP_BORD);
		listEtape.get(listEtape.size() - 1).setObservatii("Sfarsit cursa");

	}

	private void schimbaSfarsitCursa() {

		int ordMax = -1;
		int poz = 0;
		for (Etapa etapa : listEtape) {
			if (!etapa.isSalvata() && etapa.getTipEtapa() != EnumTipEtapa.STOP_BORD)
				ordMax = poz;

			poz++;
		}

		if (ordMax != -1) {
			String pozMaxEtapa = listEtape.get(ordMax).getPozitie();
			String pozSelEtapa = etapaCurenta.getPozitie();

			Etapa etapaOld = etapaCurenta;

			etapaCurenta.setTipEtapa(EnumTipEtapa.SOSIRE);
			etapaCurenta.setObservatii("");
			etapaCurenta.setPozitie(pozMaxEtapa);

			Etapa etapaNew = listEtape.get(ordMax);

			listEtape.get(ordMax).setTipEtapa(EnumTipEtapa.STOP_BORD);
			listEtape.get(ordMax).setObservatii("Sfarsit cursa");
			listEtape.get(ordMax).setPozitie(pozSelEtapa);

			listEtape.set(currentPosition, etapaNew);

			listEtape.set(ordMax, etapaOld);

			Collections.sort(listEtape);

		}

		notifyDataSetChanged();

		performSaveNewEventClienti();

	}

	@Override
	public void opEventComplete(String result, EnumOperatiiEvenimente methodName) {
		switch (methodName) {
		case SET_SFARSIT_INC:
			handleSfarsitIncarcareEvent(result);
			break;
		default:
			break;
		}

	}

	@Override
	public void eventComplete(String result, EnumOperatiiEvenimente methodName, EnumNetworkStatus networkStatus) {
		switch (methodName) {
		case SAVE_NEW_EVENT:
			handleSavedEvent();
			break;
		case CANCEL_EVENT:
			handleCancelEvent();
			notifyDataSetChanged();
			break;
		case GET_POZITIE:
			computeClientDistance(result);
			break;
		default:
			break;

		}

	}

	@Override
	public void alertDialogOk(EnumOpConfirm tipOperatie) {
		switch (tipOperatie) {
		case SOSIRE:
			dealCancelEvent();
			break;
		case SFARSIT_CURSA:
			saveStartStopEvent(etapaCurenta.getTipEtapa());
			break;
		case CURSA_INCOMPLETA:
			schimbaSfarsitCursa();
			break;
		default:
			break;
		}

	}

	@Override
	public void alertDialogCancel(EnumOpConfirm tipOperatie) {
		switch (tipOperatie) {
		case CURSA_INCOMPLETA:
			saveStartStopEvent(etapaCurenta.getTipEtapa());
			break;
		default:
			break;
		}

	}

	@Override
	public void pozitieSelected(String pozitie) {
		setPozitieEtapa(pozitie);

	}

	@Override
	public void articoleEtapaOpened() {

	}

	@Override
	public void articoleEtapaClosed() {
		if (articoleListener != null)
			articoleListener.articoleEtapaClosed();

	}

}

package com.distributie.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.distributie.beans.Factura;
import com.distributie.enums.TipBorderou;
import com.distributie.view.CustomAdapter;
import com.distributie.view.R;

public class FacturiBorderou {

	private Context context;
	private int selectedClientIndex = -1;
	private ArrayList<HashMap<String, String>> arrayListFacturi = null;

	public FacturiBorderou(Context context) {
		this.context = context;
	}

	public CustomAdapter getFacturiBorderouAdapter(ArrayList<Factura> facturiArray, TipBorderou tipDocument, boolean showTraseu) {

		CustomAdapter adapterFacturi = null;
		arrayListFacturi = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> temp = null;

		if (tipDocument == TipBorderou.DISTRIBUTIE) {

			adapterFacturi = new CustomAdapter(context, arrayListFacturi, R.layout.custom_row_list_facturi,
					new String[] { "nrCrt", "numeClient", "codClient", "adresaClient", "ev1", "timpEv1", "ev2",
							"timpEv2", "tipClient", "codAdresa" }, new int[] { R.id.textNrCrt, R.id.textNumeClient,
							R.id.textAdresaClient, R.id.textCodClient, R.id.textEv1, R.id.textTimpEv1, R.id.textEv2,
							R.id.textTimpEv2, R.id.textTipClient, R.id.textCodAdresa }, showTraseu);

			for (int i = 0; i < facturiArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("nrCrt", String.valueOf(i + 1) + ".");
				temp.put("numeClient", facturiArray.get(i).getNumeClient());
				temp.put("codClient", facturiArray.get(i).getCodClient());
				temp.put("adresaClient", facturiArray.get(i).getAdresaClient());
				temp.put("tipClient", "c");
				temp.put("codAdresa", facturiArray.get(i).getCodAdresaClient());

				if (!facturiArray.get(i).getSosireClient().equals("0")) {
					temp.put("ev1", "Sosire:");
					temp.put("timpEv1",
							facturiArray.get(i).getSosireClient().substring(0, 2) + ":"
									+ facturiArray.get(i).getSosireClient().substring(2, 4));
				} else {
					temp.put("ev1", " ");
					temp.put("timpEv1", " ");
				}

				if (!facturiArray.get(i).getPlecareClient().equals("0")) {
					temp.put("ev2", "Plecare:");
					temp.put("timpEv2", facturiArray.get(i).getPlecareClient().substring(0, 2) + ":"
							+ facturiArray.get(i).getPlecareClient().substring(2, 4));
				} else {

					temp.put("ev2", " ");
					temp.put("timpEv2", " ");
				}

				if (isClientSelected(facturiArray, i)) {
					selectedClientIndex = i;
				}

				arrayListFacturi.add(temp);
			}

		}// sf. distributie

		
		
		
		// aprovizionare
		if (tipDocument == TipBorderou.APROVIZIONARE || tipDocument == TipBorderou.INCHIRIERE
				|| tipDocument == TipBorderou.SERVICE) {

			adapterFacturi = new CustomAdapter(context, arrayListFacturi, R.layout.custom_row_list_facturi_aprov,
					new String[] { "nrCrt", "numeClient", "codClient", "ev1", "timpEv1", "ev2", "timpEv2",
							"adresaClient", "tipClient", "btnTraseu" }, new int[] { R.id.textNrCrt,
							R.id.textNumeClient, R.id.textCodClient, R.id.textEv1, R.id.textTimpEv1, R.id.textEv2,
							R.id.textTimpEv2, R.id.textAdresaClient, R.id.textTipClient, R.id.btnTraseu }, showTraseu);

			int lastIndex = 1;

			for (int i = 0; i < facturiArray.size(); i++) {
				temp = new HashMap<String, String>();

				if (0 == i) {

					temp.put("nrCrt", String.valueOf(lastIndex) + ".");
					lastIndex++;
					temp.put("numeClient", facturiArray.get(i).getNumeFurnizor());
					temp.put("codClient", facturiArray.get(i).getCodFurnizor());
					temp.put("adresaClient", facturiArray.get(i).getAdresaFurnizor());
					temp.put("tipClient", "f");
					temp.put("codAdresa", facturiArray.get(i).getCodAdresaFurnizor());
					

					if (!facturiArray.get(i).getSosireFurnizor().equals("0")) {
						temp.put("ev1", "Sosire:");
						temp.put("timpEv1", facturiArray.get(i).getSosireFurnizor().substring(0, 2) + ":"
								+ facturiArray.get(i).getSosireFurnizor().substring(2, 4));
					} else {
						temp.put("ev1", " ");
						temp.put("timpEv1", " ");
					}

					if (!facturiArray.get(i).getPlecareFurnizor().equals("0")) {
						temp.put("ev2", "Plecare:");
						temp.put("timpEv2", facturiArray.get(i).getPlecareFurnizor().substring(0, 2) + ":"
								+ facturiArray.get(i).getPlecareFurnizor().substring(2, 4));
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
					temp.put("tipClient", "c");
					temp.put("codAdresa", facturiArray.get(i).getCodAdresaClient());

					if (!facturiArray.get(i).getSosireClient().equals("0")) {
						temp.put("ev1", "Sosire:");
						temp.put("timpEv1", facturiArray.get(i).getSosireClient().substring(0, 2) + ":"
								+ facturiArray.get(i).getSosireClient().substring(2, 4));
					} else {
						temp.put("ev1", " ");
						temp.put("timpEv1", " ");
					}

					if (!facturiArray.get(i).getPlecareClient().equals("0")) {
						temp.put("ev2", "Plecare:");
						temp.put("timpEv2", facturiArray.get(i).getPlecareClient().substring(0, 2) + ":"
								+ facturiArray.get(i).getPlecareClient().substring(2, 4));
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
					temp.put("tipClient", "c");
					temp.put("codAdresa", facturiArray.get(i).getCodAdresaClient());
					

					if (!facturiArray.get(i).getSosireClient().equals("0")) {
						temp.put("ev1", "Sosire:");
						temp.put("timpEv1", facturiArray.get(i).getSosireClient().substring(0, 2) + ":"
								+ facturiArray.get(i).getSosireClient().substring(2, 4));
					} else {
						temp.put("ev1", " ");
						temp.put("timpEv1", " ");
					}

					if (!facturiArray.get(i).getPlecareClient().equals("0")) {
						temp.put("ev2", "Plecare:");
						temp.put("timpEv2", facturiArray.get(i).getPlecareClient().substring(0, 2) + ":"
								+ facturiArray.get(i).getPlecareClient().substring(2, 4));
					} else {

						temp.put("ev2", " ");
						temp.put("timpEv2", " ");
					}

					arrayListFacturi.add(temp);

				}

				if (CurrentStatus.getInstance().getCurrentClient().equals(facturiArray.get(i).getCodFurnizor())) {
					selectedClientIndex = i;
				}

			}

		}// sf. aprovizionare

		return adapterFacturi;

	}

	private boolean isClientSelected(ArrayList<Factura> facturiArray, int pos) {
		try {
			return CurrentStatus.getInstance().getCurrentClient().equals(facturiArray.get(pos).getCodClient())
					&& CurrentStatus.getInstance().getCurentClientAddr()
							.equals(facturiArray.get(pos).getCodAdresaClient());
		} catch (Exception ex) {
			return false;
		}
	}

	public int getSelectedClientIndex() {
		return selectedClientIndex;
	}

	public void setSelectedClientIndex(int selectedClientIndex) {
		this.selectedClientIndex = selectedClientIndex;
	}

	public ArrayList<HashMap<String, String>> getArrayListFacturi() {
		return arrayListFacturi;
	}

}

package com.distributie.model;

import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;

import com.distributie.beans.Etapa;

public class EtapeDTI {

	private List<Etapa> etapeDTI = new ArrayList<Etapa>();

	public void setEtapeDTI(List<Etapa> listEtape) {
		this.etapeDTI.addAll(listEtape);

	}

	public List<Etapa> getEtapeDTI() {
		return etapeDTI;
	}

	public int getEtapeDTISize() {
		return etapeDTI == null ? 0 : etapeDTI.size();
	}

	public void getEtapeFromListView(ListView listView) {
		if (listView == null)
			return;

		if (listView.getAdapter() == null)
			return;

		int nrItems = listView.getAdapter().getCount();

		for (int i = 0; i < nrItems; i++)
			etapeDTI.add((Etapa) listView.getAdapter().getItem(i));

	}

	public int verificaEtapeNoi(List<Etapa> etapeNoi) {

		boolean esteNoua = true;
		int pozitieEtapa = -1;
		int cont = 0;

		if (etapeDTI.isEmpty())
			return -1;

		for (Etapa etapaNoua : etapeNoi) {
			esteNoua = true;
			for (Etapa etapaVeche : etapeDTI) {
				if (etapaVeche.getCodClient().equals(etapaNoua.getCodClient()) && etapaVeche.getCodAdresaClient().equals(etapaNoua.getCodAdresaClient())) {
					esteNoua = false;
					break;
				}

			}

			if (esteNoua) {
				etapaNoua.setEtapaNoua(true);
				pozitieEtapa = cont;
			}

			cont++;

		}

		return pozitieEtapa;

	}

	public int getEtapaNesalvata(List<Etapa> listEtape) {

		int i = 0;
		for (Etapa etapa : listEtape) {
			if (!etapa.isSalvata())
				return i;
			i++;
		}

		return -1;
	}

}

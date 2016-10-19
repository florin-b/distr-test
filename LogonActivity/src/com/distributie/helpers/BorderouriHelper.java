package com.distributie.helpers;

import java.util.List;

import com.distributie.beans.Etapa;

public class BorderouriHelper {

	public static boolean hasEtapeOrdonate(List<Etapa> listEtape) {

		for (Etapa etapa : listEtape) {

			if (!etapa.getNume().contains("INCEPUT") && !etapa.getNume().contains("INCARCARE"))
				if (etapa.getPozitie() == null)
					return false;
		}

		return true;

	}

}

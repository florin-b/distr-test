package com.distributie.helpers;

import java.util.List;

import com.distributie.beans.Etapa;
import com.distributie.enums.EnumTipEtapa;

public class BorderouriHelper {

	public static boolean hasEtapeOrdonate(List<Etapa> listEtape) {

		for (Etapa etapa : listEtape) {

			if (!etapa.getNume().contains("INCEPUT") && !etapa.getNume().contains("INCARCARE"))
				if (etapa.getPozitie() == null)
					return false;
		}

		return true;

	}

	public static boolean hasSfarsitIncarcare(List<Etapa> listEtape) {

		for (Etapa etapa : listEtape)
			if (etapa.getTipEtapa() == EnumTipEtapa.SFARSIT_INCARCARE && !etapa.isSalvata())
				return false;

		return true;
	}

}

package com.distributie.utils;

import com.distributie.beans.Etapa;
import com.distributie.enums.EnumTipEtapa;

public class HelperEtape {

	public static String getNumeEveniment(Etapa etapa) {
		String numeEveniment = "";

		switch (etapa.getTipBorderou()) {
		case SERVICE:
		case APROVIZIONARE:
			if (etapa.getTipEtapa() == EnumTipEtapa.START_BORD)
				numeEveniment = "INCEPUT CURSA";
			if (etapa.getTipEtapa() == EnumTipEtapa.SOSIRE || etapa.getTipEtapa() == EnumTipEtapa.STOP_BORD)
				numeEveniment = "SOSIRE";
			break;
		default:
			if (!etapa.getNume().contains("SFARSIT") && !etapa.getNume().contains("INCEPUT")) {
				numeEveniment = "SOSIRE";
			} else {
				numeEveniment = etapa.getNume();
			}

			break;

		}

		return numeEveniment;
	}

}

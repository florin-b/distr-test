package com.distributie.utils;

import com.distributie.model.UserInfo;

public class Messages {

	public static String getSfarsitBordMessage(boolean borderouDTI) {
		String msg;

		if (UserInfo.getInstance().isDti()) {
			if (borderouDTI)
				msg = "PENTRU URMATOAREA CURSA CONTACTATI AGENTUL DTI LA CARE SUNTETI REPARTIZAT.";
			else
				msg = "PENTRU URMATOAREA CURSA CONTACTATI LOGISTICIANUL DIN FILIALA IN CARE ATI EXECUTAT CURSA INCHEIATA.";
		} else {
			msg = "Intoarceti-va la filiala.";
		}

		return msg;
	}

}

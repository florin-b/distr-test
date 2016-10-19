package com.distributie.model;

import java.util.HashMap;
import java.util.List;

import com.distributie.beans.Etapa;
import com.distributie.beans.EvenimentNou;

public interface OperatiiBorderouriDAO {
	public void getDocEvents(String nrBorderou, String tipEv);

	public void saveNewEventBorderou(HashMap<String, String> params, List<Etapa> listEtape);

	public void saveNewEventClient(HashMap<String, String> params);
	
	public void saveNewEventClient(EvenimentNou newEvent);
	
	public void cancelEvent(HashMap<String, String> params);
	
	public void getPozitieCurenta(HashMap<String, String> params);
	
	public void isBorderouStarted();
	
	public void saveLocalObjects();
	
}

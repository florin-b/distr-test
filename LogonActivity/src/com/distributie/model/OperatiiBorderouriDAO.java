package com.distributie.model;

import java.util.HashMap;

public interface OperatiiBorderouriDAO {
	public void getDocEvents(String nrBorderou, String tipEv);

	public void saveNewEventBorderou(HashMap<String, String> params);

	public void saveNewEventClient(HashMap<String, String> params);
}

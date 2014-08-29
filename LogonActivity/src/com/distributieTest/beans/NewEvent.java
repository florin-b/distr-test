package com.distributieTest.beans;

import java.io.Serializable;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

@SuppressWarnings("serial")
public class NewEvent implements Serializable, KvmSerializable {

	private String codSofer;
	private String document;
	private String client;
	private String codAdresa;
	private String eveniment;
	private String truckData;

	public NewEvent() {

	}

	public String getCodSofer() {
		return codSofer;
	}

	public void setCodSofer(String codSofer) {
		this.codSofer = codSofer;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCodAdresa() {
		return codAdresa;
	}

	public void setCodAdresa(String codAdresa) {
		this.codAdresa = codAdresa;
	}

	public String getEveniment() {
		return eveniment;
	}

	public void setEveniment(String eveniment) {
		this.eveniment = eveniment;
	}

	public String getTruckData() {
		return truckData;
	}

	public void setTruckData(String truckData) {
		this.truckData = truckData;
	}

	public int getPropertyCount() {
		return 6;
	}

	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "codSofer";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "document";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "client";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "codAdresa";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "eveniment";
			break;
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "truckData";
			break;
		default:
			break;

		}

	}

	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return codSofer;
		case 1:
			return document;
		case 2:
			return client;
		case 3:
			return codAdresa;
		case 4:
			return eveniment;
		case 5:
			return truckData;

		}
		return null;
	}

	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			codSofer = value.toString();
			break;
		case 1:
			document = value.toString();
			break;
		case 2:
			client = value.toString();
			break;
		case 3:
			codAdresa = value.toString();
			break;
		case 4:
			eveniment = value.toString();
			break;
		case 5:
			truckData = value.toString();
			break;

		default:
			break;
		}

	}

}

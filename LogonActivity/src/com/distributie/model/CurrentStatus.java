package com.distributie.model;

import com.distributie.enums.TipBorderou;

public class CurrentStatus {

	private String eveniment;
	private String nrBorderou;
	private TipBorderou tipBorderou;
	private String currentClient;
	private String curentClientAddr;
	private String currentClientName;
	private String evenimentClient;

	private static CurrentStatus instance = new CurrentStatus();

	private CurrentStatus() {

	}

	public static CurrentStatus getInstance() {
		return instance;
	}

	public String getEveniment() {
		return eveniment;
	}

	public void setEveniment(String eveniment) {
		this.eveniment = eveniment;
	}

	public String getNrBorderou() {
		return nrBorderou;
	}

	public void setNrBorderou(String nrBorderou) {
		this.nrBorderou = nrBorderou;
	}

	public TipBorderou getTipBorderou() {
		return tipBorderou;
	}

	public void setTipBorderou(TipBorderou tipBorderou) {
		this.tipBorderou = tipBorderou;
	}

	public String getCurrentClient() {

		if (currentClient == null)
			return "";

		return currentClient;
	}

	public void setCurrentClient(String currentClient) {
		this.currentClient = currentClient;
	}

	public String getCurentClientAddr() {
		return curentClientAddr;
	}

	public void setCurentClientAddr(String curentClientAddr) {
		this.curentClientAddr = curentClientAddr;
	}

	public String getCurrentClientName() {
		return currentClientName;
	}

	public void setCurrentClientName(String currentClientName) {
		this.currentClientName = currentClientName;
	}

	public String getEvenimentClient() {
		return evenimentClient;
	}

	public void setEvenimentClient(String evenimentClient) {
		this.evenimentClient = evenimentClient;
	}

	public void initData() {
		eveniment = "";
		nrBorderou = "";
		tipBorderou = null;
		currentClient = "";
		curentClientAddr = "";
		currentClientName = "";
		evenimentClient = "";
	}

}

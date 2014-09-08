package com.distributie.beans;

public class InitStatus {

	private String document;
	private String client;
	private String eveniment;

	private static InitStatus instance = new InitStatus();

	private InitStatus() {

	}

	public static InitStatus getInstance() {
		return instance;
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

	public String getEveniment() {
		return eveniment;
	}

	public void setEveniment(String eveniment) {
		this.eveniment = eveniment;
	}

}

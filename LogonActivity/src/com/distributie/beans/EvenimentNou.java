package com.distributie.beans;

import java.io.Serializable;

import com.distributie.enums.TipEveniment;

public class EvenimentNou implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codSofer;
	private String document;
	private String client;
	private String codAdresa;
	private String eveniment;
	private TipEveniment tipEveniment;
	private String data;
	private String ora;
	private String bordParent;

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

	public TipEveniment getTipEveniment() {
		return tipEveniment;
	}

	public void setTipEveniment(TipEveniment tipEveniment) {
		this.tipEveniment = tipEveniment;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getBordParent() {
		return bordParent;
	}

	public void setBordParent(String bordParent) {
		this.bordParent = bordParent;
	}

	@Override
	public String toString() {
		return "EvenimentNou [codSofer=" + codSofer + ", document=" + document + ", client=" + client + ", codAdresa=" + codAdresa + ", eveniment=" + eveniment
				+ ", tipEveniment=" + tipEveniment + ", data=" + data + ", ora=" + ora + ", bordParent=" + bordParent + "]";
	}

}

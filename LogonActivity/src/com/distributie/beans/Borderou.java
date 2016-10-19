package com.distributie.beans;

import com.distributie.enums.TipBorderou;

public class Borderou {

	private String numarBorderou;
	private String dataEmiterii;
	private String evenimentBorderou;
	private String tipBorderou;
	private TipBorderou stringTipBorderou;
	private String bordParent;

	public Borderou() {

	}

	public String getNumarBorderou() {
		return numarBorderou;
	}

	public void setNumarBorderou(String numarBorderou) {
		this.numarBorderou = numarBorderou;
	}

	public String getDataEmiterii() {
		return dataEmiterii;
	}

	public void setDataEmiterii(String dataEmiterii) {
		this.dataEmiterii = dataEmiterii;
	}

	public String getEvenimentBorderou() {
		return evenimentBorderou;
	}

	public void setEvenimentBorderou(String evenimentBorderou) {
		this.evenimentBorderou = evenimentBorderou;
	}

	public String getTipBorderou() {
		return tipBorderou;
	}

	public void setTipBorderou(String tipBorderou) {
		this.tipBorderou = tipBorderou;
	}

	public String getBordParent() {
		return bordParent;
	}

	public void setBordParent(String bordParent) {
		this.bordParent = bordParent;
	}

	public TipBorderou getStandardTipBorderou() {
		TipBorderou borderou = null;

		if (tipBorderou.equals("1110")) {
			borderou = TipBorderou.DISTRIBUTIE;
		}

		if (tipBorderou.equals("1120")) {
			borderou = TipBorderou.APROVIZIONARE;
		}

		if (tipBorderou.equals("1121")) {
			borderou = TipBorderou.SERVICE;
		}

		if (tipBorderou.equals("1122")) {
			borderou = TipBorderou.INCHIRIERE;
		}

		if (tipBorderou.equals("1123")) {
			borderou = TipBorderou.PALETI;
		}

		return borderou;
	}

}

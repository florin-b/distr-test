package com.distributie.beans;

import com.distributie.enums.EnumTipEtapa;
import com.distributie.enums.TipBorderou;

public class Etapa implements Comparable<Etapa> {

	private String cod;
	private String nume;
	private String descriere;
	private String document;
	private int pos;
	private EnumTipEtapa tipEtapa;
	private boolean salvata;
	private Factura factura;
	private TipBorderou tipBorderou;
	private String observatii = "";
	private String pozitie;

	public Etapa() {

	}

	public Etapa(String cod, String nume, String descriere, String document) {
		super();
		this.cod = cod;
		this.nume = nume;
		this.descriere = descriere;
		this.document = document;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getNume() {

		if (tipBorderou == TipBorderou.DISTRIBUTIE) {
			return factura.getNumeClient() + " - " + getObservatii();
		} else if (tipBorderou == TipBorderou.APROVIZIONARE || tipBorderou == TipBorderou.INCHIRIERE || tipBorderou == TipBorderou.SERVICE) {
			return factura.getNumeFurnizor() + " - " + getObservatii();
		}

		return nume.toUpperCase();
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getDescriere() {

		if (tipBorderou == TipBorderou.DISTRIBUTIE) {
			return factura.getAdresaClient();
		} else if (tipBorderou == TipBorderou.APROVIZIONARE || tipBorderou == TipBorderou.INCHIRIERE || tipBorderou == TipBorderou.SERVICE) {
			return factura.getAdresaFurnizor();
		}

		return descriere;
	}

	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public EnumTipEtapa getTipEtapa() {
		return tipEtapa;
	}

	public void setTipEtapa(EnumTipEtapa tipEtapa) {
		this.tipEtapa = tipEtapa;
	}

	public boolean isSalvata() {

		if (tipEtapa == EnumTipEtapa.SOSIRE) {
			if (tipBorderou == TipBorderou.DISTRIBUTIE) {
				return !factura.getSosireClient().equals("0") || salvata;
			} else if (tipBorderou == TipBorderou.APROVIZIONARE || tipBorderou == TipBorderou.INCHIRIERE || tipBorderou == TipBorderou.SERVICE) {
				return !factura.getSosireFurnizor().equals("0") || salvata;
			}
		}

		return salvata;
	}

	public void setSalvata(boolean salvata) {
		this.salvata = salvata;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public TipBorderou getTipBorderou() {
		return tipBorderou;
	}

	public void setTipBorderou(TipBorderou tipBorderou) {
		this.tipBorderou = tipBorderou;
	}

	public String getCodClient() {
		if (tipBorderou == TipBorderou.DISTRIBUTIE) {
			return factura.getCodClient();
		} else {
			return factura.getCodFurnizor();
		}
	}

	public String getCodAdresaClient() {

		if (tipBorderou == TipBorderou.DISTRIBUTIE) {
			return factura.getCodAdresaClient();
		} else {
			return factura.getCodAdresaFurnizor();
		}

	}

	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	public String getPozitie() {
		return pozitie;
	}

	public void setPozitie(String pozitie) {
		this.pozitie = pozitie;
	}

	@Override
	public int compareTo(Etapa arg0) {

		if (this.getPozitie() == null || arg0.getPozitie() == null)
			return 0;
		else
			return Integer.valueOf(this.getPozitie()) - Integer.valueOf(arg0.getPozitie());
	}

}

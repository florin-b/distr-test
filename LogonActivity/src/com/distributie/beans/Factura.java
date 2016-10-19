package com.distributie.beans;

public class Factura {

	private String nrFactura;

	private String codClient;
	private String numeClient;
	private String adresaClient;
	private String sosireClient;
	private String plecareClient;
	private String codAdresaClient;

	private String codFurnizor;
	private String numeFurnizor;
	private String adresaFurnizor;
	private String sosireFurnizor;
	private String plecareFurnizor;
	private String codAdresaFurnizor;

	private String dataStartCursa;

	private String pozitie;

	public Factura() {
	}

	public String getNrFactura() {
		return nrFactura;
	}

	public void setNrFactura(String nrFactura) {
		this.nrFactura = nrFactura;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public String getAdresaClient() {
		return adresaClient;
	}

	public void setAdresaClient(String adresaClient) {
		this.adresaClient = adresaClient;
	}

	public String getSosireClient() {
		return sosireClient;
	}

	public void setSosireClient(String sosireClient) {
		this.sosireClient = sosireClient;
	}

	public String getPlecareClient() {
		return plecareClient;
	}

	public void setPlecareClient(String plecareClient) {
		this.plecareClient = plecareClient;
	}

	public String getCodFurnizor() {
		return codFurnizor;
	}

	public void setCodFurnizor(String codFurnizor) {
		this.codFurnizor = codFurnizor;
	}

	public String getNumeFurnizor() {
		return numeFurnizor;
	}

	public void setNumeFurnizor(String numeFurnizor) {
		this.numeFurnizor = numeFurnizor;
	}

	public String getAdresaFurnizor() {
		return adresaFurnizor;
	}

	public void setAdresaFurnizor(String adresaFurnizor) {
		this.adresaFurnizor = adresaFurnizor;
	}

	public String getSosireFurnizor() {
		return sosireFurnizor;
	}

	public void setSosireFurnizor(String sosireFurnizor) {
		this.sosireFurnizor = sosireFurnizor;
	}

	public String getPlecareFurnizor() {
		return plecareFurnizor;
	}

	public void setPlecareFurnizor(String plecareFurnizor) {
		this.plecareFurnizor = plecareFurnizor;
	}

	public String getCodAdresaClient() {
		return codAdresaClient;
	}

	public void setCodAdresaClient(String codAdresaClient) {
		this.codAdresaClient = codAdresaClient;
	}

	public String getCodAdresaFurnizor() {
		return codAdresaFurnizor;
	}

	public void setCodAdresaFurnizor(String codAdresaFurnizor) {
		this.codAdresaFurnizor = codAdresaFurnizor;
	}

	public String getDataStartCursa() {
		return dataStartCursa;
	}

	public void setDataStartCursa(String dataStartCursa) {
		this.dataStartCursa = dataStartCursa;
	}

	public String getPozitie() {
		return pozitie;
	}

	public void setPozitie(String pozitie) {
		this.pozitie = pozitie;
	}

	@Override
	public String toString() {
		return "Factura [nrFactura=" + nrFactura + ", codClient=" + codClient + ", numeClient=" + numeClient + ", adresaClient=" + adresaClient
				+ ", sosireClient=" + sosireClient + ", plecareClient=" + plecareClient + ", codAdresaClient=" + codAdresaClient + ", codFurnizor="
				+ codFurnizor + ", numeFurnizor=" + numeFurnizor + ", adresaFurnizor=" + adresaFurnizor + ", sosireFurnizor=" + sosireFurnizor
				+ ", plecareFurnizor=" + plecareFurnizor + ", codAdresaFurnizor=" + codAdresaFurnizor + ", dataStartCursa=" + dataStartCursa + ", pozitie="
				+ pozitie + "]";
	}

}

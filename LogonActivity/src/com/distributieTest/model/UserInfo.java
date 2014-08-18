/**
 * @author florinb
 *
 */
package com.distributieTest.model;

public class UserInfo {

	private String nume = "";
	private String filiala = "";
	private String cod = "";
	private String unitLog = "";
	private String nrAuto = "";

	private static UserInfo instance = new UserInfo();

	private UserInfo() {
	}

	public static UserInfo getInstance() {
		return instance;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getFiliala() {
		return filiala;
	}

	public void setFiliala(String filiala) {
		this.filiala = filiala;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}

	public String getNrAuto() {
		return nrAuto;
	}

	public void setNrAuto(String nrAuto) {
		this.nrAuto = nrAuto;
	}

}

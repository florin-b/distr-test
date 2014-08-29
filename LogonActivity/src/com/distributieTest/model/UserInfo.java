/**
 * @author florinb
 *
 */
package com.distributieTest.model;

import com.distributieTest.beans.InitStatus;

public class UserInfo {

	private String nume;
	private String filiala;
	private String id;
	private String unitLog;
	private String status;
	private String departament;
	private String tipAcces;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitLog() {
		return unitLog;
	}

	public void setUnitLog(String unitLog) {
		this.unitLog = unitLog;
	}

	public String getLogonStatus() {
		return status;
	}

	public void setLogonStatus(String logonStatus) {
		this.status = logonStatus;
	}

	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getTipAcces() {
		return tipAcces;
	}

	public void setTipAcces(String tipAcces) {
		this.tipAcces = tipAcces;
	}

}

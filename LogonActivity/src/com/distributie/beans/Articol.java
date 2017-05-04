package com.distributie.beans;

import com.distributie.enums.EnumTipOperatie;

public class Articol {

	private String nume;
	private String cantitate;
	private String umCant;
	private EnumTipOperatie tipOperatiune;
	private String departament;
	private String greutate;
	private String umGreutate;

	public Articol() {

	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCantitate() {
		return cantitate;
	}

	public void setCantitate(String cantitate) {
		this.cantitate = cantitate;
	}

	public String getUmCant() {
		return umCant;
	}

	public void setUmCant(String umCant) {
		this.umCant = umCant;
	}

	public EnumTipOperatie getTipOperatiune() {
		return tipOperatiune;
	}

	public void setTipOperatiune(EnumTipOperatie tipOperatiune) {
		this.tipOperatiune = tipOperatiune;
	}

	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getGreutate() {
		return greutate;
	}

	public void setGreutate(String greutate) {
		this.greutate = greutate;
	}

	public String getUmGreutate() {
		return umGreutate;
	}

	public void setUmGreutate(String umGreutate) {
		this.umGreutate = umGreutate;
	}

}

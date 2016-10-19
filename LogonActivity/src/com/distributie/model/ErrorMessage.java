package com.distributie.model;

public enum ErrorMessage {
	CONT_INEXISTENT("Cont inexistent."), CONT_BLOCAT("Cont blocat 60 de minute"), PAROLA_GRESITA("Parola incorecta"), USER_INEXISTENT(
			"Utilizator nedefinit"), ACCES_INTERZIS("Acces interzis"), CONT_INACTIV("Cont inactiv"), LOGON_ESUAT(
			"Autentificare esuata");

	private final String message;

	private ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}

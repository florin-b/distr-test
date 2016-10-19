/**
 * @author florinb
 *
 */
package com.distributie.connectors;

public class ConnectionStrings {

	private static ConnectionStrings instance = new ConnectionStrings();

	private String myUrl;
	private String myNamespace;
	private String myDatabase;

	private ConnectionStrings() {
		this.myUrl = "http://10.1.0.58/AndroidWebServices/DistributieTESTService.asmx";
		this.myNamespace = "http://distributieTest.org/";

	}

	public static ConnectionStrings getInstance() {
		return instance;
	}

	public String getUrl() {
		return this.myUrl;
	}

	public String getNamespace() {
		return this.myNamespace;
	}

	public String getDatabaseName() {
		return this.myDatabase;
	}
}

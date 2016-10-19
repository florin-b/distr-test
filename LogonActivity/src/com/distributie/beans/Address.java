package com.distributie.beans;

public class Address {

	private String country = "ROMANIA";
	private String region;
	private String city;
	private String streetName;
	private String streetNo;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetNo() {
		return streetNo;
	}

	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}

	@Override
	public String toString() {
		return "Address [country=" + country + ", region=" + region + ", city=" + city + ", streetName=" + streetName
				+ ", streetNo=" + streetNo + "]";
	}

}

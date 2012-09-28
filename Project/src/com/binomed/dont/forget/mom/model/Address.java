package com.binomed.dont.forget.mom.model;

public class Address {
	private String poBox;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private Integer type;
	private String formatted_address;
	private String nieghborhood;

	private String label;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormattedAddress(String formattedAddress) {
		formatted_address = formattedAddress;
	}

	public String getNieghborhood() {
		return nieghborhood;
	}

	public void setNeighborhood(String nieghborhood) {
		this.nieghborhood = nieghborhood;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}

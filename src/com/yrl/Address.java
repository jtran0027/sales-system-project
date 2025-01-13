package com.yrl;

/**
 * Creates a class that contains the information related to an Address.
 * 
 * @author jason
 *
 */

public class Address {

	private String street;
	private String city;
	private String state;
	private String zipCode;
	
	public Address(String street, String city, String state, String zipCode) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZipCode() {
		return zipCode;
	}

	@Override
	public String toString() {
		return street + "\n" + city + " " + state + " " + zipCode;
	}
	
	
	
}

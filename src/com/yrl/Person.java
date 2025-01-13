package com.yrl;

import java.util.List;

/**
 * Creates a persons class with the given information
 * 
 * @author jason
 *
 */

public class Person {
	
	private String uuid;
	private String firstName;
	private String lastName;
	private Address address;
	private List<String> emailAddresses;
	
	public Person(String uuid, String firstName, String lastName, Address address, List<String> emailAddresses) {
		super();
		this.uuid = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.emailAddresses = emailAddresses;
	}

	public String getUUID() {
		return uuid;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public List<String> getEmailAddress() {
		return emailAddresses;
	}
	
	public String getFullName() {
		return this.lastName + ", " + this.firstName;
	}
	
}

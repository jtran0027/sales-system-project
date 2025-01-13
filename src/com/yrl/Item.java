package com.yrl;

/**
 * Creates a sales items class with the given information
 * 
 * @author jason
 *
 */

public abstract class Item {

	private String code;
	private String name;
	
	public Item(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public abstract double getBaseCost();
	
	public abstract double getTotalWithTax();
	
	public abstract double getTotalWithoutTax();
	
	public abstract double getTaxAmount();
	
}

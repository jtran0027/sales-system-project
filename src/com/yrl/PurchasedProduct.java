package com.yrl;

/**
 * 
 * Creates a purchased product with the given information.
 * 
 * @author jason
 *
 */

public class PurchasedProduct extends Product{

	private double TAX_RATE = 0.065;
	
	public PurchasedProduct(String code, String name, double price) {
		super(code, name, price);
	}
	
	@Override
	public double getTotalWithTax() {
		return Math.round((getBaseCost() + getTaxAmount())*100)/100.0;
	}
	
	@Override
	public double getTotalWithoutTax() {
		return Math.round((getBaseCost())*100)/100.0;
	}

	@Override
	public double getTaxAmount() {
		return Math.round((getBaseCost() * TAX_RATE)*100)/100.0 ;
		
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s)", getName(), getCode());
	}
}

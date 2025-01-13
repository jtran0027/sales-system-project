package com.yrl;

/**
 * Generalized product but is further broken down by being the
 * parent class of Leased/Purchased Product.
 * 
 * @author jason
 *
 */

public class Product extends Item {

	private double price;

	public Product(String code, String name, double price) {
		super(code, name);
		this.price = price;
	}

	@Override
	public double getBaseCost() {
		return price;
	}

	@Override
	public double getTotalWithTax() {
		return 0.0;
	}
	
	@Override
	public double getTotalWithoutTax() {
		return 0.0;
	}
	
	@Override
	public double getTaxAmount() {
		return 0.0;
	}
	
	
	
}

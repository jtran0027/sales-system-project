package com.yrl;

/**
 * 
 * Creates a data plan class containing information with the amount of data purchased.
 * 
 * @author jason
 *
 */

public class DataPlan extends Item{
	
	private double TAX_RATE = 0.055;
	
	private double costPerGB;
	private double amountOfGB;

	public DataPlan(String code, String name, double costPerGB) {
		super(code, name);
		this.costPerGB = costPerGB;
	}

	public DataPlan(String code, String name, double costPerGB, double amountOfGB) {
		super(code, name);
		this.costPerGB = costPerGB;
		this.amountOfGB = amountOfGB;
	}
	
	@Override
	public double getBaseCost() {
		return costPerGB;
	}
	
	public double getAmountOfGB() {
		return amountOfGB;
	}

	@Override
	public double getTotalWithTax() {
		return Math.round(((getBaseCost() * getAmountOfGB()) + getTaxAmount())*100)/100.0;
	}
	
	@Override
	public double getTotalWithoutTax() {
		return Math.round(((getBaseCost() * getAmountOfGB()))*100)/100.0;
	}

	@Override
	public double getTaxAmount() {
		return Math.round((getBaseCost() * getAmountOfGB() * TAX_RATE)*100)/100.0;
	}

	@Override
	public String toString() {
		return String.format("%s (%s) - Data\n %.2f GB @ $%.2f/GB", getName(), getCode(), getAmountOfGB(), getBaseCost());
	}
	
	
}

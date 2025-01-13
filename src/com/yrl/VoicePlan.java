package com.yrl;

/**
 * 
 * Creates a voice plan with information containing the phone number and days purchased.
 *
 * @author jason
 *
 */

public class VoicePlan extends Item{
	
	private double TAX_RATE = 0.065;

	private double periodCost;
	private int daysPurchased;
	private String phoneNumber;

	public VoicePlan(String code, String name, double periodCost) {
		super(code, name);
		this.periodCost = periodCost;
	}
	
	public VoicePlan(String code, String name, double periodCost, String phoneNumber, int daysPurchased) {
		super(code, name);
		this.periodCost = periodCost;
		this.phoneNumber = phoneNumber;
		this.daysPurchased = daysPurchased;
	}

	@Override
	public double getBaseCost() {
		return periodCost;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}


	public int getDaysPurchased() {
		return daysPurchased;
	}

	@Override
	public double getTotalWithTax() {
		return Math.round(((periodCost * (daysPurchased/30.0) + getTaxAmount()))*100)/100.0;
	}
	
	@Override
	public double getTotalWithoutTax() {
		return Math.round(((periodCost * (daysPurchased/30.0))*100))/100.0;
	}

	@Override
	public double getTaxAmount() {
		return Math.round((periodCost * (daysPurchased/30.0) * TAX_RATE)*100)/100.0;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s (%s) - Voice %s\n %d days @ $%.2f / %d days", getName(), getCode(), getPhoneNumber(), getDaysPurchased(), getBaseCost(), 30 );
	}

	
}

package com.yrl;

/**
 * 
 * Creates a service class with information containing hours and the employee's id.
 * 
 * @author jason
 *
 */

public class Service extends Item{

	private double TAX_RATE = 0.035;
	
	private double costPerHour;
	private double hours;
	private Person employeeProvider;

	public Service(String code, String name, double costPerHour) {
		super(code, name);
		this.costPerHour = costPerHour;
	}
	
	public Service(String code, String name, double costPerHour, double hours, Person employeeProvider) {
		super(code, name);
		this.costPerHour = costPerHour;
		this.hours = hours;
		this.employeeProvider = employeeProvider;
	}

	@Override
	public double getBaseCost() {
		return costPerHour;
	}

	public double getHours() {
		return hours;
	}

	public Person getEmployeeProvider() {
		return employeeProvider;
	}

	@Override
	public double getTotalWithTax() {
		return Math.round(((getBaseCost() * getHours()) + getTaxAmount())*100)/100.0;
	}
	
	@Override
	public double getTotalWithoutTax() {
		return Math.round(((getBaseCost() * getHours()))*100)/100.0;
	}

	@Override
	public double getTaxAmount() {
		return Math.round((getBaseCost() * getHours() * TAX_RATE)*100)/100.0;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s) - %.2f hours @ $%.2f/hour", getName(), getCode(), getHours(), getBaseCost());
	}

	
}

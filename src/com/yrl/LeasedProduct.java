package com.yrl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 
 * Creates a leased product with information containing dates.
 * 
 * @author jason
 *
 */

public class LeasedProduct extends Product{
	
	private LocalDate startDate;
	private LocalDate endDate;

	public LeasedProduct(String code, String name, double price) {
		super(code, name, price);
	}

	public LeasedProduct(String code, String name, double price, LocalDate startDate, LocalDate endDate) {
		super(code, name, price);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	public long getMonthsBetween() {
		return ChronoUnit.MONTHS.between(startDate, endDate);
	}
	
	@Override
	public double getTotalWithTax() {
		return Math.round(( (getBaseCost())/getMonthsBetween() * 1.5 )*100)/100.0;
	}
	
	@Override
	public double getTotalWithoutTax() {
		return Math.round(( (getBaseCost())/getMonthsBetween() * 1.5 )*100)/100.0;
	}
	
	@Override
	public String toString() {
		return String.format("%s (%s) - Lease for %d months", getName(), getCode(), getMonthsBetween());
	}
	
}

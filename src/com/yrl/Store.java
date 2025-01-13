package com.yrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a stores class with given information
 * 
 * @author jason
 *
 */

public class Store {

	private String storeCode;
	private Person manager;
	private Address address;
	private List<Sale> storeSales;

	public Store(String storeCode, Person manager, Address address) {
		this.storeCode = storeCode;
		this.manager = manager;
		this.address = address;
		this.storeSales = new ArrayList<>();
	}

	public String getStoreCode() {
		return storeCode;
	}
	
	public Address getAddress() {
		return address;
	}
	
	public Person getManager() {
		return manager;
	}

	public List<Sale> getStoreSales() {
		return storeSales;
	}

	public void addSaleToStoreSales(Sale sale) {
		if (sale != null) {
			storeSales.add(sale);
		}
	}
	
	/**
	 * Iterates through the list of sales and adds up all of their totals.
	 * 
	 * @return total
	 */
	public double getGrandTotal() {
		double total = 0;
		for (Sale sale : this.storeSales) {
			if (sale != null ) {
				total += sale.getTotalCost();
			}
		}
		return total;
	}
}

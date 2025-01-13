package com.yrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Constructor that creates an object containing information based on sales data
 * 
 * @author jason
 * 
 */

public class Sale {
	
	private String saleCode;
	private Store store;
	private Person customer;
	private Person salesPerson;
	private LocalDate date;
	private List<Item> saleItems;
	
	//Comparators created based on the needed use.
	public static final Comparator<Sale> cmpCustomerName = new Comparator<>() {
		
		@Override
		public int compare(Sale s1, Sale s2) {
			if (s1.getCustomer().getLastName().compareTo(s2.getCustomer().getLastName()) == 0) {
				if (s1.getCustomer().getFirstName().compareTo(s2.getCustomer().getFirstName()) == 0) {
					return 0;
				} else {
					return s1.getCustomer().getFirstName().compareTo(s2.getCustomer().getFirstName());
				}
			} else {
				return s1.getCustomer().getLastName().compareTo(s2.getCustomer().getLastName());
			}
		}
		
	};
	
	//Compares store code first but if they are the same, then compares the names of the salesperson
	public static final Comparator<Sale> cmpStoreCode = new Comparator<>() {
		
		@Override
		public int compare(Sale s1, Sale s2) {
			if (s1.getStore().getStoreCode().compareTo(s2.getStore().getStoreCode()) == 0) {
				if (s1.getSalesPerson().getLastName().compareTo(s2.getSalesPerson().getLastName()) == 0) {
					if (s1.getSalesPerson().getFirstName().compareTo(s2.getSalesPerson().getFirstName()) == 0) {
						return 0;
					} else {
						return s1.getSalesPerson().getFirstName().compareTo(s2.getSalesPerson().getFirstName());
					}
				} else {
					return s1.getSalesPerson().getLastName().compareTo(s2.getSalesPerson().getLastName());
				}
			} else {
				return s1.getStore().getStoreCode().compareTo(s2.getStore().getStoreCode());
			}
		}
		
	};
	
	public static final Comparator<Sale> cmpTotalCost = Comparator.comparing(Sale::getTotalCost).reversed();
	
	public Sale(String saleCode, Store store, Person customer, Person salesPerson, LocalDate date) {
		super();
		this.saleCode = saleCode;
		this.store = store;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.date = date;
		this.saleItems = new ArrayList<>();
	}
	
	public String getSaleCode() {
		return saleCode;
	}

	public Store getStore() {
		return store;
	}

	public Person getCustomer() {
		return customer;
	}

	public Person getSalesPerson() {
		return salesPerson;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public List<Item> getSaleItem() {
		return saleItems;
	}
	
	public void addSaleItemsToSale(Item salItem) {
		if (salItem != null) {
			saleItems.add(salItem);
		}
	}
	
	//Iterates through the list of items and uses their toString method
	public String getSaleItemString() {
		for (Item item : getSaleItem()) {
			return item.toString();
		}
		return null;
	}
	
	/**
	 * Iterates through the list of items and gets their tax cost
	 * 
	 * @return tax
	 */
	
	public double getTax() {
		double tax = 0;
		
		for (Item ite : saleItems) {
			if (ite != null) {
				tax += ite.getTaxAmount();
			}
		}
		return tax;
		
	}
	
	/**
	 * Iterates through the list of items and gets their total with tax
	 * 
	 * @return cost
	 */
	public double getTotalCost() {
		double cost = 0;

		for (Item ite : saleItems) {
			if (ite != null) {
				cost += ite.getTotalWithTax();
			}		
		}
		return cost;

	}
	
	/**
	 * Iterates through the list of items and gets their total without tax
	 * 
	 * @return cost
	 */
	public double getTotalCostWithoutTax() {
		double cost = 0;
		
		for (Item ite : saleItems) {
			if (ite != null) {
				cost += ite.getTotalWithoutTax();
			}
		}
		return cost;
	}	
	
}

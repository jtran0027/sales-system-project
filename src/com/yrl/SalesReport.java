package com.yrl;

import java.util.List;

/**
 * Main class that prints out a formatted report
 * 
 * @author jason
 *
 */

public class SalesReport {

	public static void main(String args[]) {
		
		//Loads data from MySQL
		List<Sale> mySQLSales = DatabaseLoader.getListOfSales();
		List<Store> mySQLStores = DatabaseLoader.getListOfStores();
		DataLoader.addSaleItemsToStore(mySQLSales, mySQLStores);
		
		//Creating custom list
		SortedLinkedList<Sale> storeCodeLinkedList = new SortedLinkedList<>(Sale.cmpStoreCode);
		SortedLinkedList<Sale> customerNameLinkedList = new SortedLinkedList<>(Sale.cmpCustomerName);
		SortedLinkedList<Sale> totalCostLinkedList = new SortedLinkedList<>(Sale.cmpTotalCost);
		
		//Using custom-method to add to list
		storeCodeLinkedList.batchAdd(mySQLSales);
		customerNameLinkedList.batchAdd(mySQLSales);
		totalCostLinkedList.batchAdd(mySQLSales);
		
		//Printing out the reports
		ReportFormatter.reportLinkedListCustomerName(customerNameLinkedList);
		ReportFormatter.reportLinkedListTotalCost(totalCostLinkedList);
		ReportFormatter.reportLinkedListStoreCode(storeCodeLinkedList);
		
	}
		
	
}

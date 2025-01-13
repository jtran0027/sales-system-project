package com.yrl;

import java.util.List;

public class ReportFormatter {
	
	public static final String personCSV = "data/Persons.csv";
	public static final String itemCSV = "data/Items.csv";
	public static final String storeCSV = "data/Stores.csv";
	public static final String saleItemsCSV = "data/SaleItems.csv";
	public static final String salesCSV = "data/Sales.csv";
	
	
	/**
	 * Creates the first report that shows how many items a customer bought
	 * and how much they spent.
	 * 
	 * @param sales
	 */
	
	public static void reportTotalSummary(List<Sale> sales) {
		
		StringBuilder sb = new StringBuilder();
		
		double grandTotal = 0.0;
		double taxGrandTotal = 0.0;
		int totalSaleItem = 0;
		
		System.out.println("+----------------------------------------------------------------------------------------+");
		System.out.println("| Summary Report - By Total                                                              |");
		System.out.println("+----------------------------------------------------------------------------------------+");
		
		sb.append(String.format("%-15s %-10s %-25s %-15s %-11s %-10s\n", 
				"Invoice #", "Store", "Customer", "Num Items", "Tax", "Total"));
		
		for(Sale sal : sales) {
			
			sb.append(String.format("%-15s %-10s %-25s %-15d $%-10.2f $%-10.2f\n", 
			sal.getSaleCode(), sal.getStore().getStoreCode(), sal.getCustomer().getFullName(), sal.getSaleItem().size(), sal.getTax(), sal.getTotalCost()));
			
			taxGrandTotal += sal.getTax();
			grandTotal += sal.getTotalCost();
			totalSaleItem += sal.getSaleItem().size();

		}
		
		sb.append(String.format("+----------------------------------------------------------------------------------------+\n"));
		sb.append(String.format("%54d %15s %.2f %5s %.2f\n", totalSaleItem, "$", taxGrandTotal, "$", grandTotal));
		sb.append(String.format("\n"));
		
		System.out.println(sb);
	}
	
	/**
	 * Creates a report for the stores with information based on the manager
	 * and number of sales they made along with the cost.
	 * 
	 * @param stores
	 */
	
	public static void reportStoreSalesSummary(List<Store> stores) {
		
		StringBuilder sb = new StringBuilder();
		
		double grandTotal = 0.0;
		int totalSales = 0;
		
		System.out.println("+----------------------------------------------------------------+");
		System.out.println("| Store Sales Summary Report                                     |");
		System.out.println("+----------------------------------------------------------------+");
		
		sb.append(String.format("%-10s %-25s %-15s %-15s\n",
				"Store", "Manager", "# Sales", "Grand Total"));
		
		for(Store stor : stores) {

			sb.append(String.format("%-10s %-25s %-15d $%-15.2f\n",
					stor.getStoreCode(), stor.getManager().getFullName(), stor.getStoreSales().size(), stor.getGrandTotal() ));
			
			grandTotal += stor.getGrandTotal();
			totalSales += stor.getStoreSales().size();
			
		}
		
		sb.append(String.format("+----------------------------------------------------------------+\n"));
		sb.append(String.format("%38d %15s %.2f\n", totalSales, "$", grandTotal));
		
		System.out.println(sb);
		
	}
	
	/**
	 * Creates a complete receipt with total information on the sale with information
	 * like date, manager, customer, and prices.
	 * 
	 * @param sales
	 */
	
	public static void reportCompleteSummary(List<Sale> sales) {
		
		StringBuilder sb = new StringBuilder();
		
		for (Sale sal : sales) {
			sb.append(String.format("Sales   #%s\n", sal.getSaleCode()));
			sb.append(String.format("Store   #%s\n", sal.getStore().getStoreCode()));
			sb.append(String.format("Date    %s\n", sal.getDate()));
			sb.append(String.format("Customer:\n"));
			sb.append(String.format("%s (%s)\n", sal.getCustomer().getFullName(),sal.getCustomer().getUUID()));
			
			//Loop that prints the customer's email
			for (String str : sal.getCustomer().getEmailAddress()) {
				sb.append(String.format(str));
			}
			sb.append(String.format("\n%s\n", sal.getCustomer().getAddress().toString()));
			
			
			sb.append(String.format("\nSales Person:\n"));
			sb.append(String.format("%s (%s)\n", sal.getSalesPerson().getFullName(), sal.getSalesPerson().getUUID()));
			for (String str : sal.getSalesPerson().getEmailAddress()) {
				sb.append(String.format(str));
			}
			sb.append(String.format("\n%s\n", sal.getSalesPerson().getAddress().toString()));
			
			
			sb.append(String.format("\n"));
			sb.append(String.format("Items (%d) %62s %11s\n", sal.getSaleItem().size(), "Tax", "Total"));
			sb.append(String.format("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-\n"));
			
			double subtotal = 0.0;
			double taxSubtotal = 0.0;
			
			//Loop that prints out each Sale's item
			for(Item ite : sal.getSaleItem()) {
				sb.append(ite.toString());
				sb.append(String.format("%40.2f %10.2f\n", ite.getTaxAmount(), ite.getTotalWithoutTax()));
				
				subtotal += ite.getTotalWithoutTax();
				taxSubtotal += ite.getTaxAmount();
				sb.append(String.format("\n"));
			}
			
			
			sb.append(String.format("\n                                                             -=-=-=-=-=- -=-=-=-=-=-"));
			sb.append(String.format("\n %59s $%10.2f $%10.2f", "Subtotals:", taxSubtotal, subtotal));
			
			sb.append(String.format("\n %59s %13s %9.2f\n", "Grand Total:", "$", (taxSubtotal + subtotal)));
		}
		System.out.println(sb);
	}
	
	/**
	 * Formats a report that is ordered on the store code.
	 * Takes a linked list as a parameter
	 * 
	 * @param sales
	 */
	public static void reportLinkedListStoreCode(SortedLinkedList<Sale> sales) {
		
		StringBuilder sb = new StringBuilder();
		
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Store                                                          |");
		System.out.println("+-------------------------------------------------------------------------+");
		
		sb.append(String.format("%-10s %-15s %-25s %-25s %-25s\n",
				"Sale", "Store", "Customer", "Salesperson", "Total"));
		
		for (Sale sal : sales) {
			sb.append(String.format("%-10s %-15s %-25s %-25s $%-25.2f\n",
				sal.getSaleCode(), sal.getStore().getStoreCode(), sal.getCustomer().getFullName(), sal.getSalesPerson().getFullName(), sal.getTotalCost() ));
		}
		System.out.println(sb);
	}
	
	/**
	 * Formats a report that is ordered on the Last and First name of the customer.
	 * Takes a linked list as a parameter
	 * 
	 * @param sales
	 */
	public static void reportLinkedListCustomerName(SortedLinkedList<Sale> sales) {
		
		StringBuilder sb = new StringBuilder();
		
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Customer                                                       |");
		System.out.println("+-------------------------------------------------------------------------+");
		
		sb.append(String.format("%-10s %-15s %-25s %-25s %-25s\n",
				"Sale", "Store", "Customer", "Salesperson", "Total"));
		
		for (Sale sal : sales) {
			sb.append(String.format("%-10s %-15s %-25s %-25s $%-25.2f\n",
				sal.getSaleCode(), sal.getStore().getStoreCode(), sal.getCustomer().getFullName(), sal.getSalesPerson().getFullName(), sal.getTotalCost() ));
		}
		System.out.println(sb);
	}
	
	/**
	 * Formats a report that is ordered on the total cost.
	 * Takes a linked list as a parameter
	 * 
	 * @param sales
	 */
	public static void reportLinkedListTotalCost(SortedLinkedList<Sale> sales) {
		
		StringBuilder sb = new StringBuilder();
		
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Sales by Total                                                          |");
		System.out.println("+-------------------------------------------------------------------------+");
		
		sb.append(String.format("%-10s %-15s %-25s %-25s %-25s\n",
				"Sale", "Store", "Customer", "Salesperson", "Total"));
		
		for (Sale sal : sales) {
			sb.append(String.format("%-10s %-15s %-25s %-25s $%-25.2f\n",
				sal.getSaleCode(), sal.getStore().getStoreCode(), sal.getCustomer().getFullName(), sal.getSalesPerson().getFullName(), sal.getTotalCost() ));
		}
		System.out.println(sb);
	}

}

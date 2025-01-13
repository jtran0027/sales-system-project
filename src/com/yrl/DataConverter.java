package com.yrl;

import java.util.List;

/**
 * @Author: Jason Tran
 * Date: 02/22/24
 * 
 * Main class that calls methods to create, compile, and output json files.
 * 
 */
public class DataConverter {
	
	public static final String personCSV = "data/Persons.csv";
	public static final String itemCSV = "data/Items.csv";
	public static final String storeCSV = "data/Stores.csv";
	
	//Calls the methods to create the json files.
	public static void main(String args[]) {
		List<Person> listOfPersons = DataLoader.loadPersons(personCSV);
		List<Item> listOfItems = DataLoader.loadSaleItems(itemCSV);
		List<Store> listOfStores = DataLoader.loadStores(storeCSV, listOfPersons);
		
		FormatData.jsonFileFormatter(listOfPersons, "data/Persons.json");
		FormatData.jsonFileFormatter(listOfItems, "data/Items.json");
		FormatData.jsonFileFormatter(listOfStores, "data/Stores.json");
		
	}
	
}

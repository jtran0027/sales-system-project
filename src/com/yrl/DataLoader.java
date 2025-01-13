package com.yrl;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Loads data files and parses it into the correct classes.
 * 
 * @author jason
 *
 */

public class DataLoader {

	/**
	 * Persons information loading and parsing
	 * 
	 * @param personCSV
	 * @return
	 */
	public static List<Person> loadPersons(String personCSV) {

		List<Person> personsInfo = new ArrayList<Person>();

		String line = null;
		
		try (Scanner s = new Scanner(new File(personCSV))) {
			
			s.nextLine();
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				String uuid = tokens[0];
				String firstName = tokens[1];
				String lastName = tokens[2];
				String street = tokens[3];
				String city = tokens[4];
				String state = tokens[5];
				String zip = tokens[6];
				List<String> emailAddress = new ArrayList<>();
				
				//Appends multiple email addresses if there are multiple.
				if (tokens.length >= 8) {
					for(int i = 7; i < tokens.length; i++) {
						emailAddress.add(tokens[i]);
					}
				}
				
				
				Address address = new Address(street,city,state,zip);
				
				Person pe = new Person(uuid,firstName,lastName,address,emailAddress);
				personsInfo.add(pe);
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		
		return personsInfo;
	}
	
	/**
	 * Sale Items information loading and parsing
	 * 
	 * @param itemCSV
	 * @return
	 */
	public static List<Item> loadSaleItems(String itemCSV) {

		List<Item> itemInfo = new ArrayList<Item>();

		String line = null;
		
		try (Scanner s = new Scanner(new File(itemCSV))) {
			
			s.nextLine();
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				String code = tokens[0];
				String type = tokens[1];
				String name = tokens[2];
				double baseCost = Double.parseDouble(tokens[3]);
				
				if (type.equals("P")) {
					Product item = new Product(code,name,baseCost);
					itemInfo.add(item);
				} else if (type.equals("S")) {
					Service item = new Service(code,name,baseCost);
					itemInfo.add(item);
				} else if (type.equals("V")) {
					VoicePlan item = new VoicePlan(code,name,baseCost);
					itemInfo.add(item);
				} else if (type.equals("D")) {
					DataPlan item = new DataPlan(code,name,baseCost);
					itemInfo.add(item);
				}

			}
			
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		
		return itemInfo;
	}
	
	/**
	 * The stores information loading and parsing
	 * 
	 * @param storeCSV
	 * @param personCSV
	 * @return
	 */
	public static List<Store> loadStores(String storeCSV, List<Person> persons) {

		List<Store> storeInfo = new ArrayList<Store>();

		String line = null;
		
		try (Scanner s = new Scanner(new File(storeCSV))) {
			
			s.nextLine();
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				String storeCode = tokens[0];
				String managerUUID = tokens[1];
				String street = tokens[2];
				String city = tokens[3];
				String state = tokens[4];
				String zip = tokens[5];
				
				Person manager = null;
				
				//Finds the manager to match up with a person
				for (Person x : persons) {
					if (x.getUUID().equals(managerUUID)) {
						manager = x;
					}
				}
				
				
				Address address = new Address(street,city,state,zip);
				Store st = new Store(storeCode,manager,address);
				storeInfo.add(st);
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
		
		return storeInfo;
	}
	
	/**
	 * The sales information is read from a csv and then parsed accordingly.
	 * 
	 * @param salesCSV
	 * @param persons
	 * @param stores
	 * @return
	 */
	
	public static List<Sale> loadSales(String salesCSV, List<Person> persons, List<Store> stores) {

		List<Sale> salesInfo = new ArrayList<Sale>();
		
		String line = null;
		
		try (Scanner s = new Scanner(new File(salesCSV))) {
			
			s.nextLine();
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				if(tokens.length == 5) {
				String saleCode = tokens[0];
				String storeCode = tokens[1];
				String customerUUID = tokens[2];
				String salesPersonUUID = tokens[3];
				LocalDate date = LocalDate.parse(tokens[4]);

				Store realStore = null;
				Person realCustomer = null;
				Person realSalesPerson = null;
				
				//Loops to match up the correct store, customer, and manager.
				for (Store st : stores) {
					if (st.getStoreCode().equals(storeCode)) {
						realStore = st; 
					}
				}
				
				for (Person per : persons) {
					if (per.getUUID().equals(customerUUID)) {
						realCustomer = per;
					}
				}
				
				for (Person per : persons) {
					if (per.getUUID().equals(salesPersonUUID)) {
						realSalesPerson = per;
					}
				}
				
				Sale sal = new Sale(saleCode, realStore, realCustomer, realSalesPerson, date);
				
				salesInfo.add(sal);
			}
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}
				
		return salesInfo;
		
	}
	
	
	/**
	 * Creates a link between sale and sale items to be kept track of.
	 * 
	 * @param saleItemsCSV
	 * @param items
	 * @param sales
	 * @param persons
	 */
	
	public static void addSaleItemToSale(String saleItemsCSV, List<Item> items, List<Sale> sales, List<Person> persons) {

		String line = null;
		
		try (Scanner s = new Scanner(new File(saleItemsCSV))) {
			
			s.nextLine();
			
			while(s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				
				if(tokens.length >= 2) {
					
				String saleCode = tokens[0];
				String itemCode = tokens[1];

				Item tempItem = null;
				
				//Used as an identifier
				for(Item it : items) {
					if(itemCode.equals(it.getCode())) {
						tempItem = it;
					}
					
				}
				
				Item saleItem = null;
				
				//Checks for the correct subclass
				if(tempItem instanceof Product) {
					
					if(tokens.length == 2) {
						saleItem = new PurchasedProduct(tempItem.getCode(), tempItem.getName(), tempItem.getBaseCost());
					}
					
					else if(tokens.length == 4) {
						saleItem = new LeasedProduct(tempItem.getCode(), tempItem.getName(), tempItem.getBaseCost(), LocalDate.parse(tokens[2]), LocalDate.parse(tokens[3]));
						
					}
					
				} else if (tempItem instanceof VoicePlan) {
					if(tokens.length == 4) {
						saleItem = new VoicePlan(tempItem.getCode(), tempItem.getName(), tempItem.getBaseCost(), tokens[2], Integer.parseInt(tokens[3]));
					}
					
				} else if (tempItem instanceof DataPlan) {
					if(tokens.length == 3) {
						saleItem = new DataPlan(tempItem.getCode(), tempItem.getName(), tempItem.getBaseCost(), Double.parseDouble(tokens[2]));
					}
					
				} else if (tempItem instanceof Service) {
					
					Person employeeProvider = null;
					
					if(tokens.length == 4) {
						
						for(Person per: persons) {
							if(tokens[3].equals(per.getUUID())) {
								employeeProvider = per;
							}
						}
						
						saleItem = new Service(tempItem.getCode(), tempItem.getName(), tempItem.getBaseCost(), Double.parseDouble(tokens[2]), employeeProvider);
					}
				}
				
				for(Sale sal : sales) {
					if(sal.getSaleCode().equals(saleCode)) {
						sal.addSaleItemsToSale(saleItem);
					}
				}
				
			}
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Encountered Error on line " + line, e);
		}

	}
	
	/**
	 *Iterate through the two lists, figure out which sale had the same store code.
	 *Once figured out, then add the saleItemList to the store list inside of the class.
	 * 
	 * @param sales
	 * @param stores
	 */
	public static void addSaleItemsToStore(List<Sale> sales, List<Store> stores) {
		
		for(Sale sal : sales) {
			for(Store stor : stores) {
				if(sal.getStore().getStoreCode().equals(stor.getStoreCode())) {
					stor.addSaleToStoreSales(sal);
				}
			}
		}
		
	}
}

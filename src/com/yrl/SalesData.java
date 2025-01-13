package com.yrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class SalesData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String queryTables = "SELECT table_name FROM information_schema.tables WHERE table_schema = ? AND table_type = 'BASE TABLE'";
		String disableFKQuery = "SET foreign_key_checks = 0";
		String enableFKQuery = "SET foreign_key_checks = 1";
		
		
		PreparedStatement psTables = null;
		ResultSet rsTables = null;
		PreparedStatement psDelete = null;
		PreparedStatement psDisableFK = null;
		PreparedStatement psEnableFK = null;
		
		try {
			//Disable foreign key restraint
			psDisableFK = conn.prepareStatement(disableFKQuery);
	        psDisableFK.executeUpdate();
	        psDisableFK.close();
			
			//Grab the tables and then start clearing the data from them
			psTables = conn.prepareStatement(queryTables);
			psTables.setString(1, DatabaseInfo.USERNAME);
			
			rsTables = psTables.executeQuery();
			while(rsTables.next()) {
				String tableName = rsTables.getString("table_name");
				
				String queryDelete = "DELETE FROM " + tableName;
				psDelete = conn.prepareStatement(queryDelete);
				psDelete.executeUpdate();
			}
			
			//Re-enable the foreign key restraint
	        psEnableFK = conn.prepareStatement(enableFKQuery);
	        psEnableFK.executeUpdate();
	        psEnableFK.close();
			
			
			//Close everything
			psDelete.close();
			rsTables.close();
			psTables.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addPerson(String personUuid, String firstName, String lastName, String street, String city,
			String state, String zip) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		String insertPersonQuery = "INSERT INTO Person (uuid, lastName, firstName, addressId) values (?, ?, ? ,?)";
				
		PreparedStatement psPerson = null;
		
		try {
			psPerson = conn.prepareStatement(insertPersonQuery);
			psPerson.setString(1, personUuid);
			psPerson.setString(2, lastName);
			psPerson.setString(3, firstName);

			
			//Get the address if it exists, if not create an address for it
			psPerson.setInt(4, getAddressId(street, city, state, zip) );
			
			System.out.println("Added person into database.");
			
			psPerson.executeUpdate();
		
			//Close everything
			psPerson.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(String personUuid, String email) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String insertEmailQuery = "INSERT INTO Email (emailAddress, personId) values (?, ?)";
				
		PreparedStatement psEmail = null;
		
		try {
			psEmail = conn.prepareStatement(insertEmailQuery);
			psEmail.setString(1, email);
			
			//Get the person if they exist
			psEmail.setInt(2, getPersonId(personUuid) );
			
			psEmail.executeUpdate();
			
			//Close everything
			psEmail.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}


	/**
	 * Adds a store record to the database managed by the person identified by the
	 * given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip) {

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String storeQuery = "INSERT INTO Store (storeCode, managerId, addressId) values (?, ?, ?)";
		
		PreparedStatement psStore = null;
		
		try {
			psStore = conn.prepareStatement(storeQuery);
			psStore.setString(1, storeCode);
			
			//Get the manager if they exist
			psStore.setInt(2, getPersonId(managerCode) );

			//Get the address if it exists, if not create it
			psStore.setInt(3, getAddressId(street, city, state, zip) );
			
			psStore.executeUpdate();
			
			//Close everything
			psStore.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds an item record to the database of the given <code>type</code> with the
	 * given <code>code</code>, <code>name</code> and <code>basePrice</code>.
	 *
	 * Valid values for the <code>type</code> will be <code>"Product"</code>,
	 * <code>"Service"</code>, <code>"Data"</code>, or <code>"Voice"</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param type
	 * @param basePrice
	 */
	public static void addItem(String code, String name, String type, double basePrice) {

		//Check if the type is a valid type
		if (!type.equals("Product") && !type.equals("Service") && !type.equals("Data") && !type.equals("Voice")) {
			throw new IllegalArgumentException("Invalid item type: " + type);
		//If it is valid, make it fit into the database by turning it into a single letter
		} else {
			if (type.equals("Product")) {
				type = "P";
			} else if (type.equals("Service")) {
				type = "S";
			} else if (type.equals("Voice")) {
				type = "V";
			} else if (type.equals("Data")) {
				type = "D";
			}
		}
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
				
		String itemQuery = "INSERT INTO Item (itemCode, itemType, itemName, baseCost) values (?, ?, ?, ?)";
		
		PreparedStatement psItem = null;
		
		try {
			psItem = conn.prepareStatement(itemQuery);
			psItem.setString(1, code);
			psItem.setString(2, type);
			psItem.setString(3, name);
			psItem.setDouble(4, basePrice);
			psItem.executeUpdate();
			
			//Close everything
			psItem.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an Sale record to the database with the given data.
	 *
	 * @param saleCode
	 * @param storeCode
	 * @param customerPersonUuid
	 * @param salesPersonUuid
	 * @param saleDate
	 */
	public static void addSale(String saleCode, String storeCode, String customerPersonUuid, String salesPersonUuid,
			String saleDate) {

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String saleQuery = "INSERT INTO Sale (saleCode, storeId, customerId, salesPersonId, date) values (?, ?, ?, ?, ?)";
		
		PreparedStatement psSale = null;
		
		try {
			psSale = conn.prepareStatement(saleQuery);
			psSale.setString(1, saleCode);
			
			//Get the store if it exists
			String storeQuery = "SELECT storeId FROM Store WHERE storeCode = ?";
			
			PreparedStatement psStore = conn.prepareStatement(storeQuery);
			
			psStore.setString(1, storeCode);
			ResultSet rs = psStore.executeQuery();
			
			if (rs.next()) {
				int storeId = rs.getInt("storeId");
				psSale.setInt(2, storeId);
			} else {
				throw new RuntimeException("No such store with code: " + storeCode);
			}
			
			//Get the customer if they exist
			psSale.setInt(3, getPersonId(customerPersonUuid) );
			
			//Get the manager if they exist
			psSale.setInt(4, getPersonId(salesPersonUuid) );
			
			psSale.setString(5, saleDate);
		
			psSale.executeUpdate();
			
			//Close everything
			rs.close();
			psStore.close();
			psSale.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular product (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>).
	 *
	 * @param saleCode
	 * @param itemCode
	 */
	public static void addProductToSale(String saleCode, String itemCode) {

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String productQuery = "INSERT INTO SaleItem (itemId, saleId) values (?, ?)";
		
		PreparedStatement psProduct = null;
		
		try {
			psProduct = conn.prepareStatement(productQuery);
			
			//Get the itemId if it exists
			psProduct.setInt(1, getItemId(itemCode) );
			
			//Get the saleId if it exists
			psProduct.setInt(2, getSaleId(saleCode) );
			
			psProduct.executeUpdate();
			
			//Close everything
			psProduct.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a particular leased (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the start/end date
	 * specified.
	 *
	 * @param saleCode
	 * @param startDate
	 * @param endDate
	 */
	public static void addLeaseToSale(String saleCode, String itemCode, String startDate, String endDate) {

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String leaseQuery = "INSERT INTO SaleItem (itemId, saleId, startDate, endDate) values (?, ?, ?, ?)";
		
		PreparedStatement psLease = null;
		
		try {
			psLease = conn.prepareStatement(leaseQuery);
			
			//Get the itemId if it exists
			psLease.setInt(1, getItemId(itemCode) );
			
			//Get the saleId if it exists
			psLease.setInt(2, getSaleId(saleCode) );
			
			psLease.setString(3, startDate);
			psLease.setString(4, endDate);
			
			psLease.executeUpdate();
			
			//Close everything
			psLease.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular service (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * number of hours. The service is done by the employee with the specified
	 * <code>servicePersonUuid</code>
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param billedHours
	 * @param servicePersonUuid
	 */
	public static void addServiceToSale(String saleCode, String itemCode, double billedHours,
			String servicePersonUuid) {

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String serviceQuery = "INSERT INTO SaleItem (itemId, saleId, costPerHour, employee) values (?, ?, ?, ?)";
		
		PreparedStatement psService = null;
		
		try {
			psService = conn.prepareStatement(serviceQuery);
			
			//Get the itemId if it exists
			psService.setInt(1, getItemId(itemCode) );
			
			//Get the saleId if it exists
			psService.setInt(2, getSaleId(saleCode) );
			
			psService.setDouble(3, billedHours);
			psService.setString(4, servicePersonUuid); 
			
			psService.executeUpdate();
			
			//Close everything
			psService.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a particular data plan (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * number of gigabytes.
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param gbs
	 */
	public static void addDataPlanToSale(String saleCode, String itemCode, double gbs) {

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String dataPlanQuery = "INSERT INTO SaleItem (itemId, saleId, amountOfGB) values (?, ?, ?)";
		
		PreparedStatement psDataPlan = null;
		
		try {
			psDataPlan = conn.prepareStatement(dataPlanQuery);
			
			//Get the itemId if it exists
			psDataPlan.setInt(1, getItemId(itemCode) );
			
			//Get the saleId if it exists
			psDataPlan.setInt(2, getSaleId(saleCode) );
			
			psDataPlan.setDouble(3, gbs);
			
			psDataPlan.executeUpdate();
			
			//Close everything
			psDataPlan.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Adds a particular voice plan (identified by <code>itemCode</code>) to a
	 * particular sale (identified by <code>saleCode</code>) with the specified
	 * <code>phoneNumber</code> for the given number of <code>days</code>.
	 *
	 * @param saleCode
	 * @param itemCode
	 * @param phoneNumber
	 * @param days
	 */
	public static void addVoicePlanToSale(String saleCode, String itemCode, String phoneNumber, int days) {

		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String voicePlanQuery = "INSERT INTO SaleItem (itemId, saleId, phoneNumber, daysBought) values (?, ?, ?, ?)";
		
		PreparedStatement psVoicePlan = null;
		
		try {
			psVoicePlan = conn.prepareStatement(voicePlanQuery);
			
			//Get the itemId if it exists
			psVoicePlan.setInt(1, getItemId(itemCode) );
			
			//Get the saleId if it exists
			psVoicePlan.setInt(2, getSaleId(saleCode) );
			
			psVoicePlan.setString(3, phoneNumber);
			psVoicePlan.setInt(4, days);
			
			psVoicePlan.executeUpdate();
			
			//Close everything
			psVoicePlan.close();
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * Helper method that returns the key of an address. Inserts an address if it doesn't exist.
	 * 
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @return
	 */
	public static int getAddressId(String street, String city, String state, String zip) {
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		try {
			//Get the address if it exists, if not create an address for it
			String addressQuery = "SELECT addressId FROM Address WHERE street = ? AND city = ? AND state = ? AND zipcode = ?";
			
			PreparedStatement psAddress = conn.prepareStatement(addressQuery);
			
			psAddress.setString(1, street);
			psAddress.setString(2, city);
			psAddress.setString(3, state);
			psAddress.setString(4, zip);
			ResultSet rs = psAddress.executeQuery();
			
			if (rs.next()) {
				int addressId = rs.getInt("addressId");
				
				//Close everything
				rs.close();
				psAddress.close();
				conn.close();
				
				return addressId;
				
			} else {
				String insertAddress = "INSERT INTO Address (street, city, state, zipcode) values (?, ?, ?, ?)";
				
				PreparedStatement psInsertAddress = conn.prepareStatement(insertAddress, Statement.RETURN_GENERATED_KEYS);
				
				psInsertAddress.setString(1, street);
				psInsertAddress.setString(2, city);
				psInsertAddress.setString(3, state);
				psInsertAddress.setString(4, zip);
				psInsertAddress.executeUpdate();
				
				ResultSet addressKeys = psInsertAddress.getGeneratedKeys();
				addressKeys.next();
				int addressKey = addressKeys.getInt(1);
				
				//Close everything
				psInsertAddress.close();
				addressKeys.close();
				rs.close();
				psAddress.close();
				conn.close();
				
				return addressKey;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Helper method that gets the personId if it exists
	 * 
	 * @param personUuid
	 * @return
	 */
	public static int getPersonId(String personUuid) {
		
		Connection conn = null;
		int personId = 0;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String personQuery = "SELECT personId FROM Person WHERE uuid = ?";
		
		try {
			
			PreparedStatement psPerson = conn.prepareStatement(personQuery);
			
			psPerson.setString(1, personUuid);
			ResultSet rs = psPerson.executeQuery();
			
			if (rs.next()) {
				personId = rs.getInt("personId");
				
				rs.close();
				psPerson.close();
				conn.close();
				return personId;
			} else {
				rs.close();
				psPerson.close();
				conn.close();
				throw new IllegalArgumentException("No such person with UUID: " + personUuid);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Helper method that returns the itemId if it exists
	 * 
	 * @param itemCode
	 * @return
	 */
	public static int getItemId(String itemCode) {
		
		Connection conn = null;
		int itemId = 0;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String itemQuery = "SELECT itemId FROM Item WHERE itemCode = ?";

		try {
			
			PreparedStatement psItem = conn.prepareStatement(itemQuery);
			
			psItem.setString(1, itemCode);
			ResultSet rs = psItem.executeQuery();
			
			if (rs.next()) {
				itemId = rs.getInt("itemId");
				
				rs.close();
				psItem.close();
				conn.close();
				return itemId;
			} else {
				rs.close();
				psItem.close();
				conn.close();
				throw new IllegalArgumentException("No such item with code: " + itemCode);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Helper method that returns a saleId if it exists
	 * 
	 * @param saleCode
	 * @return
	 */
	public static int getSaleId(String saleCode) {
		
		Connection conn = null;
		int saleId = 0;
		
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String saleQuery = "SELECT saleId FROM Sale WHERE saleCode = ?";

		try {
			
			PreparedStatement psSale = conn.prepareStatement(saleQuery);
			
			psSale.setString(1, saleCode);
			ResultSet rs = psSale.executeQuery();
			
			if (rs.next()) {
				saleId = rs.getInt("saleId");
				
				rs.close();
				psSale.close();
				conn.close();
				return saleId;
			} else {
				rs.close();
				psSale.close();
				conn.close();
				throw new IllegalArgumentException("No such sale with code: " + saleCode);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	

}

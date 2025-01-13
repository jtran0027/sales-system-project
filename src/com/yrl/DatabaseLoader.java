package com.yrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Creates a connection between the MySQL Database using JDBC to
 * correctly create the objects needed.
 * 
 * @author jason
 *
 */

public class DatabaseLoader {
	
	/**
	 * Helper method that loads emails based on personId
	 * and returns a list of emails corresponding to personId
	 * 
	 * @param personId
	 * @return
	 */
	public static List<String> loadEmails(int personId) {
		
		Connection conn = null;
		List<String> emails = new ArrayList<>();
		
		//Opening a connection
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Executing the query to find the Email
		String query = "SELECT Email.emailAddress from Email JOIN Person ON Email.personId = Person.personId WHERE Person.personId = " + personId;
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				emails.add(rs.getString("emailAddress"));
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Makes sure rs, ps, and conn are closed
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return emails;
	}
	
	/**
	 * Helper method that loads an address object 
	 * corresponding to addressId
	 * 
	 * @param addressId
	 * @return
	 */
	public static Address loadAddress(int addressId) {
		
		Connection conn = null;
		Address addr = null;
		
		//Opening a connection
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Executing the query to find the address
		String query = """
				SELECT street, city, state, zipcode FROM Address 
				WHERE addressId = ?
				""";
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, addressId);
			rs = ps.executeQuery();
			if(rs.next()) {
				addr = new Address(rs.getString("street"), rs.getString("city"), rs.getString("state"), rs.getString("zipcode"));
			} else {
				throw new IllegalStateException("no such address with addressId = " + addressId);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Makes sure rs, ps, and conn are closed
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return addr;
	}
	
	/**
	 * Helper method that loads a person object based on personId
	 * 
	 * @param personId
	 * @return
	 */
	public static Person loadPerson(int personId) {
		
		Connection conn = null;
		Person p = null;
		
		//Opening a connection
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Executing the query to find the person	
		String personQuery = "SELECT uuid, lastName, firstName, addressId FROM Person WHERE personId = ?";
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(personQuery);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				p = new Person(rs.getString("uuid"), rs.getString("firstName"), rs.getString("lastName"), loadAddress(rs.getInt("addressId")), loadEmails(personId));
			} else {
				throw new IllegalStateException("no such person with personId = " + personId);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Makes sure rs, ps, and conn are closed
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return p;
	}
	
	/**
	 * Helper method that loads a store object
	 * based on a storeId
	 * 
	 * @param storeId
	 * @return
	 */
	public static Store loadStore(int storeId) {
		
		Connection conn = null;
		Store str = null;
		
		//Opening a connection
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Executing the query to find the store	
		String query = """
				SELECT storeCode, managerId, addressId FROM Store 
				WHERE storeId = ?
				""";
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, storeId);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				str = new Store(rs.getString("storeCode"), loadPerson(rs.getInt("managerId")), loadAddress(rs.getInt("addressId")));
			} else {
				throw new IllegalStateException("no such store with storeId = " + storeId);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Makes sure rs, ps, and conn are closed
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return str;
	}
	
	/**
	 * Helper method that loads a sale object and adds and item
	 * corresponding to the sale based on the saleCode
	 * 
	 * @param saleCode
	 * @return
	 */
	public static Sale loadSale(String saleCode) {
		
		Connection conn = null;
		Sale sal = null;
		
		//Opening a connection
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Executing the query to find the Sale	
		String query = "SELECT saleCode, storeId, customerId, salesPersonId, date FROM Sale WHERE saleCode = ?";
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, saleCode);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				//Converting to LocalDate
				LocalDate purchaseDate = rs.getDate("date").toLocalDate();
				sal = new Sale(rs.getString("saleCode"), loadStore(rs.getInt("storeId")), loadPerson(rs.getInt("customerId")), loadPerson(rs.getInt("salesPersonId")), purchaseDate);
				
				//Add the items related to the sale
				List<Item> listOfItems = loadItem(rs.getString("saleCode"));
				
				for (Item ite : listOfItems) {
					sal.addSaleItemsToSale(ite);
				}
				
			} else {
				throw new IllegalStateException("no such sale with saleCode = " + saleCode);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Makes sure rs, ps, and conn are closed
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return sal;
	}
	
	/**
	 * Helper method that loads a list of items depending on the
	 * saleCode. Also makes sure the items are of the right
	 * typing.
	 * 
	 * @param saleCode
	 * @return
	 */
	public static List<Item> loadItem(String saleCode) {
		
		Connection conn = null;
		
		List<Item> listOfItems = new ArrayList<>();
		
		//Opening a connection
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		//Executing the query to find the sale item	
		String query = """
				SELECT Item.itemCode, Item.itemType, Item.itemName, Item.baseCost, SaleItem.costPerGB, SaleItem.amountOfGB, SaleItem.periodCost, SaleItem.phoneNumber, SaleItem.daysBought, SaleItem.startDate, SaleItem.endDate, SaleItem.price, SaleItem.costPerHour, SaleItem.employee, Person.personId
				FROM SaleItem
				JOIN Item ON SaleItem.itemId = Item.itemId
				JOIN Sale ON SaleItem.saleId = Sale.saleId
				LEFT JOIN Person ON SaleItem.employee = Person.uuid
				WHERE Sale.saleCode = ?
				""";
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, saleCode);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("Item.itemType").equals("P")) {
					//Purchased Product
					if( (rs.getString("SaleItem.startDate") == null) && (rs.getString("SaleItem.endDate") == null) ) {
						PurchasedProduct item = new PurchasedProduct(rs.getString("Item.itemCode"), rs.getString("Item.itemName"), rs.getDouble("Item.baseCost"));
						listOfItems.add(item);
					//Leased Product
					} else if( (rs.getString("SaleItem.startDate") != null) && (rs.getString("SaleItem.endDate") != null) ) {
						LocalDate startDate = rs.getDate("startDate").toLocalDate();
						LocalDate endDate = rs.getDate("endDate").toLocalDate();
						LeasedProduct item = new LeasedProduct(rs.getString("Item.itemCode"), rs.getString("Item.itemName"), rs.getDouble("Item.baseCost"), startDate, endDate);
						listOfItems.add(item);
					}
				} else if (rs.getString("Item.itemType").equals("S")) {
					Service item = new Service(rs.getString("Item.itemCode"), rs.getString("Item.itemName"), rs.getDouble("Item.baseCost"), rs.getDouble("SaleItem.costPerHour"), loadPerson(rs.getInt("Person.personId")));
					listOfItems.add(item);
				} else if (rs.getString("Item.itemType").equals("V")) {
					VoicePlan item = new VoicePlan(rs.getString("Item.itemCode"), rs.getString("Item.itemName"), rs.getDouble("Item.baseCost"), rs.getString("SaleItem.phoneNumber"), rs.getInt("SaleItem.daysBought"));
					listOfItems.add(item);
				} else if (rs.getString("Item.itemType").equals("D")) {
					DataPlan item = new DataPlan(rs.getString("Item.itemCode"), rs.getString("Item.itemName"), rs.getDouble("Item.baseCost"), rs.getDouble("SaleItem.amountOfGB"));
					listOfItems.add(item);
				}
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Makes sure rs, ps, and conn are closed
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return listOfItems;
	}
	
	/**
	 * Method that gathers a complete list of sale objects
	 * 
	 * @return
	 */
	public static List<Sale> getListOfSales() {
		
		Connection conn = null;
		List<Sale> listOfSales = new ArrayList<>();
		
		//Opening a connection
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String query = """
				SELECT saleCode FROM Sale
				""";
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				//Want to check if the same saleCode is already in the list
				String saleCode = rs.getString("saleCode");
				boolean isSaleCodeFound = false;
				
				for (Sale sal : listOfSales) {
					if (sal.getSaleCode().equals(saleCode)) {
						isSaleCodeFound = true;
						break;
					}
				}
				
				//Add to the list if the code is not already in there
				if (isSaleCodeFound == false) {
					listOfSales.add(loadSale(rs.getString("saleCode")));
				}
			} 
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Makes sure rs, ps, and conn are closed
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return listOfSales;
	}
	
	/**
	 * Method that returns a list of store objects that contains
	 * their list of sales
	 * 
	 * @return
	 */
	public static List<Store> getListOfStores() {
		
		Connection conn = null;
		List<Store> listOfStores = new ArrayList<>();
		
		//Opening a connection
		try {
			conn = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String query = """
				SELECT Store.storeId, Store.storeCode, Sale.saleCode FROM Store
				LEFT JOIN Sale ON Sale.storeId = Store.storeId
				""";
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				//Want to check if the same storeCode is already in the list
	            String storeCode = rs.getString("storeCode");
	            boolean isStoreCodeFound = false;
	            for (Store store : listOfStores) {
	                if (store.getStoreCode().equals(storeCode)) {
	                    isStoreCodeFound = true;
	                    break;
	                }
	            }
	            //Add to list if the code is not already in there
	            if (isStoreCodeFound == false) {
	                Store store = loadStore(rs.getInt("storeId"));
	                listOfStores.add(store);
	            }
			}
	            
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		//Makes sure rs, ps, and conn are closed
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
			if (ps != null && !ps.isClosed())
				ps.close();
			if (conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return listOfStores;
	}

}

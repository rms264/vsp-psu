package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.Stock;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.VSPUtils;
import vsp.utils.Validate;
import vsp.utils.Enumeration.SecurityQuestion;

public class PortfolioEntries {

	/**
	 * @param userName
	 * @return This will return all portfolio entries from the PortfolioEntry  
	 * 		   table for the user that matches the user name passed in.
	 * @throws SQLException
	 */
	public List<PortfolioData> requestAllUserStocks(String userName) throws SQLException
	{
		PortfolioEntries request = new PortfolioEntries();
		List<PortfolioData> results = request.submitUserPortfolioRequest(userName);
		return results;
	}
	
	public static boolean addEntry(String userName, PortfolioData data, String orderId, String transactionId)  throws
		SqlRequestException, ValidationException, SQLException
	{	
		PortfolioEntries request = new PortfolioEntries();
		return request.insert(userName, data, orderId, transactionId);
	}
	
	public static boolean updateEntry(String id, String userName, PortfolioData data, String orderId, String transactionId) throws 
		SqlRequestException, SQLException
	{
		PortfolioEntries request = new PortfolioEntries();
		return request.update(id, userName, data, orderId, transactionId);
	}
	
	public static boolean deleteEntry(String id) throws 
		SqlRequestException, SQLException
	{
		PortfolioEntries request = new PortfolioEntries();
		return request.delete(id);
	}
	
	public static boolean deleteAllEntriesFor(String userName) throws 
		SqlRequestException, SQLException, ValidationException
	{
		PortfolioEntries request = new PortfolioEntries();
		return request.deleteAllFor(userName);
	}
	 
	public static PortfolioData getEntry(String id) throws 
		SqlRequestException, SQLException
	{
		PortfolioEntries request = new PortfolioEntries();
		return request.submitUserPortfolioEntryRequest(id);
	}
	
	private PortfolioEntries(){}
	
	private boolean insert(String userName, PortfolioData data, String orderId, String transactionId) throws
		SqlRequestException, ValidationException, SQLException
	{
		Connection connection = null;
		boolean success = false;
		try
		{
			String sqlStatement = "INSERT into PortfolioEntry values(?,?,?,?,?,?,?,?,?)";
			if(Validate.userNameExistsInDb(userName))
			{
				if (!Validate.stockExistsInDb(data.getStock().getStockSymbol()))
				{ // add stock to the database if it does not exist yet
					Stock stock = data.getStock();
					Stocks.addNewStock(stock.getStockSymbol(), stock.getStockDescription());
				}
				
				connection = DatasourceConnection.getConnection();
				
				String id = UUID.randomUUID().toString();
				PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
				pStmt.setString(1, id);   
				pStmt.setDouble(2, data.getPurchasePrice());
				pStmt.setFloat(3, data.getQuantity());
				pStmt.setDouble(4, data.getCostBasisPerShare());
				pStmt.setString(5, data.getStock().getStockSymbol());
				pStmt.setString(6, orderId);
				pStmt.setString(7, userName);
				pStmt.setString(8, transactionId);
			
				int result = pStmt.executeUpdate();
				if (result == 1)
				{
					success = true;
				}
			}
		}
		catch(NumberFormatException e)
		{
			throw new ValidationException(
					"Error:  Please select a security question.");
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		return success;
	}
	
	private boolean update(String id, String userName, PortfolioData data, String orderId, String transactionId) throws 
		SqlRequestException, SQLException
	{
		boolean success = false;
		Connection connection = null;
		try
		{
			String sqlStatement = 
					"UPDATE PortfolioEntry SET purchase_price=?, cost_basis_per_share=?, quantity=?, order_id=?, transaction_id=?, WHERE entry_id=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setDouble(1, data.getPurchasePrice());
			pStmt.setDouble(2, data.getCostBasisPerShare());
			pStmt.setFloat(3, data.getQuantity());
			pStmt.setString(4, orderId);
			pStmt.setString(5, transactionId);
			pStmt.setString(6, id);
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to update entry with id: " + id);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return success;
	}

	
	private boolean delete(String id) throws 
		SqlRequestException, SQLException
	{
		boolean success = false;
		Connection connection = null;		
		try
		{
			String sqlStatement = "DELETE from PortfolioEntry WHERE entry_id=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, id);  
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to delete entry " +
						"with id: " + id);
			}
			
			return success;
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
	
	private boolean deleteAllFor(String userName) throws 
		SqlRequestException, SQLException, ValidationException
	{
		boolean success = false;
		Connection connection = null;
		if(!Validate.userNameExistsInDb(userName))
		{
			throw new ValidationException(
				"Error: Cannot remove Portfolio Entries for User: " + userName +
				" not found in database");
		}
		
		try
		{
			String sqlStatement = "DELETE from PortfolioEntry WHERE user_name=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			int result = pStmt.executeUpdate(); 
			if(result >= 0)
			{ // possible for account to be deleted when user has no portfolio entries
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error:  Failed to delete entries " +
						"for user: " + userName);
			}
			
			return success;
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
	
	private PortfolioData submitUserPortfolioEntryRequest(String id) throws SQLException
	{
		PortfolioData results = null;
		Connection connection = null;
		try
		{
			String sqlStatement = 
					"SELECT * FROM PortfolioEntry WHERE entry_id=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, id);  
			ResultSet rs = pStmt.executeQuery();
			
			if(rs.first())
			{
				String stock = rs.getString("stock_symbol");
				Stock stockInfo = Stocks.getStock(stock);
				if(stockInfo != null)
				{
					results = new PortfolioData(stockInfo,
							rs.getDouble("purchase_price"),
							rs.getDouble("cost_basis_per_share"),
							rs.getFloat("quantity"));
				}
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return results;
	}
	
	private List<PortfolioData> submitUserPortfolioRequest(String userName) throws SQLException
	{
		List<PortfolioData> results = new ArrayList<PortfolioData>();
		Connection connection = null;
		try
		{
			String sqlStatement = 
					"SELECT * FROM PortfolioEntry WHERE user_name=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			ResultSet rs = pStmt.executeQuery();
			
			while(rs.next())
			{
				String stock = rs.getString("stock_symbol");
				Stock stockInfo = Stocks.getStock(stock);
				if(stockInfo != null)
				{
					results.add(new PortfolioData(stockInfo,
							rs.getDouble("purchase_price"),
							rs.getDouble("cost_basis_per_share"),
							rs.getFloat("quantity")));
				}
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return results;
	}
}

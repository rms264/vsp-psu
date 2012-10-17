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
	public static List<PortfolioData> requestAllUserStocks(String userName) throws SQLException
	{
		return submitUserPortfolioRequest(userName);
	}
	
	public static boolean addEntry(PortfolioData data)  throws
		SqlRequestException, ValidationException, SQLException
	{	
		return insert(data);
	}
	
	public static boolean updateEntry(PortfolioData data) throws 
		SqlRequestException, SQLException
	{
		return update(data);
	}
	
	public static boolean deleteEntry(String userName, String stockSymbol) throws 
		SqlRequestException, SQLException
	{
		return delete(userName, stockSymbol);
	}
	 
	public static PortfolioData getEntry(String userName, String stockSymbol) throws 
		SqlRequestException, SQLException
	{
		return submitUserPortfolioEntryRequest(userName, stockSymbol);
	}
	
	private PortfolioEntries(){}
	
	private static boolean insert(PortfolioData data) throws
		SqlRequestException, ValidationException, SQLException
	{
		Connection connection = null;
		boolean success = false;
		try
		{
			String sqlStatement = "INSERT into PortfolioEntry values(?,?,?,?,?)";
			if (!Validate.stockExistsInDb(data.getStock().getStockSymbol()))
			{ // add stock to the database if it does not exist yet
				Stock stock = data.getStock();
				Stocks.addNewStock(stock.getStockSymbol(), stock.getStockDescription());
			}
			
			connection = DatasourceConnection.getConnection();
			
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, data.getUserName());   
			pStmt.setString(2, data.getStock().getStockSymbol());
			pStmt.setFloat(3, data.getQuantity());
			pStmt.setDouble(4, data.getCostBasisPerShare());
		
			int result = pStmt.executeUpdate();
			if (result == 1)
			{
				success = true;
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
	
	private static boolean update(PortfolioData data) throws 
		SqlRequestException, SQLException
	{
		boolean success = false;
		Connection connection = null;
		try
		{
			String sqlStatement = 
					"UPDATE PortfolioEntry SET cost_basis_per_share=?, quantity=? WHERE user_name=? AND stock_symbol=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setDouble(1, data.getCostBasisPerShare());
			pStmt.setFloat(2, data.getQuantity());
			pStmt.setString(3, data.getUserName());
			pStmt.setString(4, data.getStock().getStockSymbol());
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to update entry for user name '" + data.getUserName() + "' and symbol '" + data.getStock().getStockSymbol() + "'");
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
	
	private static boolean delete(String userName, String stockSymbol) throws 
		SqlRequestException, SQLException
	{
		boolean success = false;
		Connection connection = null;		
		try
		{
			String sqlStatement = "DELETE from PortfolioEntry WHERE user_name=? AND stock_symbol=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);
			pStmt.setString(2, stockSymbol); 
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to update entry for user name '" + userName + "' and symbol '" + stockSymbol + "'");
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
		
	private static PortfolioData submitUserPortfolioEntryRequest(String userName, String stockSymbol) throws SQLException
	{
		PortfolioData data = null;
		Connection connection = null;
		try
		{
			String sqlStatement = 
					"SELECT * FROM PortfolioEntry WHERE user_name=? AND stock_symbol=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);
			pStmt.setString(2, stockSymbol);  
			ResultSet rs = pStmt.executeQuery();
			
			if(rs.first())
			{
				data = getPortfolioDataFromResultSet(rs);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return data;
	}
	
	private static List<PortfolioData> submitUserPortfolioRequest(String userName) throws SQLException
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
			
			PortfolioData data = null;
			while(rs.next())
			{
				data = getPortfolioDataFromResultSet(rs);
				results.add(data);
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
	
	private static PortfolioData getPortfolioDataFromResultSet(ResultSet rs) throws 
		SQLException
	{
		PortfolioData data = null;
		
		String stockName = rs.getString("stock_symbol");
		Stock stock = Stocks.getStock(stockName);
		
		double costBasisPerShare = rs.getDouble("cost_basis_per_share");
		float quantity = rs.getFloat("quantity");
		String userName = rs.getString("user_name");
		
		data = new PortfolioData(stock, costBasisPerShare, quantity, userName);
		
		return data;
	}
}

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
	
	public static boolean addEntry(PortfolioData data)  throws
		SqlRequestException, ValidationException, SQLException
	{	
		PortfolioEntries request = new PortfolioEntries();
		return request.insert(data);
	}
	
	public static boolean updateEntry(PortfolioData data) throws 
		SqlRequestException, SQLException
	{
		PortfolioEntries request = new PortfolioEntries();
		return request.update(data);
	}
	
	public static boolean deleteEntry(String userName, String stockSymbol) throws 
		SqlRequestException, SQLException
	{
		PortfolioEntries request = new PortfolioEntries();
		return request.delete(userName, stockSymbol);
	}
	 
	public static PortfolioData getEntry(String userName, String stockSymbol) throws 
		SqlRequestException, SQLException
	{
		PortfolioEntries request = new PortfolioEntries();
		return request.submitUserPortfolioEntryRequest(userName, stockSymbol);
	}
	
	private PortfolioEntries(){}
	
	private boolean insert(PortfolioData data) throws
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
	
	private boolean update(PortfolioData data) throws 
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
	
	private boolean delete(String userName, String stockSymbol) throws 
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
		
	private PortfolioData submitUserPortfolioEntryRequest(String userName, String stockSymbol) throws SQLException
	{
		PortfolioData results = null;
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
				Stock stockInfo = Stocks.getStock(stockSymbol);
				if(stockInfo != null)
				{
					results = new PortfolioData(stockInfo,
							rs.getDouble("cost_basis_per_share"),
							rs.getFloat("quantity"),
							rs.getString("user_name"));
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
							rs.getDouble("cost_basis_per_share"),
							rs.getFloat("quantity"),
							rs.getString("user_name")));
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

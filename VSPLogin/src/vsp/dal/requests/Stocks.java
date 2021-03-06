package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.Stock;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;

public class Stocks {
	
	/**
	 * @return This will return all stocks from the Stock table
	 * @throws SQLException
	 */
	public static List<Stock> getAllStocks() throws SQLException
	{
		return submitGetAllStocksRequest();
	}
	
	public static Stock getStock(String stockSymbol) throws SQLException
	{
		return submitStockRequest(stockSymbol);
	}
	
	public static boolean addNewStock(String stockSymbol, String description) 
			throws SqlRequestException, ValidationException, SQLException
	{
		return insert(stockSymbol, description);
	}
	
	public static boolean deleteStock(String stockSymbol) 
			throws SqlRequestException, SQLException, ValidationException
	{
		return delete(stockSymbol);
	}
	
	private Stocks(){}
	
	private static boolean insert(String stockSymbol, String description) throws
		SqlRequestException, ValidationException, SQLException
	{
		boolean success = false;
		Connection connection = null;
		if(Validate.stockExistsInDb(stockSymbol))
		{
			throw new ValidationException(
				"Error: Cannot insert Stock: " + stockSymbol +" Already Exists");
		}
		
		try
		{
			String sqlStatement = "INSERT INTO Stock VALUES(?,?)";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, stockSymbol);  
			pStmt.setString(2, description);
			int result = pStmt.executeUpdate();
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to insert " +
						stockSymbol);
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
	
	private static boolean delete(String symbol) throws SqlRequestException,
		SQLException, ValidationException
	{
		boolean success = false;
		Connection connection = null;
		if(!Validate.stockExistsInDb(symbol))
		{
			throw new ValidationException(
				"Error: Stock: " + symbol +	" not found in database");
		}
		
		try
		{
			String sqlStatement = "DELETE from Stock WHERE stock_symbol=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, symbol);  
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to delete Stock " 
						+ symbol);
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
	
	private static List<Stock> submitGetAllStocksRequest() throws SQLException
	{
		Connection connection = null;
		List<Stock> results = new ArrayList<Stock>();
		try
		{
			String sqlStatement = "SELECT * FROM Stock";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			ResultSet rs = pStmt.executeQuery();
			while(rs.next())
			{
				results.add(new Stock(rs.getString("stock_symbol"), 
						rs.getString("stock_description")));
			}
			return results;
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
	
	private static Stock submitStockRequest(String stockSymbol) throws SQLException
	{
		Connection connection = null;
		Stock stock = null;
		try
		{
			String sqlStatement = "SELECT * FROM Stock WHERE stock_symbol=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, stockSymbol);  
			ResultSet rs = pStmt.executeQuery();
			if(rs.first())
			{
				stock = getStockFromResultSet(rs);
			}
			
			return stock;
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
	
	private static Stock getStockFromResultSet(ResultSet rs) throws 
		SQLException
	{
		Stock stock = null;
		String symbol = rs.getString("stock_symbol");
		String description = rs.getString("stock_description");
		
		stock = new Stock(symbol, description);
		
		return stock;
	}
}

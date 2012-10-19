package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.*;
import vsp.exception.*;
import vsp.utils.VSPUtils;
import vsp.utils.Validate;
import vsp.utils.Enumeration.SecurityQuestion;
import vsp.utils.Enumeration.TransactionType;

public class Transactions
{
	public static boolean addTransaction(StockTransaction transaction) throws
		SqlRequestException, SQLException
	{
		return insert(transaction);
	}
	
	public static boolean deleteTransactionById(String transactionId)  throws
		SqlRequestException, SQLException
	{
		return delete(transactionId);
	}
	
	public static StockTransaction getTransactionById(String transactionId) throws
		SqlRequestException, SQLException
	{
		return submitTransactionQuery(transactionId);
	}
	
	public static List<StockTransaction> getTransactionsForUser(String userName) throws
		SqlRequestException, SQLException
	{
		return submitUserTransactionsQuery(userName);
	}
	
	public static List<StockTransaction> getDividendTransactionsForUserAndStock(String userName, String symbol, Date after)
			throws SqlRequestException, SQLException
	{
		return submitUserDividendsTransactionsQuery(userName, symbol, after);
	}
	
	public static List<StockTransaction> getExecutedTransactionsForUserAndStock(String userName, String symbol, Date after)
		throws SqlRequestException, SQLException
	{
		return submitExecutedUserTransactionsQuery(userName, symbol, after);
	}
	
	public static StockTransaction getTransactionForOrderId(String orderId) throws
		SqlRequestException, SQLException
	{
		return submitTransactionQueryForOrder(orderId);
	}
	
	private Transactions(){}
	
	private static boolean insert(StockTransaction transaction) throws
		SqlRequestException, SQLException
	{
		boolean success = false;
		Connection connection = null;
		try
		{
			String sqlStatement = StockTransaction.getInsertStatement();
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			transaction.prepareInsertStatement(pStmt);
		
			int result = pStmt.executeUpdate();
			if (result == 1)
			{
				success = true;
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
	
	private static boolean delete(String transactionId) throws 
		SqlRequestException, SQLException
	{
		boolean success = false;
		Connection connection = null;
		try
		{
			String sqlStatement = "DELETE * FROM Transaction WHERE transaction_id=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, transactionId);
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw (new SqlRequestException("Error:  Unable to delete transaction."));
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
	
	private static StockTransaction submitTransactionQuery(String transactionId) throws
		SQLException, SqlRequestException
	{
		Connection connection = null;
		StockTransaction transaction = null;
		try
		{
			String sqlStatement = "SELECT * FROM Transaction WHERE transaction_id=? ORDER BY date";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, transactionId);
			ResultSet rs = pStmt.executeQuery();
			
			if (rs.first())
			{
				transaction = getTransactionFromResultSet(rs);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return transaction;
	}
	
	private static StockTransaction submitTransactionQueryForOrder(String orderId) throws
		SQLException, SqlRequestException
	{
		Connection connection = null;
		StockTransaction transaction = null;
		try
		{
			String sqlStatement = "SELECT * FROM Transaction WHERE order_id=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, orderId);
			ResultSet rs = pStmt.executeQuery();
			
			if (rs.first())
			{
				transaction = getTransactionFromResultSet(rs);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return transaction;
	}
	
	private static List<StockTransaction> submitUserTransactionsQuery(String userName) throws
		SQLException, SqlRequestException
	{
		Connection connection = null;
		List<StockTransaction> transactions = new ArrayList<StockTransaction>();
		try
		{
			String sqlStatement = "SELECT * FROM Transaction WHERE user_name=? ORDER BY date DESC";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);
			ResultSet rs = pStmt.executeQuery();
			
			StockTransaction transaction = null;
			while (rs.next())
			{
				transaction = getTransactionFromResultSet(rs);
				transactions.add(transaction);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return transactions;
	}
	
	private static List<StockTransaction> submitUserDividendsTransactionsQuery(String userName, String symbol, Date after) throws
		SQLException, SqlRequestException
	{
		Connection connection = null;
		List<StockTransaction> transactions = new ArrayList<StockTransaction>();
		try
		{
			// do not include cancellations
			java.sql.Date afterDate = new java.sql.Date(after.getTime());
			String sqlStatement = "SELECT * FROM Transaction WHERE user_name=? AND stock_symbol=? AND date >= ? AND type=0 ORDER BY date DESC";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);
			pStmt.setString(2, symbol);
			pStmt.setDate(3, afterDate);
			ResultSet rs = pStmt.executeQuery();
			
			StockTransaction transaction = null;
			while (rs.next())
			{
				transaction = getTransactionFromResultSet(rs);
				transactions.add(transaction);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return transactions;
	}
	
	private static List<StockTransaction> submitExecutedUserTransactionsQuery(String userName, String symbol, Date after) throws
		SQLException, SqlRequestException
	{
		Connection connection = null;
		List<StockTransaction> transactions = new ArrayList<StockTransaction>();
		try
		{
			// do not include cancellations
			java.sql.Date afterDate = new java.sql.Date(after.getTime());
			String sqlStatement = "SELECT * FROM Transaction WHERE user_name=? AND stock_symbol=? AND date >= ? AND type=2 ORDER BY date DESC";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);
			pStmt.setString(2, symbol);
			pStmt.setDate(3, afterDate);
			ResultSet rs = pStmt.executeQuery();
			
			StockTransaction transaction = null;
			while (rs.next())
			{
				transaction = getTransactionFromResultSet(rs);
				transactions.add(transaction);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return transactions;
	}
	
	private static StockTransaction getTransactionFromResultSet(ResultSet rs) throws
		SQLException, SqlRequestException
	{
		StockTransaction transaction = null;
		
		String userName = rs.getString("user_name");
		String id = rs.getString("transaction_id");
		Stock stock = Stocks.getStock(rs.getString("stock_symbol"));
		Date date = rs.getDate("date");
		float quantity = rs.getFloat("quantity");
		double pricePerShare = rs.getDouble("price_per_share");
		double totalValue = rs.getDouble("total_value");
		String orderId = rs.getString("order_id");
		String note = rs.getString("note");
		
		Order order = null;
		if (orderId != null)
		{
			order = Orders.getOrderById(orderId);	
		}
		
		TransactionType type = TransactionType.convert(rs.getInt("type"));
		transaction = StockTransaction.CreateFromDb(type, userName, id, stock, date, totalValue, pricePerShare, quantity, order, note);
		if (transaction == null)
		{
			throw (new SqlRequestException("Error:  Unrecognized transaction type: " + type.toString()));
		}
		
		return transaction;
	}
}

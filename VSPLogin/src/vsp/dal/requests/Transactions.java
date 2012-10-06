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
import vsp.orders.Order;
import vsp.transactions.StockTransaction;
import vsp.transactions.StockTransactionFactory;
import vsp.utils.VSPUtils;
import vsp.utils.Validate;
import vsp.utils.Enumeration.SecurityQuestion;
import vsp.utils.Enumeration.TransactionType;

public class Transactions
{
	public static boolean addTransaction(StockTransaction transaction) throws
		SqlRequestException, SQLException
	{
		Transactions transactions = new Transactions();
		return transactions.insert(transaction);
	}
	
	public static boolean deleteTransactionById(String transactionId)  throws
		SqlRequestException, SQLException
	{
		Transactions transactions = new Transactions();
		return transactions.delete(transactionId);
	}
	
	public static StockTransaction getTransactionById(String transactionId) throws
		SqlRequestException, SQLException
	{
		Transactions transactions = new Transactions();
		return transactions.submitTransactionQuery(transactionId);
	}
	
	public static List<StockTransaction> getTransactionsForUser(String userName) throws
		SqlRequestException, SQLException
	{
		Transactions transactions = new Transactions();
		return transactions.submitUserTransactionsQuery(userName);
	}
	
	public static StockTransaction getTransactionForOrderId(String orderId) throws
		SqlRequestException, SQLException
	{
		Transactions transactions = new Transactions();
		return transactions.submitTransactionQueryForOrder(orderId);
	}
	
	private Transactions(){}
	
	private boolean insert(StockTransaction transaction) throws
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
	
	private boolean delete(String transactionId) throws 
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
	
	private StockTransaction submitTransactionQuery(String transactionId) throws
		SQLException, SqlRequestException
	{
		Connection connection = null;
		StockTransaction transaction = null;
		try
		{
			String sqlStatement = "SELECT * FROM Transaction WHERE transaction_id=?";
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
	
	private StockTransaction submitTransactionQueryForOrder(String orderId) throws
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
	
	private List<StockTransaction> submitUserTransactionsQuery(String userName) throws
		SQLException, SqlRequestException
	{
		Connection connection = null;
		List<StockTransaction> transactions = new ArrayList<StockTransaction>();
		try
		{
			String sqlStatement = "SELECT * FROM Transaction WHERE user_name=?";
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
	
	private StockTransaction getTransactionFromResultSet(ResultSet rs) throws
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
		
		Order order = null;
		if (orderId != null)
		{
			order = Orders.getOrderById(orderId);	
		}
		
		TransactionType type = TransactionType.convert(rs.getInt("type"));
		transaction = StockTransactionFactory.Create(type, userName, id, stock, date, totalValue, pricePerShare, quantity, order);
		if (transaction == null)
		{
			throw (new SqlRequestException("Error:  Unrecognized transaction type: " + type.toString()));
		}
		
		return transaction;
	}
}

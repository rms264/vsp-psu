package vsp.dal.requests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import vsp.dataObject.*;
import vsp.exception.*;
import vsp.orders.Order;
import vsp.transactions.StockTransaction;
import vsp.transactions.StockTransactionFactory;
import vsp.utils.Enumeration.TransactionType;

public class Transactions
{
	public static boolean addTransaction(String userName, StockTransaction transaction)
	{
		//TODO: Implement Adding a transaction to the database
		return true;
	}
	
	public static boolean deleteTransactionById(String id)
	{
		//TODO: Implement Deleting a transaction from the database
		return true;
	}
	
	public static StockTransaction getTransactionById(String id)
	{
		//TODO: Implement getting the specific transaction from the database
		return null;
	}
	
	public static StockTransaction getTransactionsForUser(String userName)
	{
		//TODO: Implement getting the user's transactions from the database
		return null;
	}
	
	public static StockTransaction getTransactionForOrderId(String orderId)
	{
		//TODO: Implement getting the specific transaction from the database
		return null;
	}
	
	private Transactions(){}
	
	private boolean insert() throws
		SqlRequestException, ValidationException, SQLException
	{
		return true;
	}
	
	private boolean delete() throws 
		SqlRequestException, SQLException, ValidationException
	{
		return true;
	}
	
	private StockTransaction getTransactionFromResultSet(ResultSet rs) throws
		SQLException, SqlRequestException
	{
		StockTransaction transaction = null;
		
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
		transaction = StockTransactionFactory.Create(type, id, stock, date, totalValue, pricePerShare, quantity, order);
		if (transaction == null)
		{
			throw (new SqlRequestException("Error:  Unrecognized transaction type: " + type.toString()));
		}
		
		return transaction;
	}
}

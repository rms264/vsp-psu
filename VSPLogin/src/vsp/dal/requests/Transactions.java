package vsp.dal.requests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import vsp.dataObject.*;
import vsp.exception.*;
import vsp.utils.Enumeration.TransactionType;

public class Transactions
{
	public static boolean addTransaction()
	{
		//TODO: Implement Adding a transaction to the database
		return true;
	}
	
	public static boolean deleteTransaction()
	{
		//TODO: Implement Deleting a transaction from the database
		return true;
	}
	
	public static boolean deleteAllTransactionsFor(String userName)
	{
		//TODO: Implement Deleting all transaction from the database or a user
		return true;
	}
	
	public static StockTransaction getTransaction(String id)
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
	
	private StockTransaction getTransaction(ResultSet rs) throws
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
			order = Orders.getOrder(orderId);	
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

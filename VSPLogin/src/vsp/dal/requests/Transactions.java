package vsp.dal.requests;

import java.sql.SQLException;

import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;

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
}

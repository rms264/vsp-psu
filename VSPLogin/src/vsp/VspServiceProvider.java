package vsp;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import vsp.dal.requests.Orders;
import vsp.dal.requests.Roles;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.orders.Order;
import vsp.utils.Enumeration.Role;


public class VspServiceProvider
{
	public VspServiceProvider()
	{
		// no implementation required
	}
	
	public void createTraderAccount(String userName, String password1, 
			String password2, String email, String question, String answer) 
			throws ValidationException, SQLException, SqlRequestException 
	{
		Users.addTraderAccount(userName, email, password1, password2, question, 
				answer);
		Roles.addNewUserRole(userName, Role.TRADER);
	}
	
	public void deleteTraderAccount(String userName) 
			throws ValidationException,
		SQLException, SqlRequestException
	{
		Users.deleteTraderAccount(userName);
	}
	
	public AccountData getAccountInfo(String userName) 
			throws SQLException
	{
		AccountData data = Users.requestAccountData(userName);	
		return data;
	}
	
	public List<Order> getPendingOrders(String userName) 
			throws SQLException, SqlRequestException
	{
		return Orders.getPendingOrdersForUser(userName);
	}
	
	public List<String> getTraders() 
			throws SQLException
	{
		return Users.queryAllTraders();
	}
	
	public List<StockTransaction> getTransactionHistory(String userName)
			throws SQLException, SqlRequestException
	{
		return Transactions.getTransactionsForUser(userName);
	}
}

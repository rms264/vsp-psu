package vsp.dal.requests;

import vsp.dataObject.Order;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;

import java.sql.SQLException;
import java.util.List;

public class Orders {

	public static boolean addOrder()
	{
		//TODO: Implement adding an order to the database
		return true;
	}
	
	public static boolean deleteOrder()
	{
		//TODO: Implement deleting an order from the database
		return true;
	}
	
	public static boolean deleteAllOrdersFor(String userName)
	{
		//TODO: Implement deleting all orders from the database for a user
		return true;
	}
	
	public static Order getOrder(String id)
	{
		//TODO: implement
		return null;
	}
	
	public static List<Order> getPendingOrders(String userName)
	{
		//TODO: implement
		return null;
	}
	
	private Orders(){}
	
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

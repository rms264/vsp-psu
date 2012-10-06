package vsp.dal.requests;

import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.orders.Order;

import java.sql.SQLException;
import java.util.List;

public class Orders {

	public static boolean addOrder(Order order)
	{
		//TODO: Implement adding an order to the database
		return true;
	}
	
	public static boolean deleteOrderById(String id)
	{
		//TODO: Implement deleting an order from the database
		return true;
	}
	
	public static Order getOrderById(String id)
	{
		//TODO: implement
		return null;
	}
	
	public static List<Order> getPendingOrdersForUser(String userName)
	{
		//TODO: implement
		return null;
	}
	
	public static List<Order> getOrdersForUser(String userName)
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

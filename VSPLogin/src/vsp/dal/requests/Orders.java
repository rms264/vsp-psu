package vsp.dal.requests;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.Order;
import vsp.dataObject.Stock;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;
import vsp.utils.Enumeration.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orders
{
	public static boolean addOrder(Order order) throws
		SqlRequestException, ValidationException, SQLException
	{
		return insert(order);
	}
	
	public static void changeOrderState(String userName, String orderId, OrderState oldState, OrderState newState)
		throws SQLException, SqlRequestException
	{
		updateState(userName, orderId, oldState, newState);
	}
	
	public static boolean deleteOrderById(String orderId) throws 
		SqlRequestException, SQLException
	{
		return delete(orderId);
	}
	
	public static Order getOrderById(String orderId) throws 
		SqlRequestException, SQLException
	{
		return submitOrderQuery(orderId);
	}
	
	public static List<Order> getPendingOrdersForUser(String userName) throws 
		SqlRequestException, SQLException
	{
		return submitUserPendingOrdersQuery(userName);
	}
	
	public static List<Order> getOrdersForUser(String userName) throws 
		SqlRequestException, SQLException
	{
		return submitUserOrdersQuery(userName);
	}
	
	public static void updateLastEvaulated(String orderId, Date lastEvaluated)
		throws SQLException, SqlRequestException
	{
		updateLastEvaluatedForOrder(orderId, lastEvaluated);
	}
	
	private Orders(){}
	
	private static void updateState(String userName, String orderId, OrderState oldState, OrderState newState)
		throws SQLException, SqlRequestException
	{
		Connection connection = null;
		try
		{
			connection = DatasourceConnection.getConnection();

			// update order state
			String sqlStatement = "UPDATE vsp.Order SET state=? WHERE user_name=? AND order_id=? AND state=?";
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setInt(1, oldState.getValue());  
			pStmt.setString(2, userName);   
			pStmt.setString(3, orderId);
			pStmt.setInt(4, newState.getValue());
			int result = pStmt.executeUpdate();
			if (result != 1 && newState == OrderState.CANCELLED)
			{
				throw (new SqlRequestException("Error:  Order already executed, is already cancelled or no longer exists."));				
			}
		}
		catch (SQLException ex)
		{
			throw ex;
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
	
	private static boolean insert(Order order) throws
		SqlRequestException, ValidationException, SQLException
	{
		Connection connection = null;
		boolean success = false;
		try
		{
			connection = DatasourceConnection.getConnection();
			if (!Validate.stockExistsInDb(order.getStock().getStockSymbol()))
			{ // add stock to the database if it does not exist yet
				Stock stock = order.getStock();
				Stocks.addNewStock(stock.getStockSymbol(), stock.getStockDescription());
			}

			String sqlStatement = "INSERT into vsp.Order values(?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, order.getId());  
			pStmt.setString(2, order.getUserName());   
			pStmt.setString(3, order.getStock().getStockSymbol());
			pStmt.setDate(4, new java.sql.Date(order.getDateSubmitted().getTime()));
			pStmt.setInt(5, order.getState().getValue());
			pStmt.setFloat(6, order.getQuantity());
			pStmt.setInt(7, order.getAction().getValue());
			pStmt.setInt(8, order.getType().getValue());
			pStmt.setDouble(9, order.getLimitPrice());
			pStmt.setDouble(10,  order.getStopPrice());
			pStmt.setInt(11, order.getTimeInForce().getValue());
			pStmt.setDate(12, new java.sql.Date(order.getLastEvaluated().getTime()));
		
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
	
	private static boolean delete(String orderId) throws 
		SqlRequestException, SQLException
	{
		boolean success = false;
		Connection connection = null;
		try
		{
			String sqlStatement = "DELETE * FROM vsp.Order WHERE order_id=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, orderId);
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
	
	private static void updateLastEvaluatedForOrder(String orderId, Date lastEvaluated) 
		throws SQLException, SqlRequestException
	{
		Connection connection = null;
		try
		{
			String sqlStatement = "UPDATE vsp.Order SET last_evaluated=? WHERE order_id=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setDate(1, new java.sql.Date(lastEvaluated.getTime()));
			pStmt.setString(2, orderId);
			int result = pStmt.executeUpdate();
			if(result != 1)
			{
				throw (new SqlRequestException("Error:  Unable to update last evaluated date."));
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
	
	private static Order submitOrderQuery(String orderId) throws 
		SqlRequestException, SQLException
	{
		Order order = null;
		Connection connection = null;
		try
		{
			String sqlStatement = "SELECT * FROM vsp.Order WHERE order_id=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, orderId);
			ResultSet rs = pStmt.executeQuery();

			if (rs.first())
			{
				order = getOrderFromResultSet(rs);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return order;
	}
	
	private static List<Order> submitUserOrdersQuery(String userName) throws 
		SqlRequestException, SQLException
	{
		Connection connection = null;
		List<Order> orders = new ArrayList<Order>();
		try
		{
			String sqlStatement = "SELECT * FROM vsp.Order WHERE user_name=? ORDER BY date_submitted";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);
			ResultSet rs = pStmt.executeQuery();
			
			Order order = null;
			while (rs.next())
			{
				order = getOrderFromResultSet(rs);
				orders.add(order);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return orders;
	}
	
	private static List<Order> submitUserPendingOrdersQuery(String userName) throws 
		SqlRequestException, SQLException
	{
		Connection connection = null;
		List<Order> orders = new ArrayList<Order>();
		try
		{
			String sqlStatement = "SELECT * FROM vsp.Order WHERE state=? AND user_name=? ORDER BY date_submitted";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setInt(1, OrderState.PENDING.getValue());
			pStmt.setString(2, userName);
			ResultSet rs = pStmt.executeQuery();
			
			Order order = null;
			while (rs.next())
			{
				order = getOrderFromResultSet(rs);
				orders.add(order);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return orders;
	}
	
	private static Order getOrderFromResultSet(ResultSet rs) throws
		SQLException, SqlRequestException
	{
		Order order = null;
		
		String id = rs.getString("order_id");
		String userName = rs.getString("user_name");
		Stock stock = Stocks.getStock(rs.getString("stock_symbol"));
		Date date = rs.getDate("date_submitted");
		Date lastEval = rs.getDate("date_submitted");
		OrderState state = OrderState.convert(rs.getInt("state"));
		float quantity = rs.getFloat("quantity");
		OrderAction action = OrderAction.convert(rs.getInt("action"));
		OrderType type = OrderType.convert(rs.getInt("type"));
		double limitPrice = rs.getDouble("limit_price");
		double stopPrice = rs.getDouble("stop_price");
		TimeInForce timeInForce = TimeInForce.convert(rs.getInt("time_in_force"));
			
		order = new Order(id, userName, stock, action, quantity, type, limitPrice, stopPrice, timeInForce, state, date, lastEval);
		
		return order;
	}
}

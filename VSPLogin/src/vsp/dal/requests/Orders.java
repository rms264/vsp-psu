package vsp.dal.requests;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.Stock;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.orders.Order;
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
		Orders orders = new Orders();
		return orders.insert(order);
	}
	
	public static void cancelOrder(String userName, String orderId)
		throws SQLException, SqlRequestException
	{
		Orders orders = new Orders();
		orders.cancel(userName, orderId);
	}
	
	public static boolean deleteOrderById(String orderId) throws 
		SqlRequestException, SQLException
	{
		Orders orders = new Orders();
		return orders.delete(orderId);
	}
	
	public static Order getOrderById(String orderId) throws 
		SqlRequestException, SQLException
	{
		Orders orders = new Orders();
		return orders.submitOrderQuery(orderId);
	}
	
	public static List<Order> getPendingOrdersForUser(String userName) throws 
		SqlRequestException, SQLException
	{
		Orders orders = new Orders();
		return orders.submitUserPendingOrdersQuery(userName);
	}
	
	public static List<Order> getOrdersForUser(String userName) throws 
		SqlRequestException, SQLException
	{
		Orders orders = new Orders();
		return orders.submitUserOrdersQuery(userName);
	}
	
	private Orders(){}
	
	private void cancel(String userName, String orderId)
		throws SQLException, SqlRequestException
	{
		Connection connection = null;
		try
		{
			connection = DatasourceConnection.getConnection();

			// update order state
			String sqlStatement = "UPDATE vsp.Order SET state=? WHERE user_name=? AND order_id=? AND state=?";
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setInt(1, OrderState.CANCELLED.getValue());  
			pStmt.setString(2, userName);   
			pStmt.setString(3, orderId);
			pStmt.setInt(4, OrderState.PENDING.getValue());
			int result = pStmt.executeUpdate();
			if (result != 1)
			{
				throw (new SqlRequestException("Error:  Order already executed, is already cancelled or no longer exists."));
			}
			
			Order order = Orders.getOrderById(orderId);
			if (order == null)
			{
				throw (new SqlRequestException("Error:  Cannot find order."));
			}
			
			Date cancelled = new Date();
			
			// add cancelled transaction
			StockTransaction transaction = StockTransaction.CreateNewCancellation(userName, order, cancelled);
			Transactions.addTransaction(transaction);
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
	
	private boolean insert(Order order) throws
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

			String sqlStatement = "INSERT into vsp.Order values(?,?,?,?,?,?,?,?,?,?,?)";
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
	
	private boolean delete(String orderId) throws 
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
	
	private Order submitOrderQuery(String orderId) throws 
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
	
	private List<Order> submitUserOrdersQuery(String userName) throws 
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
	
	private List<Order> submitUserPendingOrdersQuery(String userName) throws 
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
	
	private Order getOrderFromResultSet(ResultSet rs) throws
		SQLException, SqlRequestException
	{
		Order order = null;
		
		String id = rs.getString("order_id");
		String userName = rs.getString("user_name");
		Stock stock = Stocks.getStock(rs.getString("stock_symbol"));
		Date date = rs.getDate("date_submitted");
		OrderState state = OrderState.convert(rs.getInt("state"));
		float quantity = rs.getFloat("quantity");
		OrderAction action = OrderAction.convert(rs.getInt("action"));
		OrderType type = OrderType.convert(rs.getInt("type"));
		double limitPrice = rs.getDouble("limit_price");
		double stopPrice = rs.getDouble("stop_price");
		TimeInForce timeInForce = TimeInForce.convert(rs.getInt("time_in_force"));
			
		order = new Order(id, userName, stock, action, quantity, type, limitPrice, stopPrice, timeInForce, state, date);
		
		return order;
	}
}

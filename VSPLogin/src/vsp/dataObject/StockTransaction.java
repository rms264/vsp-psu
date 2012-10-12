package vsp.dataObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import vsp.orders.Order;
import vsp.utils.Enumeration.TransactionType;

public final class StockTransaction
{
	private final TransactionType type;
	private final String userName;
	private String id;
	private final Stock stock;
	private final Date dateTime;
	private final double value;
	private final double pricePerShare;
	private final float quantity;
	private final Order order;
	
	private StockTransaction(TransactionType type, String userName, String id, Stock stock, 
			Date dateTime, double value, double pricePerShare, float quantity, Order order)
	{
		this.type = type;
		this.userName = userName;
		this.id = id;
		this.stock = stock;
		this.dateTime = dateTime;
		this.value = value;
		this.pricePerShare = pricePerShare;
		this.quantity = quantity;
		this.order = order;
	}
	
	public static StockTransaction CreateFromDb(TransactionType type, String userName, String id, Stock stock, Date dateTime, double value, double pricePerShare, float quantity, Order order)
	{
		StockTransaction transaction = null;
		switch (type)
		{
			case DIVIDEND:
				transaction = CreateDividend(userName, id, stock, dateTime, value, pricePerShare, quantity);
				break;
			case CANCELLATION:
				transaction = CreateCancellation(userName, id, order, dateTime);
				break;
			case EXECUTION:
				transaction = CreateExecution(userName, id, order, dateTime, value, pricePerShare, quantity);
				break;
			case DEFAULT:
				break;
		}
		
		return transaction;
	}
	
	public static StockTransaction CreateCancellation(String userName, String id, Order order, Date dateTime)
	{
		return new StockTransaction(TransactionType.CANCELLATION, userName, id, order.getStock(), dateTime, 0.0, 0.0, 0.0f, order);
	}
	
	public static StockTransaction CreateNewCancellation(String userName, Order order, Date dateTime)
	{
		return new StockTransaction(TransactionType.CANCELLATION, userName, CreateId(), order.getStock(), dateTime, 0.0, 0.0, 0.0f, order);
	}
	
	public static StockTransaction CreateDividend(String userName, String id, Stock stock, Date dateTime, double value, double pricePerShare, float quantity)
	{
		return new StockTransaction(TransactionType.DIVIDEND, userName, id, stock, dateTime, value, pricePerShare, quantity, null);
	}
	
	public static StockTransaction CreateNewDividend(String userName, Stock stock, Date dateTime, double value, double pricePerShare, float quantity)
	{
		return new StockTransaction(TransactionType.DIVIDEND, userName, CreateId(), stock, dateTime, value, pricePerShare, quantity, null);
	}
	
	public static StockTransaction CreateExecution(String userName, String id, Order order, Date dateTime, double value, double pricePerShare, float quantity)
	{
		return new StockTransaction(TransactionType.EXECUTION, userName, id, order.getStock(), dateTime, value, pricePerShare, quantity, order);
	}
	
	public static StockTransaction CreateNewExecution(String userName, Order order, Date dateTime, double value, double pricePerShare, float quantity)
	{
		return new StockTransaction(TransactionType.EXECUTION, userName, CreateId(), order.getStock(), dateTime, value, pricePerShare, quantity, order);
	}
	
	public static String CreateId()
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public Date getDateTime()
	{
		return this.dateTime;
	}
	
	public String getId()
	{
		return this.id;
	}
		
	public static String getInsertStatement()
	{
		return "INSERT into Orders VALUES(?,?,?,?,?,?,?,?,?)";
	}
	
	public Order getOrder()
	{
		return this.order;
	}
	
	public double getPricePerShare()
	{
		return this.pricePerShare;
	}
	
	public float getQuantity()
	{
		return this.quantity;
	}
	
	public Stock getStock()
	{
		return this.stock;
	}
	
	public TransactionType getType()
	{
		return this.type;
	}
	
	public String getUserName()
	{
		return this.userName;
	}
	
	public double getValue()
	{
		return this.value;
	}
	
	public void prepareInsertStatement(PreparedStatement statement) 
				throws SQLException
	{
		statement.setString(1, this.getId());
		statement.setInt(2,  this.type.getValue());
		if (this.type == TransactionType.DIVIDEND)
		{ // no associated order
			statement.setString(3, null);
		}
		else
		{
			statement.setString(3, this.order.getId());
		}
		
		statement.setString(4, this.getStock().getStockSymbol());
		statement.setDate(5, new java.sql.Date(this.getDateTime().getTime()));
		statement.setFloat(6,  this.getQuantity());
		statement.setDouble(7, this.getPricePerShare());
		statement.setDouble(8, this.getValue());
		statement.setString(9, this.getUserName());
	}
}

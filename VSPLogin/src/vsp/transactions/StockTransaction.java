package vsp.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import vsp.dataObject.Stock;

public abstract class StockTransaction
{
	private final String userName;
	private final String id;
	private final Stock stock;
	private final Date dateTime;
	private final double value;
	private final double pricePerShare;
	private final float quantity;
	
	public StockTransaction(String userName, String id, Stock stock, Date dateTime, 
			double value, double pricePerShare, float quantity)
	{
		this.userName = userName;
		this.id = id;
		this.stock = stock;
		this.dateTime = dateTime;
		this.value = value;
		this.pricePerShare = pricePerShare;
		this.quantity = quantity;
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
	
	public abstract List<NameValuePair> getNameValuePairs();
		
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
	
	public String getUserName()
	{
		return this.userName;
	}
	
	public double getValue()
	{
		return this.value;
	}
	
	public abstract void prepareInsertStatement(PreparedStatement statement) throws SQLException;
}

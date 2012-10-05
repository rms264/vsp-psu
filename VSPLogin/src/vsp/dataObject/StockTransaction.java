package vsp.dataObject;

import java.util.*;

import vsp.NameValuePair;

public abstract class StockTransaction
{
	private final String id;
	private final String symbol;
	private final Date dateTime;
	private final double value;
	private final int quantity;
	
	public StockTransaction(String id, String symbol, Date dateTime, 
			double value, int quantity)
	{
		this.id = id;
		this.symbol = symbol;
		this.dateTime = dateTime;
		this.value = value;
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
	
	public abstract List<NameValuePair> getNameValuePairs();
	
	public int getQuantity()
	{
		return this.quantity;
	}
	
	public String getSymbol()
	{
		return this.symbol;
	}
	
	public double getValue()
	{
		return this.value;
	}
}

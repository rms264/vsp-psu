package vsp;

import java.util.*;

public abstract class StockTransaction
{
	protected String id;
	protected String symbol;
	protected Date dateTime;
	protected double value;
	protected int quantity;
	
	public StockTransaction(String id, String symbol, Date dateTime, double value, int quantity)
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

package vsp.dataObject;

import java.util.*;

import vsp.NameValuePair;

public abstract class StockTransaction
{
	private final String id;
	private final Stock stock;
	private final Date dateTime;
	private final double value;
	private final double pricePerShare;
	private final float quantity;
	
	public StockTransaction(String id, Stock stock, Date dateTime, 
			double value, double pricePerShare, float quantity)
	{
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
	
	public double getValue()
	{
		return this.value;
	}
}

package vsp.dataObject;


import java.util.Date;
import java.util.List;

import vsp.NameValuePair;

public final class OrderExecutionTransaction extends StockTransaction
{
	private final Order order;
	
	public OrderExecutionTransaction(String id, String symbol, Date dateTime, 
			double value, double pricePerShare, int quantity, Order order)
	{
		super(id, symbol, dateTime, value, pricePerShare, quantity);
		this.order = order;
	}
	
	@Override
	public List<NameValuePair> getNameValuePairs()
	{
		// TODO: implement
		
		return null;		
	}
}

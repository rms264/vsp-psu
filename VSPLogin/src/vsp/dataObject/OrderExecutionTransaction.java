package vsp.dataObject;


import java.util.Date;
import java.util.List;

import vsp.NameValuePair;

public final class OrderExecutionTransaction extends StockTransaction
{
	private final Order order;
	
	public OrderExecutionTransaction(String id, Stock stock, Date dateTime, 
			double value, double pricePerShare, float quantity, Order order)
	{
		super(id, stock, dateTime, value, pricePerShare, quantity);
		this.order = order;
	}
	
	@Override
	public List<NameValuePair> getNameValuePairs()
	{
		// TODO: implement
		
		return null;		
	}
}

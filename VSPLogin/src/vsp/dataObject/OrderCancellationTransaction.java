package vsp.dataObject;


import java.util.Date;
import java.util.List;

import vsp.NameValuePair;

public final class OrderCancellationTransaction extends StockTransaction
{
	private final Order order;
	
	public OrderCancellationTransaction(String id, String symbol, 
			Date dateTime, double value, int quantity, Order order)
	{
		super(id, symbol, dateTime, value, quantity);
		this.order = order;
	}
	
	@Override
	public List<NameValuePair> getNameValuePairs()
	{
		// TODO: implement
		
		return null;		
	}
}

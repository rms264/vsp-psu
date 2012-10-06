package vsp.dataObject;


import java.util.Date;
import java.util.List;

import vsp.NameValuePair;

public final class OrderCancellationTransaction extends StockTransaction
{
	private final Order order;
	
	public OrderCancellationTransaction(String id, Stock stock, Date dateTime, Order order)
	{
		super(id, stock, dateTime, 0.0, 0.0, 0.0f);
		this.order = order;
	}
	
	@Override
	public List<NameValuePair> getNameValuePairs()
	{
		// TODO: implement
		
		return null;		
	}
}

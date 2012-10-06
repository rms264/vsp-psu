package vsp.transactions;


import java.util.Date;
import java.util.List;

import vsp.NameValuePair;
import vsp.dataObject.Stock;
import vsp.orders.Order;

final class OrderCancellationTransaction extends StockTransaction
{
	private final Order order;
	
	OrderCancellationTransaction(String id, Stock stock, Date dateTime, Order order)
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

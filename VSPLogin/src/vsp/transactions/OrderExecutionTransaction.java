package vsp.transactions;


import java.util.Date;
import java.util.List;

import vsp.NameValuePair;
import vsp.dataObject.Stock;
import vsp.orders.Order;

final class OrderExecutionTransaction extends StockTransaction
{
	private final Order order;
	
	OrderExecutionTransaction(String id, Stock stock, Date dateTime, 
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

package vsp;

import java.util.*;

public final class OrderExecutionTransaction extends StockTransaction
{
	private Order order;
	
	public OrderExecutionTransaction(String id, String symbol, Date dateTime, double value, int quantity, Order order)
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

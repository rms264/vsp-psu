package vsp;

import java.util.*;

public final class DividendTransaction extends StockTransaction
{
	private double perShare;
	
	public DividendTransaction(String id, String symbol, Date dateTime, double value, int quantity, double perShare)
	{
		super(id, symbol, dateTime, value, quantity);
		this.perShare = perShare;
	}
	
	@Override
	public List<NameValuePair> getNameValuePairs()
	{
		// TODO: implement
		
		return null;		
	}
}

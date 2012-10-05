package vsp.dataObject;

import java.util.Date;
import java.util.List;

import vsp.NameValuePair;


public final class DividendTransaction extends StockTransaction
{
	private final double perShare;
	
	public DividendTransaction(String id, String symbol, Date dateTime, 
			double value, int quantity, double perShare)
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

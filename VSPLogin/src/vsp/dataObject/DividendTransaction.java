package vsp.dataObject;

import java.util.Date;
import java.util.List;

import vsp.NameValuePair;


public final class DividendTransaction extends StockTransaction
{
	public DividendTransaction(String id, String symbol, Date dateTime, 
			double value, double pricePerShare, int quantity, double perShare)
	{
		super(id, symbol, dateTime, value, pricePerShare, quantity);
	}
	
	@Override
	public List<NameValuePair> getNameValuePairs()
	{
		// TODO: implement
		
		return null;		
	}
}

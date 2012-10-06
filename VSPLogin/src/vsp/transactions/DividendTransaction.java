package vsp.transactions;

import java.util.Date;
import java.util.List;

import vsp.NameValuePair;
import vsp.dataObject.Stock;


public final class DividendTransaction extends StockTransaction
{
	public DividendTransaction(String id, Stock stock, Date dateTime, 
			double value, double pricePerShare, float quantity)
	{
		super(id, stock, dateTime, value, pricePerShare, quantity);
	}
	
	@Override
	public List<NameValuePair> getNameValuePairs()
	{
		// TODO: implement
		
		return null;		
	}
}

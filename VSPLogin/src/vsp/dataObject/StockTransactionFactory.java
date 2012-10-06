package vsp.dataObject;

import java.util.Date;
import vsp.utils.Enumeration.*;

public final class StockTransactionFactory
{
	private StockTransactionFactory() {}
	
	// primarily for use by the database request layer
	public static StockTransaction Create(TransactionType type, String id, Stock stock, 
			Date date, double totalValue, double pricePerShare, float quantity, Order order)
	{
		StockTransaction transaction = null;
		switch (type)
		{
			case DIVIDEND:
				transaction = new DividendTransaction(id, stock, date, totalValue, pricePerShare, quantity);
				break;
			case CANCELLATION:			
				transaction = new OrderCancellationTransaction(id, stock, date, order);
				break;
			case EXECUTION:
				transaction = new OrderExecutionTransaction(id, stock, date, totalValue, pricePerShare, quantity, order);
				break;
			case DEFAULT:
				default:
				// return null
				break;
		}
		
		return transaction;
	}
}

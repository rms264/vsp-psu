package vsp.dataObject;

import java.util.Date;

final class MarketOrderExecutor extends OrderExecutor
{
	MarketOrderExecutor()
	{
		// no implementation required
	}
	
	@Override
	public OrderResult Execute(Order order, IUserBalance balance, IStockInfo stockInfo)
	{
		// time in force is irrelevant
		
		Date lastEvaluated = order.getLastEvaluated();
		
		return null;
	}
}

package vsp.dataObject;

import vsp.utils.Enumeration.OrderType;

public final class OrderExecutorFactory
{
	private OrderExecutorFactory() {}
	
	public static OrderExecutor CreateFor(OrderType orderType)
	{
		OrderExecutor executor = null;
		if (orderType == OrderType.MARKET)
		{
			executor = CreateMarket();
		}
		else if (orderType == OrderType.LIMIT)
		{
			executor = CreateLimit();
		}
		else if (orderType == OrderType.STOP)
		{
			executor = CreateStop();
		}
		else if (orderType == OrderType.STOPLIMIT)
		{
			executor = CreateStopLimit();
		}
		
		return executor;
	}
	
	public static OrderExecutor CreateLimit()
	{
		return new LimitOrderExecutor();
	}
	
	public static OrderExecutor CreateMarket()
	{
		return new MarketOrderExecutor();
	}
	
	public static OrderExecutor CreateStop()
	{
		return new StopOrderExecutor(new MarketOrderExecutor());
	}
	
	public static OrderExecutor CreateStopLimit()
	{
		return new StopOrderExecutor(new LimitOrderExecutor());
	}
}

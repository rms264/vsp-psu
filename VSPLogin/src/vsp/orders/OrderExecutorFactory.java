package vsp.orders;

public final class OrderExecutorFactory
{
	private OrderExecutorFactory() {}
	
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
		return new StopOrderExecutor();
	}
	
	public static OrderExecutor CreateStopLimit()
	{
		return new StopOrderExecutor(new LimitOrderExecutor());
	}
}

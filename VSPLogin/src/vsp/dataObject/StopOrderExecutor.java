package vsp.dataObject;

final class StopOrderExecutor extends OrderExecutor
{
	private OrderExecutor decoratedExecutor;
	
	StopOrderExecutor()
	{
		// no implementation required
	}
	
	StopOrderExecutor(OrderExecutor decoratedExecutor)
	{
		this.decoratedExecutor = decoratedExecutor;
	}
	
	@Override
	public OrderResult Execute(Order order, IUserBalance balanceService, IStockInfo stockService)
	{
		// TODO: implement
		return null;
	}
}

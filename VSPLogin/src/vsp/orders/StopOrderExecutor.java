package vsp.orders;

public final class StopOrderExecutor extends OrderExecutor
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
	public OrderResult Execute(Order order)
	{
		// TODO: implement
		return null;
	}
}

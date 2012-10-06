package vsp.orders;

public abstract class OrderExecutor
{
	OrderExecutor()
	{
		// no implementation required
	}
	
	public abstract OrderResult Execute(Order order);
}

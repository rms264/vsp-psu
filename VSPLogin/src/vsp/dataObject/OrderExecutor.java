package vsp.dataObject;

public abstract class OrderExecutor
{
	OrderExecutor()
	{
		// no implementation required
	}
	
	public abstract OrderResult Execute(Order order, IUserBalance balance, IStockInfo stockInfo);
}

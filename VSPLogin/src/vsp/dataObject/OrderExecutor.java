package vsp.dataObject;

import java.util.Calendar;
import java.util.Date;

public abstract class OrderExecutor
{
	OrderExecutor()
	{
		// no implementation required
	}
	
	public abstract OrderResult Execute(Order order, IUserBalance balanceService, IStockInfo stockService);
	
	protected Date getDateOneDayInTheFuture(Date dateToMove)
	{
		Calendar currDtCal = Calendar.getInstance();
		currDtCal.setTime(dateToMove);
		currDtCal.add(Calendar.DAY_OF_YEAR, 1);
	  return currDtCal.getTime();
	}
}

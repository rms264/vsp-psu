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
	    currDtCal.set(Calendar.HOUR_OF_DAY, 0);
	    currDtCal.set(Calendar.MINUTE, 0);
	    currDtCal.set(Calendar.SECOND, 0);
	    currDtCal.set(Calendar.MILLISECOND, 0);
	    currDtCal.set(Calendar.MONTH, dateToMove.getMonth());
	    currDtCal.set(Calendar.DAY_OF_MONTH, dateToMove.getDate());
	    currDtCal.set(Calendar.YEAR, dateToMove.getYear() + 1900);
	    
	    currDtCal.add(Calendar.DAY_OF_YEAR, 1);
	    
	    return currDtCal.getTime();
	}
}

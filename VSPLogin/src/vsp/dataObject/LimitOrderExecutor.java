package vsp.dataObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.TimeInForce;

final class LimitOrderExecutor extends OrderExecutor
{
	private final Date today;
	
	LimitOrderExecutor()
	{
		Calendar currDtCal = Calendar.getInstance();

	    // Zero out the hour, minute, second, and millisecond
	    currDtCal.set(Calendar.HOUR_OF_DAY, 0);
	    currDtCal.set(Calendar.MINUTE, 0);
	    currDtCal.set(Calendar.SECOND, 0);
	    currDtCal.set(Calendar.MILLISECOND, 0);
	    
		this.today = currDtCal.getTime();
	}
	
	@Override
	public OrderResult Execute(Order order, IUserBalance balanceService, IStockInfo stockService)
	{
		OrderResult result = new OrderResult(order);
		Date submitted = order.getDateSubmitted();
		if (today.equals(submitted))
		{
			StockInfo info = stockService.requestCurrentStockData(order.getStock().getStockSymbol());
			if (info != null)
			{
				attemptTrade(result, balanceService, stockService, today, info.getDayLow(), info.getDayHigh(), info.getVolume());
			}
		}
		else // SELL
		{
			List<HistoricalStockInfo> infos = stockService.requestDailyHistoricalStockData(order.getStock().getStockSymbol(), order.getLastEvaluated());
			if (infos != null && infos.size() > 0)
			{
				HistoricalStockInfo info;
				for (int i = infos.size() - 1; i >= 0; --i)
				{
					info = infos.get(i);
					attemptTrade(result, balanceService, stockService, info.getDate(), info.getDayLow(), info.getDayHigh(), info.getVolume());
					if (result.getCompleted() || result.getCancelled())
					{
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	private void attemptTrade(OrderResult result, IUserBalance balanceService, IStockInfo stockService, Date date, double dayLow, double dayHigh, int volume)
	{
		Order order = result.getOrder();
		
		float quantity = order.getQuantity();
		if (quantity > volume)
		{
			return;
		}
				
		try
		{
			if (order.getAction() == OrderAction.BUY)
			{
				if (order.getLimitPrice() < dayLow)
				{ // price was too high to buy today
					return;
				}
				
				double orderTotal = quantity * dayLow;
				double accountBalance = balanceService.getBalance(order.getUserName());
				if (accountBalance <= orderTotal)
				{
					quantity = (int) (accountBalance / dayLow);
					orderTotal = quantity * dayLow;
				}
				
				if (quantity > 0 && accountBalance >= orderTotal)
				{
					try
					{
						balanceService.updateBalance(order.getUserName(), accountBalance - orderTotal);
						result.setCompleted(true);
						result.setQuantity(quantity);
						result.setSharePrice(dayLow);
						result.setDateTime(date);
					}
					catch (Exception ex)
					{
						// ignore
					}
				}
				else
				{ // account balance is zero or not enough to purchase even one share
					result.setCancelled(true);
					result.setDateTime(date);
				}
			}
			else
			{
				if (order.getLimitPrice() > dayHigh)
				{  // price was too low to sell today
					return;
				}
				
				double orderTotal = quantity * dayHigh;
				double accountBalance = balanceService.getBalance(order.getUserName());
				try
				{
					balanceService.updateBalance(order.getUserName(), accountBalance + orderTotal);
					result.setCompleted(true);
					result.setQuantity(quantity);
					result.setSharePrice(dayHigh);
					result.setDateTime(date);
				}
				catch (Exception ex)
				{
					// ignore
				}
			}
		}
		finally
		{
			// these apply on the first historical trade attempt (should be the same day the order was submitted)
			if (!result.getCompleted())
			{
				if (order.getTimeInForce() == TimeInForce.DAY 
						&& (today.after(date) || (today.equals(date) && !stockService.isWithinTradingHours())))
				{ // Cancel order
					result.setCancelled(true);
					result.setDateTime(date);
				}
				
				if ((order.getTimeInForce() == TimeInForce.IMMEDIATEORCANCEL || order.getTimeInForce() == TimeInForce.FILLORKILL))
				{ // Cancel order
					result.setCancelled(true);
					result.setDateTime(date);
				}
			}
			
			// GOOD UNTIL CANCELLED lives on for another day
		}
	}
}

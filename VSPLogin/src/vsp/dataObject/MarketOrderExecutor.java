package vsp.dataObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.TimeInForce;

final class MarketOrderExecutor extends OrderExecutor
{
	private final Date today;
	
	MarketOrderExecutor()
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
				attemptTrade(result, balanceService, stockService, today, info.getLastTradePrice(), info.getLastTradePrice(), info.getVolume());
			}
		}
		else
		{
			HistoricalStockInfo info = stockService.requestHistoricalStockDataForDay(order.getStock().getStockSymbol(), submitted);
			if (info != null)
			{
				attemptTrade(result, balanceService, stockService, info.getDate(), info.getDayLow(), info.getDayHigh(), info.getVolume());
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
			quantity = volume;
		}
		
		try
		{
			double accountBalance = balanceService.getBalance(order.getUserName());
			if (order.getAction() == OrderAction.BUY)
			{
				double orderTotal = quantity * dayLow;
				if (accountBalance <= orderTotal)
				{
					quantity = (int) (accountBalance / dayLow);
					orderTotal = quantity * dayLow;
					result.setNote("Reduced quantity from " + order.getQuantity() + " to " + quantity);
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
					result.setNote("Insufficient funds");
				}
			}
			else // SELL
			{
				double orderTotal = quantity * dayHigh;
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
			if (!result.getCompleted() && !result.getCancelled())
			{
				if (today.after(date) || (today.equals(date) && !stockService.isWithinTradingHours()))
				{ // Cancel order
					result.setCancelled(true);
					result.setDateTime(date);
					result.setNote("Market closed");
				}
			}
		}
	}
}

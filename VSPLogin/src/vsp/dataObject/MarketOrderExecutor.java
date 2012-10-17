package vsp.dataObject;

import java.util.Date;
import java.util.List;

import vsp.utils.Enumeration.OrderAction;

final class MarketOrderExecutor extends OrderExecutor
{
	MarketOrderExecutor()
	{
		// no implementation required
	}
	
	@Override
	public OrderResult Execute(Order order, IUserBalance balanceService, IStockInfo stockService)
	{
		OrderResult result = new OrderResult(order);
		Date today = new Date();
		if (stockService.isWithinTradingHours())
		{
			Date lastEvaluated = order.getLastEvaluated();
			if (lastEvaluated.getYear() == today.getYear() 
					&& lastEvaluated.getMonth() == today.getMonth()
					&& lastEvaluated.getDay() == today.getDay())
			{
				StockInfo info = stockService.requestCurrentStockData(order.getStock().getStockSymbol());
				if (info != null)
				{
					attemptTrade(result, balanceService, today, info.getDayLow(), info.getDayHigh(), info.getVolume());
				}
			}
			else
			{
				List<HistoricalStockInfo> infos = stockService.requestDailyHistoricalStockData(order.getStock().getStockSymbol(), lastEvaluated);
				if (infos != null && infos.size() > 0)
				{
					HistoricalStockInfo info = infos.get(infos.size() - 1);
					attemptTrade(result, balanceService, info.getDate(), info.getDayLow(), info.getDayHigh(), info.getVolume());
					if (!result.getCompleted())
					{ // market order is only good for the day it was placed
						result.setCancelled(true);
						result.setDateTime(info.getDate());
					}
				}
			}
		}
		else
		{ // market order is only good for the day it was placed
			result.setCancelled(true);
			result.setDateTime(today);
		}
		
		return result;
	}
	
	protected void attemptTrade(OrderResult result, IUserBalance balanceService, Date date, double dayLow, double dayHigh, int volume)
	{
		Order order = result.getOrder();
		
		float quantity = order.getQuantity();
		if (quantity > volume)
		{
			quantity = volume;
		}
		
		double accountBalance = balanceService.getBalance(order.getUserName());
		if (order.getAction() == OrderAction.BUY)
		{
			double orderTotal = quantity * dayLow;
			if (accountBalance >= orderTotal)
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
		}
		else
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
}

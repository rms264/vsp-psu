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
		Date submitted = order.getDateSubmitted();
		if (submitted.getYear() == today.getYear() 
				&& submitted.getMonth() == today.getMonth()
				&& submitted.getDay() == today.getDay())
		{
			StockInfo info = stockService.requestCurrentStockData(order.getStock().getStockSymbol());
			if (info != null)
			{
				attemptTrade(result, balanceService, today, info.getAsk(), info.getBid(), info.getDayLow(), info.getDayHigh(), info.getVolume());
				if (!result.getCompleted() && !stockService.isWithinTradingHours())
				{ // market order is only good for the day it was placed
					result.setCancelled(true);
					result.setDateTime(today);
				}
			}
		}
		else
		{
			HistoricalStockInfo info = stockService.requestHistoricalStockDataForDay(order.getStock().getStockSymbol(), submitted);
			if (info != null)
			{
				attemptTrade(result, balanceService, info.getDate(), 0.0, 0.0, info.getDayLow(), info.getDayHigh(), info.getVolume());
				if (!result.getCompleted())
				{ // market order is only good for the day it was placed
					result.setCancelled(true);
					result.setDateTime(info.getDate());
				}
			}
		}
		
		return result;
	}
	
	protected void attemptTrade(OrderResult result, IUserBalance balanceService, Date date, double ask, double bid, double dayLow, double dayHigh, int volume)
	{
		Order order = result.getOrder();
		
		float quantity = order.getQuantity();
		if (quantity > volume)
		{
			quantity = volume;
		}
		
		double sellPrice = (bid == 0.0) ? dayHigh : bid;
		double buyPrice = (ask == 0.0) ? dayLow : ask;
		
		double accountBalance = balanceService.getBalance(order.getUserName());
		if (order.getAction() == OrderAction.BUY)
		{
			double orderTotal = quantity * buyPrice;
			if (accountBalance >= orderTotal)
			{
				try
				{
					balanceService.updateBalance(order.getUserName(), accountBalance - orderTotal);
					result.setCompleted(true);
					result.setQuantity(quantity);
					result.setSharePrice(buyPrice);
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
			double orderTotal = quantity * sellPrice;
			try
			{
				balanceService.updateBalance(order.getUserName(), accountBalance + orderTotal);
				result.setCompleted(true);
				result.setQuantity(quantity);
				result.setSharePrice(sellPrice);
				result.setDateTime(date);
			}
			catch (Exception ex)
			{
				// ignore
			}
		}
	}
}

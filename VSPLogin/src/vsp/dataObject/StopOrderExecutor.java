package vsp.dataObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.TimeInForce;

final class StopOrderExecutor extends OrderExecutor
{
	private final Date today;
	private final OrderExecutor decoratedExecutor;
	
	StopOrderExecutor(OrderExecutor decoratedExecutor)
	{
		this.decoratedExecutor = decoratedExecutor;
		
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
		else
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
		
		if (order.getAction() == OrderAction.BUY)
		{
			if (!order.getStopPriceMet() && order.getStopPrice() > dayHigh)
			{ // price was too low to trigger the stop order conversion to an executable order
				return;
			}
			
			// stop price met --> try to execute
			order.setStopPriceMet(true);
			result = this.decoratedExecutor.Execute(order, balanceService, stockService);
		}
		else // SELL
		{
			if (!order.getStopPriceMet() && order.getStopPrice() < dayLow)
			{  // price was too high to trigger the stop order conversion to an executable order
				return;
			}
			
			// stop price met --> try to execute
			order.setStopPriceMet(true);
			result = this.decoratedExecutor.Execute(order, balanceService, stockService);
		}
	}
}

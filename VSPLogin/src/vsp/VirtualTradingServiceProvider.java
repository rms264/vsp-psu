package vsp;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import vsp.dal.requests.Orders;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.IUserBalance;
import vsp.dataObject.Order;
import vsp.dataObject.OrderExecutor;
import vsp.dataObject.OrderExecutorFactory;
import vsp.dataObject.OrderResult;
import vsp.dataObject.StockInfo;
import vsp.exception.SqlRequestException;

public final class VirtualTradingServiceProvider implements IUserBalance, IStockInfo
{
	private StockInfoServiceProvider sisp = new StockInfoServiceProvider();
	
	public VirtualTradingServiceProvider()
	{
		// no implementation required
	}
	
	public void processPendingOrders(String userName) 
		throws SQLException, SqlRequestException
	{
		List<Order> pendingOrders = Orders.getPendingOrdersForUser(userName);
		if (pendingOrders != null && pendingOrders.size() > 0)
		{
			OrderResult result = null;
			OrderExecutor executor = null;
			for (Order pendingOrder : pendingOrders)
			{
				result = null;
				executor = OrderExecutorFactory.CreateFor(pendingOrder.getType());
				try
				{
					result = executor.Execute(pendingOrder, this, this);
					
					// update last evaluated date
					Date date = new Date();
					pendingOrder.setLastEvaluated(date);
					Orders.updateLastEvaulated(pendingOrder.getId(), date);
					
					if (result.getCompleted())
					{
						// TODO: change order state to complete
						// TODO: add transaction for order
					}
				}
				catch (Exception ex)
				{
					// ignore
				}
			}
		}
	}
	
	public double getBalance(String userName)
	{
		double balance = 0.0;
		try
		{
			AccountData data = Users.requestAccountData(userName);
			if (data != null)
			{
				balance = data.getBalance();
			}
		}
		catch (Exception ex)
		{
			// ignore
		}
		
		return balance;
	}

	public void updateBalance(String userName, double balance)
		throws SQLException, SqlRequestException
	{
		Users.updateBalance(userName, balance);
	}
	
	public StockInfo getLatest(String symbol)
	{
		return sisp.requestCurrentStockData(symbol);
	}

	public List<HistoricalStockInfo> getHistoricalStockInfo(String symbol, Date since)
	{
		return sisp.requestDailyHistoricalStockData(symbol, since);
	}
}

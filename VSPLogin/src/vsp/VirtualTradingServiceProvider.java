package vsp;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import vsp.dal.requests.Orders;
import vsp.dal.requests.PortfolioEntries;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.IUserBalance;
import vsp.dataObject.Order;
import vsp.dataObject.OrderExecutor;
import vsp.dataObject.OrderExecutorFactory;
import vsp.dataObject.OrderResult;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.OrderState;

public final class VirtualTradingServiceProvider implements IUserBalance
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
			StockTransaction transaction = null;
			for (Order pendingOrder : pendingOrders)
			{
				result = null;
				transaction = null;
				
				executor = OrderExecutorFactory.CreateFor(pendingOrder.getType());
				try
				{
					result = executor.Execute(pendingOrder, this, sisp);
					
					// update last evaluated date
					Date date = new Date();
					pendingOrder.setLastEvaluated(date);
					Orders.updateLastEvaulated(pendingOrder.getId(), date);
					
					if (result.getCompleted())
					{
						Orders.changeOrderState(userName, pendingOrder.getId(), pendingOrder.getState(), OrderState.COMPLETE);
						transaction = StockTransaction.CreateNewExecution(pendingOrder.getUserName(), pendingOrder, 
								result.getDateTime(), result.getValue(), result.getSharePrice(), result.getQuantity());
						Transactions.addTransaction(transaction);
						
						PortfolioData data = PortfolioEntries.getEntry(userName, pendingOrder.getStock().getStockSymbol());
						if (data == null)
						{ // add (only applies when buying)
							data = new PortfolioData(pendingOrder.getStock(), result.getSharePrice(), result.getQuantity(), userName); 
							PortfolioEntries.addEntry(data);
						}
						else
						{ // update
							if (pendingOrder.getAction() == OrderAction.BUY)
							{ // cost basis changes based on new shares
								data.addQuantity(result.getQuantity(), result.getSharePrice());
							}
							else if (pendingOrder.getAction() == OrderAction.SELL)
							{ // cost basis does not change
								data.removeQuantity(result.getQuantity());
							}
							
							PortfolioEntries.updateEntry(data);
						}
					}
					else if (result.getCancelled())
					{
						Orders.changeOrderState(userName, pendingOrder.getId(), pendingOrder.getState(), OrderState.CANCELLED);
						transaction = StockTransaction.CreateNewCancellation(userName, pendingOrder, result.getDateTime());
						Transactions.addTransaction(transaction);
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
}

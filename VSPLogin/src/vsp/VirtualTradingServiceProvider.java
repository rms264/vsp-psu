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
import vsp.exception.ValidationException;
import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.OrderState;

public final class VirtualTradingServiceProvider implements IUserBalance
{
	private IStockInfo sisp = new StockInfoServiceProvider();
	private IUserBalance ub = this;
	
	public VirtualTradingServiceProvider()
	{
		// no implementation required
	}
	
	// for unit testing
	public void setStockInfo(IStockInfo stockInfo)
	{
		this.sisp = stockInfo;
	}
	
	// for unit testing
	public void setUserBalance(IUserBalance userBalance)
	{
		this.ub = userBalance;
	}
	
	public void processDividends(String userName)
	{
		// TODO: implement
	}
	
	public void processPendingOrders(String userName) 
		throws SQLException, SqlRequestException
	{
		List<Order> pendingOrders = Orders.getPendingOrdersForUser(userName);
		if (pendingOrders != null && pendingOrders.size() > 0)
		{
			Date date = new Date();
			OrderResult result = null;
			OrderExecutor executor = null;
			for (Order pendingOrder : pendingOrders)
			{
				result = null;			
				executor = OrderExecutorFactory.CreateFor(pendingOrder.getType());
				try
				{
					result = executor.Execute(pendingOrder, this.ub, this.sisp);
					this.processResult(pendingOrder, userName, date, result);
				}
				catch (Exception ex)
				{
					// ignore
				}
			}
		}
	}
	
	private void processResult(Order pendingOrder, String userName, Date date, OrderResult result) 
			throws SQLException, SqlRequestException, ValidationException
	{
		// update last evaluated date
		pendingOrder.setLastEvaluated(date);
		Orders.updateLastEvaulated(pendingOrder.getId(), date);
		
		StockTransaction transaction = null;
		if (result.getCompleted())
		{
			// record executed transaction
			Orders.changeOrderState(userName, pendingOrder.getId(), pendingOrder.getState(), OrderState.COMPLETE);
			transaction = StockTransaction.CreateNewExecution(pendingOrder.getUserName(), pendingOrder, result.getDateTime(), result.getValue(), result.getSharePrice(), result.getQuantity());
			Transactions.addTransaction(transaction);
		
			PortfolioData data = null;
			try
			{
				data = PortfolioEntries.getEntry(userName, pendingOrder.getStock().getStockSymbol());
			}
			catch (Exception ex)
			{
				// ignore
			}
			
			// update user's stock ownership status
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
		{ // record cancellation
			Orders.changeOrderState(userName, pendingOrder.getId(), pendingOrder.getState(), OrderState.CANCELLED);
			transaction = StockTransaction.CreateNewCancellation(userName, pendingOrder, result.getDateTime(), result.getNote());
			Transactions.addTransaction(transaction);
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

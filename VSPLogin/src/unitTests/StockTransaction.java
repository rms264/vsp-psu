package unitTests;

import java.util.Date;
import java.util.List;

import org.junit.*;
import org.junit.runner.*;
import vsp.*;
import vsp.dataObject.Order;
import vsp.dataObject.StockInfo;
import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.OrderType;
import vsp.utils.Enumeration.TimeInForce;

@RunWith(OrderedRunner.class)
public class StockTransaction
{
	private final VspServiceProvider vsp = new VspServiceProvider();
	private final StockInfoServiceProvider stockInfoSP = new StockInfoServiceProvider(); 
	
	@Test	
	public void displayStockInfoWithProvider()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{			
			String stockSymbol = "Temp";
			String provider = "Yahoo";
			vsp.displayStockInfo(stockSymbol, provider);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test	
	public void displayStockInfo()
	{
		try
		{
			String stockSymbol = "AAPL";
			StockInfo stockInfo = stockInfoSP.requestCurrentStockData(stockSymbol);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void displayHistoricalStockInfoForMonths()
	{
		try
		{
			String stockSymbol = "AAPL";			
			// Get data for last 3 months
			stockInfoSP.requestHistoricalStockData(stockSymbol, 3);					
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void displayHistoricalStockInfoForDay()
	{
		try
		{
			String stockSymbol = "AAPL";			
			Date day = new Date();
			day.setDate(1);
			// Get data for particular date, default is Today
			stockInfoSP.requestHistoricalStockDataForDay(stockSymbol, day);			
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void searchStock()
	{
		try
		{
			String search = "AAPLE";
			stockInfoSP.searchForStocks(search);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void showUserTransaction()
	{		
		try
		{
			String userName = "test";
			vsp.getTransactionHistory(userName);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void displayUserPortfolioWithProvider()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int userId = 999;
			String provider = "yahoo";
			vsp.getUserPortfolio(userId, provider);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test	
	public void displayUserPortfolio()
	{
		try
		{
			String userName = "test";
			vsp.getPortfolioEntries(userName);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void showUserPendingOrder()
	{
		try
		{
			String userName = "test";
			List<Order> orders = vsp.getPendingOrders(userName);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void createOrder()
	{
		try
		{					
			String userName = "test";
			List<Order> orders = vsp.getPendingOrders(userName);
			vsp.createOrder(userName, "BUY", "AAPL", "10", 
						"MARKET", "DEFAULT", "0.0","0.0" );			
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void cancelPendingOrder()
	{
		try
		{					
			String userName = "test";
			List<Order> orders = vsp.getPendingOrders(userName);
			Order order = orders.get(0);
			vsp.cancelOrder(userName, order.getId());
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test
	public void executePendingOrders()
	{
		try
		{
			String userName = "test";
			vsp.processPendingOrders(userName);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}
	}
	
	@Test	
	public void buyStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;
			int orderType = 1;
			int time = 10;
			vsp.buyStock(stockSymbol, quantity, orderType, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void buyMarketOrderStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;			
			int time = 10;
			vsp.buyMarketOrderStock(stockSymbol, quantity, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void buyLimitOrderStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;			
			int time = 10;
			vsp.buyLimitOrderStock(stockSymbol, quantity, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void buyStopOrderStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;			
			int time = 10;
			vsp.buyStopOrderStock(stockSymbol, quantity, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void buyStopLimitOrderStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;			
			int time = 10;
			vsp.buyStopLimitOrderStock(stockSymbol, quantity, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void sellStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;
			int orderType = 1;
			int time = 10;
			vsp.sellStock(stockSymbol, quantity, orderType, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void sellMarketOrderStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;			
			int time = 10;
			vsp.sellMarketOrderStock(stockSymbol, quantity, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void sellLimitOrderStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;			
			int time = 10;
			vsp.sellLimitOrderStock(stockSymbol, quantity, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void sellStopOrderStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;			
			int time = 10;
			vsp.sellStopOrderStock(stockSymbol, quantity, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void sellStopLimitOrderStock()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "test";
			int quantity = 1;			
			int time = 10;
			vsp.sellStopLimitOrderStock(stockSymbol, quantity, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void dayOrderExecute()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int time = 10;
			vsp.dayOrderExecute(time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void setGoodUntilCancel()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int orderId = 1;
			int time = 10;
			vsp.setGoodUntilCancel(orderId, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void setGoodUntilCancelValue()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int orderId = 1;
			int time = 10;
			vsp.setGoodUntilCancelValue(orderId, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void setFillOrKillValue()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int orderId = 1;
			int time = 10;
			vsp.setFillOrKillValue(orderId, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	public void setImmediateOrCancelValue()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int orderId = 1;
			int time = 10;
			vsp.setImmediateOrCancelValue(orderId, time);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
}
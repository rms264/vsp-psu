package unitTests;

import org.junit.*;
import org.junit.runner.*;
import vsp.*;

@RunWith(OrderedRunner.class)
public class StockTransaction
{
	private final VspServiceProvider vsp = new VspServiceProvider();
	
	@Test
	@Order(order=1)
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
	@Order(order=2)
	public void displayStockInfo()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			String stockSymbol = "Test";
			vsp.displayStockInfo(stockSymbol);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	@Order(order=3)
	public void showUserTransaction()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int userId = 999;
			vsp.getUserTransaction(userId);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	@Order(order=4)
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
	@Order(order=5)
	public void displayUserPortfolio()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int userId = 999;
			vsp.getUserPortfolio(userId);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	@Order(order=6)
	public void showUserPendingOrder()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int userId = 999;
			vsp.getPendingOrder(userId);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	@Order(order=7)
	public void cancelPendingOrder()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int orderId = 999;
			vsp.cancelPendingOrder(orderId);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	@Order(order=8)
	public void executeOrder()
	{
		Assert.fail("Not yet implemented");
		/*
		try
		{
			int orderId = 999;
			vsp.executePendingOrder(orderId);
		}
		catch (Exception e)
		{
			Assert.fail("Not yet implemented");
		}*/
	}
	
	@Test
	@Order(order=9)
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
	@Order(order=10)
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
	@Order(order=11)
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
	@Order(order=12)
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
	@Order(order=13)
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
	@Order(order=14)
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
	@Order(order=15)
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
	@Order(order=16)
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
	@Order(order=17)
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
	@Order(order=18)
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
	@Order(order=19)
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
	@Order(order=20)
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
	@Order(order=21)
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
	@Order(order=22)
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
	@Order(order=23)
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
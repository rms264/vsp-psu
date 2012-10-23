package unitTests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.*;
import org.junit.runner.*;
import vsp.*;
import vsp.dal.requests.Orders;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.Order;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.OrderState;
import vsp.utils.Enumeration.OrderType;
import vsp.utils.Enumeration.TimeInForce;

@RunWith(OrderedRunner.class)
public class StockTransactions
{
	private final SimpleDateFormat historicalDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	private final VspServiceProvider vsp = new VspServiceProvider();
	private final StockInfoServiceProvider stockInfoSP = new StockInfoServiceProvider(); 
	
	@Test	
	public void displayStockInfo1()
	{
		StockInfo stockInfo = stockInfoSP.requestCurrentStockData("AAPL");
		Assert.assertNotNull("Unable to retrieve stock info.", stockInfo);
	}
	
	@Test	
	public void displayStockInfo2()
	{
		List<Stock> stocks = stockInfoSP.searchForStocks("AAPL");
		Assert.assertNotNull("Unable to retrieve search results.", stocks);
		Assert.assertTrue(stocks.size() == 1);
	}
	
	@Test	
	public void displayStockInfoProviderUnreachable()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void showUserTransactionHistory()  throws Exception
	{		
		List<StockTransaction> transactions = vsp.getTransactionHistory("test");
		Assert.assertNotNull("Unable to retrieve transaction history.", transactions);
		Assert.assertTrue(transactions.size() > 0);
	}
	
	@Test	
	public void displayUserPortfolio() throws Exception
	{
		List<PortfolioData> entries = vsp.getPortfolioEntries("test");
		Assert.assertNotNull("Unable to retrieve portfolio entries.", entries);
		Assert.assertTrue(entries.size() > 0);
	}
	
	@Test	
	public void displayUserPortfolioProviderUnreachable() throws Exception
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void showUserPendingOrdersWhenSomeExist() throws Exception
	{
		List<Order> orders = vsp.getPendingOrders("test");
		Assert.assertNotNull("Unable to retrieve pending orders.", orders);
		Assert.assertTrue(orders.size() > 0);
	}
	
	@Test	
	public void showUserPendingOrdersWhenNoneExist() throws Exception
	{
		List<Order> orders = vsp.getPendingOrders("test");
		Assert.assertNotNull("Unable to retrieve pending orders.", orders);
		Assert.assertTrue(orders.size() == 0);
	}
	
	@Test	
	public void cancelPendingOrder() throws Exception
	{
		String userName = "test";
		List<Order> orders = vsp.getPendingOrders(userName);
		Assert.assertNotNull("Unable to retrieve pending orders.", orders);
		Assert.assertTrue(orders.size() > 0);
		
		Order order = orders.get(0);
		vsp.cancelOrder(userName, order.getId());
		
		order = Orders.getOrderById(order.getId());
		Assert.assertTrue(order.getState() == OrderState.CANCELLED);
	}
	
	@Test	
	public void buyStock()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void buyStockInsufficientFunds()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	public void buyMarketOrderStock() throws Exception
	{
		Order order = vsp.createOrder("test", 
				OrderAction.BUY.toString(), 
				"AAPL", 
				"10", 
				OrderType.MARKET.toString(),
				TimeInForce.DAY.toString(), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
		Assert.assertNotNull("Unable to create order.", order);
		Assert.assertNotNull("Order ID is null.", order.getId());
	}
	
	@Test
	public void buyMarketOrderStockInsufficientFunds()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	public void buyLimitOrderStockLimitPriceMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	public void buyLimitOrderStockLimitPriceNotMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	public void buyStopOrderStockStopPriceMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	public void buyStopOrderStockStopPriceNotMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	public void buyStopLimitOrderStockStopPriceMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	public void buyStopLimitOrderStockStopPriceNotMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	
	@Test	
	public void sellStock()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void sellStockMoreThanOwned()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void sellStockMarketOrder()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void sellStockLimitOrderLimitPriceMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void sellStockLimitOrderLimitPriceNotMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void sellStockStopLossOrderStopPriceMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void sellStockStopLossOrderStopPriceNotMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void sellStockStopLimitOrderStopPriceMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void sellStockStopLimitOrderStopPriceNotMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	
	@Test	
	public void dayOrderExecution()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void dayOrderExpired()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void goodUntilCancelledExecution()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void goodUntilCancelledExpired()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void fillOrKillFilled()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void fillOrKillKilled()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test	
	public void immediateOrCancel()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
}
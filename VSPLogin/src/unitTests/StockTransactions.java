package unitTests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.*;
import org.junit.runner.*;
import vsp.*;
import vsp.dal.requests.Orders;
import vsp.dataObject.DividendInfo;
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
	@unitTests.Order(order=1)
	public void getStockInfo()
	{
		StockInfo stockInfo = stockInfoSP.requestCurrentStockData("AAPL");
		Assert.assertNotNull("Unable to retrieve stock info.", stockInfo);
	}
	
	@Test
	@unitTests.Order(order=2)
	public void getStockSearchResults()
	{
		List<Stock> stocks = stockInfoSP.searchForStocks("AAPL");
		Assert.assertNotNull("Unable to retrieve search results.", stocks);
		Assert.assertTrue(stocks.size() == 1);
	}
	
	@Test
	@unitTests.Order(order=3)
	public void unreachableStockInfoProviderTests() throws Exception
	{
		StockInfoServiceProvider sisp = new StockInfoServiceProvider();
		sisp.setCurrentInfoBaseUrl("http://127.0.0.1"); // should timeout
		sisp.setHistoryBaseUrl("http://127.0.0.1"); // should timeout
		
		StockInfo stockInfo = sisp.requestCurrentStockData("AAPL");
		Assert.assertNull("Should not be returning any results for current stock data.", stockInfo);
		
		Assert.assertFalse(sisp.isWithinTradingHours());
		
		List<String> symbols = new ArrayList<String>();
		symbols.add("AAPL");
		symbols.add("GOOG");
		List<StockInfo> currentResults = sisp.requestCurrentStockData(symbols);
		Assert.assertNotNull(currentResults);
		Assert.assertTrue(currentResults.size() == 0);
		
		Date since = historicalDateFormat.parse("2011-10-01");
		List<HistoricalStockInfo> historicalResults = sisp.requestDailyHistoricalStockData("AAPL", since);
		Assert.assertNotNull(historicalResults);
		Assert.assertTrue(historicalResults.size() == 0);
		
		historicalResults = sisp.requestHistoricalStockData("AAPL", 3);
		Assert.assertNotNull(historicalResults);
		Assert.assertTrue(historicalResults.size() == 0);
		
		HistoricalStockInfo historicalStockInfo = sisp.requestHistoricalStockDataForDay("AAPL", since);
		Assert.assertNull("Should not be returning any results for historical stock data.", historicalStockInfo);
		
		List<DividendInfo> dividendResults = sisp.requestHistoricalDividendInfoSince("AAPL", since);
		Assert.assertNotNull(dividendResults);
		Assert.assertTrue(dividendResults.size() == 0);
	}
	
	@Test	
	public void showUserTransactionHistory()  throws Exception
	{		
		// TODO:  ensure there is at least one transaction before calling this unit test (or add some default ones to database)
		List<StockTransaction> transactions = vsp.getTransactionHistory("test");
		Assert.assertNotNull("Unable to retrieve transaction history.", transactions);
		Assert.assertTrue(transactions.size() > 0);
	}
	
	@Test	
	public void displayUserPortfolio() throws Exception
	{
		Assert.fail("This test must be performed manually.");
	}
	
	@Test	
	public void showUserPendingOrdersWhenSomeExist() throws Exception
	{
		// TODO:  ensure that at least one order has been submitted that will remain in a pending state for a few seconds
		List<Order> orders = vsp.getPendingOrders("test");
		Assert.assertNotNull("Unable to retrieve pending orders.", orders);
		Assert.assertTrue(orders.size() > 0);
	}
	
	@Test	
	public void showUserPendingOrdersWhenNoneExist() throws Exception
	{
		// TODO: ensure no pending orders are in the system
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
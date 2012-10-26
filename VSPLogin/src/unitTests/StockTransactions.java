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
import vsp.dal.requests.PortfolioEntries;
import vsp.dal.requests.Stocks;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.DividendInfo;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.Order;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.OrderState;
import vsp.utils.Enumeration.OrderType;
import vsp.utils.Enumeration.TimeInForce;
import vsp.utils.Enumeration.TransactionType;

@RunWith(OrderedRunner.class)
public class StockTransactions
{
	private final SimpleDateFormat historicalDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	private final VspServiceProvider vsp = new VspServiceProvider();
	private final StockInfoServiceProvider stockInfoSP = new StockInfoServiceProvider(); 
	private final static String userName = "test";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		// delete all stocks (should delete all portfolio entries, orders and transactions too because of the DB constraints)
		List<Stock> stocks = Stocks.getAllStocks();
		for (Stock stock : stocks)
		{
			Stocks.deleteStock(stock.getStockSymbol());
		}
		
		// ensure all stocks are gone
		stocks = Stocks.getAllStocks();
		Assert.assertNotNull(stocks);
		Assert.assertTrue(stocks.size() == 0);
		
		// ensure all owned stocks are gone
		List<PortfolioData> ownedItems = PortfolioEntries.requestAllUserStocks(userName);
		Assert.assertNotNull(ownedItems);
		Assert.assertTrue(ownedItems.size() == 0);
		
		// ensure all orders are gone
		List<Order> orders = Orders.getOrdersForUser(userName);
		Assert.assertNotNull(orders);
		Assert.assertTrue(orders.size() == 0);
		
		// ensure all transactions are gone
		List<StockTransaction> transactions = Transactions.getTransactionsForUser(userName);
		Assert.assertNotNull(transactions);
		Assert.assertTrue(transactions.size() == 0);
		
		// set user balance to 0
		Users.updateBalance(userName, 0.0);
	}
	
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
		Assert.assertTrue(stocks.size() >= 1);
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
	@unitTests.Order(order=4)
	public void showUserPendingOrdersWhenNoneExist() throws Exception
	{
		List<Order> orders = vsp.getPendingOrders(userName);
		Assert.assertNotNull("Unable to retrieve pending orders.", orders);
		Assert.assertTrue(orders.size() == 0);
	}
	
	@Test
	@unitTests.Order(order=5)
	public void buyStockInsufficientFunds()
	{
		try
		{	
			// throws
			vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AAPL", 
				"5000", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()),
				"1.00",
				"0.0"
				);
			Assert.fail("Order creation should fail due to insufficient funds.");
		}
		catch (Exception ex)
		{
			Assert.assertTrue(ex.getLocalizedMessage().contains("insufficent funds"));
		}
	}
	
	@Test
	@unitTests.Order(order=6)
	public void buyMarketOrderStockInsufficientFunds()
	{
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;
			
			// throws
			vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"GOOG", 
				"5000", 
				Integer.toString(OrderType.MARKET.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
			Assert.fail("Order creation should fail due to insufficient funds.");
		}
		catch (Exception ex)
		{
			Assert.assertTrue(ex.getLocalizedMessage().contains("insufficent funds"));
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=7)
	public void buyStock() throws Exception
	{
		// make sure we have enough money
		Users.updateBalance(userName, Users.DEFAULT_BALANCE);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"WMT", 
				"50", 
				Integer.toString(OrderType.MARKET.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=8)
	public void buyMarketOrderStock() throws Exception
	{
		// make sure we have enough money
		Users.updateBalance(userName, Users.DEFAULT_BALANCE);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"MSFT", 
				"10", 
				Integer.toString(OrderType.MARKET.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
		
	@Test
	@unitTests.Order(order=9)
	public void buyLimitOrderStockLimitPriceMet() throws Exception
	{
		// make sure we have enough money
		Users.updateBalance(userName, Users.DEFAULT_BALANCE);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;			
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()),
				"500.00", // current price is over 200
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=10)
	public void buyLimitOrderStockLimitPriceNotMet() throws Exception
	{
		// make sure we have enough money
		Users.updateBalance(userName, Users.DEFAULT_BALANCE);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.GOODUNTILCANCELED.getValue()),
				"50.00", // current price is 2xx.xx
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			// should not be completed because cannot be executed (limit price too high)
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.PENDING);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=11)
	public void buyStopOrderStockStopPriceMet() throws Exception
	{
		// make sure we have enough money
		Users.updateBalance(userName, Users.DEFAULT_BALANCE);
		
		Order order = null;
		try
		{		
			VspServiceProvider vsp = new VspServiceProvider();
			TestStockInfoProvider test = new TestStockInfoProvider();
			StockInfo info = new StockInfo("AMZN", "Amazon", 200.00, 100.00, new Date(), 0.00, 100.00, 0.06, 50000, 106.00, 100.00, 106.05, 106.00, 0.00);
			test.setCurrentStockData(info);
			vsp.setStockInfo(test);
			
			StockInfoServiceProvider.ForceWithinHours = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.STOP.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()),
				"0.00",
				"107.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=12)
	public void buyStopOrderStockStopPriceNotMet() throws Exception
	{
		// make sure we have enough money
		Users.updateBalance(userName, Users.DEFAULT_BALANCE);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.STOP.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()),
				"0.00",
				"1000.00" // current price is 2xx.xx
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			// should not be completed because cannot be executed (stop price too high)
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.PENDING);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=13)
	public void buyStopLimitOrderStockStopPriceMet() throws Exception
	{
		// make sure we have enough money (growing number of pending orders means we need more)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 2.0);
		
		Order order = null;
		try
		{		
			VspServiceProvider vsp = new VspServiceProvider();
			TestStockInfoProvider test = new TestStockInfoProvider();
			StockInfo info = new StockInfo("AMZN", "Amazon", 200.00, 100.00, new Date(), 0.00, 100.00, 0.06, 50000, 106.00, 100.00, 106.05, 106.00, 0.00);
			test.setCurrentStockData(info);
			vsp.setStockInfo(test);
			
			StockInfoServiceProvider.ForceWithinHours = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.STOPLIMIT.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()),
				"950.00", // current price is 2xx.xx 
				"106.50"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=14)
	public void buyStopLimitOrderStockStopPriceNotMet() throws Exception
	{
		// make sure we have enough money (growing number of pending orders means we need more)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 2.0);
		
		Order order = null;
		try
		{		
			VspServiceProvider vsp = new VspServiceProvider();
			TestStockInfoProvider test = new TestStockInfoProvider();
			StockInfo info = new StockInfo("AMZN", "Amazon", 200.00, 100.00, new Date(), 0.00, 100.00, 0.06, 50000, 106.00, 100.00, 106.05, 106.00, 0.00);
			test.setCurrentStockData(info);
			vsp.setStockInfo(test);
			
			StockInfoServiceProvider.ForceWithinHours = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.STOPLIMIT.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()),
				"125.00", // current price is 2xx.xx 
				"107.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.PENDING);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=15)
	public void dayOrderExecution() throws Exception
	{
		// make sure we have enough money (growing number of pending orders means we need more)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 2.0);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"MSFT", 
				"10", 
				Integer.toString(OrderType.MARKET.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test	
	@unitTests.Order(order=16)
	public void dayOrderExpired() throws Exception
	{
		// make sure we have enough money (growing number of pending orders means we need more)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 2.0);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;
			VspServiceProvider.SkipProcessing = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"MSFT", 
				"10", 
				Integer.toString(OrderType.MARKET.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			StockInfoServiceProvider.ForceWithinHours = false;
			VspServiceProvider.SkipProcessing = false;
			
			Date todayLastYear = new Date();
			todayLastYear.setYear(todayLastYear.getYear() - 1);
			Orders.changeSubmittedAndEvaluatedDates(userName, order.getId(), todayLastYear);
			vsp.processPendingOrders(userName);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.CANCELLED);
			
			StockTransaction transaction = Transactions.getTransactionForOrderId(order.getId());
			Assert.assertNotNull(transaction);
			Assert.assertTrue(transaction.getType() == TransactionType.CANCELLATION);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
			VspServiceProvider.SkipProcessing = false;
		}
	}
	
	@Test	
	@unitTests.Order(order=17)
	public void goodUntilCancelledExecution() throws Exception
	{
		// make sure we have enough money
		Users.updateBalance(userName, Users.DEFAULT_BALANCE);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;			
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.GOODUNTILCANCELED.getValue()),
				"500.00", // current price is over 200
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test	
	@unitTests.Order(order=18)
	public void goodUntilCancelledExpired() throws Exception
	{
		// make sure we have enough money (growing number of pending orders means we need more)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 2.0);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;
			VspServiceProvider.SkipProcessing = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"MSFT", 
				"10", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.GOODUNTILCANCELED.getValue()),
				"1.00",
				"0.0"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			StockInfoServiceProvider.ForceWithinHours = false;
			VspServiceProvider.SkipProcessing = false;
			
			Date todayLastYear = new Date();
			todayLastYear.setYear(todayLastYear.getYear() - 1);
			Orders.changeSubmittedAndEvaluatedDates(userName, order.getId(), todayLastYear);
			vsp.processPendingOrders(userName);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.CANCELLED);
			
			StockTransaction transaction = Transactions.getTransactionForOrderId(order.getId());
			Assert.assertNotNull(transaction);
			Assert.assertTrue(transaction.getType() == TransactionType.CANCELLATION);
			Assert.assertTrue(transaction.getNote().contains("limit reached"));
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
			VspServiceProvider.SkipProcessing = false;
		}
	}
	
	@Test	
	@unitTests.Order(order=19)
	public void fillOrKillFilled() throws Exception
	{
		// make sure we have enough money (must buy at least 101 shares)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 10.0);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;	
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"101", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.FILLORKILL.getValue()),
				"800.00", // current price is over 200
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test	
	@unitTests.Order(order=20)
	public void fillOrKillKilled() throws Exception
	{
		// make sure we have enough money (must buy at least 101 shares)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 10.0);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;	
			VspServiceProvider.SkipProcessing = true;
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"101", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.FILLORKILL.getValue()),
				"15.00", // current price is over 200
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			StockInfoServiceProvider.ForceWithinHours = false;
			VspServiceProvider.SkipProcessing = false;
			vsp.processPendingOrders(userName);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.CANCELLED);
			
			StockTransaction transaction = Transactions.getTransactionForOrderId(order.getId());
			Assert.assertNotNull(transaction);
			Assert.assertTrue(transaction.getType() == TransactionType.CANCELLATION);
			Assert.assertTrue(transaction.getNote().contains("Market closed"));
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
			VspServiceProvider.SkipProcessing = false;
		}
	}
	
	@Test	
	@unitTests.Order(order=21)
	public void immediateOrCancelExecuted() throws Exception
	{
		// make sure we have enough money (growing number of pending orders means we need more)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 2.0);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;	
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.IMMEDIATEORCANCEL.getValue()),
				"750.00", // current price is over 200
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test	
	@unitTests.Order(order=22)
	public void immediateOrCancelCancelled() throws Exception
	{
		// make sure we have enough money (growing number of pending orders means we need more)
		Users.updateBalance(userName, Users.DEFAULT_BALANCE * 2.0);
		
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;	
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.BUY.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.IMMEDIATEORCANCEL.getValue()),
				"15.00", // current price is over 200
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.CANCELLED);
			
			StockTransaction transaction = Transactions.getTransactionForOrderId(order.getId());
			Assert.assertNotNull(transaction);
			Assert.assertTrue(transaction.getType() == TransactionType.CANCELLATION);
			Assert.assertTrue(transaction.getNote().contains("Unable to fill immediately"));
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=23)
	public void displayUserPortfolio() throws Exception
	{
		List<PortfolioData> portfolioEntries = vsp.getPortfolioEntries(userName);
		Assert.assertNotNull(portfolioEntries);
		
		// purchased MSFT, WMT and AMZN so far...
		Assert.assertTrue(portfolioEntries.size() == 3);
	}
	
	@Test
	@unitTests.Order(order=24)
	public void showUserPendingOrdersWhenSomeExist() throws Exception
	{
		List<Order> orders = vsp.getPendingOrders(userName);
		Assert.assertNotNull("Unable to retrieve pending orders.", orders);
		Assert.assertTrue(orders.size() > 0);
	}
	
	@Test
	@unitTests.Order(order=25)
	public void cancelPendingOrder() throws Exception
	{
		List<Order> pendingOrders = vsp.getPendingOrders(userName);
		Assert.assertNotNull("Unable to retrieve pending orders.", pendingOrders);
		Assert.assertTrue(pendingOrders.size() > 0);
		
		// cancel all pending orders
		for (int i = 0; i < pendingOrders.size(); ++i)
		{
			Order order = pendingOrders.get(i);
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			vsp.cancelOrder(userName, order.getId());
			
			order = Orders.getOrderById(order.getId());
			Assert.assertTrue(order.getState() == OrderState.CANCELLED);
		}
	}
	
	@Test
	@unitTests.Order(order=26)
	public void sellStockMoreThanOwned()
	{
		try
		{
			// throws because we only own 50 shares of Walmart
			vsp.createOrder(userName, 
				Integer.toString(OrderAction.SELL.getValue()), 
				"WMT", 
				"51", 
				Integer.toString(OrderType.MARKET.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
			
			Assert.fail("The previous line should have thrown an exception.");
		}
		catch (Exception ex)
		{
			// expected
		}
	}
	
	@Test
	@unitTests.Order(order=27)
	public void sellStock()
	{
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;	
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.SELL.getValue()), 
				"WMT", 
				"25", 
				Integer.toString(OrderType.MARKET.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
		
	@Test
	@unitTests.Order(order=28)
	public void sellStockMarketOrder()
	{
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;	
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.SELL.getValue()), 
				"WMT", 
				"25", 
				Integer.toString(OrderType.MARKET.getValue()),
				Integer.toString(TimeInForce.DAY.getValue()), // only DAY is supported for a MARKET order
				"0.0",
				"0.0"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=29)
	public void sellStockLimitOrderLimitPriceMet()
	{
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;			
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.SELL.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.GOODUNTILCANCELED.getValue()),
				"100.00", // current price is over 200
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.COMPLETE);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=30)
	public void sellStockLimitOrderLimitPriceNotMet()
	{
		Order order = null;
		try
		{
			StockInfoServiceProvider.ForceWithinHours = true;			
			order = vsp.createOrder(userName, 
				Integer.toString(OrderAction.SELL.getValue()), 
				"AMZN", 
				"10", 
				Integer.toString(OrderType.LIMIT.getValue()),
				Integer.toString(TimeInForce.GOODUNTILCANCELED.getValue()),
				"1000.00", // current price is over 200
				"0.00"
				);
			
			Assert.assertNotNull("Unable to create order.", order);
			Assert.assertNotNull("Order ID is null.", order.getId());
			Assert.assertTrue(order.getState() == OrderState.PENDING);
			
			order = Orders.getOrderById(order.getId());
			Assert.assertNotNull(order);
			Assert.assertTrue(order.getState() == OrderState.PENDING);
		}
		catch (Exception ex)
		{
			Assert.fail("Unhandled exception: " + ex.getLocalizedMessage());
		}
		finally
		{
			StockInfoServiceProvider.ForceWithinHours = false;
		}
	}
	
	@Test
	@unitTests.Order(order=31)
	public void sellStockStopLossOrderStopPriceMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	@unitTests.Order(order=32)
	public void sellStockStopLossOrderStopPriceNotMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	@unitTests.Order(order=33)
	public void sellStockStopLimitOrderStopPriceMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	@unitTests.Order(order=34)
	public void sellStockStopLimitOrderStopPriceNotMet()
	{
		// TODO: implement
		Assert.fail("Not yet implemented");
	}
	
	@Test
	@unitTests.Order(order=35)
	public void showUserTransactionHistoryWhenSomeExist()  throws Exception
	{		
		// TODO:  ensure there is at least one transaction before calling this unit test (or add some default ones to database)
		List<StockTransaction> transactions = vsp.getTransactionHistory(userName);
		Assert.assertNotNull("Unable to retrieve transaction history.", transactions);
		Assert.assertTrue(transactions.size() > 10);
	}
}
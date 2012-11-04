package vsp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vsp.dal.requests.Orders;
import vsp.dal.requests.PortfolioEntries;
import vsp.dal.requests.Roles;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.Order;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.OrderState;
import vsp.utils.Enumeration.OrderType;
import vsp.utils.Enumeration.RoleType;
import vsp.utils.Enumeration.SecurityQuestion;
import vsp.utils.Enumeration.TimeInForce;
import vsp.utils.Validate;


public class VspServiceProvider
{
	private IStockInfo sisp = new StockInfoServiceProvider();
	private VirtualTradingServiceProvider vtsp = new VirtualTradingServiceProvider();
	
	public static boolean SkipProcessing;
	
	public VspServiceProvider()
	{
		// no implementation required
	}
	
	// for unit testing
	public void setStockInfo(IStockInfo sisp)
	{
		this.sisp = sisp;
	}
	
	// for unit testing
	public void setTrading(VirtualTradingServiceProvider vtsp)
	{
		this.vtsp = vtsp;
	}
	
	public void cancelOrder(String userName, String orderId)
			throws SQLException, SqlRequestException
	{
		Orders.changeOrderState(userName, orderId, OrderState.PENDING, OrderState.CANCELLED);
		
		Order order = Orders.getOrderById(orderId);
		if (order == null)
		{
			throw (new SqlRequestException("Error:  Cannot find order."));
		}
		
		Date cancelled = new Date();
		
		// add cancelled transaction
		StockTransaction transaction = StockTransaction.CreateNewCancellation(userName, order, cancelled, "Cancelled by user");
		Transactions.addTransaction(transaction);
	}
	
	public Order createOrder(String userName, String action, String stockSymbol, String quantity, String type, String timeInForce ,
			String limitPrice, String stopPrice)
			throws SQLException, SqlRequestException, ValidationException
	{
		// validate inputs
		OrderAction orderAction = OrderAction.convert(Integer.parseInt(action));
		OrderType orderType = OrderType.convert(Integer.parseInt(type));
		TimeInForce tif = TimeInForce.convert(Integer.parseInt(timeInForce));
		float orderQuantity = Validate.validateQuantity(quantity);
		double limit = 0.0, stop = 0.0;
		if (orderType == OrderType.LIMIT || orderType == OrderType.STOPLIMIT)
		{
			limit = Validate.validateLimitPrice(limitPrice);
		}
		
		if (orderType == OrderType.STOP || orderType == OrderType.STOPLIMIT)
		{
			stop = Validate.validateStopPrice(stopPrice);
		}
		
		StockInfo stockInfo = sisp.requestCurrentStockData(stockSymbol);
		if (stockInfo == null)
		{
			throw new ValidationException("Error:  Please enter a valid stock symbol.");
		}
	
		
		// validate combination of inputs
		boolean withinTradingHours = sisp.isWithinTradingHours();
		if (orderType == OrderType.MARKET && !withinTradingHours)
		{
			throw new ValidationException("Error:  A Market Order may only be submitted when the market is open.");
		}
		
		if (orderType == OrderType.MARKET && tif != TimeInForce.DAY)
		{
			throw new ValidationException("Error:  A Market Order may only use a Time In Force of 'Day'.");
		}
		
		if ((orderType == OrderType.STOP || orderType == OrderType.STOPLIMIT) 
				&& (tif == TimeInForce.FILLORKILL || tif == TimeInForce.IMMEDIATEORCANCEL))
		{
			throw new ValidationException("Error:  A " + orderType.toString() + " Order may only use a Time In Force of 'Day' or 'Good Until Cancelled'.");
		}
		
		if (tif == TimeInForce.FILLORKILL)
		{
			if (!withinTradingHours)
			{
				throw new ValidationException("Error:  A Fill or Kill order may only be submitted when the market is open.");
			}
			else if (orderQuantity <= 100)
			{
				throw new ValidationException("Error:  A Fill or Kill order must be for 101 or more shares.");
			}
		}
		
		if (tif == TimeInForce.IMMEDIATEORCANCEL)
		{
			if (!withinTradingHours)
			{
				throw new ValidationException("Error:  An Immediate or Cancel order may only be submitted when the market is open.");
			}
		}
		
		if (orderType == OrderType.STOP || orderType == OrderType.STOPLIMIT)
		{
			if (orderAction == OrderAction.BUY)
			{
				if (stop <= stockInfo.getLastTradePrice())
				{
					throw new ValidationException("Error:  The buy Stop Price must be above the current trade price.");
				}
			}
			else // SELL
			{
				if (stop >= stockInfo.getLastTradePrice())
				{
					throw new ValidationException("Error:  The sell Stop Price must be below the current trade price.");
				}
			}
		}
				
		Order newOrder = Order.CreateNewOrder(userName, stockInfo.getStock(), orderAction, orderQuantity, orderType, limit, stop, tif);
		AccountData userInfo = Users.requestAccountData(userName);
		
		if (orderAction == OrderAction.BUY)
		{
			// get pending commitments (best effort)
			double commitments = 0.0;
			List<Order> pendingOrders = this.getPendingOrders(userName);
			if (pendingOrders != null && pendingOrders.size() > 0)
			{
				List<String> symbolList = new ArrayList<String>();
				for (Order pendingOrder : pendingOrders)
				{
					if (pendingOrder.getAction() == OrderAction.BUY)
					{
						symbolList.add(pendingOrder.getStock().getStockSymbol());
					}
				}
				
				List<StockInfo> stockInfos = sisp.requestCurrentStockData(symbolList);
				Map<String,StockInfo> stockInfoMap = new HashMap<String,StockInfo>(stockInfos.size());
				if (stockInfos != null)
				{
					for (StockInfo info : stockInfos)
					{
						stockInfoMap.put(info.getSymbol(), info);
					}
					
					String symbol;
					for (Order pendingOrder : pendingOrders)
					{
						if (pendingOrder.getAction() == OrderAction.BUY)
						{
							symbol = pendingOrder.getStock().getStockSymbol();
							if (stockInfoMap.containsKey(symbol))
							{
								commitments += pendingOrder.getLatestEstimatedValue(stockInfoMap.get(symbol));
							}
						}
					}
				}
			}
						
			// get commitment for this order
			commitments += newOrder.getLatestEstimatedValue(stockInfo);
			commitments *= -1;
			
			if (commitments > userInfo.getBalance())
			{
				throw new ValidationException("Error:  You have insufficent funds for this Buy order.");
			}
		}
		else if (orderAction == OrderAction.SELL)
		{
			// see if trader already owns stock
			PortfolioData ownedStock = null;
			List<PortfolioData> ownedStocks = this.getPortfolioEntries(userName);
			if (ownedStocks != null && ownedStocks.size() > 0)
			{
				for (PortfolioData data : ownedStocks)
				{
					if (data.getStock().getStockSymbol().equals(stockSymbol))
					{
						ownedStock = data;
						break;
					}
				}
			}
			
			if (ownedStock != null)
			{
				// see if trader has enough shares that are not already committed elsewhere
				float quantityPending = 0.0f;
				List<Order> pendingOrders = this.getPendingOrders(userName);
				if (pendingOrders != null && pendingOrders.size() > 0)
				{
					for (Order pendingOrder : pendingOrders)
					{
						if (pendingOrder.getAction() == OrderAction.SELL 
								&& pendingOrder.getStock().getStockSymbol().equals(stockSymbol))
						{
							quantityPending += pendingOrder.getQuantity();
						}
					}
				}
				
				if (orderQuantity > (ownedStock.getQuantity() - quantityPending))
				{
					throw new ValidationException("Error:  You do not have enough of '" + stockInfo.getSymbol() + "' available to sell " + orderQuantity + " shares.");
				}
			}
			else
			{
				throw new ValidationException("Error:  You do not own any shares of the '" + stockInfo.getSymbol() + "' stock.");
			}
		}
				
		// submit order (add to DB)
		Orders.addOrder(newOrder);
		
		// attempt to process order(s) with VTSP
		if (!SkipProcessing)
		{
			vtsp.processPendingOrders(userName);
		}
		
		return newOrder;
	}
	
	public void processDividends(String userName) throws SQLException, SqlRequestException
	{
		vtsp.processDividends(userName);
	}
	
	public void processPendingOrders(String userName) throws SQLException, SqlRequestException
	{
		vtsp.processPendingOrders(userName);
	}
	
	public boolean createTraderAccount(AccountData userAccount) throws SQLException, 
	  SqlRequestException, ValidationException
	{
	  if(Users.addTraderAccount(userAccount)){
	    return Roles.addNewUserRole(userAccount.getUserName(), RoleType.TRADER);
	  }
	  return false;
	}
	
//	public boolean createTraderAccount(String userName, String password1, 
//			String password2, String email, String question, String answer) 
//			throws ValidationException, SQLException, SqlRequestException 
//	{
//		if(Users.addTraderAccount(userName, email, password1, password2, question, answer))
//		{
//			return Roles.addNewUserRole(userName, RoleType.TRADER);
//		}
//		
//		return false;
//	}
	
	public void deleteTraderAccount(String userName) 
			throws ValidationException,
		SQLException, SqlRequestException
	{
		Users.deleteTraderAccount(userName);
	}
	
	public boolean updateUserPassword(String userName ,
			String passwordUpdate1, String passwordUpdate2) throws SQLException, 
			SqlRequestException, ValidationException
	{
	  return Users.updatePassword(userName, passwordUpdate1, passwordUpdate2);
	}
	
	
	public boolean checkUserSecurityQuestion(String userName, 
	    String securityQuestionAnswer) throws SQLException
	{
	  return Users.checkAnswer(userName, securityQuestionAnswer);
	}
	
	public boolean checkUserEmail(String userName, 
		      String email) throws SQLException
	{
		return Users.checkEmail(userName, email);
	}
	
	public boolean checkUserPassword(String userName, 
      String password) throws SQLException
	{
		return Users.checkPassword(userName, password);
	}
	
	public SecurityQuestion getUserSecurityQuestion(String userName) 
	    throws SQLException, ValidationException
	{
	  return Users.requestSecurityQuestion(userName);
	}
	
	public AccountData getAccountInfo(String userName) 
			throws SQLException
	{
		AccountData data = Users.requestAccountData(userName);	
		return data;
	}
	
	public List<Order> getPendingOrders(String userName) 
			throws SQLException, SqlRequestException
	{
		return Orders.getPendingOrdersForUser(userName);
	}
	
	public List<PortfolioData> getPortfolioEntries(String userName) 
			throws SQLException
	{
		return PortfolioEntries.requestAllUserStocks(userName);
	}
	
	public Map<String,StockInfo> getLatestStockInfo(List<String> symbols)
	{
		Map<String,StockInfo> map = new HashMap<String,StockInfo>();
		List<StockInfo> results = sisp.requestCurrentStockData(symbols);
		if (results != null && results.size() > 0)
		{
			for (StockInfo info : results)
			{
				map.put(info.getSymbol(), info);
			}
		}
		
		return map;
	}
	
	public List<String> getTraders() 
			throws SQLException
	{
		return Users.queryAllTraders();
	}
	
	public List<StockTransaction> getTransactionHistory(String userName)
			throws SQLException, SqlRequestException
	{
		return Transactions.getTransactionsForUser(userName);
	}
}

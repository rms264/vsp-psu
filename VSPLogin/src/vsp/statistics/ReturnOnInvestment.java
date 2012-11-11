package vsp.statistics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import unitTests.StockStatisticsTestSISP;
import vsp.StockInfoServiceProvider;
import vsp.dal.requests.PortfolioEntries;
import vsp.dal.requests.Stocks;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.utils.Enumeration.OrderAction;

public class ReturnOnInvestment {
  AccountData userAccount;
  IStockInfo stockService;
  
  public static void main(String[] args){
    String stockSymbol = null;
    try {
      AccountData account = Users.requestAccountData("rob");
      ReturnOnInvestment roi = new ReturnOnInvestment(account);
      Stock stock = Stocks.getStock("SIRI");
      stockSymbol = stock.getStockSymbol();
      System.out.println("ROI = " + roi.getReturnOnInvestment(stockSymbol));
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
//    StockInfoServiceProvider stockService = new StockInfoServiceProvider();
//    Calendar cal = Calendar.getInstance();
//    cal.clear();
//    cal.set(2008, 9, 30);
//    HistoricalStockInfo stockInfo = stockService.requestHistoricalStockDataForDay("SIRI", cal.getTime());
    
  }
  
  public static ReturnOnInvestment createTestROI(AccountData user){
    ReturnOnInvestment ROI = new ReturnOnInvestment(user);
    ROI.stockService = new StockStatisticsTestSISP();
    return ROI;
  }
  
  
  public ReturnOnInvestment(AccountData user)
  {
    userAccount = user;
    stockService = new StockInfoServiceProvider();
  }
  
  public double getReturnOnInvestment(String stockSymbol){
    List<StockTransaction> transactions = getTransactions(stockSymbol);
    double value = 0;
    try {
      PortfolioData entry = PortfolioEntries.getEntry(
          userAccount.getUserName(), stockSymbol);
      
      value = getReturnOnInvestment(stockSymbol, transactions, entry);
    } catch (SqlRequestException | SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return value;
  }
  
  public double getReturnOnInvestment(String stockSymbol, 
      List<StockTransaction> transactions, PortfolioData entry)
  {

    double investment = 0;
    double value = 0;
    double roi = 0; //percentage;
    for(StockTransaction trans : transactions){
      if(trans.getOrder().getAction() == OrderAction.BUY){
        investment += trans.getValue();
      }else if(trans.getOrder().getAction() == OrderAction.SELL){
        investment -= trans.getValue();
      }
    }
    
      
      StockInfo stockData = stockService.requestCurrentStockData(stockSymbol);
      
      value = entry.getQuantity() * stockData.getClose();
      
      roi = (value - investment)/investment; 
    
    
    return roi;
  }
  
  private List<StockTransaction> getTransactions(String stockSymbol){
    List<StockTransaction> transactions = new ArrayList<StockTransaction>();
    try {
      transactions = Transactions.getAllExecutedTransactionsForUserAndStock(
          userAccount.getUserName(), stockSymbol);
    } catch (SqlRequestException | SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return transactions;
  }
}

package vsp.statistics;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import unitTests.StockStatisticsTestSISP;
import vsp.StockInfoServiceProvider;
import vsp.dal.requests.Stocks;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.utils.Enumeration.OrderAction;

public class CompoundAnualGrowthRate {

  private final AccountData userAccount;
  private IStockInfo stockService = new StockInfoServiceProvider();
  
  public static void main(String[] args){
    try {
      AccountData account = Users.requestAccountData("rob");
      Stock stock = Stocks.getStock("SIRI");
      CompoundAnualGrowthRate cagr = new CompoundAnualGrowthRate(account);
      System.out.println("CAGR: " + cagr.calculate(stock.getStockSymbol()));
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static CompoundAnualGrowthRate createTestCAGR(AccountData user){
    CompoundAnualGrowthRate cagr = new CompoundAnualGrowthRate(user);
    cagr.stockService = new StockStatisticsTestSISP();
    
    return cagr;
  }
  
  public CompoundAnualGrowthRate(AccountData user){
    userAccount = user;
  }
  
  public double calculate(String stockSymbol){
    double value = 0;
    try {
      StockTransaction initialTrans = Transactions.getInitialExecutedTransaction(
          userAccount.getUserName(), 
          stockSymbol);
      
      List<StockTransaction> transactions = 
          Transactions.getAllExecutedTransactionsForUserAndStock(
                          userAccount.getUserName(), 
                          stockSymbol);
      
      value = calculate(stockSymbol, initialTrans, transactions);
    } catch (SQLException | SqlRequestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return value;
  }
  
  public double calculate(String stockSymbol, StockTransaction initialTrans,
      List<StockTransaction> transactions)
  {
    double endingValue;
    int years;
    Calendar today = Calendar.getInstance();
      
    double investment = 0.0;
    for(int i = transactions.size()-1; i >= 0; i--){
      StockTransaction trans = transactions.get(i);
      if(trans.getOrder().getAction() == OrderAction.BUY){
        investment += trans.getValue();
      }
      else if(trans.getOrder().getAction() == OrderAction.SELL){
        investment -= trans.getValue();
      }
    }
    
    
    StockInfo stockInfo = stockService.requestCurrentStockData(stockSymbol);
    
    endingValue = initialTrans.getQuantity() * stockInfo.getClose();
    
    Calendar beginingDate = Calendar.getInstance();
    beginingDate.clear();
    beginingDate.setTime(initialTrans.getDateTime());
    
    years = today.get(Calendar.YEAR) - beginingDate.get(Calendar.YEAR);
    if(years <= 0){
      years = 1;
    }
    return (Math.pow((endingValue/investment), (1.0/years)) -1);
  }
}

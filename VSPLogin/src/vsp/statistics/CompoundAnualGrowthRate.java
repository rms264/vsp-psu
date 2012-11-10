package vsp.statistics;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import vsp.StockInfoServiceProvider;
import vsp.dal.requests.Stocks;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.utils.Enumeration.OrderAction;

public class CompoundAnualGrowthRate {

  private final AccountData userAccount;
  private final StockInfoServiceProvider stockService = new StockInfoServiceProvider();
  
  public static void main(String[] args){
    try {
      AccountData account = Users.requestAccountData("rob");
      Stock stock = Stocks.getStock("GE");
      CompoundAnualGrowthRate cagr = new CompoundAnualGrowthRate(account);
      System.out.println("CAGR: " + cagr.calculate(stock));
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public CompoundAnualGrowthRate(AccountData user){
    userAccount = user;
  }
  
  public double calculate(Stock stock){
    double growthRate = 0.0;
    double endingValue;
    int years;
    Calendar today = Calendar.getInstance();
    try {
      StockTransaction initialTrans = Transactions.getInitialExecutedTransaction(
                                        userAccount.getUserName(), 
                                        stock.getStockSymbol());
      List<StockTransaction> transactions = 
          Transactions.getAllExecutedTransactionsForUserAndStock(
                          userAccount.getUserName(), 
                          stock.getStockSymbol());
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
      
      
      StockInfo stockInfo = stockService.requestCurrentStockData(stock.getStockSymbol());
      
      endingValue = initialTrans.getQuantity() * stockInfo.getClose();
      
      Calendar beginingDate = Calendar.getInstance();
      beginingDate.clear();
      beginingDate.setTime(initialTrans.getDateTime());
      
      years = today.get(Calendar.YEAR) - beginingDate.get(Calendar.YEAR);
      if(years <= 0){
        years = 1;
      }
      return (Math.pow((endingValue/investment), (1.0/years)) -1);
      
      
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SqlRequestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return growthRate;
  }
  
}

package vsp.statistics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import vsp.StockInfoServiceProvider;
import vsp.dal.requests.Stocks;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.utils.Enumeration.TimeType;

public class GeometricAverageRateOfReturn {
  private AccountData userAccount;
  private StockTransaction initialInvestment;
  private final StockInfoServiceProvider stockService = new StockInfoServiceProvider();
  
  public static void main(String[] args){
    try {
      AccountData account = Users.requestAccountData("rob");
      Stock stock = Stocks.getStock("GE");
      GeometricAverageRateOfReturn gror = new GeometricAverageRateOfReturn(account);
      System.out.println("GROR: " + gror.getAverageRateOfReturn(stock, TimeType.DAY));
      System.out.println("GROR: " + gror.getAverageRateOfReturn(stock, TimeType.WEEK));
      System.out.println("GROR: " + gror.getAverageRateOfReturn(stock, TimeType.MONTH));
      System.out.println("GROR: " + gror.getAverageRateOfReturn(stock, TimeType.YEAR));
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  public GeometricAverageRateOfReturn(AccountData user) {
    userAccount = user;
  }
  
 
  public double getAverageRateOfReturn(Stock stock, TimeType calendarTimeType){
    List<HistoricalStockInfo> stockData;
    try {
      initialInvestment = 
          Transactions.getInitialExecutedTransaction(
              userAccount.getUserName(), 
              stock.getStockSymbol());
      
      switch (calendarTimeType){
      case DAY:{
        stockData = stockService.requestDailyHistoricalStockData(
                                  stock.getStockSymbol(), 
                                  initialInvestment.getDateTime());
        
        return calculateGROR(stockData);
      }
      case WEEK:{
        stockData = stockService.requestWeeklyHistoricalStockData(
                                  stock.getStockSymbol(), 
                                  initialInvestment.getDateTime());

        return calculateGROR(stockData);
      }
      case MONTH:{
        stockData = stockService.requestMonthlyHistoricalStockData(
            stock.getStockSymbol(), 
            initialInvestment.getDateTime());

        return calculateGROR(stockData);
        
      }
      case YEAR:{
        return getYearlyAverageRateOfReturn(stock);
      }
      }
      
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SqlRequestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
   return 0;
      
  }
  
  private double calculateGROR(List<HistoricalStockInfo> data){
    double price = data.get(data.size()-1).getClose();
    float quantity = initialInvestment.getQuantity();
    double productROR = calculateROR((price * quantity), initialInvestment.getValue());
    for(int i = data.size()-2 ; i >=0 ; i--){
      productROR *= (1+calculateROR(data.get(i).getClose()*quantity, 
          data.get(i).getOpen()*quantity));
    }
    return (Math.pow(productROR, (1.0/(data.size()-1)))-1);
  }
 
 private double getYearlyAverageRateOfReturn(Stock stock){
   
   HistoricalStockInfo stockInfo;
   LinkedList<HistoricalStockInfo> stockData = new LinkedList<HistoricalStockInfo>();
   Calendar today = Calendar.getInstance();
   Calendar date = Calendar.getInstance();
   date.clear();
   date.setTime(initialInvestment.getDateTime());
   while(date.before(today)){
     stockInfo = getNextStockInfo(date, stock.getStockSymbol());
     stockData.addFirst(stockInfo);
     date.add(Calendar.YEAR, 1);
   }
   
   return calculateGROR(stockData);
 }
  
  private double calculateROR(double returnVal, double capital){
    
    return (returnVal - capital)/capital;
  }
  
  
  private HistoricalStockInfo getNextStockInfo(Calendar date, String symbol){
    HistoricalStockInfo data = null;
    Calendar requestDate = Calendar.getInstance();
    requestDate.clear();
    requestDate.setTime(date.getTime());
    int attempts = 0;
    while(data == null){
      if(attempts > 15){
        break;
      }
      data = stockService.requestHistoricalStockDataForDay(
          symbol, requestDate.getTime());
      
      requestDate.add(Calendar.DATE, 1);
      attempts++;
    }
    return data;
  }
}

package vsp.statistics;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import vsp.StockInfoServiceProvider;
import vsp.dal.requests.Transactions;
import vsp.dataObject.AccountData;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.Stock;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.utils.Enumeration.TimeType;

public class GeometricAverageRateOfReturn {
  private AccountData userAccount;
  private StockTransaction initialInvestment;
  private final StockInfoServiceProvider stockService = new StockInfoServiceProvider();
  
  public GeometricAverageRateOfReturn(AccountData user) {
    userAccount = user;
  }
  
 
  public double getAverageRateOfReturn(Stock stock, TimeType calendarTimeType){
    try {
      initialInvestment = 
          Transactions.getInitialExecutedTransaction(
              userAccount.getUserName(), 
              stock.getStockSymbol());
      
      switch (calendarTimeType){
      case DAY:{
        return getDailyAverageRateOfReturn(stock);
      }
      case WEEK:{
        return getWeeklyAverageRateOfReturn(stock);
      }
      case MONTH:{
        return getMonthlyAverageRateOfReturn(stock);
        
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
  
  private double getDailyAverageRateOfReturn(Stock stock){
    
    List<HistoricalStockInfo>stockInfo = 
        stockService.requestDailyHistoricalStockData(
            stock.getStockSymbol(), 
            initialInvestment.getDateTime());
    double price = stockInfo.get(stockInfo.size()-1).getClose();
    float quantity = initialInvestment.getQuantity();
    double productROR = calculateROR((price * quantity), initialInvestment.getValue());
    for(int i = stockInfo.size()-2 ; i >=0 ; i--){
      productROR *= (1+calculateROR(stockInfo.get(i).getClose()*quantity, 
                                 stockInfo.get(i).getOpen()*quantity));
    }
    
    return nRoot(stockInfo.size()-1, productROR);
  }
  
  private double getWeeklyAverageRateOfReturn(Stock stock){
    
    HistoricalStockInfo stockInfo;
    double capital = initialInvestment.getValue();
    float quantity = initialInvestment.getQuantity();
    double productROR = 1.0;
    double returnValue = 0.0;
    Calendar today = Calendar.getInstance();
    Calendar date = Calendar.getInstance();
    date.clear();
    date.setTime(initialInvestment.getDateTime());
    int period = 0;
    while(date.before(today)){
      stockInfo = stockService.requestHistoricalStockDataForDay(
          stock.getStockSymbol(), date.getTime());
      returnValue = stockInfo.getClose() * quantity;
      productROR *= calculateROR(returnValue, capital);
      capital = returnValue;
      date.add(Calendar.WEEK_OF_MONTH, 1);
      period++;
    }
    
    return nRoot(period, productROR);
  }
  
 private double getMonthlyAverageRateOfReturn(Stock stock){
    
    HistoricalStockInfo stockInfo;
    double capital = initialInvestment.getValue();
    float quantity = initialInvestment.getQuantity();
    double productROR = 1.0;
    double returnValue = 0.0;
    Calendar today = Calendar.getInstance();
    Calendar date = Calendar.getInstance();
    date.clear();
    date.setTime(initialInvestment.getDateTime());
    int period = 0;
    while(date.before(today)){
      stockInfo = stockService.requestHistoricalStockDataForDay(
          stock.getStockSymbol(), date.getTime());
      returnValue = stockInfo.getClose() * quantity;
      productROR *= calculateROR(returnValue, capital);
      capital = returnValue;
      date.add(Calendar.MONTH, 1);
      period++;
    }
    
    return nRoot(period, productROR);
  }
 
 private double getYearlyAverageRateOfReturn(Stock stock){
   
   HistoricalStockInfo stockInfo;
   double capital = initialInvestment.getValue();
   float quantity = initialInvestment.getQuantity();
   double productROR = 1.0;
   double returnValue = 0.0;
   Calendar today = Calendar.getInstance();
   Calendar date = Calendar.getInstance();
   date.clear();
   date.setTime(initialInvestment.getDateTime());
   int period = 0;
   while(date.before(today)){
     stockInfo = stockService.requestHistoricalStockDataForDay(
         stock.getStockSymbol(), date.getTime());
     returnValue = stockInfo.getClose() * quantity;
     productROR *= calculateROR(returnValue, capital);
     capital = returnValue;
     date.add(Calendar.YEAR, 1);
     period++;
   }
   
   return nRoot(period, productROR);
 }
  
  private double calculateROR(double returnVal, double capital){
    
    return (returnVal - capital)/capital;
  }
  
  public static double nRoot(int n, double num)
  {
          double epsilon = 6;
          //if you weren't sure, epsilon is the precision
          int ctr = 0;
          double root = 1;
          if(n <= 0)
                  return Double.longBitsToDouble(0x7ff8000000000000L);
          //0x7ff8000000000000L is the Java constant for NaN (Not-a-Number)
          if(num == 0) //this step is just to reduce the needed iterations
                  return 0;
          while((Math.abs(Math.pow(root, n) - num) > epsilon) && (ctr++ < 1000)) //checks if the number is good enough
          {
                  root = ((1.0/n)*(((n-1.0)*root)+(num/Math.pow(root, n-1.0))));
         
          }
          return root;
  }
}

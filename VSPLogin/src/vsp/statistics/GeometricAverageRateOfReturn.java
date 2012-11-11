package vsp.statistics;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import unitTests.StockStatisticsTestSISP;
import vsp.StockInfoServiceProvider;
import vsp.VirtualTradingServiceProvider;
import vsp.dal.requests.PortfolioEntries;
import vsp.dal.requests.Stocks;
import vsp.dal.requests.Transactions;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.Stock;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.TimeType;

public class GeometricAverageRateOfReturn {
  private AccountData userAccount;
  private StockTransaction initialInvestment;
  private IStockInfo stockService = new StockInfoServiceProvider();
  private final VirtualTradingServiceProvider tradingService = new VirtualTradingServiceProvider();
  private List<StockTransaction> stockTransactions;
  float currentQuantity = 0f;
  public static void main(String[] args){
    try {
      AccountData account = Users.requestAccountData("rob");
      Stock stock = Stocks.getStock("SIRI");
      GeometricAverageRateOfReturn gror = new GeometricAverageRateOfReturn(account);
      System.out.println("GROR: " + gror.getAverageRateOfReturn(stock, TimeType.DAY));
      System.out.println("GROR: " + gror.getAverageRateOfReturn(stock, TimeType.WEEK));
      System.out.println("GROR: " + gror.getAverageRateOfReturn(stock, TimeType.MONTH));
      System.out.println("GROR: " + gror.getAverageRateOfReturn(stock, TimeType.YEAR));
    } catch (SQLException | SqlRequestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static GeometricAverageRateOfReturn createTestGROR(AccountData user){
    IStockInfo testService = new StockStatisticsTestSISP();
    GeometricAverageRateOfReturn test = new GeometricAverageRateOfReturn(user);
    test.stockService = testService;
    
    
    return test;
  }
  
  public GeometricAverageRateOfReturn(AccountData user) {
    userAccount = user;
   
  }
  
  public double getAverageRateOfReturn(Stock stock, TimeType calendarTimeType) throws SqlRequestException, SQLException{
    stockTransactions = 
        Transactions.getAllExecutedTransactionsForUserAndStock(
            userAccount.getUserName(), stock.getStockSymbol());
    
    initialInvestment = 
        Transactions.getInitialExecutedTransaction(
            userAccount.getUserName(), 
            stock.getStockSymbol());
    
    
    
    PortfolioData portEntry = PortfolioEntries.getEntry(userAccount.getUserName(),
        stock.getStockSymbol());
    
    return getAverageRateOfReturn(stock.getStockSymbol(), calendarTimeType, stockTransactions, 
        initialInvestment, portEntry);
  }
  
 
  public double getAverageRateOfReturn(String stockSymbol, TimeType calendarTimeType, 
      List<StockTransaction> stockTrans, StockTransaction init, 
      PortfolioData portEntry) throws SqlRequestException, SQLException
  {
    List<HistoricalStockInfo> stockData;
    stockTransactions = stockTrans;
    try {
      initialInvestment = init; 
      currentQuantity = portEntry.getQuantity();
      
      switch (calendarTimeType){
      case DAY:{
        stockData = stockService.requestDailyHistoricalStockData(
                                  stockSymbol, 
                                  initialInvestment.getDateTime());
        
        return calculateGROR(stockData, stockTransactions, stockSymbol);
      }
      case WEEK:{
        stockData = stockService.requestWeeklyHistoricalStockData(
                                  stockSymbol, 
                                  initialInvestment.getDateTime());

        return calculateGROR(stockData, stockTransactions, stockSymbol);
      }
      case MONTH:{
        stockData = stockService.requestMonthlyHistoricalStockData(
            stockSymbol, 
            initialInvestment.getDateTime());

        return calculateGROR(stockData, stockTransactions, stockSymbol);
        
      }
      case YEAR:{
        return getYearlyAverageRateOfReturn(stockTransactions, stockSymbol);
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
  
  private double calculateGROR(List<HistoricalStockInfo> data, 
      List<StockTransaction> transactions, String stockSymbol) throws SQLException, SqlRequestException
  {
    HistoricalStockInfo prevData = null;
    HistoricalStockInfo curData = null;
    double productROR = 1;
    int periods = 0;
    for(int i = data.size()-1 ; i >= 0; i--){
      curData = data.get(i);
      if(prevData == null){
        prevData = curData;
      }
      
//      System.out.println("Initial Date: " + initialInvestment.getDateTime());
      if(curData.getDate().before(initialInvestment.getDateTime()) || 
          curData.getDate().equals(initialInvestment.getDateTime()))
      {
        //only conserned with stock info after initial purchase;
        prevData = curData;
        continue;
      }
      double investment = 0.0;
      double returnVal = 0.0;
      double beginingQuantity = tradingService.DetermineQuantityOwnedOnDate(
                                    prevData.getDate(), 
                                    transactions, 
                                    currentQuantity);
      
      double endingQuantity = tradingService.DetermineQuantityOwnedOnDate(
                                    curData.getDate(), 
                                    transactions, 
                                    currentQuantity);
      
//      System.out.println("Period: " + prevData.getDate().toString() + " - " + curData.getDate().toString());
//      System.out.println("\tBegining Quantity: " + beginingQuantity);
//      System.out.println("\tEnding Quantity: " + endingQuantity);
      
      
      
      List<StockTransaction> transBetweenDates = 
          Transactions.getTransactionsForUserandStockBetweenDates(
              userAccount.getUserName(),
              stockSymbol,
              prevData.getDate(),
              curData.getDate());
      
      //value at begining 
      investment = beginingQuantity * prevData.getClose();
      
      //adjust value if user bought or sold stock
      for(int j = transBetweenDates.size()-1; j >= 0; j--){
        StockTransaction trans = transBetweenDates.get(j);
        if(trans.getDateTime().equals(initialInvestment.getDateTime()))
          continue;
        if(trans.getOrder().getAction() == OrderAction.BUY){
          investment += trans.getValue();
        }
        else if(trans.getOrder().getAction() == OrderAction.SELL){
          investment -= trans.getValue();
        }
//        System.out.println("\tTransaction: " + trans.toString());
      }
      
      returnVal = endingQuantity * curData.getClose();
      
//      System.out.println("\tCapital: " + investment);
//      System.out.println("\tReturn: " + returnVal);
      
      double ror = calculateROR(returnVal, investment);
//      System.out.println("\tReturn on Investment: " + ror);
      
      productROR *= ( ror + 1);
//      System.out.println("---Product ROR: " + productROR);
      prevData = curData;
      periods++;
    }
//    System.out.println("Product ROR: " + productROR);
//    System.out.println("Periods: " + periods);
    
    return (Math.pow(productROR, (1.0/periods))-1);
  }
 
 private double getYearlyAverageRateOfReturn(
     List<StockTransaction> stockTransactions, String stockSymbol) throws SQLException, SqlRequestException
 {
   
   HistoricalStockInfo stockInfo;
   LinkedList<HistoricalStockInfo> stockData = new LinkedList<HistoricalStockInfo>();
   Calendar today = Calendar.getInstance();
   Calendar date = Calendar.getInstance();
   date.clear();
   date.setTime(initialInvestment.getDateTime());
   while(date.before(today)){
     stockInfo = getNextStockInfo(date, stockSymbol);
     if(stockInfo != null){
       stockData.addFirst(stockInfo);
     }
     date.add(Calendar.YEAR, 1);
   }
   
   return calculateGROR(stockData, stockTransactions, stockSymbol);
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

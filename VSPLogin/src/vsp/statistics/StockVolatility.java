package vsp.statistics;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import vsp.StockInfoServiceProvider;
import vsp.dataObject.HistoricalStockInfo;
import vsp.utils.Enumeration.TimeType;

public class StockVolatility {
  
  private final String stockSymbol;
  private final StockInfoServiceProvider stockService = new StockInfoServiceProvider();
  
  public static void main(String[] args){
    StockVolatility stockVol = new StockVolatility("WMT");
    Calendar date = Calendar.getInstance();
    date.add(Calendar.YEAR, -1);
    System.out.println("Wal-mart Volatility: " + stockVol.getVolatility(date,TimeType.DAY) + " percent");
    System.out.println("Wal-mart Volatility: " + stockVol.getVolatility(date,TimeType.WEEK) + " percent");
    System.out.println("Wal-mart Volatility: " + stockVol.getVolatility(date,TimeType.MONTH) + " percent");
    System.out.println("Wal-mart Volatility: " + stockVol.getVolatility(date,TimeType.YEAR) + " percent");
  }
  
  public StockVolatility(String stock){
    this.stockSymbol = stock;
  }
  
  public double getVolatility(Calendar since,TimeType calendarTimeType){
    List<HistoricalStockInfo> stockData;
    switch (calendarTimeType){
    case DAY:{
      stockData = stockService.requestDailyHistoricalStockData(
                                  stockSymbol, 
                                  since.getTime());
        
        return calculate(stockData);
      }
      case WEEK:{
        stockData = stockService.requestWeeklyHistoricalStockData(
                                  stockSymbol, 
                                  since.getTime());

        return calculate(stockData);
      }
      case MONTH:{
        stockData = stockService.requestMonthlyHistoricalStockData(
                                   stockSymbol,
                                   since.getTime());

        return calculate(stockData);
        
      }
      case YEAR:{
        return getYearlyVolatility(since);
      }
    }
      
   return 0;
  }
  
  
  private double calculate(List<HistoricalStockInfo> stockData){
    
    double mean = 0.0;
    double sum = 0;
    for(HistoricalStockInfo data : stockData){
      sum += data.getAdjustedClose();
    }
    mean = sum/stockData.size();
    sum = 0;
    for(HistoricalStockInfo data: stockData){
      sum += Math.pow((data.getAdjustedClose() - mean), 2);
    }
    return Math.sqrt(sum/stockData.size());
    
  }

  private double getYearlyVolatility(Calendar since){
    
    HistoricalStockInfo stockInfo;
    LinkedList<HistoricalStockInfo> stockData = new LinkedList<HistoricalStockInfo>();
    Calendar today = Calendar.getInstance();
    Calendar date = Calendar.getInstance();
    date.clear();
    date.setTime(since.getTime());
    while(date.before(today)){
      stockInfo = getNextStockInfo(date);
      stockData.addFirst(stockInfo);
      date.add(Calendar.YEAR, 1);
    }
    
    return calculate(stockData);
  }
  private HistoricalStockInfo getNextStockInfo(Calendar date){
    HistoricalStockInfo data = null;
    Calendar requestDate = Calendar.getInstance();
    requestDate.clear();
    requestDate.setTime(date.getTime());
    int attempts = 0;
    while(data == null && requestDate.before(Calendar.getInstance())){
      if(attempts > 15){
        break;
      }
      data = stockService.requestHistoricalStockDataForDay(
          stockSymbol, requestDate.getTime());
      requestDate.add(Calendar.DATE, 1);
      attempts++;
    }
    return data;
  }
}

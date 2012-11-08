package vsp.statistics;

import java.util.ArrayList;
import java.util.List;

import vsp.StockInfoServiceProvider;
import vsp.dataObject.HistoricalStockInfo;

public class StockVolatility {
  
  private final String stockSymbol;
  private final StockInfoServiceProvider stockService = new StockInfoServiceProvider();
  
  public static void main(String[] args){
    StockVolatility stockVol = new StockVolatility("WMT");
    System.out.println("Wal-mart Volatility: " + stockVol.calculate(1) + " percent");
  }
  
  public StockVolatility(String stock){
    this.stockSymbol = stock;
  }
  public double calculate(int timeInMonths){
    
    
    
    double mean = 0.0;
    List<HistoricalStockInfo> stockInfo = stockService.requestHistoricalStockData(
      stockSymbol, timeInMonths);
    
    double sum = 0;
    for(HistoricalStockInfo stock : stockInfo){
      sum += stock.getAdjustedClose();
    }
    mean = sum/stockInfo.size();
    sum = 0;
    for(HistoricalStockInfo stock : stockInfo){
      sum += Math.pow((stock.getAdjustedClose() - mean), 2);
    }
    return Math.sqrt(sum/stockInfo.size());
    
    
  }

}

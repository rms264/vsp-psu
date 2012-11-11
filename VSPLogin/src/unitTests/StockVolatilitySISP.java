package unitTests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import vsp.dataObject.DividendInfo;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.StockInfo;

public class StockVolatilitySISP implements IStockInfo {
  int count = 0;
  private Calendar cal = Calendar.getInstance();
  private SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
  Map<Date,HistoricalStockInfo> dayMap = new HashMap<Date, HistoricalStockInfo>();
  
  public StockVolatilitySISP() {
    
    Calendar cal = Calendar.getInstance();
    cal.clear();
    cal.set(Calendar.YEAR, 2010);
    cal.set(Calendar.MONTH, Calendar.NOVEMBER);
    cal.set(Calendar.DATE, 10);
    HistoricalStockInfo info = new HistoricalStockInfo(cal.getTime(),1.5,1.51,1.45,1.46,64071800,1.46);
    dayMap.put(cal.getTime(), info);
    cal.set(Calendar.YEAR, 2011);
    info = new HistoricalStockInfo(cal.getTime(), 1.68, 1.7, 1.61, 1.69, 61880800, 1.69);
    dayMap.put(cal.getTime(), info);
  }
  @Override
  public boolean isWithinTradingHours() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public StockInfo requestCurrentStockData(String symbol) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<StockInfo> requestCurrentStockData(List<String> symbols) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public HistoricalStockInfo requestHistoricalStockDataForDay(String symbol,
      Date day) {
    
    return dayMap.get(day);
//    HistoricalStockInfo data = new HistoricalStockInfo();
//    if(count%2 == 0)
//      data.setAdjustedClose(1.69);
//    else{
//      data.setAdjustedClose(1.46);
//    }
//    count++;
//    return data;
  }

  @Override
  public List<HistoricalStockInfo> requestDailyHistoricalStockData(
      String symbol, Date since) {
    List<HistoricalStockInfo> data = null;
    try {
      data = loadStockData("SIRI-Daily.info");
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return data;
  }

  @Override
  public List<HistoricalStockInfo> requestWeeklyHistoricalStockData(
      String symbol, Date since) {
    List<HistoricalStockInfo> data = null;
    try {
      data = loadStockData("SIRI-Weekly.info");
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
    return data;
  }

  @Override
  public List<HistoricalStockInfo> requestMonthlyHistoricalStockData(
      String symbol, Date since) {
    List<HistoricalStockInfo> data = null;
    try {
      data = loadStockData("SIRI-Monthly.info");
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
    return data;
  }

  @Override
  public List<DividendInfo> requestHistoricalDividendInfoSince(String symbol,
      Date since) {
    return null;
  }

  private  List<HistoricalStockInfo>  loadStockData(String fileName) throws ParseException{
    BufferedReader br = null;
    List<HistoricalStockInfo> stockData = new ArrayList<HistoricalStockInfo>();
    
    try {
 
      String sCurrentLine;
 
      br = new BufferedReader(new FileReader(fileName));
 
      while ((sCurrentLine = br.readLine()) != null) {
        
        StringTokenizer tokenizer = new StringTokenizer(sCurrentLine, ",");
        List<String> tokens = new ArrayList<String>();
        while(tokenizer.hasMoreTokens()){
          tokens.add(tokenizer.nextToken());
        }
        
        HistoricalStockInfo data = new HistoricalStockInfo();
        cal.setTime(sdf.parse(tokens.get(0)));
        data.setDate(cal.getTime());
        data.setDayLow(Double.parseDouble(tokens.get(1)));
        data.setDayHigh(Double.parseDouble(tokens.get(2)));
        data.setOpen(Double.parseDouble(tokens.get(3)));
        data.setClose(Double.parseDouble(tokens.get(4)));
        data.setAdjustedClose(Double.parseDouble(tokens.get(5)));
        data.setVolume(Integer.parseInt(tokens.get(6)));
        
        
        stockData.add(data);
      }
 
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)br.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return stockData;
 
  }
//  double[] daily = {2.75, 2.67, 2.74, 2.81, 2.83, 2.9, 2.81, 2.8, 2.82,
//                    2.85, 2.89, 2.87, 2.91, 2.92, 2.94, 2.84, 2.84, 2.8,
//                    2.78, 2.73, 2.75, 2.63, 2.67, 2.69, 2.7,  2.73, 2.62,
//                    2.57, 2.59, 2.59, 2.5, 2.48, 2.51, 2.54, 2.57, 2.49, 2.43,
//                    2.39, 2.47, 2.48, 2.43, 2.5, 2.51, 2.53, 2.54, 2.52, 2.54,
//                    2.53, 2.54, 2.54, 2.53, 2.54, 2.54, 2.5, 2.56, 2.57, 2.56,
//                    2.56, 2.59, 2.55, 2.53, 2.51, 2.48, 2.4, 2.48, 2.3,  2.2,
//                    2.16, 2.11, 2.15, 2.16, 2.19, 2.16, 2.11, 2.08, 2.04, 2.08,
//                    2.1, 2.11, 2.11, 2.09, 2.07, 2.05, 2.07, 2.03, 2.05, 2.08,
//                    2.05, 2.09, 2.04, 1.99, 1.85, 1.86, 1.87, 1.84, 1.82, 1.89,
//                    1.88, 1.92, 1.91, 1.84, 1.87, 1.86, 1.83, 1.86, 1.86, 1.86,
//                    1.86, 1.92, 1.89, 1.9,  1.84, 1.89, 1.89, 1.93, 1.93, 1.99,
//                    1.99, 1.96, 1.98, 1.89, 1.83, 1.96, 1.98, 2.02, 2.13, 2.18,
//                    2.18, 2.14, 2.17, 2.16, 2.21, 2.25, 2.23, 2.26, 2.21, 2.2,
//                    2.21, 2.19, 2.14, 2.23, 2.24, 2.27, 2.24, 2.17, 2.23, 2.26,
//                    2.25, 2.22, 2.33, 2.35, 2.29, 2.33, 2.4, 2.31, 2.21, 2.24,
//                    2.25, 2.25, 2.26, 2.27, 2.26, 2.26, 2.27, 2.26, 2.28, 2.29,
//                    2.29, 2.32, 2.35, 2.3, 2.25, 2.22, 2.26, 2.31, 2.23, 2.26,
//                    2.22, 2.23, 2.2, 2.1, 2.09, 2.12, 2.15, 2.15, 2.14, 2.15,
//                    2.15, 2.15, 2.19, 2.19, 2.12, 2.15, 2.15, 2.12, 2.14, 2.08,
//                    2.03, 2.04, 2.08, 2.08, 2.08, 2.1, 2.1, 2.16, 2.16, 2.16,
//                    2.14, 2.11, 2.04, 2.05, 2.05, 2.0, 2.04, 1.83, 1.86, 1.82,
//                    1.82, 1.81, 1.82, 1.81, 1.79, 1.8, 1.83, 1.81, 1.77, 1.78,
//                    1.76, 1.8, 1.77, 1.75, 1.7, 1.78, 1.79, 1.81,1.86, 1.88,1.8,
//                    1.72, 1.77, 1.75, 1.74, 1.87, 1.85, 1.78, 1.74, 1.66, 1.69,
//                    1.68, 1.7, 1.69};
//  
//  double[] weekly = {2.75, 2.9, 2.82, 2.92, 2.78, 2.69, 2.59, 2.54, 2.47, 2.53,
//                     2.53, 2.54, 2.56, 2.48, 2.16, 2.16, 2.1, 2.05, 2.05, 1.85, 
//                     1.89, 1.87, 1.86, 1.84, 1.93, 1.89, 2.13, 2.16, 2.21, 2.23,
//                     2.23, 2.35, 2.31, 2.26, 2.26, 2.35, 2.31, 2.2, 2.15, 2.15,
//                     2.15, 2.04, 2.1, 2.14, 2.0, 1.82, 1.81, 1.77, 1.75, 1.86, 
//                     1.75, 1.78, 1.7};
//  
//  double[] monthly = {2.75, 2.8, 2.59, 2.53, 2.16, 1.85, 1.89, 2.26, 2.31, 2.26, 
//                     2.08, 1.82, 1.8};
  
  double [] yearly = {1.69};
  
}

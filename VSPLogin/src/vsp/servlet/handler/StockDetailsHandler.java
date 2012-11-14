package vsp.servlet.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.dal.requests.Stocks;
import vsp.dal.requests.Transactions;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
import vsp.dataObject.StockTransaction;
import vsp.dataObject.VolatilityInfo;
import vsp.exception.SqlRequestException;
import vsp.statistics.StockVolatility;
import vsp.utils.Enumeration.TimeType;

public class StockDetailsHandler extends BaseServletHandler implements
    ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    String stockSymbol = request.getParameter("symbol");
    String userName = request.getRemoteUser();
    try {
      Stock stock = Stocks.getStock(stockSymbol);
      request.setAttribute("stock", stock);
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    Calendar since = Calendar.getInstance();
    
    if (stockSymbol != null && !stockSymbol.isEmpty())
    {
      StockInfo stockInfo = stockService.requestCurrentStockData(stockSymbol);
      request.setAttribute("stockInfo", stockInfo);
    }
    
    String performance = request.getParameter("perf");
    if(performance!= null && !performance.isEmpty()){
      request.setAttribute("performance", "true");
      StockTransaction initialInvestment;
      try {
        initialInvestment = Transactions.getInitialExecutedTransaction(userName, 
            stockSymbol);
        since.clear();
        since.setTime(initialInvestment.getDateTime());
      } catch (SQLException | SqlRequestException e) {
        //default to a year ago
        since.add(Calendar.YEAR, -2);
      }
      
    }else{
      //show volatility for the last year
      since.add(Calendar.YEAR, -2);
    }
    
    StockVolatility stockVol = new StockVolatility(stockSymbol);    
    VolatilityInfo volData = new VolatilityInfo();
    volData.setDay(stockVol.getVolatility(since,TimeType.DAY));
    volData.setWeek(stockVol.getVolatility(since,TimeType.WEEK));
    volData.setMonth(stockVol.getVolatility(since,TimeType.MONTH));
    volData.setYear(stockVol.getVolatility(since,TimeType.YEAR));
    
    request.setAttribute("volData", volData);
    
    dispatchUrl="StockDetails.jsp";
  }

}

package vsp.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.StockInfoServiceProvider;
import vsp.dataObject.StockInfo;

public class StockInfoHandler extends BaseServletHandler implements
    ServletHandler 
{
  private StockInfoServiceProvider serviceProvider;  
  
  public StockInfoHandler() {
    super();
    serviceProvider = new StockInfoServiceProvider();
  }

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    String stockSymbol = request.getParameter("stockSymbol");
    if (stockSymbol != null && !stockSymbol.isEmpty())
    {
      StockInfo stockInfo = serviceProvider.requestCurrentStockData(stockSymbol);
      if (stockInfo != null)
      {
        request.setAttribute("stockInfo", stockInfo);
      }else{
        request.setAttribute("noData", "true");
      }
    }else{
      request.setAttribute("noData", "true");
    }
    dispatchUrl = "StockInfo.jsp";
  }
//            infoLoaded = true;
//            DecimalFormat df = new DecimalFormat("###,###,##0.0000");
//            DecimalFormat volumeDf = new DecimalFormat();
//            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
//            dfs.setGroupingSeparator(',');
//            volumeDf.setDecimalFormatSymbols(dfs);
//  }

}

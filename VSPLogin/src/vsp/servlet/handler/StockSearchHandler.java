package vsp.servlet.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.StockInfoServiceProvider;
import vsp.dataObject.Stock;

public class StockSearchHandler extends BaseServletHandler implements
    ServletHandler {
  
  private StockInfoServiceProvider service;
  public StockSearchHandler() {
    service = new StockInfoServiceProvider();
  }
  
  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
      
  {
    String queryString = request.getParameter("queryString");
    if(queryString != null && !queryString.trim().isEmpty()){
      List<Stock> stockList = service.searchForStocks(queryString);
      request.setAttribute("stockList", stockList);
      request.setAttribute("queryString", queryString);
    }
    dispatchUrl="StockSearch.jsp";
  }
}

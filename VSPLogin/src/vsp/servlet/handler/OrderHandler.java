package vsp.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.StockInfoServiceProvider;

public class OrderHandler extends BaseServletHandler implements ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    StockInfoServiceProvider.ForceWithinHours = true;
    dispatchUrl = "Order.jsp";
  }

}

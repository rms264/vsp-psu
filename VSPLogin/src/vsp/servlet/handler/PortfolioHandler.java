package vsp.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PortfolioHandler extends BaseServletHandler implements
    ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    dispatchUrl="Portfolio.jsp";
  }

}

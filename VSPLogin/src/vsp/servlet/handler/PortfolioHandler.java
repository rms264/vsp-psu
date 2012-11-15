package vsp.servlet.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PortfolioHandler extends BaseServletHandler implements
    ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    if(request.isUserInRole("admin")){
      // throws on error
      List<String> traders;
      try {
        traders = vsp.getTraders();
        if (traders.size() > 0){
          request.setAttribute("traders", traders);
        }
        dispatchUrl="/admin/Admin.jsp";
      } catch (SQLException e) {
        errors.add("Unable to pull traders from the database. " + e.getMessage());
        request.setAttribute("errors", errors);
        dispatchUrl="Error.jsp";
      }
      
    }else{
      dispatchUrl="Portfolio.jsp";
    }
  }
}

package vsp.servlet.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminHandler extends BaseServletHandler implements ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
      
  {
    String delete = request.getParameter("delete");
    if (delete != null && !delete.isEmpty())
    {
      try
      {
        String deleteAccount = request.getParameter("delete");
        // throws on error
        vsp.deleteTraderAccount(delete);
        request.setAttribute("deleteSuccess", "User: " + deleteAccount + " Deleted");
        List<String> traders = vsp.getTraders();
        if (traders.size() > 0)
          request.setAttribute("traders", traders);
        dispatchUrl="/admin/Admin.jsp";
      }
      catch (Exception ex)
      {
        errors.add(">Unable to delete user. " + ex.getMessage());
        request.setAttribute("errors", errors);
        dispatchUrl="Error.jsp";
      }
    }
  }
}

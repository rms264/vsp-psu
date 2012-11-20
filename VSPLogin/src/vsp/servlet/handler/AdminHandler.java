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
    String delete_user = request.getParameter("delete");    
    if (delete_user != null && !delete_user.isEmpty())
    {
      try
      {
        // throws on error
        vsp.deleteTraderAccount(delete_user);
        request.setAttribute("deleteSuccess", "User: " + delete_user + " Deleted");
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
    else{
    	String password_user = request.getParameter("password");
    	if(password_user != null && !password_user.isEmpty()){
    		request.getSession().setAttribute("password_user", password_user);
    		dispatchUrl="ResetUserPassword.jsp";
    	}
    }
  }
}

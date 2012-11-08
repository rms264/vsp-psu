package vsp.servlet.handler;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.dataObject.AccountData;

public class UserInfoHandler extends BaseServletHandler implements
    ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    try {
      String userName = request.getRemoteUser();
      AccountData userAccount = vsp.getAccountInfo(userName);
      logger.info(userAccount.toString());
      request.setAttribute("userAccount", userAccount);
    } catch (SQLException e) {
      errors.add("Failed to retrieve user info. " + e.getMessage());
      request.setAttribute("errors", errors);
    }
    dispatchUrl="UserInfo.jsp";
  }
}

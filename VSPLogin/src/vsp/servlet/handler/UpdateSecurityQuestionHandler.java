package vsp.servlet.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateSecurityQuestionHandler extends BaseServletHandler implements ServletHandler{

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
   {
      //no action class, there is nothing to do
      dispatchUrl = "/UpdateSecurityQuestion.jsp";
   }
}

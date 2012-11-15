package vsp.servlet.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.form.validator.FormValidator;
import vsp.form.validator.FormValidatorFactory;
import vsp.servlet.form.UpdatePasswordForm;

public class SubmitPasswordUpdateHandler extends BaseServletHandler implements
    ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    try{
      UpdatePasswordForm passwordForm = new UpdatePasswordForm();
      String uri = request.getRequestURI();
      int lastIndex = uri.lastIndexOf("/");
      String action = uri.substring(lastIndex + 1); 
      
      String userName = request.getRemoteUser();
      if(userName == null || userName.isEmpty()){
        userName = (String) request.getSession().getAttribute("userName");
      }
      if(userName != null && !userName.isEmpty())
      {
        passwordForm.setUserName(userName);
        passwordForm.setPassword(request.getParameter("password"));
        passwordForm.setVerifyPassword(request.getParameter("verifyPassword"));
        
        FormValidator passwordValidator = FormValidatorFactory.getUpdatePasswordValidator();
        List<String> errors = passwordValidator.validate(passwordForm);
        if(errors.isEmpty()){
          try {
            vsp.updateUserPassword(passwordForm.getUserName(), passwordForm.getPassword(), passwordForm.getVerifyPassword());
            request.setAttribute("passwordUpdate", "Password has been successfully changed");
            if(action.equals("submitResetPassword")){
              dispatchUrl = "login";
            }
            else if(action.equals("submitUpdatePassword")){
              dispatchUrl = "updatePassword";
            }
          } catch (SQLException | SqlRequestException | ValidationException e) {
            errors.add(e.getMessage());
            request.setAttribute("errors", errors);
            if(action.equals("submitResetPassword")){
              dispatchUrl="Error.jsp";
            }
            else if(action.equals("submitUpdatePassword")){
              dispatchUrl = "updatePassword";
            }
          }
        }else{
          request.setAttribute("errors", errors);
          if(action.equals("submitResetPassword")){
            dispatchUrl="Error.jsp";
          }
          else if(action.equals("submitUpdatePassword")){
            dispatchUrl = "updatePassword";
          }
        }
      }else{
        errors.add("Unknown user name");
        if(action.equals("submitResetPassword")){
          dispatchUrl="Error.jsp";
        }
        else if(action.equals("submitUpdatePassword")){
          dispatchUrl = "updatePassword";
        }
      }
    }
    finally{
      request.getSession().removeAttribute("userName");
    }
  }
}

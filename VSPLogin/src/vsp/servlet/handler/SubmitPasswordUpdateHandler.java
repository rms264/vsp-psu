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
    UpdatePasswordForm passwordForm = new UpdatePasswordForm();
    passwordForm.setUserName(request.getParameter("userName"));
    passwordForm.setPassword(request.getParameter("password"));
    passwordForm.setVerifyPassword(request.getParameter("verifyPassword"));
    
    FormValidator passwordValidator = FormValidatorFactory.getUpdatePasswordValidator();
    List<String> errors = passwordValidator.validate(passwordForm);
    if(errors.isEmpty()){
      try {
        vsp.updateUserPassword(passwordForm.getUserName(), passwordForm.getPassword(), passwordForm.getVerifyPassword());
        request.setAttribute("passwordUpdate", "Password has been successfully changed");
        dispatchUrl = "login";
      } catch (SQLException | SqlRequestException | ValidationException e) {
        errors.add(e.getMessage());
        request.setAttribute("errors", errors);
        dispatchUrl="Error.jsp";
      }
    }else{
      request.setAttribute("errors", errors);
      dispatchUrl="Error.jsp";
    }
  }

}

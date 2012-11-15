package vsp.servlet.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.form.validator.FormValidator;
import vsp.form.validator.FormValidatorFactory;
import vsp.servlet.form.UserNameForm;

public class SubmitUserNameHandler extends BaseServletHandler implements
    ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    UserNameForm formData = new UserNameForm(request.getParameter("userName"));
    FormValidator validator = FormValidatorFactory.getUserNameValidator();
    List<String> errors = validator.validate(formData);
    if(errors.isEmpty()){
      //no errors found in the form
      try {
        AccountData userAccount = Users.requestAccountData(formData.getUserName());
        String question = userAccount.getSecurityQuestion().toString();
        request.setAttribute("question", question);
        request.getSession().setAttribute("userName", userAccount.getUserName());
        dispatchUrl="EnterSecurityQuestion.jsp";
      } catch (SQLException e) {
        errors.add("Failed to retrieve user: " + formData.getUserName() +". " + e.getMessage());
        request.setAttribute("errors", errors);
        dispatchUrl="Error.jsp";
      }
    }else{
      request.setAttribute("errors", errors);
      dispatchUrl="Error.jsp";
      request.getSession().removeAttribute("userName");
    }
  }

}

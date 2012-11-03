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
import vsp.servlet.form.SecurityAnswerForm;
import vsp.utils.VSPUtils;

public class SubmitSecurityAnswerHandler extends BaseServletHandler implements
    ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    SecurityAnswerForm answerForm = new SecurityAnswerForm();
    answerForm.setAnswer(request.getParameter("answer"));
    FormValidator validator = FormValidatorFactory.getSecurityAnswerValidator();
    List<String> errors = validator.validate(answerForm);
    try {
      if(errors.isEmpty()){
        AccountData userAccount = Users.requestAccountData(request.getParameter("userName"));
        if(userAccount.getAnswerHash().equals(VSPUtils.hashString(answerForm.getAnswer()))){
          //the answer is correct
          request.setAttribute("correctAnswer", "true");
          request.setAttribute("userAccount", userAccount);
          dispatchUrl = "ResetUserPassword.jsp";
        }else{
          errors.add("Security Answer was incorrect");
          request.setAttribute("errors", errors);
          dispatchUrl = "Error.jsp";
        }
      }else{
        request.setAttribute("errors", errors);
        dispatchUrl = "Error.jsp";
      }
      
    } catch (SQLException e) {
      errors.add("Failed to check security answer. " + e.getMessage());
      request.setAttribute("errors", errors);
      dispatchUrl = "Error.jsp";
    }
  }
}

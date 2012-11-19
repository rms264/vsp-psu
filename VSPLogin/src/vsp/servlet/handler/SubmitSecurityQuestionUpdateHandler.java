package vsp.servlet.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;
import vsp.utils.Enumeration.SecurityQuestion;

public class SubmitSecurityQuestionUpdateHandler extends BaseServletHandler implements
    ServletHandler {

  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    try{
      String userName = request.getRemoteUser();
      if(userName == null || userName.isEmpty()){
        userName = (String) request.getSession().getAttribute("userName");
      }
      if(userName != null && !userName.isEmpty())
      {            	
    	String questionNum = request.getParameter("question");
    	String answer = request.getParameter("answer");
    	
    	List<String> errors = new ArrayList<String>();
    	SecurityQuestion question = SecurityQuestion.convert(Integer.parseInt(questionNum));
    	try {
			if(Validate.validateSecurityQuestion(question) && 
			   Validate.validateSecurityAnswer(answer)){				
				vsp.updateUserSecurityQuestion(userName, questionNum, answer);    							    			     
				request.setAttribute("securityUpdate", "Security Question has been successfully changed");
				dispatchUrl = "UpdateSecurityQuestion.jsp";					
			}
			else{
				errors.add("Please select appropriate question and enter valid answer.");
				request.setAttribute("errors", errors);
				dispatchUrl = "UpdateSecurityQuestion.jsp";
			}
		} catch (SQLException | SqlRequestException | ValidationException e) {
	        errors.add(e.getMessage());
	        request.setAttribute("errors", errors);
	        dispatchUrl = "UpdateSecurityQuestion.jsp";
		}  		    	    	         
      }else{
        errors.add("Unknown user name");
        request.setAttribute("errors", errors);
        dispatchUrl = "UpdateSecurityQuestion.jsp";        
      }
    }
    finally{
      request.getSession().removeAttribute("userName");
    }
  }
}

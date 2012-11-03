package vsp.form.validator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vsp.exception.ValidationException;
import vsp.servlet.form.JspForm;
import vsp.servlet.form.RegisterForm;
import vsp.utils.Validate;
import vsp.utils.Enumeration.SecurityQuestion;

public class RegistrationValidator implements FormValidator{
  
  protected RegistrationValidator(){}
  
  public List<String> validate(JspForm formData){
    RegisterForm regData;
    if(formData instanceof RegisterForm){
      regData = (RegisterForm) formData;
    }
    else{
      throw new ClassCastException("Expecting RegisterForm");
    }
    
    List<String> errors = new ArrayList<String>();
    errors.addAll(validateUserName(regData));
    errors.addAll(validateEmail(regData));
    errors.addAll(validatePassword(regData));
    errors.addAll(validateSecurityQuestion(regData));
    errors.addAll(validateAnswer(regData));
    
    return errors;
  }

  private List<String> validateAnswer(RegisterForm formData) {
    List<String> errors = new ArrayList<String>();
    String answer = formData.getAnswer();
    if(answer == null || answer.trim().isEmpty()){
      errors.add("Must answer security question.");
      return errors;
    }    
    try {
      Validate.validateSecurityAnswer(answer);
    } catch (ValidationException e) {
      errors.add(e.getMessage());
    }
    return errors;
  }

  private List<String> validateSecurityQuestion(RegisterForm formData) {
    List<String> errors = new ArrayList<String>();
    try{
      SecurityQuestion question = SecurityQuestion.convert(Integer.parseInt(formData.getQuestion()));
      Validate.validateSecurityQuestion(question);
    }catch (NumberFormatException e){
      errors.add("Invalid Security Question Selected.");
    } catch (ValidationException e) {
      errors.add(e.getMessage());
    }
    return errors;
  }

  private List<String> validatePassword(RegisterForm formData) {
    List<String> errors = new ArrayList<String>();
    String password = formData.getPassword();
    String verifyPassword = formData.getVerifyPassword();
    
    if(password == null || password.trim().isEmpty()){
      errors.add("Password must not be empty.");
      return errors;
    }
    if(verifyPassword == null || verifyPassword.trim().isEmpty()){
      errors.add("Password verification must not be empty.");
      return errors;
    }
    
    try {
      Validate.validatePassword(formData.getUserName(), password, verifyPassword);
    } catch (ValidationException e) {
      errors.add(e.getMessage());
    }
    return errors;
  }

  private List<String> validateEmail(RegisterForm formData) {
    List<String> errors = new ArrayList<String>();
    String email = formData.getEmail();
    if(email == null || email.trim().isEmpty()){
      errors.add("Email Address must not be empty.");
      return errors;
    }
    try {
      if(Validate.emailExistsInDb(email)){
        errors.add("Email Adress already in use.");
      }
    } catch (SQLException | ValidationException e) {
      errors.add(e.getMessage());
    }
    
    return errors;
  }

  private List<String> validateUserName(RegisterForm formData) {
    List<String> errors = new ArrayList<String>();
    String userName = formData.getUserName();
    if(userName == null || userName.trim().isEmpty()){
      errors.add("User name must not be empty.");
      return errors;
    }
    try {
      if(Validate.userNameExistsInDb(userName)){
        errors.add("User name already in use");
      }
    } catch (SQLException | ValidationException e) {
        errors.add(e.getMessage());
    }
    return errors;
  }
}

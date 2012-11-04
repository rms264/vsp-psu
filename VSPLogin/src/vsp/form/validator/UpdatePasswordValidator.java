package vsp.form.validator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vsp.exception.ValidationException;
import vsp.servlet.form.JspForm;
import vsp.servlet.form.UpdatePasswordForm;
import vsp.utils.Validate;

public class UpdatePasswordValidator implements FormValidator{

  @Override
  public List<String> validate(JspForm formData) {
    UpdatePasswordForm passwordData;
    if(formData instanceof UpdatePasswordForm){
        passwordData = (UpdatePasswordForm) formData;
    }
    else{
      throw new ClassCastException("Expecting UpdatePasswordForm");
    }
    
    List<String> errors = new ArrayList<String>();
    errors.addAll(validateUserName(passwordData));
    errors.addAll(validatePassword(passwordData));
    
    return errors;
  }

  private  List<String> validatePassword(UpdatePasswordForm passwordData) {
    List<String> errors = new ArrayList<String>();
    try {
      Validate.validatePassword(passwordData.getUserName(), 
                                passwordData.getPassword(), 
                                passwordData.getVerifyPassword());
    } catch (ValidationException e) {
      errors.add(e.getMessage());
    } 
    return errors;
  }

  private List<String> validateUserName(UpdatePasswordForm passwordData) {
    List<String> errors = new ArrayList<String>();
    String userName = passwordData.getUserName();
    if(userName == null || userName.trim().isEmpty()){
      errors.add("User name must not be empty.");
      return errors;
    }
    try {
      if(!Validate.userNameExistsInDb(userName)){
        errors.add("Invalid user name");
      }
    } catch (SQLException | ValidationException e) {
        errors.add(e.getMessage());
    }
      return errors;
  }
}

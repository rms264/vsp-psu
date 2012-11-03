package vsp.form.validator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vsp.exception.ValidationException;
import vsp.servlet.form.UserNameForm;
import vsp.servlet.form.JspForm;
import vsp.utils.Validate;

public class UserNameValidator implements FormValidator{
  protected UserNameValidator(){}
  
  @Override
  public List<String> validate(JspForm formData){
    UserNameForm userNameData;
    if(formData instanceof UserNameForm){
        userNameData = (UserNameForm) formData;
    }
    else{
      throw new ClassCastException("Expecting UserNameForm");
    }
    
    List<String> errors = new ArrayList<String>();
    errors.addAll(validateUserName(userNameData));
    
    return errors;
  }
  private List<String> validateUserName(UserNameForm formData) {
    List<String> errors = new ArrayList<String>();
    String userName = formData.getUserName();
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

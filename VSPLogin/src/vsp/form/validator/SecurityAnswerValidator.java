package vsp.form.validator;

import java.util.ArrayList;
import java.util.List;

import vsp.servlet.form.JspForm;
import vsp.servlet.form.SecurityAnswerForm;

public class SecurityAnswerValidator implements FormValidator {

  @Override
  public List<String> validate(JspForm formData) {
    SecurityAnswerForm answerData;
    if(formData instanceof SecurityAnswerForm){
      answerData = (SecurityAnswerForm) formData;
    }
    else{
      throw new ClassCastException("Expecting SecurityAnswerForm");
    }
    List<String> errors = new ArrayList<String>();
    errors.addAll(validateAnswer(answerData));
    
    return errors;
  }
  
  private List<String> validateAnswer(SecurityAnswerForm answerData){
    List<String> errors = new ArrayList<String>();
    
    String answer = answerData.getAnswer();
    if(answer == null || answer.trim().isEmpty()){
      errors.add("Security Question must be answered.");
    }
    return errors;
  }

}

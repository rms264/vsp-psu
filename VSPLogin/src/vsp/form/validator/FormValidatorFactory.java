package vsp.form.validator;

public class FormValidatorFactory {
  private static UserNameValidator userNameValidator;
  private static RegistrationValidator regValidator;
  private static SecurityAnswerValidator answerValidator;
  private static UpdatePasswordValidator passwordValidator;
  
  public static FormValidator getUserNameValidator(){
    if(userNameValidator == null){
      userNameValidator = new UserNameValidator();
    }
    return userNameValidator;
  }
  public static FormValidator getRegistrationValidator(){
    if(regValidator == null){
      regValidator = new RegistrationValidator();
    }
    return regValidator;
  }
  
  public static FormValidator getSecurityAnswerValidator(){
    if(answerValidator == null){
      answerValidator = new SecurityAnswerValidator();
    }
    return answerValidator;
  }
  
  public static FormValidator getUpdatePasswordValidator(){
    if(passwordValidator == null){
      passwordValidator = new UpdatePasswordValidator();
    }
    return passwordValidator;
  }
}

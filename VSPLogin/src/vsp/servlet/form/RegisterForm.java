package vsp.servlet.form;

import java.io.Serializable;

public class RegisterForm implements Serializable, JspForm {

  private static final long serialVersionUID = 4662039961328427202L;
  private String userName;
  private String password;
  private String verifyPassword;
  private String email;
  private String question;
  private String answer;

  public RegisterForm() {}
  
  public RegisterForm(String userName, String password, String verifyPassword,
      String email, String question, String answer) {
    this.userName = userName;
    this.password = password;
    this.verifyPassword = verifyPassword;
    this.email = email;
    this.question = question;
    this.answer = answer;
  }
 
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getVerifyPassword() {
    return verifyPassword;
  }
  public void setVerifyPassword(String verifyPassword) {
    this.verifyPassword = verifyPassword;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getQuestion() {
    return question;
  }
  public void setQuestion(String question) {
    this.question = question;
  }
  public String getAnswer() {
    return answer;
  }
  public void setAnswer(String answer) {
    this.answer = answer;
  }

  

}

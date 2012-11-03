package vsp.servlet.form;

import java.io.Serializable;

public class UpdatePasswordForm implements Serializable, JspForm {

  private static final long serialVersionUID = -4874693703017257260L;
  private String userName;
  private String password;
  private String verifyPassword;
  
  public UpdatePasswordForm() {}
  public UpdatePasswordForm(String userName, String password, String verifyPassword) {
    this.userName = userName;
    this.password = password;
    this.verifyPassword = verifyPassword;
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
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
}

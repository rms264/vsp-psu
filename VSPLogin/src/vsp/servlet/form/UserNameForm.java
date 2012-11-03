package vsp.servlet.form;

import java.io.Serializable;

public class UserNameForm  implements Serializable, JspForm{
  private static final long serialVersionUID = -3219420011621180065L;

  public UserNameForm(){}
  public UserNameForm(String userName) {
    this.userName = userName;
  }
  private String userName;

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }

}

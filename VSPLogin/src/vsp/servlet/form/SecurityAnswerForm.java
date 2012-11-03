package vsp.servlet.form;

import java.io.Serializable;

public class SecurityAnswerForm implements Serializable, JspForm {
  private static final long serialVersionUID = 2341887731286908209L;
  private String answer;
  
  public SecurityAnswerForm() {}

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public SecurityAnswerForm(String answer) {
    super();
    this.answer = answer;
  }
  public String getAnswer() {
    return answer;
  }
}

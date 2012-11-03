package vsp.form.validator;

import java.util.List;

import vsp.servlet.form.JspForm;

public interface FormValidator {
  public List<String> validate(JspForm formData);

}

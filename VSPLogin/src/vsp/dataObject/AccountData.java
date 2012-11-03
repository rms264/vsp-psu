package vsp.dataObject;

import java.io.Serializable;
import java.util.*;

import vsp.dal.requests.Users;
import vsp.servlet.form.RegisterForm;
import vsp.utils.VSPUtils;
import vsp.utils.Enumeration.SecurityQuestion;

public final class AccountData implements Serializable
{
  private static final long serialVersionUID = 4552953540397428117L;
  private final String userName;
	private final String email;
	private final String passwordHash;
	private final String answerHash;
	private final Date signup;
	private final SecurityQuestion securityQuestion;
	private final double balance;
	private final Date lastDividendCheck;
  public String getUserName() {
    return userName;
  }
  
  public AccountData(String userName, String email, String passwordHash,
      String answerHash, Date signup, SecurityQuestion securityQuestion,
      double balance, Date lastDividendCheck) {
    super();
    this.userName = userName;
    this.email = email;
    this.passwordHash = passwordHash;
    this.answerHash = answerHash;
    this.signup = signup;
    this.securityQuestion = securityQuestion;
    this.balance = balance;
    this.lastDividendCheck = lastDividendCheck;
  }
  
  public AccountData(RegisterForm formData){
    this.userName = formData.getUserName();
    this.email = formData.getEmail();
    this.passwordHash = VSPUtils.hashString(formData.getPassword());
    this.answerHash = VSPUtils.hashString(formData.getAnswer());
    this.signup = Calendar.getInstance().getTime();
    this.lastDividendCheck = Calendar.getInstance().getTime();
    this.securityQuestion = SecurityQuestion.convert(Integer.parseInt(formData.getQuestion()));
    this.balance = Users.DEFAULT_BALANCE;
  }
  
  public String getEmail() {
    return email;
  }
  public String getPasswordHash() {
    return passwordHash;
  }
  public String getAnswerHash() {
    return answerHash;
  }
  public Date getSignup() {
    return signup;
  }
  public SecurityQuestion getSecurityQuestion() {
    return securityQuestion;
  }
  public double getBalance() {
    return balance;
  }
  public Date getLastDividendCheck() {
    return lastDividendCheck;
  }
	
//	public AccountData(String userName, String email, Date signup, 
//			SecurityQuestion securityQuestion, double balance, 
//			Date lastDividendCheck, String pass)
//	{
//		this.userName = userName;
//		this.email = email;
//		this.signup = signup;
//		this.securityQuestion = securityQuestion;
//		this.balance = balance;
//		this.lastDividendCheck = lastDividendCheck;
//	}
	
//	public double getBalance()
//	{
//		return this.balance;
//	}
//	
//	public String getEmail()
//	{
//		return this.email;
//	}
//	
//	public Date getLastDividendCheck()
//	{
//		return this.lastDividendCheck;
//	}
//	
//	public Date getSignup()
//	{
//		return this.signup;
//	}
//	
//	public SecurityQuestion getSecurityQuestion()
//	{
//		return this.securityQuestion;
//	}
//	
//	public String getUserName()
//	{
//		return this.userName;
//	}
}

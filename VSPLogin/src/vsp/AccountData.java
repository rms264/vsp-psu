package vsp;

import java.util.*;

public final class AccountData
{
	private String userName;
	private String email;
	private Date signup;
	private String securityQuestion;
	
	public AccountData(String userName, String email, Date signup, String securityQuestion)
	{
		this.userName = userName;
		this.email = email;
		this.signup = signup;
		this.securityQuestion = securityQuestion;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public Date getSignup()
	{
		return this.signup;
	}
	
	public String getSecurityQuestion()
	{
		return this.securityQuestion;
	}
	
	public String getUserName()
	{
		return this.userName;
	}
}

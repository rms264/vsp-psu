package vsp.dataObject;

import java.util.*;

import vsp.utils.Enumeration.SecurityQuestion;

public final class AccountData
{
	private final String userName;
	private final String email;
	private final Date signup;
	private final SecurityQuestion securityQuestion;
	
	public AccountData(String userName, String email, Date signup, 
			SecurityQuestion securityQuestion)
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
	
	public SecurityQuestion getSecurityQuestion()
	{
		return this.securityQuestion;
	}
	
	public String getUserName()
	{
		return this.userName;
	}
}

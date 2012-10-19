package vsp.dataObject;

import java.util.*;

import vsp.utils.Enumeration.SecurityQuestion;

public final class AccountData
{
	private final String userName;
	private final String email;
	private final Date signup;
	private final SecurityQuestion securityQuestion;
	private final double balance;
	private final Date lastDividendCheck;
	
	public AccountData(String userName, String email, Date signup, 
			SecurityQuestion securityQuestion, double balance, 
			Date lastDividendCheck)
	{
		this.userName = userName;
		this.email = email;
		this.signup = signup;
		this.securityQuestion = securityQuestion;
		this.balance = balance;
		this.lastDividendCheck = lastDividendCheck;
	}
	
	public double getBalance()
	{
		return this.balance;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public Date getLastDividendCheck()
	{
		return this.lastDividendCheck;
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

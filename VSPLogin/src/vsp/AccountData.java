package vsp;

import java.io.Serializable;
import java.util.*;

public final class AccountData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7335917300787737210L;
	private String email;
	private Date signup;
	
	public AccountData(String email, Date signup)
	{
		this.email = email;
		this.signup = signup;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public Date getSignup()
	{
		return this.signup;
	}
}

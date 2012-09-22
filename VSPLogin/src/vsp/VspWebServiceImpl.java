package vsp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.*;
import java.sql.*;
import javax.sql.*;
import javax.jws.*;

public class VspWebServiceImpl
{
	public AccountData getAccountInfo(String userName)
	{
		AccountData data = new AccountData("[unavailable]", new Date());
		
		String dbUrl = "jdbc:mysql://localhost:3306/vsp";
		String dbClass = "com.mysql.jdbc.Driver";
		String query = "SELECT email, signup FROM users WHERE user_name='" + userName + "'";

		try
		{
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection (dbUrl, "tomcat", "tomcat");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			rs.first();
			String email = rs.getString("email");
			Date signup = rs.getDate("signup");
			
			data = new AccountData(email, signup);

			con.close();
		} //end try
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return data;
	}
	
	public String createAccount(String userName, String password1, String password2, String email)
	{
		String status = validateUserName(userName);
		if (status.isEmpty())
		{
			String dbUrl = "jdbc:mysql://localhost:3306/vsp";
			String dbClass = "com.mysql.jdbc.Driver";
			String query = "SELECT * FROM users WHERE user_name='" + userName + "'";
			try
			{
				Class.forName(dbClass);
				Connection con = DriverManager.getConnection (dbUrl, "tomcat", "tomcat");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
						
				if(!rs.first())
				{ // user does not exist
					status = validatePassword(password1, password2);
					if (status.isEmpty())
					{
						status = validateEmail(email);
						if (status.isEmpty())
						{
							try
							{
								// hash password
								String pwHash = hashPassword(password1);
				
							    // get today's date
							    java.util.Date today = new java.util.Date();
				
							    // insert user into database
								
							    PreparedStatement pStmt = con.prepareStatement("insert into users values(?,?,?,?)");  
							    pStmt.setString(1, userName);  
							    pStmt.setString(2, pwHash);   
							    pStmt.setString(3, email);
							    pStmt.setDate(4, new java.sql.Date(today.getTime()));
							    int result = pStmt.executeUpdate(); 						    
								if (result != 1)
								{
									status = "Error:  Unable to create account.";
								}
							}
							catch(Exception e)
							{
								e.printStackTrace();
								status = "Error:  Unable to create account.";
							}
						}
					}
				}
				else
				{ // user exists
					status = "Error:  User name is already in use.  Please select another.";
				}
	
				con.close();
			} //end try
			catch(Exception e)
			{
				e.printStackTrace();
				status = "Error:  Unable to create account.";
			}
		}
		
		return status;
	}
	
	private String hashPassword(String password)
	{
		StringBuffer pwHash = new StringBuffer();
		try
		{
			MessageDigest sha256 = MessageDigest.getInstance("sha-256");
			byte[] hash = sha256.digest(password.getBytes());
		    for(byte b : hash)
		    {
		    	pwHash.append(String.format("%02x", b));
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	    
	    return pwHash.toString();
	}
	
	private String validateEmail(String email)
	{
		String status = "";
		// TODO: validate email address
		
		return status;
	}
	
	private String validatePassword(String password1, String password2)
	{
		String status = "";
		if (password1.equals(password2))
		{
			if (!password1.isEmpty())
			{
				// TODO: password validation
			}
			else
			{
				status = "Error:  Blank passwords are prohibited.";
			}
		}
		else
		{
			status = "Error:  The passwords do not match.";
		}
		
		return status;
	}
	
	private String validateUserName(String userName)
	{
		String status = "";
		// TODO: username validation
		// note that checking for duplicates is done later and does not need to be done here
		
		return status;
	}
}

package vsp;

import java.text.*;
import java.util.Date;
import java.util.regex.*;
import java.security.*;
import java.sql.*;
import javax.sql.*;
import javax.jws.*;

public class VspServiceProvider
{
	private static final String DB_URL = "jdbc:mysql://localhost:3306/vsp";
	private static final String DB_CLASS = "com.mysql.jdbc.Driver";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String LOWER_PATTERN = "[a-z]";
	private static final String UPPER_PATTERN = "[A-Z]";
	private static final String NUM_PATTERN = "[0-9]";
	
	public VspServiceProvider()
	{
		
	}
	
	private Connection CreateConnection() throws SQLException, ClassNotFoundException
	{
		Class.forName(DB_CLASS);
		return DriverManager.getConnection (DB_URL, "tomcat", "tomcat");
	}
	
	public void createAccount(String userName, String password1, String password2, String email, int question, String answer) throws Exception
	{
		try
		{
			Connection connection = CreateConnection();
			try
			{
				// all of these throw
				validateUserName(userName, connection);
				validateEmail(email, connection);
				validatePassword(userName, password1, password2);
				validateSecurityQuestion(question);
				validateSecurityAnswer(answer);
	
				// hash password & security answer
				String passwordHash = hashString(password1);
				String answerHash = hashString(answer);
	
			    // get today's date
			    java.util.Date today = new java.util.Date();
	
			    // insert user into database
			    int result = 0;
			    try
			    {
			    	// insert into users table
				    PreparedStatement pStmt = connection.prepareStatement("insert into users values(?,?,?,?,?,?)");  
				    pStmt.setString(1, userName);  
				    pStmt.setString(2, passwordHash);   
				    pStmt.setString(3, email);
				    pStmt.setDate(4, new java.sql.Date(today.getTime()));
				    pStmt.setInt(5, question);
				    pStmt.setString(6, answerHash);
				    result = pStmt.executeUpdate();
			    }
			    catch (Exception e)
			    {
					e.printStackTrace();
					throw (new Exception("Error:  Unable to create account."));
			    }
			    
				if (result != 1)
				{
					throw (new Exception("Error:  Unable to create account."));
				}
					
				try
				{
					// insert user role into user_roles table
					PreparedStatement pStmt = connection.prepareStatement("insert into user_roles values(?,?)");
					pStmt.setString(1, userName);  
				    pStmt.setString(2, "trader");
				    result = pStmt.executeUpdate(); 						    
				}
				catch(Exception e)
				{
					e.printStackTrace();
					throw (new Exception("Error:  Unable to create account."));
				}
			    
				if (result != 1)
				{
					throw (new Exception("Error:  Unable to create account permissions."));
				}
			}
			finally
			{
				connection.close();
				connection = null;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			throw (new Exception("Error:  Unable to create account."));
		}
	}
	
	public void deleteAccount(String userName) throws Exception
	{
		try
		{
			Connection connection = CreateConnection();
			try
			{
				validateUserName(userName, connection);
				throw (new Exception("Error:  Account does not exist or is invalid."));
			}
			catch (Exception e)
			{
				String errorMsg = e.toString();
				if (!errorMsg.contains("already in use"))
				{
					throw (new Exception("Error:  Account does not exist or is invalid."));
				}
			}
			
			int result = 0;
			try
			{
				// delete row from users table
				PreparedStatement pStmt = connection.prepareStatement("DELETE from users WHERE user_name=?");
				pStmt.setString(1, userName);  
			    result = pStmt.executeUpdate(); 						    
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw (new Exception("Error:  Unable to delete account."));
			}
		    
			if (result != 1)
			{
				throw (new Exception("Error:  Unable to delete account."));
			}
			
			try
			{
				// delete row from user_roles table
				PreparedStatement pStmt = connection.prepareStatement("DELETE from user_roles WHERE user_name=?");
				pStmt.setString(1, userName);  
			    result = pStmt.executeUpdate(); 						    
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw (new Exception("Error:  Unable to delete account."));
			}
		    
			if (result != 1)
			{
				throw (new Exception("Error:  Unable to delete account."));
			}			
		}
		catch (SQLException e)
		{
			throw (new Exception("Error:  Unable to delete account."));
		}
	}
	
	public AccountData getAccountInfo(String userName) throws Exception
	{
		AccountData data = null;
		
		String query = "SELECT email, signup, securityQuestion FROM users WHERE user_name='" + userName + "'";

		try
		{
			Connection con = CreateConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			rs.first();
			String email = rs.getString("email");
			Date signup = rs.getDate("signup");
			int securityQuestion = rs.getInt("securityQuestion");
			
			data = new AccountData(userName, email, signup, securityQuestion);

			con.close();
		} //end try
		catch(Exception e)
		{
			e.printStackTrace();
			throw (new Exception("Error:  Unable to get account information."));
		}
		
		return data;
	}
	
	private String hashString(String password)
	{
		StringBuffer sbHash = new StringBuffer();
		try
		{
			MessageDigest sha256 = MessageDigest.getInstance("sha-256");
			byte[] hash = sha256.digest(password.getBytes());
		    for(byte b : hash)
		    {
		    	sbHash.append(String.format("%02x", b));
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	    
	    return sbHash.toString();
	}
	
	private void validateEmail(String email, Connection connection) throws Exception
	{
		// empty email addresses are not valid
		if (email == null || email.isEmpty())
		{
			throw (new Exception("Error:  Please enter your email address."));
		}
		
		// check for existence of user name in database
		PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM users WHERE email=?");
		pStmt.setString(1, email);  
		ResultSet rs = pStmt.executeQuery(); 
		if(rs.first())
		{ // user exists
			throw (new Exception("Error:  The email address is already in use.  Please enter another one."));
		}
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		if(!matcher.matches())
		{
			throw (new Exception("Error:  The email address is invalid.  Please enter a valid email address."));
		}
	}
	
	private void validateUserName(String userName, Connection connection) throws Exception
	{
		// empty user names are not valid
		if (userName == null || userName.isEmpty())
		{
			throw (new Exception("Error:  Please enter a user name."));
		}
		
		// check for existence of user name in database
		PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM users WHERE user_name=?");
		pStmt.setString(1, userName);  
		ResultSet rs = pStmt.executeQuery(); 
		if(rs.first())
		{ // user exists
			throw (new Exception("Error:  User name is already in use.  Please enter another one."));
		}
		
		// TODO: username validation
	}
	
	private void validatePassword(String userName, String password1, String password2)  throws Exception
	{
		if (password1.isEmpty())
		{
			throw (new Exception("Error:  Blank passwords are prohibited."));
		}
		
		if (!password1.equals(password2))
		{
			throw (new Exception("Error:  Passwords do not match."));
		}
		
		if (password1.length() < 8 || password1.length() > 64)
		{
			throw (new Exception("Error:  The password must contain 8 to 64 characters."));
		}
		
		if (userName.equals(password1))
		{
			throw (new Exception("Error:  The password cannot match the user name."));
		}
		
		Pattern pattern = Pattern.compile(NUM_PATTERN);
		Matcher matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new Exception("Error:  The password must contain at least one numeric digit."));
		}
		
		pattern = Pattern.compile(UPPER_PATTERN);
		matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new Exception("Error:  The password must contain at least one uppercase character."));
		}
		
		pattern = Pattern.compile(LOWER_PATTERN);
		matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new Exception("Error:  The password must contain at least one lowercase character."));
		}
	}
	
	private void validateSecurityQuestion(int questionNum) throws Exception
	{
		String securityQuestionText = SecurityQuestions.getQuestionText(questionNum);
		if (securityQuestionText.isEmpty())
		{
			throw (new Exception("Error:  Please select a security question."));
		}
	}
	
	private void validateSecurityAnswer(String answer) throws Exception
	{
		if (answer == null || answer.isEmpty())
		{
			throw (new Exception("Error:  Please enter an answer for your security question."));
		}
	}
}

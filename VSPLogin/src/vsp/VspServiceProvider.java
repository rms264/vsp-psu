package vsp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.*;
import java.security.*;
import java.sql.*;

import vsp.dal.DatasourceConnection;
import vsp.dal.requests.CreateAccount;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;


public class VspServiceProvider
{
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String LOWER_PATTERN = "[a-z]";
	private static final String UPPER_PATTERN = "[A-Z]";
	private static final String NUM_PATTERN = "[0-9]";
	
	public VspServiceProvider()
	{
		// no implementation required (for now)
	}
	
	public void createAccount(String userName, String password1, 
			String password2, String email, String question, String answer) 
			throws ValidationException, SQLException, SqlRequestException 
	{
		CreateAccount.submit(userName, email, password1, password2, question, 
				answer);
		
	}
	
	public void deleteAccount(String userName) throws Exception
	{
		try
		{
			Connection connection = DatasourceConnection.getConnection();
			try
			{
				Validate.validateUserName(userName);
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
		Connection connection = null;
		try
		{
			connection = DatasourceConnection.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			rs.first();
			String email = rs.getString("email");
			Date signup = rs.getDate("signup");
			int securityQuestion = rs.getInt("securityQuestion");
			
			data = new AccountData(userName, email, signup, Integer.toString(securityQuestion));

		} //end try
		catch(Exception e)
		{
			e.printStackTrace();
			throw (new Exception("Error:  Unable to get account information."));
		}
		finally{
			if(connection != null){
				connection.close();
				connection = null;
			}
		}
		
		return data;
	}
	
	public List<String> getTraders() throws Exception
	{
		ArrayList<String> traders = new ArrayList<String>();
		Connection connection = null;
		try
		{
			connection = DatasourceConnection.getConnection();

			String query = "SELECT u.user_name from users u, user_roles r WHERE u.user_name = r.user_name AND r.role_name = 'trader' ORDER BY u.user_name";
			try
			{
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query);
			    
				while(rs.next())
				{
					traders.add(rs.getString("user_name"));
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				throw (new Exception("Error:  Unable to get trader list."));
			}
		    			
		}
		catch (Exception e)
		{
			throw (new Exception("Error:  Unable to get trader list."));
		}
		finally{
			if(connection != null){
				connection.close();
				connection = null;
			}
		}
		
		return traders;
	}
	
	/*private static String hashString(String password)
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
	
	private static void validateEmail(String email, Connection connection) throws Exception
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
	
	private static void validateUserName(String userName, Connection connection) throws Exception
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
	
	private static void validatePassword(String userName, String password1, String password2)  throws Exception
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
	
	private static int validateSecurityQuestion(String questionNum) throws Exception
	{
		int question = -1;
		try
		{
			question = Integer.parseInt(questionNum);
		}
		catch (NumberFormatException nfe)
		{
			throw (new Exception("Error:  Please select a security question."));
		}
		
		String securityQuestionText = SecurityQuestions.getQuestionText(question);
		if (securityQuestionText.isEmpty())
		{
			throw (new Exception("Error:  Please select a security question."));
		}
		
		return question;
	}
	
	private static void validateSecurityAnswer(String answer) throws Exception
	{
		if (answer == null || answer.isEmpty())
		{
			throw (new Exception("Error:  Please enter an answer for your security question."));
		}
	}*/
}

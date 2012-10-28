package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.AccountData;
import vsp.dataObject.PortfolioData;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.VSPUtils;
import vsp.utils.Validate;
import vsp.utils.Enumeration.SecurityQuestion;

public class Users
{
	private Users(){}
	public static final double DEFAULT_BALANCE = 20000.0; // $20,000 USD
	
	public static boolean addTraderAccount(String userName, String email, 
			String password1, String password2, String questionNum, 
			String answer) throws ValidationException, SQLException, 
			SqlRequestException
	{
		return insert(userName, email, password1, password2, questionNum, answer);
	}
	
	public static boolean deleteTraderAccount(String userName) throws 
		SQLException, SqlRequestException
	{
		return delete(userName);
	}
	
	public static boolean updateBalance(String userName, double balance) throws 
		SQLException, SqlRequestException
	{
		return submitBalanceUpdate(userName, balance);
	}
	
	public static boolean updateEmail(String userName, String email) throws 
		SQLException, SqlRequestException
	{
		return submitEmailUpdate(userName, email);
	}
	
	public static boolean updatePassword(String userName, 
	    String passwordUpdate1, String passwordUpdate2) throws 
		SQLException, SqlRequestException, ValidationException
	{
	   return submitPasswordUpdate(userName, passwordUpdate1, passwordUpdate2);
	}
	
	public static void updateLastDividendCheck(String userName, Date lastDividendCheck) throws 
		SQLException, SqlRequestException
	{
		submitLastDividendCheckUpdate(userName, lastDividendCheck);
	}
	
	public static boolean updateSecurity(String userName, String questionNum, String answer) throws 
		SQLException, SqlRequestException, ValidationException
	{
		return submitSecurityUpdate(userName, questionNum, answer);
	}
	
	
	/**
	 * @param userName
	 * @return This query returns all user names from the Users table 
	 * 		   that match the parameter passed in
	 * @throws ValidationException
	 * @throws SQLException
	 */
	public static List<String> queryUserNames(String userName) throws 
		ValidationException, SQLException
	{
		return submitUserNameQuery(userName);
	}
	
	/**
	 * @param email
	 * @return This query returns all email addresses from the Users table 
	 * 		   that match the parameter passed in
	 * @throws SQLException
	 * @throws ValidationException
	 */
	public static List<String> queryEmailAddresses(String email) throws 
		SQLException, ValidationException
	{
		return submitEmailQuery(email);
	}
	
	/**
   * @param userName
   * @return This query returns the email addresses from the Users table 
   *       associated with the user
   * @throws SQLException
   * @throws ValidationException
   */
  public static String queryUserEmailAddresses(String userName) throws 
    SQLException
  {
    return submitUserEmailQuery(userName);
  }
	
	/**
	 * @return This query returns all users from the Users table 
	 * 		   that have the Trader Role
	 * @throws SQLException
	 */
	public static List<String> queryAllTraders() throws SQLException
	{
		return submitTradersQuery();
	}
	
	/**
	 * @param userName
	 * @return This query returns a users account data from the Users table.  
	 * 		   If the user name is not found in the table null is returned
	 * @throws SQLException
	 */
	public static AccountData requestAccountData(String userName) throws SQLException 
	{
		return submitAccoutDataQuery(userName);
	}
	
	public static SecurityQuestion requestSecurityQuestion(String userName) 
	    throws SQLException, ValidationException
	{
	  try{
	    Integer questionID = Integer.parseInt(submitSecurityQuestionQuery(userName));
	    return SecurityQuestion.convert(questionID);
	  }
	  catch(NumberFormatException e){
	    throw new ValidationException("Security Question for user: " + userName + " not found");
	  }
	}
	
	
	public static boolean checkPassword(String user, String password) throws SQLException{
    Connection connection = null;
    boolean success = false;
    String sqlStatement = "SELECT user_pass from User WHERE user_name=?";
    try{
      connection = DatasourceConnection.getConnection();
      PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
      pStmt.setString(1, user);
      ResultSet rs = pStmt.executeQuery();
      if(rs.first()){
        String hashPass = rs.getString("user_pass");
        if(VSPUtils.hashString(password).equals(hashPass)){
          success = true;
        }
      }
    }
    finally
    {
      if(connection != null)
      {
        connection.close();
      }
    }
    return success;
  }
  
  public static boolean checkAnswer(String user, String answer) throws SQLException{
    Connection connection = null;
    boolean success = false;
    String sqlStatement = "SELECT security_answer from User WHERE user_name=?";
    try{
      connection = DatasourceConnection.getConnection();
      PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
      pStmt.setString(1, user);
      ResultSet rs = pStmt.executeQuery();
      if(rs.first()){
        String hashAnswer = rs.getString("security_answer");
        if(VSPUtils.hashString(answer).equals(hashAnswer)){
          success = true;
        }
      }
    }
    finally
    {
      if(connection != null)
      {
        connection.close();
      }
    }
    return success;
  }
	
	private static boolean submitBalanceUpdate(String userName, double balance) throws 
		SQLException, SqlRequestException
	{
		boolean success = false;
		Connection connection = null;
		try
		{
			String sqlStatement = 
					"UPDATE User SET current_balance=? WHERE user_name=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setDouble(1, balance);
			pStmt.setString(2, userName);
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to update balance for user name: " + userName);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return success;
	}
	
	private static boolean submitEmailUpdate(String userName, String email) throws 
		SQLException, SqlRequestException
	{
		boolean success = false;
		Connection connection = null;
		try
		{
			String sqlStatement = 
					"UPDATE User SET email=? WHERE user_name=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, email);
			pStmt.setString(2, userName);
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to update email for user name: " + userName);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return success;
	}
	
	private static void submitLastDividendCheckUpdate(String userName, Date lastDividendCheck) throws 
		SQLException, SqlRequestException
	{
		Connection connection = null;
		try
		{
			String sqlStatement = "Update User SET last_dividend_check=? WHERE user_name=?";			
			connection = DatasourceConnection.getConnection();
			java.sql.Date date = new java.sql.Date(new Date().getTime());
			
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setDate(1, date);
			pStmt.setString(2, userName);
		
			int result = pStmt.executeUpdate();
			if (result != 1)
			{
				throw new SqlRequestException("Error: Failed to update password for user name: " + userName);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
	
	private static boolean submitPasswordUpdate(String userName, String password1, String password2) throws 
		SQLException, SqlRequestException, ValidationException
	{
		Connection connection = null;
		boolean success = false;
		try
		{
			String sqlStatement = "Update User SET user_pass=? WHERE user_name=?";			
			if(Validate.validatePassword(userName, password1, password2))
			{
				connection = DatasourceConnection.getConnection();
				PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
				pStmt.setString(1, VSPUtils.hashString(password1));
				pStmt.setString(2, userName);
			
				int result = pStmt.executeUpdate();
				if (result == 1)
				{
					success = true;
				}
				else
				{
					throw new SqlRequestException("Error: Failed to update password for user name: " + userName);
				}
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return success;
	}
	
	private static boolean submitSecurityUpdate(String userName, String questionNum, String answer) throws 
		SQLException, SqlRequestException, ValidationException
	{
		Connection connection = null;
		boolean success = false;
		SecurityQuestion question = SecurityQuestion.DEFAULT;
		try
		{
			String sqlStatement = "Update User SET security_question_id=?, security_answer=? WHERE user_name=?";
			question = SecurityQuestion.convert(Integer.parseInt(questionNum));
			
			if(Validate.validateSecurityQuestion(question) && Validate.validateSecurityAnswer(answer))
			{
				connection = DatasourceConnection.getConnection();
				PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
				pStmt.setInt(1, question.getValue());
				pStmt.setString(2, VSPUtils.hashString(answer));
				pStmt.setString(3, userName);
			
				int result = pStmt.executeUpdate();
				if (result == 1)
				{
					success = true;
				}
				else
				{
					throw new SqlRequestException("Error: Failed to update security question/answer for user name: " + userName);
				}
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return success;
	}
	
	private static List<String> submitUserNameQuery(String userName) throws SQLException, 
		ValidationException
	{
		String sqlStatement = "SELECT * FROM User WHERE user_name=?";
		if(Validate.validateUserName(userName))
		{
			Connection connection = null;
			List<String> results = new ArrayList<String>();
			try
			{
				connection = DatasourceConnection.getConnection();
				PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
				pStmt.setString(1, userName);  
				ResultSet rs = pStmt.executeQuery();
				while(rs.next())
				{
					results.add(rs.getString("user_name"));
				}
				
				return results;
			}
			finally
			{
				if(connection != null)
				{
					connection.close();
				}
			}
		}
		else
		{
			throw new ValidationException(
					"Error:  Username is Invalid " +
					"Please enter a valid Username.");
		}
	}
	

	private static List<String> submitEmailQuery(String email) throws SQLException, 
		ValidationException
	{
		if(Validate.validateEmail(email))
		{
			String sqlStatement = "SELECT * FROM User WHERE email=?";
			Connection connection = null;
			List<String> result = new ArrayList<String>();
			try
			{
				connection = DatasourceConnection.getConnection();
				PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
				pStmt.setString(1, email);  
				ResultSet rs = pStmt.executeQuery();
				while(rs.next())
				{
					result.add(rs.getString("email"));
				}
				
				return result;
			}
			finally
			{
				if(connection != null)
				{
					connection.close();
				}
			}
		}
		else
		{
			throw new ValidationException(
					"Error:  The email address is invalid.  " + 
					"Please enter a valid email address.");
		}
	}
	
	private static String submitUserEmailQuery(String user) throws SQLException 
	{
    String sqlStatement = "SELECT email FROM User WHERE user_name=?";
    Connection connection = null;
    String result = new String();
    try
    {
      connection = DatasourceConnection.getConnection();
      PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
      pStmt.setString(1, user);  
      ResultSet rs = pStmt.executeQuery();
      if(rs.first())
      {
        result = rs.getString("email");
      }
      
      return result;
    }
    finally
    {
      if(connection != null)
      {
        connection.close();
      }
    }
  }
	
	private static String submitSecurityQuestionQuery(String user) throws SQLException 
  {
    String sqlStatement = "SELECT security_question_id FROM User WHERE user_name=?";
    Connection connection = null;
    String result = new String();
    try
    {
      connection = DatasourceConnection.getConnection();
      PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
      pStmt.setString(1, user);  
      ResultSet rs = pStmt.executeQuery();
      if(rs.first())
      {
        result = rs.getString("security_question_id");
      }
      
      return result;
    }
    finally
    {
      if(connection != null)
      {
        connection.close();
      }
    }
  }
	private static String submitSecurityAnswerQuery(String user) throws SQLException 
  {
    String sqlStatement = "SELECT security_answer FROM User WHERE user_name=?";
    Connection connection = null;
    String result = new String();
    try
    {
      connection = DatasourceConnection.getConnection();
      PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
      pStmt.setString(1, user);  
      ResultSet rs = pStmt.executeQuery();
      if(rs.first())
      {
        result = rs.getString("security_answer");
      }
      
      return result;
    }
    finally
    {
      if(connection != null)
      {
        connection.close();
      }
    }
  }
	
	
	private static List<String> submitTradersQuery() throws SQLException{
		List<String> results = new ArrayList<String>();
		Connection connection = null;
		try
		{
			String sqlStatement = "SELECT u.user_name from User u, " + 
					"Role r WHERE u.user_name = r.user_name AND " + 
					"r.role_name = 'trader' ORDER BY u.user_name";
			connection = DatasourceConnection.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStatement);
			while(rs.next())
			{
				results.add(rs.getString("user_name"));
			}
		    			
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return results;
	}
	
	private static AccountData submitAccoutDataQuery(String userName) throws SQLException
	{
		Connection connection = null;
		AccountData data = null;
		try
		{
			String sqlStatement = "SELECT * FROM User WHERE user_name=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);
			ResultSet rs = pStmt.executeQuery();
			
			if(rs.first())
			{
				data = getAccountDataFromResultSet(rs);
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		return data;
	}
	
	private static boolean insert(String userName, String email, 
					String password1, String password2, String questionNum, 
					String answer) throws SQLException, ValidationException
	{
		Connection connection = null;
		boolean success = false;
		SecurityQuestion question = SecurityQuestion.DEFAULT;
		try
		{
			String sqlStatement = "INSERT into User values(?,?,?,?,?,?,?,?)";
			java.sql.Date date = new java.sql.Date(new Date().getTime());
			question = SecurityQuestion.convert(
					Integer.parseInt(questionNum));
			
			if(!Validate.userNameExistsInDb(userName) && 
					   !Validate.emailExistsInDb(email) &&
					   Validate.validatePassword(userName, password1, password2) &&
					   Validate.validateSecurityQuestion(question) &&
					   Validate.validateSecurityAnswer(answer))
			{
				connection = DatasourceConnection.getConnection();
				PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
				pStmt.setString(1, userName);  
				pStmt.setString(2, VSPUtils.hashString(password1));   
				pStmt.setString(3, email);
				pStmt.setDate(4, date);
				pStmt.setInt(5, question.getValue());
				pStmt.setString(6, VSPUtils.hashString(answer));
				pStmt.setDouble(7, DEFAULT_BALANCE);
				pStmt.setDate(8 , date);
			
				int result = pStmt.executeUpdate();
				if (result == 1)
				{
					success = true;
				}
			}
		}
		catch(NumberFormatException e)
		{
			throw new ValidationException(
					"Error:  Please select a security question.");
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return success;
	}
	
	private static boolean delete(String userName) throws SQLException, SqlRequestException
	{
		Connection connection = null;
		boolean success = false;
		try
		{
			String sqlStatement = "DELETE from User WHERE user_name=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			int result = pStmt.executeUpdate(); 
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw (new SqlRequestException("Error:  Unable to delete account."));
			}
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
		
		return success;
	}
	
	private static AccountData getAccountDataFromResultSet(ResultSet rs) throws 
		SQLException
	{
		AccountData data = null;
		
		String userName = rs.getString("user_name");
		String email = rs.getString("email");
		Date signup = rs.getDate("signup");
		int securityQuestion = rs.getInt("security_question_id");
		double balance = rs.getDouble("current_balance");
		
		java.sql.Date sqlDate = rs.getDate("last_dividend_check");
		java.util.Date lastDividendCheck = new java.util.Date(sqlDate.getYear(), sqlDate.getMonth(), sqlDate.getDate());
	
		data =  new AccountData(userName, email, signup, 
			SecurityQuestion.convert(securityQuestion), balance, lastDividendCheck);
		
		return data;
	}
}

package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.AccountData;
import vsp.exception.ValidationException;
import vsp.utils.Validate;
import vsp.utils.Enumeration.SecurityQuestion;

public class UserInfo {
	
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
		UserInfo request = new UserInfo();
		return request.submitUserNameQuery(userName);
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
		UserInfo request = new UserInfo();
		return request.submitEmailQuery(email);
		
	}
	
	/**
	 * @return This query returns all users from the Users table 
	 * 		   that have the Trader Role
	 * @throws SQLException
	 */
	public static List<String> queryAllTraders() throws SQLException{
		UserInfo request = new UserInfo();
		return request.submitTradersQuery();
		
	}
	
	/**
	 * @param userName
	 * @return This query returns a users account data from the Users table.  
	 * 		   If the user name is not found in the table null is returned
	 * @throws SQLException
	 */
	public static AccountData requestAccountData(String userName) throws SQLException 
	{
		UserInfo request = new UserInfo();
		return request.submitAccoutDataQuery(userName);
	}
	
	private UserInfo(){}
	
	private List<String> submitUserNameQuery(String userName) throws SQLException, 
		ValidationException
	{
		String sqlStatement = "SELECT * FROM users WHERE user_name=?";
		// check for existence of user name in database
		if(Validate.userName(userName)){
			Connection connection = null;
			List<String> results = new ArrayList<String>();
			try{
				connection = DatasourceConnection.getConnection();
				PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
				pStmt.setString(1, userName);  
				ResultSet rs = pStmt.executeQuery();
				while(rs.next()){
					results.add(rs.getString("user_name"));
				}
				return results;
			}
			finally{
				if(connection != null){
					connection.close();
				}
			}
		}
		else{
			throw new ValidationException(
					"Error:  Username is Invalid " +
					"Please enter a valid Username.");
		}
	}
	

	private List<String> submitEmailQuery(String email) throws SQLException, 
		ValidationException
	{
		if(Validate.email(email)){
			String sqlStatement = "SELECT * FROM users WHERE email=?";
			// check for existence of user name in database
			Connection connection = null;
			List<String> result = new ArrayList<String>();
			try{
				connection = DatasourceConnection.getConnection();
				PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
				pStmt.setString(1, email);  
				ResultSet rs = pStmt.executeQuery();
				while(rs.next()){
					result.add(rs.getString("email"));
				}
				return result;
			}
			finally{
				if(connection != null){
					connection.close();
				}
			}
		}
		else{
			throw new ValidationException(
					"Error:  The email address is invalid.  " + 
					"Please enter a valid email address.");
		}
	}
	
	private List<String> submitTradersQuery() throws SQLException{
		List<String> results = new ArrayList<String>();
		Connection connection = null;
		try
		{
			String sqlStatement = "SELECT u.user_name from users u, " + 
					"user_roles r WHERE u.user_name = r.user_name AND " + 
					"r.role_name = 'trader' ORDER BY u.user_name";
			connection = DatasourceConnection.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStatement);
			while(rs.next())
			{
				results.add(rs.getString("user_name"));
			}
		    			
		}
		finally{
			if(connection != null){
				connection.close();
				connection = null;
			}
		}
		
		return results;
	}
	
	private AccountData submitAccoutDataQuery(String userName) throws SQLException
	{
		Connection connection = null;
		AccountData data = null;
		try
		{
			String sqlStatement = "SELECT email, signup, securityQuestion " + 
					"FROM users WHERE user_name='" + userName + "'";
			connection = DatasourceConnection.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sqlStatement);
			
			if(rs.first()){
				String email = rs.getString("email");
				Date signup = rs.getDate("signup");
				int securityQuestion = rs.getInt("securityQuestion");
			
				data =  new AccountData(userName, email, signup, 
					SecurityQuestion.convert(securityQuestion));
			}
		}
		finally{
			if(connection != null){
				connection.close();
			}
		}
		return data;
	}
}

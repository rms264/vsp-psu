package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vsp.dal.DatasourceConnection;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;
import vsp.utils.Enumeration.Role;

public class Roles {

	public static boolean addNewUserRole(String userName, Role role) throws 
		SQLException, SqlRequestException, ValidationException
	{
		Roles request = new Roles();
		return request.insert(userName, role);
	}
	
	public static boolean deleteUserRole(String userName) throws SQLException, 
		SqlRequestException, ValidationException
	{
		Roles request = new Roles();
		return request.delete(userName);
	}
	
	private Roles(){}
	
	private boolean insert(String userName, Role role) throws SQLException, 
		SqlRequestException, ValidationException
	{
		boolean success = false;
		Connection connection = null;
		if(role == Role.DEFAULT)
		{
			throw new ValidationException(
					"Error: Please select a valid Role to insert");
		}
		
		if(!Validate.userNameExistsInDb(userName))
		{
			throw new ValidationException(
				"Error: Cannot insert Role. User name not found in database");
		}
		
		try
		{
			String sqlStatement = "INSERT INTO Roles VALUES(?,?)";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			pStmt.setString(2, role.toString());
			int result = pStmt.executeUpdate();
			
			//Should return 1 to indicate one row was added to the role table
			if(result == 1)
			{
				success = true;
			}
			else
			{
				throw new SqlRequestException("Error: Failed to insert " +
						role.toString() + " for user: " + userName);
			}
			return success;
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
	
	private boolean delete(String userName) throws SQLException, 
		SqlRequestException, ValidationException
	{
		boolean success = false;
		Connection connection = null;
		if(!Validate.userNameExistsInDb(userName))
		{
			throw new ValidationException(
				"Error: Cannot remove Role for User: " + userName +
				" not found in database");
		}
		
		try
		{
			String sqlStatement = "DELETE from Roles WHERE user_name=?";
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
				throw new SqlRequestException("Error: Failed to delete role " +
						"for user: " + userName);
			}
			return success;
		}
		finally
		{
			if(connection != null)
			{
				connection.close();
			}
		}
	}
}

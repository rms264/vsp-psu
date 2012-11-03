package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vsp.dal.DatasourceConnection;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;
import vsp.utils.Enumeration.RoleType;

public class Roles {

	public static boolean addNewUserRole(String userName, RoleType role) throws 
		SQLException, SqlRequestException, ValidationException
	{
		return insert(userName, role);
	}
	
	private Roles(){}
	
	private static boolean insert(String userName, RoleType role) throws SQLException, 
		SqlRequestException, ValidationException
	{
		boolean success = false;
		Connection connection = null;
		if(role == RoleType.DEFAULT)
		{
			throw new ValidationException(
					"Error:  Please select a valid Role to insert");
		}
		
		if(!Validate.userNameExistsInDb(userName))
		{
			throw new ValidationException(
				"Error:  Cannot insert Role. User name not found in database");
		}
		
		try
		{
			String sqlStatement = "INSERT INTO Role VALUES(?,?)";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			pStmt.setString(2, role.toString());
			int result = pStmt.executeUpdate();
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
}

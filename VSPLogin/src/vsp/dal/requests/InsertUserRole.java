package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import vsp.dal.DatasourceConnection;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Enumeration.Role;
import vsp.utils.Validate;

public class InsertUserRole {
	private final String sqlStatement = "insert into user_roles values(?,?)";
	private final String userName;
	private final Role role;
	public static boolean submit(String userName, String role) 
			throws ValidationException, SQLException, SqlRequestException
	{
		boolean success = false;
		InsertUserRole request = new InsertUserRole(userName,role);
		if(request.isValid()){
			success = request.submitRequest();
		}
		
		return success;
	}
	
	private InsertUserRole(String userName, String role){
		this.userName = userName;
		this.role = Role.get(role);
	}

	private boolean submitRequest() throws SQLException, SqlRequestException{
		boolean success = false;
		Connection connection = null;
		try{
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			pStmt.setString(2, role.toString());
			int result = pStmt.executeUpdate();
			
			//Should return 1 to indicate one row was added to the role table
			if(result == 1){
				success = true;
			}
			else{
				throw new SqlRequestException("Error: Failed to insert " +
						role.toString() + " for user: " + userName);
			}
			return success;
		}
		finally{
			if(connection != null){
				connection.close();
			}
		}
	}
	private boolean isValid() throws ValidationException, SQLException{
		boolean valid = false;
		if(role == Role.DEFAULT){
			throw new ValidationException(
					"Error: Please select a valid Role to insert");
		}
		if(!Validate.userNameExistsInDb(userName)){
			throw new ValidationException(
				"Error: Cannot insert Role. User name not found in database");
		}
		else{
			valid = true;
		}
		return valid;
	}
}

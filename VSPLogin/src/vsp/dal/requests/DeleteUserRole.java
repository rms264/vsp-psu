package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vsp.dal.DatasourceConnection;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;

public class DeleteUserRole {
	private final String sqlStatement = "DELETE from user_roles WHERE user_name=?";
	private final String userName;
	public static boolean submit(String userName) 
			throws ValidationException, SQLException, SqlRequestException
	{
		boolean success = false;
		DeleteUserRole request = new DeleteUserRole(userName);
		if(request.isValid()){
			success = request.submitRequest();
		}
		
		return success;
	}
	
	private DeleteUserRole(String userName){
		this.userName = userName;
	}

	private boolean submitRequest() throws SQLException, SqlRequestException{
		boolean success = false;
		Connection connection = null;
		try{
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			int result = pStmt.executeUpdate(); 
			if(result == 1){
				success = true;
			}
			else{
				throw new SqlRequestException("Error: Failed to delete role " +
						"for user: " + userName);
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

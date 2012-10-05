package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vsp.dal.DatasourceConnection;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;

public class DeleteAccount {

	private final String sqlStatement = "DELETE from users WHERE user_name=?";
	private final String userName;
	public static boolean submit(String userName) throws ValidationException,
		SQLException, SqlRequestException
	{
		boolean success = false;
		DeleteAccount request = new DeleteAccount(userName);
		if(Validate.userNameExistsInDb(userName)){
			success = request.submitRequest();
		}
		else{
			throw (new ValidationException("Error:  Account does not exist."));
		}
		return success;
	}
	
	private DeleteAccount(String userName){
		this.userName = userName;
	}
	
	private boolean submitDeleteUserAccountRequest() throws SQLException, 
		SqlRequestException
	{
		Connection connection = null;
		boolean success = false;
		try
		{
			connection = DatasourceConnection.getConnection();
			int result = 0;
			// delete row from users table
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			result = pStmt.executeUpdate(); 
			
			//this should return 1 meaning that one row was removed 
			//from the users table
			if(result == 1){
				success = true;
			}
			else{
				throw (new SqlRequestException("Error:  Unable to delete account."));
			}
		}
		finally{
			if(connection != null){
				connection.close();
			}
		}
		return success;
	}
	
	private boolean submitRequest() throws SQLException, SqlRequestException, ValidationException{
		boolean success = false;
		if(submitDeleteUserAccountRequest()){
			success = DeleteUserRole.submit(userName);
		}
		
		
		return success;
	}


}

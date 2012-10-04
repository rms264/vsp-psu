package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vsp.dal.DatasourceConnection;
import vsp.exception.ValidationException;
import vsp.utils.Enumeration.Role;

public class InsertUserRole {
	private final String sqlStatement = "insert into user_roles values(?,?)";
	private final String userName;
	private final Role role;
	public static int submit(String userName, String role) 
			throws ValidationException, SQLException
	{
		int result = -1;
		InsertUserRole request = new InsertUserRole(userName,role);
		if(request.isValid()){
			request.submitRequest();
		}
		
		return result;
	}
	
	private InsertUserRole(String userName, String role){
		this.userName = userName;
		this.role = Role.get(role);
	}

	private int submitRequest() throws SQLException{
		int result = -1;
		Connection connection = null;
		try{
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			pStmt.setString(2, role.toString());
			result = pStmt.executeUpdate(); 		
			return result;
		}
		finally{
			if(connection != null){
				connection.close();
			}
		}
	}
	private boolean isValid() throws ValidationException, SQLException{
		if(role == Role.DEFAULT){
			throw new ValidationException(
					"Error: Please select a valid Role to insert");
		}
		
		String name = QueryUserName.submit(userName);
		
		if(name == null){
			throw new ValidationException(
					"Error: Cannot insert Role. User name not found in database");
		}
		return true;
	}
}

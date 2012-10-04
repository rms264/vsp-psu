package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vsp.dal.DatasourceConnection;
import vsp.exception.ValidationException;

public class QueryEmailAddress {

	private final String email;
	private final String sqlStatement = "SELECT * FROM users WHERE email=?";
	
	public static String submit(String email) throws ValidationException, SQLException{
		QueryEmailAddress request = new QueryEmailAddress(email);
		String result = null;
		if(request.isValid()){
			result =  request.submitRequest();
		}
		return result;
	}
	
	private QueryEmailAddress(String email) throws ValidationException{
		this.email = email;
	}
	
	private boolean isValid() throws ValidationException{
		// empty user names are not valid
		if (email == null || email.isEmpty())
		{
			throw (new ValidationException(
					"Error:  Please enter your email address."));
		}
		return true;
	}
	
	private String submitRequest() throws SQLException{
		// check for existence of user name in database
		Connection connection = null;
		String result = null;
		try{
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, email);  
			ResultSet rs = pStmt.executeQuery();
			if(rs.first()){
				result = rs.getString("email");
			}
			return result;
		}
		finally{
			if(connection != null){
				connection.close();
			}
		}
	}
}

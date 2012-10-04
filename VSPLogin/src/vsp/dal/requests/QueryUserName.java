package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vsp.dal.DatasourceConnection;
import vsp.exception.ValidationException;

public class QueryUserName {

	private final String userName;
	private final String sqlStatement = "SELECT * FROM users WHERE user_name=?";
	
	public static String submit(String userName) throws ValidationException, SQLException{
		QueryUserName request = new QueryUserName(userName);
		String result = null;
		if(request.isValid()){
			result = request.submitRequest();
		}
		return result;
	}
	
	private QueryUserName(String userName) throws ValidationException{
		this.userName = userName;
	}
	
	private boolean isValid() throws ValidationException{
		// empty user names are not valid
		if (userName == null || userName.isEmpty())
		{
			throw (new ValidationException("[Error] Please enter a user name."));
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
			pStmt.setString(1, userName);  
			ResultSet rs = pStmt.executeQuery();
			if(rs.first()){
				result = rs.getString("user_name");
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

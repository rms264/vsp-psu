package vsp.dal.requests;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import vsp.dal.DatasourceConnection;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Enumeration.Role;
import vsp.utils.Enumeration.SecurityQuestion;
import vsp.utils.VSPUtils;
import vsp.utils.Validate;

public class CreateAccount {
	private final String sqlStatement = "insert into users values(?,?,?,?,?,?)";
	private final String userName;
	private final String password1;
	private final String password2;
	private final String email;
	private SecurityQuestion question;
	private final String answer;
	private final Date date;
	
	
	public static boolean submit(String userName, String email, String password1, 
			String password2, String questionNum, String answer) 
			throws	ValidationException, SQLException, SqlRequestException
	{
		boolean success = false;
		CreateAccount request = new CreateAccount(userName, email, password1, 
				password2, questionNum, answer);
		
		if(request.isValid()){
			
			success = request.createAccount();
		}
		return success;
	}
	
	private CreateAccount(String userName, String email, String password1, 
			String password2, String questionNum, String answer) throws ValidationException{
		this.userName = userName;
		this.password1 = password1;
		this.password2 = password2;
		this.email = email;
		this.answer = answer;
		date = new Date(new java.util.Date().getTime());
		try{
			this.question = SecurityQuestion.convert(
					Integer.parseInt(questionNum));
		}
		catch(NumberFormatException e){
			throw new ValidationException(
					"Error:  Please select a security question.");
		}
	}
	
	private boolean createAccount() throws SQLException, SqlRequestException, 
		ValidationException 
	{
		boolean success = false;
		if(submitAccountCreationRequest()){
			success = InsertUserRole.submit(userName, Role.TRADER.toString());
		}
		else{
		   throw (new SqlRequestException("Error:  Failed to create a " + 
				   "user account for: " + userName));
		}
		return success;
		
	}
	private boolean submitAccountCreationRequest() throws SQLException, 
		SqlRequestException
	{
		Connection connection = null;
		boolean success = false;
		try{
			connection = DatasourceConnection.getConnection();
		    // insert into users table
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			pStmt.setString(2, VSPUtils.hashString(password1));   
			pStmt.setString(3, email);
			pStmt.setDate(4, date);
			pStmt.setInt(5, question.getVal());
			pStmt.setString(6, VSPUtils.hashString(answer));
			
			int result = pStmt.executeUpdate();
		   
			//this should return 1 meaning that one row was added 
			//to the users table
			if (result == 1)
			{
				success = true;
			}
			
		}
		finally
		{
			connection.close();
			connection = null;
		}
		return success;
	}
	
	private boolean isValid() throws ValidationException, SQLException{
		boolean valid = false;
		if(!Validate.userNameExistsInDb(userName) && 
		   !Validate.emailExistsInDb(email) &&
		   Validate.validatePassword(userName, password1, password2) &&
		   Validate.validateSecurityQuestion(question) &&
		   Validate.validateSecurityAnswer(answer))
		   
		{
			valid = true;
		}
		return valid;
	}
}

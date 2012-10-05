package vsp.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vsp.dal.requests.UserInfo;
import vsp.exception.FormatException;
import vsp.exception.ValidationException;
import vsp.utils.Enumeration.SecurityQuestion;

public class Validate {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String LOWER_PATTERN = "[a-z]";
	private static final String UPPER_PATTERN = "[A-Z]";
	private static final String NUM_PATTERN = "[0-9]";
	private Validate(){}
	
	
	/**
	 * @param userName
	 * @return - returns true if the user name is valid and found in the 
	 * 			 database
	 * @throws ValidationException
	 * @throws SQLException
	 */
	public static boolean userNameExistsInDb(String userName) throws 
		ValidationException, SQLException
	{
		boolean found = true;
		List<String> results = UserInfo.queryUserNames(userName);
		if(results.isEmpty()){
			found = false;
		}
		return found;
	}
	
	/**
	 * @param email
	 * @return returns true if the email address if formatted correctly and if
	 * 		   the email address is found in the database
	 * @throws SQLException
	 * @throws ValidationException
	 * @throws FormatException 
	 */
	public static boolean emailExistsInDb(String email) 
			throws SQLException, ValidationException
	{
		boolean found = true;
		List<String>results = UserInfo.queryEmailAddresses(email);
		if(results.isEmpty())
		{ 
			found = false;
		}
		return found;
		
	}
	
	/**
	 * @param userName
	 * @param password1
	 * @param password2
	 * @return returns true if the password1 matches password2
	 * 		   and it is between 8 and 64 characters and it contains at least
	 * 		   one numeric digit and the password doesn't match the username 
	 * @throws ValidationException
	 */
	public static boolean validatePassword(String userName, String password1, 
			String password2)  throws ValidationException
	{
		if (password1.isEmpty())
		{
			throw (new ValidationException(
					"Error:  Blank passwords are prohibited."));
		}
		
		if (!password1.equals(password2))
		{
			throw (new ValidationException(
					"Error:  Passwords do not match."));
		}
		
		if (password1.length() < 8 || password1.length() > 64)
		{
			throw (new ValidationException(
					"Error:  The password must contain 8 to 64 characters."));
		}
		
		if (userName.equals(password1))
		{
			throw (new ValidationException(
					"Error:  The password cannot match the user name."));
		}
		
		Pattern pattern = Pattern.compile(NUM_PATTERN);
		Matcher matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new ValidationException(
					"Error:  The password must contain at least one numeric " + 
					"digit."));
		}
		
		pattern = Pattern.compile(UPPER_PATTERN);
		matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new ValidationException(
					"Error:  The password must contain at least one " +
					"uppercase character."));
		}
		
		pattern = Pattern.compile(LOWER_PATTERN);
		matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new ValidationException(
					"Error:  The password must contain at least one lowercase "+
					"character."));
		}
		
		return true;
	}
	
	public static boolean validateSecurityQuestion(SecurityQuestion question) 
			throws ValidationException
	{
		if (question == SecurityQuestion.DEFAULT)
		{
			throw (new ValidationException(
					"Error:  Please select a security question."));
		}
		
		return true;
	}
	
	public static boolean validateSecurityAnswer(String answer) 
			throws ValidationException
	{
		if (answer == null || answer.isEmpty())
		{
			throw (new ValidationException(
					"Error:  Please enter an answer for your security " + 
					"question."));
		}
		return true;
	}
	
	public static boolean email(String email){
		boolean isValid = false;
		if (email != null && !email.isEmpty()){
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			if(matcher.matches())
			{
				isValid = true;
			}
		}
		return isValid;
	}
	
	public static boolean userName(String userName){
		boolean isValid = false;
		// empty user names are not valid
		if (userName != null && !userName.isEmpty())
		{
			isValid = true;
		}
		return isValid;
	}
}

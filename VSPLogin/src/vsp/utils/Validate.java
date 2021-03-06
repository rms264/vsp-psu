package vsp.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vsp.dal.requests.Stocks;
import vsp.dal.requests.Users;
import vsp.dataObject.Stock;
import vsp.exception.ValidationException;
import vsp.utils.Enumeration.SecurityQuestion;

public class Validate
{
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String LOWER_PATTERN = "[a-z]";
	private static final String UPPER_PATTERN = "[A-Z]";
	private static final String NUM_PATTERN = "[0-9]";
	private Validate()
	{
		// no implementation required
	}
	
	
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
		List<String> results = Users.queryUserNames(userName);
		if(results.isEmpty())
		{
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
		List<String>results = Users.queryEmailAddresses(email);
		if(results.isEmpty())
		{ 
			found = false;
		}
		
		return found;
	}
	
	/**
	 * @param symbol
	 * @return true if the stock symbol is found in the database
	 * @throws SQLException
	 */
	public static boolean stockExistsInDb(String symbol) throws SQLException
	{
		boolean found = true;
		Stock results = Stocks.getStock(symbol);
		if(results == null)
		{ 
			found = false;
		}
		
		return found;
	}
	
	public static boolean validateOrderAction(String action) 
			throws ValidationException
	{
		if (action == null || action.isEmpty())
		{
			throw (new ValidationException(
					"Please enter an answer for your security question."));
		}
		
		return true;
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
					"Blank passwords are prohibited."));
		}
		
		if (!password1.equals(password2))
		{
			throw (new ValidationException(
					"Passwords do not match."));
		}
		
		if (password1.length() < 8)
		{
			throw (new ValidationException(
					"The password must contain at least 8 characters."));
		}
		else if(password1.length() > 64){
		  throw (new ValidationException(
          "The password must contain at most 64 characters."));
		}
		
		if (userName.equals(password1))
		{
			throw (new ValidationException(
					"The password cannot match the user name."));
		}
		
		Pattern pattern = Pattern.compile(NUM_PATTERN);
		Matcher matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new ValidationException(
					"The password must contain at least one numeric " + 
					"digit."));
		}
		
		pattern = Pattern.compile(UPPER_PATTERN);
		matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new ValidationException(
					"The password must contain at least one uppercase character."));
		}
		
		pattern = Pattern.compile(LOWER_PATTERN);
		matcher = pattern.matcher(password1);
		if(!matcher.find())
		{
			throw (new ValidationException(
					"The password must contain at least one lowercase character."));
		}
		
		return true;
	}
	
	
	public static float validateLimitPrice(String limitPrice)
			throws ValidationException
	{
		float price = 0f;
		try
		{
			price = Float.parseFloat(limitPrice);	
		}
		catch (Exception ex)
		{
			throw (new ValidationException(
						"Please enter a whole number for the Limit Rice."));
		}
		
		if (price <= 0)
		{
			throw (new ValidationException(
					"Please enter a positive Limit Price."));
		}
		
		return price;
	}
	
	public static float validateStopPrice(String stopPrice)
			throws ValidationException
	{
		float price = 0f;
		try
		{
			price = Float.parseFloat(stopPrice);	
		}
		catch (Exception ex)
		{
			throw (new ValidationException(
						"Please enter a whole number for the Stop Rice."));
		}
		
		if (price <= 0)
		{
			throw (new ValidationException(
					"Please enter a positive Stop Price."));
		}
		
		return price;
	}
	
	public static float validateQuantity(String quantity)
			throws ValidationException
	{
		int quantityParsed = 0;
		try
		{
			quantityParsed = Integer.parseInt(quantity);
		}
		catch (Exception ex)
		{
			throw (new ValidationException(
					"Please enter a whole number for the quantity."));
		}
		
		if (quantityParsed <= 0)
		{
			throw (new ValidationException(
					"Please provide a positive quantity."));
		}
		
		return Float.parseFloat(quantity);
	}
	
	public static boolean validateSecurityQuestion(SecurityQuestion question) 
			throws ValidationException
	{
		if (question == SecurityQuestion.DEFAULT)
		{
			throw (new ValidationException(
					"Please select a security question."));
		}
		
		return true;
	}
	
	public static boolean validateSecurityAnswer(String answer) 
			throws ValidationException
	{
		if (answer == null || answer.isEmpty())
		{
			throw (new ValidationException(
					"Please enter an answer for your security " + 
					"question."));
		}
		
		return true;
	}
	
	public static boolean validateEmail(String email)
	{
		boolean isValid = false;
		if (email != null && !email.isEmpty())
		{
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			if(matcher.matches())
			{
				isValid = true;
			}
		}
		
		return isValid;
	}
	
	public static boolean validateUserName(String userName)
	{
		boolean isValid = false;
		// empty user names are not valid
		if (userName != null && !userName.isEmpty())
		{
			isValid = true;
		}
		
		return isValid;
	}
}

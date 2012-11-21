package unitTests;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import vsp.VspServiceProvider;
import vsp.dataObject.AccountData;
import vsp.form.validator.FormValidator;
import vsp.form.validator.FormValidatorFactory;
import vsp.servlet.form.RegisterForm;
import vsp.utils.Validate;

@RunWith(OrderedRunner.class)
public class AdministrativeFunction
{
	private final VspServiceProvider vsp = new VspServiceProvider();
	private final String userName = "unitTestUser";
	private final String password1 = "User1234";
	private final String email = "unitTestUser@unitTestUser.com";
	private final String securityQuestion = "0"; //favorite color
	private final String securityAnswer = "blue";	
	private final String password2 = "UserUser1234";	
	
	public AdministrativeFunction(){
		RegisterForm registerForm = new RegisterForm(userName, 
		        									  password1,
		        									  password1,
		        									  email,
		        									  securityQuestion,
		        									  securityAnswer);
		FormValidator validator = FormValidatorFactory.getRegistrationValidator();
		List<String> errors = validator.validate(registerForm);
		  if(errors.isEmpty())
		  {
		    try{
			  vsp.createTraderAccount(new AccountData(registerForm));
		    }
		    catch(Exception e){
		    	
		    }
		  }	
	}
	
	@Test
	@Order(order=1)
	public void getUserList()
	{
		try
		{
			// the default traders in the database include 'test' and 'test1'
			List<String> traders = vsp.getTraders();
			Assert.assertTrue(traders.size() >= 2);
			Assert.assertTrue(traders.contains("test"));
			Assert.assertTrue(traders.contains("test1"));
		}
		catch (Exception e)
		{
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=2)
	public void changePasswordInvalid()
	{
		try
		{
			// the default traders in the database include 'test' and 'test1'
			List<String> traders = vsp.getTraders();
			Assert.assertTrue(traders.size() >= 2);
			Assert.assertTrue(traders.contains(userName));			
			Assert.assertTrue(Validate.validatePassword(userName, "Test123", "Test123"));
			Assert.assertTrue(vsp.updateUserPassword(userName, "Test123", "Test123"));
			Assert.fail();
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("The password must contain at least 8 characters."));
		}		
	}
	
	@Test
	@Order(order=3)
	public void changePasswordValid()
	{
		try
		{
			// the default traders in the database include 'test' and 'test1'
			List<String> traders = vsp.getTraders();
			Assert.assertTrue(traders.size() >= 2);
			Assert.assertTrue(traders.contains(userName));			
			Assert.assertTrue(Validate.validatePassword(userName, password2, password2));
			Assert.assertTrue(vsp.updateUserPassword(userName, password2, password2));			
		}
		catch (Exception e)
		{
			Assert.fail();
		}		
	}
	
	@Test
	@Order(order=4)
	public void deleteTrader()
	{
		try
		{
			// the default traders in the database include 'test' and 'test1'
			List<String> traders = vsp.getTraders();
			Assert.assertTrue(traders.size() >= 2);
			Assert.assertTrue(traders.contains(userName));
			vsp.deleteTraderAccount(userName);					
		}
		catch (Exception e)
		{
			Assert.fail();
		}		
	}
}

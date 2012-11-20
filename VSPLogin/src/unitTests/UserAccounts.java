package unitTests;

import java.util.List;

import junit.framework.Assert;

import org.junit.*;
import org.junit.runner.*;

import vsp.*;
import vsp.dataObject.AccountData;
import vsp.form.validator.FormValidator;
import vsp.form.validator.FormValidatorFactory;
import vsp.form.validator.RegistrationValidator;
import vsp.servlet.form.RegisterForm;
import vsp.utils.Validate;

// These are ordered because "create account" will fail after the initial run unless "delete account" is run.
// In addition, the checks for duplicate user names and email addresses also require the initial account. 
@RunWith(OrderedRunner.class)
public class UserAccounts
{
	private final VspServiceProvider vsp = new VspServiceProvider();
	private final String userName = "unitTestUser";
	private final String password1 = "User1234";
	private final String email = "unitTestUser@unitTestUser.com";
	private final String securityQuestion = "0"; //favorite color
	private final String securityAnswer = "blue";
	private final String secondaryUserName = "unitTestUser2";
	private final String password2 = "UserUser1234";
	private final String secondaryEmail = "unitTestUser2@unitTestUser2.com";
	
	
	@Test
	@Order(order=1)
	public void createAccount()
	{
	  
	  RegisterForm registerForm = new RegisterForm(userName, 
        password1,
        password1,
        email,
        securityQuestion,
        securityAnswer);

		try
		{
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
		  List<String> errors = validator.validate(registerForm);
		  if(errors.isEmpty())
		  {
		    vsp.createTraderAccount(new AccountData(registerForm));
		  }
		  else
		  {
		    for(String error : errors)
		    {
		      Assert.fail(error);
		    }
		  }
		}
		catch (Exception e)
		{
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=2)
	public void denyNewAccountWithDuplicateUserName() throws Exception
	{
	  
	  RegisterForm registerForm = new RegisterForm(userName, 
        password1,
        password1,
        email,
        securityQuestion,
        securityAnswer);

	  try
    {
	    FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
          Assert.assertTrue(errors.contains("User name already in use"));
      }
    }catch(Exception e)
		{
			Assert.assertTrue(e.getLocalizedMessage().contains("in use"));
		}
	}
	
	@Test
	@Order(order=3)
	public void denyNewAccountWithBlankPassword()
	{
	  
	  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
        "",
        "",
        secondaryEmail,
        securityQuestion,
        securityAnswer);
	  try{
	    FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
         Assert.assertTrue(errors.contains("Password must not be empty."));
      }
	  }
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("Blank"));
		}
	}
	
	@Test
	@Order(order=4)
	public void denyNewAccountWithInvalidEmail()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
	        password1,
	        password1,
	        password1,
	        securityQuestion,
	        securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("The email address is invalid."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("invalid"));
		}
	}
	
	@Test
	@Order(order=5)
	public void denyNewAccountWithDuplicateEmail()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
          password1,
          password1,
          email,
          securityQuestion,
          securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("Email Adress already in use."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.getLocalizedMessage().contains("in use"));
		}
	}
	
	@Test
	@Order(order=6)
	public void denyNewAccountWithPasswordLessThan8Characters()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
		      "asdf",
		      "asdf",
		      secondaryEmail,
          securityQuestion,
          securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("The password must contain at least 8 characters."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("8"));
		}
	}
	
	@Test
	@Order(order=7)
	public void denyNewAccountWithPasswordMoreThan64Characters()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
          "asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf",
          "asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf",
          secondaryEmail,
          securityQuestion,
          securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("The password must contain at most 64 characters."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("64"));
		}
	}
	
	@Test
	@Order(order=8)
	public void denyNewAccountWithPasswordMissingUppercaseCharacter()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
          "asdfasd1",
          "asdfasd1",
          secondaryEmail,
          securityQuestion,
          securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("The password must contain at least one uppercase character."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("uppercase"));
		}
	}
	
	@Test
	@Order(order=9)
	public void denyNewAccountWithPasswordMissingLowercaseCharacter()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
          "ASDFASD1",
          "ASDFASD1",
          secondaryEmail,
          securityQuestion,
          securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("The password must contain at least one lowercase character."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("lowercase"));
		}
	}
	
	@Test
	@Order(order=10)
	public void denyNewAccountWithPasswordMissingNumericCharacter()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
          "ASDFASDF",
          "ASDFASDF",
          secondaryEmail,
          securityQuestion,
          securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("The password must contain at least one numeric digit."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("numeric digit"));
		}
	}
	
	@Test
	@Order(order=11)
	public void denyNewAccountWithPasswordMatchingUserName()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
          secondaryUserName,
          secondaryUserName,
          secondaryEmail,
          securityQuestion,
          securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("The password cannot match the user name."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("user name"));
		}
	}
	
	@Test
	@Order(order=12)
	public void denyNewAccountWithoutSecurityQuestion()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
		      password1,
		      password1,
          secondaryEmail,
          "-1",
          securityAnswer);
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
      List<String> errors = validator.validate(registerForm);
      if(errors.isEmpty()){
        vsp.createTraderAccount(new AccountData(registerForm));
        Assert.fail();
      }else{
        Assert.assertTrue(errors.contains("Please select a security question."));
      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("security question"));
		}
	}
	
	@Test
	@Order(order=13)
	public void denyNewAccountWithoutSecurityAnswer()
	{
		try
		{
		  RegisterForm registerForm = new RegisterForm(secondaryUserName, 
          password1,
          password1,
          secondaryEmail,
          securityQuestion,
          "");
		  FormValidator validator = FormValidatorFactory.getRegistrationValidator();
	      List<String> errors = validator.validate(registerForm);
	      if(errors.isEmpty())
	      {
	        vsp.createTraderAccount(new AccountData(registerForm));
	        Assert.fail();
	      }
	      else
	      {
	        Assert.assertTrue(errors.contains("Must answer security question."));
	      }
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("answer"));
		}
	}
	
	@Test
	@Order(order=14)
	public void changePasswordCorrectExisting()
	{
		try
		{
			Assert.assertTrue(vsp.checkUserPassword(userName, password1));
			Assert.assertTrue(vsp.updateUserPassword(userName, password2, password2));
			Assert.assertTrue(vsp.checkUserPassword(userName, password2));
		}
		catch (Exception e)
		{
			// implementation should not throw
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=15)
	public void changePasswordIncorrectExisting()
	{
		try
		{
			if(vsp.checkUserPassword(userName, password1))
			{
				Assert.assertTrue(vsp.updateUserPassword(userName, password1, password1));
			}
			
			// should be the same as one set in previous test
			Assert.assertTrue(vsp.checkUserPassword(userName, password2));
		}
		catch (Exception e)
		{
			// implementation should not throw
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=16)
	public void changePasswordNewOneFailsComplexityRequirements()
	{
		try
		{
			vsp.updateUserPassword(userName, "test", "test");
			// previous call should throw
			Assert.fail();
		}
		catch (Exception e)
		{
			// expected
		}
	}
	
	@Test
	@Order(order=17)
	public void changeEmailAddressWithValidPassword()
	{
		try
		{			
			if(vsp.checkUserPassword(userName, password2))
			{
				vsp.updateUserEmail(userName, secondaryEmail);
			}
			
			Assert.assertTrue(vsp.checkUserEmail(userName, secondaryEmail));
		}
		catch (Exception e)
		{
			// should not throw
			Assert.fail();
		}		
	}
	
	@Test
	@Order(order=18)
	public void changeEmailAddressInvalidPassword()
	{
		try
		{			
			if(vsp.checkUserPassword(userName, password1))
			{
				// password should be invalid
				Assert.fail();
			}			
		}
		catch (Exception e)
		{
			// implementation should not throw
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=19)
	public void changeEmailInvalidEmailFormat()
	{
		try
		{			
			String incorrect_email = "incorrect@email";
			if(Validate.validateEmail(incorrect_email))
			{	
				// email format is invalid
				Assert.fail();
			}
		}
		catch (Exception e)
		{
			// implementation should not throw
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=20)
	public void changeSecurityQuestionAnswerWithValidPassword()
	{
		try
		{			
			if(vsp.checkUserPassword(userName, password2))
			{
				Assert.assertTrue(vsp.updateUserSecurityQuestion(userName, "1", "Tiger"));
			}
			else
			{
				// password should be valid
				Assert.fail();
			}
		}
		catch (Exception e)
		{
			// implementation should not throw
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=21)
	public void changeSecurityQuestionAnswerWithoutAnswer()
	{
		try
		{	
			String answer = "";
			if(Validate.validateSecurityAnswer(answer))
			{
				// security answer should not be valid
				Assert.fail();
			}
			else
			{
				// validate method should throw
				Assert.fail();
			}
		}
		catch (Exception e)
		{
			// validate method should throw
			Assert.assertTrue(e.toString().contains("Please enter an answer for your security"));
		}
	}
	
	@Test
	@Order(order=22)
	public void changeSecurityQuestionAnswerWithInvalidPassword()
	{
		try
		{			
			if(vsp.checkUserPassword(userName, password1))
			{
				// password should not be valid
				Assert.fail();
			}			
		}
		catch (Exception e)
		{
			// password check method does not throw
			Assert.fail();
		}		
	}
	
	@Test
	@Order(order=23)
	public void resetForgottenPassword()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=24)
	public void resetForgottenPasswordWithInvalidSecurityQuestionAnswer()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=25)
	public void resetForgottenPasswordWithInvalidEmailAddress()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=26)
	public void deleteAccount()
	{
		try
		{
			vsp.deleteTraderAccount(userName);
		}
		catch (Exception e)
		{
			Assert.fail();
		}
	}
}

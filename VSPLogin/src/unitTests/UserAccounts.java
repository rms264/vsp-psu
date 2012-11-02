package unitTests;

import org.junit.*;
import org.junit.runner.*;

import vsp.*;

// These are ordered because "create account" will fail after the initial run unless "delete account" is run.
// In addition, the checks for duplicate user names and email addresses also require the initial account. 
@RunWith(OrderedRunner.class)
public class UserAccounts
{
	private final VspServiceProvider vsp = new VspServiceProvider();
	private final String userName = "unitTestUser";
	private final String secondaryUserName = "unitTestUser2";
	private final String password = "User1234";
	private final String password2 = "UserUser1234";
	private final String email = "unitTestUser@unitTestUser.com";
	private final String secondaryEmail = "unitTestUser2@unitTestUser2.com";
	private final String securityQuestion = "0"; // favorite color
	private final String securityAnswer = "blue";

	@Test
	@Order(order=1)
	public void createAccount()
	{
		try
		{
			vsp.createTraderAccount(userName, password, password, email, securityQuestion, securityAnswer);
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
		try
		{
			vsp.createTraderAccount(userName, password, password, email, securityQuestion, securityAnswer);
			Assert.fail();
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.getLocalizedMessage().contains("in use"));
		}
	}
	
	@Test
	@Order(order=3)
	public void denyNewAccountWithBlankPassword()
	{
		try
		{
			vsp.createTraderAccount(secondaryUserName, "", "", secondaryEmail, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, password, password, password, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, password, password, email, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, "asdf", "asdf", secondaryEmail, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, 
					"asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf", 
					"asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf", 
					secondaryEmail, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, 
					"asdfasd1", 
					"asdfasd1", 
					secondaryEmail, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, 
					"ASDFASD1", 
					"ASDFASD1", 
					secondaryEmail, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, 
					"ASDFASDF", 
					"ASDFASDF", 
					secondaryEmail, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, secondaryUserName, secondaryUserName, secondaryEmail, securityQuestion, securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, password, password, secondaryEmail, "-1", securityAnswer);
			Assert.fail();
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
			vsp.createTraderAccount(secondaryUserName, password, password, secondaryEmail, securityQuestion, "");
			Assert.fail();
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
			Assert.assertTrue(vsp.checkUserPassword(userName, password));
			Assert.assertTrue(vsp.updateUserPassword(userName, password2, password2));
			Assert.assertTrue(vsp.checkUserPassword(userName, password2));
		}
		catch (Exception e)
		{
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=15)
	public void changePasswordIncorrectExisting()
	{
		try
		{
			if(vsp.checkUserPassword(userName, password))
			{
				Assert.assertTrue(vsp.updateUserPassword(userName, password, password));
			}
			
			// should be the same as one set in previous test
			Assert.assertTrue(vsp.checkUserPassword(userName, password2));
		}
		catch (Exception e)
		{
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
		Assert.fail();
	}
	
	@Test
	@Order(order=18)
	public void changeEmailAddressInvalidPassword()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=19)
	public void changeEmailInvalidEmailFormat()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=20)
	public void changeSecurityQuestionAnswerWithValidPassword()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=21)
	public void changeSecurityQuestionAnswerWithoutAnswer()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=22)
	public void changeSecurityQuestionAnswerWithInvalidPassword()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=23)
	public void recoverForgottenPassword()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=24)
	public void recoverForgottenPasswordWithInvalidSecurityQuestionAnswer()
	{
		Assert.fail();
	}
	
	@Test
	@Order(order=25)
	public void recoverForgottenPasswordWithInvalidEmailAddress()
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

package unitTests;

import org.junit.*;
import org.junit.runner.*;

import vsp.*;

// these are ordered because "create account" will always fail after the initial run unless "delete account" is also run
@RunWith(OrderedRunner.class)
public class userAccounts
{
	private final VspServiceProvider vsp = new VspServiceProvider();
	private final String userName = "unitTestUser";
	private final String secondaryUserName = "unitTestUser2";
	private final String password = "User1234";
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
			vsp.createAccount(userName, password, password, email, securityQuestion, securityAnswer);
		}
		catch (Exception e)
		{
			Assert.fail();
		}
	}
	
	@Test
	@Order(order=2)
	public void denyNewAccountWithDuplicateUserName()
	{
		try
		{
			vsp.createAccount(userName, password, password, email, securityQuestion, securityAnswer);
			Assert.fail();
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("in use"));
		}
	}
	
	@Test
	@Order(order=3)
	public void denyNewAccountWithBlankPassword()
	{
		try
		{
			vsp.createAccount(secondaryUserName, "", "", secondaryEmail, securityQuestion, securityAnswer);
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
			vsp.createAccount(secondaryUserName, password, password, password, securityQuestion, securityAnswer);
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
			vsp.createAccount(secondaryUserName, password, password, email, securityQuestion, securityAnswer);
			Assert.fail();
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("in use"));
		}
	}
	
	@Test
	@Order(order=6)
	public void denyNewAccountWithPasswordLessThan8Characters()
	{
		try
		{
			vsp.createAccount(secondaryUserName, "asdf", "asdf", secondaryEmail, securityQuestion, securityAnswer);
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
			vsp.createAccount(secondaryUserName, 
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
			vsp.createAccount(secondaryUserName, 
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
			vsp.createAccount(secondaryUserName, 
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
			vsp.createAccount(secondaryUserName, 
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
			vsp.createAccount(secondaryUserName, secondaryUserName, secondaryUserName, secondaryEmail, securityQuestion, securityAnswer);
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
			vsp.createAccount(secondaryUserName, password, password, secondaryEmail, "-1", securityAnswer);
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
			vsp.createAccount(secondaryUserName, password, password, secondaryEmail, securityQuestion, "");
			Assert.fail();
		}
		catch (Exception e)
		{
			Assert.assertTrue(e.toString().contains("answer"));
		}
	}
	
	@Test
	@Order(order=14)
	public void deleteAccount()
	{
		try
		{
			vsp.deleteAccount(userName);
		}
		catch (Exception e)
		{
			Assert.fail();
		}
	}
}

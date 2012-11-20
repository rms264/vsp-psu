package unitTests;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import vsp.VspServiceProvider;
import vsp.utils.Validate;

@RunWith(OrderedRunner.class)
public class AdministrativeFunction
{
	private final VspServiceProvider vsp = new VspServiceProvider();
	
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
			Assert.assertTrue(traders.contains("test"));			
			Assert.assertTrue(Validate.validatePassword("test", "Test123", "Test123"));
			Assert.assertTrue(vsp.updateUserPassword("test", "Test123", "Test123"));
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
			Assert.assertTrue(traders.contains("test"));			
			Assert.assertTrue(Validate.validatePassword("test", "Test12345", "Test12345"));
			Assert.assertTrue(vsp.updateUserPassword("test", "Test12345", "Test12345"));			
		}
		catch (Exception e)
		{
			Assert.fail();
		}		
	}
	
	@Test
	@Order(order=4)
	public void resetPasswordValid()
	{
		try
		{
			Assert.assertTrue(vsp.updateUserPassword("test", "Test1234", "Test1234"));			
		}
		catch (Exception e)
		{
			Assert.fail();
		}		
	}
}

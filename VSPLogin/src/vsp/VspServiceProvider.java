package vsp;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import vsp.dal.requests.Roles;
import vsp.dal.requests.Users;
import vsp.dataObject.AccountData;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Enumeration.Role;


public class VspServiceProvider
{
	public VspServiceProvider()
	{
		// no implementation required
	}
	
	public void createAccount(String userName, String password1, 
			String password2, String email, String question, String answer) 
			throws ValidationException, SQLException, SqlRequestException 
	{
		Users.addUserAccount(userName, email, password1, password2, question, 
				answer);
		Roles.addNewUserRole(userName, Role.TRADER);
	}
	
	public void deleteAccount(String userName) throws ValidationException,
		SQLException, SqlRequestException
	{
		Users.deleteUserAccount(userName);
		Roles.deleteUserRole(userName);
	}
	
	public AccountData getAccountInfo(String userName) throws SQLException
	{
		AccountData data = Users.requestAccountData(userName);	
		return data;
	}
	
	public List<String> getTraders() throws SQLException
	{
		return Users.queryAllTraders();
	}
}

package vsp;

import java.sql.SQLException;
import java.util.List;

import vsp.dal.requests.CreateAccount;
import vsp.dal.requests.DeleteAccount;
import vsp.dal.requests.UserInfo;
import vsp.dataObject.AccountData;
import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;


public class VspServiceProvider
{
	public VspServiceProvider(){}// no implementation required (for now)
	
	public void createAccount(String userName, String password1, 
			String password2, String email, String question, String answer) 
			throws ValidationException, SQLException, SqlRequestException 
	{
		CreateAccount.submit(userName, email, password1, password2, question, 
				answer);
	}
	
	public void deleteAccount(String userName) throws ValidationException,
		SQLException, SqlRequestException
	{
		DeleteAccount.submit(userName);
	}
	
	public AccountData getAccountInfo(String userName) throws SQLException
	{
		
		AccountData data = UserInfo.requestAccountData(userName);
		
		return data;
	}
	
	public List<String> getTraders() throws SQLException
	{
		return UserInfo.queryAllTraders();
	}
}

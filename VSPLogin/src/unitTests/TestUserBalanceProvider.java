package unitTests;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import vsp.dataObject.IUserBalance;
import vsp.dataObject.StockInfo;
import vsp.exception.SqlRequestException;

public class TestUserBalanceProvider implements IUserBalance
{
	private double balance;
	
	public TestUserBalanceProvider()
	{
		// no implementation required
	}
		
	@Override
	public double getBalance(String userName)
	{
		return this.balance;
	}

	@Override
	public void updateBalance(String userName, double balance)
			throws SQLException, SqlRequestException
	{
		this.balance = balance;
	}
}

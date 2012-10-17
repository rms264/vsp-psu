package vsp.dataObject;

import java.sql.SQLException;

import vsp.exception.SqlRequestException;

public interface IUserBalance
{
	double getBalance(String userName);
	void updateBalance(String userName, double balance) throws SQLException, SqlRequestException;
}

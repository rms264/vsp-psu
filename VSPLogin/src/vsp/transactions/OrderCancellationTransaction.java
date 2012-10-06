package vsp.transactions;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import vsp.NameValuePair;
import vsp.dataObject.Stock;
import vsp.orders.Order;
import vsp.utils.Enumeration.TransactionType;

final class OrderCancellationTransaction extends StockTransaction
{
	private final Order order;
	
	OrderCancellationTransaction(String userName, String id, Stock stock, Date dateTime, Order order)
	{
		super(userName, id, stock, dateTime, 0.0, 0.0, 0.0f);
		this.order = order;
	}
	
	@Override
	public List<NameValuePair> getNameValuePairs()
	{
		// TODO: implement
		
		return null;		
	}
	
	@Override
	public void prepareInsertStatement(PreparedStatement statement) throws SQLException
	{
		statement.setString(1, this.getId());
		statement.setInt(2,  TransactionType.CANCELLATION.getValue());
		statement.setString(3, this.order.getId());
		statement.setString(4, this.getStock().getStockSymbol());
		statement.setDate(5, new java.sql.Date(this.getDateTime().getTime()));
		statement.setFloat(6, 0.0f);
		statement.setDouble(7, 0.0f);
		statement.setDouble(8, 0.0f);
		statement.setString(9, this.getUserName());
	}
}

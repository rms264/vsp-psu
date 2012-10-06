package vsp.orders;

import java.util.Date;

import vsp.dataObject.Stock;
import vsp.utils.Enumeration.*;

public final class Order
{
	private final String id;
	private final String userName;
	private final Stock stock;
	private final OrderAction action;
	private final float quantity;
	private final OrderType type;
	private final double limitPrice;
	private final double stopPrice;
	private final TimeInForce timeInForce;
	private OrderState state;
	private final Date dateSubmitted;
	
	public Order(String id, String userName, Stock stock, OrderAction action, 
			float quantity, OrderType type, double limitPrice, double stopPrice,
			TimeInForce timeInForce, OrderState state, Date dateSubmitted)
	{
		this.id = id;
		this.userName = userName;
		this.stock = stock;
		this.action = action;
		this.quantity = quantity;
		this.type = type;
		this.limitPrice = limitPrice;
		this.stopPrice = stopPrice;
		this.timeInForce = timeInForce;
		this.state = state;
		this.dateSubmitted = dateSubmitted;
	}
	
	public OrderAction getAction()
	{
		return this.action;
	}
	
	public Date getDateSubmitted()
	{
		return this.dateSubmitted;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public double getLimitPrice()
	{
		return this.limitPrice;
	}
	
	public float getQuantity()
	{
		return this.quantity;
	}
	
	public OrderState getState()
	{
		return this.state;
	}
	
	public Stock getStock()
	{
		return this.stock;
	}
	
	public double getStopPrice()
	{
		return this.stopPrice;
	}
	
	public TimeInForce getTimeInForce()
	{
		return this.timeInForce;
	}
	
	public OrderType getType()
	{
		return this.type;
	}
	
	public String getUserName()
	{
		return this.userName;
	}
	
	public void setState(OrderState state)
	{
		this.state = state;
	}
}

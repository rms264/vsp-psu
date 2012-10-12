package vsp.orders;

import java.util.Date;
import java.util.UUID;

import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;
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
	
	public static String CreateId()
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	public OrderAction getAction()
	{
		return this.action;
	}
	
	// estimation leans toward a worst case scenario
	public double getLatestEstimatedValue(StockInfo info)
	{
		double estimatedCost = 0.0;
		if (this.state == OrderState.PENDING)
		{
			if (this.type == OrderType.LIMIT || this.type == OrderType.STOPLIMIT)
			{
				estimatedCost = this.quantity * this.limitPrice;
			}
			else if (this.type == OrderType.STOP)
			{
				estimatedCost = this.quantity * this.stopPrice;
			}
			else if (this.type == OrderType.MARKET)
			{
				if (this.action == OrderAction.BUY)
				{
					estimatedCost = this.quantity * info.getAsk();
				}
				else // SELL
				{
					estimatedCost = this.quantity * info.getBid();
				}				
			}
			
			if (this.action == OrderAction.BUY)
			{
				estimatedCost *= -1;
			}
		}
		
		return estimatedCost;
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

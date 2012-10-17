package vsp.dataObject;

import java.util.Date;

public final class OrderResult
{
	private final Order order;
	private final boolean completed;
	private final float quantity;
	private final double sharePrice;
	private final Date dateTime;
	
	public OrderResult(Order order, boolean completed, float quantity, 
			double sharePrice, Date dateTime)
	{
		this.order = order;
		this.completed = completed;
		this.quantity = quantity;
		this.sharePrice = sharePrice;
		this.dateTime = dateTime;
	}
	
	public boolean getCompleted()
	{
		return this.completed;
	}
	
	public Date getDateTime()
	{
		return this.dateTime;
	}
	
	public Order getOrder()
	{
		return this.order;
	}
	
	public float getQuantity()
	{
		return this.quantity;
	}
	
	public double getSharePrice()
	{
		return this.sharePrice;
	}
}

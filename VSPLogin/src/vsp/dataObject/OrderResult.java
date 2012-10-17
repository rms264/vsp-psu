package vsp.dataObject;

import java.util.Date;

public final class OrderResult
{
	private final Order order;
	private boolean completed;
	private boolean cancelled;
	private float quantity;
	private double sharePrice;
	private Date dateTime;
	
	public OrderResult(Order order)
	{
		this.order = order;
	}
	
	public OrderResult(Order order, boolean completed, boolean cancelled, float quantity, 
			double sharePrice, Date dateTime)
	{
		this.order = order;
		this.completed = completed;
		this.quantity = quantity;
		this.sharePrice = sharePrice;
		this.dateTime = dateTime;
	}
	
	public boolean getCancelled()
	{
		return this.cancelled;
	}
	
	void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
	
	public boolean getCompleted()
	{
		return this.completed;
	}
	
	void setCompleted(boolean completed)
	{
		this.completed = completed;
	}
	
	public Date getDateTime()
	{
		return this.dateTime;
	}
	
	void setDateTime(Date dateTime)
	{
		this.dateTime = dateTime;
	}
	
	public Order getOrder()
	{
		return this.order;
	}
	
	public float getQuantity()
	{
		return this.quantity;
	}
	
	void setQuantity(float quantity)
	{
		this.quantity = quantity;
	}
	
	public double getSharePrice()
	{
		return this.sharePrice;
	}
	
	void setSharePrice(double sharePrice)
	{
		this.sharePrice = sharePrice;
	}
	
	public double getValue()
	{
		return this.quantity * this.sharePrice;
	}
}

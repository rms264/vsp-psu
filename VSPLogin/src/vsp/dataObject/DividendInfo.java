package vsp.dataObject;

import java.util.Date;

public final class DividendInfo
{
	private final double dividend;
	private final Date date;
	
	public DividendInfo(Date date, double dividend)
	{
		this.date = date;
		this.dividend = dividend;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	public double getDividend()
	{
		return this.dividend;
	}
}

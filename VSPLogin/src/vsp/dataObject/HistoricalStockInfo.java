package vsp.dataObject;

import java.util.Date;

public final class HistoricalStockInfo
{
	private final Date date;
	private final double dayLow;
	private final double dayHigh;
	private final double open;
	private final double close;
	private final double adjustedClose;
	private final int volume;

	public HistoricalStockInfo(Date date, double open, double dayLow, double dayHigh, double close, int volume, double adjustedClose)
	{
		this.date = date;
		this.open = open;
		this.dayLow = dayLow;
		this.dayHigh = dayHigh;
		this.close = close;
		this.adjustedClose = adjustedClose;
		this.volume = volume;
	}
	
	public double getAdjustedClose()
	{
		return this.adjustedClose;
	}
	
	public double getClose()
	{
		return this.close;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	public double getDayHigh()
	{
		return this.dayHigh;
	}
	
	public double getDayLow()
	{
		return this.dayLow;
	}
	
	public double getOpen()
	{
		return this.open;
	}
	
	public int getVolume()
	{
		return this.volume;
	}
}

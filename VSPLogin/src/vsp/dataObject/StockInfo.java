package vsp.dataObject;

import java.util.Date;

public final class StockInfo
{
	private final String symbol;
	private final String description;
	private final double dayHigh;
	private final double dayLow;
	private final Date exdividendDate;
	private final double exdividend;
	private final double priceChangeSinceOpen;
	private final double percentChangeSinceOpen;
	private final int volume;
	private final double bid;
	private final double ask;
	private final double open;
	
	public StockInfo(String symbol, String description, double dayHigh, double dayLow, 
			Date exdividendDate, double exdividend, double priceChangeSinceOpen, 
			double percentChangeSinceOpen, int volume, double bid, double open, double ask)
	{
		this.symbol = symbol;
		this.description = description;
		this.dayHigh = dayHigh;
		this.dayLow = dayLow;
		this.exdividendDate = exdividendDate;
		this.exdividend = exdividend;
		this.priceChangeSinceOpen = priceChangeSinceOpen;
		this.percentChangeSinceOpen = percentChangeSinceOpen;
		this.volume = volume;
		this.bid = bid;
		this.ask = ask;
		this.open = open;
	}
	
	public double getAsk()
	{
		return this.ask;
	}
	
	public double getBid()
	{
		return this.bid;
	}
	
	public double getDayHigh()
	{
		return this.dayHigh;
	}
	
	public double getDayLow()
	{
		return this.dayLow;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public double getExdividend()
	{
		return this.exdividend;
	}
	
	public Date getExdividendDate()
	{
		return this.exdividendDate;
	}
	
	public double getOpen()
	{
		return this.open;
	}
	
	public double getPercentChangeSinceOpen()
	{
		return this.percentChangeSinceOpen;
	}
	
	public double getPriceChanceSinceOpen()
	{
		return this.priceChangeSinceOpen;
	}
	
	public String getSymbol()
	{
		return this.symbol;
	}
	
	public int getVolume()
	{
		return this.volume;
	}
}

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
	private final double lastTradePrice;
	private final int volume;
	private final double bid;
	private final double ask;
	private final double open;
	private final double close;
	
	public StockInfo(String symbol, String description, double dayHigh, double dayLow, 
			Date exdividendDate, double exdividend, double priceChangeSinceOpen, 
			double percentChangeSinceOpen, int volume, double bid, double open, double ask, 
			double lastTradePrice, double close)
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
		this.lastTradePrice = lastTradePrice;
		this.close = close;
	}
	
	public double getAsk()
	{
		return this.ask;
	}
	
	public double getBid()
	{
		return this.bid;
	}
	
	public double getChangeSinceClosePrice()
	{
		return (this.lastTradePrice - this.close);
	}
	
	public double getChangeSinceClosePercent()
	{
		return (this.getChangeSinceClosePrice() / this.close) * 100.0;
	}
	
	public double getClose()
	{
		return this.close;
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
	
	public double getLastTradePrice()
	{
		return this.lastTradePrice;
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
	
	public Stock getStock()
	{
		return new Stock(this.symbol, this.description);
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

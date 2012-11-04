package vsp.dataObject;

import java.io.Serializable;
import java.util.Date;

public final class StockInfo implements Serializable
{
  private static final long serialVersionUID = -207704694786742191L;
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

  public String getSymbol() {
    return symbol;
  }

  public String getDescription() {
    return description;
  }

  public double getDayHigh() {
    return dayHigh;
  }

  public double getDayLow() {
    return dayLow;
  }

  public Date getExdividendDate() {
    return exdividendDate;
  }

  public double getExdividend() {
    return exdividend;
  }

  public double getPriceChangeSinceOpen() {
    return priceChangeSinceOpen;
  }

  public double getPercentChangeSinceOpen() {
    return percentChangeSinceOpen;
  }

  public double getLastTradePrice() {
    return lastTradePrice;
  }

  public int getVolume() {
    return volume;
  }

  public double getBid() {
    return bid;
  }

  public double getAsk() {
    return ask;
  }

  public double getOpen() {
    return open;
  }

  public double getClose() {
    return close;
  }
	
  public double getChangeSinceClosePrice()
  {
    return (this.lastTradePrice - this.close);
  }
  
  public double getChangeSinceClosePercent()
  {
    return (this.getChangeSinceClosePrice() / this.close) * 100.0;
  }
  public Stock getStock()
  {
  return new Stock(this.symbol, this.description);
  }
}

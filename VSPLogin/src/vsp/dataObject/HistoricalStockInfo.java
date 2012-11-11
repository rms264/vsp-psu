package vsp.dataObject;

import java.util.Date;

public final class HistoricalStockInfo
{
	private Date date;
	private double dayLow;
	private double dayHigh;
	private double open;
	private double close;
	private double adjustedClose;
	private int volume;

	public HistoricalStockInfo(){
	  
	}
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

  @Override
  public String toString() {
    return "HistoricalStockInfo [date=" + date + ", dayLow=" + dayLow
        + ", dayHigh=" + dayHigh + ", open=" + open + ", close=" + close
        + ", adjustedClose=" + adjustedClose + ", volume=" + volume + "]";
  }
  
  public String toWriter() {
    return date + "," + dayLow + "," + dayHigh + "," + open + "," + close
        + "," + adjustedClose + "," + volume;
  }
  public void setDate(Date date) {
    this.date = date;
  }
  public void setDayLow(double dayLow) {
    this.dayLow = dayLow;
  }
  public void setDayHigh(double dayHigh) {
    this.dayHigh = dayHigh;
  }
  public void setOpen(double open) {
    this.open = open;
  }
  public void setClose(double close) {
    this.close = close;
  }
  public void setAdjustedClose(double adjustedClose) {
    this.adjustedClose = adjustedClose;
  }
  public void setVolume(int volume) {
    this.volume = volume;
  }
}

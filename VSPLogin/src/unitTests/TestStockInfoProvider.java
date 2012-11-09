package unitTests;

import java.util.Date;
import java.util.List;

import vsp.StockInfoServiceProvider;
import vsp.dataObject.DividendInfo;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.IStockInfo;
import vsp.dataObject.StockInfo;

class TestStockInfoProvider implements IStockInfo
{
	private boolean controlTradingHours;
	private boolean isWithinTradingHours;
	
	private boolean controlCurrentStockData;
	private StockInfo currentStockData;
	
	private boolean controlCurrentStockDataList;
	private List<StockInfo> currentStockDataList;
	
	private boolean controlHistoricalStockDataForDay;
	private HistoricalStockInfo historicalStockInfoForDay;
	
	private boolean controlHistoricalStockData;
	private List<HistoricalStockInfo> historicalStockInfo;
	
	private boolean controlDividendData;
	private List<DividendInfo> dividendData;
	
	
	private IStockInfo wrapped = new StockInfoServiceProvider();
	
	public TestStockInfoProvider()
	{
		// no implementation required
	}
	
	public void setTradingHours(boolean isWithinTradingHours)
	{
		this.controlTradingHours = true;
		this.isWithinTradingHours = isWithinTradingHours;	
	}
	
	@Override
	public boolean isWithinTradingHours()
	{
		if (this.controlTradingHours)
		{
				return this.isWithinTradingHours;
		}
	
		return this.wrapped.isWithinTradingHours();
	}
	
	public void setCurrentStockData(StockInfo currentStockData)
	{
		this.controlCurrentStockData = true;
		this.currentStockData = currentStockData;	
	}
	
	@Override
	public StockInfo requestCurrentStockData(String symbol)
	{
		if (this.controlCurrentStockData)
		{
				return this.currentStockData;
		}
	
		return this.wrapped.requestCurrentStockData(symbol);
	}
	
	public void setCurrentStockDataList(List<StockInfo> currentStockDataList)
	{
		this.controlCurrentStockDataList = true;
		this.currentStockDataList = currentStockDataList;	
	}
	
	@Override
	public List<StockInfo> requestCurrentStockData(List<String> symbols)
	{
		if (this.controlCurrentStockDataList)
		{
				return this.currentStockDataList;
		}
	
		return this.wrapped.requestCurrentStockData(symbols);
	}
	
	public void setHistoricalStockDataForDay(HistoricalStockInfo historicalStockInfoForDay)
	{
		this.controlHistoricalStockDataForDay = true;
		this.historicalStockInfoForDay = historicalStockInfoForDay;	
	}
	
	@Override
	public HistoricalStockInfo requestHistoricalStockDataForDay(String symbol, Date day)
	{
		if (this.controlHistoricalStockDataForDay)
		{
			return this.historicalStockInfoForDay;
		}
	
		return this.wrapped.requestHistoricalStockDataForDay(symbol, day);
	}
	
	public void setHistoricalStockData(List<HistoricalStockInfo> historicalStockInfo)
	{
		this.controlHistoricalStockData = true;
		this.historicalStockInfo = historicalStockInfo;	
	}
	
	@Override
	public List<HistoricalStockInfo> requestDailyHistoricalStockData(String symbol, Date since)
	{
		if (this.controlHistoricalStockData)
		{
			return this.historicalStockInfo;
		}
	
		return this.wrapped.requestDailyHistoricalStockData(symbol, since);
	}
	
	public void setDividendData(List<DividendInfo> dividendData)
	{
		this.controlDividendData = true;
		this.dividendData = dividendData;	
	}
	
	@Override
	public List<DividendInfo> requestHistoricalDividendInfoSince(String symbol, Date since)
	{
		if (this.controlDividendData)
		{
			return this.dividendData;
		}
	
		return this.wrapped.requestHistoricalDividendInfoSince(symbol, since);
	}

  @Override
  public List<HistoricalStockInfo> requestWeeklyHistoricalStockData(
      String symbol, Date since) {
    if (this.controlHistoricalStockData)
    {
      return this.historicalStockInfo;
    }
  
    return this.wrapped.requestWeeklyHistoricalStockData(symbol, since);
  }

  @Override
  public List<HistoricalStockInfo> requestMonthlyHistoricalStockData(
      String symbol, Date since) {
    if (this.controlHistoricalStockData)
    {
      return this.historicalStockInfo;
    }
  
    return this.wrapped.requestMonthlyHistoricalStockData(symbol, since);
  }

}
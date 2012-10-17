package vsp.dataObject;

import java.util.Date;
import java.util.List;

public interface IStockInfo
{
	public boolean isWithinTradingHours();
	public StockInfo requestCurrentStockData(String symbol);
	public HistoricalStockInfo requestHistoricalStockDataForDay(String symbol, Date day);
	public List<HistoricalStockInfo> requestDailyHistoricalStockData(String symbol, Date since);
}

package vsp.dataObject;

import java.util.Date;
import java.util.List;

public interface IStockInfo
{
	public boolean isWithinTradingHours();
	public StockInfo requestCurrentStockData(String symbol);
	public List<StockInfo> requestCurrentStockData(List<String> symbols);
	public HistoricalStockInfo requestHistoricalStockDataForDay(String symbol, Date day);
	public List<HistoricalStockInfo> requestDailyHistoricalStockData(String symbol, Date since);
	public List<HistoricalStockInfo> requestWeeklyHistoricalStockData(String symbol, Date since);
	public List<HistoricalStockInfo> requestMonthlyHistoricalStockData(String symbol, Date since);
	public List<DividendInfo> requestHistoricalDividendInfoSince(String symbol, Date since);
}

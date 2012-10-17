package vsp.dataObject;

import java.util.Date;
import java.util.List;

public interface IStockInfo
{
	public boolean isWithinTradingHours();
	public StockInfo requestCurrentStockData(String symbol);
	public List<HistoricalStockInfo> requestDailyHistoricalStockData(String symbol, Date since);
}

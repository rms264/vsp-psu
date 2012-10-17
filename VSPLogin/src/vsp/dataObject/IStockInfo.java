package vsp.dataObject;

import java.util.Date;
import java.util.List;

public interface IStockInfo
{
	public StockInfo getLatest(String symbol);
	public List<HistoricalStockInfo> getHistoricalStockInfo(String symbol, Date since);
}

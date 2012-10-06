package vsp;

import java.util.List;

import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.StockInfo;

public final class StockInfoServiceProvider
{
	public StockInfoServiceProvider()
	{
		// no implementation required
	}
	
	public StockInfo requestCurrentStockData(String symbol)
	{
		// TODO: implement
		return null;
	}
	
	public List<StockInfo> requestCurrentStockData(List<String> symbols)
	{
		// TODO: implement
		return null;
	}
	
	public HistoricalStockInfo requestHistoricalStockData(String symbol, int months)
	{
		// TODO: implement
		return null;
	}
}

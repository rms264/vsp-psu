package vsp.statistics;

import vsp.dataObject.HistoricalStockInfo;

public abstract class StockStatistic
{
	public StockStatistic()
	{
		// no implementation required
	}
	
	public abstract double Calculate(HistoricalStockInfo data);
}

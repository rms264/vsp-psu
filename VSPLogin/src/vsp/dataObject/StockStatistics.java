package vsp.dataObject;

public final class StockStatistics
{
	private double returnOnInvestment;
	private double compoundRateOfReturn;
	private double compoundAnnualGrowthRate;
	private double averageRateOfReturn;
	private double standardDeviation;
	
	public StockStatistics()
	{
		// no implementation required
	}
	
	public void calculate(HistoricalStockInfo data)
	{
		// TODO: implement
	}

	public double getAverageRateOfReturn()
	{
		return this.averageRateOfReturn;
	}
	
	public double getCompoundAnnualGrowthRate()
	{
		return this.compoundAnnualGrowthRate;
	}
	
	public double getCompouandRateOfReturn()
	{
		return this.compoundRateOfReturn;
	}
	
	public double getReturnOnInvestment()
	{
		return this.returnOnInvestment;
	}
	
	public double getStandardDeviation()
	{
		return this.standardDeviation;
	}
}

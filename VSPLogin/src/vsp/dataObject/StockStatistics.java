package vsp.dataObject;

public final class StockStatistics
{
	private final double returnOnInvestment;
	private final double compoundRateOfReturn;
	private final double compoundAnnualGrowthRate;
	private final double averageRateOfReturn;
	private final double standardDeviation;
	
	private StockStatistics(double returnOnInvestment, double compoundRateOfReturn,
			double compoundAnnualGrowthRate, double averageRateOfReturn,
			double standardDeviation)
	{
		this.returnOnInvestment = returnOnInvestment;
		this.compoundRateOfReturn = compoundRateOfReturn;
		this.compoundAnnualGrowthRate = compoundAnnualGrowthRate;
		this.averageRateOfReturn = averageRateOfReturn;
		this.standardDeviation = standardDeviation;
	}
	
	public static StockStatistics Calculate(HistoricalStockInfo data)
	{
		// TODO: implement
		return null;
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

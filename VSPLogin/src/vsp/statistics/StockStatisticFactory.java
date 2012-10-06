package vsp.statistics;

public final class StockStatisticFactory
{
	private StockStatisticFactory() {}
	
	public static StockStatistic CreateAverageRateOfReturn()
	{
		return new AverageRateOfReturnStatistic();
	}
	
	public static StockStatistic CreateCompoundAnnualGrowthRate()
	{
		return new CompoundAnnualGrowthRateStatistic();
	}
	
	public static StockStatistic CreateCompoundRateOfReturn()
	{
		return new CompoundRateOfReturnStatistic();
	}
	
	public static StockStatistic CreateReturnOnInvestment()
	{
		return new ReturnOnInvestmentStatistic();
	}
	
	public static StockStatistic CreateStandardDeviation()
	{
		return new StandardDeviationStatistic();
	}
}

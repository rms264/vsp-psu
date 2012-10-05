package vsp.dataObject;

public class Stock
{
	private final String stockSymbol;
	private final String stockDescription;
	/**
	 * @param stockSymbol
	 * @param stockDescription
	 */
	public Stock(String stockSymbol, String stockDescription) {
		super();
		this.stockSymbol = stockSymbol;
		this.stockDescription = stockDescription;
	}
	/**
	 * @return the stockSymbol
	 */
	public String getStockSymbol() {
		return stockSymbol;
	}
	/**
	 * @return the stockDescription
	 */
	public String getStockDescription() {
		return stockDescription;
	}
	
}

package vsp.dataObject;

import java.util.Currency;
import java.util.Date;

public final class PortfolioData
{
	private final Stock stock;
	private final Date purchaseDate;
	private final double purchasePrice;
	private float	quantitiy;
	/**
	 * @param stock
	 * @param purchaseDate
	 * @param purchasePrice
	 * @param quantitiy
	 */
	public PortfolioData(Stock stock, Date purchaseDate,
			double purchasePrice, float quantitiy) {
		super();
		this.stock = stock;
		this.purchaseDate = purchaseDate;
		this.purchasePrice = purchasePrice;
		this.quantitiy = quantitiy;
	}
	/**
	 * @return the quantitiy
	 */
	public float getQuantitiy() {
		return quantitiy;
	}
	/**
	 * @param quantitiy the quantitiy to set
	 */
	public void setQuantitiy(int quantitiy) {
		this.quantitiy = quantitiy;
	}
	/**
	 * @return the stock
	 */
	public Stock getStock() {
		return stock;
	}
	/**
	 * @return the purchaseDate
	 */
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	/**
	 * @return the purchasePrice
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}
}

package vsp.dataObject;

import java.util.Currency;
import java.util.Date;

public final class PortfolioData
{
	private final Stock stock;
	private final double purchasePrice;
	private final double costBasisPerShare;
	private float	quantity;
	/**
	 * @param stock
	 * @param purchaseDate
	 * @param purchasePrice
	 * @param costBasisPerShare
	 * @param quantitiy
	 */
	public PortfolioData(Stock stock, double purchasePrice, 
			double costBasisPerShare, float quantity)
	{
		super();
		this.stock = stock;
		this.purchasePrice = purchasePrice;
		this.costBasisPerShare = costBasisPerShare;
		this.quantity = quantity;
	}
	/**
	 * @return the quantity
	 */
	public float getQuantity()
	{
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	/**
	 * @return the stock
	 */
	public Stock getStock()
	{
		return stock;
	}
	/**
	 * @return the purchasePrice
	 */
	public double getPurchasePrice()
	{
		return purchasePrice;
	}
	/**
	 * @return the costBasisPerShare
	 */
	public double getCostBasisPerShare()
	{
		return costBasisPerShare;
	}
}

package vsp.dataObject;

public final class PortfolioData
{
	private final Stock stock;
	private double costBasisPerShare;
	private float	quantity;
	private final String userName;
	/**
	 * @param stock
	 * @param purchaseDate
	 * @param purchasePrice
	 * @param costBasisPerShare
	 * @param quantitiy
	 */
	public PortfolioData(Stock stock, double costBasisPerShare, 
			float quantity, String userName)
	{
		super();
		this.stock = stock;
		this.costBasisPerShare = costBasisPerShare;
		this.quantity = quantity;
		this.userName = userName;
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
	 * @return the costBasis
	 */
	public double getCostBasis()
	{
		return this.quantity * this.costBasisPerShare;
	}
	/**
	 * @return the costBasisPerShare
	 */
	public double getCostBasisPerShare()
	{
		return costBasisPerShare;
	}
	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return this.userName;
	}
}

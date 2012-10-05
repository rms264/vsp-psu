package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.Stock;

public class PortfolioEntries {

	/**
	 * @param userName
	 * @return This will return all portfolio entries from the PortfolioEntry  
	 * 		   table for the user that matches the user name passed in.
	 * @throws SQLException
	 */
	public List<PortfolioData> requestAllUserStocks(String userName) throws SQLException{
		List<PortfolioData> results = new ArrayList<PortfolioData>();
		PortfolioEntries request = new PortfolioEntries();
		request.submitUserStockRequest(userName);
		return results;
	}
	
	public static boolean addEntry(String userName, PortfolioData data){
		//TODO: Implement adding a new portfolio entry
		
		return true;
	}
	
	public static boolean deleteEntry(){
		//TODO: Implement deleting a portfolio entry
		return true;
	}
	
	public static boolean deleteAllEntriesFor(String userName){
		//TODO: Implement deleting all portfolio entries for a user
		return true;
	}
	
	private PortfolioEntries(){
	}
	
	private List<PortfolioData> submitUserStockRequest(String userName) throws SQLException{
		List<PortfolioData> results = new ArrayList<PortfolioData>();

		Connection connection = null;
		try
		{
			String sqlStatement = 
					"SELECT * FROM PortfolioEntry WHERE user_name=?";
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, userName);  
			ResultSet rs = pStmt.executeQuery();
			
			while(rs.next()){
				String stock = rs.getString("stock_symbol");
				List<Stock> stockInfo = Stocks.getStock(stock);
				if(!stockInfo.isEmpty()){
					results.add(new PortfolioData(stockInfo.get(0),
							rs.getDate("purchase_date"),
							rs.getDouble("purchase_price"),
							rs.getFloat("quantitiy")));
				}
			}
		}
		finally{
			if(connection != null){
				connection.close();
				connection = null;
			}
		}
		
		return results;
	}
}
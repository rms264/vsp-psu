package vsp.dal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vsp.dal.DatasourceConnection;
import vsp.dataObject.Stock;

public class StockRequest {
	
	/**
	 * @return This will return all stocks from the Stock table
	 * @throws SQLException
	 */
	public static List<Stock> getAllStocks() throws SQLException{
		StockRequest request = new StockRequest();
		return request.submitGetAllStocksRequest();
	}
	
	public static List<Stock> getStock(String stockSymbol) throws SQLException{
		StockRequest request = new StockRequest();
		return request.submitStockRequest(stockSymbol);
	}
	
	private StockRequest(){}
	
	private List<Stock> submitGetAllStocksRequest() throws SQLException{
		String sqlStatement = "SELECT * FROM stock";
		// check for existence of user name in database
		Connection connection = null;
		List<Stock> results = new ArrayList<Stock>();
		try{
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			ResultSet rs = pStmt.executeQuery();
			while(rs.next()){
				results.add(new Stock(rs.getString("stock_symbol"), 
						rs.getString("stock_description")));
			}
			return results;
		}
		finally{
			if(connection != null){
				connection.close();
			}
		}
	}
	
	private List<Stock> submitStockRequest(String stockSymbol) throws SQLException{
		String sqlStatement = "SELECT * FROM stock WHERE stock_symbol=?";
		// check for existence of user name in database
		Connection connection = null;
		List<Stock> results = new ArrayList<Stock>();
		try{
			connection = DatasourceConnection.getConnection();
			PreparedStatement pStmt = connection.prepareStatement(sqlStatement);
			pStmt.setString(1, stockSymbol);  
			ResultSet rs = pStmt.executeQuery();
			while(rs.next()){
				results.add(new Stock(rs.getString("stock_symbol"), 
						rs.getString("stock_description")));
			}
			return results;
		}
		finally{
			if(connection != null){
				connection.close();
			}
		}
	}
}

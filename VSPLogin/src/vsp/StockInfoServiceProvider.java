package vsp;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.StockInfo;

public final class StockInfoServiceProvider
{
	public StockInfoServiceProvider()
	{
		// no implementation required
	}
	
	public StockInfo requestCurrentStockData(String symbol)
	{
		StockInfo stockInfo = null;
		String url = "http://finance.yahoo.com/d/quotes.csv?s=" + symbol + "&f=nb3ghvocqd";
		// n, Name
		// b3, Bid
		// g, Day Low
		// h, Day High
		// v, Volume
		// o, Open
		// c, Change & Percent Change
		// q, Ex-dividend date
		// d, Dividend/Share
		
		List<String> responseLines = null;
		try
		{
			responseLines = getDataFromUrl(url);
		}
		catch (Exception ex)
		{
			// ignore
		}
		
		if (responseLines.size() == 1)
		{
			stockInfo = parseStockInfo(symbol, responseLines.get(0));
		}
		
		return stockInfo;
	}
	
	public List<StockInfo> requestCurrentStockData(List<String> symbols)
	{
		List<StockInfo> results = new ArrayList<StockInfo>();
		if (!symbols.isEmpty())
		{
			String url = "http://finance.yahoo.com/d/quotes.csv?s=" + symbols.get(0);
			for (int i = 1; i < symbols.size(); ++i)
			{
				url += "+" + symbols.get(i).trim();
			}
			
			url += "&f=nb3ghvocqd";
			// n, Name
			// b3, Bid
			// g, Day Low
			// h, Day High
			// v, Volume
			// o, Open
			// c, Change & Percent Change
			// q, Ex-dividend date
			// d, Dividend/Share
			
			List<String> responseLines = null;
			try
			{
				responseLines = getDataFromUrl(url);
			}
			catch (Exception ex)
			{
				// ignore
			}
			
			StockInfo stockInfo = null;
			for (int i = 0; i < responseLines.size(); ++i)
			{
				stockInfo = parseStockInfo(symbols.get(i), responseLines.get(i));
				results.add(stockInfo);
			}
		}
		
		return results;
	}
	
	public HistoricalStockInfo requestHistoricalStockData(String symbol, int months)
	{
		// TODO: implement
		return null;
	}
	
	private static StockInfo parseStockInfo(String symbol, String line)
	{
		StockInfo stockInfo = null;
		
		// parse into columns
		String[] columns = line.split(",");
		for (int i = 0; i < columns.length; ++i)
		{
			columns[i] = columns[i].replaceAll("\"", "").trim();
		}
		
		// DEBUG only
		for (int i = 0; i < columns.length; ++i)
		{
			System.out.println(columns[i]);
		}
		
		if (columns.length == 9)
		{
			// parse change & percent change from single column
			String changeAndPercentChange = columns[6];
			String[] parts = changeAndPercentChange.split("-");
			for (int i = 0; i < parts.length; ++i)
			{
				parts[i] = parts[i].replace("%", "").trim();
			}
			
			// parse date
			Date date = null;
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.ENGLISH);	
				date = sdf.parse(columns[7]);
				// TODO: set year here if we need it
			}
			catch (ParseException pe)
			{
				// ignore
			}
			
			// create StockInfo instance
			stockInfo = new StockInfo(symbol, 
					columns[0], 					// description
					Double.parseDouble(columns[3]),	// dayHigh
					Double.parseDouble(columns[2]),	// dayLow
					date,				 			// ex-dividend date, Date
					Double.parseDouble(columns[8]),	// ex-dividend
					Double.parseDouble(parts[0]), 	// price change since open
					Double.parseDouble(parts[1]), 	// percent change since open
					Integer.parseInt(columns[4]), 	// volume
					Double.parseDouble(columns[1]), // bid
					Double.parseDouble(columns[5]) 	// open
					);
		}
		
		return stockInfo;
	}
	
	private static List<String> getDataFromUrl(String url) throws MalformedURLException, IOException
	{
		List<String> results = new ArrayList<String>();

		URL requestUrl = new URL(url);
        URLConnection connection = requestUrl.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
        {
        	results.add(inputLine);
        }
        
        in.close();
		
		return results;
	}
}

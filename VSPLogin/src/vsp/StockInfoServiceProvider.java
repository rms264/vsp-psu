package vsp;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.Stock;
import vsp.dataObject.StockInfo;

public final class StockInfoServiceProvider
{
	public StockInfoServiceProvider()
	{
		// no implementation required
	}
	
	public List<Stock> searchForStocks(String search)
	{
		String searchUrl = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=" + search + "&callback=YAHOO.Finance.SymbolSuggest.ssCallback";		
		
		List<Stock> results = new ArrayList<Stock>();
		List<String> data = null;
		try
		{
			data = getDataFromUrl(searchUrl);
		}
		catch (Exception ex)
		{
			// ignore
		}
		
		if (data.size() == 1)
		{
			results = parseStockSymbolsAndNames(data.get(0));
		}
		
		return results;
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
	
	private static List<Stock> parseStockSymbolsAndNames(String json)
	{
		/*YAHOO.Finance.SymbolSuggest.ssCallback(
		 * {
		 * 	"ResultSet":
		 * 	{
		 * 		"Query":"mfs",
		 * 		"Result":[
		 * 			{"symbol":"MCR","name": "MFS Charter Income Trust Common","exch": "NYQ","type": "S","exchDisp":"NYSE","typeDisp":"Equity"},
		 * 			{"symbol":"CMK","name": "MFS InterMarket Income Trust I","exch": "NYQ","type": "S","exchDisp":"NYSE","typeDisp":"Equity"},
		 * 			{"symbol":"CMU","name": "MFS High Yield Municipal Trust","exch": "NYQ","type": "S","exchDisp":"NYSE","typeDisp":"Equity"},
		 * 			{"symbol":"MIN","name": "MFS Intermediate Income Trust","exch": "NYQ","type": "S","exchDisp":"NYSE","typeDisp":"Equity"},
		 * 			{"symbol":"MHOCX","name": "MFS High Yield Opportunities C","exch": "NAS","type": "M","exchDisp":"NASDAQ","typeDisp":"Fund"},
		 * 			{"symbol":"CIF","name": "MFS Intermediate High Income Fu","exch": "NYQ","type": "S","exchDisp":"NYSE","typeDisp":"Equity"},
		 * 			{"symbol":"MMUFX","name": "MFS Utilities A","exch": "NAS","type": "M","exchDisp":"NASDAQ","typeDisp":"Fund"},
		 * 			{"symbol":"CCA","name": "MFS California Municipal Fund C","exch": "ASE","type": "S","exchDisp":"AMEX","typeDisp":"Equity"},
		 * 			{"symbol":"MMT","name": "MFS Multimarket Income Trust","exch": "NYQ","type": "S","exchDisp":"NYSE","typeDisp":"Equity"},
		 * 			{"symbol":"MIGFX","name": "MFS Massachusetts Investors Gr Stk A","exch": "NAS","type": "M","exchDisp":"NASDAQ","typeDisp":"Fund"}
		 * 		]
		 * 	}
		 * })
		 * */
		
		List<Stock> results = new ArrayList<Stock>();
		if (json.startsWith("YAHOO.Finance.SymbolSuggest.ssCallback({\"ResultSet\":{\"Query\":\""))
		{
			// reduce string to result entries only
			String beginString = "\"Result\":[";
			int begin = json.indexOf(beginString);
			int end = json.indexOf("]}})");
			json = json.substring(begin + beginString.length(), end);
			
			// process result entries
			String[] resultItems = json.split("}");
			if (resultItems.length > 0)
			{
				// cleanup result entries
				for (int i = 0; i < resultItems.length; ++i)
				{
					resultItems[i] = resultItems[i].replace(",{",  "");
					resultItems[i] = resultItems[i].replace("{",  "");
				}
				
				// extract pairs per entry
				boolean stock;
				String name, symbol;
				for (int i = 0; i < resultItems.length; ++i)
				{
					stock = false;
					name = null;
					symbol = null;
					
					String[] pairs = resultItems[i].split("\",\"");
					if (pairs.length > 0)
					{
						// cleanup pairs & extract name/symbol data
						for (int j = 0; j < pairs.length; ++j)
						{
							pairs[j] = pairs[j].replace("\"", "");
							if (pairs[j].startsWith("symbol:"))
							{
								symbol = pairs[j];
							}
							else if (pairs[j].startsWith("name:"))
							{
								name = pairs[j];
							}
							else if (pairs[j].startsWith("type:") && pairs[j].endsWith("S"))
							{
								stock = true;
							}
						}
						
						// extract symbol & name into a Stock instance
						if (stock && symbol != null && name != null)
						{
							symbol = symbol.replace("symbol:", "").trim();
							name = name.replace("name:", "").trim();
							results.add(new Stock(symbol, name));
						}
					}
				}
			}
		}
		
		return results;
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

package vsp.servlet.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import vsp.dal.requests.Transactions;
import vsp.dataObject.HistoricalStockInfo;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;

public class StockChartHandler extends BaseServletHandler implements
    ServletHandler 
{
  private String year;
  @Override
  public void process(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException 
  {
    String stockSymbol = request.getParameter("symbol");
    year = request.getParameter("year");
    String userName = request.getRemoteUser();
    OutputStream out = response.getOutputStream();
    try {
      
      final XYDataset dataset = createDataset(userName,stockSymbol);
      if(dataset != null){
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        response.setContentType("image/png");
  
        ChartUtilities.writeChartAsPNG(out, chart, 800, 370);
      }
      
    } catch (SQLException | SqlRequestException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private XYDataset createDataset(String userName, String stockSymbol) 
      throws SQLException, SqlRequestException 
  {
    TimeSeriesCollection dataset = null;
    Calendar since = Calendar.getInstance();
    if(year != null && !year.isEmpty()){
      since.add(Calendar.YEAR, -2);
    }else{
      StockTransaction trans = 
          Transactions.getInitialExecutedTransaction(userName, stockSymbol);
      if(trans !=null){
        since.clear();
        since.setTime(trans.getDateTime());
      }
      else{
        since.add(Calendar.YEAR, -2);
      }
    }
    
    
    List<HistoricalStockInfo> stockData = 
        stockService.requestDailyHistoricalStockData(stockSymbol, since.getTime());
    
    TimeSeries stockPerformance = new TimeSeries(stockSymbol + " Performance");
    
    for(HistoricalStockInfo data : stockData){
      stockPerformance.add(new Day(data.getDate()), data.getAdjustedClose());
    }
    
    final TimeSeries mav = MovingAverage.createMovingAverage(
        stockPerformance, "30 day moving average", 30, 30
    );
    dataset = new TimeSeriesCollection();
    dataset.addSeries(stockPerformance);
    dataset.addSeries(mav);
    return dataset;
  }
  private JFreeChart createChart(final XYDataset dataset) {
    final JFreeChart chart = ChartFactory.createTimeSeriesChart(
       "Stock Performance",            // Title
       "Date",                         // X-Axis label
       "Price",                        // Y-Axis label
       dataset,                        // Dataset
       true,                           // Show legend
       false,                          // tooltips
       false                           // url
    );
    return chart;
  }
}

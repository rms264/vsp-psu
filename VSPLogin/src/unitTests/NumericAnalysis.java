package unitTests;


import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import vsp.dataObject.AccountData;
import vsp.dataObject.PortfolioData;
import vsp.dataObject.Stock;
import vsp.dataObject.StockTransaction;
import vsp.exception.SqlRequestException;
import vsp.servlet.form.RegisterForm;
import vsp.statistics.GeometricAverageRateOfReturn;
import vsp.statistics.ReturnOnInvestment;
import vsp.statistics.StockVolatility;
import vsp.utils.Enumeration.OrderAction;
import vsp.utils.Enumeration.OrderState;
import vsp.utils.Enumeration.OrderType;
import vsp.utils.Enumeration.TimeInForce;
import vsp.utils.Enumeration.TimeType;

@RunWith(OrderedRunner.class)
public class NumericAnalysis {
  private final String userName = "unitTestUser";
  private final String password1 = "User1234";
  private final String email = "unitTestUser@unitTestUser.com";
  private final String securityQuestion = "0"; //favorite color
  private final String securityAnswer = "blue";
  private RegisterForm registerForm = new RegisterForm(userName, 
      password1,
      password1,
      email,
      securityQuestion,
      securityAnswer);

  private Stock testStock = new Stock("SIRI", "Sirius XM Radio I");
  private List<StockTransaction> stockTrans = new ArrayList<StockTransaction>();
  private StockTransaction initialInvestment;
  private PortfolioData portEntry;
  
  public NumericAnalysis(){
    
    
    Calendar today = Calendar.getInstance();
    Calendar past = Calendar.getInstance();
    today.clear();
    today.set(2012, Calendar.NOVEMBER, 10);
   
    past.clear();
    past.set(Calendar.YEAR, today.get(Calendar.YEAR));
    past.set(Calendar.MONTH, today.get(Calendar.MONTH));
    past.set(Calendar.DATE, today.get(Calendar.DATE));
    
    past.add(Calendar.YEAR, -2);
    
    initialInvestment = StockTransaction.CreateExecution(
        "test", "asdfasdfasdf",
        new vsp.dataObject.Order("asdfasdfasdf", "test", testStock,
            OrderAction.BUY, 500, OrderType.MARKET, 0.0, 0.0, TimeInForce.DAY, 
            OrderState.COMPLETE, past.getTime(), today.getTime()), past.getTime(),
            755.0, 1.77, 500);
    
    stockTrans.add(initialInvestment);
    
    portEntry = new PortfolioData(testStock, 2.67, 500, "test");
    
  }
  
	@Test
	@Order(order=1)
	public void calculateROR() {
	  GeometricAverageRateOfReturn returnRate = 
	      GeometricAverageRateOfReturn.createTestGROR(
	          new AccountData(registerForm));
	  
	  try {

	  Assert.assertTrue(almostEqual(0.001259, returnRate.getAverageRateOfReturn("SIRI", 
        TimeType.DAY, stockTrans, initialInvestment, portEntry), 1e-6));
    Assert.assertTrue(almostEqual(0.006375, returnRate.getAverageRateOfReturn("SIRI", 
        TimeType.WEEK, stockTrans, initialInvestment, portEntry), 1e-6));
    Assert.assertTrue(almostEqual(0.029458, returnRate.getAverageRateOfReturn("SIRI", 
        TimeType.MONTH, stockTrans, initialInvestment, portEntry), 1e-6));
    Assert.assertTrue(almostEqual(0.157534, returnRate.getAverageRateOfReturn("SIRI", 
        TimeType.YEAR, stockTrans, initialInvestment, portEntry), 1e-6));
    
    } catch (SqlRequestException | SQLException e) {
      Assert.fail("Failed to calculate the Geometric Average Rate Of Return");
    }
	  
	}
	
	@Test
	@Order(order=2)
	public void calculateVolatility() {
	  StockVolatility testStockVolatility = StockVolatility.createTestStockVolatility();
	      
	  Calendar date = Calendar.getInstance();
	  date.clear();
    date.set(2010, Calendar.NOVEMBER, 10);
    

    Assert.assertTrue(almostEqual(0.346888, testStockVolatility.getVolatility(date, TimeType.DAY), 1e-6));
    Assert.assertTrue(almostEqual(0.354567, testStockVolatility.getVolatility(date, TimeType.WEEK), 1e-6));
    Assert.assertTrue(almostEqual(0.374152, testStockVolatility.getVolatility(date, TimeType.MONTH), 1e-6));
    Assert.assertTrue(almostEqual(0.115, testStockVolatility.getVolatility(date, TimeType.YEAR), 1e-6));
    
	}
	
	@Test
	@Order(order=3)
	public void calculateROI() {
	  ReturnOnInvestment testRoi = ReturnOnInvestment.createTestROI(
	      new AccountData(registerForm));
	  
	  Assert.assertTrue(almostEqual(0.768211, testRoi.getReturnOnInvestment(
	      "SIRI", stockTrans, portEntry), 1e-6));
	}
	
	@Test
	@Order(order=4)
	public void calculateCAGR() {
		fail("Not yet implemented");
	}
	
	public static boolean almostEqual(double a, double b, double eps){
    return Math.abs(a-b)<eps;
}
}

package vsp.dal;

import java.sql.DriverManager;
import java.sql.Driver;
import java.util.Enumeration;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
public class DatasourceListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent cs) {
        DatasourceConnection.initDatasource();
        // log
    }
 
    @Override
    public void contextDestroyed(ServletContextEvent ce) {
 
        DatasourceConnection.closeDatasource();
         
        Enumeration<Driver> drivers = DriverManager.getDrivers();
         
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                ce.getServletContext().log("(JDBC driver (" + driver.toString() +
                        ") successfully deregistered");
            } catch (SQLException ex) {
                ce.getServletContext().log( ex.getMessage());
            }
 
        }
    }
 
 
}
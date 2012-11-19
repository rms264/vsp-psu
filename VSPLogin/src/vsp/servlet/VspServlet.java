package vsp.servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.servlet.handler.AdminHandler;
import vsp.servlet.handler.LoginHandler;
import vsp.servlet.handler.LogoutHandler;
import vsp.servlet.handler.OrderHandler;
import vsp.servlet.handler.PendingOrdersHandler;
import vsp.servlet.handler.PortfolioHandler;
import vsp.servlet.handler.RegisterHandler;
import vsp.servlet.handler.ServletHandler;
import vsp.servlet.handler.SignupHandler;
import vsp.servlet.handler.StockChartHandler;
import vsp.servlet.handler.StockDetailsHandler;
import vsp.servlet.handler.StockInfoHandler;
import vsp.servlet.handler.StockSearchHandler;
import vsp.servlet.handler.SubmitEmailUpdateHandler;
import vsp.servlet.handler.SubmitSecurityAnswerHandler;
import vsp.servlet.handler.SubmitSecurityQuestionUpdateHandler;
import vsp.servlet.handler.SubmitUserNameHandler;
import vsp.servlet.handler.SubmitPasswordUpdateHandler;
import vsp.servlet.handler.TransactionHistoryHandler;
import vsp.servlet.handler.UpdateEmailHandler;
import vsp.servlet.handler.UpdatePasswordHandler;
import vsp.servlet.handler.UpdateSecurityQuestionHandler;
import vsp.servlet.handler.UserInfoHandler;
import vsp.servlet.handler.UserNameFormHandler;

@WebServlet(name = "VspServlet", urlPatterns = {"/login", "/signup", "/register", "/enterUserName",
    "/submitUserName", "/submitSecurityAnswer", "/submitResetPassword", "/logout", "/viewPortfolio",
    "/viewUserInfo", "/submitUpdatePassword", "/submitUpdateEmail", "/submitUpdateSecurityQuestion", 
    "/updatePassword", "/updateEmail", "/updateSecurityQuestion", "/order", "/transactionHistory", 
    "/pendingOrders", "/stockSearch", "/viewStockDetails" ,"/getStockChart", "/admin"})
public class VspServlet extends HttpServlet {

  private static final long serialVersionUID = -2836553596862059698L;
  private final Map<String, ServletHandler> handlers = new HashMap<String, ServletHandler>();

  @Override
  public void init() {
    handlers.put("signup", new SignupHandler());
    handlers.put("login", new LoginHandler());
    handlers.put("register", new RegisterHandler());
    handlers.put("enterUserName", new UserNameFormHandler());
    handlers.put("submitUserName", new SubmitUserNameHandler());
    handlers.put("submitSecurityAnswer", new SubmitSecurityAnswerHandler());
    handlers.put("submitResetPassword", new SubmitPasswordUpdateHandler());
    handlers.put("logout", new LogoutHandler());
    handlers.put("viewPortfolio", new PortfolioHandler());
    handlers.put("viewUserInfo", new UserInfoHandler());
    handlers.put("submitUpdatePassword", new SubmitPasswordUpdateHandler());
    handlers.put("submitUpdateEmail", new SubmitEmailUpdateHandler());
    handlers.put("submitUpdateSecurityQuestion", new SubmitSecurityQuestionUpdateHandler());    
    handlers.put("updatePassword", new UpdatePasswordHandler());
    handlers.put("updateEmail", new UpdateEmailHandler());
    handlers.put("updateSecurityQuestion", new UpdateSecurityQuestionHandler());
    handlers.put("viewStockInfo", new StockInfoHandler());
    handlers.put("order", new OrderHandler());
    handlers.put("transactionHistory", new TransactionHistoryHandler());
    handlers.put("pendingOrders", new PendingOrdersHandler());
    handlers.put("stockSearch", new StockSearchHandler());
    handlers.put("viewStockDetails", new StockDetailsHandler());
    handlers.put("getStockChart", new StockChartHandler());
    handlers.put("admin", new AdminHandler());    
  }
  
  @Override
  public void doGet(HttpServletRequest request, 
          HttpServletResponse response)
          throws IOException, ServletException {
    
      process(request, response);
  }

  
  @Override
  public void doPost(HttpServletRequest request, 
          HttpServletResponse response)
          throws IOException, ServletException {
      process(request, response);
  }
  
  private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String uri = request.getRequestURI();
    /*
     * uri is in this form: /contextName/resourceName, 
     * for example: /app10a/product_input. 
     * However, in the case of a default context, the 
     * context name is empty, and uri has this form
     * /resourceName, e.g.: /product_input
     */
    int lastIndex = uri.lastIndexOf("/");
    String action = uri.substring(lastIndex + 1); 
    
    ServletHandler handler = handlers.get(action);
    handler.process(request, response);
    if(handler.getDispatchURL() != null){
      RequestDispatcher rd = 
          request.getRequestDispatcher(handler.getDispatchURL());
      rd.forward(request, response);
    }
  }
}

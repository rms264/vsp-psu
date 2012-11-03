package vsp.servlet.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import vsp.VspServiceProvider;

public class BaseServletHandler {
  protected String dispatchUrl = null;
  protected VspServiceProvider vsp = new VspServiceProvider();
  protected List<String> errors = new ArrayList<String>();
  protected Logger logger = Logger.getLogger("Servlet Logger");

  public String getDispatchURL() {
    return dispatchUrl;
  }
}

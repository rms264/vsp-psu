package vsp.utils;

import java.security.MessageDigest;
import java.text.DecimalFormat;

public class VSPUtils {

	private VSPUtils(){}
	
	public static String hashString(String password)
	{
		StringBuffer sbHash = new StringBuffer();
		try
		{
			MessageDigest sha256 = MessageDigest.getInstance("sha-256");
			byte[] hash = sha256.digest(password.getBytes());
		    for(byte b : hash)
		    {
		    	sbHash.append(String.format("%02x", b));
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	    
	    return sbHash.toString();
	}
	
	public static String formatColor(double value, DecimalFormat format)
	{
		return formatColor(value, format, false);
	}
	
	public static String formatColor(double value, DecimalFormat format, boolean percent)
	{
		String valueString = format.format(value);
		if (!percent)
		{
			valueString = "$" + valueString;
		}
		else
		{
			valueString += "%";
		}
		
		if (value > 0)
		{
			return "<font color=green>" + valueString + "</font>";
		}
		else if (value < 0)
		{
			return "<font color=red>" + valueString + "</font>";
		}
		
		return valueString;
	}
	public static String format(double value, DecimalFormat format, boolean percent)
  {
    String valueString = format.format(value);
    if (!percent)
    {
      valueString = "$" + valueString;
    }
    else
    {
      valueString += "%";
    }
    
    return valueString;
  }
}

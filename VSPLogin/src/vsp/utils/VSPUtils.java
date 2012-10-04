package vsp.utils;

import java.security.MessageDigest;

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
}

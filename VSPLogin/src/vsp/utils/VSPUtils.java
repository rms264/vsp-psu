package vsp.utils;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import vsp.exception.ValidationException;

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
	
	public static String generatePassword(String userName){
	  int len = 8;
	  String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	  Random rnd = new Random();

	  
	  String password = "";
	  boolean isValid = false;
    while(!isValid){
      StringBuilder builder = new StringBuilder( len );
      for( int i = 0; i < len; i++ ) 
        builder.append( AB.charAt( rnd.nextInt(AB.length()) ) );
      
      password = builder.toString();
      try {
        isValid = Validate.validatePassword(userName, password, password);
      } catch (ValidationException e) {}
    }
	  return password;
	}
	
	public static void sendPasswordResetEmail(String emailAddress, String password){

	  String to = emailAddress;
    // Sender's email ID needs to be mentioned
    String from = "vsp@fake.com";

    // Assuming you are sending email from localhost
    String host = "localhost";

    // Get system properties
    Properties properties = System.getProperties();

    // Setup mail server
    properties.setProperty("mail.smtp.host", host);

    // Get the default Session object.
    Session session = Session.getDefaultInstance(properties);

    try{
       // Create a default MimeMessage object.
       MimeMessage message = new MimeMessage(session);

       // Set From: header field of the header.
       message.setFrom(new InternetAddress(from));

       // Set To: header field of the header.
       message.addRecipient(Message.RecipientType.TO,
                                new InternetAddress(to));

       // Set Subject: header field
       message.setSubject("Password Reset for VSP!");

       // Now set the actual message
       message.setText("\nHere is your new Password:\n\n" + password);

       // Send message
       Transport.send(message);
    }catch (MessagingException mex) {
       mex.printStackTrace();
    }
	}
}

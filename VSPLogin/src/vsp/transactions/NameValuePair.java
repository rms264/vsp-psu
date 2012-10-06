package vsp.transactions;

public final class NameValuePair
{
	private final String name;
	private final String value;
	
	NameValuePair(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getValue()
	{
		return this.value;
	}
}

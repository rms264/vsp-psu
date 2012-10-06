package vsp.utils;

public class Enumeration
{
	public enum TimeInForce
	{
		DAY(0),
		GOODUNTILCANCELED(1),
		FILLORKILL(2),
		IMMEDIATEORCANCEL(3),
		DEFAULT(99);
		
		private final int value;
		private TimeInForce(int value)
		{
			this.value = value;
		}
		
		public static TimeInForce convert(int value)
		{
			TimeInForce tif;
			if(value == DAY.value)
			{
				tif = DAY;
			}
			else if(value == GOODUNTILCANCELED.value)
			{
				tif = GOODUNTILCANCELED;
			}
			else if(value == FILLORKILL.value)
			{
				tif = FILLORKILL;
			}
			else if(value == IMMEDIATEORCANCEL.value)
			{
				tif = IMMEDIATEORCANCEL;
			}
			else
			{
				tif = DEFAULT;
			}
			
			return tif;
		}
		
		public int getValue()
		{
			return this.value;
		}
	}
	
	public enum OrderType
	{
		MARKET(0),
		LIMIT(1),
		STOP(2),
		STOPLIMIT(3),
		DEFAULT(99);
		
		private final int value;
		private OrderType(int value)
		{
			this.value = value;
		}
		
		public static OrderType convert(int value)
		{
			OrderType ot;
			if(value == MARKET.value)
			{
				ot = MARKET;
			}
			else if(value == LIMIT.value)
			{
				ot = LIMIT;
			}
			else if(value == STOP.value)
			{
				ot = STOP;
			}
			else if(value == STOPLIMIT.value)
			{
				ot = STOPLIMIT;
			}
			else
			{
				ot = DEFAULT;
			}
			
			return ot;
		}
		
		public int getValue()
		{
			return this.value;
		}
	}
	
	public enum StockAction
	{
		BUY(0),
		SELL(1),
		DEFAULT(99);
		
		private final int value;
		private StockAction(int value)
		{
			this.value = value;
		}
		
		public static StockAction convert(int value)
		{
			StockAction sa;
			if(value == BUY.value)
			{
				sa = BUY;
			}
			else if(value == SELL.value)
			{
				sa = SELL;
			}
			else
			{
				sa = DEFAULT;
			}
			
			return sa;
		}
		
		public int getValue()
		{
			return this.value;
		}
	}
	
	public enum TransactionType
	{
		DIVIDEND(0),
		CANCELLATION(1),
		EXECUTION(2),
		DEFAULT(99);
		
		private final int value;
		private TransactionType(int value)
		{
			this.value = value;
		}
		
		public static TransactionType convert(int value)
		{
			TransactionType tt;
			if(value == DIVIDEND.value)
			{
				tt = DIVIDEND;
			}
			else if(value == CANCELLATION.value)
			{
				tt = CANCELLATION;
			}
			else if(value == EXECUTION.value)
			{
				tt = EXECUTION;
			}
			else
			{
				tt = DEFAULT;
			}
			
			return tt;
		}
		
		public int getValue()
		{
			return this.value;
		}
	}
	
	public enum SecurityQuestion
	{
		FAVORITE_COLOR(0), 
		PET_NAME(1),
		MOTHER_MAIDEN_NAME(2),
		SPOUSE_FIRST_NAME(3),
		ELEMENTRY_SCHOOL_NAME(4),
		HIGH_SCHOOL_NAME(5),
		DEFAULT(99);
		
		private final int value;
		private SecurityQuestion(int value)
		{
			this.value = value;
		}
		
		public static SecurityQuestion convert(int value)
		{
			SecurityQuestion sq;
			if(value == FAVORITE_COLOR.value)
			{
				sq = FAVORITE_COLOR;
			}
			else if(value == PET_NAME.value)
			{
				sq = PET_NAME;
			}
			else if(value == MOTHER_MAIDEN_NAME.value)
			{
				sq = MOTHER_MAIDEN_NAME;
			}
			else if(value == SPOUSE_FIRST_NAME.value)
			{
				sq = SPOUSE_FIRST_NAME;
			}
			else if(value == ELEMENTRY_SCHOOL_NAME.value)
			{
				sq = ELEMENTRY_SCHOOL_NAME;
			}
			else if(value == HIGH_SCHOOL_NAME.value)
			{
				sq = HIGH_SCHOOL_NAME;
			}
			else
			{
				sq = DEFAULT;
			}
			
			return sq;
		}
		
		public int getValue()
		{
			return this.value;
		}
		
		public String toString()
		{
			String result;
			switch(this)
			{
				case FAVORITE_COLOR:
					result = "What is your favorite color?";
					break;
				case PET_NAME:
					result = "What is pet's name?";
					break;
				case MOTHER_MAIDEN_NAME:
					result = "What is your mother's maiden name?";
					break;
				case SPOUSE_FIRST_NAME:
					result = "What is your spouse's first name?";
					break;
				case ELEMENTRY_SCHOOL_NAME:
					result = "What was the name of your elementary school?";
					break;
				case HIGH_SCHOOL_NAME:
					result = "What was the name of your high school?";
					break;
				default:
					result = "UNKNOWN";
			}
			
			return result;
		}
	}
	
	
	public enum Role
	{
		ADMIN("admin"), 
		TRADER("trader"),
		DEFAULT("UNKNOWN");
		
		private final String roleName;
		private Role(String roleName)
		{
			this.roleName = roleName;
		}
		
		public static Role get(String roleName)
		{
			Role role = Role.DEFAULT;
			if(roleName.toLowerCase().equals(ADMIN.toString()))
			{
				role = Role.ADMIN;
			}
			else if(roleName.toLowerCase().equals(TRADER.toString()))
			{
				role = Role.TRADER;
			} 
			
			return role;
		}
		
		public String toString()
		{
			return roleName;
		}
	}
}

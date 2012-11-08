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
			TimeInForce tif = DEFAULT;
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
			
			return tif;
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
				case DAY:
					result = "Day";
					break;
				case GOODUNTILCANCELED:
					result = "Good Until Canceled";
					break;
				case FILLORKILL:
					result = "Fill or Kill";
					break;
				case IMMEDIATEORCANCEL:
					result = "Immediate or Cancel";
					break;
				default:
					result = "UNKNOWN";
			}
			
			return result;
		}
	}
	
	public enum OrderState
	{
		PENDING(0),
		COMPLETE(1),
		CANCELLED(2),
		DEFAULT(99);
		
		private final int value;
		private OrderState(int value)
		{
			this.value = value;
		}
		
		public static OrderState convert(int value)
		{
			OrderState os = DEFAULT;
			if(value == PENDING.value)
			{
				os = PENDING;
			}
			else if(value == COMPLETE.value)
			{
				os = COMPLETE;
			}
			else if(value == CANCELLED.value)
			{
				os = CANCELLED;
			}
			
			return os;
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
				case PENDING:
					result = "Pending";
					break;
				case COMPLETE:
					result = "Complete";
					break;
				case CANCELLED:
					result = "Cancelled";
					break;
				default:
					result = "UNKNOWN";
			}
			
			return result;
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
			OrderType ot = DEFAULT;
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
			
			return ot;
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
				case MARKET:
					result = "Market";
					break;
				case LIMIT:
					result = "Limit";
					break;
				case STOP:
					result = "Stop";
					break;
				case STOPLIMIT:
					result = "Stop Limit";
					break;
				default:
					result = "UNKNOWN";
			}
			
			return result;
		}
	}
	
	public enum OrderAction
	{
		BUY(0),
		SELL(1),
		DEFAULT(99);
		
		private final int value;
		private OrderAction(int value)
		{
			this.value = value;
		}
		
		public static OrderAction convert(int value)
		{
			OrderAction sa = DEFAULT;
			if(value == BUY.value)
			{
				sa = BUY;
			}
			else if(value == SELL.value)
			{
				sa = SELL;
			}
			
			return sa;
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
				case BUY:
					result = "Buy";
					break;
				case SELL:
					result = "Sell";
					break;
				default:
					result = "UNKNOWN";
			}
			
			return result;
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
			TransactionType tt = DEFAULT;
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
			
			return tt;
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
				case DIVIDEND:
					result = "Dividend";
					break;
				case CANCELLATION:
					result = "Cancellation";
					break;
				case EXECUTION:
					result = "Execution";
					break;
				default:
					result = "UNKNOWN";
			}
			
			return result;
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
			SecurityQuestion sq = DEFAULT;
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
	
	
	public enum RoleType
	{
		ADMIN("admin"), 
		TRADER("trader"),
		DEFAULT("UNKNOWN");
		
		private final String roleName;
		private RoleType(String roleName)
		{
			this.roleName = roleName;
		}
		
		public static RoleType get(String roleName)
		{
			RoleType role = RoleType.DEFAULT;
			if(roleName.toLowerCase().equals(ADMIN.toString()))
			{
				role = RoleType.ADMIN;
			}
			else if(roleName.toLowerCase().equals(TRADER.toString()))
			{
				role = RoleType.TRADER;
			} 
			
			return role;
		}
		
		public String toString()
		{
			return roleName;
		}
	}
	
	public enum TimeType{
	  DAY,WEEK,MONTH,YEAR;
	  
	}
}

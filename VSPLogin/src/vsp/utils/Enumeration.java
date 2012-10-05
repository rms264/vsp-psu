package vsp.utils;

public class Enumeration
{

	public enum TransactionType
	{
		DIVIDEND("dividend"),
		CANCELLATION("cancellation"),
		EXECUTION("execution"),
		DEFAULT("UNKNOWN");
		
		private final String transactionType;
		private TransactionType(String transactionType)
		{
			this.transactionType = transactionType;
		}
		
		public static TransactionType get(String transactionTypeName)
		{
			TransactionType transactionType = TransactionType.DEFAULT;
			if(transactionTypeName.toLowerCase().equals(DIVIDEND.toString()))
			{
				transactionType = TransactionType.DIVIDEND;
			}
			else if(transactionTypeName.toLowerCase().equals(CANCELLATION.toString()))
			{
				transactionType = TransactionType.CANCELLATION;
			}
			else if(transactionTypeName.toLowerCase().equals(EXECUTION.toString()))
			{
				transactionType = TransactionType.EXECUTION;
			}
			
			return transactionType;
		}
		
		public String toString()
		{
			return transactionType;
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
		
		public static SecurityQuestion convert(int val)
		{
			SecurityQuestion sq;
			if(val == FAVORITE_COLOR.value)
			{
				sq = FAVORITE_COLOR;
			}
			else if(val == PET_NAME.value)
			{
				sq = PET_NAME;
			}
			else if(val == MOTHER_MAIDEN_NAME.value)
			{
				sq = MOTHER_MAIDEN_NAME;
			}
			else if(val == SPOUSE_FIRST_NAME.value)
			{
				sq = SPOUSE_FIRST_NAME;
			}
			else if(val == ELEMENTRY_SCHOOL_NAME.value)
			{
				sq = ELEMENTRY_SCHOOL_NAME;
			}
			else if(val == HIGH_SCHOOL_NAME.value)
			{
				sq = HIGH_SCHOOL_NAME;
			}
			else
			{
				sq = DEFAULT;
			}
			
			return sq;
		}
		
		public int getVal()
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

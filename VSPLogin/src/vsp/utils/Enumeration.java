package vsp.utils;

public class Enumeration {

	public enum SecurityQuestion {
		FAVORITE_COLOR(0), 
		PET_NAME(1),
		MOTHER_MAIDEN_NAME(2),
		SPOUSE_FIRST_NAME(3),
		ELEMENTRY_SCHOOL_NAME(4),
		HIGH_SCHOOL_NAME(5),
		DEFAULT(99);
		
		private final int value;
		private SecurityQuestion(int val){
			value = val;
		}
		
		public static SecurityQuestion convert(int val){
			SecurityQuestion sq;
			if(val == FAVORITE_COLOR.value){
				sq = FAVORITE_COLOR;
			}
			else if(val == PET_NAME.value){
				sq = PET_NAME;
			}
			else if(val == MOTHER_MAIDEN_NAME.value){
				sq = MOTHER_MAIDEN_NAME;
			}
			else if(val == SPOUSE_FIRST_NAME.value){
				sq = SPOUSE_FIRST_NAME;
			}
			else if(val == ELEMENTRY_SCHOOL_NAME.value){
				sq = ELEMENTRY_SCHOOL_NAME;
			}
			else if(val == HIGH_SCHOOL_NAME.value){
				sq = HIGH_SCHOOL_NAME;
			}
			else{
				sq = DEFAULT;
			}
			return sq;
		}
		
		public int getVal(){
			return this.value;
		}
		
		public String toString(){
			String result;
			switch(this){
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
	
	
	public enum Role{
		ADMIN("admin"), 
		TRADER("trader"),
		DEFAULT("UNKNOWN");
		
		private final String roleName;
		private Role(String role){
			roleName = role;
		}
		
		public static Role get(String tmpRole){
			Role role = Role.DEFAULT;
			if(tmpRole.toLowerCase().equals(ADMIN.toString())){
				role = Role.ADMIN;
			}
			else if(tmpRole.toLowerCase().equals(TRADER.toString())){
				role = Role.TRADER;
			} 
			return role;
		}
		
		public String toString(){
			return roleName;
		}
	}
}

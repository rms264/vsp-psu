package vsp;

public final class SecurityQuestions
{
	private static String[] securityQuestionStrings = new String[]
			{
				"What is your favorite color?",
				"What is pet's name?",
				"What is your mother's maiden name?",
				"What is your spouse's first name?",
				"What was the name of your elementary school?",
				"What was the name of your high school?"
			};
	
	public static String getQuestionText(int questionNum)
	{
		String questionText = "";
		switch(questionNum)
		{
			default:
				break;
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				questionText = securityQuestionStrings[questionNum];
				break;
		}
		
		return questionText;
	}
	
	public static String[] getAllQuestions()
	{
		return securityQuestionStrings;
	}
}

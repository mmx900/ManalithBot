package org.manalith.ircbot.plugin.Calc.Exceptions;

public class ParenthesesMismatchException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ParenthesesMismatchException ( )
	{
		super ("Pairs of parentheses did not match.");
	}
}

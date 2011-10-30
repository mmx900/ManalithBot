package org.manalith.ircbot.plugin.Calc.Exceptions;

public class TokenAnalysisException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public TokenAnalysisException ( )
	{
		super("Unknown type token found!");
	}
}

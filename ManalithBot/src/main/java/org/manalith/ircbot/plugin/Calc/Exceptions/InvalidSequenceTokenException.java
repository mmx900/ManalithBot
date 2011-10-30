package org.manalith.ircbot.plugin.Calc.Exceptions;

public class InvalidSequenceTokenException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidSequenceTokenException ( String AdditionalInfo ) 
	{
		super ( "Invalid sequence of token stream : " + AdditionalInfo );
	}
}

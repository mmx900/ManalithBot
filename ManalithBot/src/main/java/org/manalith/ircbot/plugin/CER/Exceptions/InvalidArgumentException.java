package org.manalith.ircbot.plugin.CER.Exceptions;

public class InvalidArgumentException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidArgumentException ( )
	{
		super ( " Invalid Argument : no reason. ");
	}
	public InvalidArgumentException ( String newMessage )
	{
		super ( " Invalid Argument : " + newMessage );
	}
	
}

package org.manalith.ircbot.plugin.TwitReader.Exceptions;

public class UnknownTypeOfStringException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public UnknownTypeOfStringException ( )
	{
		super ( "String has unknown type" );
	}

}

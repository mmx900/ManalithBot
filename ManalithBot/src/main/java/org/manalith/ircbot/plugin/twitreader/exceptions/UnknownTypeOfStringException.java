package org.manalith.ircbot.plugin.twitreader.exceptions;

public class UnknownTypeOfStringException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public UnknownTypeOfStringException ( )
	{
		super ( "String has unknown type" );
	}

}

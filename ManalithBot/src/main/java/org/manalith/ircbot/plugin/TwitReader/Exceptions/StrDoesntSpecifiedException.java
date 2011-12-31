package org.manalith.ircbot.plugin.TwitReader.Exceptions;

public class StrDoesntSpecifiedException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public StrDoesntSpecifiedException ( )
	{
		super ( "URL does not specified");
	}
}

package org.manalith.ircbot.plugin.TwitReader.Exceptions;

public class URLDoesntSpecifiedException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public URLDoesntSpecifiedException ( )
	{
		super ( "URL does not specified");
	}
}

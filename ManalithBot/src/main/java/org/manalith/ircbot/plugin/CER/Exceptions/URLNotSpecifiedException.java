package org.manalith.ircbot.plugin.CER.Exceptions;

public class URLNotSpecifiedException extends Exception{

	private static final long serialVersionUID = 1L;

	public URLNotSpecifiedException () 
	{
		super ("URL is not specified.");
	}
}

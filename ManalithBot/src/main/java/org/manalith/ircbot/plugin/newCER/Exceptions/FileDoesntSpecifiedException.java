package org.manalith.ircbot.plugin.newCER.Exceptions;

public class FileDoesntSpecifiedException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public FileDoesntSpecifiedException ( )
	{
		super ( "File does not specified");
	}
}

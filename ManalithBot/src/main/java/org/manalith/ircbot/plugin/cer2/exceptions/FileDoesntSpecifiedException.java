package org.manalith.ircbot.plugin.cer2.exceptions;

public class FileDoesntSpecifiedException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public FileDoesntSpecifiedException ( )
	{
		super ( "File does not specified");
	}
}

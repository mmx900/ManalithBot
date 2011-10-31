package org.manalith.ircbot.plugin.CER.Exceptions;

public class ArgumentNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ArgumentNotFoundException ()
	{
		super ("Argument is not found.");
	}
}

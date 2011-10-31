package org.manalith.ircbot.plugin.CER.Exceptions;

public class EmptyTokenStreamException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	public EmptyTokenStreamException ()
	{
		super("Token stream is empty");
	}
	
}

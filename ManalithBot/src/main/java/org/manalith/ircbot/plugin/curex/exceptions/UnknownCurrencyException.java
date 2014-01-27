package org.manalith.ircbot.plugin.curex.exceptions;

public class UnknownCurrencyException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UnknownCurrencyException (String currency)
	{
		super("Unknown currency is given: " + currency);
	}

}

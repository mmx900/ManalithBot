package org.manalith.ircbot.plugin.Calc.Exceptions;

public class InvalidOperatorUseException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidOperatorUseException()
	{
		super("This operator cannot be used for computing floating point.");
	}
}
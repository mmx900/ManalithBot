package org.manalith.ircbot.plugin.curex.exceptions;

import java.util.Locale;

public class IllegalArgumentException extends Exception{

	private static final long serialVersionUID = 1L;

	public IllegalArgumentException(int position)
	{
		super(String.format(Locale.getDefault(),"Illegal argument is being at position #: %d",position));
	}
}

package org.manalith.ircbot.plugin.curex.exceptions;

import java.util.Locale;

public class UnnecessaryArgumentException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public UnnecessaryArgumentException (int position)
	{
		super(String.format(Locale.getDefault(),"Unnecessary argument is given at position #: %d",position));
	}
}

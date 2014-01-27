package org.manalith.ircbot.plugin.curex.exceptions;

import java.util.Locale;

public class MissingArgumentException extends Exception{

	private static final long serialVersionUID = 1L;

	public MissingArgumentException(int position)
	{
		super(String.format(Locale.getDefault(),"There is missing argument at position #%d", position));
	}
}

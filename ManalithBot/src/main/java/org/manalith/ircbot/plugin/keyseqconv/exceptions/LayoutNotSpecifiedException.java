package org.manalith.ircbot.plugin.keyseqconv.exceptions;

public class LayoutNotSpecifiedException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public LayoutNotSpecifiedException ( )
	{
		super("Keyboard layout is not specified");
	}
}

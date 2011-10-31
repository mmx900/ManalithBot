package org.manalith.ircbot.plugin.CER.Exceptions;

public class FileNotSpecifiedException extends Exception {

	private static final long serialVersionUID = 1L;

	public FileNotSpecifiedException ( )
	{
		super ( "File is not specified." );
	}
}

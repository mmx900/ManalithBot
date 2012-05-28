package org.manalith.ircbot.plugin.typeconv.exceptions;

public class BackSlashesDoNotMatchException extends Exception {

	private static final long serialVersionUID = 1L;

	public BackSlashesDoNotMatchException ( )
	{
		super ( "Back slashes do not match");
	}
}

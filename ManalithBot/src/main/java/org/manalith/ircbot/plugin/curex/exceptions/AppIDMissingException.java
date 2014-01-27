package org.manalith.ircbot.plugin.curex.exceptions;

public class AppIDMissingException extends Exception {

	private static final long serialVersionUID = 1L;

	public AppIDMissingException() {
		super("App ID is missing");
	}
}

package org.manalith.ircbot.plugin.twitreader.exceptions;

public class StrDoesntSpecifiedException extends Exception {

	private static final long serialVersionUID = 1L;

	public StrDoesntSpecifiedException() {
		super("URL does not specified");
	}
}

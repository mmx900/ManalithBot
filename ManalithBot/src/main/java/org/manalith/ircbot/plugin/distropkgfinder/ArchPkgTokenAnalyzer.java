package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class ArchPkgTokenAnalyzer extends TokenAnalyzer {

	public ArchPkgTokenAnalyzer() {
		super();
	}

	public ArchPkgTokenAnalyzer(String newData) {
		this.data = newData;
	}

	@Override
	public TokenType getTokenType(String TokenString) {
		return null;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		return null;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		return null;
	}

}

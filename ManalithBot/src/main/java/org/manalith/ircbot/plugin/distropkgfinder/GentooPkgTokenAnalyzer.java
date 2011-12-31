package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class GentooPkgTokenAnalyzer extends TokenAnalyzer {
	public GentooPkgTokenAnalyzer() {
		super();
	}

	public GentooPkgTokenAnalyzer(String newData) {
		this.data = newData;
	}

	@Override
	public TokenType getTokenType(String TokenString) {
		TokenType result = TokenType.Unknown;

		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		TokenSubtype result = TokenSubtype.Unknown;
		return result;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		TokenArray result = new TokenArray();

		return result;
	}
}

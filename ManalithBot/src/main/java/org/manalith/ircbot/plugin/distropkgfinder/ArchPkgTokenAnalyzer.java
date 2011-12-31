package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class ArchPkgTokenAnalyzer extends TokenAnalyzer {
	
	public ArchPkgTokenAnalyzer ( )
	{
		super ( );
	}
	
	public ArchPkgTokenAnalyzer ( String newData )
	{
		this.data = newData;
	}
	
	@Override
	public TokenType getTokenType(String TokenString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		// TODO Auto-generated method stub
		return null;
	}

}

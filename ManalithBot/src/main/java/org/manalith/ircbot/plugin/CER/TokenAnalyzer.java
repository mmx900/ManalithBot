package org.manalith.ircbot.plugin.CER;

import org.manalith.ircbot.plugin.CER.Exceptions.EmptyTokenStreamException;


public abstract class TokenAnalyzer {
	protected String data;
	public TokenAnalyzer ()
	{
		this.setTokenStringData( "" );
	}
	
	public void setTokenStringData ( String newData )
	{
		this.data = newData;
	}
	public String getTokenStringData ( )
	{
		return this.data;
	}
	
	public abstract TokenType getTokenType ( String TokenString );
	public abstract TokenSubtype getTokenSubtype ( String TokenString, TokenType CurrentType );
	
	public abstract TokenArray analysisTokenStream () throws EmptyTokenStreamException;
}

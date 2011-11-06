//
// TokenAnalyzer.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.KVL;

import org.manalith.ircbot.plugin.KVL.Exceptions.EmptyTokenStreamException;


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

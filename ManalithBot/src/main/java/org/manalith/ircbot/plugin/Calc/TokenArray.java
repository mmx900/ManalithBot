package org.manalith.ircbot.plugin.Calc;
// TokenArray.java 
//
// This class can store many TokenUnit elements.
//
// This program can be distributed under the terms of GNU GPL v3 or later.
// darkcircle.0426@gmail.com

import java.util.ArrayList;
public class TokenArray {
	private ArrayList<TokenUnit> TokenArray;
	
	public TokenArray ( )
	{
		TokenArray = new ArrayList<TokenUnit>();
	}
	public TokenArray ( TokenArray sourceObject )
	{
		TokenArray = new ArrayList<TokenUnit>();
		TokenArray.addAll(sourceObject.getArray());
	}
	public TokenArray ( ArrayList<TokenUnit> sourceArray )
	{
		TokenArray = new ArrayList<TokenUnit>();
		TokenArray.addAll(sourceArray);
	}
	
	public void addToken ( TokenType newTokenType, TokenSubtype newTokenSubtype, String newTokenStr )
	{
		TokenUnit newTokenUnit = new TokenUnit ( newTokenType, newTokenSubtype, newTokenStr );
		TokenArray.add( newTokenUnit );
	}
	public void addToken ( TokenUnit newTokenUnit )
	{
		TokenArray.add( newTokenUnit );
	}
	
	public TokenUnit getToken ( int index )
	{
		return TokenArray.get( index );
	}
	public int getSize ( )
	{
		return TokenArray.size();
	}
	public ArrayList<TokenUnit> getArray ( )
	{
		return this.TokenArray;
	}
	
	public String toString ( )
	{
		String result = "";
		
		for ( TokenUnit unit : TokenArray )
		{
			result += unit.toString() + "\n";
		}
		
		return result;
	}
}

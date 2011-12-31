//
// TokenArray.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.distropkgfinder;

import java.util.ArrayList;
public class TokenArray {
	
	private ArrayList<TokenUnit> array;
	
	public TokenArray ()
	{
		array = new ArrayList<TokenUnit> ();
	}
	
	public int getSize()
	{
		return array.size();
	}
	
	public void addElement(TokenUnit newTokenUnit)
	{
		array.add(newTokenUnit);
	}
	public void addElement(String tokenString, TokenType newTokenType, TokenSubtype newTokenSubtype)
	{
		TokenUnit newTokenUnit = new TokenUnit(tokenString, newTokenType, newTokenSubtype);
		array.add(newTokenUnit);
	}
	public void removeElement (int index)
	{
		array.remove(index);
	}
	public TokenUnit getElement (int index)
	{
		return array.get(index);
	}
	
	public String toString ()
	{
		String result = "";
		
		int size = this.array.size();
		
		for ( int i = 0 ; i < size ; i++ )
		{
			result += ( this.getElement(i).toString() + "\n" );
		}
		
		return result;
	}

}

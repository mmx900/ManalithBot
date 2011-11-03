//
// NoticeDTTokenAnalyzer.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.CER;

import org.manalith.ircbot.plugin.CER.Exceptions.EmptyTokenStreamException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class NoticeDTTokenAnalyzer extends TokenAnalyzer {
	
	public NoticeDTTokenAnalyzer ()
	{
		super ();
	}
	
	@Override
	public TokenType getTokenType(String TokenString) {
		
		TokenType result = TokenType.Unknown;
		
		Pattern div_pattern = Pattern.compile("\\<[\\/]?div((\\s)(id|class|style|onclick)\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern span_pattern = Pattern.compile("\\<[\\/]?span((\\s)(id|class)\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern em_pattern = Pattern.compile("\\<[\\/]?em\\>");
		
		Matcher div_match = div_pattern.matcher(TokenString);
		Matcher span_match = span_pattern.matcher(TokenString);
		Matcher em_match = em_pattern.matcher(TokenString);
		
		if ( div_match.matches() )
		{
			result = TokenType.Div;
		}
		else if ( span_match.matches() )
		{
			result = TokenType.Span;
		}
		else if ( em_match.matches() )
		{
			result = TokenType.EM;
		}
		
		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		
		TokenSubtype result = TokenSubtype.Unknown;
		int hashcode = CurrentType.hashCode();
		
		if ( hashcode == TokenType.Div.hashCode() )
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.DivClose;
			else 
				result = TokenSubtype.DivOpen;

		else if ( hashcode == TokenType.Span.hashCode() )
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.SpanClose;
			else
				result = TokenSubtype.SpanOpen;
		else if ( hashcode == TokenType.EM.hashCode() )
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.EMClose;
			else
				result = TokenSubtype.EMOpen;
		
		
		return result;
	}

	@Override
	public TokenArray analysisTokenStream()
			throws EmptyTokenStreamException {
		TokenArray result = new TokenArray();
		TokenType currentTokenType = TokenType.Unknown;
		TokenSubtype currentTokenSubtype = TokenSubtype.Unknown;
		boolean inBoundOfSection_Ex = false;
		int DivNestingCount = 0;
		
		int len = this.data.length();
		if ( len == 0 )
			throw new EmptyTokenStreamException();
		
		int i = 0;
		
		String tokenString = "";
		String tempchar = "";
		
		while ( i < len )
		{
			tempchar = this.data.substring(i, i+1);
			i++;

			if ( ( tempchar.equals("\t")  || tempchar.equals("\n") ) || tempchar.equals("\r"))
				continue;
			
			if ( tokenString.equals("") && !tempchar.equals("<") )
			{
				if ( tokenString.length() == 0 && tempchar.equals(" ") )
					continue;
				else
				{
					tokenString = tempchar;
					tempchar = this.data.substring(i, i+1);
					i++;
				}
				

				while ( !tempchar.equals("<") )
				{
					if ( tempchar.equals("\t") || ( tempchar.equals("\r")|| tempchar.equals("\n") ) )
					{
						tempchar = this.data.substring(i, i+1);
						i++;
						continue;
					}
					else if ( tokenString.length() == 0 && tempchar.equals(" ") )
					{
						i++;
						continue;
					}
					tokenString += tempchar; 
					tempchar = this.data.substring(i, i+1);
					i++;
				}
				
				currentTokenType = TokenType.TextString;
				currentTokenSubtype = TokenSubtype.TextString;
				
				if ( inBoundOfSection_Ex )
				{
					result.addElement(tokenString, currentTokenType, currentTokenSubtype);
					tokenString = "";
					currentTokenType = TokenType.Unknown;
					currentTokenSubtype = TokenSubtype.Unknown;
				}
				
			}
			
			// get a piece of the tag
			if ( tempchar.equals("<") )
			{
				tokenString = tempchar;
				tempchar = this.data.substring(i, i+1);
				i++;
				
				while ( !tempchar.equals(">") )
				{
					tokenString += tempchar;
					tempchar = this.data.substring(i, i+1);
					i++;
				}
				
				// I need to check this point;
				tokenString += tempchar;			
			} // OK!
			
			currentTokenType = this.getTokenType(tokenString);
			if ( currentTokenType == TokenType.Unknown )
			{
				tokenString = "";
				continue;
			}
			else
			{
				
				currentTokenSubtype = this.getTokenSubtype(tokenString, currentTokenType);
				
				if ( currentTokenSubtype == TokenSubtype.DivOpen )
				{
					if ( !inBoundOfSection_Ex )
					{
						String substr = tokenString.substring(1, tokenString.length() - 1);
						String [] separatedTokens = substr.split("\\s");
						int size = separatedTokens.length;
						for ( int idx = 1 ; idx < size ; idx++ )
						{
							String [] keynval = separatedTokens[idx].split("\\="); 
							String keyname = keynval[0];
						
							if ( keyname.equals("id") )
							{
								String [] values = keynval[1].split("\\\"");
								int val_len = values.length;
							
								for ( int j = 0 ; j < val_len ; j++ )
								{
									if ( values[j].length() == 0 )
										continue;
									else
										if ( values[j].equals("section_ex1") )
										{
											inBoundOfSection_Ex = true;
											break;
										}
								}
								if ( inBoundOfSection_Ex )
								{
									DivNestingCount++;
									break;
								}
							}
						}
					}
					else 
					{
						DivNestingCount++;
					}
				}
				else if ( currentTokenSubtype == TokenSubtype.DivClose )
				{
					if ( inBoundOfSection_Ex )
					{
						if ( DivNestingCount > 1 )
						{
							DivNestingCount--;
						}
						else
						{
							DivNestingCount--;
							TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
							result.addElement(newTokenUnit);
							inBoundOfSection_Ex = false;
							break;
						}
					}		
				}
				
				TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
				if ( inBoundOfSection_Ex )
					result.addElement(newTokenUnit);
				
				tokenString = "";
				currentTokenType = TokenType.Unknown;
				currentTokenSubtype = TokenSubtype.Unknown;
				
			}
		}
		
		return result;
	}
	
}

package org.manalith.ircbot.plugin.CER;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.manalith.ircbot.plugin.CER.Exceptions.EmptyTokenStreamException;

public class CERTableTokenAnalyzer extends TokenAnalyzer{
	
	public CERTableTokenAnalyzer ()
	{
		super ();
	}
	
	public TokenType getTokenType(String tokenString)
	{
		TokenType result = TokenType.Unknown;
		
		Pattern tbody_pattern = Pattern.compile("\\<[\\/]?tbody\\>");
		Pattern tr_pattern = Pattern.compile("\\<[\\/]?tr\\>");
		Pattern td_pattern = Pattern.compile("\\<[\\/]?td(\\s(class)(\\=)(\\\")[a-zA-Z]+(\\\"))?\\>");
		//Pattern a_pattern = Pattern.compile("\\<[\\/]?a((\\s)(href|target|onclick|name)\\=\\\"(\\s|\\S)+\\\")*\\>");
		
		Matcher tbody_match = tbody_pattern.matcher(tokenString);
		Matcher tr_match = tr_pattern.matcher(tokenString);
		Matcher td_match = td_pattern.matcher(tokenString);
		//Matcher a_match = a_pattern.matcher(tokenString);
		
		if ( tbody_match.matches() )
		{
			result = TokenType.TBody;
		}
		else if ( tr_match.matches() )
		{
			result = TokenType.TR;
		}
		else if ( td_match.matches() )
		{
			result = TokenType.TD;
		}
		
		return result;
	}
	public TokenSubtype getTokenSubtype(String tokenString, TokenType currentType)
	{
		TokenSubtype result;
		int hashCode = currentType.hashCode();
		
		if ( hashCode == TokenType.TBody.hashCode() )
		{
			if ( tokenString.charAt(1) == '/' )
				result = TokenSubtype.TBodyClose;
			else
				result = TokenSubtype.TBobyOpen;
		}
		else if ( hashCode == TokenType.TR.hashCode() )
		{
			if ( tokenString.charAt(1) == '/' )
				result = TokenSubtype.TRClose;
			else 
				result = TokenSubtype.TROpen;
		}
		else if ( hashCode == TokenType.TD.hashCode() )
		{
			if ( tokenString.charAt(1) == '/')
				result = TokenSubtype.TDClose;
			else
				result = TokenSubtype.TDOpen;
		}
		else if ( hashCode == TokenType.TextString.hashCode() )
			result = TokenSubtype.TextString;
		else
			result = TokenSubtype.Unknown;
		
		return result;
	}
	
	//
	// OK! Implementation is completed. 2011/10/19
	// No more needs to modify something.
	//
	public TokenArray analysisTokenStream () throws EmptyTokenStreamException
	{
		TokenArray result = new TokenArray();
		TokenType currentTokenType = TokenType.Unknown;
		TokenSubtype currentTokenSubtype = TokenSubtype.Unknown;
		boolean inBoundOfTBody = false;
		
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
			{
				continue;
			}
			
			if ( tokenString.equals("") && !tempchar.equals("<") )
			{
				tokenString = tempchar;
				tempchar = this.data.substring(i, i+1);
				i++;

				while ( !tempchar.equals("<") )
				{
					if ( tempchar.equals("\t") || ( tempchar.equals("\r")|| tempchar.equals("\n") ) )
					{
						tempchar = this.data.substring(i, i+1);
						i++;
						continue;
					}
					tokenString += tempchar; 
					tempchar = this.data.substring(i, i+1);
					i++;
				}
				
				currentTokenType = TokenType.TextString;
				currentTokenSubtype = this.getTokenSubtype ( tokenString, currentTokenType );
				
				if ( inBoundOfTBody )
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
				
				if ( currentTokenSubtype == TokenSubtype.TBobyOpen )
					inBoundOfTBody = true;
				else if ( currentTokenSubtype == TokenSubtype.TBodyClose )
				{
					TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
					result.addElement(newTokenUnit);
					break;
				}
				
				TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
				if ( inBoundOfTBody )
					result.addElement(newTokenUnit);
				
				tokenString = "";
				currentTokenType = TokenType.Unknown;
				currentTokenSubtype = TokenSubtype.Unknown;
				
			}
		}
		
		return result;
	}
}

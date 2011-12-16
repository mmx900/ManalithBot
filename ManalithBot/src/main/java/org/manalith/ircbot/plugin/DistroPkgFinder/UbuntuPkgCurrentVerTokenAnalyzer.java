package org.manalith.ircbot.plugin.DistroPkgFinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.manalith.ircbot.plugin.DistroPkgFinder.Exceptions.EmptyTokenStreamException;

public class UbuntuPkgCurrentVerTokenAnalyzer extends TokenAnalyzer {

	public UbuntuPkgCurrentVerTokenAnalyzer ()
	{
		super ();
	}
	
	public UbuntuPkgCurrentVerTokenAnalyzer ( String newData )
	{
		this.data = newData;
	}
	@Override
	public TokenType getTokenType(String TokenString) {
		// TODO Auto-generated method stub
		TokenType result = TokenType.Unknown;
		
		Pattern select_pattern = Pattern.compile("\\<[\\/]?select((\\s)[\\S]+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern option_pattern = Pattern.compile("\\<[\\/]?option((\\s)([\\S]+\\=\\\"(\\s|\\S)+\\\")?)*\\>");
		
		Matcher select_matcher = select_pattern.matcher(TokenString);
		Matcher option_matcher = option_pattern.matcher(TokenString);
		
		if ( select_matcher.matches() )
			result = TokenType.Select;
		
		else if ( option_matcher.matches() )
			result = TokenType.Option;
		
		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		// TODO Auto-generated method stub
		TokenSubtype result = TokenSubtype.Unknown;
		int hashCode = CurrentType.hashCode();
		
		if ( hashCode == TokenType.Select.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.SelectClose;
			else
				result = TokenSubtype.SelectOpen;
		}
		else if ( hashCode == TokenType.Option.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.OptionClose;
			else
				result = TokenSubtype.OptionOpen;
		}
		
		
		return result;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		// TODO Auto-generated method stub
		TokenArray result = new TokenArray();
		TokenType currentTokenType = TokenType.Unknown;
		TokenSubtype currentTokenSubtype = TokenSubtype.Unknown;
		boolean inBoundOfResult = false;
		
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

			if ( ( tempchar.equals("\t")  || tempchar.equals("\n") ) || ( tempchar.equals("\r") || tempchar.equals(" ") ) )
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
				currentTokenSubtype = TokenSubtype.TextString;
				
				if ( inBoundOfResult )
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
				
				if ( currentTokenSubtype == TokenSubtype.SelectOpen )
				{
					String [] tagnattr = tokenString.split("\\s");
					String [] keyval = tagnattr[1].split("\\=");
					String value = keyval[1].substring(1, keyval[1].length() - 2);
					if ( value.equals("suite") )
					{
						inBoundOfResult = true;
					}
				}
				else if ( currentTokenSubtype == TokenSubtype.SelectClose && inBoundOfResult )
				{
					TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
					result.addElement(newTokenUnit);
					break;
				}
				
				TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
				if ( inBoundOfResult )
					result.addElement(newTokenUnit);
				
				tokenString = "";
				currentTokenType = TokenType.Unknown;
				currentTokenSubtype = TokenSubtype.Unknown;
				
			}
		}
		
		return result;
	}
}
package org.manalith.ircbot.plugin.distropkgfinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class FedoraPkgStatusTokenAnalyzer extends TokenAnalyzer {
	public FedoraPkgStatusTokenAnalyzer ( )
	{
		super () ;
	}
	
	public FedoraPkgStatusTokenAnalyzer ( String newData )
	{
		this.data = newData;
	}

	@Override
	public TokenType getTokenType(String TokenString) {
		// TODO Auto-generated method stub
		TokenType result = TokenType.Unknown;
		
		Pattern ul_pattern = Pattern.compile("\\<[\\/]?ul((\\s)(\\S)+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern li_pattern = Pattern.compile("\\<[\\/]?li\\>");
		Pattern p_pattern = Pattern.compile("\\<[\\/]?p\\>");
		Pattern a_pattern = Pattern.compile("\\<[\\/]?a((\\s)(\\S)+\\=\\\"(\\s|\\S)+\\\")*\\>");
		
		Matcher ul_matcher = ul_pattern.matcher(TokenString);
		Matcher li_matcher = li_pattern.matcher(TokenString);
		Matcher p_matcher = p_pattern.matcher(TokenString);
		Matcher a_matcher = a_pattern.matcher(TokenString);
		
		if ( ul_matcher.matches() )
			result = TokenType.Ul;
		else if ( li_matcher.matches() )
			result = TokenType.Li;
		else if ( p_matcher.matches() )
			result = TokenType.P;
		else if ( a_matcher.matches() )
			result = TokenType.A;
		
		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		// TODO Auto-generated method stub
		TokenSubtype result = TokenSubtype.Unknown;
		
		int hashCode = CurrentType.hashCode();
		
		if ( hashCode == TokenType.Ul.hashCode() )
		{
			if ( TokenString.charAt(1) == '/')
				result = TokenSubtype.UlClose;
			else
				result = TokenSubtype.UlOpen;
		}
		else if ( hashCode == TokenType.Li.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.LiClose;
			else
				result = TokenSubtype.LiOpen;
		}
		else if ( hashCode == TokenType.P.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.PClose;
			else
				result = TokenSubtype.POpen;
		}
		else if ( hashCode == TokenType.A.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.AClose;
			else
				result = TokenSubtype.AOpen;
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
		boolean inBoundOfVersionInfo = false;
		
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
				
				if ( inBoundOfResult || inBoundOfVersionInfo )
				{
					tokenString = tokenString.trim();
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
				
				if ( currentTokenSubtype == TokenSubtype.UlOpen )
				{
					String [] tagnattr = tokenString.split("\\s");
					if ( tagnattr.length != 1 )
					{
						String [] keyval = tagnattr[1].split("\\=");
						String value = keyval[1].substring(1, keyval[1].length() - 2);
						if ( value.equals("actions") )
						{
							inBoundOfResult = true;
						}
					}
				}
				else if ( currentTokenSubtype == TokenSubtype.POpen )
				{
					inBoundOfVersionInfo = true;
				}
				else if ( currentTokenSubtype == TokenSubtype.UlClose && inBoundOfResult )
				{
					TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
					result.addElement(newTokenUnit);
					inBoundOfResult = false;
					continue;
				}
				else if ( currentTokenSubtype == TokenSubtype.PClose && inBoundOfVersionInfo )
				{
					TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
					result.addElement(newTokenUnit);
					inBoundOfVersionInfo = false;
					continue;
				}
				
				TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
				if ( inBoundOfResult || inBoundOfVersionInfo )
					result.addElement(newTokenUnit);
				
				tokenString = "";
				currentTokenType = TokenType.Unknown;
				currentTokenSubtype = TokenSubtype.Unknown;
				
			}
		}
		
		return result;
	}
}

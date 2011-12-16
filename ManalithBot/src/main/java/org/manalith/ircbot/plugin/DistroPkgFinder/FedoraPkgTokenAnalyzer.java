package org.manalith.ircbot.plugin.DistroPkgFinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.manalith.ircbot.plugin.DistroPkgFinder.Exceptions.EmptyTokenStreamException;

public class FedoraPkgTokenAnalyzer extends TokenAnalyzer {

	public FedoraPkgTokenAnalyzer ( )
	{
		super () ;
	}
	
	public FedoraPkgTokenAnalyzer ( String newData )
	{
		this.data = newData;
	}

	@Override
	public TokenType getTokenType(String TokenString) {
		// TODO Auto-generated method stub
		TokenType result = TokenType.Unknown;
		
		Pattern div_pattern = Pattern.compile("\\<[\\/]?div((\\s)(\\S)+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern dl_pattern = Pattern.compile("\\<[\\/]?dl\\>");
		Pattern dt_pattern = Pattern.compile("\\<[\\/]?dt\\>");
		Pattern a_pattern = Pattern.compile("\\<[\\/]?a((\\s)(\\S)+\\=\\\"(\\s|\\S)+\\\")*\\>");
		
		Matcher div_matcher = div_pattern.matcher(TokenString);
		Matcher dl_matcher = dl_pattern.matcher(TokenString);
		Matcher dt_matcher = dt_pattern.matcher(TokenString);
		Matcher a_matcher = a_pattern.matcher(TokenString);
		
		if ( div_matcher.matches() )
			result = TokenType.Div;
		else if ( dl_matcher.matches() )
			result = TokenType.Dl;
		else if ( dt_matcher.matches() )
			result = TokenType.Dt;
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
		
		if ( hashCode == TokenType.Div.hashCode() )
		{
			if ( TokenString.charAt(1) == '/')
				result = TokenSubtype.DivClose;
			else
				result = TokenSubtype.DivOpen;
		}
		else if ( hashCode == TokenType.Dl.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.DlClose;
			else
				result = TokenSubtype.DlOpen;
		}
		else if ( hashCode == TokenType.Dt.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
				result = TokenSubtype.DtClose;
			else
				result = TokenSubtype.DtOpen;
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
				
				if ( currentTokenSubtype == TokenSubtype.DivOpen )
				{
					String [] tagnattr = tokenString.split("\\s");
					String [] keyval = tagnattr[1].split("\\=");
					String value = keyval[1].substring(1, keyval[1].length() - 2);
					if ( value.equals("PackageGroup") )
					{
						inBoundOfResult = true;
					}
				}
				else if ( currentTokenSubtype == TokenSubtype.DivClose && inBoundOfResult )
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

package org.manalith.ircbot.plugin.DistroPkgFinder;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.manalith.ircbot.plugin.DistroPkgFinder.TokenArray;
import org.manalith.ircbot.plugin.DistroPkgFinder.TokenSubtype;
import org.manalith.ircbot.plugin.DistroPkgFinder.TokenType;
import org.manalith.ircbot.plugin.DistroPkgFinder.TokenUnit;
import org.manalith.ircbot.plugin.DistroPkgFinder.Exceptions.EmptyTokenStreamException;

public class FedoraRpmFindTokenAnalyzer extends TokenAnalyzer {
	
	public FedoraRpmFindTokenAnalyzer ( )
	{
		super ();
	}
	
	public FedoraRpmFindTokenAnalyzer ( String newData )
	{
		this.data = newData;
	}
	
	@Override
	public TokenType getTokenType(String TokenString) {
		// TODO Auto-generated method stub
		TokenType result = TokenType.Unknown;
		
		Pattern table_pattern = Pattern.compile("\\<[\\/]?table\\>");
		Pattern tbody_pattern = Pattern.compile("\\<[\\/]?tbody\\>");
		Pattern tr_pattern = Pattern.compile("\\<[\\/]?tr((\\s)(\\S)+\\=\\'(\\s|\\S)*\\')*\\>");
		Pattern td_pattern = Pattern.compile("\\<[\\/]?td((\\s)(\\S)+\\=\\'(\\s|\\S)*\\')*\\>");
		// Pattern a_pattern = Pattern.compile("\\<[\\/]?a((\\s)(\\S)+\\=\\'(\\s|\\S)*\\')*\\>"); // uselesss
		
		Matcher table_matcher = table_pattern.matcher(TokenString);
		Matcher tbody_matcher = tbody_pattern.matcher(TokenString);
		Matcher tr_matcher = tr_pattern.matcher(TokenString);
		Matcher td_matcher = td_pattern.matcher(TokenString);

		if ( table_matcher.matches() )
		{
			result = TokenType.Table;
		}
		else if ( tbody_matcher.matches() )
		{
			result = TokenType.TBody;
		}
		else if ( tr_matcher.matches() )
		{
			result = TokenType.Tr;
		}
		else if ( td_matcher.matches() )
		{
			result = TokenType.Td;
		}
		
		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		// TODO Auto-generated method stub
		TokenSubtype result = TokenSubtype.Unknown;
		int hashCode = CurrentType.hashCode();
		
		
		if ( hashCode == TokenType.Table.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
			{
				result = TokenSubtype.TableClose;
			}
			else
			{
				result = TokenSubtype.TableOpen;
			}
		}
		else if ( hashCode == TokenType.TBody.hashCode() )
		{
			if ( TokenString.charAt(1) == '/' )
			{
				result = TokenSubtype.TBodyClose;
			}
			else
			{
				result = TokenSubtype.TBodyOpen;
			}
		}
		else if ( hashCode == TokenType.Tr.hashCode() )
		{
			if ( TokenString.charAt(1) == '/')
			{
				result = TokenSubtype.TrClose;
			}
			else
			{
				result = TokenSubtype.TrOpen;
			}
		}
		else if ( hashCode == TokenType.Td.hashCode() )
		{
			if ( TokenString.charAt(1) == '/')
			{
				result = TokenSubtype.TdClose;
			}
			else
			{
				result = TokenSubtype.TdOpen;
			}
		}
		
		return result;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		// TODO Auto-generated method stub
		TokenArray result = new TokenArray();
		TokenType currentTokenType = TokenType.Unknown;
		TokenSubtype currentTokenSubtype = TokenSubtype.Unknown;
		boolean inBoundOfTable = false;
		
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
				
				if ( inBoundOfTable )
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
				
				if ( currentTokenSubtype == TokenSubtype.TableOpen )
				{
					inBoundOfTable = true;
				}
				else if ( currentTokenSubtype == TokenSubtype.TableClose && inBoundOfTable )
				{
					TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
					result.addElement(newTokenUnit);
					break;
				}
				
				TokenUnit newTokenUnit = new TokenUnit (tokenString, currentTokenType, currentTokenSubtype);
				if ( inBoundOfTable )
					result.addElement(newTokenUnit);
				
				tokenString = "";
				currentTokenType = TokenType.Unknown;
				currentTokenSubtype = TokenSubtype.Unknown;
			}
		}
		
		return result;
	}

}

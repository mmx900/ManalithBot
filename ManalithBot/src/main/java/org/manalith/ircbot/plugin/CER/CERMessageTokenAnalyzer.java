//
// CERMessageTokenAnalyzer.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.
package org.manalith.ircbot.plugin.CER;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.manalith.ircbot.plugin.CER.Exceptions.InvalidArgumentException;

public class CERMessageTokenAnalyzer {
	
	private String TokenString;
	
	public CERMessageTokenAnalyzer ( )
	{
		this.setTokenString( "" );
	}
	public CERMessageTokenAnalyzer ( String newTokenString )
	{
		this.setTokenString ( newTokenString) ;
	}
	
	public void setTokenString ( String newTokenString )
	{
		this.TokenString = newTokenString;
	}
	public String getTokenString ( )
	{
		return this.TokenString;
	}
	
	public TokenType getTokenType ( String tokenstring )
	{
		TokenType result = TokenType.Unknown;
		Pattern cmd_pattern = Pattern.compile("[a-z]{4,9}");
		Pattern curname_pattern = Pattern.compile("[A-Z]{3}");
		Pattern field_pattern = Pattern.compile("[가-힣]{4}");
		Pattern amount_pattern = Pattern.compile("[0-9]{1,3}((\\,)?[0-9]{3})*(.[0-9]{1,2})?");
		
		Matcher cmd_match = cmd_pattern.matcher(tokenstring);
		Matcher curname_match = curname_pattern.matcher(tokenstring);
		Matcher field_match = field_pattern.matcher(tokenstring);
		Matcher amount_match = amount_pattern.matcher(tokenstring);
		
		
		if ( cmd_match.matches() )
		{
			result = TokenType.IRCOption;
		}
		else if ( curname_match.matches() )
		{
			result = TokenType.IRCCurrencyName;
		}
		else if ( field_match.matches() )
		{
			result = TokenType.IRCFieldAbbr;
		}
		else if ( amount_match.matches() )
		{
			result = TokenType.Amount;
		}
		
		return result;
	}
	public TokenSubtype getTokenSubtype ( String tokenstring, TokenType currentTokenType )
	{
		TokenSubtype result = TokenSubtype.Unknown;
		
		if ( currentTokenType == TokenType.IRCOption )
		{
			if ( tokenstring.equals("show") )
			{
				result = TokenSubtype.CommandShow;
			}
			else if ( tokenstring.equals("convfrom"))
			{
				result = TokenSubtype.CommandConvertFrom;
			}
			else if ( tokenstring.equals("convto"))
			{
				result = TokenSubtype.CommandConvertTo;
			}
			else if ( tokenstring.equals("buycash"))
			{
				result = TokenSubtype.CommandBuyCash;
			}
			else if ( tokenstring.equals("cellcash"))
			{
				result = TokenSubtype.CommandCellCash;
			}
			else if ( tokenstring.equals("sendremit"))
			{
				result = TokenSubtype.CommandSendRemittance;
			}
			else if ( tokenstring.equals("recvremit"))
			{
				result = TokenSubtype.CommandRecvRemittance;
			}
			else if ( tokenstring.equals("lastround"))
			{
				result = TokenSubtype.CommandLatestRound;
			}
			else if ( tokenstring.equals("fupdate"))
			{
				result = TokenSubtype.CommandForceUpdate;
			}
			else if ( tokenstring.equals("help"))
				result = TokenSubtype.CommandHelp;
		}
		else if ( currentTokenType == TokenType.IRCCurrencyName )
		{
			try
			{
				result = TokenSubtype.valueOf("Currency" + tokenstring );
			}
			catch ( IllegalArgumentException e )
			{
				result = TokenSubtype.Unknown;
			}
		}
		else if ( currentTokenType == TokenType.IRCFieldAbbr )
		{
			if ( tokenstring.equals("매매기준") )
			{
				result = TokenSubtype.FACentralRate;
			}
			else if ( tokenstring.equals("현찰매수") )
			{
				result = TokenSubtype.FABuyCash;
			}
			else if ( tokenstring.equals("현찰매도") )
			{
				result = TokenSubtype.FACellCash;
			}
			else if ( tokenstring.equals("송금보냄") )
			{
				result = TokenSubtype.FASendRemit;
			}
			else if ( tokenstring.equals("송금받음") )
			{
				result = TokenSubtype.FARecvRemit;
			}
			else if ( tokenstring.equals("환수수료") )
			{
				result = TokenSubtype.FAECRate;
			}
			else if ( tokenstring.equals("대미환율") )
			{
				result = TokenSubtype.FAECRate;
			}
			else if ( tokenstring.equals("모두보기") )
			{
				result = TokenSubtype.FAAll;
			}
		}
		else if ( currentTokenType == TokenType.Amount )
		{
			Pattern natural_pattern = Pattern.compile("[0-9]{1,3}((\\,)?[0-9]{3})*");
			Pattern fp_pattern = Pattern.compile("[0-9]{1,3}((\\,)?[0-9]{3})*\\.[0-9]{1,2}");
			
			Matcher natural_match = natural_pattern.matcher(TokenString);
			Matcher fp_match = fp_pattern.matcher(TokenString);
			
			if ( natural_match.matches() ) result = TokenSubtype.AmountNatural;
			else if ( fp_match.matches() ) result = TokenSubtype.AmountFp;
		}
		
		return result;
	}

	public String convertToCLICommandString ( ) throws InvalidArgumentException
	{
		String result = "";
		
		if ( this.getTokenString().length() == 0 )
		{
			result = "--show USD cr";
			return result;
		}
		
		String [] tokens = this.getTokenString().split("\\s");
		int len = tokens.length;
		
		TokenType [] TokenTypeList = new TokenType[len];
		TokenSubtype [] TokenSubtypeList = new TokenSubtype[len];
		
		TokenType curTokenType;
		TokenSubtype curTokenSubtype;
		
		if ( len > 3 )
		{
			throw new InvalidArgumentException("Too many arguments are specified.");
		}
		
		for ( int i = 0 ; i < len ; i++ ) 
		{
			curTokenType = this.getTokenType(tokens[i]);
			if ( i == 0 )
			{
				
				if ( curTokenType == TokenType.IRCOption )
				{
					curTokenSubtype = this.getTokenSubtype ( tokens[i], curTokenType );
					if ( curTokenSubtype == TokenSubtype.CommandShow )
					{
						result += "--show ";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandConvertFrom )
					{
						result += "--convertfrom ";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandConvertTo )
					{
						result += "--convertto ";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandBuyCash )
					{
						result += "--buycash ";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandCellCash )
					{
						result += "--cellcash ";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandRecvRemittance )
					{
						result += "--recvremit ";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandSendRemittance )
					{
						result += "--sendremit ";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandLatestRound )
					{
						result += "--latestround";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandForceUpdate )
					{
						result += "--forceupdate";
					}
					else if ( curTokenSubtype == TokenSubtype.CommandHelp ) 
					{
						result += "--help";
					}
					else if ( curTokenSubtype == TokenSubtype.Unknown )
					{
						throw new InvalidArgumentException("Unknown option.");
					}
					
					TokenTypeList[i] = curTokenType;
					TokenSubtypeList[i] = curTokenSubtype;
					
					curTokenType = TokenType.Unknown;
					curTokenSubtype = TokenSubtype.Unknown;
				}
				else if ( curTokenType == TokenType.IRCCurrencyName )
				{
					curTokenSubtype = this.getTokenSubtype(tokens[i], curTokenType );
					result += ( "--show " + tokens[i] );
					
					TokenTypeList[i] = curTokenType;
					TokenSubtypeList[i] = curTokenSubtype;
					
					curTokenType = TokenType.Unknown;
					curTokenSubtype = TokenSubtype.Unknown;
				}
				else
					throw new InvalidArgumentException("옵션 빠짐");
			}
			else if ( i == 1 )
			{
				curTokenSubtype = this.getTokenSubtype(tokens[i], curTokenType);
				if ( TokenSubtypeList[0] != TokenSubtype.CommandShow )
				{
					if ( ( curTokenSubtype == TokenSubtype.CommandLatestRound || curTokenSubtype == TokenSubtype.CommandForceUpdate )
							|| curTokenSubtype == TokenSubtype.CommandHelp )
					{
						throw new InvalidArgumentException("Unnecessary argument specified.");
					}
					else if ( curTokenType == TokenType.IRCCurrencyName && curTokenSubtype != TokenSubtype.Unknown )
					{
						result += ( tokens[i] + " " );
					}
					else if ( curTokenType == TokenType.FieldAbbr )
					{
						result += "USD ";
						
						if ( curTokenSubtype == TokenSubtype.FACentralRate )
						{
							result += "cr";
						}
						else if ( curTokenSubtype == TokenSubtype.FABuyCash )
						{
							result += "cb";
						}
						else if ( curTokenSubtype == TokenSubtype.FACellCash )
						{
							result += "cc";
						}
						else if ( curTokenSubtype == TokenSubtype.FARecvRemit )
						{
							result += "rr";
						}
						else if ( curTokenSubtype == TokenSubtype.FASendRemit )
						{
							result += "rs";
						}
						else if ( curTokenSubtype == TokenSubtype.FAECRate )
						{
							result += "ec";
						}
						else if ( curTokenSubtype == TokenSubtype.FADollarExcRate )
						{
							result += "de";
						}
						else if ( curTokenSubtype == TokenSubtype.FAAll )
						{
							result += "all";
						}
						else if ( curTokenSubtype == TokenSubtype.Unknown )
						{
							throw new InvalidArgumentException("Invalid Field Abbreviation.");
						}						
					}
					else if ( curTokenType == TokenType.Amount )
					{
						throw new InvalidArgumentException("Amount is not required.");
					}
					else if ( curTokenType == TokenType.IRCOption )
					{
						throw new InvalidArgumentException("Two or more options are specified.");
					}
					else
					{
						throw new InvalidArgumentException("Unknown argument found.");
					}
				}
				else if ( TokenTypeList[0] == TokenType.IRCCurrencyName )
				{
					if ( curTokenType == TokenType.IRCFieldAbbr )
					{
						if ( curTokenSubtype == TokenSubtype.FACentralRate )
						{
							result += "cr";
						}
						else if ( curTokenSubtype == TokenSubtype.FABuyCash )
						{
							result += "cb";
						}
						else if ( curTokenSubtype == TokenSubtype.FACellCash )
						{
							result += "cc";
						}
						else if ( curTokenSubtype == TokenSubtype.FARecvRemit )
						{
							result += "rr";
						}
						else if ( curTokenSubtype == TokenSubtype.FASendRemit )
						{
							result += "rs";
						}
						else if ( curTokenSubtype == TokenSubtype.FAECRate )
						{
							result += "ec";
						}
						else if ( curTokenSubtype == TokenSubtype.FADollarExcRate )
						{
							result += "de";
						}
						else if ( curTokenSubtype == TokenSubtype.FAAll )
						{
							result += "all";
						}
						else if ( curTokenSubtype == TokenSubtype.Unknown )
						{
							throw new InvalidArgumentException("Invalid Field Abbreviation.");
						}
					}
					else 
					{
						throw new InvalidArgumentException("Field name is not given.");
					}
				}
				else
				{
					if ( curTokenType == TokenType.IRCCurrencyName && curTokenSubtype != TokenSubtype.Unknown )
					{
						result += ( tokens[i] + " ");
					}
					else if ( curTokenType == TokenType.Amount && curTokenSubtype != TokenSubtype.Unknown )
					{
						result += ( "USD " + tokens[i] );
					}
					else if ( curTokenType == TokenType.IRCFieldAbbr )
					{
						throw new InvalidArgumentException("Field name is not required.");
					}
					else if ( curTokenType == TokenType.IRCOption )
					{
						throw new InvalidArgumentException("Two or more options are specified.");
					}
					else 
						throw new InvalidArgumentException("Unknown argument found.");
				}
				
				TokenTypeList[i] = curTokenType;
				TokenSubtypeList[i] = curTokenSubtype;
				
				curTokenType = TokenType.Unknown;
				curTokenSubtype = TokenSubtype.Unknown;
			}
			else if ( i == 2 )
			{
				curTokenSubtype = this.getTokenSubtype(tokens[i], curTokenType);
				if ( TokenTypeList[0] != TokenType.IRCCurrencyName )
				{
					if ( TokenSubtypeList[0] == TokenSubtype.CommandShow )
					{
						if ( curTokenType == TokenType.Amount )
							throw new InvalidArgumentException("Amount is not required.");
						else if ( curTokenType == TokenType.IRCOption )
							throw new InvalidArgumentException("Two or more options are specified");
						else if ( curTokenType == TokenType.IRCCurrencyName )
							throw new InvalidArgumentException("Two or more options cannot be specified.");
						else if ( curTokenType == TokenType.IRCFieldAbbr )
						{
							if ( curTokenSubtype == TokenSubtype.FACentralRate )
							{
								result += "cr";
							}
							else if ( curTokenSubtype == TokenSubtype.FABuyCash )
							{
								result += "cb";
							}
							else if ( curTokenSubtype == TokenSubtype.FACellCash )
							{
								result += "cc";
							}
							else if ( curTokenSubtype == TokenSubtype.FARecvRemit )
							{
								result += "rr";
							}
							else if ( curTokenSubtype == TokenSubtype.FASendRemit )
							{
								result += "rs";
							}
							else if ( curTokenSubtype == TokenSubtype.FAECRate )
							{
								result += "ec";
							}
							else if ( curTokenSubtype == TokenSubtype.FADollarExcRate )
							{
								result += "de";
							}
							else if ( curTokenSubtype == TokenSubtype.FAAll )
							{
								result += "all";
							}
							else if ( curTokenSubtype == TokenSubtype.Unknown )
							{
								throw new InvalidArgumentException("Invalid Field Abbreviation.");
							}
						}
						else 
							throw new InvalidArgumentException("Unknown argument found.");
					}
					else if ( TokenSubtypeList[0] != TokenSubtype.Unknown )
					{
						if ( curTokenType == TokenType.IRCFieldAbbr )
							throw new InvalidArgumentException("Field name is not required.");
						else if ( curTokenType == TokenType.IRCOption )
							throw new InvalidArgumentException("Two or more options are specified");
						else if ( curTokenType == TokenType.IRCCurrencyName )
							throw new InvalidArgumentException("Two or more options cannot be specified.");
						else if ( curTokenType == TokenType.Amount )
							result += tokens[i];
						else 
							throw new InvalidArgumentException("Unknown argument found.");
					}
					else 
						throw new InvalidArgumentException("Unknown argument found.");
				}
				else
				{
					throw new InvalidArgumentException("Too many options are specified.");
				}
				
				TokenTypeList[i] = curTokenType;
				TokenSubtypeList[i] = curTokenSubtype;
				
				curTokenType = TokenType.Unknown;
				curTokenSubtype = TokenSubtype.Unknown;
			}
		}
		
		if ( len == 1 )
		{
			if ( TokenTypeList[0] == TokenType.IRCOption && TokenSubtypeList[0] == TokenSubtype.CommandShow )
			{
				result += "USD cr";
				return result;
			}
		}
		else if ( len == 2 )
		{
			if ( ( TokenTypeList[0] == TokenType.IRCOption && TokenSubtypeList[0] == TokenSubtype.CommandShow )
					&&
					( TokenTypeList[1] == TokenType.IRCCurrencyName ) )
				
			{
				result += "cr";
				return result;
			}
		}
		
		return result;
	}
}

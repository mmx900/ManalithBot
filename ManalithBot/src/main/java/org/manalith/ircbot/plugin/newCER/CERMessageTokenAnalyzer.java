//
// CERMessageTokenAnalyzer.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.
package org.manalith.ircbot.plugin.newCER;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.manalith.ircbot.plugin.newCER.Exceptions.InvalidArgumentException;

public class CERMessageTokenAnalyzer {
	
	private static TokenType getTokenType ( String tokenstring )
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
			result = TokenType.IRCCurrencyUnit;
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
	private static TokenSubtype getTokenSubtype ( String tokenstring, TokenType currentTokenType )
	{
		TokenSubtype result = TokenSubtype.Unknown;
		
		if ( currentTokenType == TokenType.IRCOption )
		{
			if ( tokenstring.equals("show") )
			{
				result = TokenSubtype.CommandShow;
			}
			else if ( tokenstring.equals("conv"))
			{
				result = TokenSubtype.CommandConvert;
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
				result = TokenSubtype.CommandSendRemit;
			}
			else if ( tokenstring.equals("recvremit"))
			{
				result = TokenSubtype.CommandRecvRemit;
			}
			else if ( tokenstring.equals("lastround"))
			{
				result = TokenSubtype.CommandLastRound;
			}
			else if ( tokenstring.equals("help"))
				result = TokenSubtype.CommandHelp;
		}
		else if ( currentTokenType == TokenType.IRCCurrencyUnit )
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
			
			Matcher natural_match = natural_pattern.matcher(tokenstring);
			Matcher fp_match = fp_pattern.matcher(tokenstring);
			
			if ( natural_match.matches() ) result = TokenSubtype.AmountNatural;
			else if ( fp_match.matches() ) result = TokenSubtype.AmountFp;
		}
		
		return result;
	}

	public static String convertToCLICommandString ( String TokenString ) throws InvalidArgumentException
	{
		String result = "";
		
		if ( TokenString.length() == 0 )
		{
			result = "--show USD cr";
			return result;
		}
		
		String [] tokens = TokenString.split("\\s");
		int len = tokens.length;
		
		TokenType [] arrTokenType = new TokenType [len];
		TokenSubtype [] arrTokenSubtype = new TokenSubtype [len];

		int i;
		for ( i = 0 ; i < len ; i++ )
		{
			arrTokenType[i] = getTokenType(tokens[i]);
			arrTokenSubtype[i] = getTokenSubtype(tokens[i],arrTokenType[i]);
		}
		
		i = 0;
		
		if ( arrTokenType[i] == TokenType.IRCOption )
		{
			if ( arrTokenSubtype[i] == TokenSubtype.CommandHelp || arrTokenSubtype[i] == TokenSubtype.CommandLastRound )
			{
				if ( len > 1 ) throw new InvalidArgumentException ( "불 필요한 옵션" );
				else result = "--" + tokens[i];
			}
			else if ( arrTokenSubtype[i] == TokenSubtype.CommandShow )
			{
				result = "--" + tokens[i++];
				if ( i == len )
				{
					result += " USD cr";
					return result;
				}
				
				if ( arrTokenType[i] == TokenType.IRCCurrencyUnit )
				{
					if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
						throw new InvalidArgumentException("알 수 없는 통화단위");
					else
					{
						result += " " + tokens[i++];
						if ( i == len )
						{
							result += " cr";
							return result;
						}
					}
				}
				else
				{
					throw new InvalidArgumentException("통화단위 아님");
				}
				
				if ( arrTokenType[i] == TokenType.IRCFieldAbbr )
				{
					if ( arrTokenSubtype[i] == TokenSubtype.FACentralRate )
						result += " cr";
					else if ( arrTokenSubtype[i] == TokenSubtype.FABuyCash )
						result += " cb";
					else if ( arrTokenSubtype[i] == TokenSubtype.FACellCash )
						result += " cc";
					else if ( arrTokenSubtype[i] == TokenSubtype.FASendRemit )
						result += " rs";
					else if ( arrTokenSubtype[i] == TokenSubtype.FARecvRemit )
						result += " rr";
					else if ( arrTokenSubtype[i] == TokenSubtype.FAECRate )
						result += " ec";
					else if ( arrTokenSubtype[i] == TokenSubtype.FADollarExcRate )
						result += " de";
					else if ( arrTokenSubtype[i] == TokenSubtype.FAAll )
						result += " all";
					else
						throw new InvalidArgumentException("잘못된 옵션");
					i++;
					
					if ( i != len ) throw new InvalidArgumentException ( "불 필요한 옵션" );
				}
				else
				{
					throw new InvalidArgumentException("잘못된 필드");
				}
			}
			else if ( ( arrTokenSubtype[i] == TokenSubtype.CommandBuyCash || arrTokenSubtype[i] == TokenSubtype.CommandCellCash )
					|| ( arrTokenSubtype[i] == TokenSubtype.CommandRecvRemit || arrTokenSubtype[i] == TokenSubtype.CommandSendRemit ) )
			{
				result = "--" + tokens[i++];
				// no argument
				if ( i == len )
				{
					result += " 1 USD";
					return result;
				}
				
				// 1st argument
				if ( arrTokenType[i] == TokenType.IRCCurrencyUnit )
				{
					if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
						throw new InvalidArgumentException("알 수 없는 통화단위");
					else
					{
						result += " 1 " + tokens[i++];
						if ( i != len )	throw new InvalidArgumentException ( "불 필요한 옵션" );
					}
				}
				else if ( arrTokenType[i] == TokenType.Amount )
				{
					if ( arrTokenSubtype[i] != TokenSubtype.AmountFp && arrTokenSubtype[i] != TokenSubtype.AmountNatural )
						throw new InvalidArgumentException("알 수 없는 금액");
					else
					{
						result += " " + tokens[i++];
						if ( i == len )
						{
							result += "USD";
							return result;
						}
					}
				}
				else
				{
					throw new InvalidArgumentException("금액 혹은 통화단위 아님");
				}
				
				// 2nd argument
				if ( arrTokenType[i++] == TokenType.IRCCurrencyUnit )
				{
					if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
						throw new InvalidArgumentException("알 수 없는 통화단위");
					else
					{
						result += " " + tokens[i++];
						if ( i != len ) throw new InvalidArgumentException ( "불 필요한 옵션" );
					}
				}
				else
				{
					throw new InvalidArgumentException("통화단위 아님");
				}				
			}
			else if ( arrTokenSubtype[i] == TokenSubtype.CommandConvert )
			{
				result += "--" + arrTokenSubtype[i++].toString().substring(7).toLowerCase(); // add cmd
				
				// no argument
				if ( i == len )
				{
					throw new InvalidArgumentException ( "필요한 옵션 없음(금액)" );
				}
				
				// 1st argument
				if ( arrTokenType[i] == TokenType.Amount )
				{
					if ( arrTokenSubtype[i] != TokenSubtype.AmountFp && arrTokenSubtype[i] != TokenSubtype.AmountNatural )
						throw new InvalidArgumentException("알 수 없는 금액");
					else
					{
						result += " " + tokens[i++]; // [Amount]
						if ( i == len )
						{
							result += " KRW USD";
							return result;
						}
					}
				}
				else if ( arrTokenType[i] == TokenType.IRCCurrencyUnit )
				{
					if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
						throw new InvalidArgumentException("알 수 없는 통화단위");
					else
					{
						result += " 1 " + tokens[i++]; //  (1) [Current_unit1]
						if ( i == len )
						{
							throw new InvalidArgumentException ( "필요한 옵션 없음(통화단위)" );
						}
					}
				}
				else
				{
					throw new InvalidArgumentException("금액 혹은 통화단위 아님");
				}
				
				// 2nd argument
				if ( arrTokenType[i] == TokenType.IRCCurrencyUnit )
				{
					if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
						throw new InvalidArgumentException("알 수 없는 통화단위");
					
					if ( i + 1 == len )
					{
						if ( arrTokenType[i-1] == TokenType.IRCCurrencyUnit )
						{
							result += " " + tokens[i++]; //  (1) [Currency_unit1] [Currency_unit2]
							return result;
						}
						else if ( arrTokenType[i-1] == TokenType.Amount )
						{
							result += " KRW" + " " + tokens[i++]; // [Amount] KRW [Currency_unit2]
							return result;
						}
					}
					else
					{
						if ( arrTokenType[i-1] == TokenType.Amount )
						{
							result += " " + tokens[i++]; // [Amount] [Currency_unit1]
							if ( i == len ) throw new InvalidArgumentException ( "필요한 옵션 없음(통화단위)" );
						}
					}
				}
				else
				{
					throw new InvalidArgumentException ( "통화단위 아님" );
				}
				
				// 3rd argument
				if ( arrTokenType[i] == TokenType.IRCCurrencyUnit )
				{
					if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
						throw new InvalidArgumentException("알 수 없는 통화단위");
					
					result += " " + tokens[i++]; // [Amount] [Currency_unit1] [Currency_unit2]
					if ( i != len ) throw new InvalidArgumentException ( "불 필요한 옵션" ); // [Amount] [Currency_unit1] [Currency_unit2] (???)
				}
				else
				{
					throw new InvalidArgumentException ( "통화단위 아님" ); // [Amount] [Currency_unit1] ![Currency_unit2]
				}
			}
		}
		else if ( arrTokenType[i] == TokenType.IRCCurrencyUnit )
		{
			if ( len == 1 )
			{
				if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
					throw new InvalidArgumentException("알 수 없는 통화단위");
				result += "--show " + tokens[i++] + " cr";
				return result;
			}
			else if ( len == 2 )
			{
				if ( arrTokenType[i+1] == TokenType.IRCFieldAbbr )
				{
					if ( arrTokenSubtype[i+1] == TokenSubtype.Unknown )
						throw new InvalidArgumentException("알 수 없는 필드약자");
					result += "--show " + tokens[i] + " " + tokens[i+1];
					return result;
				}
				else if ( arrTokenType[i+1] == TokenType.IRCCurrencyUnit )
				{
					if ( arrTokenSubtype[i+1] == TokenSubtype.Unknown )
						throw new InvalidArgumentException("알 수 없는 통화단위");
					
					result += "--convert 1 " + tokens[i] + " " + tokens[i+1];
					return result;
				}
			}
		}
		else if ( arrTokenType[i] == TokenType.Amount )
		{
			// cmd and 1st argument
			result += "--convert " + tokens[i++]; // --convert [Amount]
			if ( i == len )
			{
				result += " KRW USD"; // --convert [Amount] KRW USD
				return result;
			}
			
			// 2nd argument
			if ( arrTokenType[i] == TokenType.IRCCurrencyUnit )
			{
				if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
					throw new InvalidArgumentException("알 수 없는 통화단위");
				
				result += " " + tokens[i++]; // --convert [Amount] [Currency_unit1]
				
				if ( i == len )
				{
					result += " USD"; // --convert [Amount] [Currency_unit1] USD
					return result;
				}
			}
			else
			{
				throw new InvalidArgumentException("통화단위 아님");
			}
			
			// 3rd argument
			if ( arrTokenType[i] == TokenType.IRCCurrencyUnit )
			{
				if ( arrTokenSubtype[i] == TokenSubtype.Unknown )
					throw new InvalidArgumentException("알 수 없는 통화단위");
				
				result += " " + tokens[i++]; // --convert [Amount] [Currency_unit1] [Currency_unit2]
				
				if ( i != len ) throw new InvalidArgumentException ( "불 필요한 옵션" ); // --convert [Amount] [Currency_unit1] [Currency_unit2] (???)
			}
			else
			{
				throw new InvalidArgumentException("통화단위 아님");
			}
		}
		else
		{
			throw new InvalidArgumentException ( "불 필요한 옵션" ); // maybe ... [FieldAbbr]
		}

		return result;
	}
}

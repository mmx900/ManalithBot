/*
 	org.manalith.ircbot.plugin.curex/CurexMessageTokenAnalyzer.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011  Seong-ho, Cho <darkcircle.0426@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.manalith.ircbot.plugin.curex;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.manalith.ircbot.plugin.curex.exceptions.InvalidArgumentException;

public class CurexMessageTokenAnalyzer {

	private static TokenType getTokenType(String tokenstring) {
		TokenType result = TokenType.Unknown;
		Pattern cmd_pattern = Pattern.compile("[a-z]{4,9}");
		Pattern curname_pattern = Pattern.compile("[A-Z]{3}");
		Pattern field_pattern = Pattern.compile("[가-힣]{4}");
		Pattern amount_pattern = Pattern
				.compile("[0-9]{1,3}((\\,)?[0-9]{3})*(.[0-9]{1,2})?");

		Matcher cmd_match = cmd_pattern.matcher(tokenstring);
		Matcher curname_match = curname_pattern.matcher(tokenstring);
		Matcher field_match = field_pattern.matcher(tokenstring);
		Matcher amount_match = amount_pattern.matcher(tokenstring);

		if (cmd_match.matches()) {
			result = TokenType.IRCOption;
		} else if (curname_match.matches()) {
			result = TokenType.IRCCurrencyUnit;
		} else if (field_match.matches()) {
			result = TokenType.IRCFieldAbbr;
		} else if (amount_match.matches()) {
			result = TokenType.Amount;
		}

		return result;
	}

	private static TokenSubtype getTokenSubtype(String tokenstring,
			TokenType currentTokenType) {
		TokenSubtype result = TokenSubtype.Unknown;

		if (currentTokenType == TokenType.IRCOption) {
			if (tokenstring.equals("show")) {
				result = TokenSubtype.CommandShow;
			} else if (tokenstring.equals("conv")) {
				result = TokenSubtype.CommandConvert;
			} else if (tokenstring.equals("buycash")) {
				result = TokenSubtype.CommandBuyCash;
			} else if (tokenstring.equals("cellcash")) {
				result = TokenSubtype.CommandCellCash;
			} else if (tokenstring.equals("sendremit")) {
				result = TokenSubtype.CommandSendRemit;
			} else if (tokenstring.equals("recvremit")) {
				result = TokenSubtype.CommandRecvRemit;
			} else if (tokenstring.equals("lastround")) {
				result = TokenSubtype.CommandLastRound;
			} else if (tokenstring.equals("help")) {
				result = TokenSubtype.CommandHelp;
			} else if (tokenstring.equals("unitlist")) {
				result = TokenSubtype.CommandUnitList;
			}
		} else if (currentTokenType == TokenType.IRCCurrencyUnit) {
			try {
				result = TokenSubtype.valueOf("Currency" + tokenstring);
			} catch (IllegalArgumentException e) {
				result = TokenSubtype.Unknown;
			}
		} else if (currentTokenType == TokenType.IRCFieldAbbr) {
			if (tokenstring.equals("매매기준")) {
				result = TokenSubtype.FACentralRate;
			} else if (tokenstring.equals("현찰매수")) {
				result = TokenSubtype.FABuyCash;
			} else if (tokenstring.equals("현찰매도")) {
				result = TokenSubtype.FACellCash;
			} else if (tokenstring.equals("송금보냄")) {
				result = TokenSubtype.FASendRemit;
			} else if (tokenstring.equals("송금받음")) {
				result = TokenSubtype.FARecvRemit;
			} else if (tokenstring.equals("환수수료")) {
				result = TokenSubtype.FAECRate;
			} else if (tokenstring.equals("대미환율")) {
				result = TokenSubtype.FAECRate;
			} else if (tokenstring.equals("모두보기")) {
				result = TokenSubtype.FAAll;
			}
		} else if (currentTokenType == TokenType.Amount) {
			Pattern natural_pattern = Pattern
					.compile("[0-9]{1,3}((\\,)?[0-9]{3})*");
			Pattern fp_pattern = Pattern
					.compile("[0-9]{1,3}((\\,)?[0-9]{3})*\\.[0-9]{1,2}");

			Matcher natural_match = natural_pattern.matcher(tokenstring);
			Matcher fp_match = fp_pattern.matcher(tokenstring);

			if (natural_match.matches())
				result = TokenSubtype.AmountNatural;
			else if (fp_match.matches())
				result = TokenSubtype.AmountFp;
		}

		return result;
	}

	public static String[] convertToCLICommandString(String[] args)
			throws InvalidArgumentException {
		String[] result = null;

		for (int i = 0; i < args.length; i++) {
			if (getTokenType(args[i]) == TokenType.IRCOption
					&& getTokenSubtype(args[i], TokenType.IRCOption) != TokenSubtype.Unknown) {
				args[i] = "-" + args[i];
			} else if (getTokenType(args[i]) == TokenType.IRCFieldAbbr) {
				if (getTokenSubtype(args[i], TokenType.IRCFieldAbbr) == TokenSubtype.FACentralRate)
					args[i] = "cr";
				else if (getTokenSubtype(args[i], TokenType.IRCFieldAbbr) == TokenSubtype.FABuyCash)
					args[i] = "cb";
				else if (getTokenSubtype(args[i], TokenType.IRCFieldAbbr) == TokenSubtype.FACellCash)
					args[i] = "cc";
				else if (getTokenSubtype(args[i], TokenType.IRCFieldAbbr) == TokenSubtype.FASendRemit)
					args[i] = "rs";
				else if (getTokenSubtype(args[i], TokenType.IRCFieldAbbr) == TokenSubtype.FARecvRemit)
					args[i] = "rr";
				else if (getTokenSubtype(args[i], TokenType.IRCFieldAbbr) == TokenSubtype.FAECRate)
					args[i] = "ec";
				else if (getTokenSubtype(args[i], TokenType.IRCFieldAbbr) == TokenSubtype.FADollarExcRate)
					args[i] = "de";
				else if (getTokenSubtype(args[i], TokenType.IRCFieldAbbr) == TokenSubtype.FAAll)
					args[i] = "all";
			} else if ((getTokenType(args[i]) == TokenType.IRCCurrencyUnit || getTokenType(args[i]) == TokenType.Amount)
					&& (getTokenSubtype(args[i], TokenType.IRCCurrencyUnit) != TokenSubtype.Unknown || getTokenSubtype(
							args[i], TokenType.Amount) != TokenSubtype.Unknown)) {
				;
			} else {
				throw new InvalidArgumentException(args[i] + " => "
						+ TokenType.Unknown.toString());
			}
		}

		result = new String[args.length];
		System.arraycopy(args, 0, result, 0, args.length);

		return result;
	}
}

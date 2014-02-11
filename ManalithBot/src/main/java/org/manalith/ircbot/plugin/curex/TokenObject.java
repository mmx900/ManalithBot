package org.manalith.ircbot.plugin.curex;

import java.util.regex.Pattern;

public class TokenObject {

	private TokenType tokenType;
	private TokenSubtype tokenSType;
	private String tokenString;
	private int originalTokenPosition;

	public TokenObject(String tString) {
		tokenType = getInternalTokenType(tString);
		if (tokenType.equals(TokenType.Unit))
			tokenString = tString.toUpperCase();
		else if (tokenType.equals(TokenType.Command))
			tokenString = tString.toLowerCase();
		else
			tokenString = tString;
		tokenSType = getInternalTokenSubtype(tString, tokenType);
	}

	private TokenType getInternalTokenType(String ts) {
		TokenType result = TokenType.UnDefined;

		if (Pattern.matches("[a-zA-Z]{4,8}", ts))
			result = TokenType.Command;
		else if (Pattern.matches("[0-9]+(\\.[0-9]+)?", ts))
			result = TokenType.Number;
		else if (Pattern.matches("[a-zA-Z]{3}", ts))
			result = TokenType.Unit;

		return result;
	}

	private TokenSubtype getInternalTokenSubtype(String ts, TokenType tt) {
		TokenSubtype result = TokenSubtype.UnDefined;

		switch (tt.value()) {
		case 0:
			switch(ts)
			{
				case "help":
					result = TokenSubtype.CommandHelp;
					break;
				case "show":
					result = TokenSubtype.CommandShow;
					break;
				case "unitlist":
					result = TokenSubtype.CommandUnitList;
					break;
				case "calc":
					result = TokenSubtype.CommandCalc;
					break;
			}
			break;
		case 1:
			result = TokenSubtype.CurrencyUnit;
			break;
		case 2:
			if (Pattern.matches("[0-9]+", ts))
				result = TokenSubtype.NumberNatural;
			else if (Pattern.matches("0-9]+\\.[0-9]+", ts))
				result = TokenSubtype.NumberFloatingPoint;
			break;
		default:
			break;
		}

		return result;
	}

	public String getTokenString() {
		return tokenString;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public TokenSubtype getTokenSubtype() {
		return tokenSType;
	}

	public void setOriginalTokenPosition(int pos) {
		originalTokenPosition = pos;
	}

	public int getOriginalTokenPosition() {
		return originalTokenPosition;
	}

	public enum TokenType {

		Command(0), Unit(1), Number(2), UnDefined(3);

		private final int val;

		TokenType(int init_val) {
			val = init_val;
		}

		public int value() {
			return val;
		}
	}

	public enum TokenSubtype {

		CommandShow(0), CommandHelp(1), CommandCalc(2), CommandUnitList(3), NumberNatural(
				4), NumberFloatingPoint(5), CurrencyUnit(6), UnDefined(7);

		private final int value;

		TokenSubtype(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}
}

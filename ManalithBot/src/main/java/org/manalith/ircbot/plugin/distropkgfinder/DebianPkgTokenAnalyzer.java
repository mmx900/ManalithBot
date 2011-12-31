package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.TokenArray;
import org.manalith.ircbot.plugin.distropkgfinder.TokenSubtype;
import org.manalith.ircbot.plugin.distropkgfinder.TokenType;
import org.manalith.ircbot.plugin.distropkgfinder.TokenUnit;
import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DebianPkgTokenAnalyzer extends TokenAnalyzer {

	public DebianPkgTokenAnalyzer() {
		super();
	}

	public DebianPkgTokenAnalyzer(String newData) {
		this.data = newData;
	}

	@Override
	public TokenType getTokenType(String TokenString) {
		TokenType result = TokenType.Unknown;

		Pattern div_pattern = Pattern
				.compile("\\<[\\/]?div((\\s)[\\S]+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern h2_pattern = Pattern.compile("\\<[\\/]?h2\\>");
		Pattern h3_pattern = Pattern.compile("\\<[\\/]?h3\\>");
		Pattern br_pattern = Pattern.compile("\\<br[\\/]?\\>");
		Pattern ul_pattern = Pattern.compile("\\<[\\/]?ul\\>");
		Pattern li_pattern = Pattern
				.compile("\\<[\\/]?li((\\s)[\\S]+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern a_pattern = Pattern
				.compile("\\<[\\/]?a((\\s)[\\S]+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern p_pattern = Pattern
				.compile("\\<[\\/]?p((\\s)[\\S]+\\=\\\"(\\s|\\S)+\\\")*\\>");

		Matcher div_matcher = div_pattern.matcher(TokenString);
		Matcher h2_matcher = h2_pattern.matcher(TokenString);
		Matcher h3_matcher = h3_pattern.matcher(TokenString);
		Matcher br_matcher = br_pattern.matcher(TokenString);
		Matcher ul_matcher = ul_pattern.matcher(TokenString);
		Matcher li_matcher = li_pattern.matcher(TokenString);
		Matcher a_matcher = a_pattern.matcher(TokenString);
		Matcher p_matcher = p_pattern.matcher(TokenString);

		if (div_matcher.matches())
			result = TokenType.Div;
		else if (h2_matcher.matches())
			result = TokenType.H2;
		else if (h3_matcher.matches())
			result = TokenType.H3;
		else if (br_matcher.matches())
			result = TokenType.Br;
		else if (ul_matcher.matches())
			result = TokenType.Ul;
		else if (li_matcher.matches())
			result = TokenType.Li;
		else if (a_matcher.matches())
			result = TokenType.A;
		else if (p_matcher.matches())
			result = TokenType.P;

		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		TokenSubtype result = TokenSubtype.Unknown;

		int hashCode = CurrentType.hashCode();
		if (hashCode == TokenType.Div.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.DivClose;
			else
				result = TokenSubtype.DivOpen;
		} else if (hashCode == TokenType.H2.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.H2Close;
			else
				result = TokenSubtype.H2Open;
		} else if (hashCode == TokenType.H3.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.H3Close;
			else
				result = TokenSubtype.H3Open;
		} else if (hashCode == TokenType.Br.hashCode())
			result = TokenSubtype.Br;
		else if (hashCode == TokenType.Ul.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.UlClose;
			else
				result = TokenSubtype.UlOpen;
		} else if (hashCode == TokenType.Li.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.LiClose;
			else
				result = TokenSubtype.LiOpen;
		} else if (hashCode == TokenType.A.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.AClose;
			else
				result = TokenSubtype.AOpen;
		} else if (hashCode == TokenType.P.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.PClose;
			else
				result = TokenSubtype.POpen;
		}

		return result;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		TokenArray result = new TokenArray();
		TokenType currentTokenType = TokenType.Unknown;
		TokenSubtype currentTokenSubtype = TokenSubtype.Unknown;
		boolean inBoundOfResult = false;
		int skipDivCount = 0;

		int len = this.data.length();
		if (len == 0)
			throw new EmptyTokenStreamException();

		int i = 0;

		String tokenString = "";
		String tempchar = "";

		while (i < len) {
			tempchar = this.data.substring(i, i + 1);
			i++;

			if ((tempchar.equals("\t") || tempchar.equals("\n"))
					|| (tempchar.equals("\r") || tempchar.equals(" "))) {
				continue;
			}

			if (tokenString.equals("") && !tempchar.equals("<")) {
				tokenString = tempchar;
				tempchar = this.data.substring(i, i + 1);
				i++;

				while (!tempchar.equals("<")) {
					if (tempchar.equals("\t")
							|| (tempchar.equals("\r") || tempchar.equals("\n"))) {
						tempchar = this.data.substring(i, i + 1);
						i++;
						continue;
					}
					tokenString += tempchar;
					tempchar = this.data.substring(i, i + 1);
					i++;
				}

				currentTokenType = TokenType.TextString;
				currentTokenSubtype = TokenSubtype.TextString;

				if (inBoundOfResult) {
					result.addElement(tokenString, currentTokenType,
							currentTokenSubtype);
					tokenString = "";
					currentTokenType = TokenType.Unknown;
					currentTokenSubtype = TokenSubtype.Unknown;
				}

			}

			// get a piece of the tag
			if (tempchar.equals("<")) {
				tokenString = tempchar;
				tempchar = this.data.substring(i, i + 1);
				i++;

				while (!tempchar.equals(">")) {
					tokenString += tempchar;
					tempchar = this.data.substring(i, i + 1);
					i++;
				}

				// I need to check this point;
				tokenString += tempchar;
			} // OK!

			currentTokenType = this.getTokenType(tokenString);
			if (currentTokenType == TokenType.Unknown) {
				tokenString = "";
				continue;
			} else {

				currentTokenSubtype = this.getTokenSubtype(tokenString,
						currentTokenType);

				if (currentTokenSubtype == TokenSubtype.DivOpen) {
					String[] tagnattr = tokenString.split("\\s");
					String[] keyval = tagnattr[1].split("\\=");
					String value = keyval[1].substring(1,
							keyval[1].length() - 2);
					if (value.equals("psearchres")) {
						inBoundOfResult = true;
					} else if (value.equals("note")) {
						skipDivCount++;
					}
				} else if (currentTokenSubtype == TokenSubtype.DivClose
						&& inBoundOfResult) {
					if (skipDivCount != 0) {
						skipDivCount--;
					} else {
						TokenUnit newTokenUnit = new TokenUnit(tokenString,
								currentTokenType, currentTokenSubtype);
						result.addElement(newTokenUnit);
						break;
					}
				}

				TokenUnit newTokenUnit = new TokenUnit(tokenString,
						currentTokenType, currentTokenSubtype);
				if (inBoundOfResult)
					result.addElement(newTokenUnit);

				tokenString = "";
				currentTokenType = TokenType.Unknown;
				currentTokenSubtype = TokenSubtype.Unknown;

			}
		}

		return result;
	}

}

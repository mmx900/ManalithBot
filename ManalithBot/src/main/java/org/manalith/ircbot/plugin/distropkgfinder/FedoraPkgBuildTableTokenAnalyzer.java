package org.manalith.ircbot.plugin.distropkgfinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class FedoraPkgBuildTableTokenAnalyzer extends TokenAnalyzer {
	public FedoraPkgBuildTableTokenAnalyzer() {
		super();
	}

	public FedoraPkgBuildTableTokenAnalyzer(String newData) {
		this.data = newData;
	}

	@Override
	public TokenType getTokenType(String TokenString) {
		TokenType result = TokenType.Unknown;

		Pattern table_pattern = Pattern
				.compile("\\<[\\/]?table((\\s)(\\S)+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern img_pattern = Pattern
				.compile("\\<img((\\s)(\\S)\\=\\\"(\\s|\\S)+\\\")*\\/\\>");
		Pattern th_pattern = Pattern
				.compile("\\<[\\/]?th((\\s)(\\S)+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern tr_pattern = Pattern
				.compile("\\<[\\/]?tr((\\s)(\\S)+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern td_pattern = Pattern
				.compile("\\<[\\/]?td((\\s)(\\S)+\\=\\\"(\\s|\\S)+\\\")*\\>");
		Pattern a_pattern = Pattern
				.compile("\\<[\\/]?a((\\s)(\\S+)\\=\\\"(\\s|\\S)+\\\")*\\>");

		Matcher table_matcher = table_pattern.matcher(TokenString);
		Matcher img_matcher = img_pattern.matcher(TokenString);
		Matcher th_matcher = th_pattern.matcher(TokenString);
		Matcher tr_matcher = tr_pattern.matcher(TokenString);
		Matcher td_matcher = td_pattern.matcher(TokenString);
		Matcher a_matcher = a_pattern.matcher(TokenString);

		if (table_matcher.matches())
			result = TokenType.Table;
		else if (img_matcher.matches())
			result = TokenType.Img;
		else if (th_matcher.matches())
			result = TokenType.Th;
		else if (tr_matcher.matches())
			result = TokenType.Tr;
		else if (td_matcher.matches())
			result = TokenType.Td;
		else if (a_matcher.matches())
			result = TokenType.A;

		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		TokenSubtype result = TokenSubtype.Unknown;
		int hashCode = CurrentType.hashCode();

		if (hashCode == TokenType.Table.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.TableClose;
			else
				result = TokenSubtype.TableOpen;
		} else if (hashCode == TokenType.Img.hashCode()) {
			result = TokenSubtype.Img;
		} else if (hashCode == TokenType.Th.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.ThClose;
			else
				result = TokenSubtype.ThOpen;
		} else if (hashCode == TokenType.Tr.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.TrClose;
			else
				result = TokenSubtype.TrOpen;
		} else if (hashCode == TokenType.Td.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.TdClose;
			else
				result = TokenSubtype.TdOpen;
		} else if (hashCode == TokenType.A.hashCode()) {
			if (TokenString.charAt(1) == '/')
				result = TokenSubtype.AClose;
			else
				result = TokenSubtype.AOpen;
		}

		return result;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		TokenArray result = new TokenArray();
		TokenType currentTokenType = TokenType.Unknown;
		TokenSubtype currentTokenSubtype = TokenSubtype.Unknown;
		boolean inBoundOfResult = false;

		int len = this.data.length();
		if (len == 0)
			throw new EmptyTokenStreamException();

		int i = 0;
		int rowcnt = 0;

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

				if (currentTokenSubtype == TokenSubtype.TrOpen) {
					String[] tagnattr = tokenString.substring(1,
							tokenString.length() - 1).split("\\s");
					if (tagnattr.length != 1) {
						for (int l = 1; l < tagnattr.length; l++) {
							if (tagnattr[l].contains("row-odd")
									|| tagnattr[l].contains("row-even")) {
								inBoundOfResult = true;
								break;
							}
						}
					}
				} else if (currentTokenSubtype == TokenSubtype.TrClose
						&& inBoundOfResult) {
					TokenUnit newTokenUnit = new TokenUnit(tokenString,
							currentTokenType, currentTokenSubtype);
					result.addElement(newTokenUnit);
					inBoundOfResult = false;
					rowcnt++;
					if (rowcnt == 3)
						break;
					else
						continue;
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

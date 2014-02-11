/*
 	org.manalith.ircbot.plugin.calc/CalcTokenAnalyzer.java
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
package org.manalith.ircbot.plugin.calc;

import java.util.regex.Pattern;

import org.manalith.ircbot.plugin.calc.TokenUnit.TokenSubtype;
import org.manalith.ircbot.plugin.calc.TokenUnit.TokenType;
import org.manalith.ircbot.plugin.calc.exceptions.EmptyTokenStreamException;
import org.manalith.ircbot.plugin.calc.exceptions.TokenAnalysisException;

public class CalcTokenAnalyzer {

	private String tokenStream;

	public CalcTokenAnalyzer(String mathExpr) {
		tokenStream = mathExpr;
	}

	public TokenType getTokenType(String tokenString) {
		TokenType type = TokenType.Unknown;

		// Regex patterns for recognizing integer
		if (Pattern.matches("(0|(0|1)+)(B|b)", tokenString)
				|| Pattern.matches("0[1-7][0-7]*", tokenString)
				|| Pattern.matches("(0|-?[1-9][0-9]*)", tokenString)
				|| Pattern.matches("0x([0-9a-fA-F]*)", tokenString))
			type = TokenType.Integer;
		// Regex pattern for recognizing floating point
		else if (Pattern
				.matches("(-?[0-9]+)(\\.[0-9]+)?(([Ee](-?[1-9][0-9]*))|f)?",
						tokenString))
			type = TokenType.FlPoint;
		// Regex pattern for recognizing operator
		else if (Pattern.matches("\\+|\\-|\\*|\\/|\\%|\\^|\\!", tokenString))
			type = TokenType.Operatr;
		// Regex patterns for recognizing parenthesis
		// to change priority of calculation
		else if (Pattern.matches("\\(", tokenString)
				|| Pattern.matches("\\)", tokenString))
			type = TokenType.Parents;
		// Regex patterns for recognizing parentheses
		// to preprocess for the function
		else if (Pattern.matches("sin|cos|tan|arcsin|arccos|arctan",
				tokenString))
			type = TokenType.TriangleFunc;
		else if (Pattern.matches("to(bin|oct|dec|hex)", tokenString))
			type = TokenType.BaseConvFunc;
		else if (Pattern.matches("sqrt", tokenString))
			type = TokenType.MathematFunc;
		return type;
	}

	public TokenSubtype getTokenSubtype(String tokenString, TokenType type) {
		TokenSubtype result = TokenSubtype.Unknown;

		switch (type.value()) {
		case 1: // Integer
			if (Pattern.matches("(0|(0|1)+)(B|b)", tokenString))
				result = TokenSubtype.Binary;
			else if (Pattern.matches("0[1-7][0-7]*", tokenString))
				result = TokenSubtype.Octal;
			else if (Pattern.matches("(0|-?[1-9][0-9]*)", tokenString))
				result = TokenSubtype.Decimal;
			else if (Pattern.matches("0x([0-9a-fA-F]*)", tokenString))
				result = TokenSubtype.Hexadec;
			break;
		case 2: // FlPoint
			if (Pattern.matches("(-?[0-9]+)(\\.[0-9]+)?f", tokenString))
				result = TokenSubtype.SpFltPoint;
			else if (Pattern.matches("(-?[0-9]+)(\\.[0-9]+)", tokenString))
				result = TokenSubtype.DpFltPoint;
			else if (Pattern.matches(
					"(-?[0-9]+)(\\.[0-9]+)?([Ee](-?[1-9][0-9]*))", tokenString))
				result = TokenSubtype.ExpFltPoint;
			break;
		case 3: // Operator
			if (tokenString.equals("+"))
				result = TokenSubtype.Plus;
			else if (tokenString.equals("-"))
				result = TokenSubtype.Minus;
			else if (tokenString.equals("*"))
				result = TokenSubtype.Times;
			else if (tokenString.equals("/"))
				result = TokenSubtype.Divide;
			else if (tokenString.equals("%"))
				result = TokenSubtype.Modulus;
			else if (tokenString.equals("^"))
				result = TokenSubtype.Power;
			else if (tokenString.equals("!"))
				result = TokenSubtype.Factorial;
			break;
		case 4: // Parenthesis
			if (tokenString.equals("("))
				result = TokenSubtype.Left_Parenthesis;
			else if (tokenString.equals(")"))
				result = TokenSubtype.Righ_Parenthesis;
			break;
		case 5: // Triangular function
			if (tokenString.equals("sin"))
				result = TokenSubtype.Sine;
			else if (tokenString.equals("cos"))
				result = TokenSubtype.Cosine;
			else if (tokenString.equals("tan"))
				result = TokenSubtype.Tangent;
			else if (tokenString.equals("arcsin"))
				result = TokenSubtype.ArcSine;
			else if (tokenString.equals("arccos"))
				result = TokenSubtype.ArcCosine;
			else if (tokenString.equals("arctan"))
				result = TokenSubtype.ArcTangent;
			break;
		case 6: // Base conversion
			if (tokenString.equals("tobin"))
				result = TokenSubtype.ToBin;
			else if (tokenString.equals("tooct"))
				result = TokenSubtype.ToOct;
			else if (tokenString.equals("todec"))
				result = TokenSubtype.ToDec;
			else if (tokenString.equals("tohex"))
				result = TokenSubtype.ToHex;
			break;
		case 7: // Mathematical function
			if (tokenString.equals("sqrt"))
				result = TokenSubtype.Sqrt;
		}
		return result;
	}

	public TokenArray getTokenArray() throws TokenAnalysisException,
			EmptyTokenStreamException {
		TokenArray result = new TokenArray();

		int stringLength = tokenStream.length();

		if (stringLength == 0)
			throw new EmptyTokenStreamException();

		String temp = "";

		TokenType currentType = TokenType.Unknown;
		TokenType checkedType = TokenType.Unknown; // init.

		for (int i = 0; i < stringLength; i++) {
			// x"."
			if (tokenStream.charAt(i) == '.') {
				checkedType = TokenType.FlPoint;
				currentType = TokenType.FlPoint;
				temp = temp.concat(tokenStream.substring(i, i + 1));
				continue;
			}

			// 0.001 "e" or 0xnnnne
			if ((tokenStream.charAt(i) == 'e' || tokenStream.charAt(i) == 'E')) {
				if (getTokenSubtype(temp, currentType) == TokenSubtype.Decimal) {
					currentType = TokenType.FlPoint;

					temp = temp.concat(tokenStream.substring(i, i + 1));
					continue;
				} else {
					// hexa decimal, ExpFltPoint or function
					temp = temp.concat(tokenStream.substring(i, i + 1));
					continue;
				}
			}

			// " 0x "
			if ((tokenStream.charAt(i) == 'x')
					&& currentType == TokenType.Integer) {
				temp = temp.concat(tokenStream.substring(i, i + 1));
				continue;
			}

			if (tokenStream.charAt(i) == '-') {
				if (temp.length() == 0 && i == 0) {
					temp = temp.concat(tokenStream.substring(i, i + 1));
					currentType = getTokenType(temp);
					checkedType = currentType;

					continue;
				}
				if (temp.length() == 0
						&& result.getToken(result.getSize() - 1).getTokenType() != TokenType.Operatr) {
					// operator
					temp = temp.concat(tokenStream.substring(i, i + 1));
					currentType = getTokenType(temp);
					TokenSubtype tsType = getTokenSubtype(temp, currentType);
					TokenUnit newUnit = new TokenUnit(currentType, tsType, temp);
					result.addToken(newUnit);

					currentType = TokenType.Unknown;
					temp = "";

					continue;
				} else if (currentType == TokenType.FlPoint
						&& (temp.charAt(temp.length() - 1) == 'e' || temp
								.charAt(temp.length() - 1) == 'E')) {
					// unary mark for exponential
					temp = temp.concat(tokenStream.substring(i, i + 1));
					continue;
				}
			}

			if (tokenStream.charAt(i) == ' ') {
				// add token if current point meets separator(white space)
				currentType = getTokenType(temp);
				TokenSubtype tsType = getTokenSubtype(temp, currentType);
				TokenUnit newUnit = new TokenUnit(currentType, tsType, temp);
				result.addToken(newUnit);

				// reset
				temp = "";
				checkedType = TokenType.Unknown;
				currentType = TokenType.Unknown;

				// and ignore white space
				continue;
			}

			temp = temp.concat(tokenStream.substring(i, i + 1));

			if (i == 0) {
				// unary operator
				if (temp.equals("-")) {
					continue;
				}

				currentType = getTokenType(temp);
				checkedType = currentType;
			} else if (i == 1) {

				checkedType = getTokenType(temp);

				if (currentType != checkedType) {
					if (checkedType == TokenType.Unknown) {
						temp = temp.substring(0, temp.length() - 1);

						// if selected string is empty.
						if (temp.equals(""))
							throw new TokenAnalysisException();

						TokenSubtype tsType = getTokenSubtype(temp, currentType);

						// if selected string is unknown type of token.
						if (currentType == TokenType.Unknown
								&& tsType == TokenSubtype.Unknown)
							throw new TokenAnalysisException();

						TokenUnit newUnit = new TokenUnit(currentType, tsType,
								temp);
						result.addToken(newUnit);

						currentType = TokenType.Unknown;
						temp = "";

						i--;
					} else {
						currentType = checkedType;
					}
				}
			} else {
				checkedType = getTokenType(temp);

				if (currentType != checkedType) {
					if (checkedType == TokenType.Unknown) {
						temp = temp.substring(0, temp.length() - 1);

						// if selected string is empty
						if (temp.equals(""))
							throw new TokenAnalysisException();

						TokenSubtype tsType = getTokenSubtype(temp, currentType);

						// if selected string is unknown type of token.
						if (currentType == TokenType.Unknown
								&& tsType == TokenSubtype.Unknown)
							throw new TokenAnalysisException();

						TokenUnit newUnit = new TokenUnit(currentType, tsType,
								temp);
						result.addToken(newUnit);

						temp = "";
						currentType = TokenType.Unknown;
						i--;
					} else {
						currentType = checkedType;
					}
				}
			}
		}

		TokenSubtype tsType = getTokenSubtype(temp, currentType);

		// if selected string is unknown type of token.
		if (currentType == TokenType.Unknown && tsType == TokenSubtype.Unknown)
			throw new TokenAnalysisException();

		TokenUnit newUnit = new TokenUnit(currentType, tsType, temp);
		result.addToken(newUnit);

		return result;
	}

}

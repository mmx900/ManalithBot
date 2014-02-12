/*
 	org.manalith.ircbot.plugin.calc/TokenUnit.java
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

public class TokenUnit {
	private TokenType type;
	private TokenSubtype subtype;
	private String token;

	public enum TokenType {
		Unknown(0), Integer(1), FlPoint(2), Operatr(3), Parents(4), TriangleFunc(
				5), BaseConvFunc(6), MathematFunc(7);

		private final int value;

		TokenType(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}

	public enum TokenSubtype {
		Unknown(0), Binary(1), Octal(2), Decimal(3), Hexadec(4), SpFltPoint(5), DpFltPoint(
				6), ExpFltPoint(7), Plus(8), Minus(9), Times(10), Divide(11), Modulus(
				12), Power(13), Factorial(14), Left_Parenthesis(15), Righ_Parenthesis(
				16), Sine(17), Cosine(18), Tangent(19), ArcSine(20), ArcCosine(
				21), ArcTangent(22), ToBin(23), ToOct(24), ToDec(25), ToHex(26), Sqrt(
				27);

		private int value;

		TokenSubtype(int val) {
			value = val;
		}

		public int value() {
			return value;
		}
	}

	public TokenUnit() {
		setTokenType(TokenType.Unknown);
		setTokenSubtype(TokenSubtype.Unknown);
		setTokenString("");
	}

	public TokenUnit(TokenType newTokenType, TokenSubtype newTokenSubtype,
			String newTokenStr) {
		setTokenType(newTokenType);
		setTokenSubtype(newTokenSubtype);
		setTokenString(newTokenStr);
	}

	public void setTokenType(TokenType tokenType) {
		type = tokenType;
	}

	public void setTokenSubtype(TokenSubtype tokenSubtype) {
		subtype = tokenSubtype;
	}

	public void setTokenString(String token) {
		this.token = token;
	}

	public TokenType getTokenType() {
		return type;
	}

	public TokenSubtype getTokenSubtype() {
		return subtype;
	}

	public String getTokenString() {
		return token;
	}

	@Override
	public String toString() {
		return token + " : " + type.toString() + "(" + subtype.toString() + ")";
	}
}
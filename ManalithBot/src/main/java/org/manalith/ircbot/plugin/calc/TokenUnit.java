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
	private TokenType Type;
	private TokenSubtype Subtype;
	private String TokenStr;

	public TokenUnit() {
		setTokenType(TokenType.Unknown);
		setTokenSubtype(TokenSubtype.Unknown);
		setTokenString("");
	}

	public TokenUnit(String newTokenStr) {
		setTokenType(TokenType.Unknown);
		setTokenSubtype(TokenSubtype.Unknown);
		setTokenString(newTokenStr);
	}

	public TokenUnit(TokenType newTokenType, TokenSubtype newTokenSubtype,
			String newTokenStr) {
		setTokenType(newTokenType);
		setTokenSubtype(newTokenSubtype);
		setTokenString(newTokenStr);
	}

	public void setTokenType(TokenType newTokenType) {
		this.Type = newTokenType;
	}

	public void setTokenSubtype(TokenSubtype newTokenSubtype) {
		this.Subtype = newTokenSubtype;
	}

	public void setTokenString(String newTokenStr) {
		this.TokenStr = newTokenStr;
	}

	public TokenType getTokenType() {
		return this.Type;
	}

	public TokenSubtype getTokenSubtype() {
		return this.Subtype;
	}

	public String getTokenString() {
		return this.TokenStr;
	}

	public String toString() {
		return this.TokenStr + " : " + this.Type.toString() + "("
				+ this.Subtype.toString() + ")";
	}
}
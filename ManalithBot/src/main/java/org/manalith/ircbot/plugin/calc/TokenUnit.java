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
		this.type = tokenType;
	}

	public void setTokenSubtype(TokenSubtype tokenSubtype) {
		this.subtype = tokenSubtype;
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

	public String toString() {
		return token + " : " + type.toString() + "(" + subtype.toString() + ")";
	}
}
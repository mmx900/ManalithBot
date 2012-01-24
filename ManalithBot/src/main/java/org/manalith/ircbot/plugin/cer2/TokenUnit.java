/*
 	org.manalith.ircbot.plugin.cer2/TokenUnit.java
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
package org.manalith.ircbot.plugin.cer2;

public class TokenUnit {

	private TokenType tokentype;
	private TokenSubtype tokensubtype;
	private String tokenstring;

	public TokenUnit() {
		this.setTokenString("");
		this.setTokenType(TokenType.Unknown);
		this.setTokenSubtype(TokenSubtype.Unknown);
	}

	public TokenUnit(String newTokenString) {
		this.setTokenString(newTokenString);
		this.setTokenType(TokenType.Unknown);
		this.setTokenSubtype(TokenSubtype.Unknown);
	}

	public TokenUnit(String newTokenString, TokenType newTokenType,
			TokenSubtype newTokenSubtype) {
		this.setTokenString(newTokenString);
		this.setTokenType(newTokenType);
		this.setTokenSubtype(newTokenSubtype);
	}

	public void setTokenString(String newTokenString) {
		this.tokenstring = newTokenString;
	}

	public String getTokenString() {
		return this.tokenstring;
	}

	public void setTokenType(TokenType newTokenType) {
		this.tokentype = newTokenType;
	}

	public TokenType getTokenType() {
		return this.tokentype;
	}

	public void setTokenSubtype(TokenSubtype newTokenSubtype) {
		this.tokensubtype = newTokenSubtype;
	}

	public TokenSubtype getTokenSubtype() {
		return this.tokensubtype;
	}

	public String toString() {
		return this.tokenstring + " : " + tokentype.toString() + "("
				+ tokensubtype.toString() + ")";
	}

}
